package syntax;

import lex.LexicalAnalyzer;
import lex.Token;
import lex.TokenStream;
import tree.NaryTree;
import tree.NaryTreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * implementation of LL(1) parser.
 */
public class Parser {

    public static final Parser MVA = new Parser(Grammar.MVA);

    private Grammar grammar;

    public Parser(Grammar grammar){
        this.grammar = grammar;
    }

    public static void main(String[] args){
//        lex.Token start = new lex.Token("Start", false);
//        lex.Token a     = new lex.Token("a",      true);
//        lex.Token b     = new lex.Token("b",      true);
//        lex.Token E     = new lex.Token("E",     false);
//        lex.Token B     = new lex.Token("B",     false);
//        syntax.Grammar gram = new syntax.Grammar(start);
//        gram.addRules("Start", new lex.Token[][]{{a, E},{B}});
//        gram.addRules("B",     new lex.Token[][]{{b}   ,{lex.Token.EPS}});
//        gram.addRules("E",     new lex.Token[][]{{B}   ,{a,B}});

        TokenStream tokenStream = LexicalAnalyzer.convert("Оголошуємо функцію а над змінними ікс та ігрик, що повертає сумму значення змінної а та значення змінної б. викличемо операцію а. Оголосимо операцію б над змінною р, що: виводить до консолі результат операції а над значенням змінної б та 5, повертає сумму значення змінної б та 7.");

        NaryTree<Token> parseTree = MVA.parseTree(tokenStream);
        System.out.println(parseTree);
    }

    public NaryTree<Token> parseTree(TokenStream stream) throws SyntaxException {
        Stack<NaryTreeNode<Token>> parsed = new Stack<>();
        HashMap<String, HashMap<String, Token[]>> parseTable = grammar.getParseTable();

        NaryTree<Token> root = new NaryTree<>();
        NaryTreeNode<Token> start = new NaryTreeNode<>(grammar.getStart());
        root.add(start);
        parsed.add(start);

        while (!parsed.empty()){
            NaryTreeNode<Token> node = parsed.pop();
            Token current = stream.peek();
            System.out.println("parsing: " + current + " -> " + node.value);
            if (node.value == Token.EPS) continue;
            if (node.value.equals(current)){
                stream.pop();
            } else {
                if (!parseTable.containsKey(node.value.token)) {
                    throw new SyntaxException("Found unexpected " + node.value.token);
                }
                HashMap<String, Token[]> replacements = parseTable.get(node.value.token);

                Token[] replacement;
                if (current == null){
                    if (replacements.containsKey("")){
                        replacement = replacements.get("");
                    } else {
                        throw new SyntaxException("Expected "+ replacements.keySet() + ", but found EOF");
                    }
                } else {
                    if (!replacements.containsKey(current.token)) {
                        if (replacements.containsKey("")) {
                            replacement = replacements.get("");
                        } else {
                            throw new SyntaxException("Expected " + replacements.keySet() + ", but found " + current.token);
                        }
                    } else {
                        replacement = replacements.get(current.token);
                    }
                }
                ArrayList<NaryTreeNode<Token>> nodes = new ArrayList<>();
                for (Token token : replacement){
                    nodes.add(new NaryTreeNode<>(token));
                }
                for (int i=0; i<nodes.size(); i++){
                    node.add(nodes.get(i));
                    parsed.add(nodes.get(nodes.size()-1-i));
                }
            }
        }

        return root;
    }

}
