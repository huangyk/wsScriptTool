
# 第二层的json

class pathBean:
    status='undone'
    type= "static"
    index=0
    methods= 'post'
    res_body_type = 'json'
    req_headers =  [{"required": "1","_id": "6274ea876125056ac4ddd6a9", "name": "Content-Type","value": "application/json"}]
    def __init__(self, title,path):
        self.title = title
        self.path= path
        
        
