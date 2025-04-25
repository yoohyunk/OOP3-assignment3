package implementations;
import java.io.Serializable;


/**
 * Represents a node in a binary search tree (BST). 
 * Stores an element and references to left/right child nodes.
 *
 * @param <E> the type of element stored in the node, which must be Comparable
 */
public class BSTreeNode<E extends Comparable<? super E>> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public E element;
    public BSTreeNode<E> left;
    public BSTreeNode<E> right;
    
    
    /**
     * Constructs a BST node with the specified element.
     *
     * @param element the element to store in this node
     */
    public BSTreeNode(E element) {
        this.element = element;
        this.left = null;
        this.right = null;
    }
    
    /**
     * Returns the element stored in this node.
     *
     * @return the element in this node
     */
    public E getElement() { return element; }
    
    /**
     * Sets the element of this node.
     *
     * @param element the new element to store
     */
    public void setElement(E element) { this.element = element; }

    /**
     * Returns the left child node.
     *
     * @return the left child, or null if none exists
     */
    public BSTreeNode<E> getLeft() { return left; }
    
    /**
     * Sets the left child node.
     *
     * @param left the new left child node
     */
    public void setLeft(BSTreeNode<E> left) { this.left = left; }

    /**
     * Returns the right child node.
     *
     * @return the right child, or {@code null} if none exists
     */
    public BSTreeNode<E> getRight() { return right; }
    
    /**
     * Sets the right child node.
     *
     * @param right the new right child node
     */
    public void setRight(BSTreeNode<E> right) { this.right = right; }
}
