
import requests

# api-endpoint
URL = "http://localhost:3000/users"
data = {
    "_id":8713276817587123,
    "name": "exampleUserdasdsa12312",
    "email": "example@email.com",
    "password": "examplePassword"
}

 
# sending get request and saving the response as response object
r = requests.post(url = URL,json=data)
 
# extracting data in json format
data = r.json()

print(data)