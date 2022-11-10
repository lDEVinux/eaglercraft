package net.lax1dude.eaglercraft;

public class NoCatchParse {

	public static final int INT_EXCEPTION = Integer.MIN_VALUE;
	public static final float FLOAT_EXCEPTION = Float.NaN;
	public static final double DOUBLE_EXCEPTION = Double.NaN;

	public static int parseInt(String s) {
		return parseInt(s, 10, false, INT_EXCEPTION);
	}

	public static int parseInt(String s, int radix) {
		return parseInt(s, radix, false, INT_EXCEPTION);
	}

	public static int parseInt(String s, int radix, boolean log) {
		return parseInt(s, radix, log, INT_EXCEPTION);
	}

	public static int parseInt(String s, int radix, boolean log, int exceptionResult) {
		if (s == null) {
			if (log) {
				System.err.println("parseInt: string was null");
			}
			return exceptionResult;
		}

		if (s.isEmpty()) {
			if (log) {
				System.err.println("parseInt: string was empty");
			}
			return exceptionResult;
		}

		if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX) {
			if (log) {
				System.err.println("parseInt: invalid radix '" + radix + "'");
			}
			return exceptionResult;
		}

		tryFail: {
			int result = 0;
			boolean negative = false;
			int i = 0, len = s.length();
			int limit = -Integer.MAX_VALUE;
			int multmin;
			int digit;

			if (len > 0) {
				char firstChar = s.charAt(0);
				if (firstChar < '0') { // Possible leading "+" or "-"
					if (firstChar == '-') {
						negative = true;
						limit = Integer.MIN_VALUE;
					} else if (firstChar != '+')
						break tryFail;

					if (len == 1)
						break tryFail;
					i++;
				}
				multmin = limit / radix;
				while (i < len) {
					// Accumulating negatively avoids surprises near MAX_VALUE
					digit = Character.digit(s.charAt(i++), radix);
					if (digit < 0 || result < multmin) {

						break tryFail;
					}
					result *= radix;
					if (result < limit + digit) {
						break tryFail;
					}
					result -= digit;
				}
			} else {
				break tryFail;
			}
			int ret = negative ? result : -result;
			if (ret == exceptionResult) {
				System.err.println(
						"parseInt: number '" + s + "' was parsed successfully but it is equal to exceptionResult");
			}
			return ret;
		}
		if (log) {
			System.err.println("parseInt: cannot parse '" + s + "'");
		}
		return exceptionResult;
	}

	public static double parseDouble(String s) {
		return parseDouble(s, false, DOUBLE_EXCEPTION);
	}

	public static double parseDouble(String s, boolean log) {
		return parseDouble(s, log, DOUBLE_EXCEPTION);
	}

	public static double parseDouble(String s, boolean log, double exceptionResult) {
		if (s == null) {
			if (log) {
				System.err.println("parseDouble: string was null");
			}
			return exceptionResult;
		}

		if (s.isEmpty()) {
			if (log) {
				System.err.println("parseDouble: string was empty");
			}
			return exceptionResult;
		}

		tryFail: {
			int start = 0;
			int end = s.length();
			while (s.charAt(start) <= ' ') {
				if (++start == end) {
					break tryFail;
				}
			}
			while (s.charAt(end - 1) <= ' ') {
				--end;
			}

			boolean negative = false;
			int index = start;
			if (s.charAt(index) == '-') {
				++index;
				negative = true;
			} else if (s.charAt(index) == '+') {
				++index;
			}
			if (index == end) {
				break tryFail;
			}
			char c = s.charAt(index);

			long mantissa = 0;
			int exp = 0;
			boolean hasOneDigit = false;
			if (c != '.') {
				hasOneDigit = true;
				if (c < '0' || c > '9') {
					break tryFail;
				}
				while (index < end && s.charAt(index) == '0') {
					++index;
				}
				while (index < end) {
					c = s.charAt(index);
					if (c < '0' || c > '9') {
						break;
					}
					if (mantissa < Long.MAX_VALUE / 10 - 9) {
						mantissa = mantissa * 10 + (c - '0');
					} else {
						++exp;
					}
					++index;
				}
			}
			if (index < end && s.charAt(index) == '.') {
				++index;
				while (index < end) {
					c = s.charAt(index);
					if (c < '0' || c > '9') {
						break;
					}
					if (mantissa < Long.MAX_VALUE / 10 - 9) {
						mantissa = mantissa * 10 + (c - '0');
						--exp;
					}
					++index;
					hasOneDigit = true;
				}
				if (!hasOneDigit) {
					break tryFail;
				}
			}
			if (index < end) {
				c = s.charAt(index);
				if (c != 'e' && c != 'E') {
					break tryFail;
				}
				++index;
				boolean negativeExp = false;
				if (index == end) {
					break tryFail;
				}
				if (s.charAt(index) == '-') {
					++index;
					negativeExp = true;
				} else if (s.charAt(index) == '+') {
					++index;
				}
				int numExp = 0;
				hasOneDigit = false;
				while (index < end) {
					c = s.charAt(index);
					if (c < '0' || c > '9') {
						break;
					}
					numExp = 10 * numExp + (c - '0');
					hasOneDigit = true;
					++index;
				}
				if (!hasOneDigit) {
					break tryFail;
				}
				if (negativeExp) {
					numExp = -numExp;
				}
				exp += numExp;
			}
			if (exp > 308 || exp == 308 && mantissa > 17976931348623157L) {
				return !negative ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
			}
			if (negative) {
				mantissa = -mantissa;
			}
			return mantissa * doubleDecimalExponent(exp);
		}
		if (log) {
			System.err.println("parseDouble: cannot parse '" + s + "'");
		}
		return exceptionResult;
	}

	public static double doubleDecimalExponent(int n) {
		double d;
		if (n < 0) {
			d = 0.1;
			n = -n;
		} else {
			d = 10;
		}
		double result = 1;
		while (n != 0) {
			if (n % 2 != 0) {
				result *= d;
			}
			d *= d;
			n /= 2;
		}
		return result;
	}

	public static float parseFloat(String s) {
		return parseFloat(s, false, FLOAT_EXCEPTION);
	}

	public static float parseFloat(String s, boolean log) {
		return parseFloat(s, log, FLOAT_EXCEPTION);
	}

	public static float parseFloat(String s, boolean log, float exceptionResult) {
		if (s == null) {
			if (log) {
				System.err.println("parseFloat: string was null");
			}
			return exceptionResult;
		}

		if (s.isEmpty()) {
			if (log) {
				System.err.println("parseFloat: string was empty");
			}
			return exceptionResult;
		}

		tryFail: {
			int start = 0;
			int end = s.length();
			while (s.charAt(start) <= ' ') {
				if (++start == end) {
					break tryFail;
				}
			}
			while (s.charAt(end - 1) <= ' ') {
				--end;
			}

			boolean negative = false;
			int index = start;
			if (s.charAt(index) == '-') {
				++index;
				negative = true;
			} else if (s.charAt(index) == '+') {
				++index;
			}
			if (index == end) {
				break tryFail;
			}
			char c = s.charAt(index);

			int mantissa = 0;
			int exp = 0;

			boolean hasOneDigit = false;
			if (c != '.') {
				hasOneDigit = true;
				if (c < '0' || c > '9') {
					break tryFail;
				}

				while (index < end && s.charAt(index) == '0') {
					++index;
				}
				while (index < end) {
					c = s.charAt(index);
					if (c < '0' || c > '9') {
						break;
					}
					if (mantissa < (Integer.MAX_VALUE / 10) - 9) {
						mantissa = mantissa * 10 + (c - '0');
					} else {
						++exp;
					}
					++index;
				}
			}

			if (index < end && s.charAt(index) == '.') {
				++index;
				while (index < end) {
					c = s.charAt(index);
					if (c < '0' || c > '9') {
						break;
					}
					if (mantissa < (Integer.MAX_VALUE / 10) - 9) {
						mantissa = mantissa * 10 + (c - '0');
						--exp;
					}
					++index;
					hasOneDigit = true;
				}
				if (!hasOneDigit) {
					break tryFail;
				}
			}
			if (index < end) {
				c = s.charAt(index);
				if (c != 'e' && c != 'E') {
					break tryFail;
				}
				++index;
				boolean negativeExp = false;
				if (index == end) {
					break tryFail;
				}
				if (s.charAt(index) == '-') {
					++index;
					negativeExp = true;
				} else if (s.charAt(index) == '+') {
					++index;
				}
				int numExp = 0;
				hasOneDigit = false;
				while (index < end) {
					c = s.charAt(index);
					if (c < '0' || c > '9') {
						break;
					}
					numExp = 10 * numExp + (c - '0');
					hasOneDigit = true;
					++index;
				}
				if (!hasOneDigit) {
					break tryFail;
				}
				if (negativeExp) {
					numExp = -numExp;
				}
				exp += numExp;
			}
			if (exp > 38 || exp == 38 && mantissa > 34028234) {
				return !negative ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
			}
			if (negative) {
				mantissa = -mantissa;
			}
			return mantissa * floatDecimalExponent(exp);
		}
		if (log) {
			System.err.println("parseFloat: cannot parse '" + s + "'");
		}
		return exceptionResult;
	}

	private static float floatDecimalExponent(int n) {
		double d;
		if (n < 0) {
			d = 0.1;
			n = -n;
		} else {
			d = 10;
		}
		double result = 1;
		while (n != 0) {
			if (n % 2 != 0) {
				result *= d;
			}
			d *= d;
			n /= 2;
		}
		return (float) result;
	}
}
