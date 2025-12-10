use std::fs::File;
use std::io::{self, BufRead, BufReader};

pub fn load_multiline(day_id: &str, filename: &str) -> Result<Vec<String>, io::Error> {
    let full_path = format!("./data/day{day_id}/{filename}.txt");
    let file = File::open(full_path)?;
    BufReader::new(file).lines().collect()
}
