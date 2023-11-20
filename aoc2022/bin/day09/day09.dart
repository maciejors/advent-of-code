import 'dart:io';

import 'package:aoc2022/coordinates.dart';
import 'package:aoc2022/read_input.dart';

void main() async {
  List<String> rawInput = await readInput('./bin/day09/input.txt');
  // taskOne(rawInput);
  taskTwo(rawInput);
}

void taskOne(List<String> input) {
  Position tailPos = Position(0, 0);
  Position headPos = Position(0, 0);
  Set<Position> visitedPositionsByTail = {tailPos.copy()};
  for (var line in input) {
    // e.g. ['R', 4]
    var instruction = line.split(' ');
    var direction = instruction[0];
    var steps = int.parse(instruction[1]);
    var deltaX = getDeltaX(direction);
    var deltaY = getDeltaY(direction);
    for (int i = 0; i < steps; i++) {
      Position prevHeadPos = headPos.copy();
      headPos.x += deltaX;
      headPos.y += deltaY;
      if (!headPos.isAdjacentTo(tailPos)) {
        tailPos = prevHeadPos;
      }
      visitedPositionsByTail.add(tailPos);
    }
  }
  print(visitedPositionsByTail.length);
}

void taskTwo(List<String> input) {
  List<Position> knotsPositions = [];
  for (int i = 0; i < 10; i++) {
    knotsPositions.add(Position(0, 0));
  }
  Position headPos = knotsPositions.first;
  Position tailPos = knotsPositions.last;
  Set<Position> visitedPositionsByTail = {tailPos.copy()};
  for (var line in input) {
    // e.g. ['R', 4]
    var instruction = line.split(' ');
    var direction = instruction[0];
    var steps = int.parse(instruction[1]);
    var deltaX = getDeltaX(direction);
    var deltaY = getDeltaY(direction);
    for (int i = 0; i < steps; i++) {
      headPos.x += deltaX;
      headPos.y += deltaY;
      for (int i = 1; i < knotsPositions.length; i++) {
        var prevKnotPos = knotsPositions[i - 1];
        var currKnotPos = knotsPositions[i];
        if (prevKnotPos.isAdjacentTo(currKnotPos)) {
          break;
        }
        // += 1 if prevKnot is above, otherwise -1
        currKnotPos.y += prevKnotPos.y.compareTo(currKnotPos.y);
        // += 1 if prevKnot is to the right, otherwise -1
        currKnotPos.x += prevKnotPos.x.compareTo(currKnotPos.x);
      }
      visitedPositionsByTail.add(tailPos.copy());
      // print(line);
      // drawKnotsPositions(knotsPositions, -11, 14, -5, 15);
      // stdin.readLineSync();
    }
  }
  print(visitedPositionsByTail.length);
}

void drawKnotsPositions(List<Position> knotsPositions, int minX, int maxX, int minY, int maxY, {bool hideIds = false}) {
  for (int y = maxY; y >= minY; y--) {
    String line = "";
    for (int x = minX; x <= maxX; x++) {
      int idx = knotsPositions.indexOf(Position(x, y));
      if (idx == -1) {
        line += '.';
      } else if (hideIds) {
        line += '#';
      } else {
        line += idx.toString();
      }
    }
    print(line);
  }
}

int getDeltaX(String direction) {
  if (direction == 'R') {
    return 1;
  } else if (direction == 'L') {
    return -1;
  } else {
    return 0;
  }
}

int getDeltaY(String direction) {
  if (direction == 'U') {
    return 1;
  } else if (direction == 'D') {
    return -1;
  } else {
    return 0;
  }
}
