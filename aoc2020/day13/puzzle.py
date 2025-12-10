import time
from collections import defaultdict


def main():
    for i in range(6):
        suffix = str(i + 1) if i > 0 else ''
        with open(f'example{suffix}.txt') as file:
            file_splitted = file.read().split('\n')
            data = (int(file_splitted[0]), file_splitted[1].split(','))
        print(data[1])
        puzzle2_deprecated(data)
        puzzle2(data)
        print()
    # with open('input.txt') as file:
    #     file_splitted = file.read().split('\n')
    #     data = (int(file_splitted[0]), file_splitted[1].split(','))
    # # puzzle1(data)
    # # puzzle2_deprecated(data)
    # puzzle2(data)


def puzzle1(data):
    curr_time = data[0]
    next_bus_id = 0
    wait_time = 9999999999
    for bus_id in data[1]:
        if bus_id != 'x':
            bus_id = int(bus_id)
            this_wait_time = bus_id - (curr_time % bus_id)
            if this_wait_time < wait_time:
                next_bus_id = bus_id
                wait_time = this_wait_time
    print(next_bus_id * wait_time)


def puzzle2_deprecated(data):
    t = 0
    # t = 100000000000000
    ids = list()
    n_of_consecutive_x = 0
    for bus_id in data[1]:
        if bus_id == 'x':
            n_of_consecutive_x += 1
        else:
            if n_of_consecutive_x != 0:
                ids.append(f'x{n_of_consecutive_x}')
                n_of_consecutive_x = 0
            ids.append(int(bus_id))
    largest_bus_id = max(ids, key=lambda x: 0 if type(x) == str else x)
    t -= (t + data[1].index(str(largest_bus_id))) % largest_bus_id
    delta = time.time()  # debug
    while True:
        if not t % ids[0]:
            diff = 1
            correct = True
            for bus_id in ids[1:]:
                if type(bus_id) == int:
                    if (t + diff) % bus_id:
                        correct = False
                        break
                    diff += 1
                else:
                    diff += int(bus_id[1:])
            if correct:
                break
        t += largest_bus_id
    delta = abs(delta - time.time())  # debug
    print(f'delta1: {delta}')  # debug
    print(t)


def get_prime_numbers(upper: int):
    return [i for i in range(2, upper) if all(i % j for j in range(2, i))]


def calc_prime_factorisation(x: int) -> defaultdict[int, int]:
    prime_numbers = get_prime_numbers(x + 1)
    result = defaultdict(lambda: 0)
    curr_prime_idx = 0
    while x != 1:
        while x % (curr_prime := prime_numbers[curr_prime_idx]) != 0:
            curr_prime_idx += 1

        result[curr_prime] += 1
        x //= curr_prime

    return result


def least_common_multiple(numbers: list[int]) -> int:
    prime_factorisations = [calc_prime_factorisation(x) for x in numbers]
    all_avail_primes = set().union(*[pf.keys() for pf in prime_factorisations])
    result = 1
    for prime_number in all_avail_primes:
        max_power = max(pf[prime_number] for pf in prime_factorisations)
        result *= prime_number ** max_power
    return result


def puzzle2(data):
    t = 0
    # t = 100000000000000
    desired_offsets = []  # (bus_id, offset)
    for bus_offset, bus_id in enumerate(data[1]):
        if bus_id != 'x':
            bus_id = int(bus_id)
            desired_offsets.append((bus_id, bus_offset))

    delta = time.time()  # debug

    # least common multiple of all bus ids is the period after which
    # all buses arrive at the same time
    reset_period = least_common_multiple([bus_id for bus_id, _ in desired_offsets])
    print('max: ', reset_period)

    delta = abs(delta - time.time())  # debug
    print(f'delta2: {delta}')  # debug
    print(t)


if __name__ == '__main__':
    main()
