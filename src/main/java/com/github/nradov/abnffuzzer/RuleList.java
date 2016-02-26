package com.github.nradov.abnffuzzer;

import java.util.Map.Entry;
import java.util.TreeMap;

class RuleList extends TreeMap<String, Rule> {

    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");
    private static final long serialVersionUID = 3143015840271520264L;

    public RuleList() {
        // rule names are case insensitive
        super(String.CASE_INSENSITIVE_ORDER);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (final Entry<String, Rule> e : this.entrySet()) {
            sb.append(e.getKey()).append(" = ").append(e.getValue())
                    .append(LINE_SEPARATOR);
        }
        return sb.toString();
    }

}
