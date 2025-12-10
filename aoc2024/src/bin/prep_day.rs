use std::{env, fs, io};
use std::fs::File;
use std::path::PathBuf;

fn main() -> Result<(), io::Error> {
    let args: Vec<String> = env::args().collect();
    let day_id = args.get(1)
        .expect("Please provide the Day ID")
        .as_str();
    let boilerplate_code = fs::read_to_string("./data/dayXX.rs.txt")?
        .replace("XX", day_id);

    fs::write(format!("./src/bin/day{}.rs", day_id), boilerplate_code)?;

    let data_dir = PathBuf::from(format!("./data/day{}", day_id));
    fs::create_dir_all(&data_dir)?;
    File::create(&data_dir.join("example.txt"))?;
    File::create(&data_dir.join("input.txt"))?;
    Ok(())
}