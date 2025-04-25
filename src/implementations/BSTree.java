package implementations;

import utilities.BSTreeADT;
import utilities.Iterator;
import java.util.NoSuchElementException;
import java.io.Serializable;

/**
 * A generic Binary Search Tree (BST) implementation using BSTreeNode.
 * Supports standard BST operations and various traversal iterators.
 * Elements must be comparable.
 *
 * @param <E> the type of elements in this tree, which must implement Comparable
 */
public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E>, Serializable {
    private static final long serialVersionUID = 1L;
    private BSTreeNode<E> root;
    private int size;

    
    /**
	 * Constructs an empty Binary Search Tree.
	 */
    public BSTree() {
        this.root = null;
        this.size = 0;
    }

    
    /**
     * Constructs a BST with a single element as the root.
     *
     * @param rootData the element to be the root
     */
    public BSTree(E rootData) {
        this.root = new BSTreeNode<>(rootData);
        this.size = 1;
    }

    
    /**
     * Returns the root node of the tree.
     *
     * @return the root node
     * @throws NullPointerException if the tree is empty
     */
	@Override
    public BSTreeNode<E> getRoot() throws NullPointerException {
        if (root == null) {
            throw new NullPointerException("The tree is empty.");
        }
        return root;
    }
	
	
	/**
	 * Returns the height of the tree (number of levels).
	 * 
     * @return the height of the tree
     */
    @Override
    public int getHeight() {
        return getHeight(root);
    }
    
    private int getHeight(BSTreeNode<E> node) {
        if (node == null)
            return 0;
        return 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    
    /**
     * Returns the number of elements in the tree.
     * 
     * @return the number of elements in the tree
     */
    @Override
    public int size() {
        return size;
    }

    
    /**
     * Checks if the tree is empty.
     * 
	 * @return true if the tree is empty, false otherwise
	 */
    @Override
    public boolean isEmpty() {
        return (size == 0);
    }
    
    
    /**
     * Clears all elements from the tree.
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    
    /**
     * Checks if the tree contains the specified element.
     *
     * @param entry the element to search for
     * @return true if the element is found
     * @throws NullPointerException if entry is null
     */
    @Override
    public boolean contains(E entry) throws NullPointerException {
        if (entry == null) {
            throw new NullPointerException("Entry cannot be null.");
        }
        return search(entry) != null;
    }

    
    /**
     * Searches for a node containing the specified element.
     *
     * @param entry the element to search for
     * @return the node containing the element, or null if not found
     * @throws NullPointerException if entry is null
     */
    @Override
    public BSTreeNode<E> search(E entry) throws NullPointerException {
        if (entry == null) {
            throw new NullPointerException("Entry cannot be null.");
        }
        return search(root, entry);
    }
    
    private BSTreeNode<E> search(BSTreeNode<E> node, E entry) {
        if (node == null) {
            return null;
        }
        int cmp = entry.compareTo(node.element);
        if (cmp == 0) {
            return node;
        } else if (cmp < 0) {
            return search(node.left, entry);
        } else {
            return search(node.right, entry);
        }
    }
    
    /**
     * Adds an element to the tree.
     *
     * @param newEntry the element to add
     * @return true if added successfully
     * @throws NullPointerException if element is null
     */
    @Override
    public boolean add(E newEntry) throws NullPointerException {
        if (newEntry == null) {
            throw new NullPointerException("New entry cannot be null.");
        }
        if (root == null) {
            root = new BSTreeNode<>(newEntry);
            size++;
            return true;
        }
        if (add(root, newEntry)) {
            size++;
            return true;
        }
        return false;
    }
    
    private boolean add(BSTreeNode<E> node, E newEntry) {
        int cmp = newEntry.compareTo(node.element);
        if (cmp == 0) {
            return false;
        } else if (cmp < 0) {
            if (node.left == null) {
                node.left = new BSTreeNode<>(newEntry);
                return true;
            } else {
                return add(node.left, newEntry);
            }
        } else {
            if (node.right == null) {
                node.right = new BSTreeNode<>(newEntry);
                return true;
            } else {
                return add(node.right, newEntry);
            }
        }
    }
    
    
    /**
     * Removes and returns the node with the minimum element in the tree.
     *
     * @return the removed node, or null if the tree is empty
     */

    @Override
    public BSTreeNode<E> removeMin() {
        if (root == null) {
            return null;
        }
        BSTreeNode<E> minNode = root;
        BSTreeNode<E> parent = null;
        while (minNode.left != null) {
            parent = minNode;
            minNode = minNode.left;
        }
        if (parent == null) { 
            root = root.right;
        } else {
            parent.left = minNode.right;
        }
        size--;
        return minNode;
    }
    
    
    /**
     * Removes and returns the node with the maximum element in the tree.
     *
     * @return the removed node, or null if the tree is empty
     */
    @Override
    public BSTreeNode<E> removeMax() {
        if (root == null) {
            return null;
        }
        BSTreeNode<E> maxNode = root;
        BSTreeNode<E> parent = null;
        while (maxNode.right != null) {
            parent = maxNode;
            maxNode = maxNode.right;
        }
        if (parent == null) { 
            root = root.left;
        } else {
            parent.right = maxNode.left;
        }
        size--;
        return maxNode;
    }
    
    
    /**
     * Returns an iterator for in-order traversal of the tree.
     *
     * @return an in-order iterator
     */
    @Override
    public Iterator<E> inorderIterator() {
        return new InorderIterator();
    }
    
    
    /**
     * Returns an iterator for pre-order traversal of the tree.
     *
     * @return a pre-order iterator
     */
    @Override
    public Iterator<E> preorderIterator() {
        return new PreorderIterator();
    }
    
    
    /**
     * Returns an iterator for post-order traversal of the tree.
     *
     * @return a post-order iterator
     */
    @Override
    public Iterator<E> postorderIterator() {
        return new PostorderIterator();
    }
       
   
    private class InorderIterator implements Iterator<E> {
        private java.util.Stack<BSTreeNode<E>> stack;
        
        /**
         * Constructs an in-order iterator starting from the root.
         */
        public InorderIterator() {
            stack = new java.util.Stack<>();
            BSTreeNode<E> current = root;
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
        }
        
        /**
         * Checks if there are more elements in the traversal.
         *
         * @return true if there are more elements
         */
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }
        
        /**
         * Returns the next element in the in-order traversal.
         *
         * @return the next element
         * @throws NoSuchElementException if there are no more elements
         */
        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements.");
            }
            BSTreeNode<E> node = stack.pop();
            E result = node.element;
            if (node.right != null) {
                BSTreeNode<E> curr = node.right;
                while (curr != null) {
                    stack.push(curr);
                    curr = curr.left;
                }
            }
            return result;
        }
    }
    
    private class PreorderIterator implements Iterator<E> {
        private java.util.Stack<BSTreeNode<E>> stack;
        
        /**
         * Constructs an pre-order iterator starting from the root.
         */
        public PreorderIterator() {
            stack = new java.util.Stack<>();
            if (root != null) {
                stack.push(root);
            }
        }
        
        /**
         * Checks if there are more elements in the traversal.
         *
         * @return true if there are more elements
         */
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }
        
        
        /**
         * Returns the next element in the pre-order traversal.
         *
         * @return the next element
         * @throws NoSuchElementException if there are no more elements
         */
        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements.");
            }
            BSTreeNode<E> node = stack.pop();
            E result = node.element;
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
            return result;
        }
    }
    
    private class PostorderIterator implements Iterator<E> {
        private java.util.Stack<BSTreeNode<E>> stack1;
        private java.util.Stack<BSTreeNode<E>> stack2;
        
        /**
         * Constructs an post-order iterator starting from the root.
         */
        public PostorderIterator() {
            stack1 = new java.util.Stack<>();
            stack2 = new java.util.Stack<>();
            if (root != null) {
                stack1.push(root);
                while (!stack1.isEmpty()) {
                    BSTreeNode<E> node = stack1.pop();
                    stack2.push(node);
                    if (node.left != null) {
                        stack1.push(node.left);
                    }
                    if (node.right != null) {
                        stack1.push(node.right);
                    }
                }
            }
        }
        
        /**
         * Checks if there are more elements in the traversal.
         *
         * @return true if there are more elements
         */
        @Override
        public boolean hasNext() {
            return !stack2.isEmpty();
        }
        
        /**
         * Returns the next element in the post-order traversal.
         *
         * @return the next element
         * @throws NoSuchElementException if there are no more elements
         */
        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements.");
            }
            return stack2.pop().element;
        }
    }
}
