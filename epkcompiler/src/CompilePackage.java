import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.jcraft.jzlib.CRC32;
import com.jcraft.jzlib.Deflater;
import com.jcraft.jzlib.DeflaterOutputStream;
import com.jcraft.jzlib.GZIPOutputStream;

public class CompilePackage {
	
	private static ArrayList<File> files = new ArrayList();
	
	public static void main(String[] args) throws IOException {
		if(args.length < 2 || args.length > 4) {
			System.out.println("Usage: java -jar CompilePackage.jar <input directory> <output file> [gzip|zlib|none] [file-type]");
			return;
		}
		
		File root = new File(args[0]);
		File output = new File(args[1]);
		char compressionType;
		
		if(args.length > 2) {
			if(args[2].equalsIgnoreCase("gzip")) {
				compressionType = 'G';
			}else if(args[2].equalsIgnoreCase("zlib")) {
				compressionType = 'Z';
			}else if(args[2].equalsIgnoreCase("none")) {
				compressionType = '0';
			}else {
				throw new IllegalArgumentException("Unknown compression method: " + args[2]);
			}
		}else {
			compressionType = 'G';
		}
		
		listDirectory(root);
		ByteArrayOutputStream osb = new ByteArrayOutputStream();
		String start = root.getAbsolutePath();
		
		osb.write("EAGPKG$$".getBytes(Charset.forName("UTF-8")));
		
		String chars = "ver2.0";
		osb.write(chars.length());
		osb.write(chars.getBytes(StandardCharsets.US_ASCII));
		
		Date d = new Date();
		
		String comment = "\n\n #  Eagler EPK v2.0 (c) " + (new SimpleDateFormat("yyyy")).format(d) + " Calder Young\n" +
				" #  update: on " + (new SimpleDateFormat("MM/dd/yyyy")).format(d) + " at " +
				(new SimpleDateFormat("hh:mm:ss aa")).format(d) + "\n\n";

		String nm = output.getName();
		osb.write(nm.length());
		osb.write(nm.getBytes(StandardCharsets.US_ASCII));
		
		writeShort(comment.length(), osb);
		osb.write(comment.getBytes(StandardCharsets.US_ASCII));
		
		writeLong(d.getTime(), osb);
		writeInt(files.size() + 1, osb);
		
		osb.write(compressionType);
		
		OutputStream os;
		
		if(compressionType == 'G') {
			os = new GZIPOutputStream(osb, new Deflater(9, 15+16), 16384, true);
		}else if(compressionType == 'Z') {
			os = new DeflaterOutputStream(osb, new Deflater(9), 16384, true);
		}else {
			os = osb;
		}
		
		os.write("HEAD".getBytes(StandardCharsets.US_ASCII));
		String key = "file-type";
		os.write(key.length());
		os.write(key.getBytes(StandardCharsets.US_ASCII));
		String value;
		if(args.length > 3) {
			value = args[3];
		}else {
			value = "epk/resources";
		}
		writeInt(value.length(), os);
		os.write(value.getBytes(StandardCharsets.US_ASCII));
		os.write('>');
		
		CRC32 checkSum = new CRC32();
		for(File f : files) {
			InputStream stream = new FileInputStream(f);
			byte[] targetArray = new byte[(int)f.length()];
			stream.read(targetArray);
			stream.close();
			
			checkSum.reset();
			checkSum.update(targetArray, 0, targetArray.length);
			int ch = (int)checkSum.getValue();
			
			os.write("FILE".getBytes(StandardCharsets.US_ASCII));
			
			String p = f.getAbsolutePath().replace(start, "").replace('\\', '/');
			if(p.startsWith("/")) {
				p = p.substring(1);
			}
			os.write(p.length());
			os.write(p.getBytes(StandardCharsets.US_ASCII));
			writeInt(targetArray.length + 5, os);
			writeInt(ch, os);
			
			os.write(targetArray);
			os.write(':');
			os.write('>');
		}
		
		os.write("END$".getBytes(StandardCharsets.US_ASCII));
		os.close();
		
		osb.write(":::YEE:>".getBytes(StandardCharsets.US_ASCII));
		
		FileOutputStream out = new FileOutputStream(output);
		out.write(osb.toByteArray());
		out.close();
	}
	
	public static void writeShort(int i, OutputStream os) throws IOException {
		os.write((i >> 8) & 0xFF);
		os.write(i & 0xFF);
	}
	
	public static void writeInt(int i, OutputStream os) throws IOException {
		os.write((i >> 24) & 0xFF);
		os.write((i >> 16) & 0xFF);
		os.write((i >> 8) & 0xFF);
		os.write(i & 0xFF);
	}
	
	public static void writeLong(long i, OutputStream os) throws IOException {
		os.write((int)((i >> 56) & 0xFF));
		os.write((int)((i >> 48) & 0xFF));
		os.write((int)((i >> 40) & 0xFF));
		os.write((int)((i >> 32) & 0xFF));
		os.write((int)((i >> 24) & 0xFF));
		os.write((int)((i >> 16) & 0xFF));
		os.write((int)((i >> 8) & 0xFF));
		os.write((int)(i & 0xFF));
	}
	
	public static void listDirectory(File dir) {
		for(File f : dir.listFiles()) {
			if(f.isDirectory()) {
				listDirectory(f);
			}else {
				files.add(f);
			}
		}
	}
	
}
