package com.github.nradov.abnffuzzer;

/**
 * SP / HTAB. White space.
 *
 * @author Nick Radov
 */
final class Wsp extends SingleByte {

private static final byte[][] BYTES = new byte[][] {new byte[] { ' ' }, new byte[] { '\t' }};
    
    Wsp() {
    	super(BYTES);
    }

}
