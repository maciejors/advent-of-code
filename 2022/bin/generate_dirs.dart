import 'dart:io';

void main() async {
  String basePath = './bin';
  for (int i = 1; i <= 25; i++) {
    String daySuffix = i.toString().padLeft(2, '0');
    var dir = Directory('$basePath/day$daySuffix');
    if (!dir.existsSync()) {
      dir.createSync();
      File('${dir.path}/day$daySuffix.dart')
          .writeAsStringSync(getBaseCodeSnippet(daySuffix), flush: true);
      File('${dir.path}/input.txt').createSync();
      File('${dir.path}/input_example.txt').createSync();
    }
  }
}

String getBaseCodeSnippet(String daySuffix) {
  return """import 'package:aoc2022/read_input.dart';

void main() async {
  List<String> rawInput = await readInput('./bin/day$daySuffix/input_example.txt');
  taskOne(rawInput);
  taskTwo(rawInput);
}

void taskOne(List<String> input) {
}

void taskTwo(List<String> input) {
}
""";
}