package net.lax1dude.eaglercraft.sp;

import java.util.zip.Checksum;

public class CRC32 implements Checksum {
	private com.jcraft.jzlib.CRC32 impl = new com.jcraft.jzlib.CRC32();
	long tbytes;

	@Override
	public long getValue() {
		return impl.getValue();
	}

	@Override
	public void reset() {
		impl.reset();
		tbytes = 0;
	}

	@Override
	public void update(int val) {
		impl.update(new byte[] { (byte) val }, 0, 1);
	}

	public void update(byte[] buf) {
		update(buf, 0, buf.length);
	}

	@Override
	public void update(byte[] buf, int off, int nbytes) {
		// avoid int overflow, check null buf
		if (off <= buf.length && nbytes >= 0 && off >= 0 && buf.length - off >= nbytes) {
			impl.update(buf, off, nbytes);
			tbytes += nbytes;
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}
}
