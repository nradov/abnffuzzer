package com.github.nradov.abnffuzzer;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Test the Incremental Alternatives of ABNF.
 * 
 * @author Nick Radov
 */
public class RuleTest {

	@Test
	public void testGenerateAscii() throws IOException {
		final Fuzzer rule = new Fuzzer(this.getClass().getResourceAsStream("/rule.txt"));
		final Map<String, Integer> res = new HashMap<>();
		res.put("a", 0); res.put("b", 0); res.put("c", 0);
		for (int i = 0; i < 1000; i++) {
			final String abnf = rule.generateAscii("rule");
			assertTrue(res.containsKey(abnf));
			res.put(abnf, res.get(abnf)+1);
 		}
		for (String r : res.keySet()) {
			assertTrue(res.toString(), res.get(r) > 0);
                }
	}

}
