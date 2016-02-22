package com.github.abnffuzzer.core;

/**
 * %x20.
 *
 * @author Nick Radov
 */
public final class Sp extends Fixed {

    private static final byte[] SP = new byte[] { ' ' };

    public Sp() {
        super(SP);
    }

}
