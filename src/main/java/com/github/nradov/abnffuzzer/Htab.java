package com.github.nradov.abnffuzzer;

/**
 * %x09. Horizontal tab.
 *
 * @author Nick Radov
 */
class Htab extends Fixed {

    private static final byte[] HTAB = new byte[] { '\t' };

    public Htab() {
        super(HTAB);
    }

}
