package com.github.abnffuzzer.core;

import java.util.Random;
import java.util.Set;

import com.github.abnffuzzer.Fuzzer;
import com.github.abnffuzzer.Rule;

/**
 * SP / HTAB. White space.
 *
 * @author Nick Radov
 */
public final class Wsp extends Rule {

    private static final byte[] SP = new byte[] { ' ' };
    private static final byte[] HTAB = new byte[] { '\t' };

    @Override
    public byte[] generate(final Fuzzer f, final Random r,
            final Set<String> exclude) {
        if (r.nextBoolean()) {
            return SP;
        } else {
            return HTAB;
        }
    }

}
