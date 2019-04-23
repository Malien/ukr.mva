package external;

import java.util.HashSet;
import java.util.Set;

public class ConsoleArguments {

    private Set<String> options = new HashSet<>();
    private String infile;
    private String outfile;

    public ConsoleArguments(String[] args){
        for (String arg : args){
            if (arg.startsWith("--")) {
                options.add(arg.substring(2));
                continue;
            }
            if (infile == null) {
                infile = arg;
                continue;
            }
            if (outfile == null){
                outfile = arg;
                continue;
            }
            throw new RuntimeException("Too many arguments");
        }
        if (infile == null || outfile == null){
            throw new RuntimeException("Too little arguments");
        }
    }

    public Set<String> getOptions() {
        return options;
    }

    public String getInfile() {
        return infile;
    }

    public String getOutfile() {
        return outfile;
    }
}
