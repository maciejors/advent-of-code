use std::{env, fs, io};
use std::fs::File;
use std::path::{Path, PathBuf};

fn main() -> Result<(), io::Error> {
    let args: Vec<String> = env::args().collect();
    let day_id = args.get(1)
        .expect("Please provide the Day ID")
        .as_str();
    let boilerplate_code = fs::read_to_string("./data/dayXX.rs.txt")?
        .replace("XX", day_id);

    let rs_file_path = format!("./src/bin/day{}.rs", day_id);
    create_file(rs_file_path, Some(boilerplate_code));

    let data_dir = PathBuf::from(format!("./data/day{}", day_id));
    fs::create_dir_all(&data_dir)?;
    create_file(&data_dir.join("example.txt"), None);
    create_file(&data_dir.join("input.txt"), None);

    Ok(())
}

fn create_file<P: AsRef<Path>>(path: P, content: Option<String>) {
    let path = path.as_ref();
    if path.exists() {
        println!("- Already exists: `{}`", path.display());
        return;
    }
    match File::create(path) {
        Ok(_) => {
            if let Some(content) = content {
                fs::write(path, content).unwrap();
            }
            println!("✔ Created `{}`", path.display());
        },
        Err(_) => println!("✖ Failed to create `{}`", path.display()),
    }
}
