// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.protocol.skip;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.OpCode;
import net.md_5.bungee.protocol.Protocol;

public class PacketReader {
	private final Instruction[][] instructions;

	public PacketReader(final Protocol protocol) {
		this.instructions = new Instruction[protocol.getOpCodes().length][];
		for (int i = 0; i < this.instructions.length; ++i) {
			final List<Instruction> output = new ArrayList<Instruction>();
			final OpCode[] enums = protocol.getOpCodes()[i];
			if (enums != null) {
				for (final OpCode struct : enums) {
					try {
						output.add((Instruction) Instruction.class.getDeclaredField(struct.name()).get(null));
					} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex3) {
						throw new UnsupportedOperationException("No definition for " + struct.name());
					}
				}
				final List<Instruction> crushed = new ArrayList<Instruction>();
				int nextJumpSize = 0;
				for (final Instruction child : output) {
					if (child instanceof Jump) {
						nextJumpSize += ((Jump) child).len;
					} else {
						if (nextJumpSize != 0) {
							crushed.add(new Jump(nextJumpSize));
						}
						crushed.add(child);
						nextJumpSize = 0;
					}
				}
				if (nextJumpSize != 0) {
					crushed.add(new Jump(nextJumpSize));
				}
				this.instructions[i] = crushed.toArray(new Instruction[crushed.size()]);
			}
		}
	}

	public void tryRead(final short packetId, final ByteBuf in) {
		final Instruction[] packetDef = this.instructions[packetId];
		if (packetDef != null) {
			for (final Instruction instruction : packetDef) {
				instruction.read(in);
			}
		}
	}
}
