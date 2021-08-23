#!/bin/bash

##
## @test Power10SupportEnabled.sh
## @bug 1870625
## @summary test whether power 10 support is enabled
## @run shell Power10SupportEnabled.sh
## @requires jdk.version.major >= 11
## @requires os.family != "windows"
##

${TESTJAVA} -version -XX:+UseByteReverseInstructions
