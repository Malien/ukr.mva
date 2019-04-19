package lex;

public class TokenStream {

    Token[] tokens;
    int position = 0;

    public TokenStream(Token[] tokens){
        this.tokens = tokens;
    }

    public Token peek(){
        if (position >= tokens.length) return null;
        return tokens[position];
    }

    public Token pop(){
        if (position >= tokens.length) return null;
        return tokens[position++];
    }

    public boolean hasNext(){
        return position < tokens.length;
    }

}
