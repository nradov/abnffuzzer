# ABNF Fuzzer
This is a Java [fuzz testing](https://en.wikipedia.org/wiki/Fuzz_testing) tool that can find defects in implementations of Augmented Backus-Naur Form (ABNF) rules such as IETF RFCs. ABNF is itself defined in RFCs [5234](https://tools.ietf.org/html/rfc5234) and [7405](https://tools.ietf.org/html/rfc7405). You can use it to generate random valid inputs for test cases, which can be helpful for finding edge case defects. I wrote it primarily as a way to learn [ANTLR](http://www.antlr.org/). Thanks to @rainerschuster for providing the [Anbf.g4](https://github.com/antlr/grammars-v4/blob/master/abnf/Abnf.g4) grammar.

For additional documentation including dependency information and Javadoc please see the [Maven generated site](http://nradov.github.io/abnffuzzer/site-plugin/).

## Usage
This tool can be called directly from Java code — such as a [JUnit](http://junit.org/) test case — or from the command line. In order to use it you first need a file containing **only** ABNF rule definitions. Here's a sample simple ABNF rules file.

```
foo = bar / baz
bar = "Hello"
baz = "World!"
```

If you're testing an implementation of an IETF RFC you can simply copy and paste the formal rule definitions into a new file; usually they're all contained in a single section near the end of the document. Be sure to check the errata as those correct the rules in some RFCs. You can also try the IETF [ABNF Extraction](https://tools.ietf.org/abnf/) tool, although it appears to produce incorrect output for some RFCs so you may need to manually edit the results. You should also manually remove any prose values from the rules; obviously this tool can't interpret prose so it's usually best to replace those with literals.

Options are available to limit the output by excluding certain rules. This can be useful if your application only provides a partial implementation of a particular rule and you don't want to test certain alternate forms. The tool can generate output as either raw bytes (octets), or characters strings encoded using any of the [standard Java character sets](https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/nio/charset/StandardCharsets.html).

The probability of finding a particular defect with fuzz testing increases with the number of test cases, up to an asymptotic limit. You have to balance that against test execution time. I recommend doing an extended fuzz testing run with thousands or millions of iterations the first time; let it run for hours. Then use a much smaller number of iterations in your automated continuous integration process so that it doesn't cause long delays.

### JUnit
Call one of the `generate` methods wherever you need a random `String` or `byte[]` value matching a particular ABNF rule. For example let's say you have a class named `MyClass` containing a method named `myMethod` which takes a `String` parameter and returns `true` if that parameter matches ABNF rule "foo" defined in RFC 99999. 

```java
    @Test
    public void testMyMethod() throws IOException {
        File file = new File("rfc99999.txt");
        Fuzzer fuzzer = new Fuzzer(file);
        
        MyClass m = new MyClass();
        for (int i = 0; i < 100; i++) {
            assertTrue(m.myMethod(fuzzer.generateAscii("foo")));
        }
    }
```

For additional samples see the [JUnit test cases](https://github.com/nradov/abnffuzzer/tree/master/src/test/java/com/github/nradov/abnffuzzer) in this repository.

### Command Line
For testing web services or applications written in other languages this tool can also be called from the command line. Binary jar file releases are available through this repository. By default it reads ABNF rules from `stdin`, generates a random value matching the ABNF rule named as the last command-line parameter, and writes it to `stdout`. You need to have the ANTLR [Java runtime binaries](http://www.antlr.org/download.html) and Apache [Commons CLI](https://commons.apache.org/proper/commons-cli/) in your `CLASSPATH`. Command-line options are available to control the number of values to generate, character set, input source, ouput destination. Use the `-?` command-line option for help on those options.

```
java com.github.nradov.abnffuzzer.Fuzzer -n 1000 -i rfc99999.txt -o testcases.txt foo
```
