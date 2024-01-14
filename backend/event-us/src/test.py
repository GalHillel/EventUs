
import requests

# api-endpoint
URL = "http://localhost:3000/users"
data = {
    "id":73681631928,
    "name": "exampleUserdasdsa12312",
    "email": "example@email.com",
    "password": "examplePassword"
}

 
# sending get request and saving the response as response object
r = requests.post(url = URL,json=data)
 
# extracting data in json format
data = r.json()

print(data)