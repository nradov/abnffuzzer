package com.github.nradov.abnffuzzer;

import com.github.nradov.abnffuzzer.antlr4.AbnfParser.GroupContext;

/**
 * Sequence Group: (Rule1 Rule2).
 *
 * @author Nick Radov
 * @see <a href="https://tools.ietf.org/html/rfc5234#section-3.5" target="_">
 *      IETF RFC 5234: 3.5. Sequence Group: (Rule1 Rule2)</a>
 */
class Group extends Element {

    /**
     * Create a new {@code Group} from an ANTLR context.
     *
     * @param elements
     *            ANTLR context
     */
    public Group(final GroupContext elements) {
        super(elements);
    }

    @Override
    public String toString() {
        return "(" + super.toString() + ")";
    }

}
