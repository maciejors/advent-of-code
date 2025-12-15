use aoc2024::common::data_loader::load_multiline;
use std::collections::HashSet;

const DAY_ID: &str = "06";

#[derive(Copy, Clone)]
enum Position {
    WALKABLE,
    OBSTACLE,
}

#[derive(Copy, Clone, PartialEq, Eq, Hash)]
enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT,
}

#[derive(Copy, Clone, PartialEq, Eq, Hash)]
struct GuardLocation {
    coord: (usize, usize),
    direction: Direction,
}

impl GuardLocation {
    fn rotate_right(&mut self) {
        let order = [
            Direction::UP,
            Direction::RIGHT,
            Direction::DOWN,
            Direction::LEFT,
        ];
        let curr_order_idx = order.iter().position(|&d| d == self.direction).unwrap();
        self.direction = order[(curr_order_idx + 1) % 4];
    }
}

struct LabSituation {
    map: Vec<Vec<Position>>,
    guard_starting_location: GuardLocation,
}

struct PatrolResult {
    visited_locations: Vec<GuardLocation>,
    is_looped: bool,
}

impl LabSituation {
    fn front_of_guard(
        &self,
        guard_location: &GuardLocation,
    ) -> Option<((usize, usize), &Position)> {
        let direction_vector = match guard_location.direction {
            Direction::UP => (-1, 0),
            Direction::RIGHT => (0, 1),
            Direction::DOWN => (1, 0),
            Direction::LEFT => (0, -1),
        };
        let coord_in_front = (
            guard_location.coord.0 as isize + direction_vector.0,
            guard_location.coord.1 as isize + direction_vector.1,
        );
        if coord_in_front.0 < 0
            || coord_in_front.0 as usize >= self.map.len()
            || coord_in_front.1 < 0
            || coord_in_front.1 as usize >= self.map[0].len()
        {
            None
        } else {
            let coord_in_front = (coord_in_front.0 as usize, coord_in_front.1 as usize);
            Some((
                coord_in_front,
                &self.map[coord_in_front.0][coord_in_front.1],
            ))
        }
    }

    fn run_patrol(&self) -> PatrolResult {
        let mut is_looped = true;
        let mut visited_locations_vec = vec![];
        let mut visited_locations_set = HashSet::new();
        let mut curr_guard_location = self.guard_starting_location.clone();

        while !visited_locations_set.contains(&curr_guard_location) {
            {
                let location_clone = curr_guard_location.clone();
                visited_locations_vec.push(location_clone);
                visited_locations_set.insert(location_clone);
            }

            match self.front_of_guard(&curr_guard_location) {
                None => {
                    is_looped = false;
                    break;
                }
                Some((coord_in_front, pos_in_front)) => match pos_in_front {
                    Position::OBSTACLE => curr_guard_location.rotate_right(),
                    Position::WALKABLE => curr_guard_location.coord = coord_in_front,
                },
            }
        }
        PatrolResult {
            is_looped,
            visited_locations: visited_locations_vec,
        }
    }
}

fn main() {
    println!("Puzzle 1 solution: {}", puzzle1("input"));
    println!("Puzzle 2 solution: {}", puzzle2("input"));
}

fn process_input(input_lines: Vec<String>) -> LabSituation {
    let mut lab_map = vec![];
    let mut guard_starting_location = None;

    for (row_idx, line) in input_lines.into_iter().enumerate() {
        lab_map.push(
            line.chars()
                .into_iter()
                .enumerate()
                .map(|(col_idx, char)| match char {
                    '^' => {
                        guard_starting_location = Some(GuardLocation {
                            coord: (row_idx, col_idx),
                            direction: Direction::UP,
                        });
                        Position::WALKABLE
                    }
                    '#' => Position::OBSTACLE,
                    _ => Position::WALKABLE,
                })
                .collect(),
        );
    }
    LabSituation {
        map: lab_map,
        guard_starting_location: guard_starting_location
            .expect("Guard starting location is missing from the input data"),
    }
}

fn puzzle1(filename: &str) -> usize {
    let lab_situation = process_input(load_multiline(DAY_ID, filename).unwrap());
    let patrol_result = lab_situation.run_patrol();
    let visited_coords: HashSet<(usize, usize)> = patrol_result
        .visited_locations
        .into_iter()
        .map(|loc| loc.coord)
        .collect();
    visited_coords.len()
}

fn puzzle2(filename: &str) -> u32 {
    let mut lab_situation = process_input(load_multiline(DAY_ID, filename).unwrap());
    let patrol_result = lab_situation.run_patrol();

    let mut result = 0;
    let mut past_coords = HashSet::new();

    for i in 0..patrol_result.visited_locations.len() - 1 {
        let starting_location = patrol_result.visited_locations[i];
        let new_obstruction_coord = patrol_result.visited_locations[i + 1].coord;

        if past_coords.contains(&new_obstruction_coord) {
            continue;
        }

        lab_situation.guard_starting_location = starting_location;
        lab_situation.map[new_obstruction_coord.0][new_obstruction_coord.1] = Position::OBSTACLE;
        if lab_situation.run_patrol().is_looped {
            result += 1;
        }
        lab_situation.map[new_obstruction_coord.0][new_obstruction_coord.1] = Position::WALKABLE;

        past_coords.insert(new_obstruction_coord);
    }
    result
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_puzzle1_example() {
        assert_eq!(puzzle1("example"), 41);
    }

    #[test]
    fn test_puzzle1_input() {
        assert_eq!(puzzle1("input"), 4903);
    }

    #[test]
    fn test_puzzle2_example() {
        assert_eq!(puzzle2("example"), 6);
    }

    #[test]
    fn test_puzzle2_input() {
        assert_eq!(puzzle2("input"), 1911);
    }
}
