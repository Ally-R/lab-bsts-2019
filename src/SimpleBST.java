import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Stack;
import java.util.function.BiConsumer;

/**
 * A simple implementation of binary search trees.
 */
public class SimpleBST<K, V> implements SimpleMap<K, V> {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The root of our tree. Initialized to null for an empty tree.
   */
  BSTNode<K, V> root;

  /**
   * The comparator used to determine the ordering in the tree.
   */
  Comparator<K> comparator;

  /**
   * The size of the tree.
   */
  int size;
  
  /**
   * A cached value (useful in some circumstances.
   */
  V cachedValue;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new binary search tree that orders values using the specified comparator.
   */
  public SimpleBST(Comparator<K> comparator) {
    this.comparator = comparator;
    this.root = null;
    this.size = 0;
    this.cachedValue = null;
  } // SimpleBST(Comparator<K>)

  /**
   * Create a new binary search tree that orders values using a not-very-clever default comparator.
   */
  public SimpleBST() {
    this((k1, k2) -> k1.toString().compareTo(k2.toString()));
  } // SimpleBST()


  // +-------------------+-------------------------------------------
  // | SimpleMap methods |
  // +-------------------+

  @Override
  public V set(K key, V value) {
    /*
    if (key == null) {
      throw new NullPointerException();
    } // if key null
    if (this.root == null) {
      this.root = new BSTNode<K, V>(key, value);
      this.size++;
      return null;
    } // if tree empty
    BSTNode<K, V> curr = this.root;
    while (curr != null) {
      if (this.comparator.compare(curr.key, key) == 0) {
        V temp = curr.value;
        curr.value = value;
        return temp;
      } // if curr matches key
      if (this.comparator.compare(curr.key, key) > 0) {
        if (curr.left != null) {
          curr = curr.left;
        } else {
          curr.left = new BSTNode<K, V>(key, value);
          this.size++;
          return null;
        } // if/else
      } else {
        if (curr.right != null) {
          curr = curr.right;
        } else {
          curr.right = new BSTNode<K, V>(key, value);
          this.size++;
          return null;
        } // if/else
      } // if/else
    } // while
    return null;
    */
    root = set(root, key, value);
    return this.cachedValue;
  } // set(K,V)
  
  public BSTNode<K, V> set(BSTNode<K, V> node, K key, V value) {
    if (node == null) {
      this.cachedValue = null;
      this.size++;
      return new BSTNode<K, V>(key, value);
    } // if node empty
    int compare = this.comparator.compare(key, node.key);
    if (compare == 0) {
      this.cachedValue = node.value;
      node.value = value;
      return node;
    } // if key matches current node
    else if (compare < 0) {
      node.left = set(node.left, key, value);
      return node;
    } // if key < current node's key
    else {
      node.right = set(node.right, key, value);
      return node;
    } // if key > current node's key
  } // set(BSTNode<K, V> node, K key, V value)

  @Override
  public V get(K key) {
    if (key == null) {
      throw new NullPointerException("null key");
    } // if
    return get(key, root);
  } // get(K,V)

  @Override
  public int size() {
    // TODO Auto-generated method stub
    return 0;
  } // size()

  @Override
  public boolean containsKey(K key) {
    // TODO Auto-generated method stub
    return false;
  } // containsKey(K)

  @Override
  public V remove(K key) {
    root = remove(root, key);
    return this.cachedValue;
  } // remove(K)
  
  BSTNode<K, V> remove(BSTNode<K, V> node, K key) {
    if (node == null) {
      this.cachedValue = null;
      return null;
    } // if node is empty
    this.cachedValue = node.value;
    int compare = this.comparator.compare(key, node.key);
    if (compare == 0) {
      if (node.left == null && node.right == null) {
        return null;
      } // if node subtrees are empty
      else if (node.left == null) {
        return node.right;
      } // if left subtree is null
      else if (node.right == null) {
        return node.left;
      } // if right subtree is null
      else {
        BSTNode<K, V> temp = node.left;
        while (temp.right != null) {
          temp = temp.right;
        } // while
        temp.right = node.right;
        return node.left;
      } // if both subtrees are full
    } // if key matches current node's key
    else if (compare < 0) {
      node.left = remove(node.left, key);
      return node;
    } // if key < current node's key
    else {
      node.right = remove(node.right, key);
      return node;
    } // if key > current node's key
  } // remove(BSTNode<K, V> node, K key)

  @Override
  public Iterator<K> keys() {
    return new Iterator<K>() {
      Iterator<BSTNode<K, V>> nit = SimpleBST.this.nodes();

      @Override
      public boolean hasNext() {
        return nit.hasNext();
      } // hasNext()

      @Override
      public K next() {
        return nit.next().key;
      } // next()

      @Override
      public void remove() {
        nit.remove();
      } // remove()
    };
  } // keys()

  @Override
  public Iterator<V> values() {
    return new Iterator<V>() {
      Iterator<BSTNode<K, V>> nit = SimpleBST.this.nodes();

      @Override
      public boolean hasNext() {
        return nit.hasNext();
      } // hasNext()

      @Override
      public V next() {
        return nit.next().value;
      } // next()

      @Override
      public void remove() {
        nit.remove();
      } // remove()
    };
  } // values()

  @Override
  public void forEach(BiConsumer<? super K, ? super V> action) {
    forEach(action, this.root);
  } // forEach(BiConsumer<? super K, ? super V> action)

  // +----------------------+----------------------------------------
  // | Other public methods |
  // +----------------------+

  /**
   * Dump the tree to some output location.
   */
  public void dump(PrintWriter pen) {
    dump(pen, root, "");
  } // dump(PrintWriter)


  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Dump a portion of the tree to some output location.
   */
  void dump(PrintWriter pen, BSTNode<K, V> node, String indent) {
    if (node == null) {
      pen.println(indent + "<>");
    } else {
      pen.println(indent + node.key + ": " + node.value);
      if ((node.left != null) || (node.right != null)) {
        dump(pen, node.left, indent + "  ");
        dump(pen, node.right, indent + "  ");
      } // if has children
    } // else
  } // dump
  
  /**
   * Get the value associated with a key in a subtree rooted at node.  See the
   * top-level get for more details.
   */
  V get(K key, BSTNode<K,V> node) {
    if (node == null) {
      throw new IndexOutOfBoundsException("Invalid key: " + key);
    }
    int comp = comparator.compare(key, node.key);
    if (comp == 0) {
      return node.value;
    } else if (comp < 0) {
      return get(key, node.left);
    } else {
      return get(key, node.right);
    }
  } // get(K, BSTNode<K,V>)

  /**
   * Get an iterator for all of the nodes. (Useful for implementing the other iterators.)
   */
  Iterator<BSTNode<K, V>> nodes() {
    return new Iterator<BSTNode<K, V>>() {

      Stack<BSTNode<K, V>> stack = new Stack<BSTNode<K, V>>();
      boolean initialized = false;

      @Override
      public boolean hasNext() {
        checkInit();
        return !stack.empty();
      } // hasNext()

      @Override
      public BSTNode<K, V> next() {
        checkInit();
        // TODO Auto-generated method stub
        return null;
      } // next();

      void checkInit() {
        if (!initialized) {
          stack.push(SimpleBST.this.root);
          initialized = true;
        } // if
      } // checkInit
    }; // new Iterator
  } // nodes()
  
  void forEach(BiConsumer<? super K, ? super V> action, BSTNode<K, V> node) {
    if (node != null) {
      action.accept(node.key, node.value);
      if (node.left != null) {
        forEach(action, node.left);
      } // if
      if (node.right != null) {
        forEach(action, node.right);
      } // if
    } // if not null
  } // forEach(BiConsumer<? super K, ? super V> action, BSTNode<K, V> node)

} // class SimpleBST
