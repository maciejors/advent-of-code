use aoc2024::common::data_loader::load_multiline;

const DAY_ID: &str = "01";

fn main() {
    println!("Puzzle 1 solution: {}", puzzle1("input"));
    println!("Puzzle 2 solution: {}", puzzle2("input"));
}

fn process_input(input_lines: Vec<String>) -> (Vec<u32>, Vec<u32>) {
    let mut result = (Vec::new(), Vec::new());
    input_lines.into_iter().for_each(|line| {
        let line_numbers: Vec<u32> = line.split_whitespace()
            .map(|num_str| num_str.parse().unwrap())
            .collect();
        result.0.push(line_numbers[0]);
        result.1.push(line_numbers[1]);
    });
    result.0.sort();
    result.1.sort();
    result
}

fn puzzle1(filename: &str) -> u32 {
    let (left, right) = process_input(
        load_multiline(DAY_ID, filename).unwrap()
    );
    let distances: Vec<u32> = left.into_iter()
        .zip(right.into_iter())
        .map(|(left_item, right_item)| left_item.abs_diff(right_item))
        .collect();
    distances.iter().sum()
}

fn puzzle2(filename: &str) -> u32 {
    let (left, right) = process_input(
        load_multiline(DAY_ID, filename).unwrap()
    );
    let mut scores: Vec<u32> = Vec::new();

    let mut pointer_right = 0;

    let mut prev_left_item = None;
    'outer: for left_item in left {
        if let Some(prev_left_item) = prev_left_item && prev_left_item == left_item {
            let prev_score = scores.last().unwrap();
            scores.push(*prev_score);
            continue;
        }

        let mut left_count_in_right = 0;

        while right[pointer_right] <= left_item {
            if right[pointer_right] == left_item {
                left_count_in_right += 1;
            }
            pointer_right += 1;
            if pointer_right == right.len() {
                break 'outer;
            }
        }
        scores.push(left_item * left_count_in_right);
        prev_left_item = Some(left_item);
    }
    scores.iter().sum()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_puzzle1() {
        assert_eq!(puzzle1("example"), 11)
    }

    #[test]
    fn test_puzzle2() {
        assert_eq!(puzzle2("example"), 31)
    }
}
