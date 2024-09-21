#!/bin/bash

if [[ $1 == "" ]]; then
    echo Error: day identifier not provided.
    exit 1
fi

puzzle_dir=./src/com/maciejors/aoc21/day$1

if [[ -d $puzzle_dir ]]; then
    echo Error: directory $puzzle_dir already exists.
    exit 1
fi

mkdir $puzzle_dir
touch $puzzle_dir/input.txt
touch $puzzle_dir/example.txt

boilerplate_code=$(<$puzzle_dir/../boilerplate/DayXX.java)
# update package at the top of the file
boilerplate_code=${boilerplate_code//boilerplate/day$1}
# replace 'XX' with the day identifier
boilerplate_code=${boilerplate_code//XX/$1}

java_file_path=$puzzle_dir/Day$1.java

printf "$boilerplate_code" >> $java_file_path
git add $java_file_path