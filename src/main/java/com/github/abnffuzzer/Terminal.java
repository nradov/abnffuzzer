package com.github.abnffuzzer;

import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

public class Terminal extends Element {

    private final String value;

    private static enum Type {
        Characters, LiteralText, RuleName;
    }

    private final Type type;

    private static final Pattern RULE_NAME_PATTERN = Pattern
            .compile("\\A[a-z][a-z\\d\\-]*\\z", Pattern.CASE_INSENSITIVE);

    private static enum Radix {
        /** Binary. */
        b(2),
        /** Decimal. */
        d(10),
        /** Hexadecimal. */
        x(16);

        Radix(final int value) {
            this.value = value;
        }

        final int value;
    }

    private Radix radix;

    public Terminal(final org.antlr.v4.runtime.tree.TerminalNode node) {
        final String nodeString = node.toString();
        if (nodeString.length() >= 3 && nodeString.startsWith("%")) {
            type = Type.Characters;
            radix = Radix.valueOf(nodeString.substring(1, 2));
            value = nodeString.substring(2);
        } else if (nodeString.startsWith("\"") && nodeString.endsWith("\"")) {
            type = Type.LiteralText;
            value = nodeString.substring(1, nodeString.length() - 1);
        } else if (nodeString.startsWith("<") && nodeString.endsWith(">")) {
            type = Type.RuleName;
            value = nodeString.substring(1, nodeString.length() - 1);
        } else if (RULE_NAME_PATTERN.matcher(nodeString).matches()) {
            type = Type.RuleName;
            value = nodeString;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public byte[] generate(final Fuzzer f, final Random r,
            final Set<String> exclude) {
        switch (type) {
        case Characters:
            final int dashIndex = value.indexOf('-');
            if (dashIndex == -1) {
                final int dotIndex = value.indexOf('.');
                if (dotIndex == -1) {
                    // character
                    return new byte[] {
                            (byte) Integer.parseInt(value, radix.value) };
                } else {
                    throw new UnsupportedOperationException(
                            "not implemented yet");
                }
            } else {
                // value range alternatives
                final int min = Integer.parseUnsignedInt(
                        value.substring(0, dashIndex), radix.value);
                final int max = Integer.parseUnsignedInt(
                        value.substring(dashIndex + 1), radix.value);
                return new byte[] { (byte) (r.nextInt(max - min + 1) + min) };
            }
        case LiteralText:
            // literal text string (without the quotes)
            final byte[] bytes = new byte[value.length()];
            for (int i = 0; i < value.length(); i++) {
                bytes[i] = (byte) value.charAt(i);
            }
            return bytes;
        case RuleName:
            return f.getRule(value).generate(f, r, exclude);
        default:
            throw new IllegalStateException();
        }
    }

    @Override
    public String toString() {
        switch (type) {
        case Characters:
            final StringBuilder sb = new StringBuilder();
            sb.append('%');
            sb.append(radix.toString());
            sb.append(value);
            return sb.toString();
        case LiteralText:
            return "\"" + value + "\"";
        case RuleName:
            return value;
        default:
            throw new IllegalStateException();
        }
    }

}
