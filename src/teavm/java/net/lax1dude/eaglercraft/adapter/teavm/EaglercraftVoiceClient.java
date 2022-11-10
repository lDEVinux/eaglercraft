package net.lax1dude.eaglercraft.adapter.teavm;

import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.webaudio.MediaStream;

public interface EaglercraftVoiceClient extends JSObject {
	
	int READYSTATE_NONE = 0;
	int READYSTATE_ABORTED = -1;
	int READYSTATE_DEVICE_INITIALIZED = 1;

	int PEERSTATE_FAILED = 0;
	int PEERSTATE_SUCCESS = 1;
	int PEERSTATE_LOADING = 2;

	boolean voiceClientSupported();
	
	void initializeDevices();

	void setICEServers(String[] urls);

	void setICECandidateHandler(ICECandidateHandler callback);

	void setDescriptionHandler(DescriptionHandler callback);

	void setPeerTrackHandler(PeerTrackHandler callback);

	void setPeerDisconnectHandler(PeerDisconnectHandler callback);
	
	void activateVoice(boolean active);
	
	void setMicVolume(float volume);

	void mutePeer(String peerId, boolean muted);

	void resetPeerStates();

	int getPeerState();

	int getPeerStateConnect();

	int getPeerStateInitial();

	int getPeerStateDesc();

	int getPeerStateIce();
	
	int getReadyState();
	
	int signalConnect(String peerId, boolean offer);
	
	int signalDescription(String peerId, String description);
	
	int signalDisconnect(String peerId, boolean quiet);
	
	int signalICECandidate(String peerId, String candidate);
	
	@JSFunctor
	public static interface ICECandidateHandler extends JSObject {
		void call(String peerId, String candidate);
	}
	
	@JSFunctor
	public static interface DescriptionHandler extends JSObject {
		void call(String peerId, String candidate);
	}
	
	@JSFunctor
	public static interface PeerTrackHandler extends JSObject {
		void call(String peerId, MediaStream audioNode);
	}
	
	@JSFunctor
	public static interface PeerDisconnectHandler extends JSObject {
		void call(String peerId, boolean quiet);
	}

}
