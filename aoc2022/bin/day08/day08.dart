import 'package:aoc2022/read_input.dart';

void main() async {
  List<String> rawInput = await readInput('./bin/day08/input.txt');
  // taskOne(rawInput);
  taskTwo(rawInput);
}

void taskOne(List<String> input) {
  // y1: [x1, x2, ...], y2: [x3, x4, ...], ...
  Map<int, Set<int>> visibleTrees = {};
  for (var direction in Direction.values) {
    var newTrees = getVisibleFromDirection(input, direction);
    for (var entry in newTrees.entries) {
      visibleTrees.putIfAbsent(entry.key, () => <int>{});
      visibleTrees[entry.key]!.addAll(entry.value);
    }
  }
  int visibleCount = 0;
  for (var entry in visibleTrees.entries) {
    visibleCount += entry.value.length;
  }
  print(visibleCount);
}

Map<int, Set<int>> getVisibleFromDirection(
    List<String> input, Direction direction) {
  Map<int, Set<int>> result = {};
  getTreeHeight(x, y) {
    // if direction is horizontal, the input can be transposed and
    // then the bottom direction corresponds to the right, while the top
    // can be treated as the left
    String heightStr;
    if (direction == Direction.top || direction == Direction.bottom) {
      heightStr = input[x][y];
    } else {
      heightStr = input[y][x];
    }
    return int.parse(heightStr);
  }
  int startX = 0;
  int deltaX = 1;
  if (direction == Direction.bottom || direction == Direction.right) {
    startX = input.length - 1;
    deltaX = -1;
  }
  for (int y = 0; y < input.length; y++) {
    int highestSoFar = -1;
    for (int x = startX; 0 <= x && x < input.length; x += deltaX) {
      int currTreeHeight = getTreeHeight(x, y);
      if (currTreeHeight > highestSoFar) {
        highestSoFar = currTreeHeight;
        if (direction == Direction.top || direction == Direction.bottom) {
          result.putIfAbsent(x, () => <int>{});
          result[x]!.add(y);
        } else {
          result.putIfAbsent(y, () => <int>{});
          result[y]!.add(x);
        }
      }
    }
  }
  return result;
}

enum Direction { left, right, top, bottom }

void taskTwo(List<String> input) {
  int maxScenicScore = 0;
  for (int y = 0; y < input.length; y++) {
    for (int x = 0; x < input.length; x++) {
      int score = calculateScenicScore(input, x, y);
      if (score > maxScenicScore) {
        maxScenicScore = score;
      }
    }
  }
  print(maxScenicScore);
}

int calculateScenicScore(List<String> input, int x, int y) {
  int score = 1;
  int treeHeight = int.parse(input[y][x]);
  for (var direction in Direction.values) {
    int xDelta = 0;
    int yDelta = 0;
    switch (direction) {
      case Direction.top:
        yDelta = 1;
        break;
      case Direction.left:
        xDelta = 1;
        break;
      case Direction.right:
        xDelta = -1;
        break;
      case Direction.bottom:
        yDelta = -1;
        break;
    }
    int x2 = x + xDelta;
    int y2 = y + yDelta;
    int visibleTreesCount = 0;
    while (0 <= y2 && y2 < input.length &&
        0 <= x2 && x2 < input.length) {
      int currTreeHeight = int.parse(input[y2][x2]);
      visibleTreesCount++;
      if (currTreeHeight >= treeHeight) {
        break;
      }
      x2 += xDelta;
      y2 += yDelta;
    }
    score *= visibleTreesCount;
  }
  return score;
}
