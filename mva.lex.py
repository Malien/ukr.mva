import sys
import csv
import re

MVA_TABLE_PATH = "mva_table.csv"

token_splitter = re.compile("[ \n]")
token_re = re.compile(r"[.,]|[абвгджзеіїиклмнопрстухцчшщьюєяАБВГДЖЗЕІЇИКЛМНОПРСТУХЦЧШЩЬЮЄЯ0-9']+")

DEF_KWD = 0
END_KWD = 1
START_KWD = 2
RETURN_KWD = 3
OUT_KWD = 4
IN_KWD = 5
CONSOLE_KWD = 6
CALL_KWD = 7

def tokenizer(string, regex):
    tokens = []
    match = regex.search(string)
    while match != None:
        string = string[match.end():]
        tokens.append(match.group())
        match = regex.search(string)
    return tokens

def read_rules(path):
    rules = {}
    f = open(path)
    reader = csv.reader(f.readlines())
    for r in reader:
        rules[r[0]] = r[1]
    return rules

def get_val(code):
    pch = code.split("-")
    if len(pch) == 1: return code
    if len(pch) == 2:
        if code.startswith("-"): return pch[1]
        else: return pch[0]
    if len(pch) == 2: return pch[1]

def parse(strings, rules = read_rules(MVA_TABLE_PATH)):
    tokens = []
    for s in strings:
        split_line = tokenizer(s, token_re)
        for t in split_line:
            t = t.lower()
            if t in rules:
                change = rules[t]
                if change == "-": 
                    continue
                if change.startswith("-") and get_val(tokens[len(tokens)-1]) == get_val(change):
                    tokens[len(tokens)-1] = change[1:]
                    continue
                tokens.append(change)
            else:
                tokens.append(t)
        tokens.append(END_KWD)
    return tokens

if __name__ == "__main__":
    # if len(sys.argv) == 2:
        # f = open(sys.argv[1])
        f = open("programm1.mva")
        tokens = parse(f.readlines())
        # tokens = tokenizer("hello, world", re.compile(r"[,.]|\w+"))
        print(tokens)
