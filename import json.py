import json
import os
import datetime

from yapiBean.JsonRootBean import JsonRootBean
from yapiBean.list import list


def main():
    # 自己的代码的Controller层 可以放在一个目录下，写好路径即可
    # 查看文件
    # print(dirlist)
    # 获取每个文件
    path = './controller'
    # dirlist = os.listdir(path)
    dirlist = os.walk(path)
    alldata=[]
    for root, dirs, files in os.walk(path):
        # root 表示当前正在访问的文件夹路径
        # dirs 表示该文件夹下的子目录名list
        # files 表示该文件夹下的文件list
        # 遍历文件
        for f in files:
            # print(os.path.join(root, f))
            fp = open(os.path.join(root, f),'r',encoding='utf-8')
            content=fp.readlines()
            # 这里获取一个类的path 和module
            module=''
            path=''
            list=[]
            for lines in content:
                line=lines
                if line.find('@WSRestController')!=-1:
                    # 当前这个集合的
                    path = getPath(line, path)
                    module = getModuleName(line, module)

                if line.find('@OpRequest')!=-1:
                    # 获取当前接口op信息
                    paBean = getOpInfo(line, path)
                    # print(paBean.__dict__)
                    list.append(paBean.__dict__)

            jsonBean=JsonRootBean(module,module,list)
            alldata.append(jsonBean.__dict__)
            # print(jsonBean.__dict__)
            # print(jsonpickle.dumps(jsonBean))
            # print(jsonpickle.encode(alldata))
            fp.close()
    #  生成一个不重复的文件名
    datatime=datetime.datetime.now().strftime('%Y-%m-%d-%H:%M:%S')
    fileName='yapi.json'
    # 新建一个文件
    # 暂时不需要使用 with
    json_file=open(fileName, 'w', encoding='utf8')
    data=json.dumps(alldata, default=lambda obj: obj.__dict__,ensure_ascii=False,indent=4)
    json_file.write(data)
    json_file.close()
 # 获取当前接口op信息
def getOpInfo(line, path):
    # print(line)
    first_index = line.find('@OpRequest')
    end_index = line.find('Mapping')
    # print(line[first_index+10:end_index])
    # 这个是基本的路径
    # print(path+'/'+line[first_index+10:end_index])
    # @OpRequestUpdateMapping(op = "update", desc = "平台计费-预约计费规则-修改")
    op_first_index = line.find('op = ')
    op_end_index = line.find(',')
    # 方法路径
    # print(path+'/'+line[first_index+10:end_index]+'?'+line[op_first_index:op_end_index])
    # 说明是有这个函数的
    moduleName = line.find('desc')
    # 描述
    # print(line[moduleName+6:-1])
    # 在这里做一个对象
    # print(path+'/'+line[first_index+10:end_index]+'?'+line[op_first_index:op_end_index])
    # print(line[moduleName+8:-2])
    paBean = list(line[moduleName + 8:-3],
                      path + '/' + line[first_index + 10:end_index] + '?' + line[op_first_index:op_end_index])
    return paBean


# 获取path
def getPath(line, path):
    first_index = line.find('"/')
    end_index = line.find(',')
    path = line[first_index + 1:end_index - 1]
    return path

# 获取当前module
def getModuleName(line, module):
    module_first_index = line.find('module =')
    modeule_end_index = line.find('")')
    # print(line[module_first_index+10:modeule_end_index])
    module = line[module_first_index + 10:modeule_end_index]
    return module


if __name__ == '__main__':
    main()