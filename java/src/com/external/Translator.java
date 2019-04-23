package external;

import lang.Function;
import lang.Statement;

public interface Translator {

    String preRequisites();
    String postRequisites();
    String buildStatement(Statement statement, int scopeOffset);
    String functionDefinition(Function function);
    String functionEnd();

    default String buildStatement(Statement statement){
        return buildStatement(statement, 0);
    }
}
