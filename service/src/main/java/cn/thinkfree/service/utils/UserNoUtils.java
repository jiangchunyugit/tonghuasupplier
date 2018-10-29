package cn.thinkfree.service.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 生成编号
 */
@Deprecated
public class UserNoUtils {
	private static final char[] digits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
			'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V' };
	private static String ip = null;
	private static AtomicLong number = new AtomicLong(0);
	private static long recordIpTime = 0;
	private static final String maxNumStr = "VVVVV";
	private static final int maxLength = 5;
	private static final long maxNum = UnCompressNumber(maxNumStr);
	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");

	/*public static void main(String[] args) {
		System.out.println(getNonceStr());
		for(int i=0;i<8;i++){
			System.out.println(getUserNo("pc"));
		}
	}*/


	/**
	 * 获取一个USER编号
	 * 
	 * @return String
	 */
	public static String getUserNo(String prefix) {
		synchronized (UserNoUtils.class) {
			String time = getTime();
			long num1 = number.getAndIncrement();
			if (num1 > maxNum) {
				num1 = 0;
				number.set(0);
			}
			String num = CompressNumber(num1);

			while (num.length() < maxLength) {
				num = "0" + num;
			}
			if (num.length() > maxLength) {
				num = num.substring(num.length() - maxLength, num.length());
			}
			return prefix + time + num;
		}
	}

	/**
	 * 获取一个随机字符串
	 * @return String
	 */
	public static String getNonceStr() {
		return getRandomString(16);
	}
	
	

	/**
	 * 获取指定位数的随机字符串(包含小写字母、大写字母、数字,0<length)
	 * @return String
	 */
	public static String getRandomString(int length) {
		//随机字符串的随机字符库
		String KeyString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuffer sb = new StringBuffer();
		int len = KeyString.length();
		for (int i = 0; i < length; i++) {
			sb.append(KeyString.charAt((int) Math.round(Math.random() * (len - 1))));
		}
		return sb.toString();
	}

	private static String getTime() {
		String time = formatter.format(new Date());
		return time;
	}

	private static String CompressNumber(long number) {
		char[] buf = new char[32];
		int charPos = 32;
		int radix = 1 << 5;
		long mask = radix - 1;
		do {
			buf[--charPos] = digits[(int) (number & mask)];
			number >>>= 5;
		} while (number != 0);
		String num = new String(buf, charPos, (32 - charPos));
		while (num.length() < maxLength) {
			num = "0" + num;
		}
		return num;
	}

	public static void main(String[] args) {
		System.out.println(CompressNumber(160));
	}
	private static long UnCompressNumber(String decompStr) {
		long result = 0;
		for (int i = decompStr.length() - 1; i >= 0; i--) {
			for (int j = 0; j < digits.length; j++) {
				if (decompStr.charAt(i) == digits[j]) {
					result += ((long) j) << 5 * (decompStr.length() - 1 - i);
				}
			}
		}
		return result;
	}

}
