package net.lax1dude.eaglercraft.sp;

import static java.util.zip.Deflater.BEST_COMPRESSION;
import static java.util.zip.Deflater.DEFAULT_COMPRESSION;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import com.jcraft.jzlib.Deflater;
import com.jcraft.jzlib.DeflaterOutputStream;

public class ZipOutputStream extends DeflaterOutputStream {
	long LOCSIG = 0x4034b50;
	long EXTSIG = 0x8074b50;
	long CENSIG = 0x2014b50;
	long ENDSIG = 0x6054b50;

	int LOCHDR = 30;
	int EXTHDR = 16;

	public static final int DEFLATED = 8;
	public static final int STORED = 0;

	static final int ZIPDataDescriptorFlag = 8;
	static final int ZIPLocalHeaderVersionNeeded = 20;
	private String comment;
	private final List<String> entries = new ArrayList<>();
	private int compressMethod = DEFLATED;
	private int compressLevel = -1;
	private ByteArrayOutputStream cDir = new ByteArrayOutputStream();
	private ZipEntry currentEntry;
	private final CRC32 crc = new CRC32();
	private int offset;
	private int curOffset;
	private int nameLength;
	private byte[] nameBytes;

	public ZipOutputStream(OutputStream p1) throws IOException {
		super(p1, new Deflater(-1, true));
	}

	@Override
	public void close() throws IOException {
		if (out != null) {
			finish();
			out.close();
			out = null;
		}
	}

	public void closeEntry() throws IOException {
		if (cDir == null) {
			throw new IOException();
		}
		if (currentEntry == null) {
			return;
		}
		if (currentEntry.getMethod() == DEFLATED) {
			super.finish();
		}

		// Verify values for STORED types
		if (currentEntry.getMethod() == STORED) {
			if (crc.getValue() != currentEntry.getCrc()) {
				throw new ZipException();
			}
			if (currentEntry.getSize() != crc.tbytes) {
				throw new ZipException();
			}
		}
		curOffset = LOCHDR;

		// Write the DataDescriptor
		if (currentEntry.getMethod() != STORED) {
			curOffset += EXTHDR;
			writeLong(out, EXTSIG);
			currentEntry.setCrc(crc.getValue());
			writeLong(out, currentEntry.getCrc());
			currentEntry.setCompressedSize(deflater.getTotalOut());
			writeLong(out, currentEntry.getCompressedSize());
			currentEntry.setSize(deflater.getTotalIn());
			writeLong(out, currentEntry.getSize());
		}
		// Update the CentralDirectory
		writeLong(cDir, CENSIG);
		writeShort(cDir, ZIPLocalHeaderVersionNeeded); // Version created
		writeShort(cDir, ZIPLocalHeaderVersionNeeded); // Version to extract
		writeShort(cDir, currentEntry.getMethod() == STORED ? 0 : ZIPDataDescriptorFlag);
		writeShort(cDir, currentEntry.getMethod());
		writeShort(cDir, (int) currentEntry.getTime());
		writeShort(cDir, 0);
		writeLong(cDir, crc.getValue());
		if (currentEntry.getMethod() == DEFLATED) {
			curOffset += writeLong(cDir, deflater.getTotalOut());
			writeLong(cDir, deflater.getTotalIn());
		} else {
			curOffset += writeLong(cDir, crc.tbytes);
			writeLong(cDir, crc.tbytes);
		}
		curOffset += writeShort(cDir, nameLength);
		if (currentEntry.getExtra() != null) {
			curOffset += writeShort(cDir, currentEntry.getExtra().length);
		} else {
			writeShort(cDir, 0);
		}
		String c = currentEntry.getComment();
		writeShort(cDir, c != null ? c.length() : 0);
		writeShort(cDir, 0); // Disk Start
		writeShort(cDir, 0); // Internal File Attributes
		writeLong(cDir, 0); // External File Attributes
		writeLong(cDir, offset);
		cDir.write(nameBytes);
		nameBytes = null;
		if (currentEntry.getExtra() != null) {
			cDir.write(currentEntry.getExtra());
		}
		offset += curOffset;
		if (c != null) {
			cDir.write(c.getBytes());
		}
		currentEntry = null;
		crc.reset();
		deflater.end();
		deflater.init(-1, true);
	}

	@Override
	public void finish() throws IOException {
		if (out == null) {
			throw new IOException();
		}
		if (cDir == null) {
			return;
		}
		if (entries.size() == 0) {
			throw new ZipException();
		}
		if (currentEntry != null) {
			closeEntry();
		}
		int cdirSize = cDir.size();
		// Write Central Dir End
		writeLong(cDir, ENDSIG);
		writeShort(cDir, 0); // Disk Number
		writeShort(cDir, 0); // Start Disk
		writeShort(cDir, entries.size()); // Number of entries
		writeShort(cDir, entries.size()); // Number of entries
		writeLong(cDir, cdirSize); // Size of central dir
		writeLong(cDir, offset); // Offset of central dir
		if (comment != null) {
			writeShort(cDir, comment.length());
			cDir.write(comment.getBytes());
		} else {
			writeShort(cDir, 0);
		}
		// Write the central dir
		out.write(cDir.toByteArray());
		cDir = null;

	}

	public void putNextEntry(ZipEntry ze) throws IOException {
		if (currentEntry != null) {
			closeEntry();
		}
		if (ze.getMethod() == STORED || (compressMethod == STORED && ze.getMethod() == -1)) {
			if (ze.getCrc() == -1) {
				throw new ZipException("Crc mismatch");
			}
			if (ze.getSize() == -1 && ze.getCompressedSize() == -1) {
				throw new ZipException("Size mismatch");
			}
			if (ze.getSize() != ze.getCompressedSize() && ze.getCompressedSize() != -1 && ze.getSize() != -1) {
				throw new ZipException("Size mismatch");
			}
		}
		if (cDir == null) {
			throw new IOException("Stream is closed");
		}
		if (entries.contains(ze.getName())) {
			throw new ZipException("Entry already exists: " + ze.getName());
		}
		nameLength = utf8Count(ze.getName());
		if (nameLength > 0xffff) {
			throw new IllegalArgumentException("Name too long: " + ze.getName());
		}

		deflater.params(compressLevel, 0);
		currentEntry = ze;
		entries.add(currentEntry.getName());
		if (currentEntry.getMethod() == -1) {
			currentEntry.setMethod(compressMethod);
		}
		writeLong(out, LOCSIG); // Entry header
		writeShort(out, ZIPLocalHeaderVersionNeeded); // Extraction version
		writeShort(out, currentEntry.getMethod() == STORED ? 0 : ZIPDataDescriptorFlag);
		writeShort(out, currentEntry.getMethod());
		if (currentEntry.getTime() == -1) {
			currentEntry.setTime(System.currentTimeMillis());
		}
		writeShort(out, (int) currentEntry.getTime());
		writeShort(out, 0);

		if (currentEntry.getMethod() == STORED) {
			if (currentEntry.getSize() == -1) {
				currentEntry.setSize(currentEntry.getCompressedSize());
			} else if (currentEntry.getCompressedSize() == -1) {
				currentEntry.setCompressedSize(currentEntry.getSize());
			}
			writeLong(out, currentEntry.getCrc());
			writeLong(out, currentEntry.getSize());
			writeLong(out, currentEntry.getSize());
		} else {
			writeLong(out, 0);
			writeLong(out, 0);
			writeLong(out, 0);
		}
		writeShort(out, nameLength);
		writeShort(out, currentEntry.getExtra() != null ? currentEntry.getExtra().length : 0);
		nameBytes = toUTF8Bytes(currentEntry.getName(), nameLength);
		out.write(nameBytes);
		if (currentEntry.getExtra() != null) {
			out.write(currentEntry.getExtra());
		}
	}

	public void setComment(String comment) {
		if (comment.length() > 0xFFFF) {
			throw new IllegalArgumentException();
		}
		this.comment = comment;
	}

	public void setLevel(int level) {
		if (level < DEFAULT_COMPRESSION || level > BEST_COMPRESSION) {
			throw new IllegalArgumentException();
		}
		compressLevel = level;
	}

	public void setMethod(int method) {
		if (method != STORED && method != DEFLATED) {
			throw new IllegalArgumentException();
		}
		compressMethod = method;

	}

	private long writeLong(OutputStream os, long i) throws IOException {
		// Write out the long value as an unsigned int
		os.write((int) (i & 0xFF));
		os.write((int) (i >> 8) & 0xFF);
		os.write((int) (i >> 16) & 0xFF);
		os.write((int) (i >> 24) & 0xFF);
		return i;
	}

	private int writeShort(OutputStream os, int i) throws IOException {
		os.write(i & 0xFF);
		os.write((i >> 8) & 0xFF);
		return i;

	}

	/**
	 * Writes data for the current entry to the underlying stream.
	 *
	 * @exception IOException If an error occurs writing to the stream
	 */
	@Override
	public void write(byte[] buffer, int off, int nbytes) throws IOException {
		// avoid int overflow, check null buf
		if ((off < 0 || (nbytes < 0) || off > buffer.length) || (buffer.length - off < nbytes)) {
			throw new IndexOutOfBoundsException();
		}

		if (currentEntry == null) {
			throw new ZipException("No active entry");
		}

		if (currentEntry.getMethod() == STORED) {
			out.write(buffer, off, nbytes);
		} else {
			super.write(buffer, off, nbytes);
		}
		crc.update(buffer, off, nbytes);
	}

	static int utf8Count(String value) {
		int total = 0;
		for (int i = value.length(); --i >= 0;) {
			char ch = value.charAt(i);
			if (ch < 0x80) {
				total++;
			} else if (ch < 0x800) {
				total += 2;
			} else {
				total += 3;
			}
		}
		return total;
	}

	static byte[] toUTF8Bytes(String value, int length) {
		byte[] result = new byte[length];
		int pos = result.length;
		for (int i = value.length(); --i >= 0;) {
			char ch = value.charAt(i);
			if (ch < 0x80) {
				result[--pos] = (byte) ch;
			} else if (ch < 0x800) {
				result[--pos] = (byte) (0x80 | (ch & 0x3f));
				result[--pos] = (byte) (0xc0 | (ch >> 6));
			} else {
				result[--pos] = (byte) (0x80 | (ch & 0x3f));
				result[--pos] = (byte) (0x80 | ((ch >> 6) & 0x3f));
				result[--pos] = (byte) (0xe0 | (ch >> 12));
			}
		}
		return result;
	}
}
