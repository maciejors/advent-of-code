import 'coordinates.dart';

Map<Node, int> dijkstra(
    List<Node> allNodes, Node initialNode, List<Node> nodesToReturn) {
  // Dijkstra's algorithm
  Set<Node> unvisitedSet = {};
  Set<Node> requiredNodesYetToVisit = nodesToReturn.toSet();
  Map<Node, int> tentativeDistances = {};
  int inf = 99999999;
  for (var node in allNodes) {
    unvisitedSet.add(node);
    tentativeDistances[node] = inf;
  }
  tentativeDistances[initialNode] = 0;
  var currNode = initialNode;

  while (requiredNodesYetToVisit.isNotEmpty) {
    int aaa = requiredNodesYetToVisit.length;
    int newDistance = tentativeDistances[currNode]! + 1;

    for (var neighbour in currNode.neighbours) {
      if (!unvisitedSet.contains(neighbour)) {
        continue;
      }
      if (newDistance < tentativeDistances[neighbour]!) {
        tentativeDistances[neighbour] = newDistance;
      }
    }

    unvisitedSet.remove(currNode);
    requiredNodesYetToVisit.remove(currNode);

    int smallestDistance = inf;
    for (var node in unvisitedSet) {
      int nodeDist = tentativeDistances[node]!;
      if (nodeDist < smallestDistance) {
        currNode = node;
        smallestDistance = nodeDist;
      }
    }
    if (smallestDistance == inf) {  // remaining nodes are not reachable
      break;
    }
  }

  return Map.fromEntries(
      tentativeDistances.entries.where((e) => nodesToReturn.contains(e.key)));
}

class Node extends Position {
  List<Node> neighbours = [];
  int height;

  Node(super.x, super.y, this.height);
}
