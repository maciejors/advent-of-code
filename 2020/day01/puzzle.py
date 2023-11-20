def main():
    with open("input.txt", "r") as file:
        numbers = [int(i) for i in file.read().split()]
    part1(numbers)
    part2(numbers)


def part1(numbers):
    last_checked_number_id = 0
    break_entirely = False
    correct_entry1 = 0
    correct_entry2 = 0
    for number1 in numbers:
        for number2 in numbers[last_checked_number_id + 1:]:
            if number1 + number2 == 2020:
                correct_entry1 = number1
                correct_entry2 = number2
                break_entirely = True
                break
        if break_entirely:
            break
    print(correct_entry1 * correct_entry2)


def part2(numbers):
    left_boundry1 = 1
    left_boundry2 = 2
    break_entirely = False
    correct_entry1 = 0
    correct_entry2 = 0
    correct_entry3 = 0
    for number1 in numbers:
        for number2 in numbers[left_boundry1:]:
            for number3 in numbers[left_boundry2:]:
                if number1 + number2 + number3 == 2020:
                    correct_entry1 = number1
                    correct_entry2 = number2
                    correct_entry3 = number3
                    break_entirely = True
                    break
            if break_entirely:
                break
        if break_entirely:
            break
    print(correct_entry1 * correct_entry2 * correct_entry3)


if __name__ == '__main__':
    main()
