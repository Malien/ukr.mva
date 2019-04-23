package external;

import lang.Program;
import lex.LexicalAnalyzer;
import lex.Token;
import lex.TokenStream;
import tree.NaryTree;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import static syntax.Parser.MVA;

public class Translation {

    private static HashMap<String, Translator> translators = new HashMap<>();

    public static void registerTranslator(String name, Translator translator){
        translators.put(name, translator);
    }

    public static void main(String[] args){
        ConsoleArguments arguments = new ConsoleArguments(args);
        try {
            TokenStream tokenStream = LexicalAnalyzer.convert(arguments.getInfile());

            NaryTree<Token> parseTree = MVA.parseTree(tokenStream);
            Program program = new Program(parseTree);

            Set<String> translatorArgs = translators.keySet();
            translatorArgs.retainAll(arguments.getOptions());
            if (translatorArgs.size() > 1){
                throw new RuntimeException("Cannot convert to multiple languages at once");
            }
            if (translatorArgs.size() == 0){
                throw new RuntimeException("Specify language to which translate to");
            }
            Translator translator = translators.get(translatorArgs.toArray(new String[0])[0]);

            FileWriter out = new FileWriter(arguments.getOutfile());
            Builder.build(program, translator, out);
            out.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

}
