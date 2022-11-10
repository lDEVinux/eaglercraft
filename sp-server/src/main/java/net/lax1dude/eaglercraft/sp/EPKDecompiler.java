package net.lax1dude.eaglercraft.sp;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.jcraft.jzlib.CRC32;
import com.jcraft.jzlib.GZIPInputStream;
import com.jcraft.jzlib.InflaterInputStream;

public class EPKDecompiler {
	
	public static class FileEntry {
		public final String type;
		public final String name;
		public final byte[] data;
		protected FileEntry(String type, String name, byte[] data) {
			this.type = type;
			this.name = name;
			this.data = data;
		}
	}
	
	private ByteArrayInputStream in2;
	private DataInputStream in;
	private InputStream zis;
	private SHA1Digest dg;
	private CRC32 crc32;
	private int numFiles;
	private boolean isFinished = false;
	private boolean isOldFormat = false;
	
	public EPKDecompiler(byte[] data) throws IOException {
		in2 = new ByteArrayInputStream(data);
		
		byte[] header = new byte[8];
		in2.read(header);
		
		if(Arrays.equals(header, new byte[]{(byte)69,(byte)65,(byte)71,(byte)80,(byte)75,(byte)71,(byte)36,(byte)36})) {
			byte[] endCode = new byte[] { (byte)':', (byte)':', (byte)':', (byte)'Y',
					(byte)'E', (byte)'E', (byte)':', (byte)'>' };
			for(int i = 0; i < 8; ++i) {
				if(data[data.length - 8 + i] != endCode[i]) {
					throw new IOException("EPK file is missing EOF code (:::YEE:>)");
				}
			}
			in2 = new ByteArrayInputStream(data, 8, data.length - 16);
			initNew();
		}else if(Arrays.equals(header, new byte[]{(byte)69,(byte)65,(byte)71,(byte)80,(byte)75,(byte)71,(byte)33,(byte)33})) {
			initOld();
		}
		
	}
	
	public boolean isOld() {
		return isOldFormat;
	}
	
	public FileEntry readFile() throws IOException {
		if(!isOldFormat) {
			return readFileNew();
		}else {
			return readFileOld();
		}
	}
	
	private void initNew() throws IOException {
		InputStream is = in2;
		
		String vers = readASCII(is);
		if(!vers.startsWith("ver2.")) {
			throw new IOException("Unknown or invalid EPK version: " + vers);
		}
		
		is.skip(is.read()); // skip filename
		is.skip(loadShort(is)); // skip comment
		is.skip(8); // skip millis date
		
		numFiles = loadInt(is);
		
		char compressionType = (char)is.read();
		
		switch(compressionType) {
		case 'G':
			zis = new GZIPInputStream(is);
			break;
		case 'Z':
			zis = new InflaterInputStream(is);
			break;
		case '0':
			zis = is;
			break;
		default:
			throw new IOException("Invalid or unsupported EPK compression: " + compressionType);
		}
		
		crc32 = new CRC32();
		
	}
	
	private FileEntry readFileNew() throws IOException {
		if(isFinished) {
			return null;
		}
		
		byte[] typeBytes = new byte[4];
		zis.read(typeBytes);
		String type = readASCII(typeBytes);
		
		if(numFiles == 0) {
			if(!"END$".equals(type)) {
				throw new IOException("EPK file is missing END code (END$)");
			}
			isFinished = true;
			return null;
		}else {
			if("END$".equals(type)) {
				throw new IOException("Unexpected END when there are still " + numFiles + " files remaining");
			}else {
				String name = readASCII(zis);
				int len = loadInt(zis);
				byte[] data;
				
				if("FILE".equals(type)) {
					if(len < 5) {
						throw new IOException("File '" + name + "' is incomplete (no crc)");
					}
					
					int loadedCrc = loadInt(zis);
					
					data = new byte[len - 5];
					zis.read(data);
					
					crc32.reset();
					crc32.update(data, 0, data.length);
					if((int)crc32.getValue() != loadedCrc) {
						throw new IOException("File '" + name + "' has an invalid checksum");
					}
					
					if(zis.read() != ':') {
						throw new IOException("File '" + name + "' is incomplete");
					}
				}else {
					data = new byte[len];
					zis.read(data);
				}
				
				if(zis.read() != '>') {
					throw new IOException("Object '" + name + "' is incomplete");
				}
				
				--numFiles;
				return new FileEntry(type, name, data);
			}
		}
	}
	
	private static final int loadShort(InputStream is) throws IOException {
		return (is.read() << 8) | is.read();
	}
	
	private static final int loadInt(InputStream is) throws IOException {
		return (is.read() << 24) | (is.read() << 16) | (is.read() << 8) | is.read();
	}
	
	public static final String readASCII(byte[] bytesIn) throws IOException {
		char[] charIn = new char[bytesIn.length];
		for(int i = 0; i < bytesIn.length; ++i) {
			charIn[i] = (char)((int)bytesIn[i] & 0xFF);
		}
		return new String(charIn);
	}
	
	private static final String readASCII(InputStream bytesIn) throws IOException {
		int len = bytesIn.read();
		char[] charIn = new char[len];
		for(int i = 0; i < len; ++i) {
			charIn[i] = (char)(bytesIn.read() & 0xFF);
		}
		return new String(charIn);
	}
	
	private void initOld() throws IOException {
		isOldFormat = true;
		dg = new SHA1Digest();
		in = new DataInputStream(in2);
		in.readUTF();
		in = new DataInputStream(new InflaterInputStream(in2));
	}
	
	private FileEntry readFileOld() throws IOException {
		if(isFinished) {
			return null;
		}
		String s = in.readUTF();
		if(s.equals(" end")) {
			isFinished = true;
			return null;
		}else if(!s.equals("<file>")) {
			throw new IOException("invalid epk file");
		}
		String path = in.readUTF();
		byte[] digest = new byte[20];
		byte[] digest2 = new byte[20];
		in.read(digest);
		int len = in.readInt();
		byte[] file = new byte[len];
		in.read(file);
		dg.update(file, 0, len); dg.doFinal(digest2, 0);
		if(!Arrays.equals(digest, digest2)) throw new IOException("invalid file hash for "+path);
		if(!"</file>".equals(in.readUTF())) throw new IOException("invalid epk file");
		return new FileEntry("FILE", path, file);
	}
	
}
