package net.lax1dude.eaglercraft.sp.relay;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream extends InputStream {
	
	private final ByteBuffer buffer;
	
	public ByteBufferInputStream(ByteBuffer buf) {
		buffer = buf;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int max = buffer.remaining();
		if(len > max) {
			len = max;
		}
		buffer.get(b, off, len);
		return len;
	}
	
	@Override
	public int read() throws IOException {
		if(buffer.remaining() == 0) {
			return -1;
		}else {
			return (int)buffer.get() & 0xFF;
		}
	}

	@Override
	public long skip(long n) throws IOException {
		int max = buffer.remaining();
		if(n > max) {
			n = (int)max;
		}
		return max;
	}

	@Override
	public int available() throws IOException {
		return buffer.remaining();
	}

	@Override
	public synchronized void mark(int readlimit) {
		buffer.mark();
	}

	@Override
	public synchronized void reset() throws IOException {
		buffer.reset();
	}

	@Override
	public boolean markSupported() {
		return true;
	}

}
