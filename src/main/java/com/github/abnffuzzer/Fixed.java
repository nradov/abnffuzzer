package com.github.abnffuzzer;

import java.util.Random;
import java.util.Set;

abstract class Fixed extends Rule {

    private final byte[] value;

    Fixed(final byte[] value) {
        this.value = value;
    }

    @Override
    public byte[] generate(final Fuzzer f, final Random r,
            final Set<String> exclude) {
        return value;
    }

}
