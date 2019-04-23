package syntax;

import lex.Token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class Grammar {

    public static final Grammar MVA = new Grammar();

    static{
        Token program    = new Token("program", false);
        Token function   = new Token("function", false);
        Token statement  = new Token("statement", false);
        Token expression = new Token("expression", false);

        Token id     = new Token("id");
        Token number = new Token("number");
        Token string = new Token ("string");

        Token dot       = new Token(".");
        Token comma     = new Token(",");
        Token dotcomma  = new Token(";");
        Token doubledot = new Token(":");
        Token to        = new Token("до");

        Token PStatementEnd = new Token("p-statement-end", false);
        Token func          = new Token("func", false);
        Token args          = new Token("args", false);
        Token argc          = new Token("argc", false);
        Token argv          = new Token("argv", false);
        Token FStart        = new Token("f-start", false);
        Token FDecide       = new Token("f-decide", false);
        Token FMulti        = new Token("f-multi", false);
        Token SReturn       = new Token("s-return", false);
        Token SAssign       = new Token("s-assign", false);
        Token SPrint        = new Token("s-print", false);
        Token SCall         = new Token("s-call", false);
        Token SPrintConsole = new Token("s-print-console", false);
        //TODO: if while
        Token Eargs         = new Token("e-args", false);
        Token Eargv         = new Token("e-argv", false);
        Token EVar          = new Token("e-var", false);
        Token ECall         = new Token("e-call", false);
        Token ESum          = new Token("e-sum", false);
        Token and           = new Token("and", false);
        Token onFunc        = new Token("on-func", false);
        //TODO: Mul, Sub, Div, Mod

        MVA.setStart(program);
        MVA.addRules("program", new Token[][] {
                {Token.EPS},
                {function, program},
                {statement, PStatementEnd, program}
        });
        MVA.addRules("p-statement-end", new Token[][] {
                {dot},
                {dotcomma}
        });
        MVA.addRules("function", new Token[][] {
                {new Token("оголосимо"), func, id, args, comma, FStart, dot},
                {new Token("оголошуємо"), func, id, args, comma, FStart, dot}
        });
        MVA.addRules("func", new Token[][]{
                {new Token("функцію")},
                {new Token("операцію")}
        });
        MVA.addRules("on-func", new Token[][]{
                {new Token("функції")},
                {new Token("операції")}
        });
        MVA.addRules("args", new Token[][] {
                {Token.EPS},
                {new Token("над"), argc},
        });
        MVA.addRules("argc", new Token[][] {
                {new Token("змінною"), id},
                {new Token("значенням"), id},
                {new Token("змінними"), id, argv},
                {new Token("значеннями"), id, argv}
        });
        MVA.addRules("argv", new Token[][] {
                {comma, id, argv},
                {and, id},
                {Token.EPS}
        });
        MVA.addRules("and", new Token[][]{
                {new Token("та")},
                {new Token("і")}
        });
        MVA.addRules("f-start", new Token[][] {
                {new Token("що"), FDecide},
                {new Token("яка"), FDecide}
        });
        MVA.addRules("f-decide", new Token[][] {
                {doubledot, statement, FMulti},
                {statement}
        });
        MVA.addRules("f-multi", new Token[][] {
                {Token.EPS},
                {comma, statement, FMulti}
        });
        MVA.addRules("statement", new Token[][] {
                {SReturn},
                {SAssign},
                {SCall},
                {SPrint}
        });
        MVA.addRule("s-return", new Token[]{new Token("повертає"), expression});
        MVA.addRule("s-assign", new Token[]{to, new Token("змінної"), id, new Token("внести"), expression});
        MVA.addRules("s-print", new Token[][] {
                {new Token("вивести"), SPrintConsole, expression},
                {new Token("виводить"), SPrintConsole, expression},
                {new Token("виводимо"), SPrintConsole, expression}
        });
        MVA.addRules("s-print-console", new Token[][] {
                {to, new Token("консолі")},
                {new Token("у"), new Token("консоль")}
        });
        MVA.addRules("s-call", new Token[][] {
                {new Token("викликає"), func, id, Eargs},
                {new Token("викликаємо"), func, id, Eargs},
                {new Token("викличемо"), func, id, Eargs}
        });
        MVA.addRules("e-args", new Token[][] {
                {Token.EPS},
                {new Token("над"), expression, Eargv},
        });
        MVA.addRules("e-argv", new Token[][] {
                {comma, expression, Eargv},
                {and, expression},
                {Token.EPS}
        });
        MVA.addRules("expression", new Token[][] {
                {EVar},
                {ECall},
                {ESum},
                {number},
                {string}
        });
        MVA.addRules("e-var", new Token[][]{
                {new Token("значення"), new Token("змінної"), id},
                {new Token("значенням"), new Token("змінної"), id},
                {id}
        });
        MVA.addRule("e-call", new Token[]{new Token("результат"), onFunc, id, Eargs});
        MVA.addRules("e-sum", new Token[][] {
                {new Token("сумму"), expression, and, expression},
                {new Token("сумма"), expression, and, expression}
        });
    }

    private HashMap<String, ArrayList<Token[]>> rules;
    private Token start;
    private HashMap<String, HashMap<String, Token[]>> parseTable;
    private boolean changed = true;

    public Grammar() {
        this.rules = new HashMap<>();
    }

    public Grammar(Token start, HashMap<String, ArrayList<Token[]>> rules) {
        this.start = start;
        this.rules = rules;
    }

    public Grammar(Token start){
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