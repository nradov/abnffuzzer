package com.github.nradov.abnffuzzer;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

class Terminal extends Element {

	private final String value;

	private enum Type {
		Characters, LiteralText, CaseSensitive, CaseInsensitive, RuleName, ProseVal;
	}

	private final Type type;

	private static final Pattern RULE_NAME_PATTERN = Pattern.compile("\\A[a-z][a-z\\d\\-]*\\z",
			Pattern.CASE_INSENSITIVE);

	private enum Radix {
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
		if (nodeString.length() >= 4 && nodeString.startsWith("%s\"") && nodeString.endsWith("\"")) {
			// RFC 7405: case sensitive
			type = Type.CaseSensitive;
			value = nodeString.substring(3, nodeString.length() - 1);
		} else if (nodeString.length() >= 4 && nodeString.startsWith("%i\"") && nodeString.endsWith("\"")) {
			// RFC 7405: case insensitive
			type = Type.CaseInsensitive;
			value = nodeString.substring(3, nodeString.length() - 1);
		} else if (nodeString.length() >= 3 && nodeString.startsWith("%")) {
			type = Type.Characters;
			radix = Radix.valueOf(nodeString.substring(1, 2));
			value = nodeString.substring(2);
		} else if (nodeString.startsWith("\"") && nodeString.endsWith("\"")) {
			type = Type.LiteralText;
			value = nodeString.substring(1, nodeString.length() - 1);
		} else if (nodeString.startsWith("<") && nodeString.endsWith(">")) {
			value = nodeString.substring(1, nodeString.length() - 1).trim();
			if (RULE_NAME_PATTERN.matcher(nodeString).matches()) {
				type = Type.RuleName;
			} else {
				// this isn't a real rule name
				type = Type.ProseVal;
			}
		} else if (RULE_NAME_PATTERN.matcher(nodeString).matches()) {
			type = Type.RuleName;
			value = nodeString;
		} else {
			throw new IllegalArgumentException("Illegal terminal node: " + nodeString);
		}
	}

	private static final byte[] EMPTY = new byte[0];

	@Override
	public byte[] generate(final Fuzzer f, final Random r, final Set<String> excluded) throws IOException {
		switch (type) {
		case Characters:
			final int dashIndex = value.indexOf('-');
			if (dashIndex == -1) {
				final String[] split = value.split("\\.");
				final byte[] result = new byte[split.length];
				for (int i = 0; i < split.length; i++) {
					result[i] = (byte) Integer.parseInt(split[i], radix.value);
				}
				return result;
			} else {
				// value range alternatives
				final int min = Integer.parseUnsignedInt(value.substring(0, dashIndex), radix.value);
				final int max = Integer.parseUnsignedInt(value.substring(dashIndex + 1), radix.value);
				return new byte[] { (byte) (r.nextInt(max - min + 1) + min) };
			}
		case LiteralText:
		case CaseSensitive:
			// literal text string (without the quotes)
			final byte[] literalBytes = new byte[value.length()];
			for (int i = 0; i < value.length(); i++) {
				literalBytes[i] = (byte) value.charAt(i);
			}
			return literalBytes;
		case CaseInsensitive:
			final byte[] caseInsensitiveBytes = new byte[value.length()];
			for (int i = 0; i < value.length(); i++) {
				final char c;
				if (r.nextBoolean()) {
					c = Character.toUpperCase(value.charAt(i));
				} else {
					c = Character.toLowerCase(value.charAt(i));
				}
				caseInsensitiveBytes[i] = (byte) c;
			}
			return caseInsensitiveBytes;
		case RuleName:
			List<Rule> rules = f.getRule(value);
			return rules.get(r.nextInt(rules.size())).generate(f, r, excluded);
		case ProseVal:
			// we can't generate anything for prose
			return EMPTY;
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
		case CaseSensitive:
			return "%s\"" + value + "\"";
		case CaseInsensitive:
			return "%i\"" + value + "\"";
		case RuleName:
			return value;
		case ProseVal:
			return "< " + value + " >";
		default:
			throw new IllegalStateException();
		}
	}

}
