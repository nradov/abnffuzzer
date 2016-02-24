package com.github.abnffuzzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.github.abnffuzzer.antlr4.AbnfParser.AlternationContext;
import com.github.abnffuzzer.antlr4.AbnfParser.ConcatenationContext;
import com.github.abnffuzzer.antlr4.AbnfParser.ElementContext;
import com.github.abnffuzzer.antlr4.AbnfParser.GroupContext;
import com.github.abnffuzzer.antlr4.AbnfParser.OptionContext;
import com.github.abnffuzzer.antlr4.AbnfParser.RepeatContext;
import com.github.abnffuzzer.antlr4.AbnfParser.RepetitionContext;

/**
 * Base class for all elements in an ABNF rule.
 *
 * @author Nick Radov
 */
class Element {

    final List<Element> elements = new ArrayList<Element>();

    Element() {
    }

    Element(final ParseTree elements) {
        for (int i = 0; i < elements.getChildCount(); i++) {
            final ParseTree child = elements.getChild(i);
            if (child instanceof AlternationContext) {
                this.elements.add(new Alternation((AlternationContext) child));
            } else if (child instanceof ConcatenationContext) {
                this.elements
                        .add(new Concatenation((ConcatenationContext) child));
            } else if (child instanceof ConcatenationContext) {
                this.elements
                        .add(new Concatenation((ConcatenationContext) child));
            } else if (child instanceof ElementContext) {
                this.elements.add(new Element(child));
            } else if (child instanceof GroupContext) {
                this.elements.add(new Group((GroupContext) child));
            } else if (child instanceof OptionContext) {
                this.elements.add(new Option((OptionContext) child));
            } else if (child instanceof RepeatContext) {
                this.elements.add(new Repeat((RepeatContext) child));
            } else if (child instanceof RepetitionContext) {
                this.elements.add(new Repetition((RepetitionContext) child));
            } else if (child instanceof TerminalNode) {
                final TerminalNode tn = (TerminalNode) child;
                final String value = tn.toString();
                // we don't want to store nodes for separators
                if (!(value.startsWith("[") || value.startsWith("]")
                        || value.startsWith("(") || value.startsWith(")")
                        || value.startsWith("/") || value.startsWith("*"))) {
                    this.elements.add(new com.github.abnffuzzer.Terminal(tn));
                }
            } else {
                throw new IllegalArgumentException(
                        "illegal child " + i + " type: " + child.getClass());
            }
        }
    }

    /**
     * Concatenate a list of multiple byte arrays into a single byte array.
     *
     * @param content
     *            zero or more byte arrays
     * @return a single byte array containing all of the parameter byte arrays
     *         in order
     */
    public static byte[] concatenate(final List<byte[]> content) {
        if (content.size() == 1) {
            return content.get(0);
        }

        // first pass to calculate the total array size
        int size = 0;
        for (final byte[] b : content) {
            size += b.length;
        }
        // second pass to copy all the pieces into a single array
        final byte[] generated = new byte[size];
        int destPos = 0;
        for (final byte[] b : content) {
            System.arraycopy(b, 0, generated, destPos, b.length);
            destPos += b.length;
        }

        return generated;
    }

    public byte[] generate(final Fuzzer f, final Random r,
            final Set<String> exclude) {
        final List<byte[]> childContent = new ArrayList<>(elements.size());
        for (final Element e : elements) {
            childContent.add(e.generate(f, r, exclude));
        }
        return concatenate(childContent);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (final Element e : elements) {
            sb.append(e).append(' ');
        }
        return sb.toString().trim();
    }

}
