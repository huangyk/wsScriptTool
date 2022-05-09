import json
import os
import time


from array import *
from JsonRootBean import JsonRootBean
from pathBean import pathBean

# from numpy import piecewise

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
                line=lines.strip()
                # print(line, end="")
                # 每一行做匹配
                # 做文本处理
                if line.find('@WSRestController')!=-1:
                    # print(line)s
                    first_index=line.find('"/')
                    end_index=line.find(',')
                    # print(line[first_index+1:end_index-1])
                    # 查找字符串
                    module_first_index=line.find('module =')
                    modeule_end_index=line.find('")')
                    # print(line[module_first_index+10:modeule_end_index])
                    module=line[module_first_index+10:modeule_end_index]
                    path=line[first_index+1:end_index-1]
                if line.find('@OpRequest')!=-1:
                    # print(line)
                    first_index=line.find('@OpRequest')
                    end_index=line.find('Mapping')
                    # print(line[first_index+10:end_index])
                    # 这个是基本的路径
                    # print(path+'/'+line[first_index+10:end_index])  
                    # @OpRequestUpdateMapping(op = "update", desc = "平台计费-预约计费规则-修改")  
                    op_first_index=line.find('op = ')
                    op_end_index=line.find(',')
                    # 方法路径
                    # print(path+'/'+line[first_index+10:end_index]+'?'+line[op_first_index:op_end_index])    
                    # 说明是有这个函数的 
                    moduleName=line.find('desc')
                    # 描述
                    # print(line[moduleName+6:-1])
                # 在这里做一个对象
                    # print(path+'/'+line[first_index+10:end_index]+'?'+line[op_first_index:op_end_index])
                    # print(line[moduleName+8:-2])
                    paBean=pathBean(line[moduleName+8:-2].strip,path+'/'+line[first_index+10:end_index]+'?'+line[op_first_index:op_end_index])
                    list.append(paBean)
            jsonBean=JsonRootBean(module,module,list)
            alldata.append(jsonBean)
    # 生成文件
    # 生成当前的时间戳和文件名
    with open('11.json', 'w', encoding='utf8', errors='ignore') as json_file:
        data=json.dumps(alldata)
        json_file.write(data)
    fp.close()

 
if __name__ == '__main__':
    main()        