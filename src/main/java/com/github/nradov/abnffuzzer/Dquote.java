package com.github.nradov.abnffuzzer;

/**
 * %x22. " (Double Quote)
 *
 * @author Nick Radov
 */
final class Dquote extends Fixed {

    private static final byte[] DQUOTE = new byte[] { '"' };

    public Dquote() {
        super(DQUOTE);
    }

}
