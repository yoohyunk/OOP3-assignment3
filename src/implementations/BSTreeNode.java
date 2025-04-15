package implementations;
import java.io.Serializable;

public class BSTreeNode<E extends Comparable<? super E>> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public E element;
    public BSTreeNode<E> left;
    public BSTreeNode<E> right;
    
    
    public BSTreeNode(E element) {
        this.element = element;
        this.left = null;
        this.right = null;
    }
    public E getElement() { return element; }
    public void setElement(E element) { this.element = element; }

    public BSTreeNode<E> getLeft() { return left; }
    public void setLeft(BSTreeNode<E> left) { this.left = left; }

    public BSTreeNode<E> getRight() { return right; }
    public void setRight(BSTreeNode<E> right) { this.right = right; }
}
