package net.lax1dude.eaglercraft;

import net.lax1dude.eaglercraft.sp.ipc.*;

public class IntegratedState {
	
	public static final int WORLD_WORKER_NOT_RUNNING = -2;
	public static final int WORLD_WORKER_BOOTING = -1;
	public static final int WORLD_NONE = 0;
	public static final int WORLD_LOADING = 2;
	public static final int WORLD_LOADED = 3;
	public static final int WORLD_UNLOADING = 4;
	public static final int WORLD_DELETING = 5;
	public static final int WORLD_RENAMING = 6;
	public static final int WORLD_DUPLICATING = 7;
	public static final int WORLD_PAUSED = 9;
	public static final int WORLD_LISTING = 10;
	public static final int WORLD_SAVING = 11;
	public static final int WORLD_IMPORTING = 12;
	public static final int WORLD_EXPORTING = 13;
	public static final int WORLD_GET_NBT = 14;

	public static final int WORLD_LIST_FILE = 15;
	public static final int WORLD_FILE_READ = 16;
	public static final int WORLD_FILE_WRITE = 17;
	public static final int WORLD_FILE_MOVE = 18;
	public static final int WORLD_FILE_COPY = 19;
	public static final int WORLD_CLEAR_PLAYERS = 20;
	
	public static String getStateName(int i) {
		switch(i) {
		case WORLD_WORKER_NOT_RUNNING: return "WORLD_WORKER_NOT_RUNNING";
		case WORLD_WORKER_BOOTING: return "WORLD_WORKER_BOOTING";
		case WORLD_NONE: return "WORLD_NONE";
		case WORLD_LOADING: return "WORLD_LOADING";
		case WORLD_LOADED: return "WORLD_LOADED";
		case WORLD_UNLOADING: return "WORLD_UNLOADING";
		case WORLD_DELETING: return "WORLD_DELETING";
		case WORLD_RENAMING: return "WORLD_RENAMING";
		case WORLD_DUPLICATING: return "WORLD_DUPLICATING";
		case WORLD_PAUSED: return "WORLD_PAUSED";
		case WORLD_LISTING: return "WORLD_LISTING";
		case WORLD_SAVING: return "WORLD_SAVING";
		case WORLD_IMPORTING: return "WORLD_IMPORTING";
		case WORLD_EXPORTING: return "WORLD_EXPORTING";
		case WORLD_GET_NBT: return "WORLD_GET_NBT";
		case WORLD_LIST_FILE: return "WORLD_LIST_FILE";
		case WORLD_FILE_READ: return "WORLD_FILE_READ";
		case WORLD_FILE_WRITE: return "WORLD_FILE_WRITE";
		case WORLD_FILE_MOVE: return "WORLD_FILE_MOVE";
		case WORLD_FILE_COPY: return "WORLD_FILE_COPY";
		case WORLD_CLEAR_PLAYERS: return "WORLD_CLEAR_PLAYERS";
		default: return "INVALID";
		}
	}
	
	public static boolean isACKValidInState(int ack, int state) {
		switch(ack) {
		case 0xFF: return state == WORLD_WORKER_BOOTING;
		case IPCPacket00StartServer.ID: return state == WORLD_LOADING;
		case IPCPacket01StopServer.ID: return state == WORLD_UNLOADING;
		case IPCPacket03DeleteWorld.ID: return state == WORLD_DELETING;
		case IPCPacket04RenameWorld.ID: return (state == WORLD_DUPLICATING || state == WORLD_RENAMING);
		case IPCPacket07ImportWorld.ID: return state == WORLD_IMPORTING;
		case IPCPacket0BPause.ID: return (state == WORLD_SAVING || state == WORLD_PAUSED);
		case IPCPacket12FileWrite.ID: return state == WORLD_FILE_WRITE;
		case IPCPacket13FileCopyMove.ID: return (state == WORLD_FILE_MOVE || state == WORLD_FILE_COPY);
		case IPCPacket18ClearPlayers.ID: return state == WORLD_CLEAR_PLAYERS;
		default: return false;
		}
	}
	
	public void assertState(int ack, int state) {
		if(!isACKValidInState(ack, state)) {
			String msg = "Recieved ACK " + ack + " '" + getStateName(ack) + "' while the client state was " + state + " '" + getStateName(state) + "'";
			System.err.println(msg);
			throw new IllegalStateException(msg);
		}
	}
}
