import 'package:aoc2022/math.dart';
import 'package:aoc2022/read_input.dart';

void main() async {
  List<String> rawInput = await readInput('./bin/day01/input.txt');
  taskOne(rawInput);
  taskTwo(rawInput);
}

void taskOne(List<String> input) {
  int maxElfCal = 0;
  int currElfCal = 0;
  for (var line in input) {
    if (line.isNotEmpty) {
      int cal = int.parse(line);
      currElfCal += cal;
    }
    else {
      if (currElfCal > maxElfCal) {
        maxElfCal = currElfCal;
      }
      currElfCal = 0;
    }
  }
  print(maxElfCal);
}

void taskTwo(List<String> input) {
  // cumulative sum
  List<int> totalCalPerElf = [];
  int currElfCal = 0;
  for (var line in input) {
    if (line.isNotEmpty) {
      int cal = int.parse(line);
      currElfCal += cal;
    }
    else {
      totalCalPerElf.add(currElfCal);
      currElfCal = 0;
    }
  }
  // the last one
  totalCalPerElf.add(currElfCal);
  
  // find the sum of the top three
  int topThreeSum = 0;
  List<int> usedIds = [];
  for (int i = 0; i < 3; i++) {
    int currMaxId = findIdOfMax(totalCalPerElf, usedIds);
    int currMax = totalCalPerElf[currMaxId];
    topThreeSum += currMax;
    usedIds.add(currMaxId);
  }
  
  print(topThreeSum);
}
