package implementations;

import utilities.BSTreeADT;
import utilities.Iterator;
import java.util.NoSuchElementException;
import java.io.Serializable;


public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E>, Serializable {
    private static final long serialVersionUID = 1L;
    private BSTreeNode<E> root;
    private int size;

    public BSTree() {
        this.root = null;
        this.size = 0;
    }

    public BSTree(E rootData) {
        this.root = new BSTreeNode<>(rootData);
        this.size = 1;
    }

	@Override
    public BSTreeNode<E> getRoot() throws NullPointerException {
        if (root == null) {
            throw new NullPointerException("The tree is empty.");
        }
        return root;
    }

    @Override
    public int getHeight() {
        return getHeight(root);
    }
    
    private int getHeight(BSTreeNode<E> node) {
        if (node == null)
            return 0;
        return 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean contains(E entry) throws NullPointerException {
        if (entry == null) {
            throw new NullPointerException("Entry cannot be null.");
        }
        return search(entry) != null;
    }

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
    
    @Override
    public Iterator<E> inorderIterator() {
        return new InorderIterator();
    }
    
    @Override
    public Iterator<E> preorderIterator() {
        return new PreorderIterator();
    }
    
    @Override
    public Iterator<E> postorderIterator() {
        return new PostorderIterator();
    }
    
    private class InorderIterator implements Iterator<E> {
        private java.util.Stack<BSTreeNode<E>> stack;
        
        public InorderIterator() {
            stack = new java.util.Stack<>();
            BSTreeNode<E> current = root;
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
        }
        
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }
        
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
        
        public PreorderIterator() {
            stack = new java.util.Stack<>();
            if (root != null) {
                stack.push(root);
            }
        }
        
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }
        
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
        
        @Override
        public boolean hasNext() {
            return !stack2.isEmpty();
        }
        
        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements.");
            }
            return stack2.pop().element;
        }
    }
}
