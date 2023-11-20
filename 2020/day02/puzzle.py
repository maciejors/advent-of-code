from typing import List, Union, Dict


def main():
    passwd_list: List[Dict[str, Union[int, str]]] = list()
    # passwd_list = \
    #       [{"min": min_occurences_of_char, "max": max_occurences_of_char, "char": char, "pass": password}, ...]
    with open("input.txt") as file:
        for line in file.readlines():
            line = line.split(': ')
            line[0] = line[0].split()
            password_data = dict()
            password_data["min"], password_data["max"] = tuple(map(lambda i: int(i), line[0][0].split('-')))
            password_data["char"] = line[0][1]
            password_data["pass"] = line[1].replace('\n', '')
            passwd_list.append(password_data)
    puzzle1(passwd_list)
    puzzle2(passwd_list)


def puzzle1(passwd_list):
    n_of_valid_passwords = 0
    for passwd in passwd_list:
        if passwd["min"] <= passwd["pass"].count(passwd["char"]) <= passwd["max"]:
            n_of_valid_passwords += 1
    print(n_of_valid_passwords)


def puzzle2(passwd_list):
    n_of_valid_passwords = 0
    for passwd in passwd_list:
        c = passwd["char"]
        if (passwd["pass"][passwd["min"] - 1] == c) ^ (passwd["pass"][passwd["max"] - 1] == c):
            n_of_valid_passwords += 1
    print(n_of_valid_passwords)


if __name__ == '__main__':
    main()
