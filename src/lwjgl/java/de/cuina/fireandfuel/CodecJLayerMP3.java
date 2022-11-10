package de.cuina.fireandfuel;

/*
 * CodecJLayerMP3 - an ICodec interface for Paulscode Sound System
 * Copyright (C) 2012 by fireandfuel from Cuina Team (http://www.cuina.byethost12.com/)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY
 * KIND, either express or implied. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, see http://www.gnu.org/licenses/lgpl.txt
 */

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.Obuffer;
import javazoom.mp3spi.DecodedMpegAudioInputStream;

import paulscode.sound.ICodec;
import paulscode.sound.SoundBuffer;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemLogger;

/**
 * The CodecJLayer class provides an ICodec interface to the external JLayer
 * library.
 * 
 * <b><br>
 * <br>
 * This software is based on or using the JLayer and mp3spi library from
 * http://www.javazoom.net/javalayer/javalayer.html and Tritonus library from
 * http://www.tritonus.org/.
 * 
 * JLayer, mp3spi and Tritonus library are released under the conditions of 
 * GNU Library General Public License version 2 or (at your option) 
 * any later version of the License.
 * </b><br>
 */

public class CodecJLayerMP3 implements ICodec
{
	/**
	 * Used to return a current value from one of the synchronized
	 * boolean-interface methods.
	 */
	private static final boolean GET = false;

	/**
	 * Used to set the value in one of the synchronized boolean-interface
	 * methods.
	 */
	private static final boolean SET = true;

	/**
	 * Used when a parameter for one of the synchronized boolean-interface
	 * methods is not applicable.
	 */
	private static final boolean XXX = false;

	/**
	 * True if there is no more data to read in.
	 */
	private boolean endOfStream = false;

	/**
	 * True if the stream has finished initializing.
	 */
	private boolean initialized = false;

	private Decoder decoder;
	private Bitstream bitstream;
	private DMAISObuffer buffer;

	private Header mainHeader;

	/**
	 * Audio format to use when playing back the wave data.
	 */
	private AudioFormat myAudioFormat = null;

	/**
	 * Input stream to use for reading in pcm data.
	 */
	private DecodedMpegAudioInputStream myAudioInputStream = null;

	/**
	 * Processes status messages, warnings, and error messages.
	 */
	private SoundSystemLogger logger;

	public CodecJLayerMP3()
	{
		logger = SoundSystemConfig.getLogger();
	}

	@Override
	public void reverseByteOrder(boolean b)
	{
	}

	@Override
	public boolean initialize(URL url)
	{
		initialized(SET, false);
		cleanup();
		if(url == null)
		{
			errorMessage("url null in method 'initialize'");
			cleanup();
			return false;
		}

		try
		{
			bitstream = new Bitstream(new BufferedInputStream(url.openStream()));
			decoder = new Decoder();

			mainHeader = bitstream.readFrame();

			buffer = new DMAISObuffer(2);
			decoder.setOutputBuffer(buffer);

			int channels;
			if(mainHeader.mode() < 3)
				channels = 2;
			else channels = 1;

			bitstream.closeFrame();
			bitstream.close();

			myAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
					mainHeader.frequency(), 16, channels, channels * 2, mainHeader.frequency(),
					false);

			AudioFormat mpegAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, -1.0f,
					16, channels, channels * 2, -1.0f, false);

			myAudioInputStream = new DecodedMpegAudioInputStream(myAudioFormat,
					new AudioInputStream(new BufferedInputStream(url.openStream()),
							mpegAudioFormat, -1));
			myAudioInputStream.skip((int)(myAudioInputStream.getFormat().getFrameRate() * 0.018f) * myAudioInputStream.getFormat().getFrameSize());
		} catch (Exception e)
		{
			errorMessage("Unable to set up input streams in method " + "'initialize'");
			printStackTrace(e);
			cleanup();
			return false;
		}

		if(myAudioInputStream == null)
		{
			errorMessage("Unable to set up audio input stream in method " + "'initialize'");
			cleanup();
			return false;
		}

		endOfStream(SET, false);
		initialized(SET, true);
		return true;
	}

	@Override
	public boolean initialized()
	{
		return initialized(GET, XXX);
	}

	@Override
	public SoundBuffer read()
	{
		if(myAudioInputStream == null)
		{
			endOfStream(SET, true);
			return null;
		}

		// Get the format for the audio data:
		AudioFormat audioFormat = myAudioInputStream.getFormat();

		// Check to make sure there is an audio format:
		if(audioFormat == null)
		{
			errorMessage("Audio Format null in method 'read'");
			endOfStream(SET, true);
			return null;
		}

		// Variables used when reading from the audio input stream:
		int bytesRead = 0, cnt = 0;

		// Allocate memory for the audio data:
		byte[] streamBuffer = new byte[SoundSystemConfig.getStreamingBufferSize()];

		try
		{
			// Read until buffer is full or end of stream is reached:
			while((!endOfStream(GET, XXX)) && (bytesRead < streamBuffer.length))
			{
				myAudioInputStream.execute();
				if((cnt = myAudioInputStream.read(streamBuffer, bytesRead, streamBuffer.length
						- bytesRead)) < 0)
				{
					endOfStream(SET, true);
					break;
				}
				// keep track of how many bytes were read:
				bytesRead += cnt;
			}
		} catch (IOException ioe)
		{

			/*
			 * errorMessage( "Exception thrown while reading from the " +
			 * "AudioInputStream (location #3)." ); printStackTrace( e ); return
			 * null;
			 */// TODO: Figure out why this exceptions is being thrown at end of
				// MP3 files!
			endOfStream(SET, true);
			return null;
		} catch (ArrayIndexOutOfBoundsException e)
		{
			//this exception is thrown at the end of the mp3's
			endOfStream(SET, true);
			return null;
        }

		// Return null if no data was read:
		if(bytesRead <= 0)
		{
			endOfStream(SET, true);
			return null;
		}

		// Insert the converted data into a ByteBuffer:
		// byte[] data = convertAudioBytes(streamBuffer,
		// audioFormat.getSampleSizeInBits() == 16);

		// Wrap the data into a SoundBuffer:
		SoundBuffer buffer = new SoundBuffer(streamBuffer, audioFormat);

		// Return the result:
		return buffer;
	}

	@Override
	public SoundBuffer readAll()
	{
		// Check to make sure there is an audio format:
		if(myAudioFormat == null)
		{
			errorMessage("Audio Format null in method 'readAll'");
			return null;
		}

		// Array to contain the audio data:
		byte[] fullBuffer = null;

		// Determine how much data will be read in:
		int fileSize = myAudioFormat.getChannels() * (int) myAudioInputStream.getFrameLength()
				* myAudioFormat.getSampleSizeInBits() / 8;
		if(fileSize > 0)
		{
			// Allocate memory for the audio data:
			fullBuffer = new byte[myAudioFormat.getChannels()
					* (int) myAudioInputStream.getFrameLength()
					* myAudioFormat.getSampleSizeInBits() / 8];
			int read = 0, total = 0;
			try
			{
				// Read until the end of the stream is reached:
				while((read = myAudioInputStream.read(fullBuffer, total, fullBuffer.length - total)) != -1
						&& total < fullBuffer.length)
				{
					total += read;
				}
			} catch (IOException e)
			{
				errorMessage("Exception thrown while reading from the "
						+ "AudioInputStream (location #1).");
				printStackTrace(e);
				return null;
			}
		} else
		{
			// Total file size unknown.

			// Variables used when reading from the audio input stream:
			int totalBytes = 0, bytesRead = 0, cnt = 0;
			byte[] smallBuffer = null;

			// Allocate memory for a chunk of data:
			smallBuffer = new byte[SoundSystemConfig.getFileChunkSize()];

			// Read until end of file or maximum file size is reached:
			while((!endOfStream(GET, XXX)) && (totalBytes < SoundSystemConfig.getMaxFileSize()))
			{
				bytesRead = 0;
				cnt = 0;

				try
				{
					// Read until small buffer is filled or end of file reached:
					while(bytesRead < smallBuffer.length)
					{
						myAudioInputStream.execute();
						if((cnt = myAudioInputStream.read(smallBuffer, bytesRead,
								smallBuffer.length - bytesRead)) < 0)
						{
							endOfStream(SET, true);
							break;
						}
						bytesRead += cnt;
					}
				} catch (IOException e)
				{
					errorMessage("Exception thrown while reading from the "
							+ "AudioInputStream (location #2).");
					printStackTrace(e);
					return null;
				}

				// Reverse byte order if necessary:
				// if( reverseBytes )
				// reverseBytes( smallBuffer, 0, bytesRead );

				// Keep track of the total number of bytes read:
				totalBytes += bytesRead;

				// Append the small buffer to the full buffer:
				fullBuffer = appendByteArrays(fullBuffer, smallBuffer, bytesRead);
			}
		}

		// Insert the converted data into a ByteBuffer
		// byte[] data = convertAudioBytes( fullBuffer,
		// myAudioFormat.getSampleSizeInBits() == 16 );

		// Wrap the data into an SoundBuffer:
		SoundBuffer soundBuffer = new SoundBuffer(fullBuffer, myAudioFormat);

		// Close the audio input stream
		try
		{
			myAudioInputStream.close();
		} catch (IOException e)
		{
		}

		// Return the result:
		return soundBuffer;
	}

	@Override
	public boolean endOfStream()
	{
		return endOfStream(GET, XXX);
	}

	@Override
	public void cleanup()
	{
		if(myAudioInputStream != null)
			try
			{
				myAudioInputStream.close();
			} catch (Exception e)
			{
			}
	}

	@Override
	public AudioFormat getAudioFormat()
	{
		return myAudioFormat;
	}

	/**
	 * Internal method for synchronizing access to the boolean 'initialized'.
	 * 
	 * @param action
	 *            GET or SET.
	 * @param value
	 *            New value if action == SET, or XXX if action == GET.
	 * @return True if steam is initialized.
	 */
	private synchronized boolean initialized(boolean action, boolean value)
	{
		if(action == SET)
			initialized = value;
		return initialized;
	}

	/**
	 * Internal method for synchronizing access to the boolean 'endOfStream'.
	 * 
	 * @param action
	 *            GET or SET.
	 * @param value
	 *            New value if action == SET, or XXX if action == GET.
	 * @return True if end of stream was reached.
	 */
	private synchronized boolean endOfStream(boolean action, boolean value)
	{
		if(action == SET)
			endOfStream = value;
		return endOfStream;
	}

	/**
	 * Reverse-orders all bytes contained in the specified array.
	 * 
	 * @param buffer
	 *            Array containing audio data.
	 */
	public static void reverseBytes(byte[] buffer)
	{
		reverseBytes(buffer, 0, buffer.length);
	}

	/**
	 * Reverse-orders the specified range of bytes contained in the specified
	 * array.
	 * 
	 * @param buffer
	 *            Array containing audio data.
	 * @param offset
	 *            Array index to begin.
	 * @param size
	 *            number of bytes to reverse-order.
	 */
	public static void reverseBytes(byte[] buffer, int offset, int size)
	{

		byte b;
		for(int i = offset; i < (offset + size); i += 2)
		{
			b = buffer[i];
			buffer[i] = buffer[i + 1];
			buffer[i + 1] = b;
		}
	}

	/**
	 * Prints an error message.
	 * 
	 * @param message
	 *            Message to print.
	 */
	private void errorMessage(String message)
	{
		logger.errorMessage("CodecJLayerMP3", message, 0);
	}

	/**
	 * Prints an exception's error message followed by the stack trace.
	 * 
	 * @param e
	 *            Exception containing the information to print.
	 */
	private void printStackTrace(Exception e)
	{
		logger.printStackTrace(e, 1);
	}

	/**
	 * Creates a new array with the second array appended to the end of the
	 * first array.
	 * 
	 * @param arrayOne
	 *            The first array.
	 * @param arrayTwo
	 *            The second array.
	 * @param length
	 *            How many bytes to append from the second array.
	 * @return Byte array containing information from both arrays.
	 */
	private static byte[] appendByteArrays(byte[] arrayOne, byte[] arrayTwo, int length)
	{
		byte[] newArray;
		if(arrayOne == null && arrayTwo == null)
		{
			// no data, just return
			return null;
		} else if(arrayOne == null)
		{
			// create the new array, same length as arrayTwo:
			newArray = new byte[length];
			// fill the new array with the contents of arrayTwo:
			System.arraycopy(arrayTwo, 0, newArray, 0, length);
			arrayTwo = null;
		} else if(arrayTwo == null)
		{
			// create the new array, same length as arrayOne:
			newArray = new byte[arrayOne.length];
			// fill the new array with the contents of arrayOne:
			System.arraycopy(arrayOne, 0, newArray, 0, arrayOne.length);
			arrayOne = null;
		} else
		{
			// create the new array large enough to hold both arrays:
			newArray = new byte[arrayOne.length + length];
			System.arraycopy(arrayOne, 0, newArray, 0, arrayOne.length);
			// fill the new array with the contents of both arrays:
			System.arraycopy(arrayTwo, 0, newArray, arrayOne.length, length);
			arrayOne = null;
			arrayTwo = null;
		}

		return newArray;
	}

	private static class DMAISObuffer extends Obuffer
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

		public void reset()
		{
			for(int i = 0; i < m_nChannels; i++)
			{
				/*
				 * Points to byte location, implicitly assuming 16 bit samples.
				 */
				m_anBufferPointers[i] = i * 2;
			}
		}
	}
}
