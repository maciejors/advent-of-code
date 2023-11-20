import 'package:aoc2022/read_input.dart';

void main() async {
  List<String> rawInput = await readInput('./bin/day04/input.txt');
  taskOne(rawInput);
  taskTwo(rawInput);
}

void taskOne(List<String> input) {
  List<List<int>> ranges = input.map((line) {
    return line.split(RegExp('[,-]')).map(int.parse).toList();
  }).toList();

  int nOfFullyContained = 0;

  for (List<int> rangePair in ranges) {
    if ((rangePair[0] <= rangePair[2] && rangePair[1] >= rangePair[3]) ||
        (rangePair[0] >= rangePair[2] && rangePair[1] <= rangePair[3])) {
      nOfFullyContained++;
    }
  }

  print(nOfFullyContained);
}

void taskTwo(List<String> input) {
  List<List<int>> ranges = input.map((line) {
    return line.split(RegExp('[,-]')).map(int.parse).toList();
  }).toList();

  int nOfOverlapping = 0;

  for (List<int> rangePair in ranges) {
    if ((rangePair[0] >= rangePair[2] && rangePair[0] <= rangePair[3]) ||
        (rangePair[1] >= rangePair[2] && rangePair[1] <= rangePair[3]) ||
        (rangePair[2] >= rangePair[0] && rangePair[2] <= rangePair[1])) {
      nOfOverlapping++;
    }
  }

  print(nOfOverlapping);
}
