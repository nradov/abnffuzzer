package com.github.nradov.abnffuzzer;

/**
 * %x00-FF. 8 bits of data.
 *
 * @author Nick Radov
 */
final class Octet extends SingleByte {

	private static final byte[][] BYTES = new byte[0x100][1];
	static {
		for (int i = 0; i < BYTES.length; i++) {
			BYTES[i] = new byte[] { (byte) i };
		}
	}

	Octet() {
		super(BYTES);
	}

}
