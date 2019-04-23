package external;

import lang.Function;
import lang.Program;
import lang.Statement;
import lang.StatementTypes;

import java.io.IOException;
import java.io.Writer;

public class Builder {

    /**
     * Compile to intermediate language using translator rules
     * @param program parsed mva program structure
     * @param translator translator used to provide ruleset to translate program
     * @param out stream to which write output to
     * @throws IOException if problem with stream writing is encountered
     */
    public static void build(Program program, Translator translator, Writer out) throws IOException {
        String preRequisites = translator.preRequisites();
        if (preRequisites != null){
            out.write(preRequisites);
            out.write("\n");
        }
        for (Statement statement : program.getStatements()){
            if (statement.getType() == StatementTypes.FUNCTION){
                Function function = (Function) statement;
                out.write(translator.functionDefinition(function));
                out.write("\n");
                for (Statement funcStatement : function.getStatements()){
                    out.write(translator.buildStatement(funcStatement, 1));
                    out.write("\n");
                }
                String functionEnd = translator.functionEnd();
                if (functionEnd != null){
                    out.write(functionEnd);
                    out.write("\n");
                }
            } else {
                out.write(translator.buildStatement(statement));
                out.write("\n");
            }
        }
        String postRequisites = translator.postRequisites();
        if (postRequisites != null){
            out.write(postRequisites);
            out.write("\n");
        }
        out.flush();
    }
}
