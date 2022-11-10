// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.netty;

import java.beans.ConstructorProperties;

import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class CipherBase {
	private final Cipher cipher;
	private ThreadLocal<byte[]> heapInLocal;
	private ThreadLocal<byte[]> heapOutLocal;

	private byte[] bufToByte(final ByteBuf in) {
		byte[] heapIn = this.heapInLocal.get();
		final int readableBytes = in.readableBytes();
		if (heapIn.length < readableBytes) {
			heapIn = new byte[readableBytes];
			this.heapInLocal.set(heapIn);
		}
		in.readBytes(heapIn, 0, readableBytes);
		return heapIn;
	}

	protected ByteBuf cipher(final ChannelHandlerContext ctx, final ByteBuf in) throws ShortBufferException {
		final int readableBytes = in.readableBytes();
		final byte[] heapIn = this.bufToByte(in);
		final ByteBuf heapOut = ctx.alloc().heapBuffer(this.cipher.getOutputSize(readableBytes));
		heapOut.writerIndex(this.cipher.update(heapIn, 0, readableBytes, heapOut.array(), heapOut.arrayOffset()));
		return heapOut;
	}

	protected void cipher(final ByteBuf in, final ByteBuf out) throws ShortBufferException {
		final int readableBytes = in.readableBytes();
		final byte[] heapIn = this.bufToByte(in);
		byte[] heapOut = this.heapOutLocal.get();
		final int outputSize = this.cipher.getOutputSize(readableBytes);
		if (heapOut.length < outputSize) {
			heapOut = new byte[outputSize];
			this.heapOutLocal.set(heapOut);
		}
		out.writeBytes(heapOut, 0, this.cipher.update(heapIn, 0, readableBytes, heapOut));
	}

	@ConstructorProperties({ "cipher" })
	protected CipherBase(final Cipher cipher) {
		this.heapInLocal = new EmptyByteThreadLocal();
		this.heapOutLocal = new EmptyByteThreadLocal();
		if (cipher == null) {
			throw new NullPointerException("cipher");
		}
		this.cipher = cipher;
	}

	private static class EmptyByteThreadLocal extends ThreadLocal<byte[]> {
		@Override
		protected byte[] initialValue() {
			return new byte[0];
		}
	}
}
