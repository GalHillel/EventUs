import time
import requests

# api-endpoint
URL = "http://localhost:3000/users"
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

data_profilePic1 = {
    "id":0,
    "icon":defaultpfp
}

data_profilePic2 = {
    "id":31245,
    "icon":testImg
}


 
# sending get request and saving the response as response object
r = requests.post(url = URL,json=data_user1)
 
# extracting data in json format
data = r.json()

print(data)

