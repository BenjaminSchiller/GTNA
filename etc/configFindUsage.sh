#!/bin/bash

cd ../src

for f in $(find . -type f); do
	if [ $(cat $f | grep $1 | wc -l ) -ne "0" ]; then
		echo "$1 used in $f"
	fi
done
