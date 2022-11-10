package net.lax1dude.eaglercraft;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jzlib.InflaterInputStream;

public class EaglerInflater {
	
    public static byte[] uncompress(byte[] input) throws IOException {
    	return getBytesFromInputStream(new InflaterInputStream(new ByteArrayInputStream(input)));
    }
    
    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(); 
        byte[] buffer = new byte[0xFFFF];
        for (int len = is.read(buffer); len != -1; len = is.read(buffer)) { 
            os.write(buffer, 0, len);
        }
        return os.toByteArray();
    }
    
}
