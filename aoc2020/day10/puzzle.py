from copy import copy


def main():
    data = list()
    with open('input.txt') as file:
        for line in file.readlines():
            data.append(int(line.replace('\n', '')))
    puzzle(data)


def puzzle(data):
    n_of_1_diff = 0
    n_of_3_diff = 0
    last_adapter = 0
    longest_way = list()
    for i in set(data):
        if i - last_adapter == 1:
            n_of_1_diff += 1
        elif i - last_adapter == 3:
            n_of_3_diff += 1
        elif i - last_adapter > 3:
            break
        longest_way.append(i)
        last_adapter = i
    n_of_3_diff += 1
    # print(n_of_1_diff * n_of_3_diff)
    longest_way = [0] + longest_way
    longest_way.append(longest_way[-1] + 3)
    diffs_array_reduced = list()
    curr = list()
    for i in range(len(longest_way) - 1):
        if longest_way[i + 1] - longest_way[i] == 1:
            curr.append(1)
        elif curr:
            if len(curr) > 1:
                diffs_array_reduced.append(curr)
            curr = list()
    n_of_arrangements = 1
    for ones_array in diffs_array_reduced:
        comb = count_combinations(len(ones_array))
        n_of_arrangements *= comb

    print(n_of_arrangements)


def count_combinations(array_length):
    accumulator = 0
    array = [1 for _ in range(array_length)]

    def combinate(init_array):
        nonlocal accumulator
        this_length = len(init_array)
        if this_length == 0:
            accumulator += 1
        if this_length >= 1:
            combinate(init_array[1:])
        if this_length >= 2:
            combinate(init_array[2:])
        if this_length >= 3:
            combinate(init_array[3:])

    combinate(array)
    return accumulator


if __name__ == '__main__':
    main()
