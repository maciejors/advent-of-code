from typing import List, Dict


def main():
    with open('input.txt') as file:
        raw_data = file.read().split('\n\n')
    data = list()
    for passport_string in raw_data:
        passport_string = passport_string.replace('\n', ' ')
        keys = [i.split(':')[0] for i in passport_string.split()]
        values = [i.split(':')[1] for i in passport_string.split()]
        data.append(dict(zip(keys, values)))
    # print_n_of_valid_passports(data)
    print_n_of_valid_passports_strict(data)


def print_n_of_valid_passports(data: List[Dict[str, str]]):
    required_fields = {'byr', 'iyr', 'eyr', 'hgt', 'hcl', 'ecl', 'pid'}
    n_of_valid_passports = 0
    for passport in data:
        if (required_fields & set(passport.keys())) == required_fields:
            n_of_valid_passports += 1
    print(n_of_valid_passports)


def print_n_of_valid_passports_strict(data: List[Dict[str, str]]):
    required_fields = {'byr', 'iyr', 'eyr', 'hgt', 'hcl', 'ecl', 'pid'}
    n_of_valid_passports = 0
    for passport in data:
        if (required_fields & set(passport.keys())) == required_fields:
            try:
                assert passport['byr'].isdigit() and 1920 <= int(passport['byr']) <= 2002
                assert passport['iyr'].isdigit() and 2010 <= int(passport['iyr']) <= 2020
                assert passport['eyr'].isdigit() and 2020 <= int(passport['eyr']) <= 2030
                height = passport['hgt']
                assert height[:-2].isdigit() and height[-2:] in {'cm', 'in'}
                if height[-2:] == 'cm':
                    assert 150 <= int(height[:-2]) <= 193
                else:
                    assert 59 <= int(height[:-2]) <= 76
                assert passport['hcl'][0] == '#'
                for char in passport['hcl'][1:]:
                    assert 48 <= ord(char) <= 57 or 97 <= ord(char) <= 102
                assert passport['ecl'] in {'amb', 'blu', 'brn', 'gry', 'grn', 'hzl', 'oth'}
                assert passport['pid'].isdigit() and len(passport['pid']) == 9
                n_of_valid_passports += 1
            except AssertionError:
                continue
    print(n_of_valid_passports)


if __name__ == '__main__':
    main()
