use std::fs::File;
use std::io::{BufRead, BufReader};

fn file_to_vector(file: String) -> Vec<i32> {
    let file = BufReader::new(File::open(file).unwrap());
    let mut vec: Vec<i32> = vec![];

    for (_index, line) in file.lines().enumerate() {
        let line = line.unwrap();
        vec.push(line.parse::<i32>().unwrap());
    }

    return vec;
}

fn main() {
    let input: Vec<i32> = file_to_vector("./input/day1.txt".to_string());

    let mut smaller = 0;
    let mut last = None;

    for i in input {
        match last {
            Some(x) => {
                if x < i {
                    smaller += 1;
                }
            }
            None => (),
        }
        last = Some(i);
    }

    println!("{}", smaller);
}
