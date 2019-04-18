import java.util.Objects;

public class Token {

    public static final Token EPS = new Token("", true);

    public boolean terminal;
    public String token;
    private String container;

    public Token(boolean terminal, String token, String container) {
        this.terminal = terminal;
        this.token = token;
        this.container = container;
    }

    public Token(String token, boolean terminal) {
        this.terminal = terminal;
        this.token = token;
    }

    public Token(String token){
        this(token, true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token1 = (Token) o;
        return terminal == token1.terminal &&
                Objects.equals(token, token1.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terminal, token);
    }

    @Override
    public String toString() {
        if (container == null) return "{token: " + token + ", " + terminal + "}";
        else return "{" + token + ":" + container + ", " + terminal + "}";
    }
}
