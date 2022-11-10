package ayunami2000;

import java.util.List;

import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_5_R3.Packet;
import net.minecraft.server.v1_5_R3.Packet131ItemData;

public class VideoMapPacketCodecBukkit extends VideoMapPacketCodec {

	/**
	 * @param mapIds 2D grid of map IDs that make up the screen (mapIds[y][x])
	 * @param posX audio playback X coord
	 * @param posY audio playback Y coord
	 * @param posZ audio playback Z coord
	 * @param volume the volume of the clip
	 */
	public VideoMapPacketCodecBukkit(int[][] mapIds, double posX, double posY, double posZ, float volume) {
		super(mapIds, posX, posY, posZ, volume);
	}

	/**
	 * @param mapIds 2D grid of map IDs that make up the screen (mapIds[y][x])
	 * @param posX audio playback X coord
	 * @param posY audio playback Y coord
	 * @param posZ audio playback Z coord
	 */
	public VideoMapPacketCodecBukkit(int[][] mapIds, double posX, double posY, double posZ) {
		super(mapIds, posX, posY, posZ);
	}

	/**
	 * @param mapIds 2D grid of map IDs that make up the screen (mapIds[y][x])
	 */
	public VideoMapPacketCodecBukkit(int[][] mapIds) {
		super(mapIds);
	}
	
	public static class VideoMapPacket {
		protected final Object packet;
		protected VideoMapPacket(byte[] packet) {
			this(packet, false);
		}
		protected VideoMapPacket(byte[] packet, boolean image) {
			this.packet = new Packet131ItemData((short)(104 + (image ? 1 : 0)), (short)0, packet);
		}
		public Object getNativePacket() {
			return packet;
		}
		public void send(Player p) {
			nativeSendPacketToPlayer(p, packet);
		}
		public void send(Player... p) {
			for(Player pp : p) {
				nativeSendPacketToPlayer(pp, packet);
			}
		}
		public void send(List<Player> p) {
			for(Player pp : p) {
				nativeSendPacketToPlayer(pp, packet);
			}
		}
	}

	/**
	 * @param posX audio playback X coord
	 * @param posY audio playback Y coord
	 * @param posZ audio playback Z coord
	 * @param volume the volume of the clip
	 * @return packet to send to players
	 */
	public VideoMapPacket moveAudioSourceBukkit(double posX, double posY, double posZ, float volume) {
		return new VideoMapPacket(moveAudioSource(posX, posY, posZ, volume));
	}

	/**
	 * unloads video and resets all map object to vanilla renderer
	 * @return packet to send to players
	 */
	public VideoMapPacket disableVideoBukkit() {
		return new VideoMapPacket(disableVideo());
	}

	/**
	 * unloads image and resets all map object to vanilla renderer
	 * @return packet to send to players
	 */
	public VideoMapPacket disableImageBukkit() {
		return new VideoMapPacket(disableVideo(), true);
	}

	/**
	 * syncs the server side video timestamp with players
	 * @return packet to send to players
	 */
	public VideoMapPacket syncPlaybackWithPlayersBukkit() {
		return new VideoMapPacket(syncPlaybackWithPlayers());
	}

	/**
	 * syncs the server side image with players
	 * @return packet to send to players
	 */
	public VideoMapPacket syncPlaybackWithPlayersImageBukkit() {
		return new VideoMapPacket(syncPlaybackWithPlayers(), true);
	}

	/**
	 * @param url URL to an MP4 or other HTML5 supported video file
	 * @param loop If the video file should loop
	 * @param durationSeconds duration of the video in seconds
	 * @return packet to send to players
	 */
	public VideoMapPacket beginPlaybackBukkit(String url, boolean loop, float duration) {
		return new VideoMapPacket(beginPlayback(url, loop, duration));
	}

	/**
	 * @param url URL to a PNG, JPEG, GIF, or other HTML5 supported image file
	 * @return packet to send to players
	 */
	public VideoMapPacket beginPlaybackImageBukkit(String url) {
		return new VideoMapPacket(beginPlayback(url));
	}

	/**
	 * Tells the browser to pre-load a URL to a video to be played in the future
	 * @param url the URL of the video
	 * @param ttl the amount of time the video should stay loaded
	 * @return packet to send to players
	 */
	public static VideoMapPacket bufferVideoBukkit(String url, int ttl) {
		return new VideoMapPacket(bufferVideo(url, ttl));
	}

	/**
	 * Tells the browser to pre-load a URL to an image to be played in the future
	 * @param url the URL of the image
	 * @param ttl the amount of time the image should stay loaded
	 * @return packet to send to players
	 */
	public static VideoMapPacket bufferImageBukkit(String url, int ttl) {
		return new VideoMapPacket(bufferVideo(url, ttl), true);
	}

	/**
	 * @param time time in seconds to seek the video to
	 */
	public VideoMapPacket setPlaybackTimeBukkit(float time) {
		return new VideoMapPacket(setPlaybackTime(time));
	}
	
	/**
	 * @param loop video should loop
	 */
	public VideoMapPacket setLoopEnableBukkit(boolean loop) {
		return new VideoMapPacket(setLoopEnable(loop));
	}

	/**
	 * @param pause set if video should pause
	 * @return packet to send to players
	 */
	public VideoMapPacket setPausedBukkit(boolean pause) {
		return new VideoMapPacket(setPaused(pause));
	}
	
	public static void nativeSendPacketToPlayer(Player player, Object obj) {
		if(obj == null) {
			return;
		}
		((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)obj);
	}
}
