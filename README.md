# Interledger Core (Java) [![Join the chat at https://gitter.im/interledger/java](https://badges.gitter.im/interledger/java.svg)](https://gitter.im/interledger/java) [![CircleCI](https://circleci.com/gh/interledger/java-ilp-core.svg?style=svg)](https://circleci.com/gh/interledger/java-ilp-core)

Base library for ILP projects providing basic service interfaces, event descriptions, exceptions and data models.

See https://github.com/interledger/rfcs/blob/master/0003-interledger-protocol/0003-interledger-protocol.md

Depends on [Java Crypto-Conditions](https://github.com/interledger/java-crypto-conditions)

## Development

To contribute and work on this library clone the code and dependencies to the same folder (the dependency crypto-conditions is not available as a package yet so we use a project dependency).

```
$ git clone https://github.com/interledger/java-crypto-conditions.git
$ git clone https://github.com/interledger/java-ilp-core.git
```

Before submitting a pull request ensure that your build passes with no Checkstyle errors.

## TODO

  - Better JavaDoc