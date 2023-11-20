def main():
    with open("seats.txt") as file:
        seats = file.read().split('\n')
    print_highest_id(seats)
    print_your_seat(seats)


def get_seat_data(seat):
    row_range = [0, 127]
    for i in seat[:6]:
        if i == 'F':
            row_range[1] -= (row_range[1] - row_range[0]) // 2 + 1
        elif i == 'B':
            row_range[0] += (row_range[1] - row_range[0]) // 2 + 1
    if seat[6] == 'F':
        row = row_range[0]
    else:
        row = row_range[1]
    column_range = [0, 7]
    for i in seat[7:9]:
        if i == 'L':
            column_range[1] -= (column_range[1] - column_range[0]) // 2 + 1
        elif i == 'R':
            column_range[0] += (column_range[1] - column_range[0]) // 2 + 1
    if seat[9] == 'L':
        column = column_range[0]
    else:
        column = column_range[1]
    return row, column


def get_seat_id(row, column):
    return row * 8 + column


def print_highest_id(seats):
    highest_id = 0
    for seat in seats:
        curr_id = get_seat_id(*get_seat_data(seat))
        if curr_id > highest_id:
            highest_id = curr_id
    print(highest_id)


def print_your_seat(seats):
    remaining_seats = [i for i in range(1023)]
    for seat in seats:
        remaining_seats.remove(get_seat_id(*get_seat_data(seat)))
    if remaining_seats[0] < 3:
        while remaining_seats[0] + 1 == remaining_seats[1]:
            del remaining_seats[0]
    if remaining_seats[-1] > 1020:
        while remaining_seats[-1] - 1 == remaining_seats[-2]:
            del remaining_seats[-1]
    print(remaining_seats[1])


if __name__ == '__main__':
    main()
