package com.github.nradov.abnffuzzer;

import org.antlr.v4.runtime.ParserRuleContext;

@FunctionalInterface
interface ElementFactory {

	Element create(ParserRuleContext context);
	
}
