package com.github.abnffuzzer;

import java.util.Random;
import java.util.Set;

/**
 * DIGIT / "A" / "B" / "C" / "D" / "E" / "F".
 *
 * @author Nick Radov
 */
final class Hexdig extends Rule {

    private static final byte[] HEXDIG_BYTES = new byte[] { '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    @Override
    public byte[] generate(final Fuzzer f, final Random r,
            final Set<String> exclude) {
        return new byte[] { HEXDIG_BYTES[r.nextInt(HEXDIG_BYTES.length)] };
    }

}
