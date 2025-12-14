use std::cmp::Ordering;
use aoc2024::common::data_loader::load_multiline;

const DAY_ID: &str = "05";

struct PagesData {
    ordering_rules: Vec<(u8, u8)>,
    updates: Vec<Vec<u8>>,
}

impl PagesData {
    fn new() -> Self {
        PagesData {
            ordering_rules: vec![],
            updates: vec![],
        }
    }

    fn find_required_before(&self, page: u8) -> Vec<u8> {
        self.ordering_rules
            .iter()
            .filter(|rule| rule.1 == page)
            .map(|rule| rule.0)
            .collect()
    }
}

fn main() {
    println!("Puzzle 1 solution: {}", puzzle1("input"));
    println!("Puzzle 2 solution: {}", puzzle2("input"));
}

fn process_input(input_lines: Vec<String>) -> PagesData {
    let mut result = PagesData::new();
    for line in input_lines {
        if line.contains("|") {
            let ordering_rule: Vec<u8> = line.split('|').map(|s| s.parse().unwrap()).collect();
            result
                .ordering_rules
                .push((ordering_rule[0], ordering_rule[1]));
        }
        if line.contains(",") {
            let pages_to_produce: Vec<u8> = line.split(',').map(|s| s.parse().unwrap()).collect();
            result.updates.push(pages_to_produce);
        }
    }
    result
}

fn separate_correctly_incorrectly_ordered(pages_data: &PagesData) -> (Vec<Vec<u8>>, Vec<Vec<u8>>) {
    let mut result = (vec![], vec![]);
    for update in &pages_data.updates {
        let mut is_correctly_ordered = true;
        for (i, page) in update.iter().enumerate() {
            let required_before = pages_data.find_required_before(*page);
            let is_order_violated: bool = update[i + 1..]
                .iter()
                .any(|later_page| required_before.contains(later_page));
            if is_order_violated {
                is_correctly_ordered = false;
                break;
            }
        }
        if is_correctly_ordered {
            result.0.push(update.clone());
        } else {
            result.1.push(update.clone());
        }
    }
    result
}

fn puzzle1(filename: &str) -> u32 {
    let pages_data = process_input(load_multiline(DAY_ID, filename).unwrap());
    let (correctly_ordered_updates, _) =
        separate_correctly_incorrectly_ordered(&pages_data);
    correctly_ordered_updates.iter()
        .map(|update| update[update.len() / 2] as u32)
        .sum()
}

fn puzzle2(filename: &str) -> u32 {
    let pages_data = process_input(load_multiline(DAY_ID, filename).unwrap());
    let (_, incorrect_updates) = separate_correctly_incorrectly_ordered(&pages_data);
    let mut middle_pages_sum = 0;
    for mut update in incorrect_updates {
        let related_ordering_rules: Vec<(u8, u8)> = pages_data.ordering_rules.iter()
            .copied()
            .filter(|(page_before, page_after)| update.contains(page_before) || update.contains(page_after))
            .collect();
        update.sort_by(|&page1, &page2| {
            if related_ordering_rules.contains(&(page1, page2)) {
                Ordering::Less
            } else if related_ordering_rules.contains(&(page2, page1)) {
                Ordering::Greater
            } else {
                Ordering::Equal
            }
        });
        middle_pages_sum += update[update.len() / 2] as u32
    }
    middle_pages_sum
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_puzzle1() {
        assert_eq!(puzzle1("example"), 143);
    }

    #[test]
    fn test_puzzle2() {
        assert_eq!(puzzle2("example"), 123);
    }
}
