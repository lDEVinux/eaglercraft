package net.lax1dude.eaglercraft.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface IPCPacketBase {

	public void deserialize(DataInput bin) throws IOException;
	public void serialize(DataOutput bin) throws IOException;
	public int id();
	public int size();
	
	public static int strLen(String s) {
		int strlen = s.length();
		int utflen = 2;
		int c;
		
		for (int i = 0; i < strlen; ++i) {
			c = s.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				++utflen;
			} else if (c > 0x07FF) {
				utflen += 3;
			} else {
				utflen += 2;
			}
		}
		
		return utflen;
	}
	
}
