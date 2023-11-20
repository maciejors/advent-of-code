from copy import copy


def main():
    requirements = dict()
    my_ticket = tuple()
    nearby_tickets = list()
    valid_ranges = set()
    with open('input.txt') as file:
        reading_now = 'requirements'
        for line in file.readlines():
            line = line.replace('\n', '')
            if line == '':
                continue
            elif line == 'your ticket:':
                reading_now = 'myticket'
            elif line == 'nearby tickets:':
                reading_now = 'nearbytickets'
            elif reading_now == 'requirements':
                key, rawvalues = line.split(': ')
                rawvalues = rawvalues.split(' or ')
                ranges = [tuple([int(i) for i in rawvalues[0].split('-')]),
                          tuple([int(i) for i in rawvalues[1].split('-')])]
                for range_ in ranges:
                    for i in range(range_[0], range_[1] + 1):
                        valid_ranges.add(i)
                requirements.update({key: ranges})
            elif reading_now == 'myticket':
                my_ticket = tuple([int(i) for i in line.split(',')])
            elif reading_now == 'nearbytickets':
                nearby_tickets.append(tuple([int(i) for i in line.split(',')]))
    # puzzle 1
    ticket_scanning_error_rate_puzzle1 = 0
    for ticket in [copy(ticket) for ticket in nearby_tickets]:
        for val in ticket:
            if val not in valid_ranges:
                ticket_scanning_error_rate_puzzle1 += val
                nearby_tickets.remove(ticket)
                break
    # print(ticket_scanning_error_rate_puzzle1)
    # puzzle 2
    order = {key: -1 for key in requirements.keys()}
    possible_positions_for_keys = {key: set() for key in requirements.keys()}
    for key in requirements.keys():
        remaining_positions = {i for i in range(len(my_ticket))}
        ranges = requirements[key]
        for ticket in nearby_tickets:
            i = 0
            for val in ticket:
                if not (ranges[0][0] <= val <= ranges[0][1] or ranges[1][0] <= val <= ranges[1][1]):
                    remaining_positions.remove(i)
                i += 1
        possible_positions_for_keys.update({key: remaining_positions})
    while -1 in order.values():
        for key1, key_pos in tuple(possible_positions_for_keys.items()):
            if len(key_pos) == 1:
                copy_key_pos = copy(key_pos)
                order[key1] = key_pos.pop()
                for key2 in possible_positions_for_keys:
                    possible_positions_for_keys[key2] -= copy_key_pos
    result = 1
    for key, position in order.items():
        if key.startswith("departure"):
            result *= my_ticket[order[key]]
    print(result)


if __name__ == '__main__':
    main()
