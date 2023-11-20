def main():
    data = dict()
    with open('input.txt') as file:
        for line in file.readlines():
            line = line.replace('\n', '').split(' contain ')
            key = line[0][:-5]
            bags = line[1].replace('.', '').split(', ')
            values = dict()
            if bags[0] != 'no other bags':
                for bag in bags:
                    bag = bag.split()
                    number = int(bag[0])
                    colour = f'{bag[1]} {bag[2]}'
                    values[colour] = number
            data[key] = values
    puzzle1(data)
    puzzle2(data)


def puzzle1(data):
    result = 0
    for key in data.keys():
        if key == 'shiny gold':
            continue
        if find_shiny_gold_bag(data, key):
            result += 1
    print(result)


def find_shiny_gold_bag(data, bag):
    if 'shiny gold' in data[bag].keys():
        return True
    result = False
    for key in data[bag].keys():
        result = result or find_shiny_gold_bag(data, key)
    return result


def puzzle2(data):
    print(count_bags_inside(data, 'shiny gold'))


def count_bags_inside(data, bag):
    if not data[bag]:
        return 0
    total = 0
    for inner_bag in data[bag].keys():
        total += data[bag][inner_bag] * (count_bags_inside(data, inner_bag) + 1)
    return total


if __name__ == '__main__':
    main()
