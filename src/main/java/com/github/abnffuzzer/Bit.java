package com.github.abnffuzzer;

import java.util.Random;
import java.util.Set;

/**
 * BIT = "0" / "1".
 *
 * @author Nick Radov
 */
final class Bit extends Rule {

    private static final byte[] ZERO = new byte[] { (byte) '0' };
    private static final byte[] ONE = new byte[] { (byte) '1' };

    @Override
    public byte[] generate(final Fuzzer f, final Random r,
            final Set<String> exclude) {
        if (r.nextBoolean()) {
            return ZERO;
        } else {
            return ONE;
        }
    }

}
