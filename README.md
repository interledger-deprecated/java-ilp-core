# Interledger Core (Java) [![Join the chat at https://gitter.im/interledger/java](https://badges.gitter.im/interledger/java.svg)](https://gitter.im/interledger/java) [![CircleCI](https://circleci.com/gh/interledger/java-ilp-core.svg?style=svg)](https://circleci.com/gh/interledger/java-ilp-core)

Base library for Interledger projects providing service interfaces, event descriptions, exceptions and data models.

For more information about Interledger specifications that underpin this library, please reference [https://github.com/interledger/rfcs](https://github.com/interledger/rfcs).

## Usage
For more detail about how to use this library, consult the [wiki](https://github.com/interledger/java-ilp-core/wiki)

## Development
We welcome any and all submissions, whether it's a typo, bug fix, or new feature.

### Requirements
This project uses Gradle to manage dependencies and other aspects of the build.  To install Gradle, follow the instructions at [https://gradle.org](https://gradle.org/).

### Get the Code
This project depends on [Java Crypto-Conditions](https://github.com/interledger/java-crypto-conditions). To contribute to this library, clone the following two projects to the same folder (the dependency crypto-conditions is not available as a package yet so we use a project dependency).

```bash
$ git clone https://github.com/interledger/java-crypto-conditions.git
$ git clone https://github.com/interledger/java-ilp-core.git
```

### Build the Project
To build the project, execute the following command from the top-level folder that you cloned the above two projects to.  For example:

```bash
$ gradle build test
```

### Contributing
This project utilizes a Pull Request submission model.  Before submitting a pull request, ensure that your build passes with no test failures nor Checkstyle errors.