package com.github.abnffuzzer.core;

/**
 * %x0D. Carriage return.
 *
 * @author Nick Radov
 */
public final class Cr extends Fixed {

    private static final byte[] CR = new byte[] { (byte) '\r' };

    public Cr() {
        super(CR);
    }

}
