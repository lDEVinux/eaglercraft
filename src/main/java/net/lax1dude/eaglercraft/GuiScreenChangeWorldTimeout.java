package net.lax1dude.eaglercraft;

public class GuiScreenChangeWorldTimeout {
	
	/*
	 * 'license' screen text, this is encoded to stop skids from
	 *  easily modifying it via find/replace in a text editor
	 */
	
	static final byte[] line00 = new byte[] {
			(byte)85,(byte)115,(byte)101,(byte)32,(byte)121,(byte)111,(byte)117,(byte)114,(byte)32,(byte)98,
			(byte)114,(byte)97,(byte)105,(byte)110,(byte)32,(byte)97,(byte)110,(byte)100,(byte)32,(byte)114,
			(byte)101,(byte)97,(byte)100,(byte)58
	};
	
	static final byte[] line10 = new byte[] {
		(byte)69,(byte)97,(byte)103,(byte)108,(byte)101,(byte)114,(byte)99,(byte)114,(byte)97,(byte)102,
		(byte)116,(byte)32,(byte)105,(byte)115,(byte)32,(byte)194,(byte)167,(byte)100,(byte)102,(byte)114,
		(byte)101,(byte)101,(byte)32,(byte)115,(byte)111,(byte)102,(byte)116,(byte)119,(byte)97,(byte)114,
		(byte)101,(byte)44,(byte)194,(byte)167,(byte)114,(byte)32,(byte)105,(byte)102,(byte)32,(byte)115,
		(byte)111,(byte)109,(byte)101,(byte)111,(byte)110,(byte)101,(byte)32,(byte)105,(byte)110,(byte)116,
		(byte)101,(byte)110,(byte)116,(byte)105,(byte)111,(byte)110,(byte)97,(byte)108,(byte)108,(byte)121,
		(byte)32,(byte)99,(byte)104,(byte)97,(byte)114,(byte)103,(byte)101,(byte)100
	};
	
	static final byte[] line11 = new byte[] {(byte)121,(byte)111,(byte)117,(byte)32,(byte)97,(byte)110,
		(byte)121,(byte)32,(byte)115,(byte)117,(byte)109,(byte)32,(byte)111,(byte)102,(byte)32,(byte)109,
		(byte)111,(byte)110,(byte)101,(byte)121,(byte)32,(byte)116,(byte)111,(byte)32,(byte)103,(byte)97,
		(byte)105,(byte)110,(byte)32,(byte)97,(byte)99,(byte)99,(byte)101,(byte)115,(byte)115,(byte)32,
		(byte)116,(byte)111,(byte)32,(byte)116,(byte)104,(byte)105,(byte)115,(byte)32,(byte)102,(byte)105,
		(byte)108,(byte)101,(byte)44,(byte)32,(byte)121,(byte)111,(byte)117,(byte)32,(byte)97,(byte)114,
		(byte)101,(byte)32,(byte)97,(byte)110
	};

	static final byte[] line12 = new byte[] {(byte)105,(byte)100,(byte)105,(byte)111,(byte)116,(byte)32,
		(byte)97,(byte)110,(byte)100,(byte)32,(byte)97,(byte)32,(byte)118,(byte)105,(byte)99,(byte)116,
		(byte)105,(byte)109,(byte)32,(byte)111,(byte)102,(byte)32,(byte)112,(byte)105,(byte)114,(byte)97,
		(byte)99,(byte)121,(byte)46,(byte)32,(byte)83,(byte)116,(byte)111,(byte)112,(byte)32,(byte)112,
		(byte)108,(byte)97,(byte)121,(byte)105,(byte)110,(byte)103,(byte)32,(byte)121,(byte)111,(byte)117,
		(byte)114,(byte)115,(byte)101,(byte)108,(byte)102,(byte)46
	};
	
	static final byte[] line20 = new byte[] {
		(byte)67,(byte)108,(byte)105,(byte)99,(byte)107,(byte)32,(byte)39,(byte)70,(byte)111,(byte)114,
		(byte)107,(byte)32,(byte)111,(byte)110,(byte)32,(byte)71,(byte)105,(byte)116,(byte)104,(byte)117,
		(byte)98,(byte)39,(byte)32,(byte)111,(byte)110,(byte)32,(byte)116,(byte)104,(byte)101,(byte)32,
		(byte)109,(byte)97,(byte)105,(byte)110,(byte)32,(byte)109,(byte)101,(byte)110,(byte)117,(byte)32,
		(byte)116,(byte)111,(byte)32,(byte)97,(byte)99,(byte)99,(byte)101,(byte)115,(byte)115,(byte)32,
		(byte)116,(byte)104,(byte)101,(byte)32,(byte)111,(byte)102,(byte)102,(byte)105,(byte)99,(byte)97,(byte)108
	};

	static final byte[] line21 = new byte[] {
		(byte)115,(byte)111,(byte)117,(byte)114,(byte)99,(byte)101,(byte)32,(byte)99,(byte)111,(byte)100,
		(byte)101,(byte)32,(byte)116,(byte)111,(byte)32,(byte)100,(byte)111,(byte)119,(byte)110,(byte)108,
		(byte)111,(byte)97,(byte)100,(byte)32,(byte)116,(byte)104,(byte)105,(byte)115,(byte)32,(byte)101,
		(byte)100,(byte)117,(byte)99,(byte)97,(byte)116,(byte)105,(byte)111,(byte)110,(byte)97,(byte)108,
		(byte)32,(byte)112,(byte)114,(byte)111,(byte)106,(byte)101,(byte)99,(byte)116,(byte)32,(byte)108,
		(byte)101,(byte)103,(byte)105,(byte)116,(byte)105,(byte)109,(byte)97,(byte)116,(byte)101,(byte)108,(byte)121
	};

	static final byte[] line30 = new byte[] {
		(byte)73,(byte)32,(byte)97,(byte)109,(byte)32,(byte)97,(byte)119,(byte)97,(byte)114,(byte)101,
		(byte)32,(byte)116,(byte)104,(byte)97,(byte)116,(byte)32,(byte)116,(byte)104,(byte)105,(byte)115,
		(byte)32,(byte)112,(byte)114,(byte)111,(byte)106,(byte)101,(byte)99,(byte)116,(byte)32,(byte)118,
		(byte)105,(byte)111,(byte)108,(byte)97,(byte)116,(byte)101,(byte)100,(byte)32,(byte)77,(byte)111,
		(byte)106,(byte)97,(byte)110,(byte)103,(byte)39,(byte)115,(byte)32,(byte)84,(byte)101,(byte)114,
		(byte)109,(byte)115,(byte)32,(byte)111,(byte)102,(byte)32,(byte)83,(byte)101,(byte)114,(byte)118,
		(byte)105,(byte)99,(byte)101
	};
	
	static final byte[] line31 = new byte[] {
		(byte)84,(byte)104,(byte)105,(byte)115,(byte)32,(byte)105,(byte)115,(byte)32,(byte)103,(byte)111,
		(byte)105,(byte)110,(byte)103,(byte)32,(byte)116,(byte)111,(byte)32,(byte)99,(byte)104,(byte)97,
		(byte)110,(byte)103,(byte)101,(byte)32,(byte)105,(byte)110,(byte)32,(byte)97,(byte)32,(byte)102,
		(byte)101,(byte)119,(byte)32,(byte)119,(byte)101,(byte)101,(byte)107,(byte)115,(byte)44,(byte)32,
		(byte)119,(byte)104,(byte)101,(byte)110,(byte)32,(byte)73,(byte)32,(byte)99,(byte)111,(byte)110,
		(byte)118,(byte)101,(byte)114,(byte)116,(byte)32,(byte)116,(byte)104,(byte)105,(byte)115,(byte)32,
		(byte)103,(byte)97,(byte)109,(byte)101
	};

	static final byte[] line32 = new byte[] {
		(byte)105,(byte)110,(byte)116,(byte)111,(byte)32,(byte)97,(byte)110,(byte)32,(byte)111,(byte)110,
		(byte)108,(byte)105,(byte)110,(byte)101,(byte)45,(byte)109,(byte)111,(byte)100,(byte)101,(byte)32,
		(byte)112,(byte)108,(byte)117,(byte)103,(byte)105,(byte)110,(byte)32,(byte)116,(byte)104,(byte)97,
		(byte)116,(byte)32,(byte)114,(byte)101,(byte)113,(byte)117,(byte)105,(byte)114,(byte)101,(byte)115,
		(byte)32,(byte)97,(byte)32,(byte)109,(byte)105,(byte)99,(byte)114,(byte)111,(byte)115,(byte)111,
		(byte)102,(byte)116,(byte)32,(byte)97,(byte)99,(byte)99,(byte)111,(byte)117,(byte)110,(byte)116,(byte)46
	};
	
	static final byte[] line40 = new byte[] {
		(byte)85,(byte)110,(byte)116,(byte)105,(byte)108,(byte)32,(byte)116,(byte)104,(byte)101,(byte)110,
		(byte)44,(byte)32,(byte)68,(byte)79,(byte)32,(byte)78,(byte)79,(byte)84,(byte)32,(byte)66,(byte)85,
		(byte)89,(byte)32,(byte)79,(byte)82,(byte)32,(byte)83,(byte)69,(byte)76,(byte)76,(byte)32,(byte)65,
		(byte)67,(byte)67,(byte)69,(byte)83,(byte)83,(byte)32,(byte)84,(byte)79,(byte)32,(byte)84,(byte)72,
		(byte)73,(byte)83,(byte)32,(byte)80,(byte)82,(byte)79,(byte)74,(byte)69,(byte)67,(byte)84
	};

	static final byte[] line50 = new byte[] {
		(byte)73,(byte)32,(byte)117,(byte)110,(byte)100,(byte)101,(byte)114,(byte)115,(byte)116,(byte)97,
		(byte)110,(byte)100,(byte)32,(byte)97,(byte)110,(byte)100,(byte)32,(byte)107,(byte)110,(byte)111,
		(byte)119,(byte)32,(byte)104,(byte)111,(byte)119,(byte)32,(byte)116,(byte)111,(byte)32,(byte)114,
		(byte)101,(byte)97,(byte)100
	};

	static final byte[] line60 = new byte[] {(byte)65,(byte)99,(byte)99,(byte)101,(byte)112,(byte)116};
	
	static final byte[] line61 = new byte[] {(byte)68,(byte)101,(byte)99,(byte)108,(byte)105,(byte)110,(byte)101};
	
	static final byte[] line70 = new byte[] {
		(byte)84,(byte)101,(byte)114,(byte)109,(byte)115,(byte)32,(byte)111,(byte)102,(byte)32,(byte)83,
		(byte)101,(byte)114,(byte)118,(byte)105,(byte)99,(byte)101,(byte)32,(byte)68,(byte)101,(byte)99,
		(byte)108,(byte)105,(byte)110,(byte)101,(byte)100
	};

	static final byte[] line71 = new byte[] {
		(byte)121,(byte)111,(byte)117,(byte)32,(byte)99,(byte)97,(byte)110,(byte)110,(byte)111,(byte)116,
		(byte)32,(byte)117,(byte)115,(byte)101,(byte)32,(byte)116,(byte)104,(byte)105,(byte)115,(byte)32,
		(byte)115,(byte)111,(byte)102,(byte)116,(byte)119,(byte)97,(byte)114,(byte)101,(byte)32,(byte)105,
		(byte)102,(byte)32,(byte)121,(byte)111,(byte)117,(byte)32,(byte)100,(byte)111,(byte)32,(byte)110,
		(byte)111,(byte)116,(byte)32,(byte)97,(byte)99,(byte)99,(byte)101,(byte)112,(byte)116
	};

	static final byte[] line72 = new byte[] {
		(byte)114,(byte)101,(byte)102,(byte)114,(byte)101,(byte)115,(byte)104,(byte)32,(byte)116,(byte)104,
		(byte)101,(byte)32,(byte)112,(byte)97,(byte)103,(byte)101,(byte)32,(byte)116,(byte)111,(byte)32,
		(byte)116,(byte)114,(byte)121,(byte)32,(byte)97,(byte)103,(byte)97,(byte)105,(byte)110
	};
	
}
