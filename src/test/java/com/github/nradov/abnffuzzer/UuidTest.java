package com.github.nradov.abnffuzzer;

import java.io.IOException;
import java.util.UUID;

import org.junit.Test;

import com.github.nradov.abnffuzzer.Fuzzer;

/**
 * Test the {@link UUID} class.
 *
 * @author Nick Radov
 */
public class UuidTest {

    /**
     * Test the {@link UUID#fromString(String)} method.
     * 
     * @throws IOException
     *             if an error occurs while reading a resource
     */
    @Test
    public void testFromString() throws IOException {
        final Fuzzer f = new Fuzzer(
                UuidTest.class.getResourceAsStream("/rfc4122.txt"));

        for (int i = 0; i < 1000; i++) {
            final String uuid = f.generateAscii("UUID");
            UUID.fromString(uuid);
        }
    }

}
