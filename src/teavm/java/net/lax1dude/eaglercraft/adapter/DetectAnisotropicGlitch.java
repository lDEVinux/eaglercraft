package net.lax1dude.eaglercraft.adapter;

import static net.lax1dude.eaglercraft.adapter.teavm.WebGL2RenderingContext.*;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.typedarrays.Uint8Array;

import net.lax1dude.eaglercraft.Client;
import net.lax1dude.eaglercraft.adapter.teavm.WebGL2RenderingContext;
import net.lax1dude.eaglercraft.adapter.teavm.WebGLVertexArray;

import org.teavm.jso.webgl.*;

public class DetectAnisotropicGlitch {
	
	private static boolean known = false;
	private static boolean detected = false;

	public static boolean hasGlitch() {
		if(!known) {
			detected = detect();
			known = true;
		}
		return detected;
	}
	
	public static boolean detect() {
		HTMLCanvasElement cvs = (HTMLCanvasElement) Window.current().getDocument().createElement("canvas");
		
		cvs.setWidth(400);		
		cvs.setHeight(300);
		
		WebGL2RenderingContext ctx = (WebGL2RenderingContext) cvs.getContext("webgl2");
		
		if(ctx == null) {
			Client.showIncompatibleScreen("WebGL 2.0 is not supported on this device!");
			throw new UnsupportedOperationException("WebGL 2 is not supported on this device!");
		}
		
		if(ctx.getExtension("EXT_texture_filter_anisotropic") != null) {
			
			String vshSrc = "#version 300 es\n"
					+ "precision lowp float;"
					+ "in vec2 a_pos;"
					+ "out vec2 v_pos;"
					+ "void main() {"
					+ "   gl_Position = vec4((v_pos = a_pos) * 2.0 - 1.0, 0.0, 1.0);"
					+ "}";
			
			String fshSrc = "#version 300 es\n"
					+ "precision lowp float;"
					+ "precision lowp sampler2D;"
					+ "uniform sampler2D tex;"
					+ "in vec2 v_pos;"
					+ "out vec4 fragColor;"
					+ "void main() {"
					+ "   fragColor = vec4(texture(tex, v_pos).rgb, 1.0);"
					+ "}";
			
			WebGLShader vsh = ctx.createShader(VERTEX_SHADER);
			ctx.shaderSource(vsh, vshSrc);
			ctx.compileShader(vsh);
			
			if(!ctx.getShaderParameterb(vsh, COMPILE_STATUS)) {
				System.err.println("ERROR: Could not check for ANGLE Issue #4994, VERTEX_SHADER did not compile:");
				System.err.println(ctx.getShaderInfoLog(vsh));
				ctx.deleteShader(vsh);
				return false;
			}
			
			WebGLShader fsh = ctx.createShader(FRAGMENT_SHADER);
			ctx.shaderSource(fsh, fshSrc);
			ctx.compileShader(fsh);
			
			if(!ctx.getShaderParameterb(fsh, COMPILE_STATUS)) {
				System.err.println("ERROR: Could not check for ANGLE Issue #4994, FRAGMENT_SHADER did not compile:");
				System.err.println(ctx.getShaderInfoLog(fsh));
				ctx.deleteShader(vsh);
				ctx.deleteShader(fsh);
				return false;
			}
			
			WebGLProgram pr = ctx.createProgram();
			
			ctx.attachShader(pr, vsh);
			ctx.attachShader(pr, fsh);

			ctx.bindAttribLocation(pr, 0, "a_pos");
			ctx.bindAttribLocation(pr, 0, "fragColor");
			
			ctx.linkProgram(pr);
			
			ctx.detachShader(pr, vsh);
			ctx.detachShader(pr, fsh);

			ctx.deleteShader(vsh);
			ctx.deleteShader(fsh);
			
			if(!ctx.getProgramParameterb(pr, LINK_STATUS)) {
				System.err.println("ERROR: Could not check for ANGLE Issue #4994, program did not link:");
				System.err.println(ctx.getProgramInfoLog(pr));
				ctx.deleteProgram(pr);
				return false;
			}
			
			ctx.useProgram(pr);
			
			ctx.uniform1i(ctx.getUniformLocation(pr, "tex"), 0);
			
			byte x0 = (byte)0x00;
			byte x1 = (byte)0xFF;
			
			byte[] pixelsData = new byte[] {
					x0, x0, x0, x1,
					x0, x0, x0, x1,
					x1, x1, x1, x1,
					x0, x0, x0, x1,
					x0, x0, x0, x1,
					x0, x0, x0, x1,
					x1, x1, x1, x1,
					x0, x0, x0, x1,
					x0, x0, x0, x1,
					x0, x0, x0, x1,
					x1, x1, x1, x1,
					x0, x0, x0, x1
			};
			
			Uint8Array pixels = Uint8Array.create(pixelsData.length);
			pixels.set(pixelsData);
			
			WebGLTexture tex = ctx.createTexture();
			
			ctx.bindTexture(TEXTURE_2D, tex);
			
			ctx.texParameteri(TEXTURE_2D, TEXTURE_WRAP_S, REPEAT);
			ctx.texParameteri(TEXTURE_2D, TEXTURE_WRAP_T, REPEAT);
			ctx.texParameteri(TEXTURE_2D, TEXTURE_MIN_FILTER, NEAREST_MIPMAP_LINEAR);
			ctx.texParameteri(TEXTURE_2D, TEXTURE_MAG_FILTER, NEAREST);
			ctx.texParameterf(TEXTURE_2D, TEXTURE_MAX_ANISOTROPY_EXT, 16.0f);
			
			ctx.texImage2D(TEXTURE_2D, 0, RGBA, 4, 3, 0, RGBA, UNSIGNED_BYTE, pixels);
			ctx.generateMipmap(TEXTURE_2D);
			
			float[] vertsData = new float[] {
					0.0f, 0.0f,
					1.0f, 0.0f,
					0.0f, 1.0f,
					1.0f, 0.0f,
					1.0f, 1.0f,
					0.0f, 1.0f
			};
			
			Float32Array verts = Float32Array.create(vertsData.length);
			verts.set(vertsData);
			
			WebGLBuffer buf = ctx.createBuffer();
			
			ctx.bindBuffer(ARRAY_BUFFER, buf);
			ctx.bufferData(ARRAY_BUFFER, verts, STATIC_DRAW);
			
			WebGLVertexArray arr = ctx.createVertexArray();
			
			ctx.bindVertexArray(arr);
			
			ctx.enableVertexAttribArray(0);
			ctx.vertexAttribPointer(0, 2, FLOAT, false, 8, 0);
			
			ctx.viewport(0, 0, 400, 300);
			ctx.drawArrays(TRIANGLES, 0, 6);
			
			ctx.deleteVertexArray(arr);
			ctx.deleteBuffer(buf);
			ctx.deleteTexture(tex);
			ctx.deleteProgram(pr);
			
			Uint8Array readPx = Uint8Array.create(4);
			ctx.readPixels(175, 150, 1, 1, RGBA, UNSIGNED_BYTE, readPx);
			
			boolean b = (readPx.get(0) + readPx.get(1) + readPx.get(2)) != 0;
			
			if(b) {
				System.out.println("ANGLE issue #4994 is unpatched on this browser, enabling anisotropic fix");
			}
			
			return b;
		}else {
			System.err.println("WARNING: EXT_texture_filter_anisotropic is not supported!");
			return false;
		}
		
	}
	
}
