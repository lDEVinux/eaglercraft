package net.lax1dude.eaglercraft.adapter.teavm;

import java.util.ArrayList;
import java.util.List;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.TimerHandler;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSArrayReader;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLIFrameElement;
import org.teavm.jso.dom.xml.Element;
import org.teavm.jso.dom.xml.NodeList;

public class SelfDefence {
	
	private static HTMLCanvasElement canvas = null;
	private static boolean ignoreNextWindow = false;
	
	@JSFunctor
	private static interface NewWindowCallback extends JSObject {
		void call(Window newWindow);
	}
	
	@JSBody(params = { "cb" }, script = "const ccb = cb; const _open = window.open; window.open = (url,name,params) => { var rw = _open(url,name,params); ccb(rw); return rw; }")
	private static native void injectWindowCapture(NewWindowCallback callback);
	
	private static final List<Window> capturedChildWindows = new ArrayList();
	
	public static void init(HTMLCanvasElement legitCanvas) {
		canvas = legitCanvas;
		for(int i = 0; i < 15; ++i) {
			Window.setTimeout(new TimerHandler() {

				@Override
				public void onTimer() {
					Window.setTimeout(this, (long)(Math.random() * 25000l));
					run(Window.current());
					for(int i = 0, l = capturedChildWindows.size(); i < l; ++i) {
						run(capturedChildWindows.get(i));
					}
				}
				
			}, (long)(Math.random() * 25000l));
		}
		injectWindowCapture(new NewWindowCallback() {
			@Override
			public void call(Window newWindow) {
				if(!ignoreNextWindow) {
					capturedChildWindows.add(newWindow);
				}
				ignoreNextWindow = false;
			}
		});
	}

	public static void openWindowIgnore(String url, String name) {
		ignoreNextWindow = true;
		Window.current().open(url, name);
	}
	
	private static void run(Window win) {
		try {
			run0(win);
		}catch(Throwable t) {
		}
	}
	
	private static void run0(Window win) {
		run(win.getDocument());
		JSArrayReader<HTMLIFrameElement> frms = win.getFrames();
		for(int i = 0, l = frms.getLength(); i < l; ++i) {
			HTMLIFrameElement frm = frms.get(i);
			if(checkFrame(frm)) {
				run(frm.getContentWindow());
			}
		}
	}
	
	@JSBody(params = { "frm" }, script = "try { var g = frm.contentWindow; g[\"fuck_off\"] = \"dick\"; return g[\"fuck_off\"] === \"dick\"; } catch (e) { return false; }")
	private static native boolean checkFrame(HTMLIFrameElement frame);
	
	private static void run(HTMLDocument doc) {
		try {
			run0(doc);
		}catch(Throwable t) {
		}
	}
	
	private static void run0(HTMLDocument doc) {
		NodeList<Element> els = doc.getElementsByTagName("canvas");
		for(int i = 0, l = els.getLength(); i < l; ++i) {
			HTMLCanvasElement canv = (HTMLCanvasElement) els.get(i);
			if(canvas != canv) {
				canv.delete();
			}
		}
	}
	
}
