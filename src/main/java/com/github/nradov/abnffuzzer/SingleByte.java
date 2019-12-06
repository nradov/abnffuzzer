package com.github.nradov.abnffuzzer;

import java.util.Random;
import java.util.Set;

/**
 * Superclass for rules that randomly generate a single byte out of a defined
 * set of possible values.
 * 
 * @author Nick Radov
 */
abstract class SingleByte extends Rule {

	private final byte[][] bytes;

	SingleByte(final byte[][] bytes) {
		this.bytes = bytes;
	}
	
    @Override
    public byte[] generate(final Fuzzer f, final Random r,
            final Set<String> exclude) {
        return bytes[r.nextInt(bytes.length)];
    }
    
}
