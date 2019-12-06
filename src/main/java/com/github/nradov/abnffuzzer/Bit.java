package com.github.nradov.abnffuzzer;

/**
 * BIT = "0" / "1".
 *
 * @author Nick Radov
 */
final class Bit extends SingleByte {

	private static final byte[][] BYTES = new byte[][] { new byte[] { (byte) '0' }, new byte[] { (byte) '1' } };

	Bit() {
		super(BYTES);
	}

}
