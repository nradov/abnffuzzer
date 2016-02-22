package com.github.abnffuzzer.core;

import java.util.Random;
import java.util.Set;

import com.github.abnffuzzer.Fuzzer;
import com.github.abnffuzzer.Rule;

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
