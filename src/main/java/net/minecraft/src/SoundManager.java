package net.minecraft.src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.client.Minecraft;

public class SoundManager {
	
	private static class EntitySoundEvent {
		private Entity e;
		private int id;
		public EntitySoundEvent(Entity e, int id) {
			this.e = e;
			this.id = id;
		}
	}
	
	private static class QueuedSoundEvent {
		private String sound;
		private float x;
		private float y;
		private float z;
		private float volume;
		private float pitch;
		private int timer;
		public QueuedSoundEvent(String sound, float x, float y, float z, float volume, float pitch, int timer) {
			this.sound = sound;
			this.x = x;
			this.y = y;
			this.z = z;
			this.volume = volume;
			this.pitch = pitch;
			this.timer = timer;
		}
	}
	
	private GameSettings options;
	private ArrayList<EntitySoundEvent> soundevents;
	private ArrayList<QueuedSoundEvent> queuedsoundevents;
	private ArrayList<Integer> activerecords;
	private HashMap<String,Integer> sounddefinitions;
	private EaglercraftRandom soundrandom;

	public SoundManager() {
		this.soundevents = new ArrayList();
		this.queuedsoundevents = new ArrayList();
		this.activerecords = new ArrayList();
		this.sounddefinitions = null;
		this.soundrandom = new EaglercraftRandom();
	}

	/**
	 * Used for loading sound settings from GameSettings
	 */
	public void loadSoundSettings(GameSettings par1GameSettings) {
		this.options = par1GameSettings;
		EaglerAdapter.setMusicVolume(options.musicVolume);
		EaglerAdapter.setMasterVolume(options.soundVolume);
		if(this.sounddefinitions == null) {
			this.sounddefinitions = new HashMap();
			for(int j = 0; j < 2; ++j) {
				byte[] b = EaglerAdapter.loadResourceBytes("/sounds/sounds.dat" + (j == 1 ? "x" : ""));
				if(b != null) {
					try {
						NBTTagCompound file = CompressedStreamTools.readUncompressed(b);
						if(j == 0) EaglerAdapter.setPlaybackOffsetDelay(file.hasKey("playbackOffset") ? file.getFloat("playbackOffset") : 0.03f);
						NBTTagList l = file.getTagList("sounds");
						int c = l.tagCount();
						for(int i = 0; i < c; i++) {
							NBTTagCompound cc = (NBTTagCompound)l.tagAt(i);
							this.sounddefinitions.put(cc.getString("e"), (int)cc.getByte("c") & 0xFF);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Called when one of the sound level options has changed.
	 */
	public void onSoundOptionsChanged() {
		EaglerAdapter.setMusicVolume(options.musicVolume);
		if(options.musicVolume > 0.0f) {
			EaglerAdapter.fireTitleMusicEvent(titleMusic != -1, options.musicVolume);
		}
		EaglerAdapter.setMasterVolume(options.soundVolume);
	}

	/**
	 * Called when Minecraft is closing down.
	 */
	public void closeMinecraft() {
		
	}

	/**
	 * If its time to play new music it starts it up.
	 */
	public void playRandomMusicIfReady() {
		
	}

	/**
	 * Sets the listener of sounds
	 */
	public void setListener(EntityLiving par1EntityLiving, float par2) {
		if(par1EntityLiving == null) {
			EaglerAdapter.setListenerPos(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
		}else {
			double x = par1EntityLiving.prevPosX + (par1EntityLiving.posX - par1EntityLiving.prevPosX) * par2;
			double y = par1EntityLiving.prevPosY + (par1EntityLiving.posY - par1EntityLiving.prevPosY) * par2;
			double z = par1EntityLiving.prevPosZ + (par1EntityLiving.posZ - par1EntityLiving.prevPosZ) * par2;
			double pitch = par1EntityLiving.prevRotationPitch + (par1EntityLiving.rotationPitch - par1EntityLiving.prevRotationPitch) * par2;
			double yaw = par1EntityLiving.prevRotationYaw + (par1EntityLiving.rotationYaw - par1EntityLiving.prevRotationYaw) * par2;
			
			try {
				EaglerAdapter.setListenerPos((float)x, (float)y, (float)z, (float)par1EntityLiving.motionX, (float)par1EntityLiving.motionY, (float)par1EntityLiving.motionZ, (float)pitch, (float)yaw);
			}catch(Throwable t) {
				System.err.println("AudioListener f***ed up again");
			}
		}
	}

	/**
	 * Stops all currently playing sounds
	 */
	public void stopAllSounds() {
		for(EntitySoundEvent e : soundevents) {
			EaglerAdapter.endSound(e.id);
		}
		for(Integer i : activerecords) {
			EaglerAdapter.endSound(i.intValue());
		}
	}

	public void playStreaming(String par1Str, float par2, float par3, float par4) {
		playStreaming(par1Str, par2, par3, par4, false);
	}

	public void playStreaming(String par1Str, float par2, float par3, float par4, boolean music) {
		for (Integer record : activerecords) {
			EaglerAdapter.endSound(record.intValue());
		}
		activerecords.clear();
		if (par1Str != null) {
			String path = "/records/" + par1Str.replace('.', '/') + ".mp3";
			int snd = EaglerAdapter.beginPlayback(path, par2, par3, par4, 1.0F, 1.0F, music);
			if (snd != -1) {
				activerecords.add(new Integer(snd));
			} else {
				System.err.println("unregistered record: "+par1Str);
			}
		}
	}

	/**
	 * Updates the sound associated with the entity with that entity's position and
	 * velocity. Args: the entity
	 */
	public void updateSoundLocation(Entity par1Entity) {
		for(EntitySoundEvent e : soundevents) {
			if(e.e.equals(par1Entity)) {
				EaglerAdapter.moveSound(e.id, (float)par1Entity.posX, (float)par1Entity.posY, (float)par1Entity.posZ, (float)par1Entity.motionX, (float)par1Entity.motionY, (float)par1Entity.motionZ);
			}
		}
	}

	/**
	 * Updates the sound associated with soundEntity with the position and velocity
	 * of trackEntity. Args: soundEntity, trackEntity
	 */
	public void updateSoundLocation(Entity par1Entity, Entity par2Entity) {
		for(EntitySoundEvent e : soundevents) {
			if(e.e.equals(par1Entity)) {
				EaglerAdapter.moveSound(e.id, (float)par2Entity.posX, (float)par2Entity.posY, (float)par2Entity.posZ, (float)par2Entity.motionX, (float)par2Entity.motionY, (float)par2Entity.motionZ);
			}
		}
	}

	/**
	 * Returns true if a sound is currently associated with the given entity, or
	 * false otherwise.
	 */
	public boolean isEntitySoundPlaying(Entity par1Entity) {
		for(EntitySoundEvent e : soundevents) {
			if(e.e.equals(par1Entity)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Stops playing the sound associated with the given entity
	 */
	public void stopEntitySound(Entity par1Entity) {
		for(EntitySoundEvent e : soundevents) {
			if(e.e.equals(par1Entity)) {
				EaglerAdapter.endSound(e.id);
			}
		}
	}

	/**
	 * Sets the volume of the sound associated with the given entity, if one is
	 * playing. The volume is scaled by the global sound volume. Args: the entity,
	 * the volume (from 0 to 1)
	 */
	public void setEntitySoundVolume(Entity par1Entity, float par2) {
		for(EntitySoundEvent e : soundevents) {
			if(e.e.equals(par1Entity)) {
				EaglerAdapter.setVolume(e.id, par2);
			}
		}
	}

	/**
	 * Sets the pitch of the sound associated with the given entity, if one is
	 * playing. Args: the entity, the pitch
	 */
	public void setEntitySoundPitch(Entity par1Entity, float par2) {
		for(EntitySoundEvent e : soundevents) {
			if(e.e.equals(par1Entity)) {
				EaglerAdapter.setPitch(e.id, par2);
			}
		}
	}
	
	private static final Map<String, String> remapAdl;
	
	static {
		remapAdl = new HashMap();
		remapAdl.put("ambient.cave.cave", "!adl.b");
		remapAdl.put("damage.hit", "!random.classic_hurt");
		remapAdl.put("damage.hurtflesh", "!random.classic_hurt");
		remapAdl.put("mob.zombie.hurt", "adl.yee");
		remapAdl.put("mob.zombie.say", "adl.yee");
		remapAdl.put("mob.zombiepig.zpig", "!adl.eee");
		remapAdl.put("mob.zombiepig.zpigangry", "adl.eee");
		remapAdl.put("mob.blaze.breathe", "adl.yee");
		remapAdl.put("mob.endermen.scream", "adl.yee");
		remapAdl.put("mob.endermen.stare", "!adl.l");
		remapAdl.put("mob.pig.say", "!adl.eee");
		remapAdl.put("mob.pig.death", "adl.eee");
		remapAdl.put("mob.silverfish.say", "!adl.eee");
		remapAdl.put("mob.ghast.scream", "!adl.yee");
		remapAdl.put("mob.slime.big", "!adl.eee");
		remapAdl.put("mob.slime.small", "!adl.eee");
		remapAdl.put("mob.slime.attack", "!adl.eee");
		remapAdl.put("mob.spider.say", "adl.eee");
		remapAdl.put("mob.villager.default", "!adl.a");
		remapAdl.put("mob.villager.defaulthurt", "!adl.a");
	}

	/**
	 * If a sound is already playing from the given entity, update the position and
	 * velocity of that sound to match the entity. Otherwise, start playing a sound
	 * from that entity. Setting the last flag to true will prevent other sounds
	 * from overriding this one. Args: The sound name, the entity, the volume, the
	 * pitch, priority
	 */
	public void playEntitySound(String par1Str, Entity par2Entity, float par3, float par4, boolean par5) {
		for(EntitySoundEvent e : soundevents) {
			if(e.e.equals(par2Entity)) {
				EaglerAdapter.moveSound(e.id, (float)par2Entity.posX, (float)par2Entity.posY, (float)par2Entity.posZ, (float)par2Entity.motionX, (float)par2Entity.motionY, (float)par2Entity.motionZ);
				return;
			}
		}
		if(this.options.soundVolume > 0.0F && par3 > 0.0F) {
			Minecraft mc = Minecraft.getMinecraft();
			if(mc.gameSettings.adderall || mc.entityRenderer.asdfghjkl) {
				if(mc.entityRenderer.startup > 300) {
					String rp = remapAdl.get(par1Str);
					if(rp != null) {
						if(rp.startsWith("!")) {
							par1Str = rp.substring(1);
						}else {
							int i = 4 - (mc.entityRenderer.startup - 300) / 200;
							if(i < 0) i = 0;
							if(soundrandom.nextInt(2 + i) == 0) {
								par1Str = rp;
							}
						}
					}
				}
			}
			Integer ct = this.sounddefinitions.get(par1Str);
			if(ct != null) {
				int c = ct.intValue();
				String path;
				if(c <= 1) {
					path = "/sounds/"+par1Str.replace('.', '/')+".mp3";
				}else {
					int r = soundrandom.nextInt(c) + 1;
					path = "/sounds/"+par1Str.replace('.', '/')+r+".mp3";
				}
				int id = 0;
				float i = Minecraft.getMinecraft().entityRenderer.startup / 800.0f;
				if(i > 1.0f) i = 1.0f;
				i = i * i;
				float v = i > 0.0f ? 1.0f + (soundrandom.nextFloat() - 0.5f) * i * 0.6f : 1.0f;
				float p = i > 0.0f ? 1.0f + (soundrandom.nextFloat() - 0.2f) * i * 1.3f : 1.0f;
				soundevents.add(new EntitySoundEvent(par2Entity, id = EaglerAdapter.beginPlayback(path, 0f, 0f, 0f, par3 * v * 0.8f, par4 * p)));
				EaglerAdapter.moveSound(id, (float)par2Entity.posX + (i > 0.0f ? (soundrandom.nextFloat() - 0.5f) * i * 4.0f : 0.0f),
						(float)par2Entity.posY + (i > 0.0f ? (soundrandom.nextFloat() - 0.5f) * i * 4.0f : 0.0f),
						(float)par2Entity.posZ + (i > 0.0f ? (soundrandom.nextFloat() - 0.5f) * i * 4.0f : 0.0f),
						(float)par2Entity.motionX, (float)par2Entity.motionY, (float)par2Entity.motionZ);
				if(i > 0.3f) {
					while(soundrandom.nextFloat() * i > 0.3f) {
						soundevents.add(new EntitySoundEvent(par2Entity, id = EaglerAdapter.beginPlayback(path, 0f, 0f, 0f, par3 * v *
								(soundrandom.nextFloat() * 0.4f + 0.8f), par4 * p * (pow2(soundrandom.nextFloat()) * 1.5f + 0.3f))));
						EaglerAdapter.moveSound(id, (float)par2Entity.posX + (soundrandom.nextFloat() - 0.5f) * i * 2.0f,
								(float)par2Entity.posY + (soundrandom.nextFloat() - 0.5f) * i * 2.0f,
								(float)par2Entity.posZ + (soundrandom.nextFloat() - 0.5f) * i * 2.0f,
								(float)par2Entity.motionX, (float)par2Entity.motionY, (float)par2Entity.motionZ);
					}
				}
			}else {
				System.err.println("unregistered sound effect: "+par1Str);
			}
		}
	}

	/**
	 * Plays a sound. Args: soundName, x, y, z, volume, pitch
	 */
	public void playSound(String par1Str, float par2, float par3, float par4, float par5, float par6) {
		if(this.options.soundVolume > 0.0F && par5 > 0.0F) {
			Minecraft mc = Minecraft.getMinecraft();
			if(mc.gameSettings.adderall || mc.entityRenderer.asdfghjkl) {
				if(mc.entityRenderer.startup > 300) {
					String rp = remapAdl.get(par1Str);
					if(rp != null) {
						if(rp.startsWith("!")) {
							par1Str = rp.substring(1);
						}else {
							int i = 4 - (mc.entityRenderer.startup - 300) / 200;
							if(i < 0) i = 0;
							if(soundrandom.nextInt(2 + i) == 0) {
								par1Str = rp;
							}
						}
					}
				}
			}
			Integer ct = this.sounddefinitions.get(par1Str);
			if(ct != null) {
				int c = ct.intValue();
				String path;
				if(c <= 1) {
					path = "/sounds/"+par1Str.replace('.', '/')+".mp3";
				}else {
					int r = soundrandom.nextInt(c) + 1;
					path = "/sounds/"+par1Str.replace('.', '/')+r+".mp3";
				}
				float i = mc.entityRenderer.startup / 800.0f;
				if(i > 1.0f) i = 1.0f;
				i = i * i;
				float v = i > 0.0f ? 1.0f + (soundrandom.nextFloat() - 0.5f) * i * 0.6f : 1.0f;
				float p = i > 0.0f ? 1.0f + (soundrandom.nextFloat() - 0.2f) * i * 1.3f : 1.0f;
				if(i > 0.3f) {
					par2 += (soundrandom.nextFloat() - 0.5f) * i * 3.0f;
					par3 += (soundrandom.nextFloat() - 0.5f) * i * 3.0f;
					par4 += (soundrandom.nextFloat() - 0.5f) * i * 3.0f;
					while(soundrandom.nextFloat() * i > 0.3f) {
						EaglerAdapter.beginPlayback(path, par2 + (soundrandom.nextFloat() - 0.5f) * i * 3.0f, par3 + (soundrandom.nextFloat() - 0.5f) * i * 3.0f,
								par4 + (soundrandom.nextFloat() - 0.5f) * i * 3.0f, par5 * v * (soundrandom.nextFloat() * 0.4f + 0.8f),
								par6 * p * (pow2(soundrandom.nextFloat()) * 1.5f + 0.3f));
					}
				}
				EaglerAdapter.beginPlayback(path, par2, par3, par4, par5 * v, par6 * p);
			}else {
				System.err.println("unregistered sound effect: "+par1Str);
			}
		}
	}
	
	private static float pow2(float f) {
		return f * f;
	}

	/**
	 * Plays a sound effect with the volume and pitch of the parameters passed. The
	 * sound isn't affected by position of the player (full volume and center
	 * balanced)
	 */
	public void playSoundFX(String par1Str, float par2, float par3) {
		if(this.options.soundVolume > 0.0F && par2 > 0.0F) {
			Integer ct = this.sounddefinitions.get(par1Str);
			if(ct != null) {
				int c = ct.intValue();
				String path;
				if(c <= 1) {
					path = "/sounds/"+par1Str.replace('.', '/')+".mp3";
				}else {
					int r = soundrandom.nextInt(c) + 1;
					path = "/sounds/"+par1Str.replace('.', '/')+r+".mp3";
				}
				EaglerAdapter.beginPlaybackStatic(path, par2, par3);
			}else {
				System.err.println("unregistered sound effect: "+par1Str);
			}
		}
	}

	/**
	 * Pauses all currently playing sounds
	 */
	public void pauseAllSounds() {

	}

	/**
	 * Resumes playing all currently playing sounds (after pauseAllSounds)
	 */
	public void resumeAllSounds() {

	}
	
	private int resetTimer = 0;

	public void func_92071_g() {
		++resetTimer;
		if(resetTimer % 20 == 0) {
			ArrayList<EntitySoundEvent> e = this.soundevents;
			this.soundevents = new ArrayList();
			for(EntitySoundEvent e2 : e) {
				if(EaglerAdapter.isPlaying(e2.id)) {
					soundevents.add(e2);
				}
			}
		}
		Iterator<QueuedSoundEvent> itr = queuedsoundevents.iterator();
		while(itr.hasNext()) {
			QueuedSoundEvent e = itr.next();
			if(--e.timer <= 0) {
				playSound(e.sound, e.x, e.y, e.z, e.volume, e.pitch);
				itr.remove();
			}
		}
	}

	public void func_92070_a(String par1Str, float par2, float par3, float par4, float par5, float par6, int par7) {
		queuedsoundevents.add(new QueuedSoundEvent(par1Str, par2, par3, par4, par5, par6, par7));
	}
	
	private int titleMusic = -1;
	
	public void playTheTitleMusic() {
		if(titleMusic == -1 || !EaglerAdapter.isPlaying(titleMusic)) {
			if(this.options.musicVolume > 0.0f) {
				titleMusic = EaglerAdapter.beginPlaybackStatic("/sounds/gta.mp3", 1.0f, 1.0f, true);
				EaglerAdapter.fireTitleMusicEvent(true, this.options.musicVolume);
			}
		}
	}
	
	public void stopTheTitleMusic() {
		if(EaglerAdapter.isPlaying(titleMusic)) {
			EaglerAdapter.endSound(titleMusic);
			EaglerAdapter.fireTitleMusicEvent(false, this.options.musicVolume);
		}
		titleMusic = -1;
	}
	
}
