
import requests

# api-endpoint
URL = "http://localhost:3000/users"
data = {
    "_id":47672931264,
    "name": "ziv",
    "email": "ziv.morgan@gmail.com",
    "password": "newPass",
    "userType": "creator"
}

 
# sending get request and saving the response as response object
r = requests.post(url = URL,json=data)
 
# extracting data in json format
data = r.json()

print(data)