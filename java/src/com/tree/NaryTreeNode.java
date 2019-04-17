package tree;

import java.util.Iterator;
import java.util.LinkedList;

public class NaryTreeNode<E> implements Iterable<NaryTreeNode<E>> {

    public E value;
    public LinkedList<NaryTreeNode<E>> connections;

    public NaryTreeNode(E value){
        this.value = value;
        connections = new LinkedList<>();
    }

    public void add(NaryTreeNode<E> node){
        connections.add(node);
    }

    public void add(E val){
        add(new NaryTreeNode<>(val));
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<NaryTreeNode<E>> iterator() {
        return connections.iterator();
    }

    @Override
    public String toString() {
        return "NaryTreeNode{" +
                "value=" + value +
                '}';
    }
}
