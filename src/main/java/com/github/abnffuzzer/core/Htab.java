package com.github.abnffuzzer.core;

/**
 * %x09. Horizontal tab.
 *
 * @author Nick Radov
 */
public class Htab extends Fixed {

    private static final byte[] HTAB = new byte[] { '\t' };

    public Htab() {
        super(HTAB);
    }

}
