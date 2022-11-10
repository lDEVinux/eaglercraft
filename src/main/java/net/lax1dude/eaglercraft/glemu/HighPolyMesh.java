package net.lax1dude.eaglercraft.glemu;

import static net.lax1dude.eaglercraft.adapter.EaglerAdapterImpl2.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import net.lax1dude.eaglercraft.adapter.EaglerAdapterImpl2.BufferArrayGL;
import net.lax1dude.eaglercraft.adapter.EaglerAdapterImpl2.BufferGL;
import net.minecraft.src.GLAllocation;

public class HighPolyMesh {
	
	final BufferArrayGL vertexArray;
	final BufferGL vertexBuffer;
	final BufferGL indexBuffer;
	
	public final int vertexCount;
	public final int indexCount;
	
	public final boolean hasTexture;
	
	public HighPolyMesh(BufferArrayGL vertexArray, BufferGL vertexBuffer, BufferGL indexBuffer, int vertexCount,
			int indexCount, boolean hasTexture) {
		this.vertexArray = vertexArray;
		this.vertexBuffer = vertexBuffer;
		this.indexBuffer = indexBuffer;
		this.vertexCount = vertexCount;
		this.indexCount = indexCount;
		this.hasTexture = hasTexture;
	}
	
	public void free() {
		_wglDeleteVertexArray(vertexArray);
		_wglDeleteBuffer(vertexBuffer);
		_wglDeleteBuffer(indexBuffer);
	}

	static final byte[] headerSequence = "!EAG%mdl".getBytes(StandardCharsets.UTF_8);
	
	static HighPolyMesh loadMeshData(byte[] mesh) throws IOException {
		DataInputStream mdlIn = new DataInputStream(new ByteArrayInputStream(mesh));
		
		byte[] hd = new byte[headerSequence.length];
		mdlIn.read(hd);
		if(!Arrays.equals(headerSequence, hd)) {
			throw new IOException("Not an Eaglercraft HighPoly Mesh");
		}
		
		char CT = (char)mdlIn.read();
		
		boolean textureEnabled;
		if(CT == 'C') {
			textureEnabled = false;
		}else if(CT == 'T') {
			textureEnabled = true;
		}else {
			throw new IOException("Unsupported mesh type '" + CT + "'!");
		}
		
		mdlIn.skipBytes(mdlIn.readUnsignedShort());

		int vertexCount = mdlIn.readInt();
		int indexCount = mdlIn.readInt();
		int byteIndexCount = indexCount;
		if(byteIndexCount % 2 != 0) { // must round up to int
			byteIndexCount += 1;
		}
		int stride = textureEnabled ? 24 : 16;

		int intsOfVertex = vertexCount * stride / 4;
		int intsOfIndex = byteIndexCount / 2;
		int intsTotal = intsOfIndex + intsOfVertex;
		IntBuffer up1 = GLAllocation.createDirectIntBuffer(intsTotal);
		
		for(int i = 0; i < intsTotal; ++i) {
			int ch1 = mdlIn.read();
			int ch2 = mdlIn.read();
			int ch3 = mdlIn.read();
			int ch4 = mdlIn.read();
			if ((ch1 | ch2 | ch3 | ch4) < 0) throw new EOFException(); // rip
			up1.put((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
		}
		
		BufferArrayGL vertexArray = _wglCreateVertexArray();
		_wglBindVertexArray(vertexArray);

		up1.position(0).limit(intsOfVertex);
		
		BufferGL vertexBuffer = _wglCreateBuffer();
		_wglBindBuffer(_wGL_ARRAY_BUFFER, vertexBuffer);
		_wglBufferData0(_wGL_ARRAY_BUFFER, up1, _wGL_STATIC_DRAW);
		
		up1.position(intsOfVertex).limit(intsTotal);

		BufferGL indexBuffer = _wglCreateBuffer();
		_wglBindBuffer(_wGL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		_wglBufferData0(_wGL_ELEMENT_ARRAY_BUFFER, up1, _wGL_STATIC_DRAW);
		
		_wglEnableVertexAttribArray(0);
		_wglVertexAttribPointer(0, 3, _wGL_FLOAT, false, stride, 0);
		
		if(textureEnabled) {
			_wglEnableVertexAttribArray(1);
			_wglVertexAttribPointer(1, 2, _wGL_FLOAT, false, stride, 16);
		}
		
		_wglEnableVertexAttribArray(textureEnabled ? 2 : 1);
		_wglVertexAttribPointer(textureEnabled ? 2 : 1, 4, _wGL_UNSIGNED_BYTE, true, stride, 12);
		
		return new HighPolyMesh(vertexArray, vertexBuffer, indexBuffer, vertexCount, indexCount, textureEnabled);
	}
	
}
