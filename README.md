# Pinyin4Search
汉字转拼音
数据源比对来自微软C#版拼音数据以及苹果iOS拼音数据，支持20948个汉字。
速度大概是pinyin4j 7倍。
支持拼音的模糊搜索高亮

## Java
```
//返回常用的一个读音，不带声调
public static String toHanyupinyin(char c)
//返回所有读音，带声调
public static List<String> getAllPinyin(char c)
//搜索匹配，返回匹配到的索引，用于高亮显示
public static List<Integer> matchOrPinyinMatch(String str, String searchKey)
```
