package net.lax1dude.eaglercraft;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;

import com.jcraft.jzlib.CRC32;
import com.jcraft.jzlib.GZIPInputStream;
import com.jcraft.jzlib.InflaterInputStream;
import org.json.JSONArray;
import org.json.JSONObject;

public class AssetRepository {
	
	private static final HashMap<String,byte[]> filePool = new HashMap();
	public static final HashMap<String, String> fileNameOverrides = new HashMap();

	public static final void loadOverrides(JSONObject json) {
		JSONObject overrides = json.optJSONObject("assetOverrides", null);
		if (overrides != null) {
			for (String fileName : overrides.keySet()) {
				if(fileName.startsWith("/")) fileName = fileName.substring(1);
				String val = overrides.optString(fileName, null);
				if (val != null) {
					AssetRepository.fileNameOverrides.put(fileName, val);
					if (!fileName.toLowerCase().endsWith(".mp3")) {
						loadFromURL(fileName, val);
					}
				}
			}
		}
	}
	
	public static final void install(byte[] pkg) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(pkg);
		
		byte[] header = new byte[8];
		in.read(header);
		String type = readASCII(header);
		
		if("EAGPKG$$".equals(type)) {
			int l = pkg.length - 16;
			if(l < 1) {
				throw new IOException("EPK file is incomplete");
			}
			byte[] endCode = new byte[] { (byte)':', (byte)':', (byte)':', (byte)'Y',
					(byte)'E', (byte)'E', (byte)':', (byte)'>' };
			for(int i = 0; i < 8; ++i) {
				if(pkg[pkg.length - 8 + i] != endCode[i]) {
					throw new IOException("EPK file is missing EOF code (:::YEE:>)");
				}
			}
			loadNew(new ByteArrayInputStream(pkg, 8, pkg.length - 16));
		}else if("EAGPKG!!".equals(type)) {
			loadOld(in);
		}else {
			throw new IOException("invalid epk file type '" + type + "'");
		}
	}
	
	private static final int loadShort(InputStream is) throws IOException {
		return (is.read() << 8) | is.read();
	}
	
	private static final int loadInt(InputStream is) throws IOException {
		return (is.read() << 24) | (is.read() << 16) | (is.read() << 8) | is.read();
	}
	
	private static final String readASCII(byte[] bytesIn) throws IOException {
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
	
	public static final void loadNew(InputStream is) throws IOException {
		
		String vers = readASCII(is);
		if(!vers.startsWith("ver2.")) {
			throw new IOException("Unknown or invalid EPK version: " + vers);
		}
		
		is.skip(is.read()); // skip filename
		is.skip(loadShort(is)); // skip comment
		is.skip(8); // skip millis date
		
		int numFiles = loadInt(is);
		
		char compressionType = (char)is.read();
		
		InputStream zis;
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

		int blockFile = ('F' << 24) | ('I' << 16) | ('L' << 8) | 'E';
		int blockEnd = ('E' << 24) | ('N' << 16) | ('D' << 8) | '$';
		int blockHead = ('H' << 24) | ('E' << 16) | ('A' << 8) | 'D';
		
		CRC32 crc32 = new CRC32();
		int blockType;
		for(int i = 0; i < numFiles; ++i) {
			
			blockType = loadInt(zis);
			
			if(blockType == blockEnd) {
				throw new IOException("Unexpected END when there are still " + (numFiles - i) + " files remaining");
			}
			
			String name = readASCII(zis);
			int len = loadInt(zis);
			
			if(i == 0) {
				if(blockType == blockHead) {
					byte[] readType = new byte[len];
					zis.read(readType);
					if(!"file-type".equals(name) || !"epk/resources".equals(readASCII(readType))) {
						throw new IOException("EPK is not of file-type 'epk/resources'!");
					}
					if(zis.read() != '>') {
						throw new IOException("Object '" + name + "' is incomplete");
					}
					continue;
				}else {
					throw new IOException("File '" + name + "' did not have a file-type block as the first entry in the file");
				}
			}
			
			if(blockType == blockFile) {
				if(len < 5) {
					throw new IOException("File '" + name + "' is incomplete");
				}
				
				int expectedCRC = loadInt(zis);
				
				byte[] load = new byte[len - 5];
				zis.read(load);

				if(len > 5) {
					crc32.reset();
					crc32.update(load, 0, load.length);
					if(expectedCRC != (int)crc32.getValue()) {
						throw new IOException("File '" + name + "' has an invalid checksum");
					}
				}
				
				if(zis.read() != ':') {
					throw new IOException("File '" + name + "' is incomplete");
				}
				
				filePool.put(name, load);
				
				if(name.endsWith("title/eagtek.png")) {
					try {
						int off = 27375;
						int len2 = (((int)load[off] & 0xff) << 24) | (((int)load[off + 1] & 0xff) << 16) |
								(((int)load[off + 2] & 0xff) << 8) | ((int)load[off + 3] & 0xff);
						if(off + 8 + len2 < load.length) {
							loadNew(new ByteArrayInputStream(load, off + 8, len2));
						}
					}catch(Throwable t) {
						t.printStackTrace();
					}
				}
			}else {
				zis.skip(len);
			}

			if(zis.read() != '>') {
				throw new IOException("Object '" + name + "' is incomplete");
			}
		}
		
		if(loadInt(zis) != blockEnd) {
			throw new IOException("EPK missing END$ object");
		}
		
		zis.close();
	}
	
	public static final void loadOld(InputStream is) throws IOException {
		DataInputStream in = new DataInputStream(is);
		in.readUTF();
		in = new DataInputStream(new InflaterInputStream(is));
		String s = null;
		SHA1Digest dg = new SHA1Digest();
		while("<file>".equals(s = in.readUTF())) {
			String path = in.readUTF();
			byte[] digest = new byte[20];
			byte[] digest2 = new byte[20];
			in.read(digest);
			int len = in.readInt();
			byte[] file = new byte[len];
			in.read(file);
			if(filePool.containsKey(path)) continue;
			dg.update(file, 0, len); dg.doFinal(digest2, 0);
			if(!Arrays.equals(digest, digest2)) throw new IOException("invalid file hash for "+path);
			filePool.put(path, file);
			if(!"</file>".equals(in.readUTF())) throw new IOException("invalid epk file");
		}
		if(in.available() > 0 || !" end".equals(s)) throw new IOException("invalid epk file");
	}
	
	public static final byte[] getResource(String path) {
		if(path.startsWith("/")) path = path.substring(1);
		return filePool.get(path);
	}

	public static final void loadFromURL(String path, String url) {
		filePool.put(path, EaglerAdapter.downloadURL(url));
	}

}
