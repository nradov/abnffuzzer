package com.github.abnffuzzer.core;

/**
 * CR LF. Internet standard newline.
 *
 * @author Nick Radov
 */
public final class CrLf extends Fixed {

    private static final byte[] CRLF = new byte[] { '\r', '\n' };

    public CrLf() {
        super(CRLF);
    }

}
