class Position {
  int x;
  int y;

  Position(this.x, this.y);

  Position copy() {
    return Position(x, y);
  }

  bool isAdjacentTo(Position pos2) {
    var diff = [x - pos2.x, y - pos2.y];
    return diff[0].abs() <= 1 && diff[1].abs() <= 1;
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
          other is Position && runtimeType == other.runtimeType &&
              x == other.x && y == other.y;

  @override
  int get hashCode => x.hashCode ^ y.hashCode;

  @override
  String toString() {
    return '(x: $x, y: $y)';
  }
}
