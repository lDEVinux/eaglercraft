package net.lax1dude.eaglercraft.sp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.InflaterInputStream;

import com.jcraft.jzlib.DeflaterOutputStream;
import com.jcraft.jzlib.GZIPInputStream;
import com.jcraft.jzlib.GZIPOutputStream;

import net.minecraft.src.ChunkCoordIntPair;

public class MCAConverter {
	public static void convertFromMCA(VFile dir, byte[] file, String fileName) {
		VFile levelDir = new VFile(dir,
				"level" + (fileName.startsWith("region/") ? "0" : fileName.substring(3, fileName.indexOf('/'))));

		String[] xz = fileName.substring(fileName.lastIndexOf('r') + 2, fileName.length() - 4).split("\\.");
		int gx = Integer.parseInt(xz[0]);
		int gz = Integer.parseInt(xz[1]);

		try {
			byte[] buffer = new byte[16000];
			for (int x = 0; x < 32; ++x) {
				for (int z = 0; z < 32; ++z) {
					int i = ((x % 32) + (z % 32) * 32) * 4;
					int offset = (((file[i] & 0xff) << 16) | ((file[i + 1] & 0xff) << 8) | (file[i + 2] & 0xff)) * 4096;
					if (offset == 0 && file[i + 3] == 0) {
						continue;
					}
					int chunkLen = (((file[offset] & 0xff) << 24) | ((file[offset + 1] & 0xff) << 16)
							| ((file[offset + 2] & 0xff) << 8) | (file[offset + 3] & 0xff));
					if (chunkLen == 0)
						continue;
					byte compression = file[offset + 4];
					byte[] data = new byte[chunkLen - 1];
					System.arraycopy(file, offset + 5, data, 0, chunkLen - 1);
					if (compression == 0) {
						OutputStream os = new VFile(levelDir,
								VFSChunkLoader.getChunkPath(gx * 32 + x, gz * 32 + z) + ".dat").getOutputStream();
						GZIPOutputStream gos = new GZIPOutputStream(os);
						ByteArrayInputStream bais = new ByteArrayInputStream(data);
						int len;
						while ((len = bais.read(buffer)) > 0) {
							gos.write(buffer, 0, len);
						}
						gos.close();
						os.close();
						bais.close();
					} else if (compression == 2) {
						OutputStream os = new VFile(levelDir,
								VFSChunkLoader.getChunkPath(gx * 32 + x, gz * 32 + z) + ".dat").getOutputStream();
						GZIPOutputStream gos = new GZIPOutputStream(os);
						InflaterInputStream iis = new InflaterInputStream(new ByteArrayInputStream(data));
						int len;
						while ((len = iis.read(buffer)) > 0) {
							gos.write(buffer, 0, len);
						}
						gos.close();
						os.close();
						iis.close();
					} else if (compression == 1) {
						new VFile(levelDir, VFSChunkLoader.getChunkPath(gx * 32 + x, gz * 32 + z) + ".dat")
								.setAllBytes(data);
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static Map<String, byte[]> convertToMCA(Map<ChunkCoordIntPair, byte[]> regions) {
		Map<String, byte[]> regionsOut = new HashMap<>();

		if (regions.size() == 0)
			return regionsOut;

		byte[] readBuffer = new byte[16000];

		try {
			int timestamp = (int) System.currentTimeMillis();

			int maxX = Integer.MIN_VALUE;
			int maxZ = Integer.MIN_VALUE;
			int minX = Integer.MAX_VALUE;
			int minZ = Integer.MAX_VALUE;

			for (ChunkCoordIntPair coords : regions.keySet()) {
				if (maxX < coords.chunkXPos)
					maxX = coords.chunkXPos;
				if (maxZ < coords.chunkZPos)
					maxZ = coords.chunkZPos;
				if (minX > coords.chunkXPos)
					minX = coords.chunkXPos;
				if (minZ > coords.chunkZPos)
					minZ = coords.chunkZPos;
			}

			for (int z = minZ - (32 + (minZ % 32)); z <= maxZ + (32 + (maxZ % 32)); z += 32) {
				for (int x = minX - (32 + (minX % 32)); x <= maxX + (32 + (maxX % 32)); x += 32) {
					ByteArrayOutputStream offsets = new ByteArrayOutputStream();
					DataOutputStream offsetsDos = new DataOutputStream(offsets);
					ByteArrayOutputStream timestamps = new ByteArrayOutputStream();
					DataOutputStream timestampsDos = new DataOutputStream(timestamps);
					ByteArrayOutputStream chunks = new ByteArrayOutputStream();
					DataOutputStream chunksDos = new DataOutputStream(chunks);
					boolean anyChunks = false;
					for (int cz = 0; cz < 32; cz++) {
						for (int cx = 0; cx < 32; cx++) {
							int tx = x + cx;
							int tz = z + cz;

							byte[] region = regions.get(new ChunkCoordIntPair(tx, tz));
							if (region == null) {
								offsetsDos.writeInt(0);
								timestampsDos.writeInt(0);
							} else {
								anyChunks = true;

								ByteArrayInputStream bais = new ByteArrayInputStream(region);
								GZIPInputStream gis = new GZIPInputStream(bais);
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								DeflaterOutputStream dos = new DeflaterOutputStream(baos);
								int len;
								while ((len = gis.read(readBuffer)) > 0) {
									dos.write(readBuffer, 0, len);
								}
								dos.close();
								baos.close();
								bais.close();
								gis.close();
								byte[] zlibbed = baos.toByteArray();

								int offset = 2 + (chunksDos.size() / 4096);
								offsetsDos.write((offset >> 16) & 0xff);
								offsetsDos.write((offset >> 8) & 0xff);
								offsetsDos.write(offset & 0xff);
								offsetsDos.write((int) Math.ceil((5 + zlibbed.length) / 4096.0));

								timestampsDos.writeInt(timestamp);

								chunksDos.writeInt(region.length);
								chunksDos.write(2);
								chunksDos.write(zlibbed);

								int chunksSizeOff = chunksDos.size() % 4096;
								if (chunksSizeOff != 0)
									chunksDos.write(new byte[4096 - chunksSizeOff]);
							}
						}
					}

					offsetsDos.close();
					timestampsDos.close();
					chunksDos.close();

					if (!anyChunks)
						continue;

					byte[] offsetsOut = offsets.toByteArray();
					byte[] timestampsOut = timestamps.toByteArray();
					byte[] chunksOut = chunks.toByteArray();

					byte[] regionFile = new byte[offsetsOut.length + timestampsOut.length + chunksOut.length];
					System.arraycopy(offsetsOut, 0, regionFile, 0, offsetsOut.length);
					System.arraycopy(timestampsOut, 0, regionFile, offsetsOut.length, timestampsOut.length);
					System.arraycopy(chunksOut, 0, regionFile, offsetsOut.length + timestampsOut.length,
							chunksOut.length);
					regionsOut.put("r." + (x / 32) + "." + (z / 32), regionFile);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return regionsOut;
	}
}
