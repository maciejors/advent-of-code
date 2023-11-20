def main():
    forest = list()
    with open('input.txt') as file:
        forest.extend(file.read().split('\n'))
    puzzle1(forest)
    puzzle2(forest)


def puzzle1(forest):
    x = 0
    n_of_trees = 0
    map_width = len(forest[0])
    for layer in forest:
        if x >= map_width:
            x = x % map_width
        if layer[x] == '#':
            n_of_trees += 1
        x += 3
    print(n_of_trees)


def puzzle2(forest):
    print(calc_number_of_trees(forest, 1, 1) * calc_number_of_trees(forest, 3, 1) * calc_number_of_trees(forest, 5, 1)
          * calc_number_of_trees(forest, 7, 1) * calc_number_of_trees(forest, 1, 2))


def calc_number_of_trees(forest, x_diff, y_diff):
    x = 0
    y = 0
    n_of_trees = 0
    map_width = len(forest[0])
    forest_depth = len(forest)
    while y < forest_depth:
        if x >= map_width:
            x = x % map_width
        if forest[y][x] == '#':
            n_of_trees += 1
        x += x_diff
        y += y_diff
    return n_of_trees


if __name__ == '__main__':
    main()
