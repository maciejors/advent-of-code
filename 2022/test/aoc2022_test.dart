import 'package:test/test.dart';
import 'package:aoc2022/math.dart';

void main() {
  test('least common multiple', () {
    expect(findLeastCommonMultiple([6, 4, 6]), 12);
    expect(findLeastCommonMultiple([8, 9, 21]), 504);
    expect(findLeastCommonMultiple([21, 6]), 42);
    expect(findLeastCommonMultiple([4, 6]), 12);
    expect(findLeastCommonMultiple([4, 7, 12, 21, 42]), 84);
  });
}
