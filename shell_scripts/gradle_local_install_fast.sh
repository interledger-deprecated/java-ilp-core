#!/bin/bash

# Bypass check style , unit-tests and javaDoc
gradle clean build install -x test -x checkstyleMain -x javaDoc
