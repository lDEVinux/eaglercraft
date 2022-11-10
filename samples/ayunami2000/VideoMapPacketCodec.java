package ayunami2000;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class VideoMapPacketCodec {
	
	public final int[][] mapIds;
	private boolean loop;
	private String url;
	private int duration;
	private long timestamp;
	private long pauseTimestamp;
	private double posX;
	private double posY;
	private double posZ;
	private float volume;
	private int frameRate;
	private boolean requiresFullResetPacket;
	private boolean requiresPositionPacket;
	private boolean isDisabled;
	
	/**
	 * @param mapIds 2D grid of map IDs that make up the screen (mapIds[y][x])
	 * @param posX audio playback X coord
	 * @param posY audio playback Y coord
	 * @param posZ audio playback Z coord
	 * @param volume the volume of the clip
	 */
	public VideoMapPacketCodec(int[][] mapIds, double posX, double posY, double posZ, float volume) {
		this.mapIds = mapIds;
		this.url = null;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.volume = volume;
		this.frameRate = 30;
		this.requiresPositionPacket = true;
		this.requiresFullResetPacket = true;
		this.isDisabled = true;
	}

	/**
	 * @param mapIds 2D grid of map IDs that make up the screen (mapIds[y][x])
	 * @param posX audio playback X coord
	 * @param posY audio playback Y coord
	 * @param posZ audio playback Z coord
	 */
	public VideoMapPacketCodec(int[][] mapIds, double posX, double posY, double posZ) {
		this(mapIds, posX, posY, posZ, 0.5f);
	}

	/**
	 * @param mapIds 2D grid of map IDs that make up the screen (mapIds[y][x])
	 */
	public VideoMapPacketCodec(int[][] mapIds) {
		this(mapIds, 0, 100, 0, 0.5f);
	}

	/**
	 * @param posX audio playback X coord
	 * @param posY audio playback Y coord
	 * @param posZ audio playback Z coord
	 * @param volume the volume of the clip
	 * @return packet to send to players
	 */
	public byte[] moveAudioSource(double posX, double posY, double posZ, float volume) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.volume = volume;
		this.requiresPositionPacket = true;
		return syncPlaybackWithPlayers();
	}
	
	/**
	 * unloads video and resets all map object to vanilla renderer
	 * @return packet to send to players
	 */
	public byte[] disableVideo() {
		isDisabled = true;
		return syncPlaybackWithPlayers();
	}
	
	/**
	 * syncs the server side video timestamp with players
	 * @return packet to send to players
	 */
	public byte[] syncPlaybackWithPlayers() {
		try {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			DataOutputStream str = new DataOutputStream(bao);
			
			if(isDisabled) {
				str.write(0);
				int x = mapIds[0].length;
				int y = mapIds.length;
				str.write((x << 4) | y);
				for(int yy = 0; yy < y; ++yy) {
					for(int xx = 0; xx < x; ++xx) {
						str.writeShort(mapIds[yy][xx]);
					}
				}
				return bao.toByteArray();
			}
			
			int packetType = 1;
			if(requiresFullResetPacket) {
				packetType = packetType | 2;
			}
			if(requiresFullResetPacket || requiresPositionPacket) {
				packetType = packetType | 4;
			}
			
			str.write(packetType);
			
			if(requiresFullResetPacket) {
				int x = mapIds[0].length;
				int y = mapIds.length;
				str.write((x << 4) | y);
				for(int yy = 0; yy < y; ++yy) {
					for(int xx = 0; xx < x; ++xx) {
						str.writeShort(mapIds[yy][xx]);
					}
				}
				str.write(frameRate);
				str.writeInt(duration);
				str.writeUTF(url);
			}
			
			if(requiresFullResetPacket || requiresPositionPacket) {
				str.writeFloat(volume);
				str.writeDouble(posX);
				str.writeDouble(posY);
				str.writeDouble(posZ);
			}
			
			str.writeInt(getElapsedMillis());
			str.writeBoolean(loop);
			str.writeBoolean(pauseTimestamp > 0l);
			
			requiresFullResetPacket = false;
			requiresPositionPacket = false;
				
			return bao.toByteArray();
		}catch(IOException e) {
			throw new RuntimeException("serialization error", e);
		}
	}
	
	/**
	 * this is dual purpose, it calculates elapsed time but also loops or pauses the video if it is finished playing
	 */
	private int getElapsedMillis() {
		if(pauseTimestamp > 0l) {
			return (int)(pauseTimestamp - timestamp);
		}
		int t = (int)(System.currentTimeMillis() - timestamp);
		if(loop) {
			while(t > duration) {
				t -= duration;
				timestamp += duration;
			}
		}else {
			if(t > duration) {
				timestamp = (int)(System.currentTimeMillis() - duration);
				return duration;
			}
		}
		return t;
	}
	
	/**
	 * @param url URL to an MP4 or other HTML5 supported video file
	 * @param loop If the video file should loop
	 * @param duration duration of the video in seconds
	 * @return packet to send to players
	 */
	public byte[] beginPlayback(String url, boolean loop, float duration) {
		this.url = url;
		this.loop = loop;
		this.duration = (int)(duration * 1000.0f);
		this.pauseTimestamp = 0l;
		this.timestamp = 0l;
		this.requiresFullResetPacket = true;
		this.isDisabled = false;
		return syncPlaybackWithPlayers();
	}
	
	/**
	 * Tells the browser to pre-load a URL to a video to be played in the future
	 * @param url the URL of the video
	 * @param ttl the amount of time the video should stay loaded
	 * @return packet to send to players
	 */
	public static byte[] bufferVideo(String url, int ttl) {
		try {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			DataOutputStream str = new DataOutputStream(bao);
			str.write(8);
			str.writeInt(ttl);
			str.writeUTF(url);
			return bao.toByteArray();
		}catch(IOException e) {
			throw new RuntimeException("serialization error", e);
		}
	}
	
	/**
	 * @return the duration of the current clip
	 */
	public float getDuration() {
		return duration * 0.001f;
	}

	/**
	 * @return the URL of the current clip
	 */
	public String getURL() {
		return url;
	}

	/**
	 * @return the server's current timestamp
	 */
	public float getPlaybackTime() {
		return getElapsedMillis() * 0.001f;
	}
	
	/**
	 * @param time time in seconds to seek the video to
	 */
	public byte[] setPlaybackTime(float time) {
		timestamp = System.currentTimeMillis() - (int)(time * 1000.0f);
		return syncPlaybackWithPlayers();
	}
	
	/**
	 * @return if playback is complete (false if loop)
	 */
	public boolean isPlaybackFinished() {
		return !loop && getElapsedMillis() == duration;
	}
	
	/**
	 * @param loop video should loop
	 */
	public byte[] setLoopEnable(boolean loop) {
		this.loop = loop;
		return syncPlaybackWithPlayers();
	}
	
	/**
	 * @return if loop is enabled
	 */
	public boolean isLoopEnable() {
		return loop;
	}
	
	/**
	 * @param pause set if video should pause
	 * @return packet to send to players
	 */
	public byte[] setPaused(boolean pause) {
		getElapsedMillis();
		if(pause && pauseTimestamp <= 0l) {
			pauseTimestamp = System.currentTimeMillis();
		}else if(!pause && pauseTimestamp > 0l) {
			timestamp = System.currentTimeMillis() - (pauseTimestamp - timestamp);
			pauseTimestamp = 0l;
		}
		return syncPlaybackWithPlayers();
	}
	
	/**
	 * @return if video is currently paused
	 */
	public boolean isPaused() {
		return pauseTimestamp > 0l;
	}
	
	/**
	 * @return current server-side volume
	 */
	public float getVolume() {
		return volume;
	}
	
}
