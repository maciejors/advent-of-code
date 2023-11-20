import 'package:aoc2022/read_input.dart';

void main() async {
  List<String> rawInput = await readInput('./bin/day03/input.txt');
  // taskOne(rawInput);
  taskTwo(rawInput);
}

void taskOne(List<String> input) {
  var prioritiesByRucksack = convertItemsToPriorities(input);
  int sumOfCommonItemsPriorities = 0;
  for (List<int> rucksack in prioritiesByRucksack) {
    // searching for a common element in two sorted halves of the list
    // (there is always one per rucksack)
    int lengthOfHalf = rucksack.length ~/ 2;
    int pointerLeftHalf = 0;
    int pointerRightHalf = lengthOfHalf;
    int itemLeftHalf = rucksack[pointerLeftHalf];
    int itemRightHalf = rucksack[pointerRightHalf];

    while (itemLeftHalf != itemRightHalf) {
      if (itemLeftHalf < itemRightHalf) {
        pointerLeftHalf++;
        itemLeftHalf = rucksack[pointerLeftHalf];
      } else {
        pointerRightHalf++;
        itemRightHalf = rucksack[pointerRightHalf];
      }
    }
    sumOfCommonItemsPriorities += itemRightHalf;
  }

  print(sumOfCommonItemsPriorities);
}

void taskTwo(List<String> input) {
  var prioritiesByRucksack = convertItemsToPriorities(input);
  int sumOfBadgesPriorities = 0;
  for (int i = 0; i < prioritiesByRucksack.length; i += 3) {
    var rucksack1 = prioritiesByRucksack[i];
    var rucksack2 = prioritiesByRucksack[i + 1];
    var rucksack3 = prioritiesByRucksack[i + 2];
    // pointers for each rucksack
    int pointer1 = 0;
    int pointer2 = 0;
    int pointer3 = 0;
    int item1 = rucksack1[pointer1];
    int item2 = rucksack2[pointer2];
    int item3 = rucksack3[pointer3];
    while (item1 != item2 || item2 != item3) {
      // first find the common item in r1 and r2 and then search for it in r3
      if (item1 < item2) {
        pointer1++;
        item1 = rucksack1[pointer1];
      } else if (item1 > item2) {
        pointer2++;
        item2 = rucksack2[pointer2];
      } else { // item1 == item2
        if (item3 < item2) {
          pointer3++;
          item3 = rucksack3[pointer3];
        } else if (item3 > item2) {  // <=> item2 is not present in rucksack3
          pointer1++;
          item1 = rucksack1[pointer1];
        }
      }
    }
    sumOfBadgesPriorities += item1;
  }
  print(sumOfBadgesPriorities);
}

List<List<int>> convertItemsToPriorities(List<String> input) {
  // it also sorts the items within a compartment
  return input.map((rucksack) {
    List<int> priorities = rucksack.codeUnits.map((asciiCode) {
      if (asciiCode >= 97) {  // a-z
        return asciiCode - 96;  // 1-26
      } else {  // A-Z
        return asciiCode - 38;  // 27-52
      }
    }).toList();

    // sort within a compartment (pt. 1)
    // int n = priorities.length;
    // List<int> firstCompartment = priorities.sublist(0, n ~/ 2);
    // List<int> secondCompartment = priorities.sublist(n ~/ 2);
    // firstCompartment.sort();
    // secondCompartment.sort();
    // // merge back the compartments
    // return firstCompartment + secondCompartment;

    // sort entirely (pt. 2)
    priorities.sort();
    return priorities;
  }).toList();
}
