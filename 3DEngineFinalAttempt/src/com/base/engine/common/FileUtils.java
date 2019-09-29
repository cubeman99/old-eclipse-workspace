package com.base.engine.common;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;

public class FileUtils {
	public static final boolean LITTLE_ENDIAN = false;
	public static final boolean BIG_ENDIAN    = true;
	
	

	public static ByteBuffer readBytesIntoBuffer(ByteBuffer buffer, int length) {
		ByteBuffer result = BufferUtils.createByteBuffer(length);
		result.put(readBytes(buffer, length));
		return result;
	}
	
	public static byte[] readBytes(ByteBuffer buffer, int length) {
		byte[] data = new byte[length];
		buffer.get(data);
		return data;
	}
	
	public static int readInt(ByteBuffer buffer) {
		return buffer.getInt();
	}
	
	public static int readLEInt(ByteBuffer buffer) {
		byte[] data = new byte[4];
		buffer.get(data);
		return (data[0] & 0xFF) | (data[1] & 0xFF) << 8 | (data[2] & 0xFF) << 16 | (data[3] & 0xFF) << 24;
	}
	
	public static short readShort(ByteBuffer buffer) {
		return buffer.getShort();
	}
	
	public static short readLEShort(ByteBuffer buffer) {
		byte[] data = new byte[2];
		buffer.get(data);
		return (short) ((data[0] & 0xFF) | (data[1] & 0xFF) << 8);
	}
	
	public static String readString(ByteBuffer buffer, int length) {
		return new String(readBytes(buffer, length));
	}
}
