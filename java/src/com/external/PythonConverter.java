package external;

import lang.Expression;
import lang.Function;
import lang.Program;
import lang.Statement;
import lex.LexicalAnalyzer;
import lex.Token;
import lex.TokenStream;
import tree.NaryTree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import static syntax.Parser.MVA;

public class PythonConverter implements Translator{
    private static final PythonConverter instance = new PythonConverter();

    private PythonConverter(){}

    public static Translator getInstance() {
        return instance;
    }

    public static void main(String[] args){
        if (args.length != 2){
            throw new RuntimeException("Wrong argument count");
        }
        //TokenStream tokenStream = LexicalAnalyzer.convert("Оголошуємо функцію а над змінними ікс та ігрик, що повертає сумму значення змінної iкс та значення змінної ігрик. викличемо операцію а над 5 і 7. Оголосимо операцію б над змінною р, що: виводить до консолі результат операції а над значенням змінної р та 5, повертає сумму значення змінної р та 7.");
        try {
            TokenStream tokenStream = LexicalAnalyzer.convert(new File(args[0]));

            NaryTree<Token> parseTree = MVA.parseTree(tokenStream);
            Program program = new Program(parseTree);

            FileWriter out = new FileWriter(args[1]);
            Builder.build(program, getInstance(), out);
            out.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public String preRequisites() {
        return null;
    }

    @Override
    public String postRequisites() {
        return null;
    }

    /**
     * Translates expression structure into python code
     * @param statement statement to be translated
     * @param scopeOffset scope at which statement in
     * @return translated to python code string
     */
    @Override
    public String buildStatement(Statement statement, int scopeOffset) {
        StringBuilder out = new StringBuilder();
        for (int i=0; i<scopeOffset; i++){
            out.append("  ");
        }
        switch (statement.getType()){
            case RETURN:
                out.append("return ");
                out.append(buildExpression(statement.getExpression()));
                return out.toString();
            case CALL:
                out.append(statement.getId());
                out.append('(');
                //My god! Java 8 functions are so beautiful ✨
                statement.getArgs().stream()
                        .map(this::buildExpression)
                        .reduce((cur, next) -> cur + "," + next)
                        .ifPresent(out::append);
                out.append(')');
                return out.toString();
            case VAR:
                out.append(statement.getId());
                out.append('=');
                out.append(buildExpression(statement.getExpression()));
                return out.toString();
            case PRINT:
                out.append("print(");
                out.append(buildExpression(statement.getExpression()));
                out.append(')');
                return out.toString();
            case FUNCTION:
                throw new TranslationException("Asked to translate statement, got function");
        }
        throw new TranslationException("Unsupported statement type: " + statement.getType());
    }

    /**
     * Translates expression structure into python code
     * @param expression expression to be translated
     * @return translated to python code string
     */
    private String buildExpression(Expression expression) {
        StringBuilder out = new StringBuilder();
        switch (expression.getType()){
            case ADD:
                out.append(buildExpression(expression.getLeft()));
                out.append('+');
                out.append(buildExpression(expression.getRight()));
                return out.toString();
            case VAR:
                return expression.getId();
            case CALL:
                out.append(expression.getId());
                out.append('(');
                expression.getArgs().stream()
                        .map(this::buildExpression)
                        .reduce((cur, next) -> cur + "," + next)
                        .ifPresent(out::append);
                out.append(')');
                return out.toString();
            case NUMBER:
                return String.valueOf(expression.getNumValue());
            case STRING:
                out.append('\"');
                out.append(expression.getStrValue());
                out.append('\"');
                return out.toString();
        }
        throw new TranslationException("Unsupported expression type: " + expression.getType());
    }

    /**
     * Provides function definition in Python
     * @param function mva function structure
     * @return python function definition
     */
    @Override
    public String functionDefinition(Function function) {
        Optional<String> opargs = function.getFargs().stream().reduce((cur, next) -> cur + "," + next);
        return "def " + function.getId() + "(" + ((opargs.isPresent()) ? opargs.get() : "")  + "):";
    }

    @Override
    public String functionEnd() {
        return null;
    }
}
