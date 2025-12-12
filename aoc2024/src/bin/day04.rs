use aoc2024::common::data_loader::load_multiline;

const DAY_ID: &str = "04";
const XMAS: &str = "XMAS";

fn main() {
    println!("Puzzle 1 solution: {}", puzzle1("input"));
    println!("Puzzle 2 solution: {}", puzzle2("input"));
}

fn process_input(input_lines: Vec<String>) -> Vec<Vec<char>> {
    input_lines
        .into_iter()
        .map(|line| line.chars().collect())
        .collect()
}

fn traverse(
    map: &[Vec<char>],
    mut curr_word: String,
    curr_coord: (usize, usize),
    direction: (isize, isize),
) -> bool {
    if !XMAS.starts_with(&curr_word) {
        return false;
    }
    if &curr_word == XMAS {
        return true;
    }

    let curr_coord = (curr_coord.0 as isize, curr_coord.1 as isize);
    let next_coord = (curr_coord.0 + direction.0, curr_coord.1 + direction.1);
    if next_coord.0 < 0
        || next_coord.0 as usize >= map.len()
        || next_coord.1 < 0
        || next_coord.1 as usize >= map[0].len()
    {
        return false;
    }

    let next_coord = (next_coord.0 as usize, next_coord.1 as usize);
    let next_char = map[next_coord.0][next_coord.1];
    curr_word.push(next_char);
    traverse(map, curr_word, next_coord, direction)
}

fn perform_traversals(map: &[Vec<char>], start_coord: (usize, usize)) -> u32 {
    let mut successful_traversals_count = 0;
    for i in [-1, 0, 1] {
        for j in [-1, 0, 1] {
            let start_char = map[start_coord.0][start_coord.1];
            if (i != 0 || j != 0) && traverse(map, String::from(start_char), start_coord, (i, j)) {
                successful_traversals_count += 1;
            }
        }
    }
    successful_traversals_count
}

fn puzzle1(filename: &str) -> u32 {
    let input = process_input(load_multiline(DAY_ID, filename).unwrap());
    let mut result = 0;
    for row_idx in 0..input.len() {
        for col_idx in 0..input[0].len() {
            result += perform_traversals(&input, (row_idx, col_idx));
        }
    }
    result
}

fn puzzle2(filename: &str) -> u32 {
    let input = process_input(load_multiline(DAY_ID, filename).unwrap());
    let mut result = 0;
    for row_idx in 1..input.len() - 1 {
        'char_loop: for col_idx in 1..input[0].len() - 1 {
            let curr_char = input[row_idx][col_idx];
            if curr_char != 'A' {
                continue;
            }
            let upper_left_char = input[row_idx - 1][col_idx - 1];
            let upper_right_char = input[row_idx - 1][col_idx + 1];
            let lower_left_char = input[row_idx + 1][col_idx - 1];
            let lower_right_char = input[row_idx + 1][col_idx + 1];
            for (upper_char, lower_char) in [
                (upper_left_char, lower_right_char),
                (upper_right_char, lower_left_char),
            ] {
                if !(upper_char == 'M' && lower_char == 'S')
                    && !(upper_char == 'S' && lower_char == 'M')
                {
                    continue 'char_loop;
                }
            }
            result += 1
        }
    }
    result
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_puzzle1() {
        assert_eq!(puzzle1("example"), 18);
    }

    #[test]
    fn test_puzzle2() {
        assert_eq!(puzzle2("example"), 9);
    }
}
