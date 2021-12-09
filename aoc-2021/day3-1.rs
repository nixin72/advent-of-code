use std::fs::File;
use std::io::{BufRead, BufReader};

#[derive(Debug)]
struct Count {
    one: i32,
    zero: i32
}

fn parse_input(file: String) -> Vec<String> {
    let file = BufReader::new(File::open(file).unwrap());
    file.lines().map(|l| {
        l.unwrap()
    }).collect()
}

fn main() {
    let input = parse_input("./input/day3.txt".to_string());

    let mut bits: Vec<Count> = input[0].chars().map(|_| Count {one: 0, zero: 0}).collect();

    for x in input {
        for (index, y) in x.chars().enumerate() {
            match y {
                '1' => bits[index].one += 1,
                '0' => bits[index].zero += 1,
                _ => (),
            };
        }
    }

    let gamma_rate = bits.iter().map(|b| (if b.one > b.zero { "1" } else { "0" }).to_string()).collect::<Vec<String>>().join("");
    let epsil_rate = bits.iter().map(|b| (if b.zero > b.one { "1" } else { "0" }).to_string()).collect::<Vec<String>>().join("");

    let gr = isize::from_str_radix(&gamma_rate, 2).unwrap();
    let er = isize::from_str_radix(&epsil_rate, 2).unwrap();

    println!("{:?}", gr * er);
}
