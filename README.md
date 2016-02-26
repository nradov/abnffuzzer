# ABNF Fuzzer
This is a Java [fuzz testing](http://pages.cs.wisc.edu/~bart/fuzz/) tool that can find defects in implementations of [Augmented Backus-Naur Form](https://tools.ietf.org/html/rfc5234) (ABNF) rules such as IETF RFCs. You can use it to generate random inputs for test cases, which can be helpful for finding edge case defects. I wrote it primarily as a way to learn [ANTLR](http://www.antlr.org/).

For additional documentation including dependencies and Javadoc please see the [Maven generated site](http://nradov.github.io/abnffuzzer/site-plugin/).

## Usage
This tool can be called directly from Java code — such as a JUnit test case — or from the command line. In order to use it you first need a file containing **only** ABNF rule definitions. If you're testing an implementation of an IETF RFC you can simply copy and paste the formal rule definitions into a new file; usually they're all contained in a single section near the end of the document. You can also try the IETF [ABNF Extraction](https://tools.ietf.org/abnf/) tool, although it appears to produce incorrect output for some RFCs so you may need to manually edit the results.

### JUnit

### Command Line
