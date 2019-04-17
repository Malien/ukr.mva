package tree;

public class NaryTree<E> extends NaryTreeNode<E> {

    public NaryTree() {
        super(null);
    }

    public void add(NaryTree<E> tree){
        for (NaryTreeNode<E> node : this.connections) {
            add(node);
        }
    }

    @Override
    public String toString() {
        return "NaryTree{" +
                "connections=" + connections +
                '}';
    }
}
