#!/bin/bash

. preChecks

gradle clean build install -x test -x checkstyleMain -x javaDoc
gradle clean test -x checkstyleMain -x javaDoc
