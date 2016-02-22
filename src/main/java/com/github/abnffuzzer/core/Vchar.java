package com.github.abnffuzzer.core;

import java.util.Random;
import java.util.Set;

import com.github.abnffuzzer.Fuzzer;
import com.github.abnffuzzer.Rule;

/**
 * %x21-7E. Visible (printing) characters.
 *
 * @author Nick Radov
 */
public final class Vchar extends Rule {

    @Override
    public byte[] generate(final Fuzzer f, final Random r,
            final Set<String> exclude) {
        return new byte[] { (byte) (r.nextInt('~' - '!' + 1) + '!') };
    }

}
