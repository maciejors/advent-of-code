import 'package:aoc2022/read_input.dart';

void main() async {
  List<String> rawInput = await readInput('./bin/day07/input.txt');
  // task one & generating a directory tree
  int resultTaskOne = 0;
  var rootDir = MyDirectory.newRoot();
  var currDir = rootDir;
  for (var line in rawInput.sublist(1)) {
    if (line.startsWith('\$')) {
      var splitInput = line.split(' ');
      String command = splitInput[1];
      if (command == 'cd') {
        String newDirName = splitInput[2];
        if (newDirName == '..') {
          int dirSize = currDir.getSize();
          if (dirSize <= 100000) {
            resultTaskOne += dirSize;
          }
          currDir = currDir.getParent();
        } else if (newDirName == '/') {
          currDir = rootDir;
        } else {
          currDir = currDir.getContents().firstWhere(
                  (item) => item.isDirectory() && item.name == newDirName)
              as MyDirectory;
        }
      }
    } else {
      // ls output
      ItemInMemory itemInMemory = ItemInMemory.parse(currDir, line);
      currDir.getContents().add(itemInMemory);
    }
  }
  // print(resultTaskOne);

  // task two
  int totalDiskSpace = 70000000;
  int requiredUnusedSpace = 30000000;
  int usedSpace = rootDir.getSize();
  int currUnusedSpace = totalDiskSpace - usedSpace;
  currDir = rootDir;
  Map<MyDirectory, int> candidatesToDelete = {};
  findCandidatesToDelete(MyDirectory startDir) {
    Map<MyDirectory, int> directoriesBigEnoughWithSizes = Map.fromEntries(
        startDir
            .getContents()
            .where((item) => item.isDirectory())
            .cast<MyDirectory>()
            .map((item) => MapEntry(item, item.getSize()))
            .where((entry) =>
                currUnusedSpace + entry.value >= requiredUnusedSpace));
    candidatesToDelete.addAll(directoriesBigEnoughWithSizes);
    for (var entry in directoriesBigEnoughWithSizes.entries) {
      findCandidatesToDelete(entry.key);
    }
  }

  findCandidatesToDelete(rootDir);
  var dirToDelete = candidatesToDelete.entries.reduce((smallest, nextDir) =>
      nextDir.value < smallest.value ? nextDir : smallest).key;
  print(dirToDelete);
}

abstract class ItemInMemory {
  abstract final String name;

  int getSize();
  bool isDirectory();
  List<ItemInMemory> getContents();
  MyDirectory getParent();

  static ItemInMemory parse(MyDirectory parentDir, String input) {
    if (input.startsWith('dir')) {
      return MyDirectory.empty(input.split(' ')[1], parentDir);
    } else {
      var regex = RegExp('(\\d+) (.+)');
      var match = regex.firstMatch(input)!;
      int fileSize = int.parse(match.group(1)!);
      String fileName = match.group(2)!;
      return MyFile(fileName, parentDir, fileSize);
    }
  }
}

class MyFile extends ItemInMemory {
  @override
  final String name;
  final MyDirectory _parent;
  final int _size;

  MyFile(this.name, this._parent, this._size);

  @override
  int getSize() {
    return _size;
  }

  @override
  List<ItemInMemory> getContents() {
    return [];
  }

  @override
  bool isDirectory() {
    return false;
  }

  @override
  MyDirectory getParent() {
    return _parent;
  }

  @override
  String toString() {
    return 'file: $name, size: $_size}';
  }
}

class MyDirectory extends ItemInMemory {
  @override
  final String name;
  final MyDirectory? _parent;
  final List<ItemInMemory> _contents;

  MyDirectory(this.name, this._parent, this._contents);

  MyDirectory.empty(this.name, this._parent) : _contents = [];

  MyDirectory.newRoot()
      : name = '/',
        _parent = null,
        _contents = [];

  @override
  List<ItemInMemory> getContents() {
    return _contents;
  }

  @override
  int getSize() {
    int totalSize = 0;
    for (var item in _contents) {
      totalSize += item.getSize();
    }
    return totalSize;
  }

  @override
  bool isDirectory() {
    return true;
  }

  @override
  MyDirectory getParent() {
    // _parent == null => root dir
    return _parent ?? this;
  }

  @override
  String toString() {
    return 'dir: $name, size: ${getSize()}';
  }
}
