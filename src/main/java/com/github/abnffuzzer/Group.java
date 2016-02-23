package com.github.abnffuzzer;

import com.github.abnffuzzer.antlr4.AbnfParser.GroupContext;

public class Group extends Element {

    public Group(final GroupContext elements) {
        super(elements);
    }

    @Override
    public String toString() {
        return "(" + super.toString() + ")";
    }

}
