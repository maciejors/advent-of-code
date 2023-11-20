def main():
    with open('input.txt') as file:
        data = tuple([int(i) for i in file.read().split(',')])
    puzzle1(data)


def puzzle1(data):
    most_recent_number = None
    numbers_last_time = dict()
    for turn in range(1, 30000001):
        if turn <= len(data):
            if most_recent_number is not None:
                numbers_last_time[most_recent_number] = turn - 1
            most_recent_number = data[turn - 1]
        else:
            if most_recent_number not in numbers_last_time.keys():
                numbers_last_time[most_recent_number] = turn - 1
                most_recent_number = 0
            else:
                last_time = numbers_last_time[most_recent_number]
                numbers_last_time[most_recent_number] = turn - 1
                most_recent_number = turn - 1 - last_time
    print(most_recent_number)


if __name__ == '__main__':
    main()
