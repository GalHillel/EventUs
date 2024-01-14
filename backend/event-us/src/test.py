import time
import requests
import json

# api-endpoint
URL = "http://localhost:3000/profilepics"
data_user1 = {
    "_id":123,
    "pfp_id":31245,
    "name": "ziv",
    "email": "ziv.morgan@gmail.com",
    "password": "newPass",
    "userType": "creator"
}
data_user2 = {
    "_id":321,
    "name": "user1",
    "email": "user1@gmail.com",
    "password": "userPass",
    "userType": "user"
}

data_event = {
    "_id":421,
    "name":"Event1",
    "date":time.time(),
    "creator_id":123
}

data_message = {
    "_id":4215123,
    "sender_id":123,
    "receiver_id":321,
    "title":"test message",
    "content":"this is a test message from ziv to user1"
}

with open("backend/event-us/src/testimg.png",'rb') as f:
    testImg = f.read()
with open("backend/event-us/src/defaultpfp.png",'rb') as f:
    defaultpfp = f.read()

data_profilePic1 = {"_id":31245}
files1 = {
    "_id":('payload.json',json.dumps(data_profilePic1),'application/json'),
    "icon": ("testimg.png", testImg, "multipart/form-data")
    }

data_profilePic2 = {"_id":0}
files2 = {
    "_id":('payload.json',json.dumps(data_profilePic2),'application/json'),
    "icon": ("defaultpfp.png", defaultpfp, "multipart/form-data")
    }

 
# sending get request and saving the response as response object
for i in range(1):
    r = requests.post(url = URL,files=files1)
 
# extracting data in json format
data = r.json()

print(data)

