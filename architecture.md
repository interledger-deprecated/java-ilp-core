#DRAFT

This is an attempt to pull together the various peices of work being done on Java implementations of ILP. It will evolve to be a proper description of the architecture in time.

## Componenents

The architecture consists of a number of components that tie together to facilitate a complete end-to-end ILP payment.

### Core

The core library contains non-implementation-specific code that should be used by all implementations. It defines interfaces and some concrete classes for domain specific data models such as Interledger Addresses.

The core component is implemented in https://github.com/interledger/java-ilp-core/

### Client (possibly rename to node/peer?)

The client contains the high level orchestration logic for the protocol. It is used by senders, receviers and connectors to perform functions like perform setup, send messages to other clients connected to a common ledger and make transfers to other clients on a ledger.

### 

## Roadmap

To get us closer to a complete end-to-end solution using Java componenets we should aim to have a working setup as follows:

![Phase 1 components](https://raw.githubusercontent.com/interledger/java-ilp-core/development/ILP%20Java%20-%20Phase%201.png)

Once we have this working we can expand to a more complete setup with multiple connectors and implement the routing and quoting protocols more fully:

![Phase 2 components](https://raw.githubusercontent.com/interledger/java-ilp-core/development/ILP%20Java%20-%20Phase%202.png)
