# -*- coding: utf-8 -*-
import sys,os
import ConfigParser
import collections
import errno

def convert(ds):
    try:
        os.makedirs(ds)
    except OSError as exc:  # Guard against race condition
        if exc.errno != errno.EEXIST:
            raise
    py = {}
    allpy = set()
    with open(ds + ".txt") as f:
        for line in f:
           (key, val) = line.strip().split("=")
           allpy.update(val.split(","))
    allpy = sorted(list(allpy))
    indexs = dict((item,index) for index, item in enumerate(allpy))

    print py
    with open(ds + ".txt") as f:
        for line in f:
           (key, val) = line.strip().split("=")
           print key,val,key.decode('unicode-escape')
           py[key] = [indexs[x] for x in val.split(",")]
    print py

    with open(ds + "/pinyinindex.properties", 'w') as f:
        for index, item in enumerate(allpy):
            f.write(str(index) + '=' + item + "\n")
    with open(ds + "/pinyinmap.properties", 'w') as f:
        od = collections.OrderedDict(sorted(py.items()))
        for k, v in od.iteritems():
            f.write(k + '=' + ','.join(map(str, v))+ "\n")

def merge(a,b):
    aa = {}
    with open(a + ".txt") as f:
        for line in f:
           (key, val) = line.strip().split("=")
           # print key,val,key.decode('unicode-escape')
           aa[key] = val.split(",")
    print aa
    with open(b + ".txt") as f:
        for line in f:
           (key, val) = line.strip().split("=")
           # print key,val,key.decode('unicode-escape')
           if (key in aa):
               vv = val.split(",")
               vvv = [item for item in vv if item not in aa[key]]
               print vvv,aa[key]
               aa[key] = aa[key] + vvv
               print aa[key]
           else:
               aa[key] = val.split(",")
    print aa
    with open("merge.txt", 'w') as f:
        od = collections.OrderedDict(sorted(aa.items()))
        for k, v in od.iteritems():
            print k, v
            f.write(k + '=' + ','.join(str(x) for x in v) + "\n")

# convert("ios")
# convert("ms")
# merge("ios","ms")
convert("merge")