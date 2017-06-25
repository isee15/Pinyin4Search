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
	private static Map<Character,List<Integer>> charMap;
	private static Map<Integer,String> pinyinMap;
	
	static {
		charMap = new HashMap<Character,List<Integer>>();
		try {
			Properties charProp = new Properties();
			charProp.load(new FileInputStream("data/pinyinmap.properties"));
			Enumeration<?> e = charProp.propertyNames();
		    while (e.hasMoreElements()) {
		      String key = (String) e.nextElement();
		      String indexStr = charProp.getProperty(key);
		      String[] indexArr = indexStr.split(",");
		      List<Integer> indexs = new ArrayList<Integer>();
		      for(String index : indexArr) {
		    	  indexs.add(Integer.parseInt(index));
		      }
		      charMap.put(key.charAt(0), indexs);
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}

		pinyinMap = new HashMap<Integer,String>();
		
		try {
			Properties indexProp = new Properties();
			indexProp.load(new FileInputStream("data/pinyinindex.properties"));
			Enumeration<?> e = indexProp.propertyNames();

		    while (e.hasMoreElements()) {
		      String key = (String) e.nextElement();
		      String indexStr = indexProp.getProperty(key);
		      pinyinMap.put(Integer.parseInt(key),indexStr);
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	public static String toHanyupinyin(char c) {
		List<Integer> indexs = charMap.get(c);
		if (indexs != null) {
			String py =  pinyinMap.get(indexs.get(0));
            return py.substring(0,py.length() - 1);
		}
		return "";
	}
	
	public static List<String> getAllPinyin(char c) {
		List<Integer> indexs = charMap.get(c);
		List<String> pys = new ArrayList<String>();
		if (indexs != null) {
			for(Integer index : indexs) {
				String py =  pinyinMap.get(index);
				pys.add(py);
			}
            return pys;
		}
		return pys;
	}
}
