package cn.z;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PinyinUtil {
	private static final Map<Character, List<Integer>> charMap;
	private static final Map<Integer, String> pinyinMap;

	static {
		charMap = new HashMap<>();
		try {
			Properties charProp = new Properties();
			charProp.load(new FileInputStream("data/pinyinmap.properties"));
			Enumeration<?> e = charProp.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String indexStr = charProp.getProperty(key);
				String[] indexArr = indexStr.split(",");
				List<Integer> indexs = new ArrayList<>();
				for (String index : indexArr) {
					indexs.add(Integer.parseInt(index));
				}
				charMap.put(key.charAt(0), indexs);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		pinyinMap = new HashMap<>();

		try {
			Properties indexProp = new Properties();
			indexProp.load(new FileInputStream("data/pinyinindex.properties"));
			Enumeration<?> e = indexProp.propertyNames();

			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String indexStr = indexProp.getProperty(key);
				pinyinMap.put(Integer.parseInt(key), indexStr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param c
	 * @return 最常用的一个拼音，小写
	 */
	public static String toHanyupinyin(char c) {
		List<Integer> indexs = charMap.get(c);
		if (indexs != null) {
			String py = pinyinMap.get(indexs.get(0));
			return py.substring(0, py.length() - 1);
		}
		return String.valueOf(c);
	}
	
	/**
	 * 
	 * @param str
	 * @return 汉字转拼音
	 */
	public static String toHanyupinyin(String str) {
	    if (str == null) {
	    	return "";
	    }
	    StringBuilder sb = new StringBuilder();
	    char[] arr = str.toCharArray();
	    boolean isFirst = true;
		for(char c : arr) {
			if (isFirst){
				isFirst = false;
			}
			else {
				sb.append(" ");
			}
			sb.append(toHanyupinyin(c));
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param c
	 * @return 全部拼音，带声调
	 */
	public static List<String> getAllPinyin(char c) {
		List<Integer> indexs = charMap.get(c);
		List<String> pys = new ArrayList<>();
		if (indexs != null) {
			for (Integer index : indexs) {
				String py = pinyinMap.get(index);
				pys.add(py);
			}
			return pys;
		}
		return pys;
	}

	private static List<Integer> match(final String str, final String key) {
		System.out.println("matching: " + str + " " + key);
		if (key.length() == 0) {
			return new ArrayList<>();
		}
		List<Integer> matchAllIndex = new ArrayList<>();
		List<Integer> matchIndex = new ArrayList<>();
		int j = 0;
		for (int i = 0; i < str.length() && j < key.length(); i++) {
			if (key.charAt(j) == str.charAt(i)) {
				matchIndex.add(i);
				j++;
			}
			if (j == key.length()) {
				matchAllIndex.addAll(matchIndex);
				matchIndex = new ArrayList<>();
				j = 0;
			}
		}

		return matchAllIndex;
	}

	/**
	 * 
	 * @param str
	 * @param searchKey
	 * @return 搜索匹配的index，可以用于高亮显示结果
	 */
	public static List<Integer> matchOrPinyinMatch(String str, String searchKey) {
		String name = str.toLowerCase();
		String key = searchKey.toLowerCase();
		List<Integer> ret = match(name, key);
		if (ret.size() > 0) {
			return ret;
		}
		List<Integer> pyIndex = new ArrayList<>();
		int startIndex = 0;
		StringBuilder pyStr = new StringBuilder();
		for (int i = 0; i < name.length(); i++) {
			String py = toHanyupinyin(name.charAt(i));
			pyStr.append(py);
			for (int j = 0; j < py.length(); j++) {
				pyIndex.add(startIndex);
			}
			startIndex++;
		}

		ret = match(pyStr.toString(), key);
		if (ret.size() > 0) {
			List<Integer> pyRet = new ArrayList<>();
			for (Integer index : ret) {
				Integer oriIndex = pyIndex.get(index);
				if (!pyRet.contains(oriIndex)) {
					pyRet.add(oriIndex);
				}
			}

			return pyRet;
		}

		return new ArrayList<>();
	}
	
	public static String hilightSearch(String str, String searchKey) {
		StringBuilder sb = new StringBuilder();
		List<Integer> m = matchOrPinyinMatch(str,searchKey);
		for(int i = 0; i < str.length(); i++) {
			if (m.contains(i)) {
				sb.append("<em>").append(str.charAt(i)).append("</em>");
			}
			else {
				sb.append(str.charAt(i));
			}
		}
        return sb.toString();
    }

}
