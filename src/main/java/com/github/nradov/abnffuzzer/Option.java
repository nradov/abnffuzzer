package com.github.nradov.abnffuzzer;

import java.io.IOException;
import java.util.Random;
import java.util.Set;

import com.github.nradov.abnffuzzer.antlr4.AbnfParser.OptionContext;

/**
 * Optional Sequence: [RULE].
 *
 * @author Nick Radov
 * @see <a href="https://tools.ietf.org/html/rfc5234#section-3.8" target="_">
 *      IETF RFC 5234: 3.8. Optional Sequence: [RULE]</a>
 */
class Option extends Element {

    /**
     * Create a new {@code Option} from an ANTLR context.
     *
     * @param elements
     *            ANTLR context
     */
    public Option(final OptionContext elements) {
        super(elements);
    }

    private static final byte[] EMPTY = new byte[0];

    @Override
    public byte[] generate(final Fuzzer f, final Random r,
            final Set<String> exclude) throws IOException {
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
