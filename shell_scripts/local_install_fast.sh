#!/bin/bash

# fast install to local gradle repository bypassing  
#  check-style , unit-tests and javaDoc

. preChecks

gradle build install -x test -x checkstyleMain -x javaDoc
