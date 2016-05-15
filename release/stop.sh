#! /bin/sh -e
touch pidFile
kill -9 $(<pidFile) || echo 'not alive, run "ps -x" to check running process, or check in "log/pid.log" file'