#!/bin/bash

# fast install to local gradle repository bypassing  
#  check-style , unit-tests and javaDoc

. preChecks

# REF: Listing map phase (1 <-> N) goals:
#  https://stackoverflow.com/questions/1709625/maven-command-to-list-lifecycle-phases-along-with-bound-goals
#  mvn fr.jcgay.maven.plugins:buildplan-maven-plugin:list -Dbuildplan.tasks=install
# mvn resources:resources compiler:compile jar:jar install:install
  mvn resources:resources compiler:compile jar:jar install:install
