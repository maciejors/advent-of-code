use aoc2024::common::data_loader::load_multiline;

const DAY_ID: &str = "02";

fn main() {
    println!("Puzzle 1 solution: {}", puzzle1("input"));
    println!("Puzzle 2 solution: {}", puzzle2("input"));
}

fn process_input(input_lines: Vec<String>) -> Vec<Vec<i32>> {
    input_lines.into_iter()
        .map(|line| line
            .split_whitespace()
            .into_iter()
            .map(|s| s.parse().unwrap())
            .collect())
        .collect()
}

fn verify_report(report: Vec<i32>) -> bool {
    let diffs: Vec<i32> = report[0..report.len() - 1]
        .iter()
        .zip(&report[1..])
        .map(|(prev, next)| next - prev)
        .collect();

    let are_all_increasing = diffs.iter().all(|val| *val > 0);
    let are_all_decreasing = diffs.iter().all(|val| *val < 0);
    let are_all_diffs_valid = diffs.iter()
        .all(|val| val.abs() >= 1 && val.abs() <= 3);

    (are_all_increasing || are_all_decreasing) && are_all_diffs_valid
}

fn puzzle1(filename: &str) -> u32 {
    let input = process_input(
        load_multiline(DAY_ID, filename).unwrap()
    );
    let mut safe_count = 0;

    for report in input {
        if verify_report(report) {
            safe_count += 1;
        }
    }
    safe_count
}

fn puzzle2(filename: &str) -> u32 {
    let input = process_input(
        load_multiline(DAY_ID, filename).unwrap()
    );
    let mut safe_count = 0;

    for report in input {
        let mut report_variants_all = vec![];
        for i in 0..report.len() {
            report_variants_all.push(
                report.iter()
                    .enumerate()
                    .filter(|(idx, _)| *idx != i)
                    .map(|(_, val)| *val)
                    .collect()
            );
        }
        report_variants_all.push(report);

        for report_variant in report_variants_all {
            if verify_report(report_variant) {
                safe_count += 1;
                break
            }
        }
    }
    safe_count
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_puzzle1() {
        assert_eq!(puzzle1("example"), 2)
    }

    #[test]
    fn test_puzzle2() {
        assert_eq!(puzzle2("example"), 4)
    }
}
