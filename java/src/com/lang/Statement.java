package lang;

import java.util.ArrayList;

public class Statement {

    private StatementTypes type;
    private Expression expression;
    private String id;
    private ArrayList<Expression> args;

    protected Statement(StatementTypes type, Expression expression, String id, ArrayList<Expression> args) {
        this.type = type;
        this.expression = expression;
        this.id = id;
        this.args = args;
    }

    public static Statement var(String id, Expression expr){
        return new Statement(StatementTypes.VAR, expr, id, null);
    }

    public static Statement ret(Expression expr){
        return new Statement(StatementTypes.RETURN, expr, null, null);
    }

    public static Statement print(Expression expr){
        return new Statement(StatementTypes.PRINT, expr, null, null);
    }

    public static Statement call(String id, ArrayList<Expression> args){
        return new Statement(StatementTypes.CALL, null, id, args);
    }

    public StatementTypes getType() {
        return type;
    }

    public Expression getExpression() {
        return expression;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Expression> getArgs() {
        return args;
    }
}
