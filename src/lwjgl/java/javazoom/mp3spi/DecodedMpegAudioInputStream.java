/*
 *   DecodedMpegAudioInputStream.
 * 
 *   JavaZOOM : mp3spi@javazoom.net 
 * 				http://www.javazoom.net
 * 
 * Copyright (c) 2012 by fireandfuel from Cuina Team (http://www.cuina.byethost12.com/)
 *
 *-----------------------------------------------------------------------------
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *------------------------------------------------------------------------
 */

package javazoom.mp3spi;

import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

import tritonus.TAsynchronousFilteredAudioInputStream;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.Obuffer;

/**
 * Main decoder.
 */
public class DecodedMpegAudioInputStream extends TAsynchronousFilteredAudioInputStream
{
	private InputStream m_encodedStream;
	private Bitstream m_bitstream;
	private Decoder m_decoder;
	private Header m_header;
	private DMAISObuffer m_oBuffer;

	// Bytes info.
	private long byteslength = -1;
	private long currentByte = 0;
	// Frame info.
	private int frameslength = -1;
	private long currentFrame = 0;
	private int currentFramesize = 0;

	public DecodedMpegAudioInputStream(AudioFormat outputFormat,
			AudioInputStream bufferedInputStream)
	{
		super(outputFormat, -1);

		try
		{
			// Try to find out inputstream length to allow skip.
			byteslength = bufferedInputStream.available();
		} catch (IOException e)
		{
			byteslength = -1;
		}
		m_encodedStream = bufferedInputStream;
		m_bitstream = new Bitstream(bufferedInputStream);
		m_decoder = new Decoder(null);
		// m_equalizer = new Equalizer();
		// m_equalizer_values = new float[32];
		// for (int b=0;b<m_equalizer.getBandCount();b++)
		// {
		// m_equalizer_values[b] = m_equalizer.getBand(b);
		// }
		// m_decoder.setEqualizer(m_equalizer);
		m_oBuffer = new DMAISObuffer(outputFormat.getChannels());
		m_decoder.setOutputBuffer(m_oBuffer);
		try
		{
			m_header = m_bitstream.readFrame();
			if((m_header != null) && (frameslength == -1) && (byteslength > 0))
				frameslength = m_header.max_number_of_frames((int) byteslength);
		} catch (BitstreamException e)
		{

			byteslength = -1;
		}
	}

	public void execute()// if( reverseBytes )
	// reverseBytes( smallBuffer, 0, bytesRead );
	{

		try
		{
			// Following line hangs when FrameSize is available in AudioFormat.
			Header header = null;
			if(m_header == null)
				header = m_bitstream.readFrame();
			else header = m_header;

			if(header == null)
			{

				getCircularBuffer().close();
				return;
			}
			currentFrame++;
			currentFramesize = header.calculate_framesize();
			currentByte = currentByte + currentFramesize;
			// Obuffer decoderOutput =
			m_decoder.decodeFrame(header, m_bitstream);
			m_bitstream.closeFrame();
			getCircularBuffer().write(m_oBuffer.getBuffer(), 0, m_oBuffer.getCurrentBufferSize());
			m_oBuffer.reset();
			if(m_header != null)
				m_header = null;
		} catch (BitstreamException e)
		{

		} catch (DecoderException e)
		{

		}

	}

	public long skip(long bytes)
	{
		if((byteslength > 0) && (frameslength > 0))
		{
			float ratio = bytes * 1.0f / byteslength * 1.0f;
			long bytesread = skipFrames((long) (ratio * frameslength));
			currentByte = currentByte + bytesread;
			m_header = null;
			return bytesread;
		} else return -1;
	}

	/**
	 * Skip frames. You don't need to call it severals times, it will exactly
	 * skip given frames number.
	 * 
	 * @param frames
	 * @return bytes length skipped matching to frames skipped.
	 */
	public long skipFrames(long frames)
	{

		int framesRead = 0;
		int bytesReads = 0;
		try
		{
			for(int i = 0; i < frames; i++)
			{
				Header header = m_bitstream.readFrame();
				if(header != null)
				{
					int fsize = header.calculate_framesize();
					bytesReads = bytesReads + fsize;
				}
				m_bitstream.closeFrame();
				framesRead++;
			}
		} catch (BitstreamException e)
		{

		}

		currentFrame = currentFrame + framesRead;
		return bytesReads;
	}

	private boolean isBigEndian()
	{
		return getFormat().isBigEndian();
	}

	public void close() throws IOException
	{
		super.close();
		m_encodedStream.close();
	}

	private class DMAISObuffer extends Obuffer
	{
		private int m_nChannels;
		private byte[] m_abBuffer;
		private int[] m_anBufferPointers;
		private boolean m_bIsBigEndian;

		public DMAISObuffer(int nChannels)
		{
			m_nChannels = nChannels;
			m_abBuffer = new byte[OBUFFERSIZE * nChannels];
			m_anBufferPointers = new int[nChannels];
			reset();
			m_bIsBigEndian = DecodedMpegAudioInputStream.this.isBigEndian();
		}

		public void append(int nChannel, short sValue)
		{
			byte bFirstByte;
			byte bSecondByte;
			if(m_bIsBigEndian)
			{
				bFirstByte = (byte) ((sValue >>> 8) & 0xFF);
				bSecondByte = (byte) (sValue & 0xFF);
			} else
			// little endian
			{
				bFirstByte = (byte) (sValue & 0xFF);
				bSecondByte = (byte) ((sValue >>> 8) & 0xFF);
			}
			m_abBuffer[m_anBufferPointers[nChannel]] = bFirstByte;
			m_abBuffer[m_anBufferPointers[nChannel] + 1] = bSecondByte;
			m_anBufferPointers[nChannel] += m_nChannels * 2;
		}

		public void set_stop_flag()
		{
		}

		public void close()
		{
		}

		public void write_buffer(int nValue)
		{
		}

		public void clear_buffer()
		{
		}

		public byte[] getBuffer()
		{
			return m_abBuffer;
		}

		public int getCurrentBufferSize()
		{
			return m_anBufferPointers[0];
		}

		public void reset()
		{
			for(int i = 0; i < m_nChannels; i++)
			{
				/*
				 * Points to byte location, implicitely assuming 16 bit samples.
				 */
				m_anBufferPointers[i] = i * 2;
			}
		}
	}
}
