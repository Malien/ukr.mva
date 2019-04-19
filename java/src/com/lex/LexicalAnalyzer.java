package lex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

public class LexicalAnalyzer {

    private static HashSet<String> RESERVED = new HashSet<>();

    static {
        RESERVED.addAll(Arrays.asList(
                "оголошуємо", "оголосимо", "функцію", "операцію", ",", ".", ";", ":",  "що", "яка", "повертає", "число",
                "виводимо", "вивести", "у", "до", "консоль", "консолі", "результат", "функції", "операції", "над",
                "змінними", "змінною", "та", "і", "виводить", "вивести", "викликає", "викликаємо", "викличемо","сумму",
                "сумма","значення","змінної","значенням"
        ));
    }

    private static boolean isDigit(String str){
        for (int i=0; i<str.length(); i++){
            char ch = str.charAt(i);
            if (!Character.isDigit(ch)) return false;
        }
        return true;
    }

    public static TokenStream convert(String input) {
        String[] split = input.split("(?=[,.:;\"\\\\/*!\\s\n])|(?<=[,.:;\"\\\\/*!\\s\n])");
        LinkedList<Token> formed = new LinkedList<>();

        StringBuilder buffer = null;
        boolean comment = false;
        for (String str : split){
            if (str.equals("\"")){
                if (buffer != null){
                    formed.add(new Token(true, "string", buffer.toString()));
                    buffer = null;
                } else {
                    buffer = new StringBuilder();
                }
                continue;
            }
            if (buffer != null){
                buffer.append(str);
                continue;
            }
            str = str.toLowerCase();
            if (str.equals("примітка")) comment = true;
            if (str.equals("\n")) {
                comment = false;
                continue;
            }
            if (comment) continue;
            if (str.equals(" ")) continue;
            if (isDigit(str)){
                formed.add(new Token(true, "number", str));
                continue;
            }
            if (RESERVED.contains(str)) formed.add(new Token(str));
            else formed.add(new Token(true, "id", str));
        }

        formed.forEach(System.out::println);

        return new TokenStream(formed.toArray(new Token[0]));
    }

    public static TokenStream convert(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        return convert(new String(data, StandardCharsets.UTF_8));
    }


    public static void main(String[] args){
        try {
            convert(new File("/Users/user/Documents/Development/ukr.mva/programm1.mva"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
