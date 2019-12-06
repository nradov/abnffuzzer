package com.github.nradov.abnffuzzer;

/**
 * ALPHA = %x41-5A / %x61-7A. A-Z / a-z.
 *
 * @author Nick Radov
 */
final class Alpha extends SingleByte {

	private static final byte[][] CHARS = new byte[52][1];

	static {
		for (byte b = (byte) 'A'; b <= (byte) 'Z'; b++) {
			CHARS[b - 'A'] = new byte[] { b };
		}
		for (byte b = (byte) 'a'; b <= (byte) 'z'; b++) {
			CHARS[b - 'a' + 26] = new byte[] { b };
		}
	}

	Alpha() {
		super(CHARS);
	}

}
