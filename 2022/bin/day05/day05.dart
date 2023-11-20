import 'package:aoc2022/read_input.dart';

void main() async {
  List<String> rawInput = await readInput('./bin/day05/input.txt');
  taskOne(rawInput);
  taskTwo(rawInput);
}

void taskOne(List<String> input) {
  List<List<String>> crateLayout = parseCrateLayout(input);
  List<List<int>> instructions = parseInstructions(input);

  for (var instruction in instructions) {
    int howMany = instruction[0];
    int fromId = instruction[1] - 1;
    int toId = instruction[2] - 1;

    for (int i = 0; i < howMany; i++) {
      var crate = crateLayout[fromId].removeLast();
      crateLayout[toId].add(crate);
    }
  }

  String result = '';

  for (var stack in crateLayout) {
    result += stack.last;
  }

  print(result);
}

void taskTwo(List<String> input) {
  List<List<String>> crateLayout = parseCrateLayout(input);
  List<List<int>> instructions = parseInstructions(input);

  for (var instruction in instructions) {
    int howMany = instruction[0];
    int fromId = instruction[1] - 1;
    int toId = instruction[2] - 1;

    var stackFrom = crateLayout[fromId];
    var stackFromLength = stackFrom.length;
    // get the crates off the stack
    var cratesToMove =
        stackFrom.getRange(stackFromLength - howMany, stackFromLength).toList();
    stackFrom.removeRange(stackFromLength - howMany, stackFromLength);
    // put them elsewhere
    crateLayout[toId].addAll(cratesToMove);
  }

  String result = '';

  for (var stack in crateLayout) {
    result += stack.last;
  }

  print(result);
}

List<List<String>> parseCrateLayout(List<String> rawInput) {
  List<String> rawCrateLayout = [];

  for (String line in rawInput) {
    // the crate layout and instructions are separated by a blank line
    if (line.isEmpty) {
      break;
    }
    rawCrateLayout.add(line);
  }

  String lastLine = rawCrateLayout.removeLast();
  // max stack number in the last line
  int nOfStacks = RegExp('\\d')
      .allMatches(lastLine)
      .map((e) => int.parse(e.group(0)!))
      .reduce((value, element) => element > value ? element : value);

  List<List<String>> result = [];
  // fill the result list with empty lists representing each stack
  for (var i = 0; i < nOfStacks; i++) {
    result.add([]);
  }

  for (var line in rawCrateLayout) {
    // characters representing crates are in positions
    // 1, 5, 9, ..., 4k+1, ... in the string
    for (var i = 0; 4 * i + 1 < line.length; i++) {
      String crate = line[4 * i + 1];
      if (crate != ' ') {
        result[i].insert(0, line[4 * i + 1]);
      }
    }
  }

  return result;
}

List<List<int>> parseInstructions(List<String> rawInput) {
  List<List<int>> result = [];

  bool crateLayoutRead = false;
  for (String line in rawInput) {
    // the crate layout and instructions are separated by a blank line
    if (line.isEmpty) {
      crateLayoutRead = true;
    } else if (crateLayoutRead) {
      // a line with an instruction
      var regex = RegExp('move (\\d+) from (\\d+) to (\\d+)');
      var match = regex.firstMatch(line)!;
      // [<move>, <from>, <to>]
      var instruction =
          match.groups([1, 2, 3]).map((e) => int.parse(e!)).toList();
      result.add(instruction);
    }
  }
  return result;
}
