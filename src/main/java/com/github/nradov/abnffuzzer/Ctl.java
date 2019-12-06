package com.github.nradov.abnffuzzer;

/**
 * %x00-1F / %x7F. Controls.
 *
 * @author Nick Radov
 */
final class Ctl extends SingleByte {

	private static final byte[][] BYTES = new byte['\u001F' + 1][1];
	static {
		for (byte i = '\u0000'; i <= '\u001F'; i++) {
			BYTES[i] = new byte[] {(byte) (i)};
		}
		BYTES[BYTES.length - 1] = new byte[] { '\u007F' };
	}

	Ctl() {
		super(BYTES);
	}
	
}
