from copy import copy


def main():
    data = list()
    with open('input.txt') as file:
        for line in file.readlines():
            data.append(list(line.replace('\n', '')))
    puzzle1(data)
    puzzle2(data)


def puzzle1(data):
    data = simulate(data)
    n_of_occupied_seats = 0
    for row in data:
        n_of_occupied_seats += row.count('#')
    print(n_of_occupied_seats)


def puzzle2(data):
    data = simulate2(data)
    n_of_occupied_seats = 0
    for row in data:
        n_of_occupied_seats += row.count('#')
    print(n_of_occupied_seats)


def simulate(data):
    while True:
        new_data = [copy(row) for row in data]
        for row_id in range(len(data)):
            for column_id in range(len(data[row_id])):
                seat_state = data[row_id][column_id]
                if seat_state != '.':
                    occupied_seats_around = 0
                    for x in (-1, 0, 1):
                        for y in (-1, 0, 1):
                            if not (x == y == 0):
                                neighbour_row_id = row_id + y
                                neighbour_col_id = column_id + x
                                if 0 <= neighbour_col_id < len(data[row_id]) and 0 <= neighbour_row_id < len(data):
                                    if data[neighbour_row_id][neighbour_col_id] == '#':
                                        occupied_seats_around += 1
                    if seat_state == 'L':
                        if occupied_seats_around == 0:
                            new_data[row_id][column_id] = '#'
                    elif seat_state == '#':
                        if occupied_seats_around >= 4:
                            new_data[row_id][column_id] = 'L'
        if new_data == data:
            break
        data = new_data
    return data


def simulate2(data):
    while True:
        new_data = [copy(row) for row in data]
        for row_id in range(len(data)):
            for column_id in range(len(data[row_id])):
                seat_state = data[row_id][column_id]
                if seat_state != '.':
                    occupied_seats_around = 0
                    for x in (-1, 0, 1):
                        for y in (-1, 0, 1):
                            if not (x == y == 0):
                                neighbour_row_id = row_id
                                neighbour_col_id = column_id
                                while True:
                                    neighbour_row_id += y
                                    neighbour_col_id += x
                                    if not (0 <= neighbour_col_id < len(data[row_id]) and
                                            0 <= neighbour_row_id < len(data)):
                                        break
                                    if data[neighbour_row_id][neighbour_col_id] == '#':
                                        occupied_seats_around += 1
                                        break
                                    if data[neighbour_row_id][neighbour_col_id] == 'L':
                                        break
                    if seat_state == 'L':
                        if occupied_seats_around == 0:
                            new_data[row_id][column_id] = '#'
                    elif seat_state == '#':
                        if occupied_seats_around >= 5:
                            new_data[row_id][column_id] = 'L'
        if new_data == data:
            break
        data = new_data
    return data


if __name__ == '__main__':
    main()
