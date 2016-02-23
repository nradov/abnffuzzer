package com.github.abnffuzzer;

import com.github.abnffuzzer.antlr4.AbnfParser.ConcatenationContext;

public class Concatenation extends Element {

    public Concatenation(final ConcatenationContext elements) {
        super(elements);
    }

}
