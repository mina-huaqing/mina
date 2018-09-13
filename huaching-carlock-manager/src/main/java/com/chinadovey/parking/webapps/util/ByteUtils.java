package com.chinadovey.parking.webapps.util;

public class ByteUtils {
	
	public static void main(String[] args){
	    byte[] b1 = new byte[13];
		ByteUtils.writeByte(1, b1, 2);
		System.err.println(b1[2]);
		
		
		
		long v = 65537l;
		byte b = (byte)v;
		System.out.println(b);
		byte[] data = writeLong6(v, new byte[6], 0);
		for(int i=0 ; i < data.length ; i++){
			System.out.println(data[i]);
		}
		
	}

	public static byte[] writeLong8(long v, byte[] b, int offset) {
		b[offset + 7] = (byte) v;
		b[offset + 6] = (byte) (v >> 8);
		b[offset + 5] = (byte) (v >> 16);
		b[offset + 4] = (byte) (v >> 24);
		b[offset + 3] = (byte) (v >> 32);
		b[offset + 2] = (byte) (v >> 40);
		b[offset + 1] = (byte) (v >> 48);
		b[offset] = (byte) (v >> 56);
		return b;
	}

	public static byte[] writeLong6(long v, byte[] b, int offset) {
		b[offset + 5] = (byte) v;
		b[offset + 4] = (byte) (v >> 8);
		b[offset + 3] = (byte) (v >> 16);
		b[offset + 2] = (byte) (v >> 24);
		b[offset + 1] = (byte) (v >> 32);
		b[offset] = (byte) (v >> 40);
		return b;
	}

	public static byte[] writeInt4(int v, byte[] b, int offset) {
		b[offset + 3] = (byte) v;
		b[offset + 2] = (byte) (v >> 8);
		b[offset + 1] = (byte) (v >> 16);
		b[offset] = (byte) (v >> 24);
		return b;
	}

	public static byte[] writeInt4(int v, int offset) {
		return writeInt4(v, new byte[4], offset);
	}

	public static byte[] writeInt4(String hex, int offset) {
		return writeInt4(Integer.parseInt(hex, 16), offset);
	}

	public static byte[] writeInt2(int v, byte[] b, int offset) {
		b[offset + 1] = (byte) v;
		b[offset] = (byte) (v >> 8);
		return b;
	}

	public static byte[] writeInt2(int v, int offset) {
		return writeInt2(v, new byte[2], offset);
	}

	public static byte[] writeInt2(String hex, int offset) {
		return writeInt2(Integer.parseInt(hex, 16), offset);
	}

	public static void writeByte(int v, byte[] b, int offset) {
		b[offset] = (byte) v;
	}

	public static int makeIntFromByte(byte[] b) {
		return makeIntFromByte(b, Math.min(4, b.length), 0);
	}

	public static int makeIntFromByte(byte[] b, int len) {
		return makeIntFromByte(b, len, 0);
	}

	public static int makeIntFromByte(byte[] b, int len, int offset) {
		int temp = 0;
		int res = 0;
		for (int i = 0; i < len; i++) {
			res <<= 8;
			temp = b[i + offset] & 0xff;
			res |= temp;
		}
		return res;
	}

	public static final long makeLongFromByte(byte[] b) {
		return makeLongFromByte(b, Math.min(8, b.length), 0);
	}

	public static final long makeLongFromByte(byte[] b, int len) {
		return makeLongFromByte(b, len, 0);
	}

	public static final long makeLongFromByte(byte[] b, int len, int offset) {
		long temp = 0;
		long res = 0;
		for (int i = 0; i < len; i++) {
			res <<= 8;
			temp = b[i + offset] & 0xff;
			res |= temp;
		}
		return res;
	}

	public static String asHex(byte bytes) {
		return asHex(new byte[] { bytes }, null);
	}

	public static String asHex(byte[] bytes) {
		return asHex(bytes, null);
	}

	public static String asHex(byte[] bytes, String separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String code = Integer.toHexString(bytes[i] & 0xFF);
			if ((bytes[i] & 0xFF) < 16) {
				sb.append('0');
			}

			sb.append(code);

			if (separator != null && i < bytes.length - 1) {
				sb.append(separator);
			}
		}

		return sb.toString();
	}
}
