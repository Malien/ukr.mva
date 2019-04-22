package lang;

import java.util.ArrayList;

public class Expression {

    private ExpressionTypes type;
    private Expression left;
    private Expression right;
    private ArrayList<Expression> args;
    private String strValue;
    private Integer numValue;
    private String id;

    private Expression(ExpressionTypes type, Expression left, Expression right, ArrayList<Expression> args, String strValue, Integer numValue, String id) {
        this.type = type;
        this.left = left;
        this.right = right;
        this.args = args;
        this.strValue = strValue;
        this.numValue = numValue;
        this.id = id;
    }

    public static Expression add(Expression left, Expression right){
        return new Expression(ExpressionTypes.ADD, left, right, null, null, null, null);
    }

    public static Expression call(String id, ArrayList<Expression> args){
        return new Expression(ExpressionTypes.CALL, null, null, args, null, null, id);
    }

    public static Expression number(int value){
        return new Expression(ExpressionTypes.NUMBER, null, null, null, null, value, null);
    }

    public static Expression string(String value){
        return new Expression(ExpressionTypes.STRING, null, null, null, value, null, null);
    }

    public static Expression var(String id){
        return new Expression(ExpressionTypes.VAR, null, null, null, null, null, id);
    }

    public ExpressionTypes getType() {
        return type;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public ArrayList<Expression> getArgs() {
        return args;
    }

    public String getStrValue() {
        return strValue;
    }

    public int getNumValue() {
        return numValue;
    }

    public String getId() {
        return id;
    }
}
