from copy import copy


def main():
    answers = list()
    with open('input.txt') as file:
        group = list()
        for line in file.readlines():
            if line == '\n':
                answers.append(copy(group))
                group = list()
            else:
                group.append(line.replace('\n', ''))
        answers.append(copy(group))
    puzzle2(answers)


def puzzle1(answers):
    n_of_yes_answers = 0
    for group in answers:
        group_questions_answered_yes = set()
        for person in group:
            for answer in person:
                if answer not in group_questions_answered_yes:
                    n_of_yes_answers += 1
                    group_questions_answered_yes.add(answer)
    print(n_of_yes_answers)


def puzzle2(answers):
    n_of_yes_answers = 0
    for group in answers:
        all_yes_questions = set(group[0])
        for person in group[1:]:
            all_yes_questions = all_yes_questions & set(person)
        n_of_yes_answers += len(all_yes_questions)
    print(n_of_yes_answers)


if __name__ == '__main__':
    main()
