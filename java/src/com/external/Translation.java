package external;

import lang.Program;
import lex.LexicalAnalyzer;
import lex.Token;
import lex.TokenStream;
import syntax.SyntaxException;
import tree.NaryTree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static syntax.Parser.MVA;

public class Translation {

    private static HashMap<String, Translator> translators = new HashMap<>();

    private static Set<String> possibleOptions = new HashSet<>();

    public static void main(String[] args){
        addOptions();
        translators.put("python", PythonConverter.getInstance());

        ConsoleArguments arguments = new ConsoleArguments(args);
        try {
            Set<String> unsupported = new HashSet<>(arguments.getOptions());
            unsupported.retainAll(possibleOptions);
            unsupported.retainAll(translators.keySet());
            if (unsupported.size() > 0){
                System.err.println("Unknown option(s): " + unsupported);
            }

            Set<String> translatorArgs = translators.keySet();
            translatorArgs.retainAll(arguments.getOptions());
            if (translatorArgs.size() > 1){
                throw new RuntimeException("Cannot convert to multiple languages at once");
            }
            if (translatorArgs.size() == 0){
                throw new RuntimeException("Specify language to which translate to / Specified language is not supported yet");
            }
            Translator translator = translators.get(translatorArgs.toArray(new String[0])[0]);

            TokenStream tokenStream = LexicalAnalyzer.convert(
                    new File(arguments.getInfile()),
                    arguments.getOptions().contains("verbose-lex"));

            NaryTree<Token> parseTree = MVA.parseTree(tokenStream, arguments.getOptions().contains("verbose-syntax"));
            Program program = new Program(parseTree);

            FileWriter out = new FileWriter(arguments.getOutfile());
            Builder.build(program, translator, out);
            out.close();
        } catch (IOException ex){
            ex.printStackTrace();
        } catch (SyntaxException ex){
            System.err.println(ex.getLocalizedMessage());
        } catch (RuntimeException ex){
            System.err.println(ex.getMessage());
        }
    }

    private static void addOptions(){
        possibleOptions.add("verbose-lex");
        possibleOptions.add("verbose-syntax");
    }
}
