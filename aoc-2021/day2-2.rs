use std::fs::File;
use std::io::{BufRead, BufReader};

enum Direction {
    Forward(i32),
    Down(i32),
    Up(i32),
}

fn to_i32(s: &str) -> i32 {
    s.to_string().parse::<i32>().unwrap()
}

fn file_to_enum(file: String) -> Vec<Direction> {
    let file = BufReader::new(File::open(file).unwrap());
    let mut vec: Vec<Direction> = vec![];

    for line in file.lines() {
        let line = line.unwrap();
        let parsed: Vec<&str> = line.split(' ').collect();
        vec.push(match parsed[..] {
            ["forward", x] => Direction::Forward(to_i32(x)),
            ["down", x] => Direction::Down(to_i32(x)),
            ["up", x] => Direction::Up(to_i32(x)),
            _ => Direction::Forward(0)
        });
    }

    return vec;
}

fn main() {
    let input: Vec<Direction> = file_to_enum("./input/day2.txt".to_string());
    let mut horizontal = 0;
    let mut vertical = 0;
    let mut aim = 0;

    for x in input {
        match x {
            Direction::Forward(x) => {
                horizontal += x;
                vertical += aim * x;
            },
            Direction::Down(x) => aim += x,
            Direction::Up(x) => aim -= x,
        }
    }
    println!("{}", horizontal * vertical);
}
