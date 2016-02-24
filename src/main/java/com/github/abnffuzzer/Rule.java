package com.github.abnffuzzer;

import com.github.abnffuzzer.antlr4.AbnfParser.ElementsContext;

/**
 * Rule definition.
 *
 * @author Nick Radov
 * @see <a href="https://tools.ietf.org/html/rfc5234#section-2" target="_"> IETF
 *      RFC 5234: 2. Rule Definition</a>
 */
class Rule extends Element {

    /**
     * Create a new {@code Rule}.
     */
    protected Rule() {

    }

    /**
     * Create a new rule from an ANTLR context.
     *
     * @param elements
     *            ANTLR context
     */
    public Rule(final ElementsContext elements) {
        super(elements);
    }

}
