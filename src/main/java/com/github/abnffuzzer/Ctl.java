package com.github.abnffuzzer;

import java.util.Random;
import java.util.Set;

/**
 * %x00-1F / %x7F. Controls.
 *
 * @author Nick Radov
 */
final class Ctl extends Rule {

    private static final byte[] DEL = new byte[] { '\u007F' };

    @Override
    public byte[] generate(final Fuzzer f, final Random r,
            final Set<String> exclude) {
        if (r.nextBoolean()) {
            return new byte[] { (byte) (r.nextInt(0x1F) + 1) };
        } else {
            return DEL;
        }
    }

}
