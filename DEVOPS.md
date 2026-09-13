# EventUs - DevOps setup

This is the DevOps part of the EventUs project. The app itself is a NestJS API with
MongoDB and an Android client. What I added around it: the API runs in Kubernetes,
Jenkins builds and deploys it on every push, the logs go to Elasticsearch, and a job
checks the error rate every minute and rolls back a bad deploy by itself.

That last part is the point of the project. Kubernetes restarts a pod that crashes,
but it does nothing about a pod that is up, passes its health checks, and still
returns 500 on half the requests. The healer covers that case.

## What each tool does

- Ansible - installs Docker, K3s, kubectl and Terraform on the machine. One playbook.
- K3s - a light Kubernetes distribution. Runs the containers.
- Terraform - defines everything inside the cluster: namespaces, the API deployment
  and service, ingress, Elasticsearch, Kibana, Filebeat, the healer CronJob, and the
  service account Jenkins uses.
- Docker - builds the API image and pushes it to Docker Hub.
- Jenkins - on every push to the repo: builds the image with a unique tag, pushes it,
  updates the deployment, and runs a smoke test against the new pod.
- Filebeat - runs on the node, reads the container log files and sends them to
  Elasticsearch.
- Elasticsearch and Kibana - store the logs and let me query them.
- Healer - a CronJob that runs every minute. It asks Elasticsearch how many requests
  and how many 500s there were in the last five minutes, and if the ratio is too high
  it runs a rollback on the deployment.

I used Traefik as the ingress because it already ships with K3s, so there was no
reason to install Nginx. Filebeat writes JSON straight into Elasticsearch, so there
is no Logstash either.

## Files

    backend/event-us/       the API and its Dockerfile
    frontend/               the Android client
    infra/ansible/          the playbook that prepares the machine
    infra/terraform/        every resource inside the cluster
    infra/healer/           the healer image and its script
    infra/jenkins/          the Jenkins image
    infra/kibana/           the dashboard definition
    eventus.sh              wrapper around all the commands
    Jenkinsfile             the pipeline

## Before the first run

Everything runs inside WSL2 on Ubuntu. Two settings have to be right before anything
else works, and neither one is the default.

K3s needs cgroup v2. On cgroup v1 the kubelet refuses to start, and k3s just restarts
in a loop with exit status 0. Put this in `%USERPROFILE%\.wslconfig` on the Windows side:

    [wsl2]
    memory=10GB
    processors=6
    kernelCommandLine=cgroup_no_v1=all systemd.unified_cgroup_hierarchy=1

and this in `/etc/wsl.conf` inside Ubuntu:

    [boot]
    systemd=true

Then `wsl --shutdown` and start it again. `stat -fc %T /sys/fs/cgroup` has to print
`cgroup2fs`.

WSL networking has to stay on NAT, and the ports have to be forwarded from Windows.
Run this in PowerShell as administrator:

    $ip = (wsl hostname -I).Trim().Split(" ")[0]
    foreach ($p in 80,8080,6443) {
      netsh interface portproxy delete v4tov4 listenport=$p listenaddress=0.0.0.0
      netsh interface portproxy add v4tov4 listenport=$p listenaddress=0.0.0.0 connectport=$p connectaddress=$ip
    }

The WSL address changes every time it restarts, so this has to run again after every
reboot. For Kibana, add `127.0.0.1 kibana.local` to
`C:\Windows\System32\drivers\etc\hosts`.

Ansible is the only thing that has to be installed by hand. It installs the rest.

    sudo apt-get install -y ansible
    ansible-galaxy collection install ansible.posix

## Running it

    export DOCKER_USER=<docker hub user>
    docker login -u <docker hub user>
    ./eventus.sh up
    ./eventus.sh jenkins

`up` takes about ten minutes the first time. It runs the playbook, builds and pushes
the two images, applies Terraform, writes the kubeconfig for Jenkins and creates the
Kibana data view.

`jenkins` builds the Jenkins image, starts the container and prints the admin password.
In Jenkins: create a Pipeline job, point it at this repository and the `devops_project`
branch, set the script path to `Jenkinsfile`, and enable "Poll SCM" with `H/2 * * * *`.
It needs two credentials - `dockerhub` for the registry and `kubeconfig-jenkins` as a
secret file, which `./eventus.sh up` already wrote to `infra/jenkins/`.

Commands:

| command | what it does |
|---|---|
| `up` | playbook, images, terraform, kubeconfig, data view |
| `jenkins` | build and start Jenkins, print the admin password |
| `status` | one screen with the state of everything |
| `traffic` | send requests until Ctrl+C, green for 2xx and red for 5xx |
| `break` | push a commit that turns on error injection |
| `reset` | revert that commit and clear the healer cooldown |

Where things are, from Windows, once the port forward is running:

| url | what |
|---|---|
| http://localhost/ | the API |
| http://localhost/docs | Swagger |
| http://localhost:8080 | Jenkins |
| http://kibana.local/ | Kibana |

## How the healer decides

It rolls back only when both of these are true over the last five minutes: at least 10
server errors, and server errors are at least 25% of all requests. After a rollback it
waits ten minutes before it is allowed to fire again, so it does not roll back twice
while the first one is still settling. If Elasticsearch is not reachable it writes a
log line and exits without touching anything.

The thresholds are in the healer ConfigMap in `infra/terraform/main.tf`.

To see it work end to end: run `./eventus.sh traffic` in one terminal, `./eventus.sh
break` in another, and `./eventus.sh status` every minute or so. Jenkins picks the
commit up within two minutes, builds and deploys it, the red responses start showing
up in the traffic window, and about five minutes later the healer rolls the deployment
back and they stop. `rollout history` shows the new revision with the reason in
CHANGE-CAUSE. Run `./eventus.sh reset` afterwards.

## Things that went wrong

Most of the time on this project went into these, so they are worth writing down.

**k3s would not start.** `systemctl status k3s` showed it restarting over and over
with exit status 0, which looks like success. The real message was only in
`journalctl -u k3s`: the kubelet will not run on a host using cgroup v1. That is the
`kernelCommandLine` setting above.

**apt failed with "Conflicting values set for option Signed-By".** The Ansible
`apt_repository` module appends a line every time it runs, so after two runs the same
repository was declared twice with different keys. I replaced it with `copy`, which
writes the file to a fixed content instead of adding to it, and added a task that
removes older source files declaring the same repositories. That also made the
playbook idempotent, which was the actual requirement.

**The ingress answered from inside the cluster but not from Windows.** curl from the
node returned 200, curl from Windows returned 000. WSL was on mirrored networking,
which mirrors listening sockets but not iptables rules - and a Kubernetes hostPort is
a DNAT rule, so it never reached Windows. Opening the Hyper-V firewall did not help.
Switching back to NAT and using `netsh interface portproxy` fixed it. Also worth
checking that nothing else is holding port 80 inside WSL; I had an old nginx service
running there.

**Filebeat shipped nothing and reported everything dropped.** The count was exactly
"dropped: 49, total: 49". The error was a mapping conflict: Filebeat installs the ECS
index template by default, and in ECS `service` is an object, while the API writes it
as a plain string. Setting `setup.template.enabled` to false and deleting the existing
data stream and index template fixed it.

**`./eventus.sh break` did nothing.** The error rate stayed at 0 even after the new
build was live. `CHAOS_ERROR_RATE` was set to `"0"` in the ConfigMap and the code read
it with `??`, which only falls back on null or undefined - `"0"` is a real value, so
it always won over the default. I removed the variable from the ConfigMap and left one
constant in the code.

**Every log line had the path `/`.** Under `forRoutes('*')` Express gives `req.path`
as `/` for everything, so the dashboard grouped every request together. `req.originalUrl`
gives the real path.

**A health check returned `404000`.** The jsonpath that reads the node address returned
both the IPv4 and the IPv6 address, so curl was handed two URLs and printed two status
codes stuck together. Piping through `awk '{print $1}'` fixed it.

**The Jenkins smoke test hung for twenty minutes.** The curl in the pipeline had no
timeouts, so when the pod was not ready yet it just sat there. `--connect-timeout`,
`--max-time` and `--retry` turned it into a bounded wait.

**`./eventus.sh` gave "Permission denied".** The Windows drive is mounted without
execute bits, and git had the file as `100644`. `git update-index --chmod=+x` fixed it
in the repository.

**A rollback did nothing the first time I tried it.** `kubectl rollout undo` cannot
swap the image back if both revisions point at the same tag, so `:latest` is useless
here. Image tags are `<git-sha>-<build-number>`, and `variables.tf` rejects `:latest`
outright.

**Terraform and Jenkins kept overwriting each other.** Terraform owns the deployment
and Jenkins owns the image tag, so every `terraform apply` reset the image back to the
seed one. `ignore_changes` on the image field and on the annotations settled it.
