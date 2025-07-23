#!/usr/bin/env bash

td=$(mktemp -d foooXXXXXX) && trap "rm -rf $td" EXIT
expected="$td/expected.txt"
submitted="$td/submitted.txt"

cat expected.txt | sort -k2,2 > $expected
cat submitted.txt | sort -k2,2 > $submitted

diff $expected $submitted

if [ "$?" == 0 ]; then
    echo "good"
    exit 0
else
    echo "bad"
    exit 1
fi
