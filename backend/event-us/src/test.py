import time
import requests
import json

# api-endpoint
URL = "http://localhost:3000/"

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

    r_user1 = requests.post(url = URL+"users",json=data_user1)
    r_user2 = requests.post(url = URL+"users",json=data_user2)
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
    
    r_event = requests.post(url = URL+"events",json=data_event)
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
    r_join = requests.patch(URL+"users/"+user_id+"/joinEvent",json=event1_id_data)
    time.sleep(1)
    r_exit = requests.patch(URL+"users/"+user_id+"/exitEvent",json=event1_id_data)
    print(r_join)
    print(r_exit)
    


def loginTest(email,password,user_type):
    print("-----------------Login test-----------------")
    data = {
        "email": email,
        "password": password,
        "user_type": user_type
    }
    r = requests.get(URL+"users/login",json=data)
    print(r)
    return r.content



#user1,user2 = addUserTest()
#event,message = addMessageEventTest(user1["_id"],user2["_id"])
#pfp,defualtPfp = addProfilePicTest()
#joinExitEventTest(event["_id"],user2["_id"])
r_login = loginTest("ziv.morgan@gmail.com","newPass","Organizer")
print(r_login)
r_login = loginTest("ziv.morgan@gmail.com","newPass","Participant")
print(r_login)

# post request example for profile picture


# post request example for other collections
#r2 = requests.post(url = URL,json=data_user1)


#r = requests.delete("http://localhost:3000/events/421")


# extracting data in json format
#data = r.json()

#print(data)

