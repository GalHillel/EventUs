# EventUs - DevOps

DevOps setup for the EventUs project. The app is a NestJS API with MongoDB and an
Android client. What I added around it: the API runs on Kubernetes, Jenkins deploys it
on every push, logs go to Elasticsearch, and a job watches the error rate and rolls
back a bad deploy on its own.

Kubernetes restarts a pod that crashes. It does nothing about a pod that is up, passes
its health checks and still returns 500 on half the requests. That is what the healer
is for.

## The pieces

- Ansible - installs Docker, K3s, kubectl and Terraform. One playbook.
- K3s - Kubernetes. Traefik ships with it so I used that as the ingress, no Nginx.
- Terraform - everything inside the cluster. Three namespaces: `eventus` for the API and
  Mongo, `observability` for Elasticsearch, Kibana and Filebeat, `platform` for the healer.
- Jenkins - polls the repo every 2 minutes. Checkout, Build, Push, Deploy, Smoke test.
  Image tag is the short git sha plus the build number.
- Filebeat - reads the container logs and sends them to Elasticsearch. No Logstash.
- Healer - CronJob, every minute, asks Elasticsearch for the error rate and rolls back
  if it is too high.

## Files

    backend/event-us/   API and Dockerfile
    frontend/           Android client
    infra/ansible/      host setup
    infra/terraform/    cluster resources
    infra/healer/       the healer
    infra/jenkins/      Jenkins image
    infra/kibana/       dashboard
    eventus.sh          all the commands
    Jenkinsfile         the pipeline

## WSL setup

Two things have to be right before anything works.

`%USERPROFILE%\.wslconfig` on the Windows side:

    [wsl2]
    memory=10GB
    processors=6
    kernelCommandLine=cgroup_no_v1=all systemd.unified_cgroup_hierarchy=1

`/etc/wsl.conf` inside Ubuntu:

    [boot]
    systemd=true

Then `wsl --shutdown` and start it again. `stat -fc %T /sys/fs/cgroup` has to print
`cgroup2fs`.

Keep WSL on NAT, and forward the ports from an admin PowerShell:

    $ip = (wsl hostname -I).Trim().Split(" ")[0]
    foreach ($p in 80,8080,6443) {
      netsh interface portproxy delete v4tov4 listenport=$p listenaddress=0.0.0.0
      netsh interface portproxy add v4tov4 listenport=$p listenaddress=0.0.0.0 connectport=$p connectaddress=$ip
    }

This has to run again after every reboot, the WSL address changes. For Kibana add
`127.0.0.1 kibana.local` to `C:\Windows\System32\drivers\etc\hosts`.

## Running it

    sudo apt-get install -y ansible
    ansible-galaxy collection install ansible.posix

    export DOCKER_USER=<docker hub user>
    docker login -u <docker hub user>
    ./eventus.sh up
    ./eventus.sh jenkins

`up` takes about ten minutes the first time. `jenkins` prints the admin password.

In Jenkins: new Pipeline job, this repo, branch `devops_project`, script path
`Jenkinsfile`, Poll SCM `H/2 * * * *`. Two credentials - `dockerhub` with the registry
username and password, and `kubeconfig-jenkins` as a secret file, which `up` already
wrote to `infra/jenkins/`.

The Kibana dashboard is created once:

    cd infra/kibana && ./setup-dashboard.sh

| command | what it does |
|---|---|
| `up` | playbook, images, terraform, kubeconfig, data view |
| `jenkins` | build and start Jenkins |
| `status` | state of everything on one screen |
| `traffic` | requests until Ctrl+C, green for 2xx and red for 5xx |
| `break` | push a commit that turns on error injection |
| `reset` | revert it and clear the cooldown |

From Windows: API on http://localhost/, Swagger on /docs, Jenkins on :8080, Kibana on
http://kibana.local/ under the "EventUs platform" dashboard.

## The healer

It rolls back when both are true over the last 5 minutes: at least 10 server errors,
and errors are at least 25% of all requests. After that it waits 10 minutes before it
can fire again. If Elasticsearch is unreachable it logs and exits. Thresholds are in
the healer ConfigMap in `infra/terraform/main.tf`.

To watch it work: `./eventus.sh traffic` in one terminal, `./eventus.sh break` in
another, `./eventus.sh status` every minute. Jenkins picks the commit up within two
minutes and deploys it, red starts showing in the traffic window, and about five
minutes later the healer rolls it back and it stops. `rollout history` shows the
revision and the reason. Run `./eventus.sh reset` afterwards.

## Android

The server URL used to be written into the Java. It is a build config field now - debug
points at `http://10.0.2.2/` and release at `http://eventus.local/`, both through the
ingress on port 80 instead of straight to 3000. `usesCleartextTraffic` was replaced with
a network security config for those two hosts. Open `frontend/` in Android Studio and
run the debug build on an emulator, with the port forward up.

## Problems I ran into

- k3s restarted in a loop with exit status 0, which looks like success. The real reason
  was only in `journalctl -u k3s`: the kubelet will not run on cgroup v1. That is the
  `kernelCommandLine` line above.
- apt failed with "Conflicting values set for option Signed-By". Ansible's
  `apt_repository` appends a line every run, so the same repo ended up declared twice
  with different keys. Switched to `copy`, plus a task that clears older files
  declaring the same repos. That is also what made the playbook idempotent.
- The ingress answered 200 from the node and 000 from Windows. WSL was on mirrored
  networking, which mirrors listening sockets but not iptables rules, and a hostPort is
  a DNAT rule. Opening the Hyper-V firewall changed nothing. NAT plus portproxy fixed
  it. I also had an old nginx service holding port 80 inside WSL.
- Filebeat reported "dropped: 49, total: 49". Mapping conflict - the ECS template has
  `service` as an object and the API writes it as a string. Turned
  `setup.template.enabled` off and deleted the data stream and the template.
- `break` did nothing. `CHAOS_ERROR_RATE` was `"0"` in the ConfigMap and the code read
  it with `??`, which only falls back on null or undefined, so `"0"` always won over
  the default. Removed it from the ConfigMap.
- Every log line had the path `/`. Under `forRoutes('*')` Express gives `req.path` as
  `/` for everything. `req.originalUrl` has the real path.
- A health check printed `404000`. The jsonpath returned both the IPv4 and the IPv6
  address, so curl got two URLs and printed two codes. Fixed with `awk '{print $1}'`.
- The Jenkins smoke test hung for twenty minutes. The curl had no timeouts, so it sat
  there while the pod was still starting. Added `--connect-timeout`, `--max-time` and
  `--retry`.
- `./eventus.sh` gave Permission denied. The Windows drive has no exec bit and git had
  the file as 100644. `git update-index --chmod=+x`.
- The first rollback I tried did nothing. `rollout undo` cannot swap the image if both
  revisions point at the same tag, so `:latest` is useless here. `variables.tf` rejects it.
- Terraform and Jenkins kept overwriting each other's image, since Terraform owns the
  deployment and Jenkins owns the tag. `ignore_changes` on the image field and the
  annotations.
