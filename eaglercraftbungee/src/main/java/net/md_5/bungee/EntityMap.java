// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee;

public class EntityMap {
	public static final int[][] entityIds;

	public static void rewrite(final byte[] packet, final int oldId, final int newId) {
		final int packetId = packet[0] & 0xFF;
		if (packetId == 29) {
			for (int pos = 2; pos < packet.length; pos += 4) {
				final int readId = readInt(packet, pos);
				if (readId == oldId) {
					setInt(packet, pos, newId);
				} else if (readId == newId) {
					setInt(packet, pos, oldId);
				}
			}
		} else {
			final int[] idArray = EntityMap.entityIds[packetId];
			if (idArray != null) {
				for (final int pos2 : idArray) {
					final int readId2 = readInt(packet, pos2);
					if (readId2 == oldId) {
						setInt(packet, pos2, newId);
					} else if (readId2 == newId) {
						setInt(packet, pos2, oldId);
					}
				}
			}
		}
		if (packetId == 23) {
			final int type = packet[5] & 0xFF;
			if (type == 60 || type == 90) {
				final int index20 = readInt(packet, 20);
				if (packet.length > 24 && index20 == oldId) {
					setInt(packet, 20, newId);
				}
			}
		}
	}

	private static void setInt(final byte[] buf, final int pos, final int i) {
		buf[pos] = (byte) (i >> 24);
		buf[pos + 1] = (byte) (i >> 16);
		buf[pos + 2] = (byte) (i >> 8);
		buf[pos + 3] = (byte) i;
	}

	private static int readInt(final byte[] buf, final int pos) {
		return (buf[pos] & 0xFF) << 24 | (buf[pos + 1] & 0xFF) << 16 | (buf[pos + 2] & 0xFF) << 8 | (buf[pos + 3] & 0xFF);
	}

	static {
		(entityIds = new int[256][])[5] = new int[] { 1 };
		EntityMap.entityIds[7] = new int[] { 1, 5 };
		EntityMap.entityIds[17] = new int[] { 1 };
		EntityMap.entityIds[18] = new int[] { 1 };
		EntityMap.entityIds[19] = new int[] { 1 };
		EntityMap.entityIds[20] = new int[] { 1 };
		EntityMap.entityIds[22] = new int[] { 1, 5 };
		EntityMap.entityIds[23] = new int[] { 1 };
		EntityMap.entityIds[24] = new int[] { 1 };
		EntityMap.entityIds[25] = new int[] { 1 };
		EntityMap.entityIds[26] = new int[] { 1 };
		EntityMap.entityIds[28] = new int[] { 1 };
		EntityMap.entityIds[30] = new int[] { 1 };
		EntityMap.entityIds[31] = new int[] { 1 };
		EntityMap.entityIds[32] = new int[] { 1 };
		EntityMap.entityIds[33] = new int[] { 1 };
		EntityMap.entityIds[34] = new int[] { 1 };
		EntityMap.entityIds[35] = new int[] { 1 };
		EntityMap.entityIds[38] = new int[] { 1 };
		EntityMap.entityIds[39] = new int[] { 1, 5 };
		EntityMap.entityIds[40] = new int[] { 1 };
		EntityMap.entityIds[41] = new int[] { 1 };
		EntityMap.entityIds[42] = new int[] { 1 };
		EntityMap.entityIds[55] = new int[] { 1 };
		EntityMap.entityIds[71] = new int[] { 1 };
	}
}
