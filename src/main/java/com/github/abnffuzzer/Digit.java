package com.github.abnffuzzer;

import java.util.Random;
import java.util.Set;

/**
 * %x30-39. 0-9.
 *
 * @author Nick Radov
 */
final class Digit extends Rule {

    @Override
    public byte[] generate(final Fuzzer f, final Random r,
            final Set<String> exclude) {
        return new byte[] { (byte) (r.nextInt(10) + 0x30) };
    }

}
