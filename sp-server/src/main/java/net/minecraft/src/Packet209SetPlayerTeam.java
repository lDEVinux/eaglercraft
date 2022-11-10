package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Packet209SetPlayerTeam extends Packet {
	/** A unique name for the team. */
	public String teamName = "";

	/** Only if mode = 0 or 2. */
	public String teamDisplayName = "";

	/**
	 * Only if mode = 0 or 2. Displayed before the players' name that are part of
	 * this team.
	 */
	public String teamPrefix = "";

	/**
	 * Only if mode = 0 or 2. Displayed after the players' name that are part of
	 * this team.
	 */
	public String teamSuffix = "";

	/** Only if mode = 0 or 3 or 4. Players to be added/remove from the team. */
	public Collection playerNames = new ArrayList();

	/**
	 * If 0 then the team is created. If 1 then the team is removed. If 2 the team
	 * team information is updated. If 3 then new players are added to the team. If
	 * 4 then players are removed from the team.
	 */
	public int mode = 0;

	/** Only if mode = 0 or 2. */
	public int friendlyFire;

	public Packet209SetPlayerTeam() {
	}

	public Packet209SetPlayerTeam(ScorePlayerTeam par1, int par2) {
		this.teamName = par1.func_96661_b();
		this.mode = par2;

		if (par2 == 0 || par2 == 2) {
			this.teamDisplayName = par1.func_96669_c();
			this.teamPrefix = par1.func_96668_e();
			this.teamSuffix = par1.func_96663_f();
			this.friendlyFire = par1.func_98299_i();
		}

		if (par2 == 0) {
			this.playerNames.addAll(par1.getMembershipCollection());
		}
	}

	public Packet209SetPlayerTeam(ScorePlayerTeam par1ScorePlayerTeam, Collection par2Collection, int par3) {
		if (par3 != 3 && par3 != 4) {
			throw new IllegalArgumentException("Method must be join or leave for player constructor");
		} else if (par2Collection != null && !par2Collection.isEmpty()) {
			this.mode = par3;
			this.teamName = par1ScorePlayerTeam.func_96661_b();
			this.playerNames.addAll(par2Collection);
		} else {
			throw new IllegalArgumentException("Players cannot be null/empty");
		}
	}

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.teamName = readString(par1DataInputStream, 16);
		this.mode = par1DataInputStream.readByte();

		if (this.mode == 0 || this.mode == 2) {
			this.teamDisplayName = readString(par1DataInputStream, 32);
			this.teamPrefix = readString(par1DataInputStream, 16);
			this.teamSuffix = readString(par1DataInputStream, 16);
			this.friendlyFire = par1DataInputStream.readByte();
		}

		if (this.mode == 0 || this.mode == 3 || this.mode == 4) {
			short var2 = par1DataInputStream.readShort();

			for (int var3 = 0; var3 < var2; ++var3) {
				this.playerNames.add(readString(par1DataInputStream, 16));
			}
		}
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		writeString(this.teamName, par1DataOutputStream);
		par1DataOutputStream.writeByte(this.mode);

		if (this.mode == 0 || this.mode == 2) {
			writeString(this.teamDisplayName, par1DataOutputStream);
			writeString(this.teamPrefix, par1DataOutputStream);
			writeString(this.teamSuffix, par1DataOutputStream);
			par1DataOutputStream.writeByte(this.friendlyFire);
		}

		if (this.mode == 0 || this.mode == 3 || this.mode == 4) {
			par1DataOutputStream.writeShort(this.playerNames.size());
			Iterator var2 = this.playerNames.iterator();

			while (var2.hasNext()) {
				String var3 = (String) var2.next();
				writeString(var3, par1DataOutputStream);
			}
		}
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleSetPlayerTeam(this);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 3 + this.teamName.length();
	}
}
