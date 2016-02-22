package com.github.abnffuzzer.core;

import java.util.Random;
import java.util.Set;

import com.github.abnffuzzer.Fuzzer;
import com.github.abnffuzzer.Rule;

/**
 * %x00-FF. 8 bits of data.
 *
 * @author Nick Radov
 */
public final class Octet extends Rule {

    @Override
    public byte[] generate(final Fuzzer f, final Random r,
            final Set<String> exclude) {
        final byte[] bytes = new byte[1];
        r.nextBytes(bytes);
        return bytes;
    }

}
