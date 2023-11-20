import time


def main():
    with open('input.txt') as file:
        file_splitted = file.read().split('\n')
        data = (int(file_splitted[0]), file_splitted[1].split(','))
    # puzzle1(data)
    # puzzle2_deprecated(data)
    puzzle2(data)


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


def puzzle2(data):
    t = 0
    t = 100000000000000
    bus_offsets = dict()
    offset = 0
    for bus_id in data[1]:
        if bus_id != 'x':
            bus_offsets.update({int(bus_id): offset})
        offset += 1
    bus_ids = tuple(bus_offsets.keys())
    largest_bus_id = max(bus_ids)
    t -= (t + bus_offsets[largest_bus_id]) % largest_bus_id
    # delta = time.time()
    while True:
        if not t % bus_ids[0]:
            correct = True
            for bus_id in bus_ids[1:]:
                if (t + bus_offsets[bus_id]) % bus_id:
                    correct = False
                    break
            if correct:
                break
        t += largest_bus_id
    # delta = abs(delta - time.time())
    # print(f'delta2: {delta}')
    print(t)


if __name__ == '__main__':
    main()
