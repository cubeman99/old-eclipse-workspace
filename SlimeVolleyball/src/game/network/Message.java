package game.network;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class Message {
	private ByteBuffer buffer;
	private int type;
	
	// ================== CONSTRUCTORS ================== //

	public Message(int type) {
		this(type, 0);
	}
	
	public Message(int type, int initialSize) {
		this.type = type;
		buffer    = ByteBuffer.allocate(8 + initialSize);
		buffer.putInt(8);
		buffer.putInt(type);
	}
    
	public Message(byte[] data) {
		buffer   = ByteBuffer.wrap(data);
		buffer.order(ByteOrder.BIG_ENDIAN);
		type     = buffer.getInt(4);
		buffer.position(8);
	}
	
	public Message(byte[] data, int size) {
		buffer = ByteBuffer.wrap(data);
		buffer.order(ByteOrder.BIG_ENDIAN);
		type = buffer.getInt(4);
		buffer.putInt(0, size);
		buffer.position(8);
	}
	
	public Message(int type, Object... arguments) {
	}
	
	

	// =================== ACCESSORS =================== //
	
	public int getSize() {
		return buffer.getInt(0);
	}
	
	public int getType() {
		return type;
	}
	
	public int getBufferCapacity() {
		return buffer.capacity();
	}
	
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	public byte[] getRawData() {
		return buffer.array();
	}
	
	public boolean hasNext() {
		return buffer.hasRemaining();
	}
	
	public boolean readBoolean() {
		return (buffer.get() == (byte) 1);
	}
	
	public byte readByte() {
		return buffer.get();
	}
	
	public short readShort() {
		return buffer.getShort();
	}
	
	public int readInt() {
		return buffer.getInt();
	}
	
	public long readLong() {
		return buffer.getLong();
	}
	
	public float readFloat() {
		return buffer.getFloat();
	}
	
	public double readDouble() {
		return buffer.getDouble();
	}
	
	public char readChar() {
		return buffer.getChar();
	}
	
	public String readString() {
		int length = buffer.getInt();
		String str = "";
		for (int i = 0; i < length; i++)
			str += buffer.getChar();
		return str;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void writeBoolean(boolean value) {
		allocateSpace(1);
		buffer.put((byte) (value ? 1 : 0));
	}
	
	public void writeByte(byte value) {
		allocateSpace(1);
		buffer.put(value);
	}
	
	public void writeShort(short value) {
		allocateSpace(2);
		buffer.putShort(value);
	}
	
	public void writeInt(int value) {
		allocateSpace(4);
		buffer.putInt(value);
	}
	
	public void writeLong(long value) {
		allocateSpace(8);
		buffer.putLong(value);
	}
	
	public void writeFloat(float value) {
		allocateSpace(4);
		buffer.putFloat(value);
	}
	
	public void writeDouble(double value) {
		allocateSpace(8);
		buffer.putDouble(value);
	}
	
	public void writeChar(char c) {
		allocateSpace(2);
		buffer.putChar(c);
	}
	
	public void writeString(String str) {
		allocateSpace((str.length() * 2) + 4);
		buffer.putInt(str.length());
		for (int i = 0; i < str.length(); i++)
			buffer.putChar(str.charAt(i));
	}
	
	private void allocateSpace(int amount) {
		int pos     = buffer.position();
		int space   = buffer.capacity() - buffer.position();
		int newSize = buffer.getInt(0) + amount;
		buffer.putInt(0, newSize);
		
		if (amount > space) {
			// Allocate more space.
			int cap = buffer.capacity() + amount - space;
			ByteBuffer newBuffer = ByteBuffer.allocate(cap);
			newBuffer.put(buffer.array());
			buffer = newBuffer;
			buffer.position(pos);
		}
	}

	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public String toString() {
		String str = "[size=" + buffer.getInt(0) + ", type=" + type + ", data=";
		int pos = buffer.position();
		buffer.position(8);
		while (buffer.hasRemaining())
			str += buffer.getChar();
		buffer.position(pos);
		str += "]";
		return str;
	}
}
