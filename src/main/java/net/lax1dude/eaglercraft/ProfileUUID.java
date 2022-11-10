package net.lax1dude.eaglercraft;

public class ProfileUUID {

	public final long msb;
	public final long lsb;

	public ProfileUUID(long msb, long lsb) {
		this.msb = msb;
		this.lsb = lsb;
	}

	public ProfileUUID(byte[] uuid) {
		long msb = 0;
		long lsb = 0;
		for (int i = 0; i < 8; i++)
			msb = (msb << 8) | (uuid[i] & 0xff);
		for (int i = 8; i < 16; i++)
			lsb = (lsb << 8) | (uuid[i] & 0xff);
		this.msb = msb;
		this.lsb = lsb;
	}

	public ProfileUUID(String uuid) {
		String[] components = uuid.split("-");
		if (components.length != 5)
			throw new IllegalArgumentException("Invalid UUID string: " + uuid);
		for (int i = 0; i < 5; i++)
			components[i] = "0x" + components[i];

		long mostSigBits = Long.decode(components[0]).longValue();
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(components[1]).longValue();
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(components[2]).longValue();

		long leastSigBits = Long.decode(components[3]).longValue();
		leastSigBits <<= 48;
		leastSigBits |= Long.decode(components[4]).longValue();

		this.msb = mostSigBits;
		this.lsb = leastSigBits;
	}

	private static byte long7(long x) { return (byte)(x >> 56); }
	private static byte long6(long x) { return (byte)(x >> 48); }
	private static byte long5(long x) { return (byte)(x >> 40); }
	private static byte long4(long x) { return (byte)(x >> 32); }
	private static byte long3(long x) { return (byte)(x >> 24); }
	private static byte long2(long x) { return (byte)(x >> 16); }
	private static byte long1(long x) { return (byte)(x >>  8); }
	private static byte long0(long x) { return (byte)(x      ); }

	public byte[] getBytes() {
		byte[] ret = new byte[16];
		ret[0] = long7(msb);
		ret[1] = long6(msb);
		ret[2] = long5(msb);
		ret[3] = long4(msb);
		ret[4] = long3(msb);
		ret[5] = long2(msb);
		ret[6] = long1(msb);
		ret[7] = long0(msb);
		ret[8] = long7(lsb);
		ret[9] = long6(lsb);
		ret[10] = long5(lsb);
		ret[11] = long4(lsb);
		ret[12] = long3(lsb);
		ret[13] = long2(lsb);
		ret[14] = long1(lsb);
		ret[15] = long0(lsb);
		return ret;
	}

	@Override
	public String toString() {
		return (digits(msb >> 32, 8) + "-" + digits(msb >> 16, 4) + "-" + digits(msb, 4) + "-"
				+ digits(lsb >> 48, 4) + "-" + digits(lsb, 12));
	}

	private static String digits(long val, int digits) {
		long hi = 1L << (digits * 4);
		return Long.toHexString(hi | (val & (hi - 1))).substring(1);
	}

	@Override
	public int hashCode() {
		long hilo = msb ^ lsb;
		return ((int) (hilo >> 32)) ^ (int) hilo;
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof ProfileUUID) && ((ProfileUUID)o).lsb == lsb && ((ProfileUUID)o).msb == msb;
	}

}