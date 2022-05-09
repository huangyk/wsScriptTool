import json
import numpy
import pathBean

class NpEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, pathBean):
            return {'list': obj.pathBean}  # 为了简化, 我没有写inner,
        return json.JSONEncoder.default(self, obj)
