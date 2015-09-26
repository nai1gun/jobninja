#!/usr/bin/env bash
pid=`lsof -i -n -P | grep TCP | grep 8080 | awk '{print $2}'`;
if [ $pid ];
then
kill $pid;
echo "Process $pid killed";
else echo "No previous app launched";
fi
