package com.github.nradov.abnffuzzer;

import com.github.nradov.abnffuzzer.antlr4.AbnfParser.RepeatContext;

class Repeat extends Element {

    public Repeat(final RepeatContext elements) {
        super(elements);
    }

}
