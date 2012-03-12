#!/bin/bash

for key in $(cat configKeys.txt); do
	temp=$(./configFindUsage.sh $key | wc -l)
	if [ $temp -eq 0 ]; then
		echo "$key => none"
#	else
#		echo "$key => $temp"
	fi
done
