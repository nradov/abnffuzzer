package com.github.nradov.abnffuzzer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.github.nradov.abnffuzzer.antlr4.AbnfParser.AlternationContext;
import com.github.nradov.abnffuzzer.antlr4.AbnfParser.ConcatenationContext;
import com.github.nradov.abnffuzzer.antlr4.AbnfParser.ElementContext;
import com.github.nradov.abnffuzzer.antlr4.AbnfParser.GroupContext;
import com.github.nradov.abnffuzzer.antlr4.AbnfParser.OptionContext;
import com.github.nradov.abnffuzzer.antlr4.AbnfParser.RepeatContext;
import com.github.nradov.abnffuzzer.antlr4.AbnfParser.RepetitionContext;

/**
 * Base class for all elements in an ABNF rule.
 *
 * @author Nick Radov
 */
class Element {

	final List<Element> elements = new ArrayList<>();

	Element() {
	}

	@SuppressWarnings("serial")
	private static final Map<Class<? extends ParserRuleContext>, ElementFactory> CONTEXT_CTOR = new HashMap<>() {
		{
			put(AlternationContext.class, Alternation::new);
			put(ConcatenationContext.class, Concatenation::new);
			put(ElementContext.class, Element::new);
			put(GroupContext.class, Group::new);
			put(OptionContext.class, Option::new);
			put(RepeatContext.class, Repeat::new);
			put(RepetitionContext.class, Repetition::new);
		}
	};

	Element(final ParseTree elements) {
		for (int i = 0; i < elements.getChildCount(); i++) {
			final ParseTree child = elements.getChild(i);

			if (CONTEXT_CTOR.containsKey(child.getClass())) {
				this.elements.add(CONTEXT_CTOR.get(child.getClass()).create((ParserRuleContext) child));
			} else if (child instanceof TerminalNode) {
				final TerminalNode tn = (TerminalNode) child;
				final String value = tn.toString();
				// we don't want to store nodes for separators
				if (!(value.startsWith("[") || value.startsWith("]") || value.startsWith("(") || value.startsWith(")")
						|| value.startsWith("/") || value.startsWith("*"))) {
					this.elements.add(new com.github.nradov.abnffuzzer.Terminal(tn));
				}
			} else {
				throw new IllegalArgumentException("illegal child " + i + " type: " + child.getClass());
			}
		}
	}

	/**
	 * Concatenate a list of multiple byte arrays into a single byte array.
	 *
	 * @param content zero or more byte arrays
	 * @return a single byte array containing all of the parameter byte arrays in
	 *         order
	 * @throws IOException shouldn't happen
	 * @see <a href="https://stackoverflow.com/a/9133993" target="_">Easy way to
	 *      concatenate two byte arrays</a>
	 */
	public static byte[] concatenate(final List<byte[]> content) throws IOException {
		if (content.size() == 1) {
			return content.get(0);
		}
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (final byte[] b : content) {
			baos.write(b);
		}
		return baos.toByteArray();
	}

	public byte[] generate(final Fuzzer f, final Random r, final Set<String> exclude) throws IOException {
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
