import sys

DEF_KWD = 0
END_KWD = 1
START_KWD = 2
RETURN_KWD = 3

def parse(string: [str]):
    token = []
    pass

if __name__ == "__main__":
    if len(sys.argv) == 2:
        file = open(sys.argv[1])
        parse(file.readlines)
