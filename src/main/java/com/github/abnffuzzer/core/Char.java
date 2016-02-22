package com.github.abnffuzzer.core;

import java.util.Random;
import java.util.Set;

import com.github.abnffuzzer.Fuzzer;
import com.github.abnffuzzer.Rule;

/**
 * CHAR = %x01-7F. Any 7-bit US-ASCII character, excluding NUL.
 *
 * @author Nick Radov
 */
public final class Char extends Rule {

    @Override
    public byte[] generate(final Fuzzer f, final Random r,
            final Set<String> exclude) {
        return new byte[] { (byte) (r.nextInt(0x7F) + 1) };
    }

}
