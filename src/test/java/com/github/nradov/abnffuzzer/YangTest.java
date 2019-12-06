package com.github.nradov.abnffuzzer;

import java.io.IOException;

import org.junit.Test;

/**
 * Test of fix for issue 2. Due to the complexity of this specification,
 * successful test execution with the <code>action-stmt</code> rule may require
 * increasing your JVM's stack size; for Oracle this can be done with the
 * <code>-Xss</code> <i>size</i> option. Thus this test currently uses the
 * simpler <code>range-arg</code> rule.
 * 
 * @author Damian ONeill
 * @author Nick Radov
 * @see <a href="https://tools.ietf.org/html/rfc7950" target="_">RFC 7950</a>
 * @see <a href="https://github.com/nradov/abnffuzzer/issues/2" target="_">No enum constant com.github.nradov.abnffuzzer.Terminal.Radix.s</a>
 */
public class YangTest {

	@Test
	// TODO: this method sometimes takes an extremely long time to execute
	public void testGenerateAscii() throws IOException {
		final Fuzzer rfc7950 = new Fuzzer(this.getClass().getResourceAsStream("/rfc7950.txt"));
		for (int i = 0; i < 5; i++) {
			rfc7950.generateAscii("range-arg");
		}
	}

}
