import java.util.*;
import java.util.stream.Collectors;

public class Grammar {

    private HashMap<String, ArrayList<Token[]>> rules;
    private Token start;
    private HashMap<String, HashMap<String, Token[]>> parseTable;
    private boolean changed = true;

    public Grammar() {
        this.rules = new HashMap<>();
//        this.terminals = new HashSet<>();
    }

    public Grammar(Token start, HashMap<String, ArrayList<Token[]>> rules) {
        this.start = start;
        this.rules = rules;
//        this.terminals = terminals;
    }

    public Grammar(Token start){
//        this.terminals = terminals.stream().map(Token::new).collect(Collectors.toSet());
        this.start = start;
        this.rules = new HashMap<>();
    }

    public static boolean isTerminal(Collection<Token> word){
        for (Token token : word){
            if (!token.terminal) return false;
        }
        return true;
    }

    public HashMap<String, Token[]> firstReplacements(String testedVal){
        HashMap<String, Token[]> out = new HashMap<>();
        if (!rules.containsKey(testedVal)) throw new SyntaxException("Got unresolved terminal " + testedVal);
        for (Token[] result : rules.get(testedVal)){
            HashSet<Token> res = first(result);
            for (Token ch : res){
                out.put(ch.token, result);
            }
        }
        return out;
    }

    public HashSet<Token> first(Token testedVal) {
        HashSet<Token> out = new HashSet<>();
        if (testedVal.terminal) {
            out.add(testedVal);
            return out;
        }
        if (!rules.containsKey(testedVal.token)) throw new SyntaxException("Got unresolved terminal " + testedVal.token);
        for (Token[] replacement : rules.get(testedVal.token)) {
            if (replacement.length == 0) {
                out.add(Token.EPS);
                continue;
            }
            out.addAll(first(replacement));
        }
        return out;
    }

    public void addRules(String nonTerminal, Token[][] replacements) {
        for (Token[] replacement : replacements){
            addRule(nonTerminal, replacement);
        }
    }

    public void addRule(String nonTerminal, Token[] replacement) {
        changed = true;
        rules.putIfAbsent(nonTerminal, new ArrayList<>());
        rules.get(nonTerminal).add(replacement);
    }

    public Token getStart() {
        return start;
    }

    public void setStart(Token start) {
        this.start = start;
    }

    public HashMap<String, HashMap<String, Token[]>> getParseTable(){
        if (parseTable == null){
            parseTable = new HashMap<>();
        }
        if (changed){
            for (String nonTerminal : rules.keySet()){
                parseTable.put(nonTerminal, firstReplacements(nonTerminal));
            }
            changed = false;
        }
        return parseTable;
    }

//    public Set<Token> getTerminals() {
//        return terminals;
//    }

    private HashSet<Token> first(Token[] word){
        HashSet<Token> out = new HashSet<>();
        if (word.length == 0) {
            out.add(Token.EPS);
            return out;
        }
        int i = 0;
        HashSet<Token> result = new HashSet<>();
        while (true){
            if (i >= word.length) break;
            result.remove(Token.EPS);
            result.addAll(first(word[i]));
            if (result.contains(Token.EPS)) {
                i++;
            } else {
                break;
            }
        }
        out.addAll(result);
        return out;
    }

}