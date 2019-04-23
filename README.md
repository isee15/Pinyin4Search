# Pinyin4Search
Java汉字转拼音
数据源比对来自微软C#版拼音数据以及苹果iOS拼音数据，支持20948个汉字。
速度大概是pinyin4j 7倍。
支持拼音的模糊搜索高亮

## Java
```
//返回常用的一个读音，不带声调
public static String toHanyupinyin(char c)
public static String toHanyupinyin(String str)

String result = PinyinUtil.toHanyupinyin("拈朵微笑的花");
//拈朵微笑的花:nian duo wei xiao de hua 

//返回所有读音，带声调
public static List<String> getAllPinyin(char c)
//搜索匹配，返回匹配到的索引，用于高亮显示
public static List<Integer> matchOrPinyinMatch(String str, String searchKey)

List<Integer> m = PinyinUtil.matchOrPinyinMatch("拈朵微笑的花", "weix");
//hilight: [2,3]
PinyinUtil.hilightSearch("拈朵微笑的花", "weix")
//拈朵<em>微</em><em>笑</em>的花
```
