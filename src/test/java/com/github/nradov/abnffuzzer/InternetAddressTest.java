package com.github.nradov.abnffuzzer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.junit.Test;

import com.github.nradov.abnffuzzer.Fuzzer;

/**
 * Test the {@link InternetAddress} class.
 *
 * @author Nick Radov
 */
public class InternetAddressTest {

    @SuppressWarnings("serial")
    private static final Set<String> EXCLUDE_2822 = Collections
            .unmodifiableSet(new HashSet<String>() {
                {
                    add("CFWS");
                    add("comment");
                    add("quoted-string");
                    add("obs-local-part");
                }
            });

    @Test
    public void testInternetAddress()
            throws URISyntaxException, IOException, AddressException {
        final Fuzzer rfc2822 = new Fuzzer(
                InternetAddressTest.class.getResourceAsStream("/rfc2822.txt"));
        final Fuzzer rfc1034 = new Fuzzer(
                InternetAddressTest.class.getResourceAsStream("/rfc1034.txt"));

        for (int i = 0; i < 1000; i++) {
            final String localPart = rfc2822.generateAscii("local-part",
                    EXCLUDE_2822);
            final String domain = rfc1034.generateAscii("domain").trim();
            if (domain.isEmpty()) {
                // legal but not supported
                continue;
            }
            final InternetAddress addr = new InternetAddress(
                    localPart + "@" + domain, true);
            addr.validate();
        }
    }

}
