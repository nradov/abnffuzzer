package com.github.nradov.abnffuzzer;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

/**
 * Test the definition of ABNF in ABNF.
 * 
 * @author Nick Radov
 */
public class AbnfTest {

	@Test
	public void testGenerateAscii() throws IOException {
		final Fuzzer rfc5234 = new Fuzzer(this.getClass().getResourceAsStream("/rfc5234.txt"));
		for (int i = 0; i < 3; i++) {
			final String abnf = rfc5234.generateAscii("rule") + "\n";
			assertTrue(abnf.contains("="));
		}
	}

}
