package com.github.nradov.abnffuzzer;

/**
 * CHAR = %x01-7F. Any 7-bit US-ASCII character, excluding NUL.
 *
 * @author Nick Radov
 */
final class Char extends SingleByte {

	private static final byte[][] BYTES = new byte['\u007F' - '\u0001' + 1][1];
	static {
		for (byte i = 0; i < (byte) BYTES.length; i++) {
			BYTES[i] = new byte[] {(byte) (i + '\u0001')};
		}
	}

	Char() {
		super(BYTES);
	}
	
}
