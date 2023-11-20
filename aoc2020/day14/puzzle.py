from copy import copy

iii = 0


def main():
    program = list()
    with open('input.txt') as file:
        for line in file.readlines():
            program.append(line.replace('\n', '').split(' = '))
    memory = dict()
    mask = str()
    puzzle_to_solve = 2
    # puzzle 1
    if puzzle_to_solve == 1:
        for line in program:
            call = line[0]
            arg = line[1]
            if call.startswith('mask'):
                mask = arg
            else:
                where_to_store = int(call.split('[')[1][:-1])
                value = apply_mask(dec_to_bin(int(arg), len(mask)), mask)
                memory[where_to_store] = value
        sum_ = 0
        for i in memory.values():
            sum_ += bin_to_dec(i)
        print(sum_)
    # puzzle 2
    elif puzzle_to_solve == 2:
        for line in program:
            call = line[0]
            arg = line[1]
            if call.startswith('mask'):
                mask = arg
            else:
                raw_address = int(call.split('[')[1][:-1])
                value = int(arg)
                addresses = apply_mask_v2(dec_to_bin(raw_address, len(mask)), mask)
                for address in addresses:
                    memory[bin_to_dec(address)] = value
        print(sum(memory.values()))


def apply_mask(binvalue, mask):
    result = list(binvalue)
    for i in range(len(binvalue)):
        if mask[i] != 'X':
            result[i] = mask[i]
    return ''.join(result)


def apply_mask_v2(binvalue, mask):
    global iii
    print(f'{iii}: began apply_mask_v2')
    result = list(binvalue)
    for i in range(len(binvalue)):
        if mask[i] != '0':
            result[i] = mask[i]
    addresses = set()

    def add_addresses(maskedb: list):
        for j in range(len(maskedb)):
            if maskedb[j] == 'X':
                copy0 = copy(maskedb)
                copy1 = copy(maskedb)
                copy0[j] = '0'
                copy1[j] = '1'
                add_addresses(copy0)
                add_addresses(copy1)
                return
        if 'X' not in maskedb:
            addresses.add(''.join(maskedb))

    add_addresses(result)
    print(f'{iii}: finished apply_mask_v2')
    iii += 1
    return addresses


def dec_to_bin(n, min_length):
    b = str()
    while n != 0:
        b = str(n % 2) + b
        n //= 2
    while len(b) < min_length:
        b = '0' + b
    return b


def bin_to_dec(b):
    dec = 0
    while b[0] == '0':
        b = b[1:]
        if b == '':
            return 0
    for i in range(len(b)):
        dec += int(b[-i - 1]) * 2 ** i
    return dec


if __name__ == '__main__':
    main()
