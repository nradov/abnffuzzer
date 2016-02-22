package com.github.abnffuzzer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

import org.junit.Test;

/**
 * Test the {@link UUID} class.
 *
 * @author Nick Radov
 */
public class UuidTest {

    /** Test the {@link UUID#fromString(String)} method. */
    @Test
    public void testFromString() throws IOException, URISyntaxException {
        final Fuzzer f = new Fuzzer(
                UuidTest.class.getResourceAsStream("/rfc4122.txt"));

        for (int i = 0; i < 1000; i++) {
            final String uuid = f.generateAscii("UUID");
            UUID.fromString(uuid);
        }
    }

}
