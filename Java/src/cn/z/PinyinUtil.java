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
	private static Map<Character, List<Integer>> charMap;
	private static Map<Integer, String> pinyinMap;

	static {
		charMap = new HashMap<Character, List<Integer>>();
		try {
			Properties charProp = new Properties();
			charProp.load(new FileInputStream("data/pinyinmap.properties"));
			Enumeration<?> e = charProp.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String indexStr = charProp.getProperty(key);
				String[] indexArr = indexStr.split(",");
				List<Integer> indexs = new ArrayList<Integer>();
				for (String index : indexArr) {
					indexs.add(Integer.parseInt(index));
				}
				charMap.put(key.charAt(0), indexs);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		pinyinMap = new HashMap<Integer, String>();

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
	 * @param c
	 * @return 全部拼音，带声调
	 */
	public static List<String> getAllPinyin(char c) {
		List<Integer> indexs = charMap.get(c);
		List<String> pys = new ArrayList<String>();
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
		if (key.length() == 0)
			new ArrayList<Integer>();
		List<Integer> matchIndex = new ArrayList<Integer>();
		int j = 0;
		for (int i = 0; i < str.length() && j < key.length(); i++) {
			if (key.charAt(j) == str.charAt(i)) {
				matchIndex.add(i);
				j++;
			}
		}
		if (j == key.length()) {
			return matchIndex;
		}
		return new ArrayList<Integer>();
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
		List<Integer> pyIndex = new ArrayList<Integer>();
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
			List<Integer> pyRet = new ArrayList<Integer>();
			for (Integer index : ret) {
				Integer oriIndex = pyIndex.get(index.intValue());
				if (!pyRet.contains(oriIndex)) {
					pyRet.add(oriIndex);
				}
			}

			return pyRet;
		}

		return new ArrayList<Integer>();
	}
}
