package com.github.abnffuzzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.github.abnffuzzer.AbnfParser.RepeatContext;
import com.github.abnffuzzer.AbnfParser.RepetitionContext;

public class Repetition extends Element {

    private static final String VARIABLE = "*";

    private final int atLeast;
    private final int atMost;

    public Repetition(final RepetitionContext elements) {
        switch (elements.getChildCount()) {
        case 1:
            atLeast = 1;
            atMost = 1;
            this.elements.add(new Element(elements.getChild(0)));
            break;
        case 2:
            final RepeatContext repeat = (RepeatContext) elements.getChild(0);
            this.elements.add(new Element(elements.getChild(1)));
            switch (repeat.getChildCount()) {
            case 0:
                throw new IllegalArgumentException();
            case 1:
                if ("*".equals(repeat.getChild(0).toString())) {
                    atLeast = 0;
                    atMost = Integer.MAX_VALUE;
                } else {
                    atLeast = Integer
                            .parseUnsignedInt(repeat.getChild(0).toString());
                    atMost = atLeast;
                }
                break;
            case 2:
                if (VARIABLE.equals(repeat.getChild(0).toString())) {
                    atLeast = 0;
                } else {
                    atLeast = Integer
                            .parseUnsignedInt(repeat.getChild(0).toString());
                }
                if (VARIABLE.equals(repeat.getChild(1).toString())) {
                    atMost = Integer.MAX_VALUE;
                } else {
                    atMost = Integer
                            .parseUnsignedInt(repeat.getChild(1).toString());
                }
                break;
            case 3:
                atLeast = Integer
                        .parseUnsignedInt(repeat.getChild(0).toString());
                if (!VARIABLE.equals(repeat.getChild(1).toString())) {
                    throw new IllegalArgumentException();
                }
                atMost = Integer
                        .parseUnsignedInt(repeat.getChild(2).toString());
                break;
            default:
                throw new IllegalArgumentException();
            }
            break;
        default:
            throw new IllegalArgumentException();
        }

    }

    @Override
    public byte[] generate(final Fuzzer f, final Random r,
            final Set<String> exclude) {
        final List<byte[]> childContent = new ArrayList<>(atLeast);
        for (int i = 0; i < atLeast; i++) {
            childContent.add(super.generate(f, r, exclude));
        }
        int repetitions = atLeast;
        while (repetitions < atMost && r.nextBoolean()) {
            childContent.add(super.generate(f, r, exclude));
            repetitions++;
        }
        return concatenate(childContent);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (atLeast == atMost) {
            if (atLeast != 1) {
                sb.append(atLeast);
            }
        } else {
            if (atLeast != 0) {
                sb.append(atLeast);
            }
            sb.append(VARIABLE);
            if (atMost != Integer.MAX_VALUE) {
                sb.append(atMost);
            }
        }
        sb.append(super.toString());
        return sb.toString();
    }
}
