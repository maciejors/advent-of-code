import 'package:aoc2022/read_input.dart';

void main() async {
  List<String> rawInput = await readInput('./bin/day02/input.txt');
  getTotalPoints(rawInput);
}

void getTotalPoints(List<String> input) {
  List<RockPaperScissorsGame> games = input
      .map(RockPaperScissorsGame.parse)
      .toList()
      .cast<RockPaperScissorsGame>();
  int cumulativeScore = 0;
  for (var game in games) {
    cumulativeScore += game.calculatePoints();
  }
  print(cumulativeScore);
}

void taskTwo(List<String> input) {
}


class RockPaperScissorsGame {
  late Choice opponentMove;
  late Outcome desiredOutcome;
  // late Choice myMove;

  static final opponentMoves = {
    'A': Choice.rock,
    'B': Choice.paper,
    'C': Choice.scissors,
  };

  // static final myMoves = {
  //   'X': Choice.rock,
  //   'Y': Choice.paper,
  //   'Z': Choice.scissors,
  // };
  static final outcomes = {
    'X': Outcome.loss,
    'Y': Outcome.draw,
    'Z': Outcome.win,
  };

  static final winningStrategy = {
    // opponent: me
    Choice.rock: Choice.paper,
    Choice.paper: Choice.scissors,
    Choice.scissors: Choice.rock,
  };
  static final losingStrategy = {
    // opponent: me
    Choice.rock: Choice.scissors,
    Choice.paper: Choice.rock,
    Choice.scissors: Choice.paper,
  };

  static final pointsForChoice = {
    Choice.rock: 1,
    Choice.paper: 2,
    Choice.scissors: 3,
  };

  RockPaperScissorsGame.parse(String line) {
    var splitLine = line.split(' ');
    opponentMove = opponentMoves[splitLine[0]]!;
    desiredOutcome = outcomes[splitLine[1]]!;
  }

  Choice getMyMove() {
    if (desiredOutcome == Outcome.loss) {
      return losingStrategy[opponentMove]!;
    }
    if (desiredOutcome == Outcome.win) {
      return winningStrategy[opponentMove]!;
    }
    return opponentMove;
  }

  int calculatePoints() {
    int result = 0;

    Choice myMove = getMyMove();

    result += pointsForChoice[myMove]!;

    if (myMove == opponentMove) {
      result += 3;
    }
    if ((myMove == Choice.rock && opponentMove == Choice.scissors) ||
        (myMove == Choice.paper && opponentMove == Choice.rock) ||
        (myMove == Choice.scissors && opponentMove == Choice.paper)) {
      result += 6;
    }
    return result;
  }
}

enum Choice {rock, paper, scissors}
enum Outcome {loss, draw, win}
