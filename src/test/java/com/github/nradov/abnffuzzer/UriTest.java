package com.github.nradov.abnffuzzer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.mail.internet.AddressException;

import org.junit.Test;

import com.github.nradov.abnffuzzer.Fuzzer;

/**
 * Test the {@link URI} class.
 *
 * @author Nick Radov
 */
public class UriTest {

    @SuppressWarnings("serial")
    private static final Set<String> EXCLUDE_2396 = Collections
            .unmodifiableSet(new HashSet<String>() {
                {
                    add("IPvFuture"); // not supported
                    add("path-empty"); // not supported
                    add("path-abempty"); // not supported
                }
            });

    @Test
    public void testUri()
            throws URISyntaxException, IOException, AddressException {
        final Fuzzer f = new Fuzzer(
                UriTest.class.getResourceAsStream("/rfc3986.txt"));

        final Set<String> tried = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            final String uri = f.generateAscii("URI-reference", EXCLUDE_2396);
            if (!tried.contains(uri)) {
                try {
                    new URI(uri);
                } catch (final URISyntaxException e) {
                    // work around differences between RFC 3986 and RFC 2396
                    if (!(e.getMessage()
                            .startsWith("Expected authority at index")
                            || e.getMessage().startsWith(
                                    "Malformed port number at index"))) {
                        System.err.println(e.getMessage());
                        System.err.println(uri);
                        System.err.println();
                        throw e;
                    }
                }
                tried.add(uri);
            }
        }
    }

}
