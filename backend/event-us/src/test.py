import time
import requests
import json

# api-endpoint
URL = "http://localhost:3000/"



def addUser(name,email,password,user_type)->requests.Response:
    print("-----------------Add user-----------------")
    data_user1 = {
        "name": name,
        "email": email,
        "password": password,
        "user_type": user_type
    }
    r_user1 = requests.post(url = URL+"users/register",json=data_user1)
    return r_user1

#Organizer
def genUsers(names,u_type="Participant")->list[requests.Response]:
    
    emails = [i+"@gmail.com" for i in names]
    passwords = [i+"Pass" for i in names]
    users = []
    for i in range(len(names)):
        users.append(addUser(names[i],emails[i],passwords[i],u_type))
        time.sleep(1)
    return users

names1 = ["messageTest","messageTest2"]
names2 = ["messageTestO"]
names = names1 + names2
user_responses = genUsers(names1) + genUsers(names2,"Organizer")
users = []
for i,user_r in enumerate(user_responses):
    if user_r.status_code != 201:
        user_r = requests.get(URL+"users?name="+names[i])
        time.sleep(1)
        users.append(user_r.json()[0])
    else:
        users.append(user_r.json())

#print(users)
u1,u2,o1 = users

e1 = {}
time.sleep(1)
if len(o1["events"])==0:
    data_event = {
            "name":"Event1",
            "date":time.time(),
            "creator_id":o1["_id"]}
    event_r = requests.post(url=URL+"events/create",data=data_event)
    e1 = event_r.json()
else:
    event_r = requests.get(URL+"events/"+o1["events"][0]+"/info")
    e1 = event_r.json()
time.sleep(1)

if len(u1["events"]) == 0:
    requests.patch(url=URL+"events/"+e1["_id"]+"/joinEvent",data={"_id":u1["_id"]})
    time.sleep(1)
if len(u2["events"]) == 0:
    requests.patch(url=URL+"events/"+e1["_id"]+"/joinEvent",data={"_id":u2["_id"]})
    time.sleep(1)

data_message = {
        "sender_id":o1["_id"],
        "receiver_ids":[u1["_id"],u2["_id"]],
        "title":"test message",
        "content":"this is a test message from o1 to u1 and u2"
    }

m1 = requests.post(url=URL+"messages",data=data_message).json()
time.sleep(1)

data_message = {
        "sender_id":u1["_id"],
        "receiver_ids":[o1["_id"]],
        "title":"test message",
        "content":"this is a test message from u1 to o1"
    }

m2 = requests.post(url=URL+"messages",data=data_message).json()
time.sleep(1)


#r = requests.get(url=URL+"messages/"+m1["_id"]+"/info?_id="+u1["_id"])
#print(r.text)
#time.sleep(1)


r = requests.get(url=URL+"users/"+u2["_id"]+"/messages")
print(r.text)
time.sleep(1)


#r = requests.get(url=URL+"messages/"+m1["_id"]+"/info?_id="+u1["_id"])
#print(r.text)
#time.sleep(1)


r = requests.get(url=URL+"users/"+u1["_id"]+"/removeMessage",data={"_id":m2["_id"]})

r = requests.patch(url=URL+"users/"+u1["_id"]+"/exitEvent",data={"_id":e1["_id"]})
time.sleep(1)

print(r)
print(r.text)



    




def addUserTest():
    print("-----------------Add user test-----------------")
    data_user1 = {
        "name": "ziv",
        "email": "ziv.morgan@gmail.com",
        "password": "newPass",
        "user_type": "Organizer"
    }
    data_user2 = {
        "name": "user1",
        "email": "user1@gmail.com",
        "password": "userPass",
        "user_type": "Participant"
    }

    r_user1 = requests.post(url = URL+"users/register",json=data_user1)
    r_user2 = requests.post(url = URL+"users/register",json=data_user2)
    print(r_user1)
    print(r_user2)
    return r_user1.json(), r_user2.json()


def addMessageEventTest(u1_id,u2_id):
    print("-----------------Add message and event test-----------------")
    data_event = {

        "name":"Event1",
        "date":time.time(),
        "creator_id":u1_id
    }
    data_message = {
        "sender_id":u1_id,
        "receiver_ids":[u2_id],
        "title":"test message",
        "content":"this is a test message from ziv to user1"
    }
    
    r_event = requests.post(url = URL+"events/create",json=data_event)
    r_messages = requests.post(url = URL+"messages",json=data_message)
    print(r_event)
    print(r_messages)
    return r_event.json(), r_messages.json()


def addProfilePicTest():
    print("-----------------Add profile pic test-----------------")
    with open("backend/event-us/src/testimg.png",'rb') as f:
        testImg = f.read()
    with open("backend/event-us/src/defaultpfp.png",'rb') as f:
        defaultpfp = f.read()
    
    files1 = {
        "icon": ("testimg.png", testImg, "multipart/form-data")
    }
    files2 = {
        "icon": ("defaultpfp.png", defaultpfp, "multipart/form-data")
    }
    r_files1 = requests.post(url = URL+"profilepics",files=files1)
    r_files2 = requests.post(url = URL+"profilepics",files=files2)
    print(r_files1)
    print(r_files2)
    return r_files1.json(), r_files2.json()
    
def joinExitEventTest(event_id,user_id):
    print("-----------------Join/Exit event test-----------------")
    event1_id_data = {"_id":event_id}
    r_join = requests.patch(URL+"events/"+event_id+"/joinEvent",json={"_id":user_id})
    time.sleep(1)
    r_exit = requests.patch(URL+"users/"+user_id+"/exitEvent",json=event1_id_data)
    print(r_join)
    print(r_join.text)
    print(r_exit)
    print(r_exit.text)
    


def loginTest(email,password,user_type):
    print("-----------------Login test-----------------")
    data = {
        "email": email,
        "password": password,
        "user_type": user_type
    }
    r = requests.get(URL+"users/login?email="+email+"&password="+password+"&user_type="+user_type)
    print(r)
    return r.content



#user1,user2 = addUserTest()
#event,message = addMessageEventTest(user1["_id"],user2["_id"])
#pfp,defualtPfp = addProfilePicTest()
#joinExitEventTest(event["_id"],user2["_id"])
#joinExitEventTest("65b909810fc846c08b8a4d4b","65b9477b8c22a314c4681496")
#r_login = loginTest("ziv.morgan3@gmail.com","newPass","Organizer")
#print(r_login)
#r_login = loginTest("ziv.morgan3@gmail.com","newPass","Participant")
#print(r_login)

# post request example for profile picture


# post request example for other collections
#r2 = requests.post(url = URL,json=data_user1)


#r = requests.delete("http://localhost:3000/events/421")


# extracting data in json format
#data = r.json()

#print(data)

