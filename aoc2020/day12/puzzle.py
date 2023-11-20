def main():
    data = list()
    with open('input.txt') as file:
        for line in file.readlines():
            data.append((line[0], int(line[1:].replace('\n', ''))))
    puzzle1(data)
    puzzle2(data)


def puzzle1(data):
    coordinates = [0, 0, 90]
    # coordinates = list(N_pos, E_pos, facing)
    for instruction, value in data:
        if instruction == 'L':
            coordinates[2] = (coordinates[2] - value) % 360
        elif instruction == 'R':
            coordinates[2] = (coordinates[2] + value) % 360
        elif instruction == 'F':
            if coordinates[2] == 0:
                coordinates[0] += value
            elif coordinates[2] == 90:
                coordinates[1] += value
            elif coordinates[2] == 180:
                coordinates[0] -= value
            elif coordinates[2] == 270:
                coordinates[1] -= value
        elif instruction == 'N':
            coordinates[0] += value
        elif instruction == 'E':
            coordinates[1] += value
        elif instruction == 'S':
            coordinates[0] -= value
        elif instruction == 'W':
            coordinates[1] -= value
    print(abs(coordinates[0]) + abs(coordinates[1]))


def puzzle2(data):
    waypoint = [1, 10]
    ship_coords = [0, 0]
    # coordinates = list(N_pos, E_pos, facing)
    for instruction, value in data:
        if instruction == 'R':
            for i in range((value % 360) // 90):
                new_n = -waypoint[1]
                new_e = waypoint[0]
                waypoint[0] = new_n
                waypoint[1] = new_e
        elif instruction == 'L':
            for i in range((value % 360) // 90):
                new_n = waypoint[1]
                new_e = -waypoint[0]
                waypoint[0] = new_n
                waypoint[1] = new_e
        elif instruction == 'F':
            ship_coords[0] += waypoint[0] * value
            ship_coords[1] += waypoint[1] * value
        elif instruction == 'N':
            waypoint[0] += value
        elif instruction == 'E':
            waypoint[1] += value
        elif instruction == 'S':
            waypoint[0] -= value
        elif instruction == 'W':
            waypoint[1] -= value
    print(abs(ship_coords[0]) + abs(ship_coords[1]))


if __name__ == '__main__':
    main()
