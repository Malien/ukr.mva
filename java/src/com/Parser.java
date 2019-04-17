import tree.NaryTree;
import tree.NaryTreeNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * implementation of LL(1) parser.
 */
public class Parser {

    protected Grammar grammar;
    protected HashSet<Character> terminals;

    public Parser(Grammar grammar){
        this.grammar = grammar;
        this.terminals = grammar.getTerminals();
    }

    public boolean analysis(String testedVal){
        String parseString = "" + grammar.getStart();
        while (!testedVal.equals(parseString)){
            if (grammar.isTerminal(parseString)) return false;
            try {
                parseString = step(parseString, testedVal);
            } catch (SyntaxException ex) {
                ex.printStackTrace();
                return false;
            }
        }
        return true;
    }

    NaryTree global;

    public NaryTree<Character> parseTree(String val) throws SyntaxException{
        NaryTree<Character> tree = new NaryTree<>();
        global = tree;
        tree.add(grammar.getStart());
        int index = step(tree, val, 0);
        if (index != val.length()) throw new SyntaxException("Expected EOL, but found more tokens");
        return tree;
    }

    protected int step(NaryTreeNode<Character> input, String testedVal, int index) throws SyntaxException {
        for (NaryTreeNode<Character> node : input){
            if (!terminals.contains(node.value)) {
                HashMap<Character, String> replacements = grammar.firstReplacements(node.value);
                if (testedVal.length() <= index) {
                    if (replacements.containsKey((char) 0)) {
                        node.add((char) 0);
                        continue;
                    }
                    throw new SyntaxException("Reached EOL and couldn't resolve non-terminal " + node.value);
                }
                char next = testedVal.charAt(index);
                if (!replacements.containsKey(next)) {
                    if (replacements.containsKey((char) 0)) {
                        node.add((char) 0);
                        continue;
                    }
                    throw new SyntaxException("Expected "+ replacements.keySet() + " , but found "+ next);
                }
                String replacement = replacements.get(next);
                if (replacement.length() == 0){
                    node.add((char) 0);
                }
                for (int i=0; i<replacement.length(); i++){
                    node.add(replacement.charAt(i));
                }
                index = step(node, testedVal, index);
            } else {
                index++;
            }
        }
        return index;
    }

    protected String step(String input, String testedVal) throws SyntaxException{
        for (int i=0; i<input.length(); i++){
            char ch = input.charAt(i);
            if (!terminals.contains(ch)){
                HashMap<Character, String> map = grammar.firstReplacements(ch);
                char next = (i < testedVal.length()) ? testedVal.charAt(i) : (char) 0;
                if (!map.containsKey(next)) {
                    if (map.containsKey((char) 0)) {
                        return input.replaceFirst(String.valueOf(ch), "");
                    }
                    throw new SyntaxException("Expected " + map.keySet() + ", but found " + next);
                }
                return input.replaceFirst(String.valueOf(ch), map.get(next));
            }
        }
        return input;
    }
}
