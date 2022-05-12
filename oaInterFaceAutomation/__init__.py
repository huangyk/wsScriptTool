import pymysql
import psycopg2
import http.client
import json

conn = http.client.HTTPSConnection("localhost", 7777)
payload = json.dumps({
    "code": "300750.SZ",
    "srartTradeDate": "20210218",
    "endTradeDate": "20210219"
})
headers = {
    'Content-Type': 'application/json'
}
conn.request("GET", "/dataservice/v1/mongoSercher", payload, headers)
res = conn.getresponse()
data = res.read()
print(data.decode("utf-8"))


conn = http.client.HTTPSConnection("jvehicle.stsdp.fcachinagsdp.com")
payload = json.dumps({
    "brandCode": "JEEP"
})
headers = {
    'Content-Type': 'application/json'
}
conn.request("POST", "/v1.0/mdm/series/info", payload, headers)
res = conn.getresponse()
data = res.read()
print(data.decode("utf-8"))