package com.github.abnffuzzer.core;

import java.util.Random;
import java.util.Set;

import com.github.abnffuzzer.Fuzzer;
import com.github.abnffuzzer.Rule;

/**
 * %x30-39. 0-9.
 *
 * @author Nick Radov
 */
public final class Digit extends Rule {

    @Override
    public byte[] generate(final Fuzzer f, final Random r,
            final Set<String> exclude) {
        return new byte[] { (byte) (r.nextInt(10) + 0x30) };
    }

}
