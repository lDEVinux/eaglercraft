package net.lax1dude.eaglercraft.glemu;

import static net.lax1dude.eaglercraft.EaglerAdapter.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.adapter.Tessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.Gui;
import net.minecraft.src.MathHelper;

public class EffectPipeline {

	private static FramebufferGL noiseGenFramebuffer = null;
	private static TextureGL noiseGenTexture = null;

	private static ProgramGL noiseProgram = null;
	private static TextureGL noiseSourceTexture = null;
	private static UniformGL noiseCounter = null;
	
	private static BufferArrayGL renderQuadArray = null;
	private static BufferGL renderQuadBuffer;

	private static final int NOISE_WIDTH = 128;
	private static final int NOISE_HEIGHT = 128;
	
	private static boolean hasInit = false;

	public static void updateNoiseTexture(int viewportW, int viewportH, float intensity) {
		if(!hasInit) {
			hasInit = true;
			String src = fileContents("/glsl/adderallNoise.glsl");
			if(src != null) {
				renderQuadArray = _wglCreateVertexArray();
				renderQuadBuffer = _wglCreateBuffer();
				
				IntBuffer upload = (isWebGL ? IntBuffer.wrap(new int[12]) : ByteBuffer.allocateDirect(12 << 2).order(ByteOrder.nativeOrder()).asIntBuffer());
				upload.put(Float.floatToRawIntBits(0.0f)); upload.put(Float.floatToRawIntBits(0.0f));
				upload.put(Float.floatToRawIntBits(0.0f)); upload.put(Float.floatToRawIntBits(1.0f));
				upload.put(Float.floatToRawIntBits(1.0f)); upload.put(Float.floatToRawIntBits(0.0f));
				upload.put(Float.floatToRawIntBits(0.0f)); upload.put(Float.floatToRawIntBits(1.0f));
				upload.put(Float.floatToRawIntBits(1.0f)); upload.put(Float.floatToRawIntBits(1.0f));
				upload.put(Float.floatToRawIntBits(1.0f)); upload.put(Float.floatToRawIntBits(0.0f));
				upload.flip();
				
				_wglBindVertexArray0(renderQuadArray);
				_wglBindBuffer(_wGL_ARRAY_BUFFER, renderQuadBuffer);
				_wglBufferData0(_wGL_ARRAY_BUFFER, upload, _wGL_STATIC_DRAW);
				_wglEnableVertexAttribArray(0);
				_wglVertexAttribPointer(0, 2, _wGL_FLOAT, false, 8, 0);
				
				ShaderGL pvert_shader = _wglCreateShader(_wGL_VERTEX_SHADER);
	
				_wglShaderSource(pvert_shader, _wgetShaderHeader() + "\n" + fileContents("/glsl/pvert.glsl"));
				_wglCompileShader(pvert_shader);
	
				if (!_wglGetShaderCompiled(pvert_shader)) System.err.println(("\n" + _wglGetShaderInfoLog(pvert_shader)).replace("\n", "\n[/glsl/pvert.glsl] ") + "\n");
				
				ShaderGL noise_shader = _wglCreateShader(_wGL_FRAGMENT_SHADER);
				_wglShaderSource(noise_shader, _wgetShaderHeader() + "\n" + src);
				_wglCompileShader(noise_shader);
				
				if (!_wglGetShaderCompiled(noise_shader)) System.err.println(("\n" + _wglGetShaderInfoLog(noise_shader)).replace("\n", "\n[/glsl/fxaa.glsl] ") + "\n");
				
				noiseProgram = _wglCreateProgram();
				_wglAttachShader(noiseProgram, pvert_shader);
				_wglAttachShader(noiseProgram, noise_shader);
				_wglLinkProgram(noiseProgram);
				_wglDetachShader(noiseProgram, pvert_shader);
				_wglDetachShader(noiseProgram, noise_shader);
				_wglDeleteShader(pvert_shader);
				_wglDeleteShader(noise_shader);
				
				if(!_wglGetProgramLinked(noiseProgram)) {
					System.err.println(("\n"+_wglGetProgramInfoLog(noiseProgram)).replace("\n", "\n[/glsl/fxaa.glsl][LINKER] ") + "\n");
					noiseProgram = null;
					throw new RuntimeException("Invalid shader code");
				}
				
				noiseCounter = _wglGetUniformLocation(noiseProgram, "counter");
				
				noiseSourceTexture = _wglGenTextures();
				_wglBindTexture(_wGL_TEXTURE_2D, noiseSourceTexture);
				_wglTexParameteri(_wGL_TEXTURE_2D, _wGL_TEXTURE_MAG_FILTER, _wGL_NEAREST);
				_wglTexParameteri(_wGL_TEXTURE_2D, _wGL_TEXTURE_MIN_FILTER, _wGL_NEAREST);
				_wglTexParameteri(_wGL_TEXTURE_2D, _wGL_TEXTURE_WRAP_S, _wGL_REPEAT);
				_wglTexParameteri(_wGL_TEXTURE_2D, _wGL_TEXTURE_WRAP_T, _wGL_REPEAT);

				EaglercraftRandom noiseRandom = new EaglercraftRandom(696969696969l);
				
				byte[] b = new byte[NOISE_WIDTH * NOISE_HEIGHT];
				noiseRandom.nextBytes(b);
				
				ByteBuffer buf = GLAllocation.createDirectByteBuffer(NOISE_WIDTH * NOISE_HEIGHT);
				buf.put(b);
				buf.flip();
				
				_wglTexImage2D(_wGL_TEXTURE_2D, 0, _wGL_R8, NOISE_WIDTH, NOISE_HEIGHT, 0, _wGL_RED, _wGL_UNSIGNED_BYTE, buf);
				
				noiseGenFramebuffer = _wglCreateFramebuffer();
				noiseGenTexture = _wglGenTextures();

				_wglBindTexture(_wGL_TEXTURE_2D, noiseGenTexture);
				_wglTexParameteri(_wGL_TEXTURE_2D, _wGL_TEXTURE_MAG_FILTER, _wGL_LINEAR);
				_wglTexParameteri(_wGL_TEXTURE_2D, _wGL_TEXTURE_MIN_FILTER, _wGL_LINEAR);
				_wglTexParameteri(_wGL_TEXTURE_2D, _wGL_TEXTURE_WRAP_S, _wGL_REPEAT);
				_wglTexParameteri(_wGL_TEXTURE_2D, _wGL_TEXTURE_WRAP_T, _wGL_REPEAT);

				_wglTexImage2D(_wGL_TEXTURE_2D, 0, _wGL_RGBA, NOISE_WIDTH, NOISE_HEIGHT, 0, _wGL_RGBA, _wGL_UNSIGNED_BYTE, (ByteBuffer)null);
				
				_wglBindFramebuffer(_wGL_FRAMEBUFFER, noiseGenFramebuffer);
				_wglFramebufferTexture2D(_wGL_COLOR_ATTACHMENT0, noiseGenTexture);
			}
		}
		
		if(noiseProgram != null) {
			_wglBindFramebuffer(_wGL_FRAMEBUFFER, noiseGenFramebuffer);
			_wglViewport(0, 0, NOISE_WIDTH, NOISE_HEIGHT);
			_wglUseProgram(noiseProgram);
			
			long l = System.currentTimeMillis();
			if(timer > 0l && l - timer < 20000l) {
				counter += (float)((l - timer) * 0.0007) * intensity;
				if(counter > 10000.0f) {
					counter = 0.0f;
				}
			}
			timer = l;
			
			_wglUniform1f(noiseCounter, counter * 3.0f);
			
			_wglBindVertexArray0(renderQuadArray);
			glActiveTexture(_wGL_TEXTURE0);
			_wglBindTexture(_wGL_TEXTURE_2D, noiseSourceTexture);
			glDisable(GL_DEPTH_TEST);
			glDisable(GL_CULL_FACE);
			glDisable(GL_BLEND);
			glColorMask(true, true, true, true);
			glDepthMask(false);
			_wglDrawArrays(_wGL_TRIANGLES, 0, 6);
			glColorMask(true, true, true, false);
			glDepthMask(true);
			glEnable(GL_DEPTH_TEST);
			glEnable(GL_CULL_FACE);
			_wglBindFramebuffer(_wGL_FRAMEBUFFER, null);
			_wglViewport(0, 0, viewportW, viewportH);
		}

	}

	private static float counter = 0.0f;
	private static long timer = 0l;
	
	public static void drawNoise(int viewportW, int viewportH, float intensity) {
		if(noiseProgram == null) {
			return;
		}
		
		// three guesses to figure out what this does
		
		glActiveTexture(_wGL_TEXTURE0);
		_wglBindTexture(_wGL_TEXTURE_2D, noiseGenTexture);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glDisable(GL_ALPHA_TEST);
		glPushMatrix(); // 1
		glLoadIdentity();
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadIdentity();
		glMatrixMode(GL_MODELVIEW);
		
		float aspect = (float) viewportW / viewportH;
		
		float bb = 3.0f * intensity;
		float intensityModifier = 0.0f;
		
		EntityLiving lv = Minecraft.getMinecraft().renderViewEntity;
		if(lv != null) {
			int j = lv.getBrightnessForRender(0.0f);
			intensityModifier = Math.min(1.0f - ((j / 65536) / 256.0f), 1.0f - ((j % 65536) / 256.0f)) * 3.0f;
			bb += intensityModifier * bb;
		}
		
		glColor4f(0.0166f * bb, 0.0166f * bb, 0.0166f * bb, 0.0f);

		glPushMatrix(); // 2
		
		_wglBlendColor(0.0f, 0.0f, 0.0f, 1.0f - (intensity * 0.1f));
		glBlendFunc(GL_DST_COLOR, GL_CONSTANT_ALPHA);
		
		glMatrixMode(GL_TEXTURE);
		glPushMatrix();
		glScalef(1.5f, 1.25f * aspect, 1.0f);
		drawGradientTextureRect(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
		
		if(intensityModifier > 1.5f) {
			glBlendFunc(GL_SRC_ALPHA, GL_ONE);
			glColor4f(0.8f, 1.0f, 0.5f, (intensityModifier - 1.5f) * 0.03f * intensity);
			glPushMatrix();
			glScalef(0.5f, 0.5f, 1.0f);
			drawGradientTextureRect(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
			glPopMatrix();
			glColor4f(0.05f, 0.05f, 0.05f, 0.0f);
			glBlendFunc(GL_DST_COLOR, GL_CONSTANT_ALPHA);
		}
		
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);
		glPopMatrix(); // 2

		float fs1 = MathHelper.sin(counter) + MathHelper.sin(counter * 0.7f + 0.3f) * 0.5f;
		fs1 = fs1 * 0.1f;
		float fs3 = MathHelper.sin(counter * 0.7f) + MathHelper.sin(counter * 1.1f + 0.6f) * 0.4f
				+ MathHelper.sin(counter * 2.3f + 1.1f) * 0.2f + MathHelper.sin(counter * 3.3f + 0.75f) * 0.3f;

		glPushMatrix(); // 1.5
		glRotatef(50.0f * fs1, 0.0f, 0.0f, 1.0f);
		
		_wglBlendColor(0.0f, 0.0f, 0.0f, 1.0f);

		for(int i = 0; i < 4; ++i) {
			float fs2 = MathHelper.sin(counter * 0.7f + i * i * 0.2f) + MathHelper.sin(counter * 2.2f + 0.44f + i * 0.3f) * 0.2f +
					MathHelper.sin(counter * 5.0f + 0.7f + i * i * 0.5f) * 0.2f;
			
			glPushMatrix(); // 2

			glRotatef(90.0f * i, 0.0f, 0.0f, 1.0f);
			glTranslatef(-fs1 * 0.1f, 0.3f + Math.max(fs3 * 0.25f + 0.1f, -0.25f), 0.0f);
			
			glRotatef(45.0f, 0.0f, 0.0f, 1.0f);
			glScalef(1.5f, 0.7f, 1.0f);
			glTranslatef(0.0f, 1.0f, 0.0f);
	
			float f1 = Math.max(fs1 * 0.5f + 1.0f, 0.0f);
			glColor4f(0.1f * bb * f1, 0.1f * bb * f1, 0.1f * bb * f1, 0.0f);
			
			
			
			glMatrixMode(GL_TEXTURE);
			glPushMatrix();
			
			glTranslatef(-counter * 0.2f + fs1 * 1.4f, -counter * 0.2f, 0.0f);
			glScalef(3.0f * 1.5f, 0.5f * aspect, 1.0f);
			
			drawGradientTextureRect(1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
			glPopMatrix();
			
	
			glMatrixMode(GL_MODELVIEW);
			
			glTranslatef(0.0f, Math.max(fs2 * 0.5f + 1.0f, 0.0f) * 0.8f, 0.0f);
			glMatrixMode(GL_TEXTURE);
			
			glPushMatrix();
			
			glTranslatef(-counter * 0.2f, counter * 0.2f, 0.0f);
			glScalef(3.0f * 1.5f, 0.3f * aspect, 1.0f);
			
			glRotatef(190.0f, 0.0f, 0.0f, 1.0f);
			
			glColor4f(0.1f * bb, 0.1f * bb, 0.1f * bb, 0.0f);
			
			drawGradientTextureRect(1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
			glPopMatrix();
			
			glMatrixMode(GL_MODELVIEW);
			glPopMatrix(); // 2
		}
		
		glPopMatrix(); // 1.5
		
		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);
		glPopMatrix(); // 1
		glEnable(GL_ALPHA_TEST);
		glDisable(GL_BLEND);
	}
	
	private static void drawGradientTextureRect(float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2) {
		Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.setColorRGBA_F(r2, g2, b2, a2);
		var9.addVertexWithUV(-1.0, -1.0, 0.0, 0.0, 0.0);
		var9.addVertexWithUV(1.0, -1.0, 0.0, 1.0, 0.0);
		var9.setColorRGBA_F(r1, g1, b1, a1);
		var9.addVertexWithUV(1.0, 1.0, 0.0, 1.0, 1.0);
		var9.addVertexWithUV(-1.0, 1.0, 0.0, 0.0, 1.0);
		var9.draw();
	}
	
}
