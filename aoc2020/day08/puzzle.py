from copy import copy
from typing import Tuple


def main():
    data = list()
    with open('input.txt') as file:
        for line in file.readlines():
            line = line.replace('\n', '')
            # instruction = tuple(<name>, <arg>, <wasExecuted>)
            instruction = [line.split()[0], int(line.split()[1]), False]
            data.append(instruction)
    puzzle2(data)


def puzzle1(data):
    i = 0
    acc = 0
    while True:
        if data[i][2]:
            break
        data[i][2] = True
        if data[i][0] == 'acc':
            acc += data[i][1]
            i += 1
        elif data[i][0] == 'jmp':
            i += data[i][1]
        else:
            i += 1
    print(acc)


def is_program_infinite(data):
    i = 0
    acc = 0
    is_looping = False
    while True:
        if i == len(data):
            break
        if data[i][2]:
            is_looping = True
            break
        data[i][2] = True
        if data[i][0] == 'acc':
            acc += data[i][1]
            i += 1
        elif data[i][0] == 'jmp':
            i += data[i][1]
        else:
            i += 1
    return is_looping, acc


def puzzle2(data):
    accvalue = 0
    for instruction_id in range(len(data)):
        if data[instruction_id][0] == 'nop':
            data_copy = [copy(instruction) for instruction in data]
            data_copy[instruction_id][0] = 'jmp'
            is_looping, accvalue = is_program_infinite(data_copy)
            data_copy[instruction_id][0] = 'nop'
            if not is_looping:
                break
        elif data[instruction_id][0] == 'jmp':
            data_copy = [copy(instruction) for instruction in data]
            data_copy[instruction_id][0] = 'nop'
            is_looping, accvalue = is_program_infinite(data_copy)
            data_copy[instruction_id][0] = 'jmp'
            if not is_looping:
                break
    print(accvalue)


if __name__ == '__main__':
    main()
