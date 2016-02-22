package com.github.abnffuzzer.core;

/**
 * %x0A. Linefeed.
 *
 * @author Nick Radov
 *
 */
public class Lf extends Fixed {

    private static final byte[] LF = new byte[] { '\n' };

    public Lf() {
        super(LF);
    }

}
