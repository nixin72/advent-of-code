use std::fs::File;
use std::io::{BufRead, BufReader};

fn file_to_vector(file: String) -> Vec<i32> {
    let file = BufReader::new(File::open(file).unwrap());
    let mut vec: Vec<i32> = vec![];

    for line in file.lines() {
        let line = line.unwrap();
        vec.push(line.parse::<i32>().unwrap());
    }

    return vec;
}

fn main() {
    let input: Vec<i32> = file_to_vector("./input/day1.txt".to_string());

    let mut groups: Vec<Vec<i32>> = vec![];
    for i in 0..=(input.len() - 3) {
        groups.push(vec![input[i], input[i + 1], input[i + 2]]);
    }

    let mut smaller = 0;
    let mut last = None;

    for i in groups {
        let sum = i[0] + i[1] + i[2];
        match last {
            Some(x) => {
                if x < sum {
                    smaller += 1;
                }
            }
            None => (),
        }
        last = Some(sum);
    }

    println!("{}", smaller);
}
