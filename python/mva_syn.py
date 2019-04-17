from mva_lex import tokenizer

S = 'S.'
F = 'F.'
T = 'T.'
N = 'N.'
e = ''

# grammar = {
#     S: [(F, S), (T, N, S), e],
#     N: ['.', ';'],
#     F: []
# }

class TreeNode:
    def __init__(self, value, isTerminal = True, parent = None):
        self.value = value
        self.isTerminal = isTerminal
        self.parent = parent
        self.connections = []
    
    def append(self, node):
        node.parent = self
        self.connections.append(node)

    def __len__(self):
        length = 1
        for child in self.connections:
            length += len(child)
        return length

    def __repr__(self):
        return "TreeNode(" + str(self.value) + ", " + str(self.connections) + ")"

class Grammar:
    def __init__(self, grammar = {}):
        self.grammar = grammar

    def build_table(self):
        self.parse_table = {}

    def first(self, non_terminal):
        self.grammar

class LLParser:
    def __init__(self, grammar:Grammar):
        self.grammar = grammar
        if (grammar.parse_table == None):
            grammar.build_table()
    
    def replacement(self, non_terminal):
        return self.grammar.parse_table[non_terminal]

    def parse_tree(self, string):
        pass

def main():
    tokens = tokenizer(open("programm1.mva").read())
    root = TreeNode("prog", False)
    

if __name__ == "__main__":
    main()