import sys
import csv

MVA_TABLE_PATH = "mva_table.csv"

DEF_KWD = 0
END_KWD = 1
START_KWD = 2
RETURN_KWD = 3
OUT_KWD = 4
IN_KWD = 5
CONSOLE_KWD = 6
CALL_KWD = 7

def read_rules(path: str):
    f = open(path)
    reader = csv.reader(f.readlines())
    f.close()

def parse(strings: [str], rule: dict = read_rules(MVA_TABLE_PATH)):
    token = []

if __name__ == "__main__":
    if len(sys.argv) == 2:
        f = open(sys.argv[1])
        parse(f.readlines)
        f.close()
