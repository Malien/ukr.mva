import tree.NaryTree;
import tree.NaryTreeNode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * implementation of LL(1) parser.
 */
public class Parser {

    protected Grammar grammar;

    public Parser(Grammar grammar){
        this.grammar = grammar;
    }

//    public boolean analysis(String testedVal){
//        String parseString = "" + grammar.getStart();
//        while (!testedVal.equals(parseString)){
//            if (grammar.isTerminal(parseString)) return false;
//            try {
//                parseString = step(parseString, testedVal);
//            } catch (SyntaxException ex) {
//                ex.printStackTrace();
//                return false;
//            }
//        }
//        return true;
//    }

//    public NaryTree<Token> parseTree(Stream<Token> val) throws SyntaxException{
//        NaryTree<Token> tree = new NaryTree<>();
//        tree.add(grammar.getStart());
////        int index = step(tree, val);
////        if (index != val.length()) throw new SyntaxException("Expected EOL, but found more tokens");
//        return tree;
//    }

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
            if (node.value.equals(current)){
                stream.pop();
            } else {
                if (!parseTable.containsKey(node.value.token)) {
                    throw new SyntaxException("Found unexpected " + node.value.token);
                }
                HashMap<String, Token[]> replacements = parseTable.get(node.value.token);
                if (!replacements.containsKey(current.token)) {
                    throw new SyntaxException("Expected " + replacements.keySet() + ", but found " + current.token);
                }
                Token[] replacement = replacements.get(current.token);
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

    public static void main(String[] args){
        Token start = new Token("Start", false);
        Token a     = new Token("a",      true);
        Token b     = new Token("b",      true);
        Token E     = new Token("E",     false);
        Token B     = new Token("B",     false);
        Grammar gram = new Grammar(start);
        gram.addRules("Start", new Token[][]{{a, E},{B}});
        gram.addRules("B",     new Token[][]{{b}   ,{Token.EPS}});
        gram.addRules("E",     new Token[][]{{B}   ,{a,B}});

        Parser parser = new Parser(gram);

        TokenStream tokenStream = new TokenStream(new Token[]{a,a,b});

        NaryTree<Token> parseTree = parser.parseTree(tokenStream);
        System.out.println(parseTree);
    }

//    private int step(NaryTreeNode<Token> input, Stream<Token> tokenStream) throws SyntaxException {
//        for (NaryTreeNode<Token> node : input){
//            if (!terminals.contains(node.value)) {
//                HashMap<Character, String> replacements = grammar.firstReplacements(node.value);
//                if (testedVal.length() <= index) {
//                    if (replacements.containsKey((char) 0)) {
//                        node.add((char) 0);
//                        continue;
//                    }
//                    throw new SyntaxException("Reached EOL and couldn't resolve non-terminal " + node.value);
//                }
//                char next = testedVal.charAt(index);
//                if (!replacements.containsKey(next)) {
//                    if (replacements.containsKey((char) 0)) {
//                        node.add((char) 0);
//                        continue;
//                    }
//                    throw new SyntaxException("Expected "+ replacements.keySet() + " , but found "+ next);
//                }
//                String replacement = replacements.get(next);
//                if (replacement.length() == 0){
//                    node.add((char) 0);
//                }
//                for (int i=0; i<replacement.length(); i++){
//                    node.add(replacement.charAt(i));
//                }
//                index = step(node, testedVal, index);
//            } else {
//                index++;
//            }
//        }
//        return index;
//    }

//    protected String step(String input, String testedVal) throws SyntaxException{
//        for (int i=0; i<input.length(); i++){
//            char ch = input.charAt(i);
//            if (!terminals.contains(ch)){
//                HashMap<Character, String> map = grammar.firstReplacements(ch);
//                char next = (i < testedVal.length()) ? testedVal.charAt(i) : (char) 0;
//                if (!map.containsKey(next)) {
//                    if (map.containsKey((char) 0)) {
//                        return input.replaceFirst(String.valueOf(ch), "");
//                    }
//                    throw new SyntaxException("Expected " + map.keySet() + ", but found " + next);
//                }
//                return input.replaceFirst(String.valueOf(ch), map.get(next));
//            }
//        }
//        return input;
//    }
}
