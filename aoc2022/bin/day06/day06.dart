import 'package:aoc2022/read_input.dart';

void main() async {
  List<String> rawInput = await readInput('./bin/day06/input.txt');
  taskOne(rawInput[0]);
  taskTwo(rawInput[0]);
}

void taskOne(String input) {
  int result = -1;
  Map<String, int> characterCounts = {};
  // the first four characters will be processed manually
  for (var i = 0; i < 4; i++) {
    var char = input[i];
    characterCounts.update(char, (value) => value + 1, ifAbsent: () => 1);
  }
  // if all first four characters are different from one another, return 4
  if (characterCounts.keys.length == 4) {
    print(4);
    return;
  }
  // the remainder of the input will be processed now
  for (var i = 4; i < input.length; i++) {
    var charToDrop = input[i - 4];
    var charToAdd = input[i];
    // add a new char
    characterCounts.update(charToAdd, (value) => value + 1, ifAbsent: () => 1);
    // decrement the count of the char to drop, or remove it if the count was 1
    if (characterCounts[charToDrop] == 1) {
      characterCounts.remove(charToDrop);
    } else {
      characterCounts.update(charToDrop, (value) => value - 1);
    }
    // check if all 4 characters are unique
    if (characterCounts.keys.length == 4) {
      print(i + 1);
      return;
    }
  }
}

void taskTwo(String input) {
  int result = -1;
  Map<String, int> characterCounts = {};
  // the first fourteen characters will be processed manually
  for (var i = 0; i < 14; i++) {
    var char = input[i];
    characterCounts.update(char, (value) => value + 1, ifAbsent: () => 1);
  }
  // if all first fourteen characters are different from one another, return 4
  if (characterCounts.keys.length == 14) {
    print(4);
    return;
  }
  // the remainder of the input will be processed now
  for (var i = 14; i < input.length; i++) {
    var charToDrop = input[i - 14];
    var charToAdd = input[i];
    // add a new char
    characterCounts.update(charToAdd, (value) => value + 1, ifAbsent: () => 1);
    // decrement the count of the char to drop, or remove it if the count was 1
    if (characterCounts[charToDrop] == 1) {
      characterCounts.remove(charToDrop);
    } else {
      characterCounts.update(charToDrop, (value) => value - 1);
    }
    // check if all 14 characters are unique
    if (characterCounts.keys.length == 14) {
      print(i + 1);
      return;
    }
  }
}
