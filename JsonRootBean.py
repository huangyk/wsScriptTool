from matplotlib.pyplot import cla


# 最外层的json

class JsonRootBean:
    index=0
    def __init__(self, name,desc,pathBean):
        self.name = name
        self.desc= desc
        self.pathBean=pathBean
    def json_serialize(self):
        return {'pathBean': self.pathBean}