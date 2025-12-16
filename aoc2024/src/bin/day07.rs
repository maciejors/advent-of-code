use aoc2024::common::data_loader::load_multiline;
use std::cell::RefCell;
use std::collections::HashMap;
use std::rc::Rc;

const DAY_ID: &str = "07";

fn main() {
    println!("Puzzle 1 solution: {}", puzzle1("input"));
    println!("Puzzle 2 solution: {}", puzzle2("input"));
}

struct CalibrationData {
    test_value: u64,
    numbers: Vec<u64>,
}

fn process_input(input_lines: Vec<String>) -> Vec<CalibrationData> {
    input_lines
        .into_iter()
        .map(|line| {
            let line_split_by_colon: Vec<&str> = line.split(":").collect();
            let test_value = line_split_by_colon[0].parse().unwrap();
            let numbers = line_split_by_colon[1]
                .split_whitespace()
                .map(|num_str| num_str.parse().unwrap())
                .collect();
            CalibrationData {
                test_value,
                numbers,
            }
        })
        .collect()
}

fn puzzle1(filename: &str) -> u64 {
    let input = process_input(load_multiline(DAY_ID, filename).unwrap());
    let mut total_calibration_result = 0;

    for calibration_data in input {
        let operators_count = calibration_data.numbers.len() - 1;
        for mask in 0..2_u64.pow(operators_count as u32) {
            let mut calibration_result = calibration_data.numbers[0];
            for i in 0..operators_count {
                let next_number = calibration_data.numbers[i + 1];
                let bit = (mask >> i) & 1;
                if bit == 0 {
                    calibration_result += next_number;
                } else {
                    calibration_result *= next_number;
                }
                if calibration_result > calibration_data.test_value {
                    // no point calculating further
                    break;
                }
            }
            if calibration_result == calibration_data.test_value {
                total_calibration_result += calibration_result;
                break;
            }
        }
    }
    total_calibration_result
}

fn puzzle2(filename: &str) -> u64 {
    let input = process_input(load_multiline(DAY_ID, filename).unwrap());
    let mask_generator = MaskGenerator::new(3);
    let mut total_calibration_result = 0;

    for calibration_data in input {
        let operators_count = calibration_data.numbers.len() - 1;

        for operators_mask in mask_generator.get_all_masks(operators_count).iter() {
            let mut calibration_result = calibration_data.numbers[0];
            for (next_number, operator_idx) in
                calibration_data.numbers[1..].iter().zip(operators_mask)
            {
                if *operator_idx == 0 {
                    calibration_result += next_number;
                } else if *operator_idx == 1 {
                    calibration_result *= next_number;
                } else {
                    let next_number_digits_count = (*next_number as f64).log10().floor() as u32 + 1;
                    calibration_result =
                        calibration_result * 10_u64.pow(next_number_digits_count) + next_number;
                }
                if calibration_result > calibration_data.test_value {
                    // no point calculating further
                    break;
                }
            }
            if calibration_result == calibration_data.test_value {
                total_calibration_result += calibration_result;
                break;
            }
        }
    }
    total_calibration_result
}

// Cache implementation based on: https://matklad.github.io/2022/06/11/caches-in-rust.html
struct MaskGenerator {
    n_values: u8,
    cache: RefCell<HashMap<
        usize, Rc<Vec<Vec<u8>>>
    >>,
}

impl MaskGenerator {
    fn new(n_values: u8) -> Self {
        MaskGenerator {
            n_values,
            cache: RefCell::new(HashMap::new()),
        }
    }

    fn get_all_masks(&self, len: usize) -> Rc<Vec<Vec<u8>>> {
        {
            let cache = self.cache.borrow();
            if let Some(cached_masks) = cache.get(&len) {
                return Rc::clone(cached_masks)
            }
        }

        let masks = if len == 1 {
            (0..self.n_values).map(|e| vec![e.into()]).collect()
        } else {
            let mut result = vec![];
            for next_mask_item in 0..self.n_values {
                for sub_mask in self.get_all_masks(len - 1).iter() {
                    let mut mask = sub_mask.clone();
                    mask.push(next_mask_item);
                    result.push(mask);
                }
            }
            result
        };

        {
            let mut cache = self.cache.borrow_mut();
            cache.insert(len, Rc::new(masks));
        }
        let cache = self.cache.borrow();
        Rc::clone(cache.get(&len).unwrap())
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_puzzle1_example() {
        assert_eq!(puzzle1("example"), 3749);
    }

    #[test]
    fn test_puzzle1_input() {
        assert_eq!(puzzle1("input"), 1985268524462);
    }

    #[test]
    fn test_puzzle2_example() {
        assert_eq!(puzzle2("example"), 11387);
    }

    #[test]
    fn test_puzzle2_input() {
        assert_eq!(puzzle2("input"), 150077710195188);
    }
}
