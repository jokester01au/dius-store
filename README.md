# DiUS Coding Test
**Joseph Thomas-Kerr**
**12-11-2019**

This repository is a Java module that fulfils the requirements set out in https://github.com/DiUS/coding-tests/blob/master/dius_shopping.md.

## Features

### TDD

Failing tests written and in most cases all edge cases identified prior to implementation of functionality.

_NB Architecture defined in the form of class stubs in parallel with test development._
 
### Generic persistence mechanism

Provision is made for multiple persistence mechanisms for product and pricing rules. This is defined by the `me.jtk.dius.store.persistence.Loader` interface. The integration tests use csv files, but a production system would likely use some kind of database. In addition, `Loader` should also support distributed systems with persistent data delivered by a network connection or reactive stream.

The `CsvLoader` uses a pluggable `me.jtk.dius.store.persistence.Builder` interface to allow for dynamic extension to new data types.

### Simple pricing rule specification

As required by the coding test guidelines, pricing rules may be altered by editing a [csv file](./pricing-rules.csv). The pricing rules implemented cover the discount mechanisms required by the guidelines, and should cover most commonly-used pricing schemes, but new types of discount can be added by registering a new implementation of the `me.jtk.dius.store.models.PricingRule` abstract class.

### Functional implementation

The majority of the codebase is implemented in a functional style which is well suited to the type of data operations required by the task, and also lends itself to distributed and reactive architectures which are widely used in modern systems.

## Limitations

### Java is not primarily a functional language

Java's functional constructs are not the best; in particular the need to convert collections to and from streams can at times impair readability of the code. In contrast, languages that feature functional tools as a primary feature (such as Scala) may have offered simpler and more maintainable implementation.

### Singleton-based dependency management

Because of stipulations regarding external libraries, and time limitations, several inter-class dependencies are implemented using the Singleton pattern. This is well known to impair testability, distributed architectures and the flexibility of the codebase, but it was decided to be an adequate compromise.

### Missing tests

Due to time constraints, some test suites are missing the full set of corner cases. In particular, the [CSVLoaderTest](test/me/jvlk/dius/store/persistence/CsvLoaderTest.java) requires substantially more testing to be fully production-ready, and [CheckoutTest](test/me/jvlk/dius/store/CheckoutTest.java) is merely a stub. [FreeGiftBuilder](src/me/jvlk/dius/store/persistence/rules/FreeGiftBuilder.java) and [VolumePricingBuilder](src/me/jvlk/dius/store/persistence/rules/VolumePricingBuilder.java) are not covered by tests at all. While these are very simple classes, it is often the simple things that attract errors due to inadequate consideration of edge cases and the like. The core functionality of these classes is covered by the [integration test](it/me/jvlk/dius/store/IntegrationTests.java) but this is not adequate for a production system.

# Running the tests

The system may be built and exercised via the included [ant](https://ant.apache.org) build (file)[./build.xml]. The junit5 task (JUnitLauncher)[https://ant.apache.org/manual/Tasks/junitlauncher.html] must be present in the ant installation (this is installed with ant on OSX; it is not known if this is the case on other systems).

To install ant, on Mac (using homebrew), execute `brew install ant`. On Linux, perform `apt install ant` (as root).

After installation, running `ant` will compile the module, and run integration and unit tests and tests. `ant it` will run the integration tests (NB these have a dependency on unit tests passing) or `ant test` will execute just the unit tests. `ant clean` removes build outputs and test reports.