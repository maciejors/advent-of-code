import 'dart:convert';
import 'dart:io';

Future<List<String>> readInput(String path) async {
  List<String> result = [];
  // https://api.dart.dev/stable/2.18.5/dart-io/File-class.html
  final file = File(path);
  Stream<String> lines = file.openRead()
      .transform(utf8.decoder)       // Decode bytes to UTF-8.
      .transform(LineSplitter());    // Convert stream to individual lines.
  return await lines.toList();
}