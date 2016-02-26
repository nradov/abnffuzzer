# ABNF Fuzzer
This is a Java [fuzz testing](http://pages.cs.wisc.edu/~bart/fuzz/) tool that can find defects in implementations of [Augmented Backus-Naur Form](https://tools.ietf.org/html/rfc5234) (ABNF) rules such as IETF RFCs. You can use it to generate random inputs for test cases, which can be helpful for finding edge case defects. I wrote it primarily as a way to learn [ANTLR](http://www.antlr.org/).

For additional documentation including dependencies and Javadoc please see the [Maven generated site](http://nradov.github.io/abnffuzzer/site-plugin/).

## Usage
This tool can be called directly from Java code — such as a JUnit test case — or from the command line. In order to use it you first need a file containing **only** ABNF rule definitions. Here's a sample of simple ABNF rules file.

```
foo = bar / baz
bar = "Hello"
baz = "World!"
```

If you're testing an implementation of an IETF RFC you can simply copy and paste the formal rule definitions into a new file; usually they're all contained in a single section near the end of the document. You can also try the IETF [ABNF Extraction](https://tools.ietf.org/abnf/) tool, although it appears to produce incorrect output for some RFCs so you may need to manually edit the results.

### JUnit
In order to use the library. Fuzz testing is more likely to test. For example let's say you have a class named MyClass containing a method named myMethod which takes a String parameter and returns `true` if that parameter matches ABNF rule "foo" defined in RFC 99999. 

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

For additional samples see the [JUnit test cases](https://github.com/nradov/abnffuzzer/tree/master/src/test/java/com/github/abnffuzzer) in this repository.

### Command Line
For testing web services or applications written in other languages this tool can also be called from the command line.
