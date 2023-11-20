int findLeastCommonMultiple(List<int> numbers) {
  // algorithm: https://en.wikipedia.org/wiki/Least_common_multiple
  List<int> sequence = numbers.map((e) => e).toList();  // copy
  // while (not all elements are equal) {
  while (!sequence.every((element) => element == sequence[0])) {
    int minElement = sequence
        .reduce((value, element) => element < value ? element : value);
    int idOfMinElement = sequence.indexOf(minElement);
    sequence[idOfMinElement] += numbers[idOfMinElement];
  }
  return sequence[0];
}

int findIdOfMax(List<int> numbers, List<int> idsToIgnore) {
  int maxVal = 0;
  int maxId = 0;
  for (int i = 0; i < numbers.length; i++) {
    if (numbers[i] > maxVal && !idsToIgnore.contains(i)) {
      maxVal = numbers[i];
      maxId = i;
    }
  }
  return maxId;
}
