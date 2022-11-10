package net.md_5.bungee.eaglercraft.sun.net.util;

import java.net.URL;
import java.util.Arrays;

public class IPAddressUtil {
	private static final int INADDR4SZ = 4;

	private static final int INADDR16SZ = 16;

	private static final int INT16SZ = 2;

	private static final long L_IPV6_DELIMS = 0L;

	private static final long H_IPV6_DELIMS = 671088640L;

	private static final long L_GEN_DELIMS = -8935000888854970368L;

	private static final long H_GEN_DELIMS = 671088641L;

	private static final long L_AUTH_DELIMS = 288230376151711744L;

	private static final long H_AUTH_DELIMS = 671088641L;

	private static final long L_COLON = 288230376151711744L;

	private static final long H_COLON = 0L;

	private static final long L_SLASH = 140737488355328L;

	private static final long H_SLASH = 0L;

	private static final long L_BACKSLASH = 0L;

	private static final long H_BACKSLASH = 268435456L;

	private static final long L_NON_PRINTABLE = 4294967295L;

	private static final long H_NON_PRINTABLE = -9223372036854775808L;

	private static final long L_EXCLUDE = -8935000884560003073L;

	private static final long H_EXCLUDE = -9223372035915251711L;

	public static byte[] textToNumericFormatV4(String paramString) {
		byte[] arrayOfByte = new byte[4];
		long l = 0L;
		byte b1 = 0;
		boolean bool = true;
		int i = paramString.length();
		if (i == 0 || i > 15)
			return null;
		for (byte b2 = 0; b2 < i; b2++) {
			char c = paramString.charAt(b2);
			if (c == '.') {
				if (bool || l < 0L || l > 255L || b1 == 3)
					return null;
				arrayOfByte[b1++] = (byte) (int) (l & 0xFFL);
				l = 0L;
				bool = true;
			} else {
				int j = Character.digit(c, 10);
				if (j < 0)
					return null;
				l *= 10L;
				l += j;
				bool = false;
			}
		}
		if (bool || l < 0L || l >= 1L << (4 - b1) * 8)
			return null;
		switch (b1) {
			case 0 :
				arrayOfByte[0] = (byte) (int) (l >> 24L & 0xFFL);
			case 1 :
				arrayOfByte[1] = (byte) (int) (l >> 16L & 0xFFL);
			case 2 :
				arrayOfByte[2] = (byte) (int) (l >> 8L & 0xFFL);
			case 3 :
				arrayOfByte[3] = (byte) (int) (l >> 0L & 0xFFL);
				break;
		}
		return arrayOfByte;
	}

	public static byte[] textToNumericFormatV6(String paramString) {
		if (paramString.length() < 2)
			return null;
		char[] arrayOfChar = paramString.toCharArray();
		byte[] arrayOfByte1 = new byte[16];
		int j = arrayOfChar.length;
		int k = paramString.indexOf("%");
		if (k == j - 1)
			return null;
		if (k != -1)
			j = k;
		byte b = -1;
		byte b1 = 0, b2 = 0;
		if (arrayOfChar[b1] == ':' && arrayOfChar[++b1] != ':')
			return null;
		byte b3 = b1;
		boolean bool = false;
		int i = 0;
		while (b1 < j) {
			char c = arrayOfChar[b1++];
			int m = Character.digit(c, 16);
			if (m != -1) {
				i <<= 4;
				i |= m;
				if (i > 65535)
					return null;
				bool = true;
				continue;
			}
			if (c == ':') {
				b3 = b1;
				if (!bool) {
					if (b != -1)
						return null;
					b = b2;
					continue;
				}
				if (b1 == j)
					return null;
				if (b2 + 2 > 16)
					return null;
				arrayOfByte1[b2++] = (byte) (i >> 8 & 0xFF);
				arrayOfByte1[b2++] = (byte) (i & 0xFF);
				bool = false;
				i = 0;
				continue;
			}
			if (c == '.' && b2 + 4 <= 16) {
				String str = paramString.substring(b3, j);
				byte b4 = 0;
				int n = 0;
				while ((n = str.indexOf('.', n)) != -1) {
					b4++;
					n++;
				}
				if (b4 != 3)
					return null;
				byte[] arrayOfByte = textToNumericFormatV4(str);
				if (arrayOfByte == null)
					return null;
				for (byte b5 = 0; b5 < 4; b5++)
					arrayOfByte1[b2++] = arrayOfByte[b5];
				bool = false;
				break;
			}
			return null;
		}
		if (bool) {
			if (b2 + 2 > 16)
				return null;
			arrayOfByte1[b2++] = (byte) (i >> 8 & 0xFF);
			arrayOfByte1[b2++] = (byte) (i & 0xFF);
		}
		if (b != -1) {
			int m = b2 - b;
			if (b2 == 16)
				return null;
			for (b1 = 1; b1 <= m; b1++) {
				arrayOfByte1[16 - b1] = arrayOfByte1[b + m - b1];
				arrayOfByte1[b + m - b1] = 0;
			}
			b2 = 16;
		}
		if (b2 != 16)
			return null;
		byte[] arrayOfByte2 = convertFromIPv4MappedAddress(arrayOfByte1);
		if (arrayOfByte2 != null)
			return arrayOfByte2;
		return arrayOfByte1;
	}

	public static boolean isIPv4LiteralAddress(String paramString) {
		return (textToNumericFormatV4(paramString) != null);
	}

	public static boolean isIPv6LiteralAddress(String paramString) {
		return (textToNumericFormatV6(paramString) != null);
	}

	public static byte[] convertFromIPv4MappedAddress(byte[] paramArrayOfbyte) {
		if (isIPv4MappedAddress(paramArrayOfbyte)) {
			byte[] arrayOfByte = new byte[4];
			System.arraycopy(paramArrayOfbyte, 12, arrayOfByte, 0, 4);
			return arrayOfByte;
		}
		return null;
	}

	private static boolean isIPv4MappedAddress(byte[] paramArrayOfbyte) {
		if (paramArrayOfbyte.length < 16)
			return false;
		if (paramArrayOfbyte[0] == 0 && paramArrayOfbyte[1] == 0 && paramArrayOfbyte[2] == 0 && paramArrayOfbyte[3] == 0
				&& paramArrayOfbyte[4] == 0 && paramArrayOfbyte[5] == 0 && paramArrayOfbyte[6] == 0
				&& paramArrayOfbyte[7] == 0 && paramArrayOfbyte[8] == 0 && paramArrayOfbyte[9] == 0
				&& paramArrayOfbyte[10] == -1 && paramArrayOfbyte[11] == -1)
			return true;
		return false;
	}
	
	public static boolean match(char paramChar, long paramLong1, long paramLong2) {
		if (paramChar < '@')
			return ((1L << paramChar & paramLong1) != 0L);
		return false;
	}

	public static int scan(String paramString, long paramLong1, long paramLong2) {
		byte b = -1;
		int i;
		if (paramString == null || (i = paramString.length()) == 0)
			return -1;
		boolean bool = false;
		while (++b < i && !(bool = match(paramString.charAt(b), paramLong1, paramLong2)));
		if (bool)
			return b;
		return -1;
	}

	public static int scan(String paramString, long paramLong1, long paramLong2, char[] paramArrayOfchar) {
		byte b = -1;
		int i;
		if (paramString == null || (i = paramString.length()) == 0)
			return -1;
		boolean bool = false;
		char c2 = paramArrayOfchar[0];
		char c1;
		while (++b < i && !(bool = match(c1 = paramString.charAt(b), paramLong1, paramLong2))) {
			if (c1 >= c2 && Arrays.binarySearch(paramArrayOfchar, c1) > -1) {
				bool = true;
				break;
			}
		}
		if (bool)
			return b;
		return -1;
	}

	private static String describeChar(char paramChar) {
		if (paramChar < ' ' || paramChar == '') {
			if (paramChar == '\n')
				return "LF";
			if (paramChar == '\r')
				return "CR";
			return "control char (code=" + paramChar + ")";
		}
		if (paramChar == '\\')
			return "'\\'";
		return "'" + paramChar + "'";
	}

	private static String checkUserInfo(String paramString) {
		int i = scan(paramString, -9223231260711714817L, -9223372035915251711L);
		if (i >= 0)
			return "Illegal character found in user-info: " + describeChar(paramString.charAt(i));
		return null;
	}

	private static String checkHost(String paramString) {
		if (paramString.startsWith("[") && paramString.endsWith("]")) {
			paramString = paramString.substring(1, paramString.length() - 1);
			if (isIPv6LiteralAddress(paramString)) {
				int j = paramString.indexOf('%');
				if (j >= 0) {
					j = scan(paramString = paramString.substring(j), 4294967295L, -9223372036183687168L);
					if (j >= 0)
						return "Illegal character found in IPv6 scoped address: " + describeChar(paramString.charAt(j));
				}
				return null;
			}
			return "Unrecognized IPv6 address format";
		}
		int i = scan(paramString, -8935000884560003073L, -9223372035915251711L);
		if (i >= 0)
			return "Illegal character found in host: " + describeChar(paramString.charAt(i));
		return null;
	}

	private static String checkAuth(String paramString) {
		int i = scan(paramString, -9223231260711714817L, -9223372036586340352L);
		if (i >= 0)
			return "Illegal character found in authority: " + describeChar(paramString.charAt(i));
		return null;
	}

	public static String checkAuthority(URL paramURL) {
		if (paramURL == null)
			return null;
		String str1;
		String str2;
		if ((str1 = checkUserInfo(str2 = paramURL.getUserInfo())) != null)
			return str1;
		String str3;
		if ((str1 = checkHost(str3 = paramURL.getHost())) != null)
			return str1;
		if (str3 == null && str2 == null)
			return checkAuth(paramURL.getAuthority());
		return null;
	}

	public static String checkExternalForm(URL paramURL) {
		if (paramURL == null)
			return null;
		String str;
		int i = scan(str = paramURL.getUserInfo(), 140741783322623L, Long.MIN_VALUE);
		if (i >= 0)
			return "Illegal character found in authority: " + describeChar(str.charAt(i));
		if ((str = checkHostString(paramURL.getHost())) != null)
			return str;
		return null;
	}

	public static String checkHostString(String paramString) {
		if (paramString == null)
			return null;
		return null;
	}
}