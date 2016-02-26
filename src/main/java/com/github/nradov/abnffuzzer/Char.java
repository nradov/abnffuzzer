package com.github.nradov.abnffuzzer;

import java.util.Random;
import java.util.Set;

/**
 * CHAR = %x01-7F. Any 7-bit US-ASCII character, excluding NUL.
 *
 * @author Nick Radov
 */
final class Char extends Rule {

    @Override
    public byte[] generate(final Fuzzer f, final Random r,
            final Set<String> exclude) {
        return new byte[] { (byte) (r.nextInt(0x7F) + 1) };
    }

}
