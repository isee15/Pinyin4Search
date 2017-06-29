package cn.z.test

import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.PinyinHelper
import cn.z.PinyinUtil
import java.util.stream.Collectors

class Main {
	companion object {
		fun toHanyupinyin4j(ch: Char): String {
			// 1.创建一个格式化输出对象
			val outputF = HanyuPinyinOutputFormat()
			// 2.设置好格式
			outputF.setToneType(HanyuPinyinToneType.WITHOUT_TONE)
			outputF.setCaseType(HanyuPinyinCaseType.LOWERCASE)
			try {
				val str = PinyinHelper.toHanyuPinyinStringArray(ch, outputF)
				if (str != null) {
					return str[0]
				}
			} catch (ex: Exception) {
				ex.printStackTrace()
			}
			return ch + ""
		}
	}
}

fun main(args: Array<String>) {
	var name = "zhongguo"
	var key = "gu"
	var m = PinyinUtil.matchOrPinyinMatch(name, key)
	println(name + " " + key + " " + " ret:" + m.stream().map({ i -> i.toString() }).collect(Collectors.joining(",")))
	name = "aaa中guo"
	key = "zu"
	m = PinyinUtil.matchOrPinyinMatch(name, key)
	println(name + " " + key + " " + " ret:" + m.stream().map({ i -> i.toString() }).collect(Collectors.joining(",")))
	var start = 0x4e00
	var end = 0x9fa5
	var cc: String
	var dd: String
	var begin = System.currentTimeMillis()
	for (loop in 0..999) {
		for (i in start..end) {
			cc = Main.toHanyupinyin4j(i.toChar())
		}
	}
	println("pinyin4j time ellapse:" + (System.currentTimeMillis() - begin))
	begin = System.currentTimeMillis()
	for (loop in 0..999) {
		for (i in start..end) {
			dd = PinyinUtil.toHanyupinyin(i.toChar())
		}
	}
	println("pinyinutil time ellapse:" + (System.currentTimeMillis() - begin))
	begin = System.currentTimeMillis()
	for (loop in 0..999) {
		for (i in start..end) {
			cc = Main.toHanyupinyin4j(i.toChar())
		}
	}
	println("pinyin4j time ellapse:" + (System.currentTimeMillis() - begin))
	for (i in start..end) {
		dd = PinyinUtil.toHanyupinyin(i.toChar())
		if (dd == (i.toChar()).toString()) {
			val j4 = PinyinHelper.toHanyuPinyinStringArray(i.toChar())
			val j4s = if (j4 == null) "" else PinyinHelper.toHanyuPinyinStringArray(i.toChar()).joinToString(",")
			println(
					i.toChar() + " " + Integer.toHexString(i) + " pinyin4j:" + j4s)
		}
	}
	for (i in start..end) {
		cc = Main.toHanyupinyin4j(i.toChar()).replace(("u:").toRegex(), "v")
		dd = PinyinUtil.toHanyupinyin(i.toChar())
		if (!cc.equals(dd, ignoreCase = true)) {
			val j4 = PinyinHelper.toHanyuPinyinStringArray(i.toChar())
			val j4s = if (j4 == null) "" else PinyinHelper.toHanyuPinyinStringArray(i.toChar()).joinToString(",")
			println(
					i.toChar() + " 4j:" + j4s + " py:" + PinyinUtil.getAllPinyin(i.toChar()).joinToString(","))
		}
	}
}