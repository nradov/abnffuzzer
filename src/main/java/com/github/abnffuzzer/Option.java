package com.github.abnffuzzer;

import java.util.Random;
import java.util.Set;

import com.github.abnffuzzer.AbnfParser.OptionContext;

public class Option extends Element {

    public Option(final OptionContext elements) {
        super(elements);
    }

    private static final byte[] EMPTY = new byte[0];

    @Override
    public byte[] generate(final Fuzzer f, final Random r,
            final Set<String> exclude) {
        if (elements.size() != 1) {
            throw new IllegalStateException("option should contain 1 element");
        }
        if (exclude.contains(elements.get(0).toString())) {
            return EMPTY;
        }
        if (r.nextBoolean()) {
            return elements.get(0).generate(f, r, exclude);
        }
        return EMPTY;
    }

    @Override
    public String toString() {
        return "[" + super.toString() + "]";
    }

}
