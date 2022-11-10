package net.lax1dude.eaglercraft.adapter;

import static net.lax1dude.eaglercraft.adapter.teavm.WebGL2RenderingContext.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.ajax.ReadyStateChangeHandler;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.browser.Storage;
import org.teavm.jso.browser.TimerHandler;
import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.canvas.ImageData;
import org.teavm.jso.dom.css.CSSStyleDeclaration;
import org.teavm.jso.dom.events.ErrorEvent;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.KeyboardEvent;
import org.teavm.jso.dom.events.MessageEvent;
import org.teavm.jso.dom.events.MouseEvent;
import org.teavm.jso.dom.events.WheelEvent;
import org.teavm.jso.dom.html.HTMLAudioElement;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLImageElement;
import org.teavm.jso.dom.html.HTMLVideoElement;
import org.teavm.jso.media.MediaError;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.DataView;
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.typedarrays.Int32Array;
import org.teavm.jso.typedarrays.Uint8Array;
import org.teavm.jso.typedarrays.Uint8ClampedArray;
import org.teavm.jso.webaudio.AnalyserNode;
import org.teavm.jso.webaudio.AudioBuffer;
import org.teavm.jso.webaudio.AudioBufferSourceNode;
import org.teavm.jso.webaudio.AudioContext;
import org.teavm.jso.webaudio.AudioListener;
import org.teavm.jso.webaudio.AudioNode;
import org.teavm.jso.webaudio.ChannelMergerNode;
import org.teavm.jso.webaudio.DecodeErrorCallback;
import org.teavm.jso.webaudio.DecodeSuccessCallback;
import org.teavm.jso.webaudio.GainNode;
import org.teavm.jso.webaudio.MediaElementAudioSourceNode;
import org.teavm.jso.webaudio.MediaEvent;
import org.teavm.jso.webaudio.MediaStream;
import org.teavm.jso.webaudio.MediaStreamAudioSourceNode;
import org.teavm.jso.webaudio.PannerNode;
import org.teavm.jso.webgl.WebGLBuffer;
import org.teavm.jso.webgl.WebGLFramebuffer;
import org.teavm.jso.webgl.WebGLProgram;
import org.teavm.jso.webgl.WebGLRenderbuffer;
import org.teavm.jso.webgl.WebGLShader;
import org.teavm.jso.webgl.WebGLTexture;
import org.teavm.jso.webgl.WebGLUniformLocation;
import org.teavm.jso.websocket.CloseEvent;
import org.teavm.jso.websocket.WebSocket;
import org.teavm.jso.workers.Worker;

import net.lax1dude.eaglercraft.AssetRepository;
import net.lax1dude.eaglercraft.Base64;
import net.lax1dude.eaglercraft.Client;
import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglerImage;
import net.lax1dude.eaglercraft.EaglerProfile;
import net.lax1dude.eaglercraft.EarlyLoadScreen;
import net.lax1dude.eaglercraft.ExpiringSet;
import net.lax1dude.eaglercraft.IntegratedServer;
import net.lax1dude.eaglercraft.LANPeerEvent;
import net.lax1dude.eaglercraft.LocalStorageManager;
import net.lax1dude.eaglercraft.PKT;
import net.lax1dude.eaglercraft.RelayQuery;
import net.lax1dude.eaglercraft.RelayServerSocket;
import net.lax1dude.eaglercraft.RelayWorldsQuery;
import net.lax1dude.eaglercraft.ServerQuery;
import net.lax1dude.eaglercraft.Voice;
import net.lax1dude.eaglercraft.adapter.teavm.EaglercraftLANClient;
import net.lax1dude.eaglercraft.adapter.teavm.EaglercraftLANServer;
import net.lax1dude.eaglercraft.adapter.teavm.EaglercraftVoiceClient;
import net.lax1dude.eaglercraft.adapter.teavm.SelfDefence;
import net.lax1dude.eaglercraft.adapter.teavm.WebGL2RenderingContext;
import net.lax1dude.eaglercraft.adapter.teavm.WebGLQuery;
import net.lax1dude.eaglercraft.adapter.teavm.WebGLVertexArray;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket00Handshake;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket07LocalWorlds;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket07LocalWorlds.LocalWorld;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacket69Pong;
import net.lax1dude.eaglercraft.sp.relay.pkt.IPacketFFErrorCode;
import net.minecraft.src.MathHelper;

public class EaglerAdapterImpl2 {

	public static final boolean _wisWebGL() {
		return true;
	}
	public static final String _wgetShaderHeader() {
		return "#version 300 es";
	}
	
	@JSBody(params = { }, script = "return window.location.href;")
	private static native String getLocationString();
	
	public static final boolean isSSLPage() {
		return getLocationString().startsWith("https");
	}
	
	public static final InputStream loadResource(String path) {
		byte[] file = loadResourceBytes(path);
		if (file != null) {
			return new ByteArrayInputStream(file);
		} else {
			return null;
		}
	}

	public static final byte[] loadResourceBytes(String path) {
		return AssetRepository.getResource(path);
	}
	
	public static final String fileContents(String path) {
		byte[] contents = loadResourceBytes(path);
		if(contents == null) {
			return null;
		}else {
			return new String(contents, Charset.forName("UTF-8"));
		}
	}
	
	public static final String[] fileContentsLines(String path) {
		String contents = fileContents(path);
		if(contents == null) {
			return null;
		}else {
			return contents.replace("\r\n", "\n").split("[\r\n]");
		}
	}

	@Async
	public static native String downloadAssetPack(String assetPackageURI);

	private static void downloadAssetPack(String assetPackageURI, final AsyncCallback<String> cb) {
		final XMLHttpRequest request = XMLHttpRequest.create();
		request.setResponseType("arraybuffer");
		request.open("GET", assetPackageURI, true);
		request.setOnReadyStateChange(new ReadyStateChangeHandler() {
			@Override
			public void stateChanged() {
				if(request.getReadyState() == XMLHttpRequest.DONE) {
					Uint8Array bl = Uint8Array.create((ArrayBuffer)request.getResponse());
					loadedPackage = new byte[bl.getByteLength()];
					for(int i = 0; i < loadedPackage.length; ++i) {
						loadedPackage[i] = (byte) bl.get(i);
					}
					cb.complete("yee");
				}
			}
		});
		request.send();
	}

	@Async
	public static native byte[] downloadURL(String url);

	private static void downloadURL(String url, final AsyncCallback<byte[]> cb) {
		if(url.isEmpty()) {
			cb.complete(new byte[0]);
			return;
		}
		final XMLHttpRequest request = XMLHttpRequest.create();
		request.setResponseType("arraybuffer");
		request.open("GET", url, true);
		request.setOnReadyStateChange(new ReadyStateChangeHandler() {
			@Override
			public void stateChanged() {
				if(request.getReadyState() == XMLHttpRequest.DONE) {
					Uint8Array bl = Uint8Array.create((ArrayBuffer)request.getResponse());
					byte[] res = new byte[bl.getByteLength()];
					for(int i = 0; i < res.length; ++i) {
						res[i] = (byte) bl.get(i);
					}
					cb.complete(res);
				}
			}
		});
		request.send();
	}

	@JSBody(params = { "v", "s" }, script = "window[v] = s;")
	public static native void setDebugVar(String v, String s);
	
	@JSBody(params = { }, script = "if(window.navigator.userActivation){return window.navigator.userActivation.hasBeenActive;}else{return false;}")
	public static native boolean hasBeenActive();

	public static HTMLDocument doc = null;
	public static HTMLElement parent = null;
	public static HTMLCanvasElement canvas = null;
	public static CanvasRenderingContext2D frameBuffer = null;
	public static HTMLCanvasElement renderingCanvas = null;
	public static WebGL2RenderingContext webgl = null;
	public static Window win = null;
	private static byte[] loadedPackage = null;
	private static EventListener contextmenu = null;
	private static EventListener mousedown = null;
	private static EventListener mouseup = null;
	private static EventListener mousemove = null;
	private static EventListener keydown = null;
	private static EventListener keyup = null;
	private static EventListener keypress = null;
	private static EventListener wheel = null;
	private static String[] identifier = new String[0];
	private static String integratedServerScript = "worker_bootstrap.js";
	private static boolean anisotropicFilteringSupported = false;
	
	public static final String[] getIdentifier() {
		return identifier;
	}

	@JSBody(params = { "v" }, script = "try { return \"\"+window.navigator[v]; } catch(e) { return \"<error>\"; }")
	private static native String getNavString(String var);
	
	public static void onWindowUnload() {
		LocalStorageManager.saveStorageA();
		LocalStorageManager.saveStorageG();
		LocalStorageManager.saveStorageP();
	}

	@JSBody(params = { "m" }, script = "return m.offsetX;")
	private static native int getOffsetX(MouseEvent m);
	
	@JSBody(params = { "m" }, script = "return m.offsetY;")
	private static native int getOffsetY(MouseEvent m);
	
	@JSBody(params = { "e" }, script = "return e.which;")
	private static native int getWhich(KeyboardEvent e);
	
	public static final void initializeContext(HTMLElement rootElement, String assetPackageURI, String serverWorkerURI) {
		parent = rootElement;
		String s = parent.getAttribute("style");
		parent.setAttribute("style", (s == null ? "" : s)+"overflow-x:hidden;overflow-y:hidden;");
		win = Window.current();
		doc = win.getDocument();
		integratedServerScript = serverWorkerURI;
		double r = win.getDevicePixelRatio();
		int iw = parent.getClientWidth();
		int ih = parent.getClientHeight();
		int sw = (int)(r * iw);
		int sh = (int)(r * ih);
		canvas = (HTMLCanvasElement)doc.createElement("canvas");
		CSSStyleDeclaration canvasStyle = canvas.getStyle();
		canvasStyle.setProperty("width", "100%");
		canvasStyle.setProperty("height", "100%");
		canvasStyle.setProperty("image-rendering", "pixelated");
		canvas.setWidth(sw);
		canvas.setHeight(sh);
		rootElement.appendChild(canvas);
		try {
			doc.exitPointerLock();
		}catch(Throwable t) {
			Client.showIncompatibleScreen("Mouse cursor lock is not available on this device!");
			throw new RuntimeException("Mouse cursor lock is not available on this device!");
		}
		SelfDefence.init(canvas);
		renderingCanvas = (HTMLCanvasElement)doc.createElement("canvas");
		renderingCanvas.setWidth(sw);
		renderingCanvas.setHeight(sh);
		frameBuffer = (CanvasRenderingContext2D) canvas.getContext("2d");
		webgl = (WebGL2RenderingContext) renderingCanvas.getContext("webgl2", youEagler());
		if(webgl == null) {
			Client.showIncompatibleScreen("WebGL 2.0 is not supported on this device!");
			throw new RuntimeException("WebGL 2.0 is not supported in your browser ("+getNavString("userAgent")+")");
		}
		
		//String agent = getString("window.navigator.userAgent").toLowerCase();
		//if(agent.contains("windows")) isAnisotropicPatched = false;
		
		anisotropicFilteringSupported = webgl.getExtension("EXT_texture_filter_anisotropic") != null;
		
		win.addEventListener("contextmenu", contextmenu = new EventListener<MouseEvent>() {
			@Override
			public void handleEvent(MouseEvent evt) {
				evt.preventDefault();
				evt.stopPropagation();
			}
		});
		canvas.addEventListener("mousedown", mousedown = new EventListener<MouseEvent>() {
			@Override
			public void handleEvent(MouseEvent evt) {
				int b = evt.getButton();
				buttonStates[b == 1 ? 2 : (b == 2 ? 1 : b)] = true;
				mouseEvents.add(evt);
				evt.preventDefault();
				evt.stopPropagation();
			}
		});
		canvas.addEventListener("mouseup", mouseup = new EventListener<MouseEvent>() {
			@Override
			public void handleEvent(MouseEvent evt) {
				int b = evt.getButton();
				buttonStates[b == 1 ? 2 : (b == 2 ? 1 : b)] = false;
				mouseEvents.add(evt);
				evt.preventDefault();
				evt.stopPropagation();
			}
		});
		canvas.addEventListener("mousemove", mousemove = new EventListener<MouseEvent>() {
			@Override
			public void handleEvent(MouseEvent evt) {
				mouseX = (int)(getOffsetX(evt) * win.getDevicePixelRatio());
				mouseY = (int)((canvas.getClientHeight() - getOffsetY(evt)) * win.getDevicePixelRatio());
				mouseDX += evt.getMovementX();
				mouseDY += -evt.getMovementY();
				if(hasBeenActive()) {
					mouseEvents.add(evt);
				}
				evt.preventDefault();
				evt.stopPropagation();
			}
		});
		win.addEventListener("keydown", keydown = new EventListener<KeyboardEvent>() {
			@Override
			public void handleEvent(KeyboardEvent evt) {
				//keyStates[remapKey(evt.getKeyCode())] = true;
				keyStates[remapKey(getWhich(evt))] = true;
				keyEvents.add(evt);
				evt.preventDefault();
				evt.stopPropagation();
			}
		});
		win.addEventListener("keyup", keyup = new EventListener<KeyboardEvent>() {
			@Override
			public void handleEvent(KeyboardEvent evt) {
				//keyStates[remapKey(evt.getKeyCode())] = false;
				keyStates[remapKey(getWhich(evt))] = false;
				keyEvents.add(evt);
				evt.preventDefault();
				evt.stopPropagation();
			}
		});
		win.addEventListener("keypress", keypress = new EventListener<KeyboardEvent>() {
			@Override
			public void handleEvent(KeyboardEvent evt) {
				if(enableRepeatEvents && evt.isRepeat()) keyEvents.add(evt);
				evt.preventDefault();
				evt.stopPropagation();
			}
		});
		canvas.addEventListener("wheel", wheel = new EventListener<WheelEvent>() {
			@Override
			public void handleEvent(WheelEvent evt) {
				mouseEvents.add(evt);
				evt.preventDefault();
				evt.stopPropagation();
			}
		});
		win.addEventListener("blur", new EventListener<WheelEvent>() {
			@Override
			public void handleEvent(WheelEvent evt) {
				isWindowFocused = false;
			}
		});
		win.addEventListener("focus", new EventListener<WheelEvent>() {
			@Override
			public void handleEvent(WheelEvent evt) {
				isWindowFocused = true;
			}
		});
		onBeforeCloseRegister();
		
		initFileChooser();
		
		EarlyLoadScreen.paintScreen();
		
		voiceClient = startVoiceClient();
		rtcLANClient = startRTCLANClient();
		
		//todo: safely skip startRTCLANServer() if the integrated server is disabled:
		
		//if(integratedServerScript != null) {
			rtcLANServer = startRTCLANServer();
		//}
		
		downloadAssetPack(assetPackageURI);
		
		try {
			AssetRepository.install(loadedPackage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(mouseEvents.isEmpty() && keyEvents.isEmpty() && !hasBeenActive()) {
			EarlyLoadScreen.paintEnable();
			
			while(mouseEvents.isEmpty() && keyEvents.isEmpty()) {
				try {
					Thread.sleep(100l);
				} catch (InterruptedException e) {
					;
				}
			}
		}
		
		audioctx = AudioContext.create();
		masterVolumeNode = audioctx.createGain();
		masterVolumeNode.getGain().setValue(1.0f);
		masterVolumeNode.connect(audioctx.getDestination());
		musicVolumeNode = audioctx.createGain();
		musicVolumeNode.getGain().setValue(1.0f);
		musicVolumeNode.connect(audioctx.getDestination());
		
		mouseEvents.clear();
		keyEvents.clear();
		
		Window.setInterval(new TimerHandler() {
			@Override
			public void onTimer() {
				Iterator<BufferedVideo> vids = videosBuffer.values().iterator();
				while(vids.hasNext()) {
					BufferedVideo v = vids.next();
					if(System.currentTimeMillis() - v.requestedTime > v.ttl) {
						v.videoElement.setSrc("");
						vids.remove();
					}
				}
			}
		}, 5000);
	}
	
	@JSBody(params = { }, script = "return window.startVoiceClient();")
	private static native EaglercraftVoiceClient startVoiceClient();
	
	@JSBody(params = { }, script = 
			"window.eagsFileChooser = {\r\n" + 
			"inputElement: null,\r\n" + 
			"openFileChooser: function(ext, mime){\r\n" + 
			"var el = window.eagsFileChooser.inputElement = document.createElement(\"input\");\r\n" + 
			"el.type = \"file\";\r\n" + 
			"el.multiple = false;\r\n" + 
			"el.addEventListener(\"change\", function(evt){\r\n" + 
			"var f = window.eagsFileChooser.inputElement.files;\r\n" + 
			"if(f.length == 0){\r\n" + 
			"window.eagsFileChooser.getFileChooserResult = null;\r\n" + 
			"}else{\r\n" + 
			"(async function(){\r\n" + 
			"window.eagsFileChooser.getFileChooserResult = await f[0].arrayBuffer();\r\n" + 
			"window.eagsFileChooser.getFileChooserResultName = f[0].name;\r\n" + 
			"})();\r\n" + 
			"}\r\n" + 
			"});\r\n" + 
			"window.eagsFileChooser.getFileChooserResult = null;\r\n" + 
			"window.eagsFileChooser.getFileChooserResultName = null;\r\n" + 
			"el.accept = \".\" + ext;\r\n" +
			"el.click();\r\n" + 
			"},\r\n" + 
			"getFileChooserResult: null,\r\n" + 
			"getFileChooserResultName: null\r\n" + 
			"};")
	private static native void initFileChooser();
	
	public static final void destroyContext() {
		
	}
	
	public static final void removeEventHandlers() {
		try {
			win.removeEventListener("contextmenu", contextmenu);
			canvas.removeEventListener("mousedown", mousedown);
			canvas.removeEventListener("mouseup", mouseup);
			canvas.removeEventListener("mousemove", mousemove);
			win.removeEventListener("keydown", keydown);
			win.removeEventListener("keyup", keyup);
			win.removeEventListener("keypress", keypress);
			canvas.removeEventListener("wheel", wheel);
		}catch(Throwable t) {
		}
		try {
			String screenImg = canvas.toDataURL("image/png");
			canvas.delete();
			HTMLImageElement newImage = (HTMLImageElement) doc.createElement("img");
			newImage.setSrc(screenImg);
			newImage.setWidth(parent.getClientWidth());
			newImage.setHeight(parent.getClientHeight());
			parent.appendChild(newImage);
		}catch(Throwable t) {
		}
	}

	private static LinkedList<MouseEvent> mouseEvents = new LinkedList();
	private static LinkedList<KeyboardEvent> keyEvents = new LinkedList();

	private static int mouseX = 0;
	private static int mouseY = 0;
	private static double mouseDX = 0.0D;
	private static double mouseDY = 0.0D;
	private static int width = 0;
	private static int height = 0;
	private static boolean enableRepeatEvents = false;
	private static boolean isWindowFocused = true;
	
	@JSBody(params = { }, script = "return {antialias: false, depth: true, powerPreference: \"high-performance\", desynchronized: false, preserveDrawingBuffer: false, premultipliedAlpha: false, alpha: false};")
	public static native JSObject youEagler();
	
	public static final int _wGL_TEXTURE_2D = TEXTURE_2D;
	public static final int _wGL_DEPTH_TEST = DEPTH_TEST;
	public static final int _wGL_LEQUAL = LEQUAL;
	public static final int _wGL_GEQUAL = GEQUAL;
	public static final int _wGL_GREATER = GREATER;
	public static final int _wGL_LESS = LESS;
	public static final int _wGL_BACK = BACK;
	public static final int _wGL_FRONT = FRONT;
	public static final int _wGL_FRONT_AND_BACK = FRONT_AND_BACK;
	public static final int _wGL_COLOR_BUFFER_BIT = COLOR_BUFFER_BIT;
	public static final int _wGL_DEPTH_BUFFER_BIT = DEPTH_BUFFER_BIT;
	public static final int _wGL_BLEND = BLEND;
	public static final int _wGL_RGBA = RGBA;
	public static final int _wGL_RGB = RGB;
	public static final int _wGL_RGB8 = RGB8;
	public static final int _wGL_RGBA8 = RGBA8;
	public static final int _wGL_RED = RED;
	public static final int _wGL_R8 = R8;
	public static final int _wGL_UNSIGNED_BYTE = UNSIGNED_BYTE;
	public static final int _wGL_UNSIGNED_SHORT = UNSIGNED_SHORT;
	public static final int _wGL_SRC_ALPHA = SRC_ALPHA;
	public static final int _wGL_ONE_MINUS_SRC_ALPHA = ONE_MINUS_SRC_ALPHA;
	public static final int _wGL_ONE_MINUS_DST_COLOR = ONE_MINUS_DST_COLOR;
	public static final int _wGL_ONE_MINUS_SRC_COLOR = ONE_MINUS_SRC_COLOR;
	public static final int _wGL_ZERO = ZERO;
	public static final int _wGL_CULL_FACE = CULL_FACE;
	public static final int _wGL_TEXTURE_MIN_FILTER = TEXTURE_MIN_FILTER;
	public static final int _wGL_TEXTURE_MAG_FILTER = TEXTURE_MAG_FILTER;
	public static final int _wGL_LINEAR = LINEAR;
	public static final int _wGL_EQUAL = EQUAL;
	public static final int _wGL_SRC_COLOR = SRC_COLOR;
	public static final int _wGL_ONE = ONE;
	public static final int _wGL_NEAREST = NEAREST;
	public static final int _wGL_CLAMP = CLAMP_TO_EDGE;
	public static final int _wGL_TEXTURE_WRAP_S = TEXTURE_WRAP_S;
	public static final int _wGL_TEXTURE_WRAP_T = TEXTURE_WRAP_T;
	public static final int _wGL_REPEAT = REPEAT;
	public static final int _wGL_DST_COLOR = DST_COLOR;
	public static final int _wGL_DST_ALPHA = DST_ALPHA;
	public static final int _wGL_FLOAT = FLOAT;
	public static final int _wGL_SHORT = SHORT;
	public static final int _wGL_TRIANGLES = TRIANGLES;
	public static final int _wGL_TRIANGLE_STRIP = TRIANGLE_STRIP;
	public static final int _wGL_TRIANGLE_FAN = TRIANGLE_FAN;
	public static final int _wGL_LINE_STRIP = LINE_STRIP;
	public static final int _wGL_LINES = LINES;
	public static final int _wGL_PACK_ALIGNMENT = PACK_ALIGNMENT;
	public static final int _wGL_UNPACK_ALIGNMENT = UNPACK_ALIGNMENT;
	public static final int _wGL_TEXTURE0 = TEXTURE0;
	public static final int _wGL_TEXTURE1 = TEXTURE1;
	public static final int _wGL_TEXTURE2 = TEXTURE2;
	public static final int _wGL_TEXTURE3 = TEXTURE3;
	public static final int _wGL_VIEWPORT = VIEWPORT;
	public static final int _wGL_VERTEX_SHADER = VERTEX_SHADER;
	public static final int _wGL_FRAGMENT_SHADER = FRAGMENT_SHADER;
	public static final int _wGL_ARRAY_BUFFER = ARRAY_BUFFER;
	public static final int _wGL_ELEMENT_ARRAY_BUFFER = ELEMENT_ARRAY_BUFFER;
	public static final int _wGL_STATIC_DRAW = STATIC_DRAW;
	public static final int _wGL_DYNAMIC_DRAW = DYNAMIC_DRAW;
	public static final int _wGL_INVALID_ENUM = INVALID_ENUM;
	public static final int _wGL_INVALID_VALUE= INVALID_VALUE;
	public static final int _wGL_INVALID_OPERATION = INVALID_OPERATION;
	public static final int _wGL_OUT_OF_MEMORY = OUT_OF_MEMORY;
	public static final int _wGL_CONTEXT_LOST_WEBGL = CONTEXT_LOST_WEBGL;
	public static final int _wGL_FRAMEBUFFER_COMPLETE = FRAMEBUFFER_COMPLETE;
	public static final int _wGL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = FRAMEBUFFER_INCOMPLETE_ATTACHMENT;
	public static final int _wGL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT;
	public static final int _wGL_COLOR_ATTACHMENT0 = COLOR_ATTACHMENT0;
	public static final int _wGL_DEPTH_STENCIL_ATTACHMENT = DEPTH_STENCIL_ATTACHMENT;
	public static final int _wGL_DEPTH_STENCIL = DEPTH_STENCIL;
	public static final int _wGL_NEAREST_MIPMAP_LINEAR = NEAREST_MIPMAP_LINEAR; 
	public static final int _wGL_LINEAR_MIPMAP_LINEAR = LINEAR_MIPMAP_LINEAR; 
	public static final int _wGL_LINEAR_MIPMAP_NEAREST = LINEAR_MIPMAP_NEAREST; 
	public static final int _wGL_NEAREST_MIPMAP_NEAREST = NEAREST_MIPMAP_NEAREST;
	public static final int _wGL_TEXTURE_MAX_LEVEL = TEXTURE_MAX_LEVEL; 
	public static final int _wGL_UNSIGNED_INT_24_8 = UNSIGNED_INT_24_8;
	public static final int _wGL_UNSIGNED_INT = UNSIGNED_INT;
	public static final int _wGL_ANY_SAMPLES_PASSED = ANY_SAMPLES_PASSED; 
	public static final int _wGL_QUERY_RESULT = QUERY_RESULT;
	public static final int _wGL_QUERY_RESULT_AVAILABLE = QUERY_RESULT_AVAILABLE;
	public static final int _wGL_TEXTURE_MAX_ANISOTROPY = TEXTURE_MAX_ANISOTROPY_EXT;
	public static final int _wGL_DEPTH24_STENCIL8 = DEPTH24_STENCIL8;
	public static final int _wGL_DEPTH_COMPONENT32F = DEPTH_COMPONENT32F;
	public static final int _wGL_DEPTH_ATTACHMENT = DEPTH_ATTACHMENT;
	public static final int _wGL_MULTISAMPLE = -1;
	public static final int _wGL_LINE_SMOOTH = -1;
	public static final int _wGL_READ_FRAMEBUFFER = READ_FRAMEBUFFER;
	public static final int _wGL_DRAW_FRAMEBUFFER = DRAW_FRAMEBUFFER;
	public static final int _wGL_FRAMEBUFFER = FRAMEBUFFER;
	public static final int _wGL_POLYGON_OFFSET_FILL = POLYGON_OFFSET_FILL;
	
	public static final class TextureGL { 
		protected final WebGLTexture obj;
		public int w = -1;
		public int h = -1;
		public boolean nearest = true;
		public boolean anisotropic = false;
		protected TextureGL(WebGLTexture obj) { 
			this.obj = obj; 
		} 
	} 
	public static final class BufferGL { 
		protected final WebGLBuffer obj; 
		protected BufferGL(WebGLBuffer obj) { 
			this.obj = obj; 
		} 
	} 
	public static final class ShaderGL { 
		protected final WebGLShader obj; 
		protected ShaderGL(WebGLShader obj) { 
			this.obj = obj; 
		} 
	}
	private static int progId = 0;
	public static final class ProgramGL { 
		protected final WebGLProgram obj; 
		protected final int hashcode; 
		protected ProgramGL(WebGLProgram obj) { 
			this.obj = obj; 
			this.hashcode = ++progId;
		} 
	} 
	public static final class UniformGL { 
		protected final WebGLUniformLocation obj; 
		protected UniformGL(WebGLUniformLocation obj) { 
			this.obj = obj; 
		} 
	} 
	public static final class BufferArrayGL { 
		protected final WebGLVertexArray obj; 
		public boolean isQuadBufferBound; 
		protected BufferArrayGL(WebGLVertexArray obj) { 
			this.obj = obj; 
			this.isQuadBufferBound = false; 
		} 
	} 
	public static final class FramebufferGL { 
		protected final WebGLFramebuffer obj; 
		protected FramebufferGL(WebGLFramebuffer obj) { 
			this.obj = obj; 
		} 
	} 
	public static final class RenderbufferGL { 
		protected final WebGLRenderbuffer obj; 
		protected RenderbufferGL(WebGLRenderbuffer obj) { 
			this.obj = obj; 
		} 
	} 
	public static final class QueryGL { 
		protected final WebGLQuery obj; 
		protected QueryGL(WebGLQuery obj) { 
			this.obj = obj; 
		} 
	}
	
	public static final boolean anisotropicFilteringSupported() {
		return anisotropicFilteringSupported;
	}
	public static final void _wglEnable(int p1) {
		webgl.enable(p1);
	}
	public static final void _wglClearDepth(float p1) {
		webgl.clearDepth(p1);
	}
	public static final void _wglDepthFunc(int p1) {
		webgl.depthFunc(p1);
	}
	public static final void _wglCullFace(int p1) {
		webgl.cullFace(p1);
	}
	private static int[] viewportCache = new int[4];
	public static final void _wglViewport(int p1, int p2, int p3, int p4) {
		viewportCache[0] = p1; viewportCache[1] = p2;
		viewportCache[2] = p3; viewportCache[3] = p4;
		webgl.viewport(p1, p2, p3, p4);
	}
	public static final void _wglClear(int p1) {
		webgl.clear(p1);
	}
	public static final void _wglClearColor(float p1, float p2, float p3, float p4) {
		webgl.clearColor(p1, p2, p3, p4);
	}
	public static final void _wglDisable(int p1) {
		webgl.disable(p1);
	}
	public static final int _wglGetError() {
		return webgl.getError();
	}
	public static final void _wglFlush() {
		//webgl.flush();
	}
	private static Uint8Array uploadBuffer = Uint8Array.create(ArrayBuffer.create(4 * 1024 * 1024));
	public static final void _wglTexImage2D(int p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8, ByteBuffer p9) {
		if(p9 == null) {
			webgl.texImage2D(p1, p2, p3, p4, p5, p6, p7, p8, null);
		}else {
			int len = p9.remaining();
			Uint8Array uploadBuffer1 = uploadBuffer;
			for(int i = 0; i < len; ++i) {
				uploadBuffer1.set(i, (short) ((int)p9.get() & 0xff));
			}
			Uint8Array data = Uint8Array.create(uploadBuffer.getBuffer(), 0, len);
			webgl.texImage2D(p1, p2, p3, p4, p5, p6, p7, p8, data);
		}
	}
	public static final void _wglBlendFunc(int p1, int p2) {
		webgl.blendFunc(p1, p2);
	}
	public static final void _wglBlendColor(float r, float g, float b, float a) {
		webgl.blendColor(r, g, b, a);
	}
	public static final void _wglDepthMask(boolean p1) {
		webgl.depthMask(p1);
	}
	public static final void _wglColorMask(boolean p1, boolean p2, boolean p3, boolean p4) {
		webgl.colorMask(p1, p2, p3, p4);
	}
	public static final void _wglBindTexture(int p1, TextureGL p2) {
		webgl.bindTexture(p1, p2 == null ? null : p2.obj);
	}
	public static final void _wglCopyTexSubImage2D(int p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8) {
		webgl.copyTexSubImage2D(p1, p2, p3, p4, p5, p6, p7, p8);
	}
	public static final void _wglTexParameteri(int p1, int p2, int p3) {
		webgl.texParameteri(p1, p2, p3);
	}
	public static final void _wglTexParameterf(int p1, int p2, float p3) {
		webgl.texParameterf(p1, p2, p3);
	}
	public static final void _wglTexImage2D(int p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8, IntBuffer p9) {
		int len = p9.remaining();
		DataView deevis = DataView.create(uploadBuffer.getBuffer());
		for(int i = 0; i < len; ++i) {
			deevis.setInt32(i * 4, p9.get(), true);
		}
		Uint8Array data = Uint8Array.create(uploadBuffer.getBuffer(), 0, len*4);
		webgl.texImage2D(p1, p2, p3, p4, p5, p6, p7, p8, data);
	}
	public static final void _wglTexSubImage2D(int p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8, IntBuffer p9) {
		int len = p9.remaining();
		DataView deevis = DataView.create(uploadBuffer.getBuffer());
		for(int i = 0; i < len; ++i) {
			deevis.setInt32(i * 4, p9.get(), true);
		}
		Uint8Array data = Uint8Array.create(uploadBuffer.getBuffer(), 0, len*4);
		webgl.texSubImage2D(p1, p2, p3, p4, p5, p6, p7, p8, data);
	}
	public static final void _wglDeleteTextures(TextureGL p1) {
		webgl.deleteTexture(p1.obj);
	}
	public static final void _wglDrawArrays(int p1, int p2, int p3) {
		webgl.drawArrays(p1, p2, p3);
	}
	public static final void _wglDrawElements(int p1, int p2, int p3, int p4) {
		webgl.drawElements(p1, p2, p3, p4);
	}
	public static final TextureGL _wglGenTextures() {
		return new TextureGL(webgl.createTexture());
	}
	public static final void _wglTexSubImage2D(int p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8, ByteBuffer p9) {
		int len = p9.remaining();
		for(int i = 0; i < len; ++i) {
			//uploadBuffer.set(swapEndian ? ((i >> 2) + (3 - (i & 3))) : i, (short) ((int)p9.get() & 0xff));
			uploadBuffer.set(i, (short) ((int)p9.get() & 0xff));
		}
		Uint8Array data = Uint8Array.create(uploadBuffer.getBuffer(), 0, len);
		webgl.texSubImage2D(p1, p2, p3, p4, p5, p6, p7, p8, data);
	}
	public static final void _wglActiveTexture(int p1) {
		webgl.activeTexture(p1);
	}
	public static final ProgramGL _wglCreateProgram() {
		return new ProgramGL(webgl.createProgram());
	}
	public static final ShaderGL _wglCreateShader(int p1) {
		return new ShaderGL(webgl.createShader(p1));
	}
	public static final void _wglAttachShader(ProgramGL p1, ShaderGL p2) {
		webgl.attachShader(p1.obj, p2.obj);
	}
	public static final void _wglDetachShader(ProgramGL p1, ShaderGL p2) {
		webgl.detachShader(p1.obj, p2.obj);
	}
	public static final void _wglCompileShader(ShaderGL p1) {
		webgl.compileShader(p1.obj);
	}
	public static final void _wglLinkProgram(ProgramGL p1) {
		webgl.linkProgram(p1.obj);
	}
	public static final void _wglShaderSource(ShaderGL p1, String p2) {
		webgl.shaderSource(p1.obj, p2);
	}
	public static final String _wglGetShaderInfoLog(ShaderGL p1) {
		return webgl.getShaderInfoLog(p1.obj);
	}
	public static final String _wglGetProgramInfoLog(ProgramGL p1) {
		return webgl.getProgramInfoLog(p1.obj);
	}
	public static final boolean _wglGetShaderCompiled(ShaderGL p1) {
		return webgl.getShaderParameteri(p1.obj, COMPILE_STATUS) == 1;
	}
	public static final boolean _wglGetProgramLinked(ProgramGL p1) {
		return webgl.getProgramParameteri(p1.obj, LINK_STATUS) == 1;
	}
	public static final void _wglDeleteShader(ShaderGL p1) {
		webgl.deleteShader(p1.obj);
	}
	public static final void _wglDeleteProgram(ProgramGL p1) {
		webgl.deleteProgram(p1.obj);
	}
	public static final BufferGL _wglCreateBuffer() {
		return new BufferGL(webgl.createBuffer());
	}
	public static final void _wglDeleteBuffer(BufferGL p1) {
		webgl.deleteBuffer(p1.obj);
	}
	public static final void _wglBindBuffer(int p1, BufferGL p2) {
		webgl.bindBuffer(p1, p2 == null ? null : p2.obj);
	}
	public static final void _wglBufferData0(int p1, IntBuffer p2, int p3) {
		int len = p2.remaining();
		DataView deevis = DataView.create(uploadBuffer.getBuffer());
		for(int i = 0; i < len; ++i) {
			deevis.setInt32(i * 4, p2.get(), true);
		}
		Uint8Array data = Uint8Array.create(uploadBuffer.getBuffer(), 0, len*4);
		webgl.bufferData(p1, data, p3);
	}
	public static final void _wglBufferSubData0(int p1, int p2, IntBuffer p3) {
		int len = p3.remaining();
		DataView deevis = DataView.create(uploadBuffer.getBuffer());
		for(int i = 0; i < len; ++i) {
			deevis.setInt32(i * 4, p3.get(), true);
		}
		Uint8Array data = Uint8Array.create(uploadBuffer.getBuffer(), 0, len*4);
		webgl.bufferSubData(p1, p2, data);
	}
	public static final void _wglBufferData(int p1, Object p2, int p3) {
		webgl.bufferData(p1, (Int32Array)p2, p3);
	}
	public static final void _wglBufferSubData(int p1, int p2, Object p3) {
		webgl.bufferSubData(p1, p2, (Int32Array)p3);
	}
	public static final void _wglBindAttribLocation(ProgramGL p1, int p2, String p3) {
		webgl.bindAttribLocation(p1.obj, p2, p3);
	}
	public static final void _wglEnableVertexAttribArray(int p1) {
		webgl.enableVertexAttribArray(p1);
	}
	public static final void _wglDisableVertexAttribArray(int p1) {
		webgl.disableVertexAttribArray(p1);
	}
	public static final UniformGL _wglGetUniformLocation(ProgramGL p1, String p2) {
		WebGLUniformLocation u = webgl.getUniformLocation(p1.obj, p2);
		return u == null ? null : new UniformGL(u);
	}
	public static final void _wglBindAttributeLocation(ProgramGL p1, int p2, String p3) {
		webgl.bindAttribLocation(p1.obj, p2, p3);
	}
	public static final void _wglUniform1f(UniformGL p1, float p2) {
		if(p1 != null) webgl.uniform1f(p1.obj, p2);
	}
	public static final void _wglUniform2f(UniformGL p1, float p2, float p3) {
		if(p1 != null) webgl.uniform2f(p1.obj, p2, p3);
	}
	public static final void _wglUniform3f(UniformGL p1, float p2, float p3, float p4) {
		if(p1 != null) webgl.uniform3f(p1.obj, p2, p3, p4);
	}
	public static final void _wglUniform4f(UniformGL p1, float p2, float p3, float p4, float p5) {
		if(p1 != null) webgl.uniform4f(p1.obj, p2, p3, p4, p5);
	}
	public static final void _wglUniform1i(UniformGL p1, int p2) {
		if(p1 != null) webgl.uniform1i(p1.obj, p2);
	}
	public static final void _wglUniform2i(UniformGL p1, int p2, int p3) {
		if(p1 != null) webgl.uniform2i(p1.obj, p2, p3);
	}
	public static final void _wglUniform3i(UniformGL p1, int p2, int p3, int p4) {
		if(p1 != null) webgl.uniform3i(p1.obj, p2, p3, p4);
	}
	public static final void _wglUniform4i(UniformGL p1, int p2, int p3, int p4, int p5) {
		if(p1 != null) webgl.uniform4i(p1.obj, p2, p3, p4, p5);
	}
	private static Float32Array mat2 = Float32Array.create(4);
	private static Float32Array mat3 = Float32Array.create(9);
	private static Float32Array mat4 = Float32Array.create(16);
	public static final void _wglUniformMat2fv(UniformGL p1, float[] mat) {
		mat2.set(mat);
		if(p1 != null) webgl.uniformMatrix2fv(p1.obj, false, mat2);
	}
	public static final void _wglUniformMat3fv(UniformGL p1, float[] mat) {
		mat3.set(mat);
		if(p1 != null) webgl.uniformMatrix3fv(p1.obj, false, mat3);
	}
	public static final void _wglUniformMat4fv(UniformGL p1, float[] mat) {
		mat4.set(mat);
		if(p1 != null) webgl.uniformMatrix4fv(p1.obj, false, mat4);
	}
	private static int currentProgram = -1;
	public static final void _wglUseProgram(ProgramGL p1) {
		if(p1 != null && currentProgram != p1.hashcode) {
			currentProgram = p1.hashcode;
			webgl.useProgram(p1.obj);
		}
	}
	public static final void _wglGetParameter(int p1, int size, int[] ret) {
		if(p1 == _wGL_VIEWPORT) {
			ret[0] = viewportCache[0];
			ret[1] = viewportCache[1];
			ret[2] = viewportCache[2];
			ret[3] = viewportCache[3];
		}
	}
	public static final void _wglPolygonOffset(float p1, float p2) {
		webgl.polygonOffset(p1, p2);
	}
	public static final void _wglVertexAttribPointer(int p1, int p2, int p3, boolean p4, int p5, int p6) {
		webgl.vertexAttribPointer(p1, p2, p3, p4, p5, p6);
	}
	public static final void _wglBindFramebuffer(int p1, FramebufferGL p2) {
		webgl.bindFramebuffer(p1, p2 == null ? null : p2.obj);
	}
	public static final void _wglReadBuffer(int p1) {
		webgl.readBuffer(p1);
	}
	public static final FramebufferGL _wglCreateFramebuffer() {
		return new FramebufferGL(webgl.createFramebuffer());
	}
	public static final void _wglDeleteFramebuffer(FramebufferGL p1) {
		webgl.deleteFramebuffer(p1.obj);
	}
	public static final void _wglFramebufferTexture2D(int p1, TextureGL p2) {
		webgl.framebufferTexture2D(FRAMEBUFFER, p1, TEXTURE_2D, p2 == null ? null : p2.obj, 0);
	}
	public static final void _wglFramebufferTexture2D(int p1, TextureGL p2, int p3) {
		webgl.framebufferTexture2D(FRAMEBUFFER, p1, TEXTURE_2D, p2 == null ? null : p2.obj, p3);
	}
	public static final QueryGL _wglCreateQuery() { 
		return new QueryGL(webgl.createQuery()); 
	}
	public static final void _wglBeginQuery(int p1, QueryGL p2) { 
		webgl.beginQuery(p1, p2.obj); 
	}
	public static final void _wglEndQuery(int p1) { 
		webgl.endQuery(p1); 
	}
	public static final void _wglDeleteQuery(QueryGL p1) { 
		webgl.deleteQuery(p1.obj);
	}
	public static final int _wglGetQueryObjecti(QueryGL p1, int p2) { 
		return webgl.getQueryParameter(p1.obj, p2);
	}
	public static final BufferArrayGL _wglCreateVertexArray() {
		return new BufferArrayGL(webgl.createVertexArray());
	}
	public static final void _wglDeleteVertexArray(BufferArrayGL p1) {
		webgl.deleteVertexArray(p1.obj);
	}
	public static final void _wglBindVertexArray(BufferArrayGL p1) {
		webgl.bindVertexArray(p1 == null ? null : p1.obj);
	}
	public static final void _wglDrawBuffer(int p1) {
		webgl.drawBuffers(new int[] { p1 });
	}
	public static final RenderbufferGL _wglCreateRenderBuffer() {
		return new RenderbufferGL(webgl.createRenderbuffer());
	}
	public static final void _wglBindRenderbuffer(RenderbufferGL p1) {
		webgl.bindRenderbuffer(RENDERBUFFER, p1 == null ? null : p1.obj);
	}
	public static final void _wglRenderbufferStorage(int p1, int p2, int p3) {
		webgl.renderbufferStorage(RENDERBUFFER, p1, p2, p3);
	}
	public static final void _wglFramebufferRenderbuffer(int p1, RenderbufferGL p2) {
		webgl.framebufferRenderbuffer(FRAMEBUFFER, p1, RENDERBUFFER, p2 == null ? null : p2.obj);
	}
	public static final void _wglDeleteRenderbuffer(RenderbufferGL p1) {
		webgl.deleteRenderbuffer(p1.obj);
	}
	public static final void _wglRenderbufferStorageMultisample(int p1, int p2, int p3, int p4) {
		webgl.renderbufferStorageMultisample(RENDERBUFFER, p1, p2, p3, p4);
	}
	public static final void _wglBlitFramebuffer(int p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8, int p9, int p10) {
		webgl.blitFramebuffer(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10);
	}
	public static final int _wglGetAttribLocation(ProgramGL p1, String p2) {
		return webgl.getAttribLocation(p1.obj, p2);
	}
	
	@JSBody(params = { "ctx", "p" }, script = "return ctx.getTexParameter(0x0DE1, p) | 0;")
	private static final native int __wglGetTexParameteri(WebGL2RenderingContext ctx, int p);
	public static final int _wglGetTexParameteri(int p1) {
		return __wglGetTexParameteri(webgl, p1);
	}
	@JSBody(params = { "ctx", "p" }, script = "return (0.0 + ctx.getTexParameter(0x0DE1, p));")
	private static final native float __wglGetTexParameterf(WebGL2RenderingContext ctx, int p);
	public static final float _wglGetTexParameterf(int p1) {
		return __wglGetTexParameterf(webgl, p1);
	}
	public static final boolean isWindows() {
		return getNavString("platform").toLowerCase().contains("win");
	}
	public static final boolean glNeedsAnisotropicFix() {
		return anisotropicFilteringSupported && DetectAnisotropicGlitch.hasGlitch();
	}

	private static HTMLCanvasElement imageLoadCanvas = null;
	private static CanvasRenderingContext2D imageLoadContext = null;

	@JSBody(params = { "buf", "mime" }, script = "return URL.createObjectURL(new Blob([buf], {type: mime}));")
	private static native String getDataURL(ArrayBuffer buf, String mime);
	
	@JSBody(params = { "url" }, script = "URL.revokeObjectURL(url);")
	private static native void freeDataURL(String url);
	
	public static final EaglerImage loadPNG(byte[] data) {
		ArrayBuffer arr = ArrayBuffer.create(data.length);
		Uint8Array.create(arr).set(data);
		return loadPNG0(arr);
	}
	
	@Async
	private static native EaglerImage loadPNG0(ArrayBuffer data);
	
	private static void loadPNG0(ArrayBuffer data, final AsyncCallback<EaglerImage> ret) {
		final HTMLImageElement toLoad = (HTMLImageElement) doc.createElement("img");
		toLoad.addEventListener("load", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				if(imageLoadCanvas == null) {
					imageLoadCanvas = (HTMLCanvasElement) doc.createElement("canvas");
				}
				if(imageLoadCanvas.getWidth() < toLoad.getWidth()) {
					imageLoadCanvas.setWidth(toLoad.getWidth());
				}
				if(imageLoadCanvas.getHeight() < toLoad.getHeight()) {
					imageLoadCanvas.setHeight(toLoad.getHeight());
				}
				if(imageLoadContext == null) {
					imageLoadContext = (CanvasRenderingContext2D) imageLoadCanvas.getContext("2d");
				}
				imageLoadContext.clearRect(0, 0, toLoad.getWidth(), toLoad.getHeight());
				imageLoadContext.drawImage(toLoad, 0, 0, toLoad.getWidth(), toLoad.getHeight());
				ImageData pxlsDat = imageLoadContext.getImageData(0, 0, toLoad.getWidth(), toLoad.getHeight());
				Uint8ClampedArray pxls = pxlsDat.getData();
				int totalPixels = pxlsDat.getWidth() * pxlsDat.getHeight();
				freeDataURL(toLoad.getSrc());
				if(pxls.getByteLength() < totalPixels << 2) {
					ret.complete(null);
					return;
				}
				DataView dv = DataView.create(pxls.getBuffer());
				int[] pixels = new int[totalPixels];
				for(int i = 0, j; i < pixels.length; ++i) {
					j = dv.getUint32(i << 2, false);
					pixels[i] = ((j >> 8) & 0xFFFFFF) | ((j & 0xFF) << 24);
				}
				ret.complete(new EaglerImage(pixels, pxlsDat.getWidth(), pxlsDat.getHeight(), true));
			}
		});
		toLoad.addEventListener("error", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				freeDataURL(toLoad.getSrc());
				ret.complete(null);
			}
		});
		String src = getDataURL(data, "image/png");
		if(src == null) {
			ret.complete(null);
		}else {
			toLoad.setSrc(src);
		}
	}

	private static HTMLVideoElement currentVideo = null;
	private static TextureGL videoTexture = null;
	private static boolean videoIsLoaded = false;
	private static boolean videoTexIsInitialized = false;
	private static int frameRate = 33;
	private static long frameTimer = 0l;
	
	public static final boolean isVideoSupported() {
		return true;
	}
	public static final void loadVideo(String src, boolean autoplay) {
		loadVideo(src, autoplay, null, null);
	}
	public static final void loadVideo(String src, boolean autoplay, String setJavascriptPointer) {
		loadVideo(src, autoplay, setJavascriptPointer, null);
	}
	
	@JSBody(params = { "ptr", "el" }, script = "window[ptr] = el;")
	private static native void setVideoPointer(String ptr, HTMLVideoElement el);
	@JSBody(params = { "ptr", "el" }, script = "window[ptr](el);")
	private static native void callVideoLoadEvent(String ptr, HTMLVideoElement el);
	
	private static MediaElementAudioSourceNode currentVideoAudioSource = null;
	
	private static GainNode currentVideoAudioGain = null;
	private static float currentVideoAudioGainValue = 1.0f;
	
	private static PannerNode currentVideoAudioPanner = null;
	private static float currentVideoAudioX = 0.0f;
	private static float currentVideoAudioY = 0.0f;
	private static float currentVideoAudioZ = 0.0f;
	
	public static final void loadVideo(String src, boolean autoplay, String setJavascriptPointer, final String javascriptOnloadFunction) {
		videoIsLoaded = false;
		videoTexIsInitialized = false;
		if(videoTexture == null) {
			videoTexture = _wglGenTextures();
		}
		if(currentVideo != null) {
			currentVideo.pause();
			currentVideo.setSrc("");
		}
		
		BufferedVideo vid = videosBuffer.get(src);
		
		if(vid != null) {
			currentVideo = vid.videoElement;
			videosBuffer.remove(src);
		}else {
			currentVideo = (HTMLVideoElement) win.getDocument().createElement("video");
			currentVideo.setAttribute("crossorigin", "anonymous");
			currentVideo.setAutoplay(autoplay);
		}
		
		if(setJavascriptPointer != null) {
			setVideoPointer(setJavascriptPointer, currentVideo);
		}
		
		currentVideo.addEventListener("playing", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				videoIsLoaded = true;
				if(javascriptOnloadFunction != null) {
					callVideoLoadEvent(javascriptOnloadFunction, currentVideo);
				}
			}
		});
		
		if(vid == null) {
			currentVideo.setControls(false);
			currentVideo.setSrc(src);
		}else {
			if(autoplay) {
				currentVideo.play();
			}
		}
		
		if(currentVideoAudioSource != null) {
			currentVideoAudioSource.disconnect();
		}
		
		currentVideoAudioSource = audioctx.createMediaElementSource(currentVideo);
		
		if(currentVideoAudioGainValue < 0.0f) {
			currentVideoAudioSource.connect(masterVolumeNode);
		}else {
			if(currentVideoAudioGain == null) {
				currentVideoAudioGain = audioctx.createGain();
				currentVideoAudioGain.getGain().setValue(currentVideoAudioGainValue > 1.0f ? 1.0f : currentVideoAudioGainValue);
			}
			
			currentVideoAudioSource.connect(currentVideoAudioGain);
			
			if(currentVideoAudioPanner == null) {
				currentVideoAudioPanner = audioctx.createPanner();
				currentVideoAudioPanner.setRolloffFactor(1f);
				currentVideoAudioPanner.setDistanceModel("linear");
				currentVideoAudioPanner.setPanningModel("HRTF");
				currentVideoAudioPanner.setConeInnerAngle(360f);
				currentVideoAudioPanner.setConeOuterAngle(0f);
				currentVideoAudioPanner.setConeOuterGain(0f);
				currentVideoAudioPanner.setOrientation(0f, 1f, 0f);
				currentVideoAudioPanner.setPosition(currentVideoAudioX, currentVideoAudioY, currentVideoAudioZ);
				currentVideoAudioPanner.setMaxDistance(currentVideoAudioGainValue * 16f + 0.1f);
				currentVideoAudioGain.connect(currentVideoAudioPanner);
				currentVideoAudioPanner.connect(audioctx.getDestination());
			}
		}
		
	}
	
	private static class BufferedVideo {
		
		protected final HTMLVideoElement videoElement;
		protected final String url;
		protected final long requestedTime;
		protected final int ttl;
		
		public BufferedVideo(HTMLVideoElement videoElement, String url, int ttl) {
			this.videoElement = videoElement;
			this.url = url;
			this.requestedTime = System.currentTimeMillis();
			this.ttl = ttl;
		}
		
	}
	
	private static final HashMap<String, BufferedVideo> videosBuffer = new HashMap();
	
	public static final void bufferVideo(String src, int ttl) {
		if(!videosBuffer.containsKey(src)) {
			HTMLVideoElement video = (HTMLVideoElement) win.getDocument().createElement("video");
			video.setAutoplay(false);
			video.setAttribute("crossorigin", "anonymous");
			video.setPreload("auto");
			video.setControls(false);
			video.setSrc(src);
			videosBuffer.put(src, new BufferedVideo(video, src, ttl));
		}
	}
	
	public static final void unloadVideo() {
		if(videoTexture != null) {
			_wglDeleteTextures(videoTexture);
			videoTexture = null;
		}
		if(currentVideo != null) {
			currentVideo.pause();
			currentVideo.setSrc("");
			currentVideo = null;
		}
		if(currentVideoAudioSource != null) {
			currentVideoAudioSource.disconnect();
		}
	}
	public static final boolean isVideoLoaded() {
		return videoTexture != null && currentVideo != null && videoIsLoaded;
	}
	public static final boolean isVideoPaused() {
		return currentVideo == null || currentVideo.isPaused();
	}
	public static final void setVideoPaused(boolean pause) {
		if(currentVideo != null) {
			if(pause) {
				currentVideo.pause();
			}else {
				currentVideo.play();
			}
		}
	}
	public static final void setVideoLoop(boolean loop) {
		if(currentVideo != null) {
			currentVideo.setLoop(loop);
		}
	}
	public static final void setVideoVolume(float x, float y, float z, float v) {
		currentVideoAudioX = x;
		currentVideoAudioY = y;
		currentVideoAudioZ = z;
		if(v < 0.0f) {
			if(currentVideoAudioGainValue >= 0.0f && currentVideoAudioSource != null) {
				currentVideoAudioSource.disconnect();
				currentVideoAudioSource.connect(masterVolumeNode);
			}
			currentVideoAudioGainValue = v;
		}else {
			if(currentVideoAudioGain != null) {
				currentVideoAudioGain.getGain().setValue(v > 1.0f ? 1.0f : v);
				if(currentVideoAudioGainValue < 0.0f && currentVideoAudioSource != null) {
					currentVideoAudioSource.disconnect();
					currentVideoAudioSource.connect(currentVideoAudioGain);
				}
			}
			currentVideoAudioGainValue = v;
			if(currentVideoAudioPanner != null) {
				currentVideoAudioPanner.setMaxDistance(v * 16f + 0.1f);
				currentVideoAudioPanner.setPosition(x, y, z);
			}
		}
	}
	
	@JSBody(
		params = {"ctx", "target", "internalformat", "format", "type", "video"},
		script = "ctx.texImage2D(target, 0, internalformat, format, type, video);"
	)
	private static native void html5VideoTexImage2D(WebGL2RenderingContext ctx, int target, int internalformat, int format, int type, HTMLVideoElement video);

	@JSBody(
		params = {"ctx", "target", "format", "type", "video"},
		script = "ctx.texSubImage2D(target, 0, 0, 0, format, type, video);"
	)
	private static native void html5VideoTexSubImage2D(WebGL2RenderingContext ctx, int target, int format, int type, HTMLVideoElement video);
	
	public static final void updateVideoTexture() {
		long ms = System.currentTimeMillis();
		if(ms - frameTimer < frameRate && videoTexIsInitialized) {
			return;
		}
		frameTimer = ms;
		if(currentVideo != null && videoTexture != null && videoIsLoaded) {
			try {
				_wglBindTexture(_wGL_TEXTURE_2D, videoTexture);
				if(videoTexIsInitialized) {
					html5VideoTexSubImage2D(webgl, _wGL_TEXTURE_2D, _wGL_RGBA, _wGL_UNSIGNED_BYTE, currentVideo);
				}else {
					html5VideoTexImage2D(webgl, _wGL_TEXTURE_2D, _wGL_RGBA, _wGL_RGBA, _wGL_UNSIGNED_BYTE, currentVideo);
					_wglTexParameteri(_wGL_TEXTURE_2D, _wGL_TEXTURE_WRAP_S, _wGL_CLAMP);
					_wglTexParameteri(_wGL_TEXTURE_2D, _wGL_TEXTURE_WRAP_T, _wGL_CLAMP);
					_wglTexParameteri(_wGL_TEXTURE_2D, _wGL_TEXTURE_MIN_FILTER, _wGL_LINEAR);
					_wglTexParameteri(_wGL_TEXTURE_2D, _wGL_TEXTURE_MAG_FILTER, _wGL_LINEAR);
					videoTexIsInitialized = true;
				}
			}catch(Throwable t) {
				// rip
			}
		}
	}
	public static final void bindVideoTexture() {
		if(videoTexture != null) {
			_wglBindTexture(_wGL_TEXTURE_2D, videoTexture);
		}
	}
	public static final int getVideoWidth() {
		if(currentVideo != null && videoIsLoaded) {
			return currentVideo.getWidth();
		}else {
			return -1;
		}
	}
	public static final int getVideoHeight() {
		if(currentVideo != null && videoIsLoaded) {
			return currentVideo.getHeight();
		}else {
			return -1;
		}
	}
	public static final float getVideoCurrentTime() {
		if(currentVideo != null && videoIsLoaded) {
			return (float) currentVideo.getCurrentTime();
		}else {
			return -1.0f;
		}
	}
	public static final void setVideoCurrentTime(float seconds) {
		if(currentVideo != null && videoIsLoaded) {
			currentVideo.setCurrentTime(seconds);
		}
	}
	public static final float getVideoDuration() {
		if(currentVideo != null && videoIsLoaded) {
			return (float) currentVideo.getDuration();
		}else {
			return -1.0f;
		}
	}

	public static final int VIDEO_ERR_NONE = -1;
	public static final int VIDEO_ERR_ABORTED = 1;
	public static final int VIDEO_ERR_NETWORK = 2;
	public static final int VIDEO_ERR_DECODE = 3;
	public static final int VIDEO_ERR_SRC_NOT_SUPPORTED = 4;

	public static final int getVideoError() {
		if(currentVideo != null && videoIsLoaded) {
			MediaError err = currentVideo.getError();
			if(err != null) {
				return err.getCode();
			}else {
				return -1;
			}
		}else {
			return -1;
		}
	}
	
	public static final void setVideoFrameRate(float fps) {
		frameRate = (int)(1000.0f / fps);
		if(frameRate < 1) {
			frameRate = 1;
		}
	}

	private static HTMLImageElement currentImage = null;
	private static TextureGL imageTexture = null;
	private static boolean imageIsLoaded = false;
	private static boolean imageTexIsInitialized = false;
	private static int imageFrameRate = 33;
	private static long imageFrameTimer = 0l;

	public static final boolean isImageSupported() {
		return true;
	}
	public static final void loadImage(String src) {
		loadImage(src, null);
	}
	public static final void loadImage(String src, String setJavascriptPointer) {
		loadImage(src, setJavascriptPointer, null);
	}

	@JSBody(params = { "ptr", "el" }, script = "window[ptr] = el;")
	private static native void setImagePointer(String ptr, HTMLImageElement el);
	@JSBody(params = { "ptr", "el" }, script = "window[ptr](el);")
	private static native void callImageLoadEvent(String ptr, HTMLImageElement el);

	public static final void loadImage(String src, String setJavascriptPointer, final String javascriptOnloadFunction) {
		imageIsLoaded = false;
		imageTexIsInitialized = false;
		if(imageTexture == null) {
			imageTexture = _wglGenTextures();
		}
		if(currentImage != null) {
			currentImage.setSrc("");
		}

		BufferedImageElem img = imagesBuffer.get(src);

		if(img != null) {
			currentImage = img.imageElement;
			imagesBuffer.remove(src);
		}else {
			currentImage = (HTMLImageElement) win.getDocument().createElement("img");
			currentImage.setAttribute("crossorigin", "anonymous");
		}

		if(setJavascriptPointer != null) {
			setImagePointer(setJavascriptPointer, currentImage);
		}

		currentImage.addEventListener("load", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				imageIsLoaded = true;
				if(javascriptOnloadFunction != null) {
					callImageLoadEvent(javascriptOnloadFunction, currentImage);
				}
			}
		});

		if(img == null) {
			currentImage.setSrc(src);
		}
	}

	private static class BufferedImageElem {

		protected final HTMLImageElement imageElement;
		protected final String url;
		protected final long requestedTime;
		protected final int ttl;

		public BufferedImageElem(HTMLImageElement imageElement, String url, int ttl) {
			this.imageElement = imageElement;
			this.url = url;
			this.requestedTime = System.currentTimeMillis();
			this.ttl = ttl;
		}

	}

	private static final HashMap<String, BufferedImageElem> imagesBuffer = new HashMap();

	public static final void bufferImage(String src, int ttl) {
		if(!imagesBuffer.containsKey(src)) {
			HTMLImageElement image = (HTMLImageElement) win.getDocument().createElement("img");
			image.setAttribute("crossorigin", "anonymous");
			image.setSrc(src);
			imagesBuffer.put(src, new BufferedImageElem(image, src, ttl));
		}
	}

	public static final void unloadImage() {
		if(imageTexture != null) {
			_wglDeleteTextures(imageTexture);
			imageTexture = null;
		}
		if(currentImage != null) {
			currentImage.setSrc("");
			currentImage = null;
		}
	}
	public static final boolean isImageLoaded() {
		return imageTexture != null && currentImage != null && imageIsLoaded;
	}

	@JSBody(
			params = {"ctx", "target", "internalformat", "format", "type", "image"},
			script = "ctx.texImage2D(target, 0, internalformat, format, type, image);"
	)
	private static native void html5ImageTexImage2D(WebGL2RenderingContext ctx, int target, int internalformat, int format, int type, HTMLImageElement image);

	@JSBody(
			params = {"ctx", "target", "format", "type", "image"},
			script = "ctx.texSubImage2D(target, 0, 0, 0, format, type, image);"
	)
	private static native void html5ImageTexSubImage2D(WebGL2RenderingContext ctx, int target, int format, int type, HTMLImageElement image);

	public static final void updateImageTexture() {
		long ms = System.currentTimeMillis();
		if(ms - imageFrameTimer < imageFrameRate && imageTexIsInitialized) {
			return;
		}
		imageFrameTimer = ms;
		if(currentImage != null && imageTexture != null && imageIsLoaded) {
			try {
				_wglBindTexture(_wGL_TEXTURE_2D, imageTexture);
				if(imageTexIsInitialized) {
					html5ImageTexSubImage2D(webgl, _wGL_TEXTURE_2D, _wGL_RGBA, _wGL_UNSIGNED_BYTE, currentImage);
				}else {
					html5ImageTexImage2D(webgl, _wGL_TEXTURE_2D, _wGL_RGBA, _wGL_RGBA, _wGL_UNSIGNED_BYTE, currentImage);
					_wglTexParameteri(_wGL_TEXTURE_2D, _wGL_TEXTURE_WRAP_S, _wGL_CLAMP);
					_wglTexParameteri(_wGL_TEXTURE_2D, _wGL_TEXTURE_WRAP_T, _wGL_CLAMP);
					_wglTexParameteri(_wGL_TEXTURE_2D, _wGL_TEXTURE_MIN_FILTER, _wGL_LINEAR);
					_wglTexParameteri(_wGL_TEXTURE_2D, _wGL_TEXTURE_MAG_FILTER, _wGL_LINEAR);
					imageTexIsInitialized = true;
				}
			}catch(Throwable t) {
				// rip
			}
		}
	}
	public static final void bindImageTexture() {
		if(imageTexture != null) {
			_wglBindTexture(_wGL_TEXTURE_2D, imageTexture);
		}
	}
	public static final int getImageWidth() {
		if(currentImage != null && imageIsLoaded) {
			return currentImage.getWidth();
		}else {
			return -1;
		}
	}
	public static final int getImageHeight() {
		if(currentImage != null && imageIsLoaded) {
			return currentImage.getHeight();
		}else {
			return -1;
		}
	}

	public static final void setImageFrameRate(float fps) {
		frameRate = (int)(1000.0f / fps);
		if(frameRate < 1) {
			frameRate = 1;
		}
	}
	
	private static MouseEvent currentEvent = null;
	private static KeyboardEvent currentEventK = null;
	private static boolean[] buttonStates = new boolean[8];
	private static boolean[] keyStates = new boolean[256];
	public static final boolean mouseNext() {
		currentEvent = null;
		return !mouseEvents.isEmpty() && (currentEvent = mouseEvents.remove(0)) != null;
	}
	public static final int mouseGetEventButton() {
		if(currentEvent == null || currentEvent.getType().equals(MouseEvent.MOUSEMOVE)) return -1;
		int b = currentEvent.getButton();
		return b == 1 ? 2 : (b == 2 ? 1 : b);
	}
	public static final boolean mouseGetEventButtonState() {
		return currentEvent == null ? false : currentEvent.getType().equals(MouseEvent.MOUSEDOWN);
	}
	public static final boolean mouseIsButtonDown(int p1) {
		return buttonStates[p1];
	}
	public static final int mouseGetEventDWheel() {
		return ("wheel".equals(currentEvent.getType())) ? (((WheelEvent)currentEvent).getDeltaY() == 0.0D ? 0 : (((WheelEvent)currentEvent).getDeltaY() > 0.0D ? -1 : 1)) : 0;
	}
	public static final void mouseSetCursorPosition(int x, int y) {
		
	}
	private static long mouseUngrabTimer = 0l;
	private static int mouseUngrabTimeout = 0;
	public static final void mouseSetGrabbed(boolean grabbed) {
		if(grabbed) {
			canvas.requestPointerLock();
			long t = System.currentTimeMillis();
			if(mouseUngrabTimeout != 0) Window.clearTimeout(mouseUngrabTimeout);
			mouseUngrabTimeout = 0;
			if(t - mouseUngrabTimer < 3000l) {
				mouseUngrabTimeout = Window.setTimeout(new TimerHandler() {
					@Override
					public void onTimer() {
						canvas.requestPointerLock();
					}
				}, 3000 - (int)(t - mouseUngrabTimer));
			}
		}else {
			mouseUngrabTimer = System.currentTimeMillis();
			if(mouseUngrabTimeout != 0) Window.clearTimeout(mouseUngrabTimeout);
			mouseUngrabTimeout = 0;
			doc.exitPointerLock();
		}
	}
	public static final int mouseGetDX() {
		double dx = mouseDX;
		mouseDX = 0.0D;
		return (int)dx;
	}
	public static final int mouseGetDY() {
		double dy = mouseDY;
		mouseDY = 0.0D;
		return (int)dy;
	}
	public static final int mouseGetX() {
		return mouseX;
	}
	public static final int mouseGetY() {
		return mouseY;
	}
	public static final int mouseGetEventX() {
		return currentEvent == null ? -1 : (int)(currentEvent.getClientX() * win.getDevicePixelRatio());
	}
	public static final int mouseGetEventY() {
		return currentEvent == null ? -1 : (int)((canvas.getClientHeight() - currentEvent.getClientY()) * win.getDevicePixelRatio());
	}
	public static final boolean keysNext() {
		if(unpressCTRL) { //un-press ctrl after copy/paste permission
			keyEvents.clear();
			currentEventK = null;
			keyStates[29] = false;
			keyStates[157] = false;
			keyStates[28] = false;
			keyStates[219] = false;
			keyStates[220] = false;
			unpressCTRL = false;
			return false;
		}
		currentEventK = null;
		return !keyEvents.isEmpty() && (currentEventK = keyEvents.remove(0)) != null;
	}
	public static final int getEventKey() {
		return currentEventK == null ? -1 : remapKey(getWhich(currentEventK));
	}
	public static final char getEventChar() {
		if(currentEventK == null) return '\0';
		String s = currentEventK.getKey();
		return currentEventK == null ? ' ' : (char) (s.length() > 1 ? '\0' : s.charAt(0));
	}
	public static final boolean getEventKeyState() {
		return currentEventK == null? false : !currentEventK.getType().equals("keyup");
	}
	public static final boolean isKeyDown(int p1) {
		if(unpressCTRL) { //un-press ctrl after copy/paste permission
			keyStates[28] = false;
			keyStates[29] = false;
			keyStates[157] = false;
			keyStates[219] = false;
			keyStates[220] = false;
		}
		return keyStates[p1];
	}
	public static final String getKeyName(int p1) {
		return (p1 >= 0 && p1 < 256) ? LWJGLKeyNames[p1] : "null";
	}
	public static final void setFullscreen(boolean p1) {
		Window.alert("use F11 to enter fullscreen");
	}
	public static final boolean shouldShutdown() {
		return false;
	}
	
	@JSBody(params = { "obj" }, script = "if(obj.commit) obj.commit();")
	private static native int commitContext(JSObject obj);
	
	public static final void updateDisplay() {
		//commitContext(webgl);
		double r = win.getDevicePixelRatio();
		int w = parent.getClientWidth();
		int h = parent.getClientHeight();
		int w2 = (int)(w * r);
		int h2 = (int)(h * r);
		if(canvas.getWidth() != w2) {
			canvas.setWidth(w2);
		}
		if(canvas.getHeight() != h2) {
			canvas.setHeight(h2);
		}
		frameBuffer.drawImage(renderingCanvas, 0, 0, w2, h2);
		if(renderingCanvas.getWidth() != w2) {
			renderingCanvas.setWidth(w2);
		}
		if(renderingCanvas.getHeight() != h2) {
			renderingCanvas.setHeight(h2);
		}
		try {
			Thread.sleep(1l);
		} catch (InterruptedException e) {
			;
		}
	}
	public static final float getContentScaling() {
		 return (float)win.getDevicePixelRatio();
	}
	public static final void setVSyncEnabled(boolean p1) {
		
	} 
	public static final void enableRepeatEvents(boolean b) {
		enableRepeatEvents = b;
	}
	
	@JSBody(params = { }, script = "return document.pointerLockElement != null;")
	public static native boolean isPointerLocked();
	
	private static boolean pointerLockFlag = false;
	
	public static final boolean isFocused() {
		boolean yee = isPointerLocked();
		boolean dee = pointerLockFlag;
		pointerLockFlag = yee;
		if(!dee && yee) {
			mouseDX = 0.0D;
			mouseDY = 0.0D;
		}
		return isWindowFocused && !(dee && !yee);
	}
	public static final int getScreenWidth() {
		return win.getScreen().getAvailWidth();
	}
	public static final int getScreenHeight() {
		return win.getScreen().getAvailHeight();
	}
	public static final int getCanvasWidth() {
		return renderingCanvas.getWidth();
	}
	public static final int getCanvasHeight() {
		return renderingCanvas.getHeight();
	}
	public static final void setDisplaySize(int x, int y) {
		
	}
	public static final void syncDisplay(int performanceToFps) {
		
	}
	
	private static final DateFormat dateFormatSS = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
	public static final void saveScreenshot() {
		saveScreenshot("screenshot_" + dateFormatSS.format(new Date()).toString() + ".png", canvas);
	}
	
	@JSBody(params = { "name", "cvs" }, script = "var a=document.createElement(\"a\");a.href=cvs.toDataURL(\"image/png\");a.download=name;a.click();")
	private static native void saveScreenshot(String name, HTMLCanvasElement cvs);

	public static enum RateLimit {
		NONE, FAILED, BLOCKED, FAILED_POSSIBLY_LOCKED, LOCKED, NOW_LOCKED;
	}

	private static final Set<String> rateLimitedAddresses = new HashSet();
	private static final Set<String> blockedAddresses = new HashSet();
	
	private static WebSocket sock = null;
	private static boolean sockIsConnecting = false;
	private static boolean sockIsConnected = false;
	private static boolean sockIsAlive = false;
	private static LinkedList<byte[]> readPackets = new LinkedList();
	private static RateLimit rateLimitStatus = null;
	private static String currentSockURI = null;
	
	public static final RateLimit getRateLimitStatus() {
		RateLimit l = rateLimitStatus;
		rateLimitStatus = null;
		return l;
	}
	public static final void logRateLimit(String addr, RateLimit l) {
		if(l == RateLimit.BLOCKED) {
			blockedAddresses.add(addr);
		}else {
			rateLimitedAddresses.add(addr);
		}
	}
	public static final RateLimit checkRateLimitHistory(String addr) {
		if(blockedAddresses.contains(addr)) {
			return RateLimit.LOCKED;
		}else if(rateLimitedAddresses.contains(addr)) {
			return RateLimit.BLOCKED;
		}else {
			return RateLimit.NONE;
		}
	}
	
	@Async
	public static native String connectWebSocket(String sockURI);
	
	private static void connectWebSocket(String sockURI, final AsyncCallback<String> cb) {
		sockIsConnecting = true;
		sockIsConnected = false;
		sockIsAlive = false;
		rateLimitStatus = null;
		currentSockURI = sockURI;
		try {
			sock = WebSocket.create(sockURI);
		} catch(Throwable t) {
			sockIsConnecting = false;
			sockIsAlive = false;
			return;
		}
		sock.setBinaryType("arraybuffer");
		sock.onOpen(new EventListener<MessageEvent>() {
			@Override
			public void handleEvent(MessageEvent evt) {
				sockIsConnecting = false;
				sockIsAlive = false;
				sockIsConnected = true;
				readPackets.clear();
				cb.complete("okay");
			}
		});
		sock.onClose(new EventListener<CloseEvent>() {
			@Override
			public void handleEvent(CloseEvent evt) {
				sock = null;
				if(sockIsConnecting) {
					if(rateLimitStatus == null) {
						if(blockedAddresses.contains(currentSockURI)) {
							rateLimitStatus = RateLimit.LOCKED;
						}else if(rateLimitedAddresses.contains(currentSockURI)) {
							rateLimitStatus = RateLimit.FAILED_POSSIBLY_LOCKED;
						}else {
							rateLimitStatus = RateLimit.FAILED;
						}
					}
				}else if(!sockIsAlive) {
					if(rateLimitStatus == null) {
						if(blockedAddresses.contains(currentSockURI)) {
							rateLimitStatus = RateLimit.LOCKED;
						}else if(rateLimitedAddresses.contains(currentSockURI)) {
							rateLimitStatus = RateLimit.BLOCKED;
						}
					}
				}
				boolean b = sockIsConnecting;
				sockIsConnecting = false;
				sockIsConnected = false;
				sockIsAlive = false;
				if(b) cb.complete("fail");
			}
		});
		sock.onMessage(new EventListener<MessageEvent>() {
			@Override
			public void handleEvent(MessageEvent evt) {
				sockIsAlive = true;
				if(isString(evt.getData())) {
					String stat = evt.getDataAsString();
					if(stat.equalsIgnoreCase("BLOCKED")) {
						if(rateLimitStatus == null) {
							rateLimitStatus = RateLimit.BLOCKED;
						}
						rateLimitedAddresses.add(currentSockURI);
					}else if(stat.equalsIgnoreCase("LOCKED")) {
						if(rateLimitStatus == null) {
							rateLimitStatus = RateLimit.NOW_LOCKED;
						}
						rateLimitedAddresses.add(currentSockURI);
						blockedAddresses.add(currentSockURI);
					}
					sockIsConnecting = false;
					sockIsConnected = false;
					sock.close();
					return;
				}
				Uint8Array a = Uint8Array.create(evt.getDataAsArray());
				byte[] b = new byte[a.getByteLength()];
				for(int i = 0; i < b.length; ++i) {
					b[i] = (byte) (a.get(i) & 0xFF);
				}
				readPackets.add(b);
			}
		});
	}
	
	public static final boolean startConnection(String uri) {
		String res = connectWebSocket(uri);
		return !"fail".equals(res);
	}
	public static final void endConnection() {
		if(sock == null || sock.getReadyState() == 3) {
			sockIsConnecting = false;
		}
		if(sock != null && !sockIsConnecting) sock.close();

		enableVoice(Voice.VoiceChannel.NONE);
	}
	public static final boolean connectionOpen() {
		if(IntegratedServer.doesChannelExist(EaglerProfile.username) && IntegratedServer.isWorldRunning()) {
			return true;
		}
		if(!EaglerAdapter.clientLANClosed()) {
			return true;
		}
		if(sock == null || sock.getReadyState() == 3) {
			sockIsConnecting = false;
		}
		return sock != null && !sockIsConnecting && sock.getReadyState() != 3;
	}
	@JSBody(params = { "sock", "buffer" }, script = "sock.send(buffer);")
	private static native void nativeBinarySend(WebSocket sock, ArrayBuffer buffer);
	public static final void writePacket(byte[] packet) {
		if(sock != null && !sockIsConnecting) {
			Uint8Array arr = Uint8Array.create(packet.length);
			arr.set(packet);
			nativeBinarySend(sock, arr.getBuffer());
		}
	}
	public static final byte[] readPacket() {
		if(!readPackets.isEmpty()) {
			return readPackets.remove(0);
		}else {
			return null;
		}
	}
	public static final byte[] loadLocalStorage(String key) {
		try {
			Storage strg = win.getLocalStorage();
			if(strg != null) {
				String s = strg.getItem("_eaglercraft."+key);
				if(s != null) {
					return Base64.decodeBase64(s);
				}else {
					return null;
				}
			}else {
				return null;
			}
		}catch(Throwable t) {
			return null;
		}
	}
	public static final void saveLocalStorage(String key, byte[] data) {
		try {
			Storage strg = win.getLocalStorage();
			if(strg != null) {
				strg.setItem("_eaglercraft."+key, Base64.encodeBase64String(data));
			}
		}catch(Throwable t) {
		}
	}
	public static final void openLink(String url) {
		SelfDefence.openWindowIgnore(url, "_blank");
	}
	public static final void redirectTo(String url) {
		win.getLocation().setFullURL(url);
	}
	
	@JSBody(params = { }, script = "window.onbeforeunload = function(){javaMethods.get('net.lax1dude.eaglercraft.adapter.EaglerAdapterImpl2.onWindowUnload()V').invoke();return false;};")
	private static native void onBeforeCloseRegister();

	@JSBody(params = { "ext", "mime" }, script = "window.eagsFileChooser.openFileChooser(ext, mime);")
	public static native void openFileChooser(String ext, String mime);
	
	@JSBody(params = { }, script = "return window.eagsFileChooser.getFileChooserResult != null;")
	public static final native boolean getFileChooserResultAvailable();
	
	public static final byte[] getFileChooserResult() {
		ArrayBuffer b = getFileChooserResult0();
		if(b == null) return null;
		Uint8Array array = Uint8Array.create(b);
		byte[] ret = new byte[array.getByteLength()];
		for(int i = 0; i < ret.length; ++i) {
			ret[i] = (byte) array.get(i);
		}
		return ret;
	}
	
	public static final void clearFileChooserResult() {
		getFileChooserResult0();
	}

	@JSBody(params = {  }, script = "var ret = window.eagsFileChooser.getFileChooserResult; window.eagsFileChooser.getFileChooserResult = null; return ret;")
	private static native ArrayBuffer getFileChooserResult0();

	@JSBody(params = { }, script = "var ret = window.eagsFileChooser.getFileChooserResultName; window.eagsFileChooser.getFileChooserResultName = null; return ret;")
	public static native String getFileChooserResultName();
	
	public static final void setListenerPos(float x, float y, float z, float vx, float vy, float vz, float pitch, float yaw) {
		float var2 = MathHelper.cos(-yaw * 0.017453292F);
		float var3 = MathHelper.sin(-yaw * 0.017453292F);
		float var4 = -MathHelper.cos(pitch * 0.017453292F);
		float var5 = MathHelper.sin(pitch * 0.017453292F);
		AudioListener l = audioctx.getListener();
		l.setPosition(x, y, z);
		l.setOrientation(-var3 * var4, -var5, -var2 * var4, 0.0f, 1.0f, 0.0f);
	}
	
	private static int playbackId = 0;
	private static int audioElementId = 0;
	private static final HashMap<String,AudioBufferX> loadedSoundFiles = new HashMap();
	private static AudioContext audioctx = null;
	private static GainNode masterVolumeNode = null;
	private static GainNode musicVolumeNode = null;
	private static float playbackOffsetDelay = 0.03f;
	
	public static final void setPlaybackOffsetDelay(float f) {
		playbackOffsetDelay = f;
	}

	private static final void setGainlessAudioVolume(float oldGain, float f, boolean music) {
		if (f != oldGain) {
			for (AudioSourceNodeX a : activeSoundEffects.values()) {
				if (a.music == music && a instanceof MediaElementAudioSourceNodeX && a.gain == null) {
					HTMLAudioElement aud = ((MediaElementAudioSourceNodeX) a).audio;
					float newVolume = 0.5F;
					if (oldGain == 0) {
						aud.setMuted(false);
						newVolume = f * aud.getVolume();
					} else if (f == 0) {
						aud.setMuted(true);
						newVolume = aud.getVolume() / oldGain;
					} else {
						newVolume = f * aud.getVolume() / oldGain;
					}
					aud.setVolume(newVolume > 1.0f ? 1.0f : newVolume);
				}
			}
		}
	}

	public static final void setMasterVolume(float f) {
		setGainlessAudioVolume(masterVolumeNode.getGain().getValue(), f, false);
		masterVolumeNode.getGain().setValue(f);
	}

	public static final void setMusicVolume(float f) {
		setGainlessAudioVolume(musicVolumeNode.getGain().getValue(), f, true);
		musicVolumeNode.getGain().setValue(f);
	}
	
	@Async
	public static native AudioBuffer decodeAudioAsync(ArrayBuffer buffer);
	
	private static void decodeAudioAsync(ArrayBuffer buffer, final AsyncCallback<AudioBuffer> cb) {
		audioctx.decodeAudioData(buffer, new DecodeSuccessCallback() {
			@Override
			public void onSuccess(AudioBuffer decodedData) {
				cb.complete(decodedData);
			}
		}, new DecodeErrorCallback() {
			@Override
			public void onError(JSObject error) {
				cb.complete(null);
			}
		});
	}
	
	private static final HashMap<Integer,AudioSourceNodeX> activeSoundEffects = new HashMap();

	private static class AudioBufferX {
		private final AudioBuffer buffer;
		private AudioBufferX(AudioBuffer buffer) {
			this.buffer = buffer;
		}
	}

	private static class AudioSourceNodeX {
		private final PannerNode panner;
		private final GainNode gain;
		private final boolean music;
		private AudioSourceNodeX(PannerNode panner, GainNode gain, boolean music) {
			this.panner = panner;
			this.gain = gain;
			this.music = music;
		}
	}

	private static class AudioBufferSourceNodeX extends AudioSourceNodeX {
		private final AudioBufferSourceNode source;
		private AudioBufferSourceNodeX(AudioBufferSourceNode source, PannerNode panner, GainNode gain, boolean music) {
			super(panner, gain, music);
			this.source = source;
		}
	}

	private static class MediaElementAudioSourceNodeX extends AudioSourceNodeX {
		private final MediaElementAudioSourceNode source;
		private final HTMLAudioElement audio;
		private MediaElementAudioSourceNodeX(MediaElementAudioSourceNode source, HTMLAudioElement audio, PannerNode panner, GainNode gain, boolean music) {
			super(panner, gain, music);
			this.source = source;
			this.audio = audio;
		}
	}

	@JSBody(params = { "playing", "volume" }, script = "window.dispatchEvent(new CustomEvent('eagTitleMusic', { detail: { playing: playing, volume: volume } }));return;")
	public static native void fireTitleMusicEvent(boolean playing, float volume);
	
	private static final AudioBuffer getBufferFor(String fileName) {
		AudioBufferX ret = loadedSoundFiles.get(fileName);
		if(ret == null) {
			byte[] file = loadResourceBytes(fileName);
			if(file == null) return null;
			Uint8Array buf = Uint8Array.create(file.length);
			buf.set(file);
			ret = new AudioBufferX(decodeAudioAsync(buf.getBuffer()));
			loadedSoundFiles.put(fileName, ret);
		}
		return ret.buffer;
	}
	public static final int beginPlayback(String fileName, float x, float y, float z, float volume, float pitch) {
		return beginPlayback(fileName, x, y, z, volume, pitch, false);
	}
	public static final int beginPlayback(String fileNamePre, float x, float y, float z, float volume, float pitch, boolean music) {
		if(fileNamePre.startsWith("/")) fileNamePre = fileNamePre.substring(1);
		String fileName = AssetRepository.fileNameOverrides.getOrDefault(fileNamePre, fileNamePre);
		AudioNode s;
		HTMLAudioElement audioElement = null;
		String lowerFileName = fileName.toLowerCase();
		boolean usingUrl = AssetRepository.fileNameOverrides.containsKey(fileNamePre) || lowerFileName.startsWith("http://") || lowerFileName.startsWith("https://") || lowerFileName.startsWith("blob:") || lowerFileName.startsWith("data:");
		if (usingUrl) {
			audioElement = (HTMLAudioElement) win.getDocument().createElement("audio");
			audioElement.setAutoplay(true);
			audioElement.setCrossOrigin("anonymous");
			audioElement.setSrc(fileName);
			s = audioctx.createMediaElementSource(audioElement);
			audioElement.setPlaybackRate(pitch);
		} else {
			AudioBuffer b = getBufferFor(fileName);
			if(b == null) return -1;
			s = audioctx.createBufferSource();
			((AudioBufferSourceNode) s).setBuffer(b);
			((AudioBufferSourceNode) s).getPlaybackRate().setValue(pitch);
		}
		ChannelMergerNode c = audioctx.createChannelMerger(1);
		PannerNode p = audioctx.createPanner();
		p.setPosition(x, y, z);
		p.setMaxDistance(volume * 16f + 0.1f);
		p.setRolloffFactor(1f);
		//p.setVelocity(0f, 0f, 0f);
		p.setDistanceModel("linear");
		p.setPanningModel("HRTF");
		p.setConeInnerAngle(360f);
		p.setConeOuterAngle(0f);
		p.setConeOuterGain(0f);
		p.setOrientation(0f, 1f, 0f);
		GainNode g = audioctx.createGain();
		g.getGain().setValue(volume > 1.0f ? 1.0f : volume);
		s.connect(c);
		c.connect(g);
		g.connect(p);
		p.connect(music ? musicVolumeNode : masterVolumeNode);
		if (!usingUrl) {
			((AudioBufferSourceNode) s).start(0.0d, playbackOffsetDelay);
		}
		final int theId = ++playbackId;
		if (usingUrl) {
			activeSoundEffects.put(theId, new MediaElementAudioSourceNodeX((MediaElementAudioSourceNode) s, audioElement, p, g, music));
			audioElement.addEventListener("canplay", new EventListener<Event>() {
				@Override
				public void handleEvent(Event evt) {
					if (activeSoundEffects.containsKey(theId)) {
						((MediaElementAudioSourceNodeX) activeSoundEffects.get(theId)).audio.play();
					}
				}
			});
			audioElement.addEventListener("ended", new EventListener<Event>() {
				@Override
				public void handleEvent(Event evt) {
					((MediaElementAudioSourceNodeX) activeSoundEffects.remove(theId)).audio.setSrc("");
				}
			});
		} else {
			activeSoundEffects.put(theId, new AudioBufferSourceNodeX((AudioBufferSourceNode) s, p, g, music));
			((AudioBufferSourceNode) s).setOnEnded(new EventListener<MediaEvent>() {
				@Override
				public void handleEvent(MediaEvent evt) {
					activeSoundEffects.remove(theId);
				}
			});
		}
		return theId;
	}
	public static final int beginPlaybackStatic(String fileName, float volume, float pitch) {
		return beginPlaybackStatic(fileName, volume, pitch, false);
	}
	public static final int beginPlaybackStatic(String fileNamePre, float volume, float pitch, boolean music) {
		if(fileNamePre.startsWith("/")) fileNamePre = fileNamePre.substring(1);
		String fileName = AssetRepository.fileNameOverrides.getOrDefault(fileNamePre, fileNamePre);
		AudioNode s = null;
		GainNode g = null;
		HTMLAudioElement audioElement = null;
		String lowerFileName = fileName.toLowerCase();
		boolean usingUrl = AssetRepository.fileNameOverrides.containsKey(fileNamePre) || lowerFileName.startsWith("http://") || lowerFileName.startsWith("https://") || lowerFileName.startsWith("blob:") || lowerFileName.startsWith("data:");
		if (usingUrl) {
			audioElement = (HTMLAudioElement) win.getDocument().createElement("audio");
			audioElement.setAutoplay(true);
			// audioElement.setCrossOrigin("anonymous");
			audioElement.setSrc(fileName);
			audioElement.setPlaybackRate(pitch);
		} else {
			AudioBuffer b = getBufferFor(fileName);
			if(b == null) return -1;
			s = audioctx.createBufferSource();
			((AudioBufferSourceNode) s).setBuffer(b);
			((AudioBufferSourceNode) s).getPlaybackRate().setValue(pitch);
			g = audioctx.createGain();
			g.getGain().setValue(volume > 1.0f ? 1.0f : volume);
			s.connect(g);
			g.connect(music ? musicVolumeNode : masterVolumeNode);
			((AudioBufferSourceNode) s).start(0.0d, playbackOffsetDelay);
		}

		final int theId = ++playbackId;
		if (usingUrl) {
			activeSoundEffects.put(theId, new MediaElementAudioSourceNodeX(null, audioElement, null, null, music));
			audioElement.addEventListener("canplay", new EventListener<Event>() {
				@Override
				public void handleEvent(Event evt) {
					if (activeSoundEffects.containsKey(theId)) {
						((MediaElementAudioSourceNodeX) activeSoundEffects.get(theId)).audio.play();
					}
				}
			});
			audioElement.addEventListener("ended", new EventListener<Event>() {
				@Override
				public void handleEvent(Event evt) {
					((MediaElementAudioSourceNodeX) activeSoundEffects.remove(theId)).audio.setSrc("");
				}
			});
		} else {
			activeSoundEffects.put(theId, new AudioBufferSourceNodeX(((AudioBufferSourceNode) s), null, g, music));
			((AudioBufferSourceNode) s).setOnEnded(new EventListener<MediaEvent>() {
				@Override
				public void handleEvent(MediaEvent evt) {
					activeSoundEffects.remove(theId);
				}
			});
		}
		return theId;
	}
	public static final void setPitch(int id, float pitch) {
		AudioSourceNodeX a = activeSoundEffects.get(id);
		if(a != null) {
			if (a instanceof AudioBufferSourceNodeX) {
				((AudioBufferSourceNodeX) a).source.getPlaybackRate().setValue(pitch);
			} else if (a instanceof MediaElementAudioSourceNodeX) {
				((MediaElementAudioSourceNodeX) a).audio.setPlaybackRate(pitch);
			}
		}
	}
	public static final void setVolume(int id, float volume) {
		AudioSourceNodeX a = activeSoundEffects.get(id);
		if(a != null) {
			if (a instanceof MediaElementAudioSourceNodeX && a.gain == null) {
				HTMLAudioElement audioElem = ((MediaElementAudioSourceNodeX) a).audio;
				float gainValue = (a.music ? musicVolumeNode : masterVolumeNode).getGain().getValue();
				float newVolume;
				if (gainValue == 0) {
					audioElem.setMuted(true);
					newVolume = volume;
				} else {
					audioElem.setMuted(false);
					newVolume = gainValue * volume;
				}
				audioElem.setVolume(newVolume > 1.0f ? 1.0f : volume);
			} else {
				a.gain.getGain().setValue(volume > 1.0f ? 1.0f : volume);
				if (a.panner != null) a.panner.setMaxDistance(volume * 16f + 0.1f);
			}
		}
	}
	public static final void moveSound(int id, float x, float y, float z, float vx, float vy, float vz) {
		AudioSourceNodeX a = activeSoundEffects.get(id);
		if(a != null && a.panner != null) {
			a.panner.setPosition(x, y, z);
			//a.panner.setVelocity(vx, vy, vz);
		}
	}
	public static final void endSound(int id) {
		AudioSourceNodeX a = activeSoundEffects.get(id);
		if(a != null) {
			if (a instanceof AudioBufferSourceNodeX) {
				((AudioBufferSourceNodeX) a).source.stop();
			} else if (a instanceof MediaElementAudioSourceNodeX) {
				((MediaElementAudioSourceNodeX) a).audio.pause();
				((MediaElementAudioSourceNodeX) a).audio.setSrc("");
			}
			activeSoundEffects.remove(id);
		}
	}
	public static final boolean isPlaying(int id) {
		return activeSoundEffects.containsKey(id);
	}
	public static final void openConsole() {
		Window.alert("Still under development");
	}

	private static EaglercraftVoiceClient voiceClient = null;
	
	private static boolean voiceAvailableStat = false;
	private static boolean voiceSignalHandlersInitialized = false;

	private static Consumer<byte[]> returnSignalHandler = null;

	private static final HashMap<String, AnalyserNode> voiceAnalysers = new HashMap<>();
	private static final HashMap<String, GainNode> voiceGains = new HashMap<>();
	private static final HashMap<String, PannerNode> voicePanners = new HashMap<>();
	private static final HashSet<String> nearbyPlayers = new HashSet<>();

	public static void clearVoiceAvailableStatus() {
		voiceAvailableStat = false;
	}

	public static void setVoiceSignalHandler(Consumer<byte[]> signalHandler) {
		returnSignalHandler = signalHandler;
	}

	public static final int VOICE_SIGNAL_ALLOWED = 0;
	public static final int VOICE_SIGNAL_REQUEST = 0;
	public static final int VOICE_SIGNAL_CONNECT = 1;
	public static final int VOICE_SIGNAL_DISCONNECT = 2;
	public static final int VOICE_SIGNAL_ICE = 3;
	public static final int VOICE_SIGNAL_DESC = 4;
	public static final int VOICE_SIGNAL_GLOBAL = 5;

	public static void handleVoiceSignal(byte[] data) {
		try {
			DataInputStream streamIn = new DataInputStream(new ByteArrayInputStream(data));
			int sig = streamIn.read();
			switch(sig) {
				case VOICE_SIGNAL_GLOBAL:
					if (enabledChannel != Voice.VoiceChannel.GLOBAL) return;
					String[] voicePlayers = new String[streamIn.readInt()];
					for(int i = 0; i < voicePlayers.length; i++) voicePlayers[i] = streamIn.readUTF();
					for (String username : voicePlayers) {
						// notice that literally everyone except for those already connected using voice chat will receive the request; however, ones using proximity will simply ignore it.
						sendVoiceRequestIfNeeded(username);
					}
					break;
				case VOICE_SIGNAL_ALLOWED:
					voiceAvailableStat = streamIn.read() == 1;
					String[] servs = new String[streamIn.read()];
					for(int i = 0; i < servs.length; i++) {
						servs[i] = streamIn.readUTF();
					}
					voiceClient.setICEServers(servs);
					break;
				case VOICE_SIGNAL_CONNECT:
					String peerId = streamIn.readUTF();
					try {
						boolean offer = streamIn.readBoolean();
						voiceClient.signalConnect(peerId, offer);
					} catch (EOFException e) { // this is actually a connect ANNOUNCE, not an absolute "yes please connect" situation
						if (enabledChannel == Voice.VoiceChannel.PROXIMITY && !nearbyPlayers.contains(peerId)) return;
						// send request to peerId
						sendVoiceRequest(peerId);
					}
					break;
				case VOICE_SIGNAL_DISCONNECT:
					String peerId2 = streamIn.readUTF();
					voiceClient.signalDisconnect(peerId2, true);
					break;
				case VOICE_SIGNAL_ICE:
					String peerId3 = streamIn.readUTF();
					String candidate = streamIn.readUTF();
					voiceClient.signalICECandidate(peerId3, candidate);
					break;
				case VOICE_SIGNAL_DESC:
					String peerId4 = streamIn.readUTF();
					String descJSON = streamIn.readUTF();
					voiceClient.signalDescription(peerId4, descJSON);
					break;
				default:
					System.err.println("Unknown voice signal packet '" + sig + "'!");
					break;
			}
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static final boolean voiceAvailable() {
		return voiceClient.voiceClientSupported() && voiceClient.getReadyState() != EaglercraftVoiceClient.READYSTATE_ABORTED;
	}
	public static final boolean voiceAllowed() {
		return voiceAvailableStat;
	}
	public static final boolean voiceRelayed() {
		return false;
	}
	private static Voice.VoiceChannel enabledChannel = Voice.VoiceChannel.NONE;

	public static final void addNearbyPlayer(String username) {
		recentlyNearbyPlayers.remove(username);
		if (nearbyPlayers.add(username)) {
			sendVoiceRequestIfNeeded(username);
		}
	}

	private static final void sendVoiceRequest(String username) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.write(VOICE_SIGNAL_REQUEST);
			dos.writeUTF(username);
			returnSignalHandler.accept(baos.toByteArray());
		} catch (IOException ignored) {  }
	}

	private static final void sendVoiceRequestIfNeeded(String username) {
		if (getVoiceStatus() == Voice.VoiceStatus.DISCONNECTED || getVoiceStatus() == Voice.VoiceStatus.UNAVAILABLE) return;
		if (!voiceGains.containsKey(username)) sendVoiceRequest(username);
	}

	private static final ExpiringSet<String> recentlyNearbyPlayers = new ExpiringSet<>(5000, new ExpiringSet.ExpiringEvent<String>() {
		@Override
		public void onExpiration(String username) {
			if (!nearbyPlayers.contains(username)) voiceClient.signalDisconnect(username, false);
		}
	});

	public static final void removeNearbyPlayer(String username) {
		if (nearbyPlayers.remove(username)) {
			if (getVoiceStatus() == Voice.VoiceStatus.DISCONNECTED || getVoiceStatus() == Voice.VoiceStatus.UNAVAILABLE) return;
			if (enabledChannel == Voice.VoiceChannel.PROXIMITY) recentlyNearbyPlayers.add(username);
		}
	}

	public static final void cleanupNearbyPlayers(HashSet<String> existingPlayers) {
		nearbyPlayers.stream().filter(un -> !existingPlayers.contains(un)).collect(Collectors.toSet()).forEach(EaglerAdapterImpl2::removeNearbyPlayer);
	}

	public static final void updateVoicePosition(String username, double x, double y, double z) {
		if (voicePanners.containsKey(username)) voicePanners.get(username).setPosition((float) x, (float) y, (float) z);
	}

	public static final void sendInitialVoice() {
		returnSignalHandler.accept(new byte[] { VOICE_SIGNAL_CONNECT });
		for (String username : nearbyPlayers) sendVoiceRequest(username);
	}

	public static final void enableVoice(Voice.VoiceChannel enable) {
		if (enabledChannel == enable) return;
		voiceClient.resetPeerStates();
		if (enabledChannel == Voice.VoiceChannel.PROXIMITY) {
			for (String username : nearbyPlayers) voiceClient.signalDisconnect(username, false);
			for (String username : recentlyNearbyPlayers) voiceClient.signalDisconnect(username, false);
			nearbyPlayers.clear();
			recentlyNearbyPlayers.clear();
			returnSignalHandler.accept(new byte[] { VOICE_SIGNAL_DISCONNECT });
		} else if(enabledChannel == Voice.VoiceChannel.GLOBAL) {
			Set<String> antiConcurrentModificationUsernames = new HashSet<>(voiceGains.keySet());
			for (String username : antiConcurrentModificationUsernames) voiceClient.signalDisconnect(username, false);
			returnSignalHandler.accept(new byte[] { VOICE_SIGNAL_DISCONNECT });
		}
		enabledChannel = enable;
		if(enable == Voice.VoiceChannel.NONE) {
			talkStatus = false;
		}else {
			if(!voiceSignalHandlersInitialized) {
				voiceSignalHandlersInitialized = true;
				voiceClient.setICECandidateHandler(new EaglercraftVoiceClient.ICECandidateHandler() {
					@Override
					public void call(String peerId, String candidate) {
						try {
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							DataOutputStream dat = new DataOutputStream(bos);
							dat.write(VOICE_SIGNAL_ICE);
							dat.writeUTF(peerId);
							dat.writeUTF(candidate);
							returnSignalHandler.accept(bos.toByteArray());
						}catch(IOException ex) {
						}
					}
				});
				voiceClient.setDescriptionHandler(new EaglercraftVoiceClient.DescriptionHandler() {
					@Override
					public void call(String peerId, String candidate) {
						try {
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							DataOutputStream dat = new DataOutputStream(bos);
							dat.write(VOICE_SIGNAL_DESC);
							dat.writeUTF(peerId);
							dat.writeUTF(candidate);
							returnSignalHandler.accept(bos.toByteArray());
						}catch(IOException ex) {
						}
					}
				});
				voiceClient.setPeerTrackHandler(new EaglercraftVoiceClient.PeerTrackHandler() {
					@Override
					public void call(String peerId, MediaStream audioStream) {
						if (enabledChannel == Voice.VoiceChannel.NONE) return;
						MediaStreamAudioSourceNode audioNode = audioctx.createMediaStreamSource(audioStream);
						AnalyserNode analyser = audioctx.createAnalyser();
						analyser.setSmoothingTimeConstant(0f);
						analyser.setFftSize(32);
						audioNode.connect(analyser);
						voiceAnalysers.put(peerId, analyser);
						if (enabledChannel == Voice.VoiceChannel.GLOBAL) {
							GainNode gain = audioctx.createGain();
							gain.getGain().setValue(getVoiceListenVolume());
							analyser.connect(gain);
							gain.connect(audioctx.getDestination());
							voiceGains.put(peerId, gain);
						} else if (enabledChannel == Voice.VoiceChannel.PROXIMITY) {
							PannerNode panner = audioctx.createPanner();
							panner.setRolloffFactor(1f);
							panner.setDistanceModel("linear");
							panner.setPanningModel("HRTF");
							panner.setConeInnerAngle(360f);
							panner.setConeOuterAngle(0f);
							panner.setConeOuterGain(0f);
							panner.setOrientation(0f, 1f, 0f);
							panner.setPosition(0, 0, 0);
							float vol = getVoiceListenVolume();
							panner.setMaxDistance(vol * 2 * getVoiceProximity() + 0.1f);
							GainNode gain = audioctx.createGain();
							gain.getGain().setValue(vol);
							analyser.connect(gain);
							gain.connect(panner);
							panner.connect(audioctx.getDestination());
							voiceGains.put(peerId, gain);
							voicePanners.put(peerId, panner);
						}
						if(mutedSet.contains(peerId)) voiceClient.mutePeer(peerId, true);
					}
				});
				voiceClient.setPeerDisconnectHandler(new EaglercraftVoiceClient.PeerDisconnectHandler() {
					@Override
					public void call(String peerId, boolean quiet) {
						if (voiceAnalysers.containsKey(peerId)) {
							voiceAnalysers.get(peerId).disconnect();
							voiceAnalysers.remove(peerId);
						}
						if (voiceGains.containsKey(peerId)) {
							voiceGains.get(peerId).disconnect();
							voiceGains.remove(peerId);
						}
						if (voicePanners.containsKey(peerId)) {
							voicePanners.get(peerId).disconnect();
							voicePanners.remove(peerId);
						}
						if (!quiet) {
							try {
								ByteArrayOutputStream bos = new ByteArrayOutputStream();
								DataOutputStream dat = new DataOutputStream(bos);
								dat.write(VOICE_SIGNAL_DISCONNECT);
								dat.writeUTF(peerId);
								returnSignalHandler.accept(bos.toByteArray());
							} catch (IOException ex) {
							}
						}
					}
				});
				voiceClient.initializeDevices();
			}
			sendInitialVoice();
		}
	}
	public static final Voice.VoiceChannel getVoiceChannel() {
		return enabledChannel;
	}
	public static final boolean voicePeerErrored() {
		return voiceClient.getPeerState() == EaglercraftVoiceClient.PEERSTATE_FAILED || voiceClient.getPeerStateConnect() == EaglercraftVoiceClient.PEERSTATE_FAILED || voiceClient.getPeerStateInitial() == EaglercraftVoiceClient.PEERSTATE_FAILED || voiceClient.getPeerStateDesc() == EaglercraftVoiceClient.PEERSTATE_FAILED || voiceClient.getPeerStateIce() == EaglercraftVoiceClient.PEERSTATE_FAILED;
	}
	public static final Voice.VoiceStatus getVoiceStatus() {
		return (!voiceAvailable() || !voiceAllowed()) ? Voice.VoiceStatus.UNAVAILABLE :
			(voiceClient.getReadyState() != EaglercraftVoiceClient.READYSTATE_DEVICE_INITIALIZED ?
					Voice.VoiceStatus.CONNECTING : (voicePeerErrored() ? Voice.VoiceStatus.UNAVAILABLE : Voice.VoiceStatus.CONNECTED));
	}

	private static boolean talkStatus = false;
	public static final void activateVoice(boolean talk) {
		if(talkStatus != talk) {
			voiceClient.activateVoice(talk);
		}
		talkStatus = talk;
	}

	private static int proximity = 16;
	public static final void setVoiceProximity(int prox) {
		for (PannerNode panner : voicePanners.values()) panner.setMaxDistance(getVoiceListenVolume() * 2 * prox + 0.1f);
		proximity = prox;
	}
	public static final int getVoiceProximity() {
		return proximity;
	}

	private static float volumeListen = 0.5f;
	public static final void setVoiceListenVolume(float f) {
		for (String username : voiceGains.keySet()) {
			GainNode gain = voiceGains.get(username);
			float val = f;
			if(val > 0.5f) val = 0.5f + (val - 0.5f) * 3.0f;
			if(val > 2.0f) val = 2.0f;
			if(val < 0.0f) val = 0.0f;
			gain.getGain().setValue(val * 2.0f);
			if (voicePanners.containsKey(username)) voicePanners.get(username).setMaxDistance(f * 2 * getVoiceProximity() + 0.1f);
		}
		volumeListen = f;
	}
	public static final float getVoiceListenVolume() {
		return volumeListen;
	}

	private static float volumeSpeak = 0.5f;
	public static final void setVoiceSpeakVolume(float f) {
		if(volumeSpeak != f) {
			voiceClient.setMicVolume(f);
		}
		volumeSpeak = f;
	}
	public static final float getVoiceSpeakVolume() {
		return volumeSpeak;
	}

	private static final Set<String> mutedSet = new HashSet();
	private static final Set<String> speakingSet = new HashSet();
	public static final Set<String> getVoiceListening() {
		return voiceGains.keySet();
	}
	public static final Set<String> getVoiceSpeaking() {
		return speakingSet;
	}
	public static final void setVoiceMuted(String username, boolean mute) {
		voiceClient.mutePeer(username, mute);
		if(mute) {
			mutedSet.add(username);
		}else {
			mutedSet.remove(username);
		}
	}
	public static final Set<String> getVoiceMuted() {
		return mutedSet;
	}
	public static final List<String> getVoiceRecent() {
		return new ArrayList<>(voiceGains.keySet());
	}

	public static final void tickVoice() {
		recentlyNearbyPlayers.checkForExpirations();
		speakingSet.clear();
		for (String username : voiceAnalysers.keySet()) {
			AnalyserNode analyser = voiceAnalysers.get(username);
			Uint8Array array = Uint8Array.create(analyser.getFrequencyBinCount());
			analyser.getByteFrequencyData(array);
			int len = array.getLength();
			for (int i = 0; i < len; i++) {
				if (array.get(i) >= 0.1f) {
					speakingSet.add(username);
					break;
				}
			}
		}
	}
	
	
	public static final void doJavascriptCoroutines() {
		
	}
	public static final long maxMemory() {
		return 1024*1024*1024;
	}
	public static final long totalMemory() {
		return 1024*1024*1024;
	}
	public static final long freeMemory() {
		return 0l;
	}
	public static final void exit() {
		
	}
	
	@JSBody(params = { }, script = "return window.navigator.userAgent;")
	public static native String getUserAgent();
	
	private static String[] LWJGLKeyNames = new String[] {"NONE", "ESCAPE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "MINUS", "EQUALS", "BACK", "TAB", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "LBRACKET", "RBRACKET", "RETURN", "LCONTROL", "A", "S", "D", "F", "G", "H", "J", "K", "L", "SEMICOLON", "APOSTROPHE", "GRAVE", "LSHIFT", "BACKSLASH", "Z", "X", "C", "V", "B", "N", "M", "COMMA", "PERIOD", "SLASH", "RSHIFT", "MULTIPLY", "LMENU", "SPACE", "CAPITAL", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "NUMLOCK", "SCROLL", "NUMPAD7", "NUMPAD8", "NUMPAD9", "SUBTRACT", "NUMPAD4", "NUMPAD5", "NUMPAD6", "ADD", "NUMPAD1", "NUMPAD2", "NUMPAD3", "NUMPAD0", "DECIMAL", "null", "null", "null", "F11", "F12", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "F13", "F14", "F15", "F16", "F17", "F18", "null", "null", "null", "null", "null", "null", "KANA", "F19", "null", "null", "null", "null", "null", "null", "null", "CONVERT", "null", "NOCONVERT", "null", "YEN", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "NUMPADEQUALS", "null", "null", "CIRCUMFLEX", "AT", "COLON", "UNDERLINE", "KANJI", "STOP", "AX", "UNLABELED", "null", "null", "null", "null", "NUMPADENTER", "RCONTROL", "null", "null", "null", "null", "null", "null", "null", "null", "null", "SECTION", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "NUMPADCOMMA", "null", "DIVIDE", "null", "SYSRQ", "RMENU", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "FUNCTION", "PAUSE", "null", "HOME", "UP", "PRIOR", "null", "LEFT", "null", "RIGHT", "null", "END", "DOWN", "NEXT", "INSERT", "DELETE", "null", "null", "null", "null", "null", "null", "CLEAR", "LMETA", "RMETA", "APPS", "POWER", "SLEEP", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null"};
	
	private static int[] LWJGLKeyCodes = new int[] {
			/* 0 */ -1, /* 1 */ -1, /* 2 */ -1, /* 3 */ -1, /* 4 */ -1,
			/* 5 */ -1, /* 6 */ -1, /* 7 */ -1, /* 8 */ 14, /* 9 */ 15,
			/* 10 */ -1, /* 11 */ -1, /* 12 */ -1, /* 13 */ 28, /* 14 */ -1,
			/* 15 */ -1, /* 16 */ 42, /* 17 */ 29, /* 18 */ 56, /* 19 */ -1,
			/* 20 */ -1, /* 21 */ -1, /* 22 */ -1, /* 23 */ -1, /* 24 */ -1,
			/* 25 */ -1, /* 26 */ -1, /* 27 */ 1, /* 28 */ -1, /* 29 */ -1,
			/* 30 */ -1, /* 31 */ -1, /* 32 */ 57, /* 33 */ 210, /* 34 */ 201,
			/* 35 */ 207, /* 36 */ 199, /* 37 */ 203, /* 38 */ 200, /* 39 */ 205,
			/* 40 */ 208, /* 41 */ 205, /* 42 */ 208, /* 43 */ -1, /* 44 */ -1,
			/* 45 */ 210, /* 46 */ 211, /* 47 */ 211, /* 48 */ 11, /* 49 */ 2,
			/* 50 */ 3, /* 51 */ 4, /* 52 */ 5, /* 53 */ 6, /* 54 */ 7,
			/* 55 */ 8, /* 56 */ 9, /* 57 */ 10, /* 58 */ -1, /* 59 */ -1,
			/* 60 */ -1, /* 61 */ -1, /* 62 */ -1, /* 63 */ -1, /* 64 */ -1,
			/* 65 */ 30, /* 66 */ 48, /* 67 */ 46, /* 68 */ 32, /* 69 */ 18,
			/* 70 */ 33, /* 71 */ 34, /* 72 */ 35, /* 73 */ 23, /* 74 */ 36,
			/* 75 */ 37, /* 76 */ 38, /* 77 */ 50, /* 78 */ 49, /* 79 */ 24,
			/* 80 */ 25, /* 81 */ 16, /* 82 */ 19, /* 83 */ 31, /* 84 */ 20,
			/* 85 */ 22, /* 86 */ 47, /* 87 */ 17, /* 88 */ 45, /* 89 */ 21,
			/* 90 */ 44, /* 91 */ -1, /* 92 */ -1, /* 93 */ -1, /* 94 */ -1,
			/* 95 */ -1, /* 96 */ -1, /* 97 */ -1, /* 98 */ -1, /* 99 */ -1,
			/* 100 */ -1, /* 101 */ -1, /* 102 */ -1, /* 103 */ -1, /* 104 */ -1,
			/* 105 */ -1, /* 106 */ -1, /* 107 */ -1, /* 108 */ -1, /* 109 */ 12,
			/* 110 */ 52, /* 111 */ 53, /* 112 */ -1, /* 113 */ -1, /* 114 */ -1,
			/* 115 */ -1, /* 116 */ -1, /* 117 */ -1, /* 118 */ -1, /* 119 */ -1,
			/* 120 */ -1, /* 121 */ -1, /* 122 */ -1, /* 123 */ -1, /* 124 */ -1,
			/* 125 */ -1, /* 126 */ -1, /* 127 */ -1, /* 128 */ -1, /* 129 */ -1,
			/* 130 */ -1, /* 131 */ -1, /* 132 */ -1, /* 133 */ -1, /* 134 */ -1,
			/* 135 */ -1, /* 136 */ -1, /* 137 */ -1, /* 138 */ -1, /* 139 */ -1,
			/* 140 */ -1, /* 141 */ -1, /* 142 */ -1, /* 143 */ -1, /* 144 */ -1,
			/* 145 */ -1, /* 146 */ -1, /* 147 */ -1, /* 148 */ -1, /* 149 */ -1,
			/* 150 */ -1, /* 151 */ -1, /* 152 */ -1, /* 153 */ -1, /* 154 */ -1,
			/* 155 */ -1, /* 156 */ -1, /* 157 */ -1, /* 158 */ -1, /* 159 */ -1,
			/* 160 */ -1, /* 161 */ -1, /* 162 */ -1, /* 163 */ -1, /* 164 */ -1,
			/* 165 */ -1, /* 166 */ -1, /* 167 */ -1, /* 168 */ -1, /* 169 */ -1,
			/* 170 */ -1, /* 171 */ -1, /* 172 */ -1, /* 173 */ -1, /* 174 */ -1,
			/* 175 */ -1, /* 176 */ -1, /* 177 */ -1, /* 178 */ -1, /* 179 */ -1,
			/* 180 */ -1, /* 181 */ -1, /* 182 */ -1, /* 183 */ -1, /* 184 */ -1,
			/* 185 */ -1, /* 186 */ 39, /* 187 */ 13, /* 188 */ 51, /* 189 */ 12,
			/* 190 */ 52, /* 191 */ 53, /* 192 */ -1, /* 193 */ -1, /* 194 */ -1,
			/* 195 */ -1, /* 196 */ -1, /* 197 */ -1, /* 198 */ -1, /* 199 */ -1,
			/* 200 */ -1, /* 201 */ -1, /* 202 */ -1, /* 203 */ -1, /* 204 */ -1,
			/* 205 */ -1, /* 206 */ -1, /* 207 */ -1, /* 208 */ -1, /* 209 */ -1,
			/* 210 */ -1, /* 211 */ -1, /* 212 */ -1, /* 213 */ -1, /* 214 */ -1,
			/* 215 */ -1, /* 216 */ -1, /* 217 */ -1, /* 218 */ -1, /* 219 */ 26,
			/* 220 */ 43, /* 221 */ 27, /* 222 */ 40
	};

	public static final int _wArrayByteLength(Object obj) {
		return ((Int32Array)obj).getByteLength();
	}
	
	public static final Object _wCreateLowLevelIntBuffer(int len) {
		return Int32Array.create(len);
	}
	
	private static int appendbufferindex = 0;
	private static Int32Array appendbuffer = Int32Array.create(ArrayBuffer.create(525000*4));

	public static final void _wAppendLowLevelBuffer(Object arr) {
		Int32Array a = ((Int32Array)arr);
		if(appendbufferindex + a.getLength() < appendbuffer.getLength()) {
			appendbuffer.set(a, appendbufferindex);
			appendbufferindex += a.getLength();
		}
	}
	
	public static final Object _wGetLowLevelBuffersAppended() {
		Int32Array ret = Int32Array.create(appendbuffer.getBuffer(), 0, appendbufferindex);
		appendbufferindex = 0;
		return ret;
	}
	
	private static int remapKey(int k) {
		return (k > LWJGLKeyCodes.length || k < 0) ? -1 : LWJGLKeyCodes[k];
	}
	
	public static final boolean isIntegratedServerAvailable() {
		return integratedServerScript != null;
	}
	
	@JSFunctor
	private static interface WorkerBinaryPacketHandler extends JSObject {
		public void onMessage(String channel, ArrayBuffer buf);
	}
	
	private static final HashMap<String,List<PKT>> workerMessageQueue = new HashMap();
	
	private static Worker server = null;
	private static boolean serverAlive = false;
	
	private static class WorkerBinaryPacketHandlerImpl implements WorkerBinaryPacketHandler {
		
		public void onMessage(String channel, ArrayBuffer buf) {
			if(channel == null) {
				System.err.println("Recieved IPC packet with null channel");
				return;
			}
			
			serverAlive = true;
			synchronized(workerMessageQueue) {
				List<PKT> existingQueue = workerMessageQueue.get(channel);
				
				if(existingQueue == null) {
					System.err.println("Recieved IPC packet with unknown '" + channel + "' channel");
					return;
				}
				
				if(buf == null) {
					System.err.println("Recieved IPC packet with null buffer");
					return;
				}
				
				Uint8Array a = Uint8Array.create(buf);
				byte[] pkt = new byte[a.getLength()];
				for(int i = 0; i < pkt.length; ++i) {
					pkt[i] = (byte) a.get(i);
				}
				
				existingQueue.add(new PKT(channel, pkt));
			}
		}
		
	}
	
	@JSBody(params = { "w", "wb" }, script = "w.onmessage = function(o) { wb(o.data.ch, o.data.dat); };")
	private static native void registerPacketHandler(Worker w, WorkerBinaryPacketHandler wb);

	@JSBody(params = { "w", "ch", "dat" }, script = "w.postMessage({ ch: ch, dat : dat });")
	private static native void sendWorkerPacket(Worker w, String channel, ArrayBuffer arr);
	
	@JSBody(params = { "w", "dbName" }, script = "w.postMessage({ worldDatabaseName : dbName });")
	private static native void sendWorkerStartPacket(Worker w, String dbName);
	
	private static String worldDatabaseName = "MAIN";
	
	public static final void beginLoadingIntegratedServer() {
		if(server != null) {
			server.terminate();
		}
		workerMessageQueue.put("IPC", new LinkedList<PKT>());
		server = Worker.create(integratedServerScript);
		server.onError(new EventListener<ErrorEvent>() {
			@Override
			public void handleEvent(ErrorEvent evt) {
				System.err.println("Worker Error: " + evt.getError());
			}
		});
		sendWorkerStartPacket(server, worldDatabaseName);
		registerPacketHandler(server, new WorkerBinaryPacketHandlerImpl());
	}
	
	public static final void setWorldDatabaseName(String name) {
		worldDatabaseName = name;
	}
	
	public static final boolean isIntegratedServerAlive() {
		return serverAlive && server != null;
	}
	
	public static final void terminateIntegratedServer() {
		if(server != null) {
			server.terminate();
			server = null;
			serverAlive = false;
		}
	}
	
	public static final void sendToIntegratedServer(String channel, byte[] pkt) {
		ArrayBuffer arb = ArrayBuffer.create(pkt.length);
		Uint8Array ar = Uint8Array.create(arb);
		ar.set(pkt);
		sendWorkerPacket(server, channel, arb);
		//System.out.println("[Client][WRITE][" + channel + "]: " + pkt.length);
	}
	
	public static final void enableChannel(String channel) {
		synchronized(workerMessageQueue) {
			if(workerMessageQueue.containsKey(channel)) {
				System.err.println("Tried to enable existing channel '" + channel + "' again");
			}else {
				System.out.println("[Client][ENABLE][" + channel + "]");
				workerMessageQueue.put(channel, new LinkedList());
			}
		}
	}
	
	public static final void disableChannel(String channel) {
		synchronized(workerMessageQueue) {
			if(workerMessageQueue.remove(channel) == null) {
				System.err.println("Tried to disable unknown channel '" + channel + "'");
			}
			System.out.println("[Client][DISABLE][" + channel + "]");
		}
	}
	
	public static final PKT recieveFromIntegratedServer(String channel) {
		synchronized(workerMessageQueue) {
			List<PKT> list = workerMessageQueue.get(channel);
			if(list == null) {
				System.err.println("Tried to read from unknown channel '" + channel + "'");
				return null;
			}else {
				return list.size() > 0 ? list.remove(0) : null;
			}
		}
	}
	
	@JSBody(params = { "name", "buf" }, script =
		"var hr = window.URL.createObjectURL(new Blob([buf], {type: \"octet/stream\"}));" +
		"var a = document.createElement(\"a\");" +
		"a.href = hr; a.download = name; a.click();" +
		"window.URL.revokeObjectURL(hr);")
	private static final native void downloadBytesImpl(String str, ArrayBuffer buf);
	
	public static final void downloadBytes(String str, byte[] dat) {
		ArrayBuffer d = ArrayBuffer.create(dat.length);
		Uint8Array.create(d).set(dat);
		downloadBytesImpl(str, d);
	}
	
	@JSFunctor
	private static interface StupidFunctionResolveString extends JSObject {
		void resolveStr(String s);
	}
	
	private static boolean unpressCTRL = false;
	
	@Async
	public static native String getClipboard();
	
	private static void getClipboard(final AsyncCallback<String> cb) {
		final long start = System.currentTimeMillis();
		getClipboard0(new StupidFunctionResolveString() {
			@Override
			public void resolveStr(String s) {
				if(System.currentTimeMillis() - start > 500l) {
					unpressCTRL = true;
				}
				cb.complete(s);
			}
		});
	}
	
	@JSBody(params = { "cb" }, script = "if(!window.navigator.clipboard) cb(null); else window.navigator.clipboard.readText().then(function(s) { cb(s); }, function(s) { cb(null); });")
	private static native void getClipboard0(StupidFunctionResolveString cb);
	
	@JSBody(params = { "str" }, script = "if(window.navigator.clipboard) window.navigator.clipboard.writeText(str);")
	public static native void setClipboard(String str);
	
	@JSBody(params = { "obj" }, script = "return typeof obj === \"string\";")
	private static native boolean isString(JSObject obj);
	
	private static class ServerQueryImpl implements ServerQuery {
		
		private final LinkedList<QueryResponse> queryResponses = new LinkedList();
		private final LinkedList<byte[]> queryResponsesBytes = new LinkedList();
		private final String type;
		private boolean open;
		private boolean alive;
		private String uriString;
		private long pingStart;
		private long pingTimer;
		
		private final WebSocket sock;
		
		private ServerQueryImpl(String type_, String uri) {
			type = type_;
			uriString = uri;
			alive = false;
			pingStart = -1l;
			pingTimer = -1l;
			WebSocket s = null;
			try {
				s = WebSocket.create(uri);
				s.setBinaryType("arraybuffer");
				open = true;
			}catch(Throwable t) {
				open = false;
				if(EaglerAdapterImpl2.blockedAddresses.contains(uriString)) {
					queryResponses.add(new QueryResponse(true, -1l));
				}else if(EaglerAdapterImpl2.rateLimitedAddresses.contains(uriString)) {
					queryResponses.add(new QueryResponse(false, -1l));
				}
				sock = null;
				return;
			}
			sock = s;
			if(open) {
				sock.onOpen(new EventListener<MessageEvent>() {
					@Override
					public void handleEvent(MessageEvent evt) {
						pingStart = System.currentTimeMillis();
						sock.send("Accept: " + type);
					}
				});
				sock.onClose(new EventListener<CloseEvent>() {
					@Override
					public void handleEvent(CloseEvent evt) {
						open = false;
						if(!alive) {
							if(EaglerAdapterImpl2.blockedAddresses.contains(uriString)) {
								queryResponses.add(new QueryResponse(true, pingTimer));
							}else if(EaglerAdapterImpl2.rateLimitedAddresses.contains(uriString)) {
								queryResponses.add(new QueryResponse(false, pingTimer));
							}
						}
					}
				});
				sock.onMessage(new EventListener<MessageEvent>() {
					@Override
					public void handleEvent(MessageEvent evt) {
						alive = true;
						if(pingTimer == -1) {
							pingTimer = System.currentTimeMillis() - pingStart;
						}
						if(isString(evt.getData())) {
							try {
								String str = evt.getDataAsString();
								if(str.equalsIgnoreCase("BLOCKED")) {
									EaglerAdapterImpl2.rateLimitedAddresses.add(uriString);
									queryResponses.add(new QueryResponse(false, pingTimer));
									sock.close();
									return;
								}else if(str.equalsIgnoreCase("LOCKED")) {
									EaglerAdapterImpl2.blockedAddresses.add(uriString);
									queryResponses.add(new QueryResponse(true, pingTimer));
									sock.close();
									return;
								}else {
									QueryResponse q = new QueryResponse(new JSONObject(str), pingTimer);
									if(q.rateLimitStatus != null) {
										if(q.rateLimitStatus == RateLimit.BLOCKED) {
											EaglerAdapterImpl2.rateLimitedAddresses.add(uriString);
										}else if(q.rateLimitStatus == RateLimit.LOCKED) {
											EaglerAdapterImpl2.blockedAddresses.add(uriString);
										}
										sock.close();
									}
									queryResponses.add(q);
								}
							}catch(Throwable t) {
								System.err.println("Query response could not be parsed: " + t.toString());
							}
						}else {
							Uint8Array a = Uint8Array.create(evt.getDataAsArray());
							byte[] b = new byte[a.getByteLength()];
							for(int i = 0; i < b.length; ++i) {
								b[i] = (byte) (a.get(i) & 0xFF);
							}
							queryResponsesBytes.add(b);
						}
					}
				});
				Window.setTimeout(new TimerHandler() {
					@Override
					public void onTimer() {
						if(open && sock.getReadyState() != 1) {
							if(sock.getReadyState() == 0) {
								sock.close();
							}
							open = false;
						}
					}
				}, 5000l);
			}
		}

		@Override
		public boolean isQueryOpen() {
			return open;
		}

		@Override
		public void close() {
			open = false;
			sock.close();
		}

		@Override
		public void send(String str) {
			sock.send(str);
		}

		@Override
		public int responseAvailable() {
			return queryResponses.size();
		}

		@Override
		public int responseBinaryAvailable() {
			return queryResponsesBytes.size();
		}

		@Override
		public QueryResponse getResponse() {
			return queryResponses.size() > 0 ? queryResponses.remove(0) : null;
		}

		@Override
		public byte[] getBinaryResponse() {
			return queryResponsesBytes.size() > 0 ? queryResponsesBytes.remove(0) : null;
		}
		
	}

	public static final ServerQuery openQuery(String type, String uri) {
		return new ServerQueryImpl(type, uri);
	}
	
	private static String serverToJoinOnLaunch = null;
	
	public static final void setServerToJoinOnLaunch(String s) {
		serverToJoinOnLaunch = s;
	}
	
	public static final String getServerToJoinOnLaunch() {
		return serverToJoinOnLaunch;
	}

	private static boolean endianWasChecked = false;
	private static boolean isBigEndian = false;
	private static boolean isLittleEndian = false;
	
	public static final boolean isBigEndian() {
		if(!endianWasChecked) {
			int checkIntegerA = 0xFF000000;
			int checkIntegerB = 0x000000FF;
			
			ArrayBuffer buf = ArrayBuffer.create(4);
			Int32Array bufW = Int32Array.create(buf);
			Uint8Array bufR = Uint8Array.create(buf);
			
			bufW.set(0, checkIntegerA);

			boolean knownBig1 = false;
			if(bufR.get(0) == (short)0xFF && bufR.get(1) == (short)0 && bufR.get(2) == (short)0 && bufR.get(3) == (short)0) {
				knownBig1 = true;
			}
			
			boolean knownLittle1 = false;
			if(bufR.get(0) == (short)0 && bufR.get(1) == (short)0 && bufR.get(2) == (short)0 && bufR.get(3) == (short)0xFF) {
				knownLittle1 = true;
			}
			
			bufW.set(0, checkIntegerB);
			
			boolean knownBig2 = false;
			if(bufR.get(0) == (short)0 && bufR.get(1) == (short)0 && bufR.get(2) == (short)0 && bufR.get(3) == (short)0xFF) {
				knownBig2 = true;
			}

			boolean knownLittle2 = false;
			if(bufR.get(0) == (short)0xFF && bufR.get(1) == (short)0 && bufR.get(2) == (short)0 && bufR.get(3) == (short)0) {
				knownLittle2 = true;
			}
			
			if(knownBig1 == knownBig2 && knownLittle1 == knownLittle2 && knownBig1 != knownLittle1) {
				isBigEndian = knownBig1;
				isLittleEndian = knownLittle1;
			}
			
			if(isBigEndian) {
				System.out.println("This browser is BIG endian!");
			}else if(isLittleEndian) {
				System.out.println("This browser is LITTLE endian!");
			}else {
				System.out.println("The byte order of this browser is inconsistent!");
				System.out.println(" - the sequence FF000000 was " + (knownBig1 ? "" : "not ") + "big endian.");
				System.out.println(" - the sequence FF000000 was " + (knownLittle1 ? "" : "not ") + "little endian.");
				System.out.println(" - the sequence 000000FF was " + (knownBig2 ? "" : "not ") + "big endian.");
				System.out.println(" - the sequence 000000FF was " + (knownLittle2 ? "" : "not ") + "little endian.");
			}
			
			endianWasChecked = true;
		}
		return !isLittleEndian;
	}
	
	private static final ArrayBuffer convertToArrayBuffer(byte[] arr) {
		Uint8Array buf = Uint8Array.create(arr.length);
		buf.set(arr);
		return buf.getBuffer();
	}
	
	private static final Map<String,Long> relayQueryLimited = new HashMap();
	private static final Map<String,Long> relayQueryBlocked = new HashMap();
	
	private static class RelayQueryImpl implements RelayQuery {

		private final WebSocket sock;
		private final String uri;

		private boolean open;
		private boolean failed;
		
		private boolean hasRecievedAnyData = false;
		
		private int vers = -1;
		private String comment = "<no comment>";
		private String brand = "<no brand>";
		
		private long connectionOpenedAt;
		private long connectionPingStart = -1;
		private long connectionPingTimer = -1;
		
		private RateLimit rateLimitStatus = RateLimit.NONE;
		
		private VersionMismatch versError = VersionMismatch.UNKNOWN;
		
		private RelayQueryImpl(String uri) {
			this.uri = uri;
			WebSocket s = null;
			try {
				connectionOpenedAt = System.currentTimeMillis();
				s = WebSocket.create(uri);
				s.setBinaryType("arraybuffer");
				open = true;
				failed = false;
			}catch(Throwable t) {
				connectionOpenedAt = 0l;
				sock = null;
				open = false;
				failed = true;
				return;
			}
			sock = s;
			sock.onOpen(new EventListener<MessageEvent>() {
				@Override
				public void handleEvent(MessageEvent evt) {
					try {
						connectionPingStart = System.currentTimeMillis();
						nativeBinarySend(sock, convertToArrayBuffer(
								IPacket.writePacket(new IPacket00Handshake(0x03, IntegratedServer.preferredRelayVersion, ""))
						));
					} catch (IOException e) {
						System.err.println(e.toString());
						sock.close();
						failed = true;
					}
				}
			});
			sock.onMessage(new EventListener<MessageEvent>() {
				@Override
				public void handleEvent(MessageEvent evt) {
					if(evt.getData() != null && !isString(evt.getData())) {
						hasRecievedAnyData = true;
						Uint8Array buf = Uint8Array.create(evt.getDataAsArray());
						byte[] arr = new byte[buf.getLength()];
						for(int i = 0; i < arr.length; ++i) {
							arr[i] = (byte)buf.get(i);
						}
						if(arr.length == 2 && arr[0] == (byte)0xFC) {
							long millis = System.currentTimeMillis();
							if(arr[1] == (byte)0x00 || arr[1] == (byte)0x01) {
								rateLimitStatus = RateLimit.BLOCKED;
								relayQueryLimited.put(RelayQueryImpl.this.uri, millis);
							}else if(arr[1] == (byte)0x02) {
								rateLimitStatus = RateLimit.NOW_LOCKED;
								relayQueryLimited.put(RelayQueryImpl.this.uri, millis);
								relayQueryBlocked.put(RelayQueryImpl.this.uri, millis);
							}else {
								rateLimitStatus = RateLimit.LOCKED;
								relayQueryBlocked.put(RelayQueryImpl.this.uri, millis);
							}
							failed = true;
							open = false;
							sock.close();
						}else {
							if(open) {
								try {
									IPacket pkt = IPacket.readPacket(new DataInputStream(new ByteArrayInputStream(arr)));
									if(pkt instanceof IPacket69Pong) {
										IPacket69Pong ipkt = (IPacket69Pong)pkt;
										versError = RelayQuery.VersionMismatch.COMPATIBLE;
										if(connectionPingTimer == -1) {
											connectionPingTimer = System.currentTimeMillis() - connectionPingStart;
										}
										vers = ipkt.protcolVersion;
										comment = ipkt.comment;
										brand = ipkt.brand;
										open = false;
										failed = false;
										sock.close();
									}else if(pkt instanceof IPacketFFErrorCode) {
										IPacketFFErrorCode ipkt = (IPacketFFErrorCode)pkt;
										if(ipkt.code == IPacketFFErrorCode.TYPE_PROTOCOL_VERSION) {
											String s = ipkt.desc.toLowerCase();
											if(s.contains("outdated client") || s.contains("client outdated")) {
												versError = RelayQuery.VersionMismatch.CLIENT_OUTDATED;
											}else if(s.contains("outdated server") || s.contains("server outdated") ||
													s.contains("outdated relay") || s.contains("server relay")) {
												versError = RelayQuery.VersionMismatch.RELAY_OUTDATED;
											}else {
												versError = RelayQuery.VersionMismatch.UNKNOWN;
											}
										}
										System.err.println(uri + ": Recieved query error code " + ipkt.code + ": " + ipkt.desc);
										open = false;
										failed = true;
										sock.close();
									}else {
										throw new IOException("Unexpected packet '" + pkt.getClass().getSimpleName() + "'");
									}
								} catch (IOException e) {
									System.err.println("Relay Query Error: " + e.toString());
									e.printStackTrace();
									open = false;
									failed = true;
									sock.close();
								}
							}
						}
					}
				}
			});
			sock.onClose(new EventListener<CloseEvent>() {
				@Override
				public void handleEvent(CloseEvent evt) {
					open = false;
					if(!hasRecievedAnyData) {
						failed = true;
						Long l = relayQueryBlocked.get(uri);
						if(l != null) {
							if(System.currentTimeMillis() - l.longValue() < 400000l) {
								rateLimitStatus = RateLimit.LOCKED;
								return;
							}
						}
						l = relayQueryLimited.get(uri);
						if(l != null) {
							if(System.currentTimeMillis() - l.longValue() < 900000l) {
								rateLimitStatus = RateLimit.BLOCKED;
								return;
							}
						}
					}
				}
			});
		}

		@Override
		public boolean isQueryOpen() {
			return open;
		}

		@Override
		public boolean isQueryFailed() {
			return failed;
		}

		@Override
		public RateLimit isQueryRateLimit() {
			return rateLimitStatus;
		}

		@Override
		public void close() {
			if(sock != null && open) {
				sock.close();
			}
			open = false;
		}

		@Override
		public int getVersion() {
			return vers;
		}

		@Override
		public String getComment() {
			return comment;
		}

		@Override
		public String getBrand() {
			return brand;
		}

		@Override
		public long getPing() {
			return connectionPingTimer < 1 ? 1 : connectionPingTimer;
		}

		@Override
		public VersionMismatch getCompatible() {
			return versError;
		}
		
	}
	
	private static class RelayQueryRatelimitDummy implements RelayQuery {
		
		private final RateLimit type;
		
		private RelayQueryRatelimitDummy(RateLimit type) {
			this.type = type;
		}

		@Override
		public boolean isQueryOpen() {
			return false;
		}

		@Override
		public boolean isQueryFailed() {
			return true;
		}

		@Override
		public RateLimit isQueryRateLimit() {
			return type;
		}

		@Override
		public void close() {
		}

		@Override
		public int getVersion() {
			return IntegratedServer.preferredRelayVersion;
		}

		@Override
		public String getComment() {
			return "this query was rate limited";
		}

		@Override
		public String getBrand() {
			return "lax1dude";
		}

		@Override
		public long getPing() {
			return 0l;
		}

		@Override
		public VersionMismatch getCompatible() {
			return VersionMismatch.COMPATIBLE;
		}
		
	}
	
	public static final RelayQuery openRelayQuery(String addr) {
		long millis = System.currentTimeMillis();
		
		Long l = relayQueryBlocked.get(addr);
		if(l != null && millis - l.longValue() < 60000l) {
			return new RelayQueryRatelimitDummy(RateLimit.LOCKED);
		}
		
		l = relayQueryLimited.get(addr);
		if(l != null && millis - l.longValue() < 10000l) {
			return new RelayQueryRatelimitDummy(RateLimit.BLOCKED);
		}
		
		return new RelayQueryImpl(addr);
	}
	
	private static class RelayWorldsQueryImpl implements RelayWorldsQuery {
		
		private final WebSocket sock;
		private final String uri;

		private boolean open;
		private boolean failed;
		
		private boolean hasRecievedAnyData = false;
		private RateLimit rateLimitStatus = RateLimit.NONE;
		
		private RelayQuery.VersionMismatch versError = RelayQuery.VersionMismatch.UNKNOWN;
		
		private List<LocalWorld> worlds = null;
		
		private RelayWorldsQueryImpl(String uri) {
			this.uri = uri;
			WebSocket s = null;
			try {
				s = WebSocket.create(uri);
				s.setBinaryType("arraybuffer");
				open = true;
				failed = false;
			}catch(Throwable t) {
				sock = null;
				open = false;
				failed = true;
				return;
			}
			sock = s;
			sock.onOpen(new EventListener<MessageEvent>() {
				@Override
				public void handleEvent(MessageEvent evt) {
					try {
						nativeBinarySend(sock, convertToArrayBuffer(
								IPacket.writePacket(new IPacket00Handshake(0x04, IntegratedServer.preferredRelayVersion, ""))
						));
					} catch (IOException e) {
						System.err.println(e.toString());
						sock.close();
						open = false;
						failed = true;
					}
				}
			});
			sock.onMessage(new EventListener<MessageEvent>() {
				@Override
				public void handleEvent(MessageEvent evt) {
					if(evt.getData() != null && !isString(evt.getData())) {
						hasRecievedAnyData = true;
						Uint8Array buf = Uint8Array.create(evt.getDataAsArray());
						byte[] arr = new byte[buf.getLength()];
						for(int i = 0; i < arr.length; ++i) {
							arr[i] = (byte)buf.get(i);
						}
						if(arr.length == 2 && arr[0] == (byte)0xFC) {
							long millis = System.currentTimeMillis();
							if(arr[1] == (byte)0x00 || arr[1] == (byte)0x01) {
								rateLimitStatus = RateLimit.BLOCKED;
								relayQueryLimited.put(RelayWorldsQueryImpl.this.uri, millis);
							}else if(arr[1] == (byte)0x02) {
								rateLimitStatus = RateLimit.NOW_LOCKED;
								relayQueryLimited.put(RelayWorldsQueryImpl.this.uri, millis);
								relayQueryBlocked.put(RelayWorldsQueryImpl.this.uri, millis);
							}else {
								rateLimitStatus = RateLimit.LOCKED;
								relayQueryBlocked.put(RelayWorldsQueryImpl.this.uri, millis);
							}
							open = false;
							failed = true;
							sock.close();
						}else {
							if(open) {
								try {
									IPacket pkt = IPacket.readPacket(new DataInputStream(new ByteArrayInputStream(arr)));
									if(pkt instanceof IPacket07LocalWorlds) {
										worlds = ((IPacket07LocalWorlds)pkt).worldsList;
										sock.close();
										open = false;
										failed = false;
									}else if(pkt instanceof IPacketFFErrorCode) {
										IPacketFFErrorCode ipkt = (IPacketFFErrorCode)pkt;
										if(ipkt.code == IPacketFFErrorCode.TYPE_PROTOCOL_VERSION) {
											String s = ipkt.desc.toLowerCase();
											if(s.contains("outdated client") || s.contains("client outdated")) {
												versError = RelayQuery.VersionMismatch.CLIENT_OUTDATED;
											}else if(s.contains("outdated server") || s.contains("server outdated") ||
													s.contains("outdated relay") || s.contains("server relay")) {
												versError = RelayQuery.VersionMismatch.RELAY_OUTDATED;
											}else {
												versError = RelayQuery.VersionMismatch.UNKNOWN;
											}
										}
										System.err.println(uri + ": Recieved query error code " + ipkt.code + ": " + ipkt.desc);
										open = false;
										failed = true;
										sock.close();
									}else {
										throw new IOException("Unexpected packet '" + pkt.getClass().getSimpleName() + "'");
									}
								} catch (IOException e) {
									System.err.println("Relay World Query Error: " + e.toString());
									e.printStackTrace();
									open = false;
									failed = true;
									sock.close();
								}
							}
						}
					}
				}
			});
			sock.onClose(new EventListener<CloseEvent>() {
				@Override
				public void handleEvent(CloseEvent evt) {
					open = false;
					if(!hasRecievedAnyData) {
						failed = true;
						Long l = relayQueryBlocked.get(uri);
						if(l != null) {
							if(System.currentTimeMillis() - l.longValue() < 400000l) {
								rateLimitStatus = RateLimit.LOCKED;
								return;
							}
						}
						l = relayQueryLimited.get(uri);
						if(l != null) {
							if(System.currentTimeMillis() - l.longValue() < 900000l) {
								rateLimitStatus = RateLimit.BLOCKED;
								return;
							}
						}
					}
				}
			});
		}

		@Override
		public boolean isQueryOpen() {
			return open;
		}

		@Override
		public boolean isQueryFailed() {
			return failed;
		}

		@Override
		public RateLimit isQueryRateLimit() {
			return rateLimitStatus;
		}

		@Override
		public void close() {
			if(open && sock != null) {
				sock.close();
			}
			open = false;
		}

		@Override
		public List<LocalWorld> getWorlds() {
			return worlds;
		}

		@Override
		public RelayQuery.VersionMismatch getCompatible() {
			return versError;
		}
		
	}
	
	private static class RelayWorldsQueryRatelimitDummy implements RelayWorldsQuery {
		
		private final RateLimit rateLimit;
		
		private RelayWorldsQueryRatelimitDummy(RateLimit rateLimit) {
			this.rateLimit = rateLimit;
		}

		@Override
		public boolean isQueryOpen() {
			return false;
		}

		@Override
		public boolean isQueryFailed() {
			return true;
		}

		@Override
		public RateLimit isQueryRateLimit() {
			return rateLimit;
		}

		@Override
		public void close() {
		}

		@Override
		public List<LocalWorld> getWorlds() {
			return new ArrayList(0);
		}

		@Override
		public RelayQuery.VersionMismatch getCompatible() {
			return RelayQuery.VersionMismatch.COMPATIBLE;
		}
	}
	
	public static final RelayWorldsQuery openRelayWorldsQuery(String addr) {
		long millis = System.currentTimeMillis();
		
		Long l = relayQueryBlocked.get(addr);
		if(l != null && millis - l.longValue() < 60000l) {
			return new RelayWorldsQueryRatelimitDummy(RateLimit.LOCKED);
		}
		
		l = relayQueryLimited.get(addr);
		if(l != null && millis - l.longValue() < 10000l) {
			return new RelayWorldsQueryRatelimitDummy(RateLimit.BLOCKED);
		}
		
		return new RelayWorldsQueryImpl(addr);
	}
	
	private static class RelayServerSocketImpl implements RelayServerSocket {
		
		private final WebSocket sock;
		private final String uri;

		private boolean open;
		private boolean closed;
		private boolean failed;
		
		private boolean hasRecievedAnyData;

		private final List<Throwable> exceptions = new LinkedList();
		private final List<IPacket> packets = new LinkedList();
		
		private RelayServerSocketImpl(String uri, int timeout) {
			this.uri = uri;
			WebSocket s = null;
			try {
				s = WebSocket.create(uri);
				s.setBinaryType("arraybuffer");
				open = false;
				closed = false;
				failed = false;
			}catch(Throwable t) {
				exceptions.add(t);
				sock = null;
				open = false;
				closed = true;
				failed = true;
				return;
			}
			sock = s;
			sock.onOpen(new EventListener<MessageEvent>() {
				@Override
				public void handleEvent(MessageEvent evt) {
					open = true;
				}
			});
			sock.onMessage(new EventListener<MessageEvent>() {
				@Override
				public void handleEvent(MessageEvent evt) {
					if(evt.getData() != null && !isString(evt.getData())) {
						hasRecievedAnyData = true;
						Uint8Array buf = Uint8Array.create(evt.getDataAsArray());
						byte[] arr = new byte[buf.getLength()];
						for(int i = 0; i < arr.length; ++i) {
							arr[i] = (byte)buf.get(i);
						}
						try {
							packets.add(IPacket.readPacket(new DataInputStream(new ByteArrayInputStream(arr))));
						} catch (IOException e) {
							exceptions.add(e);
							System.err.println("Relay Socket Error: " + e.toString());
							e.printStackTrace();
							open = false;
							failed = true;
							closed = true;
							sock.close();
						}
					}
				}
			});
			sock.onClose(new EventListener<CloseEvent>() {
				@Override
				public void handleEvent(CloseEvent evt) {
					if(!hasRecievedAnyData) {
						failed = true;
					}
					open = false;
					closed = true;
				}
			});
			Window.setTimeout(new TimerHandler() {

				@Override
				public void onTimer() {
					if(!open && !closed) {
						closed = true;
						sock.close();
					}
				}
				
			}, timeout);
		}

		@Override
		public boolean isOpen() {
			return open;
		}

		@Override
		public boolean isClosed() {
			return closed;
		}

		@Override
		public void close() {
			if(open && sock != null) {
				sock.close();
			}
			open = false;
			closed = true;
		}

		@Override
		public boolean isFailed() {
			return failed;
		}

		@Override
		public Throwable getException() {
			if(exceptions.size() > 0) {
				return exceptions.remove(0);
			}else {
				return null;
			}
		}

		@Override
		public void writePacket(IPacket pkt) {
			try {
				nativeBinarySend(sock, convertToArrayBuffer(IPacket.writePacket(pkt)));
			} catch (Throwable e) {
				System.err.println("Relay connection error: " + e.toString());
				e.printStackTrace();
				exceptions.add(e);
				failed = true;
				open = false;
				closed = true;
				sock.close();
			}
		}

		@Override
		public IPacket readPacket() {
			if(packets.size() > 0) {
				return packets.remove(0);
			}else {
				return null;
			}
		}

		@Override
		public IPacket nextPacket() {
			if(packets.size() > 0) {
				return packets.get(0);
			}else {
				return null;
			}
		}
		
		@Override
		public RateLimit getRatelimitHistory() {
			if(relayQueryBlocked.containsKey(uri)) {
				return RateLimit.LOCKED;
			}
			if(relayQueryLimited.containsKey(uri)) {
				return RateLimit.BLOCKED;
			}
			return RateLimit.NONE;
		}
		
		@Override
		public String getURI() {
			return uri;
		}
		
	}
	
	private static class RelayServerSocketRatelimitDummy implements RelayServerSocket {
		
		private final RateLimit limit;
		
		private RelayServerSocketRatelimitDummy(RateLimit limit) {
			this.limit = limit;
		}
		
		@Override
		public boolean isOpen() {
			return false;
		}

		@Override
		public boolean isClosed() {
			return true;
		}

		@Override
		public void close() {
		}

		@Override
		public boolean isFailed() {
			return true;
		}

		@Override
		public Throwable getException() {
			return null;
		}

		@Override
		public void writePacket(IPacket pkt) {
		}

		@Override
		public IPacket readPacket() {
			return null;
		}

		@Override
		public IPacket nextPacket() {
			return null;
		}
		
		@Override
		public RateLimit getRatelimitHistory() {
			return limit;
		}
		
		@Override
		public String getURI() {
			return "<disconnected>";
		}
		
	}
	
	public static final RelayServerSocket openRelayConnection(String addr, int timeout) {
		long millis = System.currentTimeMillis();
		
		Long l = relayQueryBlocked.get(addr);
		if(l != null && millis - l.longValue() < 60000l) {
			return new RelayServerSocketRatelimitDummy(RateLimit.LOCKED);
		}
		
		l = relayQueryLimited.get(addr);
		if(l != null && millis - l.longValue() < 10000l) {
			return new RelayServerSocketRatelimitDummy(RateLimit.BLOCKED);
		}
		
		return new RelayServerSocketImpl(addr, timeout);
	}

	private static EaglercraftLANClient rtcLANClient = null;
	
	@JSBody(params = { }, script = "return window.startLANClient();")
	private static native EaglercraftLANClient startRTCLANClient();

	private static boolean clientLANinit = false;
	private static final List<byte[]> clientLANPacketBuffer = new ArrayList<>();
	
	private static String clientICECandidate = null;
	private static String clientDescription = null;
	private static boolean clientDataChannelOpen = false;
	private static boolean clientDataChannelClosed = true;
	
	public static final boolean clientLANSupported() {
		return rtcLANClient.LANClientSupported();
	}
	
	public static final int clientLANReadyState() {
		return rtcLANClient.getReadyState();
	}
	
	public static final void clientLANCloseConnection() {
		rtcLANClient.signalRemoteDisconnect(false);
	}
	
	public static final void clientLANSendPacket(byte[] pkt) {
		rtcLANClient.sendPacketToServer(convertToArrayBuffer(pkt));
	}
	
	public static final byte[] clientLANReadPacket() {
		return clientLANPacketBuffer.size() > 0 ? clientLANPacketBuffer.remove(0) : null;
	}
	
	public static final void clientLANSetICEServersAndConnect(String[] servers) {
		if(!clientLANinit) {
			clientLANinit = true;
			rtcLANClient.setDescriptionHandler(new EaglercraftLANClient.DescriptionHandler() {
				@Override
				public void call(String description) {
					clientDescription = description;
				}
			});
			rtcLANClient.setICECandidateHandler(new EaglercraftLANClient.ICECandidateHandler() {
				@Override
				public void call(String candidate) {
					clientICECandidate = candidate;
				}
			});
			rtcLANClient.setRemoteDataChannelHandler(new EaglercraftLANClient.ClientSignalHandler() {
				@Override
				public void call() {
					clientDataChannelClosed = false;
					clientDataChannelOpen = true;
				}
			});
			rtcLANClient.setRemotePacketHandler(new EaglercraftLANClient.RemotePacketHandler() {
				@Override
				public void call(ArrayBuffer buffer) {
					Uint8Array array = Uint8Array.create(buffer);
					byte[] ret = new byte[array.getByteLength()];
					for(int i = 0; i < ret.length; ++i) {
						ret[i] = (byte) array.get(i);
					}
					clientLANPacketBuffer.add(ret);
				}
			});
			rtcLANClient.setRemoteDisconnectHandler(new EaglercraftLANClient.ClientSignalHandler() {
				@Override
				public void call() {
					clientDataChannelClosed = true;
				}
			});
		}
		rtcLANClient.setICEServers(servers);
		if(clientLANReadyState() == rtcLANClient.READYSTATE_CONNECTED || clientLANReadyState() == rtcLANClient.READYSTATE_CONNECTING) {
			rtcLANClient.signalRemoteDisconnect(true);
		}
		rtcLANClient.initializeClient();
		rtcLANClient.signalRemoteConnect();
	}

	public static final void clearLANClientState() {
		clientICECandidate = null;
		clientDescription = null;
		clientDataChannelOpen = false;
		clientDataChannelClosed = true;
	}
	
	public static final String clientLANAwaitICECandidate() {
		if(clientICECandidate != null) {
			String ret = clientICECandidate;
			clientICECandidate = null;
			return ret;
		}else {
			return null;
		}
	}
	
	public static final String clientLANAwaitDescription() {
		if(clientDescription != null) {
			String ret = clientDescription;
			clientDescription = null;
			return ret;
		}else {
			return null;
		}
	}
	
	public static final boolean clientLANAwaitChannel() {
		if(clientDataChannelOpen) {
			clientDataChannelOpen = false;
			return true;
		}else {
			return false;
		}
	}
	
	public static final boolean clientLANClosed() {
		return clientDataChannelClosed;
	}
	
	public static final void clientLANSetICECandidate(String candidate) {
		rtcLANClient.signalRemoteICECandidate(candidate);
	}
	
	public static final void clientLANSetDescription(String description) {
		rtcLANClient.signalRemoteDescription(description);
	}
	
	private static EaglercraftLANServer rtcLANServer = null;

	@JSBody(params = { }, script = "return window.startLANServer();")
	private static native EaglercraftLANServer startRTCLANServer();

	private static boolean serverLANinit = false;
	private static final List<LANPeerEvent> serverLANEventBuffer = new LinkedList<>();
	
	public static final boolean serverLANSupported() {
		return rtcLANServer.LANServerSupported();
	}
	
	public static final void serverLANInitializeServer(String[] servers) {
		serverLANEventBuffer.clear();
		rtcLANServer.setICEServers(servers);
		rtcLANServer.initializeServer();
		if(!serverLANinit) {
			serverLANinit = true;
			rtcLANServer.setDescriptionHandler(new EaglercraftLANServer.DescriptionHandler() {
				@Override
				public void call(String peerId, String description) {
					serverLANEventBuffer.add(new LANPeerEvent.LANPeerDescriptionEvent(peerId, description));
				}
			});
			rtcLANServer.setICECandidateHandler(new EaglercraftLANServer.ICECandidateHandler() {
				@Override
				public void call(String peerId, String candidate) {
					serverLANEventBuffer.add(new LANPeerEvent.LANPeerICECandidateEvent(peerId, candidate));
				}
			});
			rtcLANServer.setRemoteClientDataChannelHandler(new EaglercraftLANServer.ClientSignalHandler() {
				@Override
				public void call(String peerId) {
					serverLANEventBuffer.add(new LANPeerEvent.LANPeerDataChannelEvent(peerId));
				}
			});
			rtcLANServer.setRemoteClientPacketHandler(new EaglercraftLANServer.PeerPacketHandler() {
				@Override
				public void call(String peerId, ArrayBuffer buffer) {
					Uint8Array array = Uint8Array.create(buffer);
					byte[] ret = new byte[array.getByteLength()];
					for(int i = 0; i < ret.length; ++i) {
						ret[i] = (byte) array.get(i);
					}
					serverLANEventBuffer.add(new LANPeerEvent.LANPeerPacketEvent(peerId, ret));
				}
			});
			rtcLANServer.setRemoteClientDisconnectHandler(new EaglercraftLANServer.ClientSignalHandler() {
				@Override
				public void call(String peerId) {
					serverLANEventBuffer.add(new LANPeerEvent.LANPeerDisconnectEvent(peerId));
				}
			});
		}
	}

	public static final void serverLANCloseServer() {
		rtcLANServer.signalRemoteDisconnect("");
	}
	
	public static final LANPeerEvent serverLANGetEvent(String clientId) {
		if(serverLANEventBuffer.size() > 0) {
			Iterator<LANPeerEvent> i = serverLANEventBuffer.iterator();
			while(i.hasNext()) {
				LANPeerEvent evt = i.next();
				if(evt.getPeerId().equals(clientId)) {
					i.remove();
					return evt;
				}
			}
			return null;
		}else {
			return null;
		}
	}

	private static final int fragmentSize = 65536;

	public static final void serverLANWritePacket(String peer, byte[] data) {
		if (data.length > fragmentSize) {
			for (int i = 0; i < data.length; i += fragmentSize) {
				byte[] fragData = new byte[((i + fragmentSize > data.length) ? (data.length % fragmentSize) : fragmentSize) + 1];
				System.arraycopy(data, i, fragData, 1, fragData.length - 1);
				fragData[0] = (i + fragmentSize < data.length) ? (byte) 1 : (byte) 0;
				ArrayBuffer arr = ArrayBuffer.create(fragData.length);
				Uint8Array.create(arr).set(fragData);
				rtcLANServer.sendPacketToRemoteClient(peer, arr);
			}
		} else {
			byte[] sendData = new byte[data.length + 1];
			sendData[0] = 0;
			System.arraycopy(data, 0, sendData, 1, data.length);
			ArrayBuffer arr = ArrayBuffer.create(sendData.length);
			Uint8Array.create(arr).set(sendData);
			rtcLANServer.sendPacketToRemoteClient(peer, arr);
		}
	}
	
	public static final void serverLANCreatePeer(String peer) {
		rtcLANServer.signalRemoteConnect(peer);
	}
	
	public static final void serverLANPeerICECandidates(String peer, String iceCandidates) {
		rtcLANServer.signalRemoteICECandidate(peer, iceCandidates);
	}
	
	public static final void serverLANPeerDescription(String peer, String description) {
		rtcLANServer.signalRemoteDescription(peer, description);
	}
	
	public static final void serverLANDisconnectPeer(String peer) {
		rtcLANServer.signalRemoteDisconnect(peer);
	}
	
	public static final int countPeers() {
		return rtcLANServer.countPeers();
	}
	
}
