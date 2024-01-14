import time
import requests
"""
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

data_profilePic = {
    "id":0,

}

 
# sending get request and saving the response as response object
r = requests.post(url = URL,json=data)
 
# extracting data in json format
data = r.json()

print(data)

"""
import cv2
img = cv2.imread('backend/event-us/src/defaultpfp.png')
