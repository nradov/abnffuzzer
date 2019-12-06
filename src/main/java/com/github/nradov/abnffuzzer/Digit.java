package com.github.nradov.abnffuzzer;

/**
 * %x30-39. 0-9.
 *
 * @author Nick Radov
 */
final class Digit extends SingleByte {
	
	private static final byte[][] BYTES = new byte[10][1];
	static {
		for (byte i = 0; i < (byte) BYTES.length; i++) {
			BYTES[i] = new byte[] {(byte) (i + '0')};
		}
	}

	Digit() {
		super(BYTES);
	}
	
}
