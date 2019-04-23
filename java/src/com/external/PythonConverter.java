package external;

import lang.Expression;
import lang.Function;
import lang.Statement;

import java.util.Optional;

public class PythonConverter implements Translator{
    private static final PythonConverter instance = new PythonConverter();

    private PythonConverter(){}

    public static Translator getInstance() {
        return instance;
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
