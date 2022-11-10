package net.lax1dude.eaglercraft.sp.ipc;

import java.io.IOException;
import java.io.OutputStream;

public class IPCOutputStream extends OutputStream {

	private String className = null;
	private byte[] currentBuffer = null;
	private int idx = 0;
	private int originalSize = 0;
	
	public void feedBuffer(byte[] buf, String clazzName) {
		currentBuffer = buf;
		idx = 0;
		originalSize = buf.length;
		className = clazzName;
	}
	
	public byte[] returnBuffer() {
		if(className != null && currentBuffer.length != originalSize) {
			System.err.println("WARNING: Packet '" + className + "' was supposed to be " + originalSize + " bytes but buffer has grown by " + (currentBuffer.length - originalSize) + " to " + currentBuffer.length + " bytes");
		}
		return currentBuffer;
	}
	
	void growBuffer(int i) {
		int ii = currentBuffer.length;
		int iii = i - ii;
		if(iii > 0) {
			byte[] n = new byte[i];
			System.arraycopy(currentBuffer, 0, n, 0, ii);
			currentBuffer = n;
		}
	}

	@Override
	public void write(int b) throws IOException {
		if(idx >= currentBuffer.length) {
			growBuffer(idx + 1);
		}
		currentBuffer[idx++] = (byte) b;
	}
	
	@Override
	public void write(byte b[], int off, int len) throws IOException {
		if(idx + len > currentBuffer.length) {
			growBuffer(idx + len);
		}
		System.arraycopy(b, off, currentBuffer, idx, len);
		idx += len;
	}

}
