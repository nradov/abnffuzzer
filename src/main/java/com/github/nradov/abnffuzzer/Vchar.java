package com.github.nradov.abnffuzzer;

/**
 * %x21-7E. Visible (printing) characters.
 *
 * @author Nick Radov
 */
final class Vchar extends SingleByte {

	private static final char FIRST_VISIBLE_CHAR = '!';
	private static final char LAST_VISIBLE_CHAR = '~';

	private static final byte[][] BYTES = new byte[LAST_VISIBLE_CHAR - FIRST_VISIBLE_CHAR + 1][1];
	static {
		for (byte i = 0; i < (byte) BYTES.length; i++) {
			BYTES[i] = new byte[] { (byte) (i + FIRST_VISIBLE_CHAR) };
		}
	}

	Vchar() {
		super(BYTES);
	}

}
