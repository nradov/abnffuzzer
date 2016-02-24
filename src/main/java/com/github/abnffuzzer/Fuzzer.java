package com.github.abnffuzzer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.github.abnffuzzer.antlr4.AbnfLexer;
import com.github.abnffuzzer.antlr4.AbnfParser;
import com.github.abnffuzzer.antlr4.AbnfParser.ElementsContext;
import com.github.abnffuzzer.antlr4.AbnfParser.Rule_Context;

/**
 * Main class for fuzz testing. Instantiate this class with a set of ABNF rules
 * and then call one of the {@code generate} methods to create random output
 * suitable for use in a test.
 *
 * @author Nick Radov
 */
public class Fuzzer {

    @SuppressWarnings("unused")
    private static final long serialVersionUID = -5734957539715862213L;

    private transient Random random;

    /** Map of rule names to their elements. */
    private final Map<String, Rule> ruleList;

    /**
     * Parse a set of ABNF rules and generate one or more matching random
     * values. By default it reads the rules from {@code stdin} and writes one
     * value to {@code stdout}. Command-line parameters can be used to read from
     * and write to files, and to generate multiple values.
     *
     * @param args
     *            command line arguments
     * @throws ParseException
     *             if the command line can't be parsed
     * @throws IOException
     *             if an error occurs reading the input or writing the output
     */
    public static void main(final String[] args)
            throws ParseException, IOException {
        final Options options = new Options();
        options.addOption("n", "count", true,
                "specify the number of random values to generate (default to 1)");
        options.addOption("s", "separator", true,
                "string inserted between output values (default to the \"line.separator\" property)");
        options.addOption("i", "input", true, "input file path");
        options.addOption("o", "output", true, "output file path");
        options.addOption("e", "encoding", true,
                "character encoding (default to US ASCII)");
        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(options, args);
        final int count;
        if (cmd.hasOption("count")) {
            count = Integer.parseInt(cmd.getOptionValue("count"));
            if (count < 1) {
                throw new IllegalArgumentException("count must be >= 1");
            }
        } else {
            count = 1;
        }

        final Fuzzer f;
        if (cmd.hasOption("input")) {
            f = new Fuzzer(new File(cmd.getOptionValue("input")));
        } else {
            f = new Fuzzer(System.in);
        }

        if (cmd.getArgList().size() != 1) {
            throw new IllegalArgumentException("must specify 1 rule name");
        }
        final String ruleName = cmd.getArgList().get(0);
        System.out.print(f.generateAscii(ruleName));
    }

    /**
     * Create a new {@code Fuzzer} by reading ABNF rules from a URL.
     *
     * @param url
     *            location of ANBF rules
     * @throws IOException
     *             if the rules can't be read from the URL
     */
    public Fuzzer(final URL url) throws IOException {
        this(url.openStream());
    }

    /**
     * Create a new {@code Fuzzer} by reading ABNF rules from a URI.
     *
     * @param uri
     *            location of ANBF rules
     * @throws IOException
     *             if the rules can't be read from the URI
     */
    public Fuzzer(final URI uri) throws IOException {
        this(uri.toURL());
    }

    /**
     * Create a new {@code Fuzzer} by reading ABNF rules from a file.
     *
     * @param file
     *            location of ANBF rules
     * @throws IOException
     *             if the rules can't be read from the file
     */
    public Fuzzer(final File file) throws IOException {
        this(file.toURI());
    }

    /**
     * Create a new {@code Fuzzer} by reading ABNF rules from a stream.
     *
     * @param is
     *            ANBF rules
     * @throws IOException
     *             if the rules can't be read from the stream
     */
    public Fuzzer(final InputStream is) throws IOException {
        this(new InputStreamReader(is));
    }

    /**
     * Create a new {@code Fuzzer} by reading ABNF rules from a string.
     *
     * @param rules
     *            ANBF rules
     * @throws IOException
     *             if the rules can't be read
     */
    public Fuzzer(final String rules) throws IOException {
        this(new StringReader(rules));
    }

    /**
     * Create a new {@code Fuzzer} by reading ABNF rules from a reader.
     *
     * @param rules
     *            ANBF rules
     * @throws IOException
     *             if the rules can't be read
     */
    @SuppressWarnings("serial")
    public Fuzzer(final Reader rules) throws IOException {
        final AbnfLexer l = new AbnfLexer(new ANTLRInputStream(rules));
        final CommonTokenStream tokens = new CommonTokenStream(l);
        final AbnfParser p = new AbnfParser(tokens);
        p.setBuildParseTree(true);
        p.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(final Recognizer<?, ?> recognizer,
                    final Object offendingSymbol, final int line,
                    final int charPositionInLine, final String msg,
                    final RecognitionException e) {
                throw new IllegalStateException(
                        "failed to parse at line " + line + " due to " + msg,
                        e);
            }
        });

        final ParseTree tree = p.rulelist();
        ruleList = Collections.unmodifiableMap(new RuleList() {
            {
                for (int i = 0; i < tree.getChildCount(); i++) {
                    final ParseTree child = tree.getChild(i);
                    if (child instanceof Rule_Context
                            && child.getChildCount() == 3) {
                        // rule definition
                        final ParseTree name = child.getChild(0);
                        if (!(name instanceof TerminalNode)) {
                            throw new IllegalArgumentException();
                        }
                        final ParseTree equalSign = child.getChild(1);
                        if (!(equalSign instanceof TerminalNode)
                                || !"=".equals(equalSign.toString())) {
                            throw new IllegalArgumentException();
                        }
                        final ParseTree elements = child.getChild(2);
                        if (!(elements instanceof ElementsContext)) {
                            throw new IllegalArgumentException();
                        }

                        put(name.toString(),
                                new Rule((ElementsContext) elements));
                    }
                }
            }
        });
    }

    /**
     * Generate a random sequence of bytes which matches a named ABNF rule. The
     * output will be suitable for use in a fuzz test.
     *
     * @param ruleName
     *            ABNF rule name
     * @return random sequence which matches the specified rule
     * @throws IllegalArgumentException
     *             if {@code ruleName} doesn't exist
     * @throws IllegalStateException
     *             if any defined rule references another rule which doesn't
     *             exist
     * @see #generate(String, Set)
     */
    public byte[] generate(final String ruleName) {
        return generate(ruleName, Collections.<String> emptySet());
    }

    /**
     * Generate a random sequence of characters which matches a named ABNF rule.
     * The output will be suitable for use in a fuzz test.
     *
     * @param ruleName
     *            ABNF rule name
     * @param exclude
     *            ABNF rule names to exclude during alternative selection; this
     *            allows for testing code that implements only a subset of an
     *            RFC
     * @return random sequence of characters which matches the specified rule
     * @throws IllegalArgumentException
     *             if {@code ruleName} doesn't exist
     * @throws IllegalStateException
     *             if any defined rule references another rule which doesn't
     *             exist
     */
    public String generateAscii(final String ruleName,
            final Set<String> exclude) {
        return generate(ruleName, exclude, StandardCharsets.US_ASCII);
    }

    /**
     * Generate a random sequence of characters which matches a named ABNF rule.
     * The output will be suitable for use in a fuzz test.
     *
     * @param ruleName
     *            ABNF rule name
     * @return random sequence of characters which matches the specified rule
     *         encoded in the US_ASCII character set
     * @throws IllegalArgumentException
     *             if {@code ruleName} doesn't exist
     * @throws IllegalStateException
     *             if any defined rule references another rule which doesn't
     *             exist
     */
    public String generateAscii(final String ruleName) {
        return generateAscii(ruleName, Collections.<String> emptySet());
    }

    /**
     * Generate a random sequence of bytes which matches a named ABNF rule. The
     * output will be suitable for use in a fuzz test.
     *
     * @param ruleName
     *            ABNF rule name
     * @param exclude
     *            rule names to exclude when generating output
     * @return random sequence of bytes which matches the specified rule
     * @throws IllegalArgumentException
     *             if {@code ruleName} doesn't exist
     * @throws IllegalStateException
     *             if any defined rule references another rule which doesn't
     *             exist
     */
    public byte[] generate(final String ruleName, final Set<String> exclude) {
        return getRule(ruleName).generate(this, getRandom(), exclude);
    }

    /**
     * Generate a random sequence of characters which matches a named ABNF rule.
     * The output will be suitable for use in a fuzz test.
     *
     * @param ruleName
     *            ABNF rule name
     * @param exclude
     *            rule names to exclude when generating output
     * @param charset
     *            encoding for the return value
     * @return random sequence of characters which matches the specified rule
     *         encoded in the specified character set
     * @throws IllegalArgumentException
     *             if {@code ruleName} doesn't exist
     * @throws IllegalStateException
     *             if any defined rule references another rule which doesn't
     *             exist
     */
    public String generate(final String ruleName, final Set<String> exclude,
            final Charset charset) {
        return new String(generate(ruleName, exclude), charset);
    }

    // built in rules

    @SuppressWarnings("serial")
    private static final Map<String, Rule> BUILT_IN_RULES = Collections
            .unmodifiableMap(new HashMap<String, Rule>() {
                {
                    put("ALPHA", new Alpha());
                    put("BIT", new Bit());
                    put("CHAR", new Char());
                    put("CR", new Cr());
                    put("CRLF", new CrLf());
                    put("CTL", new Ctl());
                    put("DIGIT", new Digit());
                    put("DQUOTE", new Dquote());
                    put("HEXDIG", new Hexdig());
                    put("HTAB", new Htab());
                    put("LF", new Lf());
                    put("LWSP", new Lwsp());
                    put("OCTET", new Octet());
                    put("SP", new Sp());
                    put("VCHAR", new Vchar());
                    put("WSP", new Wsp());
                }
            });

    /**
     * Get a defined ABNF rule.
     *
     * @param ruleName
     *            rule name (could be one of the core rules defined in RFC 5234)
     * @return the rule
     * @throws IllegalArgumentException
     *             if the rule name isn't defined
     */
    Rule getRule(final String ruleName) {
        if (BUILT_IN_RULES.containsKey(ruleName)) {
            return BUILT_IN_RULES.get(ruleName);
        } else if (ruleList.containsKey(ruleName)) {
            return ruleList.get(ruleName);
        }
        throw new IllegalArgumentException("no rule \"" + ruleName + "\"");
    }

    /**
     * Get the random number generator used to pick between options and
     * alternatives.
     *
     * @return random number generator
     * @see #setRandom(Random)
     */
    public Random getRandom() {
        if (random == null) {
            random = new Random();
        }
        return random;
    }

    /**
     * Set the random number generator used to pick between alternatives. If
     * this isn't set then a default implementation will be used.
     *
     * @param r
     *            random number generator
     * @see #getRandom()
     */
    public void setRandom(final Random r) {
        if (r == null) {
            throw new IllegalArgumentException("null");
        }
        random = r;
    }

}
