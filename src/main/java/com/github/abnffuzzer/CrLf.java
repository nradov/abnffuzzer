package com.github.abnffuzzer;

/**
 * CR LF. Internet standard newline.
 *
 * @author Nick Radov
 */
final class CrLf extends Fixed {

    private static final byte[] CRLF = new byte[] { '\r', '\n' };

    public CrLf() {
        super(CRLF);
    }

}
