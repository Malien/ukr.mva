package lang;

import lex.Token;
import tree.NaryTree;
import tree.NaryTreeNode;

import java.util.ArrayList;

public class Program {

    private ArrayList<? extends Statement> statements;

    public Program(NaryTree<Token> parseTree){
        statements = program(parseTree.connections.get(0));
    }

    private ArrayList<? extends Statement> program(NaryTreeNode<Token> node){
        if (!node.value.token.equals("program"))
            throw new StructuringException("got " + node.value.token + " node, not program arguments node");
        ArrayList<Statement> statements;
        switch (node.connections.size()){
            case 1:
                return new ArrayList<>();
            case 2:
                statements = new ArrayList<>();
                statements.add(func(node.connections.get(0)));
                statements.addAll(program(node.connections.get(1)));
                return statements;
            case 3:
                statements = new ArrayList<>();
                statements.add(statement(node.connections.get(0)));
                statements.addAll(program(node.connections.get(2)));
                return statements;
        }
        throw new StructuringException("Unsupported program node with size " + node.connections.size());
    }

    private ArrayList<String> funcArgs(NaryTreeNode<Token> node){
        if (!node.value.token.equals("argc") && !node.value.token.equals("argv") && !node.value.token.equals("args"))
            throw new StructuringException("got " + node.value.token + " node, not function arguments node");
        if (node.value.token.equals("args")){
            if (node.connections.size() == 2){
                return funcArgs(node.connections.get(1));
            }
        }
        ArrayList<String> args;
        switch (node.connections.size()) {
            case 1:
                return new ArrayList<>();
            case 2:
                args = new ArrayList<>();
                args.add(node.connections.get(1).value.container);
                return args;
            case 3:
                args = new ArrayList<>();
                args.add(node.connections.get(1).value.container);
                args.addAll(funcArgs(node.connections.get(2)));
                return args;
        }
        throw new StructuringException("Unsupported function arguments node with size " + node.connections.size());
    }

    private ArrayList<Expression> exprArgs(NaryTreeNode<Token> node){
        if (!node.value.token.equals("e-args") && !node.value.token.equals("e-argv"))
            throw new StructuringException("got " + node.value.token + " node, not expression arguments node");
        ArrayList<Expression> args;
        switch (node.connections.size()) {
            case 1:
                return new ArrayList<>();
            case 2:
                args = new ArrayList<>();
                args.add(expr(node.connections.get(1)));
                return args;
            case 3:
                args = new ArrayList<>();
                args.add(expr(node.connections.get(1)));
                args.addAll(exprArgs(node.connections.get(2)));
                return args;
        }
        throw new StructuringException("Unsupported expression arguments node with size  " + node.connections.size());
    }

    private Expression expr(NaryTreeNode<Token> node){
        if (!node.value.token.equals("expression"))
            throw new StructuringException("got " + node.value.token + " node, not expression node");
        NaryTreeNode<Token> next = node.connections.get(0);
        switch (next.value.token){
            case "e-call":
                return Expression.call(next.connections.get(2).value.container, exprArgs(next.connections.get(3)));
            case "e-var":
                return Expression.var(
                        (next.connections.size() == 3) ? next.connections.get(2).value.container : next.connections.get(0).value.container
                );
            case "e-sum":
                return Expression.add(expr(next.connections.get(1)), expr(next.connections.get(3)));
            case "number":
                return Expression.number(Integer.valueOf(next.value.container));
            case "string":
                return Expression.string(next.value.container);
        }
        throw new StructuringException("Unsupported statement " + next.value.token);
    }

    private ArrayList<Statement> funcStatements(NaryTreeNode<Token> node){
        if (!node.value.token.equals("f-start"))
            throw new StructuringException("got " + node.value.token + " node, not function start node");
        NaryTreeNode<Token> decide = node.connections.get(1);
        if (decide.connections.size() == 1) {
            ArrayList<Statement> statements = new ArrayList<>();
            statements.add(statement(decide.connections.get(0)));
            return statements;
        } else {
            ArrayList<Statement> statements = new ArrayList<>();
            statements.add(statement(decide.connections.get(1)));
            statements.addAll(funcMulti(decide.connections.get(2)));
            return statements;
        }
    }

    private ArrayList<Statement> funcMulti(NaryTreeNode<Token> node){
        if (!node.value.token.equals("f-multi"))
            throw new StructuringException("got " + node.value.token + " node, not function multi-statement node");
        if (node.connections.size() == 1) return new ArrayList<>();
        else {
            ArrayList<Statement> statements = new ArrayList<>();
            statements.add(statement(node.connections.get(1)));
            statements.addAll(funcMulti(node.connections.get(2)));
            return statements;
        }
    }

    private Statement statement(NaryTreeNode<Token> node){
        if (!node.value.token.equals("statement"))
            throw new StructuringException("got " + node.value.token + " node, not statement node");
        NaryTreeNode<Token> next = node.connections.get(0);
        switch (next.value.token){
            case "s-assign":
                return Statement.var(next.connections.get(2).value.container, expr(next.connections.get(4)));
            case "s-return":
                return Statement.ret(expr(next.connections.get(1)));
            case "s-print":
                return Statement.print(expr(next.connections.get(2)));
            case "s-call":
                return Statement.call(next.connections.get(2).value.container, exprArgs(next.connections.get(3)));
        }
        throw new StructuringException("Unsupported statement " + next.value.token);
    }

    private Function func(NaryTreeNode<Token> node){
        if (!node.value.token.equals("function"))
            throw new StructuringException("got " + node.value.token + " node, not function node");
        String id = node.connections.get(2).value.container;
        ArrayList<String> args = funcArgs(node.connections.get(3));
        ArrayList<Statement> statements = funcStatements(node.connections.get(5));
        return new Function(id, args, statements);
    }

    public ArrayList<? extends Statement> getStatements() {
        return statements;
    }
}
