#!/bin/bash
if [[ $1 == "" ]]; then
    echo Error: day identifier not provided.
    exit 1
fi

puzzle_dir=./src/puzzles/day$1

if [[ -d $puzzle_dir ]]; then
    echo Error: directory $puzzle_dir already exists.
    exit 1
fi

mkdir $puzzle_dir
touch $puzzle_dir/input.txt
touch $puzzle_dir/example.txt

boilerplate_code="import { readInput } from '../../shared/inputReader';

export function day${1}puzzle1(): string {
  const lines = readInput('${1}', true);
  let result = 0;
  for (let line of lines) {
    
  }
  return \`\${result}\`;
}

export function day${1}puzzle2(): string {
  const lines = readInput('${1}', true);
  let result = 0;
  for (let line of lines) {
    
  }
  return \`\${result}\`;
}
"
printf "$boilerplate_code" >> $puzzle_dir/day$1.ts
