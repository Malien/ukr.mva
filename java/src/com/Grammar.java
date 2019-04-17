import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class Grammar {

    private HashMap<Character, ArrayList<String>> rules;
    private HashSet<Character> terminals;
    private char start;
    private HashMap<String, HashMap<String, ? extends Collection<String>>> parseTable;

    public Grammar() {
        this.rules = new HashMap<>();
        this.terminals = new HashSet<>();
    }
    public Grammar(Collection<Character> terminals, char start){
        this.start = start;
        this.rules = new HashMap<>();
        this.terminals = new HashSet<>(terminals);
    }
    public Grammar(HashSet<Character> terminals, char start, HashMap<Character, ArrayList<String>> rules) {
        this.start = start;
        this.rules = rules;
        this.terminals = terminals;
    }

    public boolean isTerminal(String word){
        for (int i=0; i<word.length(); i++) {
            if (!terminals.contains(word.charAt(i))) return false;
        }
        return true;
    }

    public HashMap<Character, String> firstReplacements(char testedVal){
        HashMap<Character, String> out = new HashMap<>();
        if (!rules.containsKey(testedVal)) throw new SyntaxException("Got unresolved terminal " + testedVal);
        for (String result : rules.get(testedVal)){
            HashSet<Character> res = first(result);
            for (char ch : res){
                out.put(ch, result);
            }
        }
        return out;
    }

    public HashSet<Character> first(char testedVal) {
        HashSet<Character> out = new HashSet<>();
        if (terminals.contains(testedVal)) {
            out.add(testedVal);
            return out;
        }
        if (!rules.containsKey(testedVal)) throw new SyntaxException("Got unresolved terminal " + testedVal);
        for (String replacement : rules.get(testedVal)) {
            if (replacement.length() == 0) {
                out.add((char) 0);
                continue;
            }
            out.addAll(first(replacement));
        }
        return out;
    }

    public void addRules(Character nonTerminal, String[] replacements) {
        for (String replacement : replacements){
            addRule(nonTerminal, replacement);
        }
    }

    public void addRule(Character nonTerminal, String replacement) {
        rules.putIfAbsent(nonTerminal, new ArrayList<>());
        rules.get(nonTerminal).add(replacement);
    }

    public char getStart() {
        return start;
    }

    public void setStart(char start) {
        this.start = start;
    }

    public HashSet<Character> getTerminals() {
        return terminals;
    }

    private HashSet<Character> first(String word){
        HashSet<Character> out = new HashSet<>();
        if (word.isEmpty()) {
            out.add((char) 0);
            return out;
        }
        int i = 0;
        HashSet<Character> result = new HashSet<>();
        while (true){
            if (i >= word.length()) break;
            result.remove((char) 0);
            result.addAll(first(word.charAt(i)));
            if (result.contains((char) 0)) {
                i++;
            } else {
                break;
            }
        }
        out.addAll(result);
        return out;
    }

    public HashMap<Character, String> lastReplacements(char testedVal) {
        HashMap<Character, String> out = new HashMap<>();
        if (!rules.containsKey(testedVal)) throw new SyntaxException("Got unresolved terminal " + testedVal);
        for (String result : rules.get(testedVal)){
            HashSet<Character> res = last(result);
            for (char ch : res){
                out.put(ch, result);
            }
        }
        return out;
    }

    public HashSet<Character> last(String word){
        HashSet<Character> out = new HashSet<>();
        if (word.isEmpty()) {
            out.add((char) 0);
            return out;
        }
        int i = word.length()-1;
        while (i>=0){
            out.remove((char) 0);
            out.addAll(last(word.charAt(i)));
            if (out.contains((char) 0)) {
                i--;
            } else {
                break;
            }
        }
        return out;
    }

    public HashSet<Character> last(char ch){
        HashSet<Character> out = new HashSet<>();
        if (terminals.contains(ch)) {
            out.add(ch);
            return out;
        }
        if (!rules.containsKey(ch)) throw new SyntaxException("Got unresolved terminal " + ch);
        for (String replacement : rules.get(ch)) {
            if (replacement.length() == 0) {
                out.add((char) 0);
                continue;
            }
            out.addAll(last(replacement));
        }
        return out;
    }
}