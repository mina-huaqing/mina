package com.chinadovey.parking.webapps.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import com.mysql.jdbc.StringUtils;

import net.sf.json.JSONObject;

/**
 * 字符串工具类
 * @author Joshua
 *
 */

public final class StringUtil {
	/**
	 * list 转换 sql in str 'one','two'
	 */
	public static String list2sqlStr(List list) {
		String sqlStr = "'396611357'";
		if (list != null && list.size() > 0) {
			for (Object object : list) {
				sqlStr += ",'" + object + "'";
			}
		}
		return sqlStr;
	}

	/**
	 * 检测字符串是否为空字符串
	 * 
	 * @param input
	 *            待检测字符串
	 * @return
	 * 		<li>true：字符串为空</li>
	 *         <li>false：字符串不为空</li>
	 */
	public static boolean isEmpty(String input) {
		if (input == null)
			return true;
		return (input.trim().length() == 0);
	}

	/**
	 * 判断字符串是否为数字整型
	 * 
	 * @param str
	 * @return true 是数字 false 不是
	 */
	public static boolean isNumeric(String str) {
		if (isEmpty(str))
			return false;
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 设置显示值
	 * 
	 * @param memo
	 * @param maxLength
	 * @return
	 */
	public static String getOmitMemo(String memo, int maxLength) {

		if (memo == null || "".equals(memo)) {
			return memo;
		}

		if (memo.length() < maxLength) {
			return memo;
		} else {
			return memo.substring(0, maxLength) + "...";
		}
	}

	/**
	 * 从一段字符串str开始截取
	 * 
	 * @param string
	 * @param str
	 * @return
	 */
	public static String mySubString(String string, String str) {
		if (string != null && !("".equals(string))) {
			return string.substring(string.lastIndexOf(str));
		} else {
			return "";
		}
	}

	/**
	 * str2是否以str1为开头
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isStartsWith(String str1[], String str2) {
		for (String str : str1) {
			if (str2.startsWith(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取指定位数的值不够补0
	 * 
	 * @param memo
	 * @param count
	 * @return
	 */
	public static String getSubstitution(String memo, int count) {
		if (memo != null && !"".equals(memo)) {
			if (memo.length() <= count) {
				int temp = count - memo.length();
				String tailNumber = "";
				for (int i = 0; i < temp; i++) {
					tailNumber += "0";
				}
				return tailNumber + memo;
			}
			return memo.substring(0, count - 1);
		}
		return null;
	}

	/**
	 * 在str2的position位置插入str1
	 * 
	 * @param str1
	 *            insert字符
	 * @param str2
	 *            被insert字符
	 * @param position
	 *            指定最后出现字符的位置
	 * @return
	 */
	public static String insertStrToString(String str1, String str2, String position) {
		int p = str2.lastIndexOf(position);
		return insertStrToString(str1, str2, p);
	}

	/**
	 * 在str2的position位置插入str1
	 * 
	 * @param str1
	 *            insert字符
	 * @param str2
	 *            被insert字符
	 * @param position
	 *            指定位置
	 * @return
	 */
	public static String insertStrToString(String str1, String str2, int position) {
		String strStart = str2.substring(0, position);
		String strEnd = str2.substring(position);
		return strStart + str1 + strEnd;
	}

	/**
	 * upload 路径处理
	 */
	public static String uploade2WebRoot(String filePath, String beginStr) {
		String str = "";
		if (StringUtils.isNullOrEmpty(filePath) || StringUtils.isNullOrEmpty(beginStr)) {
			return str;
		}
		if (!(filePath.indexOf(beginStr) > -1)) {
			return str;
		}
		str = filePath.substring(filePath.indexOf(beginStr)).replace("\\", "/");

		return str;
	}

	/**
	 * 主要功能用于 排序字段，因为数据库中的字段为 xx_yy 而自动生成的为xxYy， 所以在排序时直接用排序字段不行，得转换一下，转换为xx_Yy
	 * 数据库大小写不敏感， 所以就直接加上了下划线，没有改变大小写
	 * 
	 * @param word
	 * @return
	 */
	public static String changeOrderStr(String word) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if (!Character.isLowerCase(c)) {
				str.append("_");
			}
			str.append(c);

		}

		return str.toString();
	}

	

	/**
	 * 随机生成两位字符加5位数字
	 * 
	 * @return
	 */
	public static String randomCouponPassword() {
		Random random = new Random();
		char i = (char) (random.nextInt(26) + 65);
		char j = (char) (random.nextInt(26) + 65);
		String str = i + "" + j;
		String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
		Long uuidlong = Long.parseLong(uuid, 16);
		return (str + uuidlong).substring(0, 7);
	}
	
	/**
	 * 随机生成8位数字
	 * 
	 * @return
	 */
	public static String randomNumber(int flag) {
		String str = "";
		Random random = new Random();
		for (int i = 0; i < flag; i++) {
			str += random.nextInt(10);
		}
		return str;
	}

	/**
	 * 随机生成7位数字
	 * 
	 * @return
	 */
	public static String randomCouponNumPassword() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
		Long uuidlong = Long.parseLong(uuid, 16);
		return (uuidlong + "21").substring(0, 7);
	}

	/**
	 * 随机生成16位字符串
	 * 
	 * @return
	 */
	public static String randomStr() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
		return uuid;
	}

	/**
	 * 首字母转小写
	 * 
	 * @param s
	 * @return
	 */
	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}

	/**
	 * 首字母转大写
	 * 
	 * @param s
	 * @return
	 */
	public static String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param byteArray
	 * @return
	 */
	public static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

	/**
	 * 将字节转换为十六制字符串
	 * 
	 * @param mByte
	 * @return
	 */
	public static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0x0f];
		tempArr[1] = Digit[mByte & 0x0f];

		String s = new String(tempArr);
		return s;
	}

	/**
	 * 对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）后，拼接成字符串string
	 * 
	 * @param arr
	 * @return
	 */
	public static String sortAndJoin(String... arr) {
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		return content.toString();
	}

	/**
	 * 对所有待签名Url格式参数按照字段名的ASCII 码从小到大排序（字典序）后，拼接成字符串string
	 * 
	 * @param arr
	 * @return
	 */
	public static String sortUrlAndJoin(String... arr) {
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0, length = arr.length; i < length; i++) {
			content.append(arr[i]);
			if (i != length - 1) {
				content.append("&");
			}
		}
		return content.toString();
	}

	/**
	 * 将一个字符串转化为输入流
	 */
	public static InputStream getStringStream(String sInputString) {
		if (sInputString != null && !sInputString.trim().equals("")) {
			try {
				ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
				return tInputStringStream;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 将一个输入流转化为字符串
	 */
	public static String getStreamString(InputStream tInputStream) {
		if (tInputStream != null) {
			try {
				BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(tInputStream));
				StringBuffer tStringBuffer = new StringBuffer();
				String sTempOneLine = new String("");
				while ((sTempOneLine = tBufferedReader.readLine()) != null) {
					tStringBuffer.append(sTempOneLine);
				}
				return tStringBuffer.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 将JSONObject转换成URL所用链接
	 * @param json
	 * @return
	 */
	public static String jsonToUrlStr(JSONObject json) {
		Iterator it = json.keys();
		StringBuilder strBuilder = new StringBuilder();
		// 遍历jsonObject数据，添加到Map对象
		while (it.hasNext()) {
			String key = String.valueOf(it.next());
			String value = (String) json.get(key);
			strBuilder.append(key).append("=").append(value).append("&");
		}
		strBuilder.delete(strBuilder.length() - 1, strBuilder.length());
		return strBuilder.toString();
	}
}
