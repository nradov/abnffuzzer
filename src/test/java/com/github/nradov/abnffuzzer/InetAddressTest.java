package com.github.nradov.abnffuzzer;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URISyntaxException;

import org.junit.Test;

import com.github.nradov.abnffuzzer.Fuzzer;

/**
 * Test the {@link InetAddress} class.
 *
 * @author Nick Radov
 */
public class InetAddressTest {

    @Test
    public void testGetByName() throws IOException, URISyntaxException {
        final InputStream is = InetAddressTest.class
                .getResourceAsStream("/rfc3986.txt");
        final Fuzzer f = new Fuzzer(is);

        for (int i = 0; i < 1000; i++) {
            final String ipV6Address = f.generateAscii("IPv6address");
            final InetAddress inetAddress = InetAddress.getByName(ipV6Address);
            assertTrue(inetAddress instanceof Inet6Address);
        }
    }

}
