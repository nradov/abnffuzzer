package com.github.abnffuzzer.core;

/**
 * %x22. " (Double Quote)
 *
 * @author Nick Radov
 */
public final class Dquote extends Fixed {

    private static final byte[] DQUOTE = new byte[] { '"' };

    public Dquote() {
        super(DQUOTE);
    }

}
