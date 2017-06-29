package cn.z

import java.io.File
import java.util.Properties
import java.io.FileInputStream

class PinyinUtil {
	companion object {
		val charMap: MutableMap<Char, List<Int>> = HashMap();
		val pinyinMap: MutableMap<Int, String> = HashMap();

		init {
			//here goes static initializer code

			var charProp = Properties()
			charProp.load(FileInputStream("data/pinyinmap.properties"));
			var e = charProp.propertyNames();
			while (e.hasMoreElements()) {
				var key = e.nextElement() as String;
				var indexStr = charProp.getProperty(key);
				var indexs = indexStr.split(",").map { index -> Integer.parseInt(index) };
				charMap.put(key.elementAt(0), indexs);
			}

			charProp = Properties()
			charProp.load(FileInputStream("data/pinyinindex.properties"));
			e = charProp.propertyNames();
			while (e.hasMoreElements()) {
				var key = e.nextElement() as String;
				var indexStr = charProp.getProperty(key);

				pinyinMap.put(Integer.parseInt(key), indexStr);
			}
		}

		/**
		 * 最常用的一个拼音，小写
		 */
		fun toHanyupinyin(c: Char): String {
			var indexs = charMap.get(c);
			if (indexs != null) {
				var py = pinyinMap.get(indexs.get(0));
				return py!!.substring(0, py.length - 1);
			}
			return c.toString();
		}

		/**
		 * 全部拼音，带声调
		 */
		fun getAllPinyin(c: Char): List<String> {
			val indexs = charMap.get(c);
			val pys = ArrayList<String>();
			if (indexs != null) {
				for (index in indexs) {
					val py = pinyinMap.get(index);
					pys.add(py!!);
				}
			}
			return pys;
		}

		private fun match(str: String, key: String): List<Int> {
			println("matching: " + str + " " + key)
			if (key.length == 0)
				ArrayList<Int>()
			val matchIndex = ArrayList<Int>()
			var j = 0
			var i = 0
			while (i < str.length && j < key.length) {
				if (key.get(j) == str.get(i)) {
					matchIndex.add(i)
					j++
				}
				i++
			}
			if (j == key.length) {
				return matchIndex
			}
			return ArrayList<Int>()
		}

		/**
		 *
		 * @param str
		 * @param searchKey
		 * @return 搜索匹配的index，可以用于高亮显示结果
		 */
		fun matchOrPinyinMatch(str: String, searchKey: String): List<Int> {
			val name = str.toLowerCase()
			val key = searchKey.toLowerCase()
			var ret = match(name, key)
			if (ret.size > 0) {
				return ret
			}
			val pyIndex = ArrayList<Int>()
			var startIndex = 0
			val pyStr = StringBuilder()
			for (i in 0..name.length - 1) {
				val py = toHanyupinyin(name.get(i))
				pyStr.append(py)
				for (j in 0..py.length - 1) {
					pyIndex.add(startIndex)
				}
				startIndex++
			}
			ret = match(pyStr.toString(), key)
			if (ret.size > 0) {
				val pyRet = ArrayList<Int>()
				for (index in ret) {
					val oriIndex = pyIndex.get(index.toInt())
					if (!pyRet.contains(oriIndex)) {
						pyRet.add(oriIndex)
					}
				}
				return pyRet
			}
			return ArrayList<Int>()
		}
	}
}