package com.github.abnffuzzer;

import com.github.abnffuzzer.antlr4.AbnfParser.ConcatenationContext;

/**
 * Concatenation: Rule1 Rule2.
 *
 * @author Nick Radov
 * @see <a href="https://tools.ietf.org/html/rfc5234#section-3.1" target="_">
 *      IETF RFC 5234: 3.1. Concatenation: Rule1 Rule2</a>
 */
class Concatenation extends Element {

    /**
     * Create a new {@code Concatenation} from an ANTLR context.
     * 
     * @param elements
     *            ANTLR context
     */
    public Concatenation(final ConcatenationContext elements) {
        super(elements);
    }

}
