package lang;

import java.util.ArrayList;

public class Function extends Statement{

    private ArrayList<Statement> statements;
    private ArrayList<String> fargs;

    public Function(String id, ArrayList<String> fargs, ArrayList<Statement> statements) {
        super(StatementTypes.FUNCTION, null, id, null);
        this.statements = statements;
        this.fargs = fargs;
    }

    public ArrayList<Statement> getStatements() {
        return statements;
    }

    public ArrayList<String> getFargs() {
        return fargs;
    }
}
