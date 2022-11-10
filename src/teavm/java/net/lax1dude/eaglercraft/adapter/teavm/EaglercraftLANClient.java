package net.lax1dude.eaglercraft.adapter.teavm;

import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.ArrayBuffer;

public interface EaglercraftLANClient extends JSObject {

	final int READYSTATE_INIT_FAILED = -2;
	final int READYSTATE_FAILED = -1;
	final int READYSTATE_DISCONNECTED = 0;
	final int READYSTATE_CONNECTING = 1;
	final int READYSTATE_CONNECTED = 2;

	boolean LANClientSupported();
	
	void initializeClient();
	
	void setICEServers(String[] urls);
	
	void setICECandidateHandler(ICECandidateHandler callback);

	void setDescriptionHandler(DescriptionHandler callback);
	
	void setRemoteDataChannelHandler(ClientSignalHandler cb);
	
	void setRemoteDisconnectHandler(ClientSignalHandler cb);
	
	void setRemotePacketHandler(RemotePacketHandler cb);
	
	int getReadyState();
	
	void sendPacketToServer(ArrayBuffer buffer);
	
	void signalRemoteConnect();
	
	void signalRemoteDescription(String descJSON);
	
	void signalRemoteICECandidate(String candidate);

	void signalRemoteDisconnect(boolean quiet);

	@JSFunctor
	public static interface ICECandidateHandler extends JSObject {
		void call(String candidate);
	}
	
	@JSFunctor
	public static interface DescriptionHandler extends JSObject {
		void call(String description);
	}
	
	@JSFunctor
	public static interface ClientSignalHandler extends JSObject {
		void call();
	}
	
	@JSFunctor
	public static interface RemotePacketHandler extends JSObject {
		void call(ArrayBuffer buffer);
	}
	
}
