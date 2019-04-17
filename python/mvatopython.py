import mvaparser

def append(out, kwd, indentlvl):
    if out[len(out) - 1] == "\n": out += "".join("    " for _ in range(indentlvl)) + kwd
    else: out += kwd

def translate(parsed):
    indentlvl = 0
    out = ""
    i = 0

def save(string, path):
    f = open(path, 'x')
    f.write(string)

if __name__ == "__main__":
    save(translate(mvaparser.parse(open("programm1.mva").readlines())), "out.py")