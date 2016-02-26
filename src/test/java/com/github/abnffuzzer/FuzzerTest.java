package com.github.abnffuzzer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

/**
 * Test the {@link Fuzzer} class.
 *
 * @author Nick Radov
 */
public class FuzzerTest {

    @Test
    public void testMain() throws ParseException, IOException {
        final File input = File.createTempFile(FuzzerTest.class.getSimpleName(),
                null);
        input.deleteOnExit();
        try (final PrintWriter pw = new PrintWriter(
                new FileOutputStream(input))) {
            pw.println("foo = \"foo\" / bar\n");
            pw.flush();
        }
        final File output = File
                .createTempFile(FuzzerTest.class.getSimpleName(), null, null);
        output.deleteOnExit();
        Fuzzer.main(new String[] { "--count=10", "--separator=_",
                "--input=" + input.getAbsolutePath(),
                "--output=" + output.getAbsolutePath(),
                "--charset=" + StandardCharsets.US_ASCII, "--exclude=bar",
                "foo" });
        assertEquals(10 * "foo".length() + 9, output.length());

        OutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os));
        Fuzzer.main(new String[] { "-?" });
        assertTrue(os.toString().length() > 0);

        os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os));
        Fuzzer.main(new String[] { "-help" });
        assertTrue(os.toString().length() > 0);
    }

    @Test
    public void testGenerateAscii() throws IOException {
        final Fuzzer f = new Fuzzer("foo = \"foo\"\n");
        assertEquals("foo", f.generateAscii("foo"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateAsciiExclude() throws IOException {
        final Fuzzer f = new Fuzzer("foo = bar / baz\n");
        f.generateAscii("foo", new HashSet<String>(
                Arrays.asList(new String[] { "bar", "baz" })));
    }

    @Test
    public void testGenerate() throws IOException {
        final Fuzzer f = new Fuzzer("foo = \"foo\"\n");
        assertArrayEquals(new byte[] { 'f', 'o', 'o' }, f.generate("foo"));
    }

    @Test
    public void testGenerateCharset() throws IOException {
        final Fuzzer f = new Fuzzer("foo = %x00.66.00.6F.00.6F");
        assertEquals("foo", f.generate("foo", Collections.emptySet(),
                StandardCharsets.UTF_16));
    }

    @Test
    public void testSetRandom() throws NoSuchAlgorithmException, IOException {
        final Random r = SecureRandom.getInstanceStrong();
        final Fuzzer f = new Fuzzer("");
        f.setRandom(r);
        assertEquals(r, f.getRandom());
    }

}
