import 'package:aoc2022/math.dart';
import 'package:aoc2022/read_input.dart';

void main() async {
  List<String> rawInput = await readInput('./bin/day11/input.txt');

  List<Monkey> monkeys = [];
  List<String> singleMonkeyInput = [];
  for (var line in rawInput) {
    if (line.isEmpty) {
      monkeys.add(parseMonkey(singleMonkeyInput));
      singleMonkeyInput.clear();
    } else {
      singleMonkeyInput.add(line);
    }
  }
  // the last one
  monkeys.add(parseMonkey(singleMonkeyInput));

  // taskOne(monkeys);
  taskTwo(monkeys);
}

void taskOne(List<Monkey> monkeys) {
  List<int> inspectedCountByMonkey = monkeys.map((e) => 0).toList();
  for (int round = 1; round <= 20; round++) {
    for (var monkey in monkeys) {
      for (var item in monkey.items) {
        item = monkey.executeOperation(item) ~/ 3;
        int nextId = monkey.getNextMonkeyId(item);
        monkeys[nextId].items.add(item);
        inspectedCountByMonkey[monkey.id]++;
      }
      monkey.items.clear();
    }
  }
  inspectedCountByMonkey.sort((a, b) => b.compareTo(a));
  int monkeyBusiness = inspectedCountByMonkey[0] * inspectedCountByMonkey[1];
  print(monkeyBusiness);
}

void taskTwo(List<Monkey> monkeys) {
  List<int> inspectedCountByMonkey = monkeys.map((e) => 0).toList();

  // to make the worry levels manageable, all operations will be done
  // in a modulo leastCommonMultiple of monkeys test numbers
  List<int> testNumbers = monkeys.map((monkey) => monkey.testNumber).toList();
  int leastCommonMultiple = findLeastCommonMultiple(testNumbers);

  for (int round = 1; round <= 10000; round++) {
    for (var monkey in monkeys) {
      for (var item in monkey.items) {
        item = monkey.executeOperation(item) % leastCommonMultiple;
        int nextId = monkey.getNextMonkeyId(item);
        monkeys[nextId].items.add(item);
        inspectedCountByMonkey[monkey.id]++;
      }
      monkey.items.clear();
    }
  }
  inspectedCountByMonkey.sort((a, b) => b.compareTo(a));
  int monkeyBusiness = inspectedCountByMonkey[0] * inspectedCountByMonkey[1];
  print(monkeyBusiness);
}

Monkey parseMonkey(List<String> rawInput) {
  var numberRegex = RegExp('(\\d+)');
  var operationRegex = RegExp('Operation: (.+)');

  var idMatch = numberRegex.firstMatch(rawInput[0])!;
  var startingItemsMatch = numberRegex.allMatches(rawInput[1]);
  var operationMatch = operationRegex.firstMatch(rawInput[2])!;
  var testNumberMatch = numberRegex.firstMatch(rawInput[3])!;
  var testPassedMatch = numberRegex.firstMatch(rawInput[4])!;
  var testFailedMatch = numberRegex.firstMatch(rawInput[5])!;

  int id = int.parse(idMatch.group(1)!);
  List<int> startingItems =
      startingItemsMatch.map((match) => int.parse(match.group(1)!)).toList();
  String operation = operationMatch.group(1)!;
  int testNumber = int.parse(testNumberMatch.group(1)!);
  int idIfTestPassed = int.parse(testPassedMatch.group(1)!);
  int idIfTestFailed = int.parse(testFailedMatch.group(1)!);
  return Monkey(
      id, startingItems, operation, testNumber, idIfTestPassed, idIfTestFailed);
}

class Monkey {
  int id;
  List<int> items;
  final String _operation;
  final int testNumber;
  final int _monkeyIdIfTestPassed;
  final int _monkeyIdIfTestFailed;

  Monkey(this.id, this.items, this._operation, this.testNumber,
      this._monkeyIdIfTestPassed, this._monkeyIdIfTestFailed);

  int getNextMonkeyId(int item) {
    return item % testNumber == 0
        ? _monkeyIdIfTestPassed
        : _monkeyIdIfTestFailed;
  }

  int executeOperation(int item) {
    var regex = RegExp('new = old ([\\*\\+]) (.+)');
    var match = regex.firstMatch(_operation)!;
    String operand = match.group(1)!;
    String secondValRaw = match.group(2)!;
    int secondVal;
    if (secondValRaw == 'old') {
      secondVal = item;
    } else {
      secondVal = int.parse(secondValRaw);
    }
    if (operand == '+') {
      return item + secondVal;
    } else {
      return item * secondVal;
    }
  }

  @override
  String toString() {
    return 'Monkey $id:\n'
        '\tItems: $items\n'
        '\tOperation: $_operation\n'
        '\tTest: divisible by $testNumber\n'
        '\t\tIf true: throw to monkey $_monkeyIdIfTestPassed\n'
        '\t\tIf false: throw to monkey $_monkeyIdIfTestFailed\n';
  }
}
