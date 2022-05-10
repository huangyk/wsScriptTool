from matplotlib.pyplot import cla
import yapiBean.list

# 最外层的json

class JsonRootBean:
    index=0
    def __init__(self, name,desc,list):
        self.name = name
        self.desc= desc
        self.list=list
    def json_serialize(self):
        return {'pathBean': self.pathBean}