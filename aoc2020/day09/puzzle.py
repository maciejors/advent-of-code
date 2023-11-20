preamble_len = 25


def main():
    data = list()
    with open('input.txt') as file:
        for line in file.readlines():
            data.append(int(line.replace('\n', '')))
    puzzle(data)


def puzzle(data):
    first_invalid = -1
    first_invalid_id = 0
    for i in range(preamble_len, len(data)):
        if not is_sum(data[i - preamble_len:i], data[i]):
            first_invalid = data[i]
            first_invalid_id = i
    print(first_invalid)
    for start_id in range(first_invalid_id):
        for end_id in range(start_id + 1, first_invalid_id):
            suma = sum(data[start_id:end_id + 1])
            if suma == first_invalid:
                cont_range = data[start_id:end_id + 1]
                print(min(cont_range) + max(cont_range))
                return
            elif suma > first_invalid:
                break


def is_sum(numbers, number):
    i = 0
    for n1 in numbers:
        i += 1
        for n2 in numbers[i:]:
            if n1 + n2 == number:
                return True
    return False


if __name__ == '__main__':
    main()
