package cn.z.test;

import java.io.Console;
import java.util.List;
import java.util.stream.Collectors;

import cn.z.PinyinUtil;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

public class Main {

	private static String toHanyupinyin4j(char ch) {
		// 1.创建一个格式化输出对象
		HanyuPinyinOutputFormat outputF = new HanyuPinyinOutputFormat();
		// 2.设置好格式
		outputF.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		outputF.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		try {
			String[] str = PinyinHelper.toHanyuPinyinStringArray(ch, outputF);
			if (str != null) {
				return str[0];
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ch + "";
	}

	public static void main(String[] args) {
		String oriString = "拈朵微笑的花请微笑向前";
		String result = PinyinUtil.toHanyupinyin(oriString);
		System.out.println(oriString + ":" + result);
		
		String name = "拈朵微笑的花请微笑向前";
		String key = "weix";
		List<Integer> m = PinyinUtil.matchOrPinyinMatch(name, key);
		System.out.println(name + " " + key + " " + " ret:" + m.stream().map(i->i.toString()).collect(Collectors.joining(",")));
		System.out.println(PinyinUtil.hilightSearch(name, key));
		name = "aaa中guo";
		key = "zu";
		m = PinyinUtil.matchOrPinyinMatch(name, key);
		System.out.println(name + " " + key + " " + " ret:" + m.stream().map(i->i.toString()).collect(Collectors.joining(",")));
		int start = 0x4e00;
		int end = 0x9fa5;
		String cc = null;
		String dd = null;
		long begin = System.currentTimeMillis();
		for (int loop = 0; loop < 1000; loop++) {
			for (int i = start; i <= end; i++) {
				cc = toHanyupinyin4j((char) i);
			}
		}
		System.out.println("pinyin4j time ellapse:" + (System.currentTimeMillis()-begin));
		begin = System.currentTimeMillis();
		for (int loop = 0; loop < 1000; loop++) {
			for (int i = start; i <= end; i++) {
				dd = PinyinUtil.toHanyupinyin((char) i);
			}
		}
		System.out.println("pinyinutil time ellapse:" + (System.currentTimeMillis()-begin));
		
		begin = System.currentTimeMillis();
		for (int loop = 0; loop < 1000; loop++) {
			for (int i = start; i <= end; i++) {
				cc = toHanyupinyin4j((char) i);
			}
		}
		System.out.println("pinyin4j time ellapse:" + (System.currentTimeMillis()-begin));
		
		for (int i = start; i <= end; i++) {
			dd = PinyinUtil.toHanyupinyin((char) i);
			if (dd.equals(String.valueOf((char)i))) {
				String[] j4 = PinyinHelper.toHanyuPinyinStringArray((char) i);
				String j4s = j4 == null ? "" : String.join(",", PinyinHelper.toHanyuPinyinStringArray((char) i));
				System.out.println(
						(char) i + " " + Integer.toHexString(i) + " pinyin4j:" + j4s);
			}
		}
		
		for (int i = start; i <= end; i++) {
			cc = toHanyupinyin4j((char) i).replaceAll("u:", "v");
			dd = PinyinUtil.toHanyupinyin((char) i);
			if (!cc.equalsIgnoreCase(dd)) {
				String[] j4 = PinyinHelper.toHanyuPinyinStringArray((char) i);
				String j4s = j4 == null ? "" : String.join(",", PinyinHelper.toHanyuPinyinStringArray((char) i));
				System.out.println(
						(char) i + " 4j:" + j4s + " py:" + String.join(",", PinyinUtil.getAllPinyin((char) i)));
			}
		}

	}

}
