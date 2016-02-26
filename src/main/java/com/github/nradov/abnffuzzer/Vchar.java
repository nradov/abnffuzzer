package com.github.nradov.abnffuzzer;

import java.util.Random;
import java.util.Set;

/**
 * %x21-7E. Visible (printing) characters.
 *
 * @author Nick Radov
 */
final class Vchar extends Rule {

    @Override
    public byte[] generate(final Fuzzer f, final Random r,
            final Set<String> exclude) {
        return new byte[] { (byte) (r.nextInt('~' - '!' + 1) + '!') };
    }

}
