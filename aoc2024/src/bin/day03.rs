use aoc2024::common::data_loader::load_multiline;
use regex::Regex;

const DAY_ID: &str = "03";

fn main() {
    println!("Puzzle 1 solution: {}", puzzle1("input"));
    println!("Puzzle 2 solution: {}", puzzle2("input"));
}

fn process_input(input_lines: Vec<String>) -> String {
    input_lines.concat()
}

fn puzzle1(filename: &str) -> u32 {
    let input = process_input(load_multiline(DAY_ID, filename).unwrap());
    let mut result = 0;
    let mul_pattern = Regex::new(r"mul\((\d+),(\d+)\)").unwrap();
    for captures in mul_pattern.captures_iter(&input) {
        let left_arg: u32 = captures[1].parse().unwrap();
        let right_arg: u32 = captures[2].parse().unwrap();
        result += left_arg * right_arg;
    }
    result
}

fn puzzle2(filename: &str) -> u32 {
    let input = process_input(load_multiline(DAY_ID, filename).unwrap());

    let instruction_regex = Regex::new(r"(do|don't|mul)\(([,\d]*)\)")
        .unwrap();

    let mut result = 0;
    let mut is_mul_enabled = true;

    for instruction_captures in instruction_regex.captures_iter(&input) {
        let instruction_name = instruction_captures[1].to_owned();
        match instruction_name.as_str() {
            "do" => is_mul_enabled = true,
            "don't" => is_mul_enabled = false,
            "mul" => {
                if !is_mul_enabled {
                    continue;
                }
                let args_regex = Regex::new(r"(\d+),(\d+)").unwrap();
                let args_text = &instruction_captures[2];
                if let Some(args_captures) = args_regex.captures(args_text) {
                    let left_arg: u32 = args_captures[1].parse().unwrap();
                    let right_arg: u32 = args_captures[2].parse().unwrap();
                    result += left_arg * right_arg;
                }
            }
            _ => {}
        }
    }
    result
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_puzzle1() {
        assert_eq!(puzzle1("example1"), 161)
    }

    #[test]
    fn test_puzzle2() {
        assert_eq!(puzzle2("example2"), 48)
    }
}
