package com.github.nradov.abnffuzzer;

import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

class RuleList extends TreeMap<String, List<Rule>> {

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
        boolean first = true;
        for (final Entry<String, List<Rule>> e : this.entrySet()) {
            for (Rule r : e.getValue()) {
                    String def = first ? " =  " : " =/ ";
                    sb.append(e.getKey()).append(def).append(e.getValue())
                            .append(LINE_SEPARATOR);
                    first = false;
            }
        }
        return sb.toString();
    }

}
