import 'package:aoc2022/coordinates.dart';
import 'package:aoc2022/read_input.dart';
import 'package:aoc2022/dijkstra.dart';

void main() async {
  List<String> rawInput = await readInput('./bin/day12/input.txt');
  List<List<int>> heights = rawInput
      .map((line) => line.codeUnits.map((e) => e - 97).toList())
      .toList();
  // heights vary from 0 to 25 except for the starting point (-14) and the destination (-28)
  // STARTING NODE AND DESTINATION NODE WILL BE REVERSED IN THE ALGORITHM
  // (the algorithm will find the shortest path by backtracking)
  Node? startingNode;
  Node? destinationNode;
  List<Node> allNodes = [];
  List<List<Node>> nodesMap = [];
  // populate nodesMap
  for (int y = 0; y < heights.length; y++) {
    nodesMap.add([]);
    for (int x = 0; x < heights[y].length; x++) {
      var node = Node(x, y, heights[y][x]);
      allNodes.add(node);
      nodesMap[y].add(node);
      if (node.height == -14) {
        startingNode = node;
        node.height = 0;
      } else if (node.height == -28) {
        destinationNode = node;
        node.height = 25;
      }
    }
  }
  // fill each node's neighbours
  for (var node in allNodes) {
    List<Position> adjacentPositions = [
      Position(node.x, node.y + 1),
      Position(node.x + 1, node.y),
      Position(node.x, node.y - 1),
      Position(node.x - 1, node.y),
    ];
    for (var adjacentPos in adjacentPositions) {
      if (adjacentPos.x < 0 ||
          adjacentPos.x >= heights[0].length ||
          adjacentPos.y < 0 ||
          adjacentPos.y >= heights.length) {
        continue;
      }
      var adjacentNode = nodesMap[adjacentPos.y][adjacentPos.x];
      if (node.height - adjacentNode.height <= 1) {
        // backtracking condition
        node.neighbours.add(adjacentNode);
      }
    }
  }
  // taskOne(allNodes, startingNode!, destinationNode!);
  taskTwo(allNodes, startingNode!, destinationNode!);
}

void taskOne(List<Node> allNodes, Node startingNode, Node destinationNode) {
  print(dijkstra(allNodes, destinationNode, [startingNode]));
}

void taskTwo(List<Node> allNodes, Node startingNode, Node destinationNode) {
  List<Node> nodesToReturn =
      allNodes.where((node) => node.height == 0).toList();

  var shortestPathsLengths = dijkstra(allNodes, destinationNode, nodesToReturn);
  print(shortestPathsLengths.values
      .reduce((value, element) => element < value ? element : value));
}
