import time
import requests
import json

# api-endpoint
URL = "http://localhost:3000/profilepics"
data_user1 = {

    "name": "ziv",
    "email": "ziv.morgan@gmail.com",
    "password": "newPass",
    "user_type": "creator"
}
data_user2 = {
    "name": "user1",
    "email": "user1@gmail.com",
    "password": "userPass",
    "user_type": "user"
}

r_user1 = requests.post(url = "http://localhost:3000/users",json=data_user1).json()

r_user2 = requests.post(url = "http://localhost:3000/users",json=data_user2).json()


print(r_user1["_id"])



data_event = {

    "name":"Event1",
    "date":time.time(),
    "creator_id":r_user1["_id"]
}

data_message = {
    
    "sender_id":r_user1["_id"],
    "receiver_ids":[r_user2["_id"]],
    "title":"test message",
    "content":"this is a test message from ziv to user1"
}

r_event = requests.post(url = "http://localhost:3000/events",json=data_event).json()

r_messages = requests.post(url = "http://localhost:3000/messages",json=data_message).json()





with open("backend/event-us/src/testimg.png",'rb') as f:
    testImg = f.read()
with open("backend/event-us/src/defaultpfp.png",'rb') as f:
    defaultpfp = f.read()

data_profilePic1 = {}
files1 = {
    
    "icon": ("testimg.png", testImg, "multipart/form-data")
    }

data_profilePic2 = {}
files2 = {
    
    "icon": ("defaultpfp.png", defaultpfp, "multipart/form-data")
    }
"""
#[data_message,"messages"],[data_event,"events"]
all_data = [[data_user1,"users"],[data_user2,"users"]] 


# load test db
for d in all_data:
    r = requests.post(url = "http://localhost:3000/"+d[1],json=d[0])
"""

event1_id_data = {"_id":r_event["_id"]}
r = requests.patch("http://localhost:3000/users/"+r_user2["_id"]+"/joinEvent",json=event1_id_data)

#time.sleep(3)

r = requests.patch("http://localhost:3000/users/"+r_user2["_id"]+"/exitEvent",json=event1_id_data)




# post request example for profile picture
r = requests.post(url = URL,files=files1)
r = requests.post(url = URL,files=files2)

# post request example for other collections
#r2 = requests.post(url = URL,json=data_user1)


#r = requests.delete("http://localhost:3000/events/421")


# extracting data in json format
#data = r.json()

#print(data)

