package com.github.nradov.abnffuzzer;

import java.util.Locale;

/**
 * DIGIT / "A" / "B" / "C" / "D" / "E" / "F".
 *
 * @author Nick Radov
 */
final class Hexdig extends SingleByte {

	private static final byte[][] BYTES = new byte[0x10][1];
	static {
		for (byte b = 0; b < (byte) BYTES.length; b++) {
			BYTES[(int) b] = new byte[] { (byte) Integer.toString(b, BYTES.length).toUpperCase(Locale.US).charAt(0) };
		}
	}

	Hexdig() {
		super(BYTES);
	}

}
