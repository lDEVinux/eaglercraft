package net.lax1dude.eaglercraft.glemu;

import static net.lax1dude.eaglercraft.EaglerAdapter.*;

import net.lax1dude.eaglercraft.adapter.EaglerAdapterImpl2.BufferArrayGL;
import net.lax1dude.eaglercraft.adapter.EaglerAdapterImpl2.BufferGL;
import net.lax1dude.eaglercraft.adapter.EaglerAdapterImpl2.ProgramGL;
import net.lax1dude.eaglercraft.adapter.EaglerAdapterImpl2.ShaderGL;
import net.lax1dude.eaglercraft.adapter.EaglerAdapterImpl2.UniformGL;
import net.lax1dude.eaglercraft.glemu.vector.Matrix4f;
import net.lax1dude.eaglercraft.glemu.vector.Vector2f;
import net.lax1dude.eaglercraft.glemu.vector.Vector4f;

public class FixedFunctionShader {
	
	private static final FixedFunctionShader[] instances = new FixedFunctionShader[4096]; //lol
	
	public static void refreshCoreGL() {
		for(int i = 0; i < instances.length; ++i) {
			if(instances[i] != null) {
				_wglDeleteProgram(instances[i].globject);
				instances[i] = null;
			}
		}
		shaderSource = null;
	}

	public static final int COLOR = 1;
	public static final int NORMAL = 2;
	public static final int TEXTURE0 = 4;
	public static final int TEXTURE1 = 8;
	public static final int TEXGEN = 16;
	public static final int LIGHTING = 32;
	public static final int FOG = 64;
	public static final int ALPHATEST = 128;
	public static final int UNIT0 = 256;
	public static final int UNIT1 = 512;
	public static final int FIX_ANISOTROPIC = 1024;
	public static final int SWAP_RB = 2048;
	
	public static FixedFunctionShader instance(int i) {
		FixedFunctionShader s = instances[i];
		if(s == null) {
			boolean CC_a_color = false;
			boolean CC_a_normal = false;
			boolean CC_a_texture0 = false;
			boolean CC_a_texture1 = false;
			boolean CC_TEX_GEN_STRQ = false;
			boolean CC_lighting = false;
			boolean CC_fog = false;
			boolean CC_alphatest = false;
			boolean CC_unit0 = false;
			boolean CC_unit1 = false;
			boolean CC_anisotropic = false;
			boolean CC_swap_rb = false;
			if((i & COLOR) == COLOR) {
				CC_a_color = true;
			}
			if((i & NORMAL) == NORMAL) {
				CC_a_normal = true;
			}
			if((i & TEXTURE0) == TEXTURE0) {
				CC_a_texture0 = true;
			}
			if((i & TEXTURE1) == TEXTURE1) {
				CC_a_texture1 = true;
			}
			if((i & TEXGEN) == TEXGEN) {
				CC_TEX_GEN_STRQ = true;
			}
			if((i & LIGHTING) == LIGHTING) {
				CC_lighting = true;
			}
			if((i & FOG) == FOG) {
				CC_fog = true;
			}
			if((i & ALPHATEST) == ALPHATEST) {
				CC_alphatest = true;
			}
			if((i & UNIT0) == UNIT0) {
				CC_unit0 = true;
			}
			if((i & UNIT1) == UNIT1) {
				CC_unit1 = true;
			}
			if((i & FIX_ANISOTROPIC) == FIX_ANISOTROPIC) {
				CC_anisotropic = true;
			}
			if((i & SWAP_RB) == SWAP_RB) {
				CC_swap_rb = true;
			}
			s = new FixedFunctionShader(i, CC_a_color, CC_a_normal, CC_a_texture0, CC_a_texture1, CC_TEX_GEN_STRQ, CC_lighting,
					CC_fog, CC_alphatest, CC_unit0, CC_unit1, CC_anisotropic, CC_swap_rb);
			instances[i] = s;
		}
		return s;
	}

	private static String shaderSource = null;
	
	private final boolean enable_color;
	private final boolean enable_normal;
	private final boolean enable_texture0;
	private final boolean enable_texture1;
	private final boolean enable_TEX_GEN_STRQ;
	private final boolean enable_lighting;
	private final boolean enable_fog;
	private final boolean enable_alphatest;
	private final boolean enable_unit0;
	private final boolean enable_unit1;
	private final boolean enable_anisotropic_fix;
	private final boolean enable_swap_rb;
	private final ProgramGL globject;

	private UniformGL u_matrix_m = null;
	private UniformGL u_matrix_p = null;
	private UniformGL u_matrix_t = null;
	
	private UniformGL u_fogColor = null;
	private UniformGL u_fogMode = null;
	private UniformGL u_fogStart = null;
	private UniformGL u_fogEnd = null;
	private UniformGL u_fogDensity = null;
	private UniformGL u_fogPremultiply = null;

	private UniformGL u_colorUniform = null;
	private UniformGL u_normalUniform = null;
	
	private UniformGL u_alphaTestF = null;

	private UniformGL u_textureGenS_M = null;
	private UniformGL u_textureGenT_M = null;
	private UniformGL u_textureGenR_M = null;
	private UniformGL u_textureGenQ_M = null;
	private UniformGL u_textureGenS_V = null;
	private UniformGL u_textureGenT_V = null;
	private UniformGL u_textureGenR_V = null;
	private UniformGL u_textureGenQ_V = null;
	
	private UniformGL u_texCoordV0 = null;
	private UniformGL u_texCoordV1 = null;
	
	private UniformGL u_light0Pos = null;
	private UniformGL u_light1Pos = null;
	//private UniformGL u_invertNormals = null;
	
	private UniformGL u_anisotropic_fix = null;

	private final int a_position;
	private final int a_texture0;
	private final int a_color;
	private final int a_normal;
	private final int a_texture1;
	
	private final int attributeIndexesToEnable;

	public final BufferArrayGL genericArray;
	public final BufferGL genericBuffer;
	public boolean bufferIsInitialized = false;
	
	private FixedFunctionShader(int j, boolean CC_a_color, boolean CC_a_normal, boolean CC_a_texture0, boolean CC_a_texture1, boolean CC_TEX_GEN_STRQ, boolean CC_lighting, 
			boolean CC_fog, boolean CC_alphatest, boolean CC_unit0, boolean CC_unit1, boolean CC_anisotropic_fix, boolean CC_swap_rb) {
		enable_color = CC_a_color;
		enable_normal = CC_a_normal;
		enable_texture0 = CC_a_texture0;
		enable_texture1 = CC_a_texture1;
		enable_TEX_GEN_STRQ = CC_TEX_GEN_STRQ;
		enable_lighting = CC_lighting;
		enable_fog = CC_fog;
		enable_alphatest = CC_alphatest;
		enable_unit0 = CC_unit0;
		enable_unit1 = CC_unit1;
		enable_anisotropic_fix = CC_anisotropic_fix;
		enable_swap_rb = CC_swap_rb;
		
		if(shaderSource == null) {
			shaderSource = fileContents("/glsl/core.glsl");
		}
		
		String source = "";
		if(enable_color) source += "\n#define CC_a_color\n";
		if(enable_normal) source += "#define CC_a_normal\n";
		if(enable_texture0) source += "#define CC_a_texture0\n";
		if(enable_texture1) source += "#define CC_a_texture1\n";
		if(enable_TEX_GEN_STRQ) source += "#define CC_TEX_GEN_STRQ\n";
		if(enable_lighting) source += "#define CC_lighting\n";
		if(enable_fog) source += "#define CC_fog\n";
		if(enable_alphatest) source += "#define CC_alphatest\n";
		if(enable_unit0) source += "#define CC_unit0\n";
		if(enable_unit1) source += "#define CC_unit1\n";
		if(enable_anisotropic_fix) source += "#define CC_patch_anisotropic\n";
		if(enable_swap_rb) source += "#define CC_swap_rb\n";
		source += shaderSource;
		
		ShaderGL v = _wglCreateShader(_wGL_VERTEX_SHADER);
		_wglShaderSource(v, _wgetShaderHeader()+"\n#define CC_VERT\n"+source);
		_wglCompileShader(v);
		
		if(!_wglGetShaderCompiled(v)) {
			System.err.println(("\n\n"+_wglGetShaderInfoLog(v)).replace("\n", "\n[/glsl/core.glsl][CC_VERT] "));
			throw new RuntimeException("broken shader file");
		}
		
		ShaderGL f = _wglCreateShader(_wGL_FRAGMENT_SHADER);
		_wglShaderSource(f, _wgetShaderHeader()+"\n#define CC_FRAG\n"+source);
		_wglCompileShader(f);
		
		if(!_wglGetShaderCompiled(f)) {
			System.err.println(("\n\n"+_wglGetShaderInfoLog(f)).replace("\n", "\n[/glsl/core.glsl][CC_FRAG] "));
			throw new RuntimeException("broken shader file");
		}
		
		globject = _wglCreateProgram();
		_wglAttachShader(globject, v);
		_wglAttachShader(globject, f);
		
		int i = 0;
		a_position = i++;
		_wglBindAttributeLocation(globject, a_position, "a_position");
		
		if(enable_texture0) {
			a_texture0 = i++;
			_wglBindAttributeLocation(globject, a_texture0, "a_texture0");
		}else {
			a_texture0 = -1;
		}
		if(enable_color) {
			a_color = i++;
			_wglBindAttributeLocation(globject, a_color, "a_color");
		}else {
			a_color = -1;
		}
		if(enable_normal) {
			a_normal = i++;
			_wglBindAttributeLocation(globject, a_normal, "a_normal");
		}else {
			a_normal = -1;
		}
		if(enable_texture1) {
			a_texture1 = i++;
			_wglBindAttributeLocation(globject, a_texture1, "a_texture1");
		}else {
			a_texture1 = -1;
		}
		
		attributeIndexesToEnable = i;
		
		_wglLinkProgram(globject);
		
		_wglDetachShader(globject, v);
		_wglDetachShader(globject, f);
		_wglDeleteShader(v);
		_wglDeleteShader(f);
		
		if(!_wglGetProgramLinked(globject)) {
			System.err.println(("\n\n"+_wglGetProgramInfoLog(globject)).replace("\n", "\n[LINKER] "));
			throw new RuntimeException("broken shader file");
		}
		
		_wglUseProgram(globject);

		u_matrix_m = _wglGetUniformLocation(globject, "matrix_m");
		u_matrix_p = _wglGetUniformLocation(globject, "matrix_p");
		u_matrix_t = _wglGetUniformLocation(globject, "matrix_t");

		u_colorUniform = _wglGetUniformLocation(globject, "colorUniform");
		
		if(enable_lighting) {
			u_normalUniform = _wglGetUniformLocation(globject, "normalUniform");
			//u_invertNormals = _wglGetUniformLocation(globject, "invertNormals");
			u_light0Pos = _wglGetUniformLocation(globject, "light0Pos");
			u_light1Pos = _wglGetUniformLocation(globject, "light1Pos");
		}
		
		if(enable_fog) {
			u_fogColor = _wglGetUniformLocation(globject, "fogColor");
			u_fogMode = _wglGetUniformLocation(globject, "fogMode");
			u_fogStart = _wglGetUniformLocation(globject, "fogStart");
			u_fogEnd = _wglGetUniformLocation(globject, "fogEnd");
			u_fogDensity = _wglGetUniformLocation(globject, "fogDensity");
			u_fogPremultiply = _wglGetUniformLocation(globject, "fogPremultiply");
		}
		
		if(enable_alphatest) {
			u_alphaTestF = _wglGetUniformLocation(globject, "alphaTestF");
		}
		
		if(enable_TEX_GEN_STRQ) {
			u_textureGenS_M = _wglGetUniformLocation(globject, "textureGenS_M");
			u_textureGenT_M = _wglGetUniformLocation(globject, "textureGenT_M");
			u_textureGenR_M = _wglGetUniformLocation(globject, "textureGenR_M");
			u_textureGenQ_M = _wglGetUniformLocation(globject, "textureGenQ_M");
			u_textureGenS_V = _wglGetUniformLocation(globject, "textureGenS_V");
			u_textureGenT_V = _wglGetUniformLocation(globject, "textureGenT_V");
			u_textureGenR_V = _wglGetUniformLocation(globject, "textureGenR_V");
			u_textureGenQ_V = _wglGetUniformLocation(globject, "textureGenQ_V");
		}
		
		if(enable_anisotropic_fix) {
			u_anisotropic_fix =  _wglGetUniformLocation(globject, "anisotropic_fix");
			_wglUniform2f(u_anisotropic_fix, 1024.0f * 63.0f / 64.0f, 1024.0f * 63.0f / 64.0f);
		}
		
		_wglUniform1i(_wglGetUniformLocation(globject, "tex0"), 0);
		_wglUniform1i(_wglGetUniformLocation(globject, "tex1"), 1);
		
		u_texCoordV0 = _wglGetUniformLocation(globject, "texCoordV0");
		u_texCoordV1 = _wglGetUniformLocation(globject, "texCoordV1");
		
		genericArray = _wglCreateVertexArray();
		genericBuffer = _wglCreateBuffer();
		_wglBindVertexArray(genericArray);
		_wglBindBuffer(_wGL_ARRAY_BUFFER, genericBuffer);
		setupArrayForProgram();
		
	}
	
	public void setupArrayForProgram() {
		_wglEnableVertexAttribArray(a_position);
		_wglVertexAttribPointer(a_position, 3, _wGL_FLOAT, false, 32, 0);
		if(enable_texture0) {
			_wglEnableVertexAttribArray(a_texture0);
			_wglVertexAttribPointer(a_texture0, 2, _wGL_FLOAT, false, 32, 12);
		}
		if(enable_color) {
			_wglEnableVertexAttribArray(a_color);
			_wglVertexAttribPointer(a_color, 4, _wGL_UNSIGNED_BYTE, true, 32, 20);
		}
		if(enable_normal) {
			_wglEnableVertexAttribArray(a_normal);
			_wglVertexAttribPointer(a_normal, 4, _wGL_UNSIGNED_BYTE, true, 32, 24);
		}
		if(enable_texture1) {
			_wglEnableVertexAttribArray(a_texture1);
			_wglVertexAttribPointer(a_texture1, 2, _wGL_SHORT, false, 32, 28);
		}
	}
	
	public void useProgram() {
		_wglUseProgram(globject);
	}
	
	public void unuseProgram() {
		
	}

	private float[] modelBuffer = new float[16];
	private float[] projectionBuffer = new float[16];
	private float[] textureBuffer = new float[16];

	private Matrix4f modelMatrix = (Matrix4f) new Matrix4f().setZero();
	private Matrix4f projectionMatrix = (Matrix4f) new Matrix4f().setZero();
	private Matrix4f textureMatrix = (Matrix4f) new Matrix4f().setZero();
	private Vector4f light0Pos = new Vector4f();
	private Vector4f light1Pos = new Vector4f();
	private Vector2f anisotropicFix = new Vector2f(0.0f, 0.0f);
	
	public void setAnisotropicFix(float x, float y) {
		if(anisotropicFix.x != x || anisotropicFix.y != y) {
			anisotropicFix.x = x;
			anisotropicFix.y = y;
			_wglUniform2f(u_anisotropic_fix, x, y);
		}
	}

	public void setModelMatrix(Matrix4f mat) {
		if(!mat.equals(modelMatrix)) {
			modelMatrix.load(mat).store(modelBuffer);
			_wglUniformMat4fv(u_matrix_m, modelBuffer);
		}
	}
	public void setProjectionMatrix(Matrix4f mat) {
		if(!mat.equals(projectionMatrix)) {
			projectionMatrix.load(mat).store(projectionBuffer);
			_wglUniformMat4fv(u_matrix_p, projectionBuffer);
		}
	}
	public void setTextureMatrix(Matrix4f mat) {
		if(!mat.equals(textureMatrix)) {
			textureMatrix.load(mat).store(textureBuffer);
			_wglUniformMat4fv(u_matrix_t, textureBuffer);
		}
	}
	public void setLightPositions(Vector4f pos0, Vector4f pos1) {
		if(!pos0.equals(light0Pos) || !pos1.equals(light1Pos)) {
			light0Pos.set(pos0);
			light1Pos.set(pos1);
			_wglUniform3f(u_light0Pos, light0Pos.x, light0Pos.y, light0Pos.z);
			_wglUniform3f(u_light1Pos, light1Pos.x, light1Pos.y, light1Pos.z);
		}
	}

	private int fogMode = 0;
	public void setFogMode(int mode) {
		if(fogMode != mode) {
			fogMode = mode;
			_wglUniform1i(u_fogMode, mode % 2);
			_wglUniform1f(u_fogPremultiply, mode / 2);
		}
	}

	private float fogColorR = 0.0f;
	private float fogColorG = 0.0f;
	private float fogColorB = 0.0f;
	private float fogColorA = 0.0f;
	public void setFogColor(float r, float g, float b, float a) {
		if(fogColorR != r || fogColorG != g || fogColorB != b || fogColorA != a) {
			fogColorR = r;
			fogColorG = g;
			fogColorB = b;
			fogColorA = a;
			_wglUniform4f(u_fogColor, fogColorR, fogColorG, fogColorB, fogColorA);
		}
	}

	private float fogStart = 0.0f;
	private float fogEnd = 0.0f;
	public void setFogStartEnd(float s, float e) {
		if(fogStart != s || fogEnd != e) {
			fogStart = s;
			fogEnd = e;
			_wglUniform1f(u_fogStart, fogStart);
			_wglUniform1f(u_fogEnd, fogEnd);
		}
	}

	private float fogDensity = 0.0f;
	public void setFogDensity(float d) {
		if(fogDensity != d) {
			fogDensity = d;
			_wglUniform1f(u_fogDensity, fogDensity);
		}
	}
	
	private float alphaTestValue = 0.0f;
	public void setAlphaTest(float limit) {
		if(alphaTestValue != limit) {
			alphaTestValue = limit;
			_wglUniform1f(u_alphaTestF, alphaTestValue);
		}
	}

	private float tex0x = 0.0f;
	private float tex0y = 0.0f;
	public void setTex0Coords(float x, float y) {
		if(tex0x != x || tex0y != y) {
			tex0x = x;
			tex0y = y;
			_wglUniform2f(u_texCoordV0, tex0x, tex0y);
		}
	}
	
	private float tex1x = 0.0f;
	private float tex1y = 0.0f;
	public void setTex1Coords(float x, float y) {
		if(tex1x != x || tex1y != y) {
			tex1x = x;
			tex1y = y;
			_wglUniform2f(u_texCoordV1, tex1x, tex1y);
		}
	}
	
	public void setTexGenS(int plane, float x, float y, float z, float w) {
		_wglUniform1i(u_textureGenS_M, plane);
		_wglUniform4f(u_textureGenS_V, x, y, z, w);
	}
	
	public void setTexGenT(int plane, float x, float y, float z, float w) {
		_wglUniform1i(u_textureGenT_M, plane);
		_wglUniform4f(u_textureGenT_V, x, y, z, w);
	}
	
	public void setTexGenR(int plane, float x, float y, float z, float w) {
		_wglUniform1i(u_textureGenR_M, plane);
		_wglUniform4f(u_textureGenR_V, x, y, z, w);
	}
	
	public void setTexGenQ(int plane, float x, float y, float z, float w) {
		_wglUniform1i(u_textureGenQ_M, plane);
		_wglUniform4f(u_textureGenQ_V, x, y, z, w);
	}

	private float colorUniformR = 0.0f;
	private float colorUniformG = 0.0f;
	private float colorUniformB = 0.0f;
	private float colorUniformA = 0.0f;
	public void setColor(float r, float g, float b, float a) {
		if(colorUniformR != r || colorUniformG != g || colorUniformB != b || colorUniformA != a) {
			colorUniformR = r;
			colorUniformG = g;
			colorUniformB = b;
			colorUniformA = a;
			_wglUniform4f(u_colorUniform, colorUniformR, colorUniformG, colorUniformB, colorUniformA);
		}
	}

	private float normalUniformX = 0.0f;
	private float normalUniformY = 0.0f;
	private float normalUniformZ = 0.0f;
	public void setNormal(float x, float y, float z) {
		if(normalUniformX != x || normalUniformY != y || normalUniformZ != z) {
			normalUniformX = x;
			normalUniformY = y;
			normalUniformZ = z;
			_wglUniform3f(u_normalUniform, normalUniformX, normalUniformY, normalUniformZ);
		}
	}

}
