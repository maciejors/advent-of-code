import 'package:aoc2022/coordinates.dart';
import 'package:aoc2022/read_input.dart';

void main() async {
  List<String> rawInput = await readInput('./bin/day10/input.txt');
  // taskOne(rawInput);
  taskTwo(rawInput);
}

void taskOne(List<String> input) {
  int result = 0;
  int x = 1;
  int cycleNo = 0;
  Set<int> breakPoints = {20, 60, 100, 140, 180, 220};
  for (var line in input) {
    if (line == 'noop') {
      cycleNo++;
      if (breakPoints.contains(cycleNo)) {
        result += cycleNo * x;
      }
    } else {
      int addxValue = int.parse(line.split(' ')[1]);
      for (int i = 0; i < 2; i++) {
        cycleNo++;
        if (breakPoints.contains(cycleNo)) {
          result += cycleNo * x;
        }
      }
      x += addxValue;
    }
  }
  print(result);
}

void taskTwo(List<String> input) {
  int spriteCenter = 1;
  int cycleNo = 0;
  List<Position> brightPixels = [];
  int currInstructionId = 0;
  // timeToCompleteCurrentInstruction:
  // >0 -> wait
  // ==0 -> execute current
  // -1 -> queue a new one
  int timeToCompleteCurrentInstruction = 1;
  while (currInstructionId < input.length) {
    cycleNo++;
    int x = (cycleNo - 1) % 40;
    int y = (cycleNo - 1) ~/ 40;
    // during a cycle: drawing a pixel:
    if ((x - spriteCenter).abs() <= 1) {
      brightPixels.add(Position(x, y));
    }
    var instruction = input[currInstructionId];
    // end of cycle: completing an operation
    if (instruction == 'noop') {
      if (timeToCompleteCurrentInstruction == -1) {
        timeToCompleteCurrentInstruction = 0;
      }
    } else {  // instruction == 'addx <number>'
      if (timeToCompleteCurrentInstruction == -1) {
        timeToCompleteCurrentInstruction = 1;
      } else if (timeToCompleteCurrentInstruction == 0) {
        int addxValue = int.parse(instruction.split(' ')[1]);
        spriteCenter += addxValue;
      }
    }
    timeToCompleteCurrentInstruction--;
    if (timeToCompleteCurrentInstruction == -1) {
      currInstructionId++;
    }
  }
  drawImage(brightPixels);
}


void drawImage(List<Position> brightPixels) {
  for (int y = 0; y < 6; y++) {
    String line = "";
    for (int x = 0; x < 40; x++) {
      int idx = brightPixels.indexOf(Position(x, y));
      line += idx == -1 ? ' ' : '#';
    }
    print(line);
  }
}
