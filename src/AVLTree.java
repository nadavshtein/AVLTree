/**
 * Details:
 * <p>
 * Nadav Shtein 316130350  username: nadavshtein
 * Elad Ridel   318658036  username: eladridel
 */

/**
 * AVLTree
 * An implementation of a AVL Tree with
 */

public class AVLTree {

    private int size;
    private AVLNode root;
    private AVLNode min;
    private AVLNode max;


    public boolean empty() { // O(1)
        return this.root == null;
    }

    /**
     * public String search(int k)
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public String search(int k) // O(logn)
    {
        AVLNode curr = this.root;
        while (curr != null) {
            if (curr.getKey() == k) { // found key
                return curr.getValue();
            } else {
                if (curr.getKey() > k) {
                    curr = curr.getLeft();
                } else {
                    curr = curr.getLeft();
                }
            }
        }
        return null;
    }

    /**
     * public int insert(int k, String i)
     * inserts an item with key k and info i to the AVL tree.
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
     * returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, String i) { // O(logn)
        AVLNode curr = this.root;
        if (curr == null) { // empty tree -> add node as root
            this.root = new AVLNode(k, i);
            this.size++;
            this.max = this.root;
            this.min = this.root;
            return 0;
        }
        boolean R = false; // add node to right if true, to left if false
        while (curr != null) { // search where to add the new node
            if (curr.getKey() == k) { // node with key is already exists
                return -1;
            } else {
                if (curr.getKey() > k) {
                    if (curr.getLeft() == null) {
                        R = false;
                        break;
                    }
                    curr = curr.getLeft();
                } else {
                    if (curr.getRight() == null) {
                        R = true;
                        break;
                    }
                    curr = curr.getRight();
                }
            }
        }
        // curr will be the parent
        AVLNode node = new AVLNode(k, i);
        node.setParent(curr);
        if (R) {
            if (curr == this.max) { // insert right son to max -> node is the new max
                this.max = node;
            }
            curr.setRight(node);
            if (curr.getLeft() == null) { // update height if other child is empty
                curr.setHeight(curr.getHeight() + 1);
            }
        } else {
            if (curr == this.min) { // insert left son to min -> node is the new min
                this.min = node;
            }
            curr.setLeft(node);
            if (curr.getRight() == null) { // update height if other child is empty
                curr.setHeight(curr.getHeight() + 1);
            }
        }
        this.size++;
        return Rotate(node, true);
    }

    public void UpdateSize(AVLNode node) { // update all sizes from node to root - O(logn)
        if (node != null) {
            UpdateSizeNode(node);
            UpdateSize(node.getParent());
        }
    }

    public void UpdateHeight(AVLNode node) { // update all heights from node to root - O(logn)
        if (node != null) {
            UpdateHeightNode(node);
            UpdateHeight(node.getParent()); // recursive to parent
        }
    }

    public int Rotate(AVLNode node, boolean isInsert) {
        /** isInsert = true -> insert // isInsert = false -> Delete (only one rotation if true)
         *   balance by rotating from node to root
         *   return number of rotations done
         **/
        if (node == null) {
            return 0;
        }
        UpdateHeightNode(node);
        UpdateSizeNode(node);
        int result = 0; // counting the number of rotations
        int b = BF(node);
        if (Math.abs(b) > 1) { // tree is not balanced at node
            if (b == 2) {
                int bfLeft = BF(node.getLeft());
                if (bfLeft == 1 || bfLeft == 0) {
                    RightRotation(node);
                    result++;
                } else { // bfLeft==-1
                    LeftRotation(node.getLeft());
                    RightRotation(node);
                    result += 2;
                }
            } else { // b==-2
                int bfRight = BF(node.getRight());
                if (bfRight == 1) {
                    RightRotation(node.getRight());
                    LeftRotation(node);
                    result += 2;
                } else { // bfRight==-1 || bfRight == 0
                    LeftRotation(node);
                    result++;
                }
            }
            if (isInsert) { // when inserting there is maximum one rotation needed -> break after this rotation
                UpdateHeight(node); // continue update all heights and sizes to root
                UpdateSize(node);
                return result;
            }
        }
        return result + Rotate(node.getParent(), isInsert); // sum result in recursion
    }

    private void RightRotation(AVLNode node) { // O(1)
        AVLNode y = node.getLeft();
        node.setLeft(y.getRight());
        if (y.getRight() != null) {
            y.getRight().setParent(node);
        }
        y.setParent(node.getParent());
        if (node.getParent() == null) {
            this.root = (AVLNode) y;
        } else {
            if (node == node.getParent().getRight()) {
                node.getParent().setRight(y);
            } else {
                node.getParent().setLeft(y);
            }
        }
        y.setRight(node);
        node.setParent(y);
        // maintain height and size fields
        UpdateHeightNode(node);
        UpdateHeightNode(y);
        UpdateSizeNode(node);
        UpdateSizeNode(y);
    }

    private void LeftRotation(AVLNode node) { // O(1)
        AVLNode y = node.getRight();
        node.setRight(y.getLeft());
        if (y.getLeft() != null) {
            y.getLeft().setParent(node);
        }
        y.setParent(node.getParent());
        if (node.getParent() == null) { // node is the root
            this.root = y;
        } else {
            if (node == node.getParent().getLeft()) {
                node.getParent().setLeft(y);
            } else {
                node.getParent().setRight(y);
            }
        }
        y.setLeft(node);
        node.setParent(y);
        // maintain height and size fields
        UpdateHeightNode(node);
        UpdateHeightNode(y);
        UpdateSizeNode(node);
        UpdateSizeNode(y);
    }

    private void UpdateSizeNode(AVLNode node) {
        int L = 0;
        int R = 0; // set default size of node's sons as 0
        if (node.getRight() != null) {
            AVLNode son = node.getRight();
            R = son.getSize();
        }
        if (node.getLeft() != null) {
            AVLNode son = node.getLeft();
            L = son.getSize();
        }
        node.setSize(L + R + 1);
    }

    private void UpdateHeightNode(AVLNode node) {
        int L = -1;
        int R = -1; // height of empty tree
        if (node.getLeft() != null) {
            L = node.getLeft().getHeight();
        }
        if (node.getRight() != null) {
            R = node.getRight().getHeight();
        }
        node.setHeight(1 + Math.max(R, L));
    }

    private int BF(AVLNode node) { // return Balance Factor of node
        int L = -1;
        int R = -1; // height of empty tree
        if (node.getLeft() != null) {
            L = node.getLeft().getHeight();
        }
        if (node.getRight() != null) {
            R = node.getRight().getHeight();
        }
        return L - R;
    }


    /**
     * public int delete(int k)
     * deletes an item with key k from the binary tree, if it is there;
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
     * returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) // O(logn)
    {
        AVLNode curr = this.root;
        if (curr == null) { // empty tree
            return -1;
        }
        while (curr != null) {
            if (curr.getKey() == k) {
                break; // curr is the node to delete
            } else {
                if (curr.getKey() > k) {
                    curr = curr.getLeft();
                } else {
                    curr = curr.getRight();
                }
            }
        }
        if (curr == null) { // no found k in tree
            return -1;
        }
        // curr is found!
        return deleteNode(curr);
    }

    public int deleteNode(AVLNode curr) { // O(logn) - delete curr from the tree
        if (curr == this.min) { // update min
            this.min = Successor(curr);
        }
        if (curr == this.max) { // update max
            this.max = Predecessor(curr);
        }

        AVLNode rotateFrom; // rotate from this node - set in each case

        if (curr.getRight() == null && curr.getLeft() == null) {    // case 1 - curr is a leaf //
            rotateFrom = curr.getParent();
            DeleteNoChild(curr);
        } else {
            if (curr.getRight() == null || curr.getLeft() == null) { // case 2 - curr has one child //
                rotateFrom = curr.getParent();
                DeleteOneChild(curr);
            } else {                                              // case 3 - curr has 2 children //
                rotateFrom = Successor(curr); // rotate from physically deleted node
                if (rotateFrom.getParent() != curr) {
                    rotateFrom = rotateFrom.getParent();
                }
                DeleteTwoChild(curr);
            }
        }
        this.size--;
        if (empty()) { // curr was the root and a leaf
            return 0;
        }

        return Rotate(rotateFrom, false);
    }

    /* @pre curr has 2 children */
    private void DeleteTwoChild(AVLNode curr) { // O(logn)
        AVLNode y = Successor(curr);
        if (y.getRight() == null && y.getLeft() == null) {// y is a leaf
            DeleteNoChild(y);
        } else {
            // curr has one child - y cant have two children
            DeleteOneChild(y);
        }
        // switch curr to y
        y.setParent(curr.getParent());
        y.setLeft(curr.getLeft());
        y.setRight(curr.getRight());
        if (curr.getRight() != null) { // y is the new parent
            curr.getRight().setParent(y);
        }
        if (curr.getLeft() != null) {
            curr.getLeft().setParent(y);
        }
        if (curr.getParent() == null) { // curr was the root, now y is
            this.root = y;
        } else {
            if (curr.getParent().getLeft() == curr) {
                curr.getParent().setLeft(y);
            } else {
                curr.getParent().setRight(y);
            }
        }
    }

    /* @pre curr has one child */
    private void DeleteOneChild(AVLNode curr) { // O(1)
        if (curr.getRight() == null) { // curr has left child
            if (curr.getParent() == null) {
                this.root = curr.getLeft();
                this.root.setParent(null);
            } else {
                if (curr.getParent().getLeft() == curr) { // curr is the left child of his parent
                    curr.getParent().setLeft(curr.getLeft());
                } else {// curr is the right child of his parent
                    curr.getParent().setRight(curr.getLeft());
                }
                curr.getLeft().setParent(curr.getParent());
            }
        } else {// curr has right child
            if (curr.getParent() == null) {
                this.root = curr.getRight();
                this.root.setParent(null);
            } else {
                if (curr.getParent().getLeft() == curr) { // curr is the left child of his parent
                    curr.getParent().setLeft(curr.getRight());
                } else {// curr is the right child of his parent
                    curr.getParent().setRight(curr.getRight());
                }
                curr.getRight().setParent(curr.getParent());
            }
        }
    }

    /* @pre curr is a leaf */
    private void DeleteNoChild(AVLNode curr) {// O(1)
        if (curr.getParent() == null) {
            this.root = null;
        } else {
            if (curr.getParent().getLeft() == curr) {
                curr.getParent().setLeft(null);
            } else {
                curr.getParent().setRight(null);
            }
        }
    }

    private AVLNode Successor(AVLNode node) {// O(logn)
        if (node.getRight() != null) {
            return MinFromNode(node.getRight()); // return the node with the min key from the right subtree of node
        }
        AVLNode y = node.getParent();
        while (y != null && node == y.getRight()) { //go up till first time right
            node = y;
            y = node.getParent();
        }
        return y;
    }

    public AVLNode Predecessor(AVLNode node) { // O(logn)
        if (node.getLeft() != null) {
            return maxFromNode(node.getLeft()); // return the node with the max key from the left subtree of node
        }
        AVLNode y = node.getParent();
        while (y != null && node == y.getLeft()) { //go up till first time left
            node = y;
            y = node.getParent();
        }
        return y;
    }

    // return the node with the max key from the subtree of node
    private AVLNode maxFromNode(AVLNode node) { // O(logn)
        while (node.getRight() != null) {
            node = node.getRight();
        }
        return node;
    }

    // return the node with the min key from the subtree of node
    private AVLNode MinFromNode(AVLNode node) { // O(logn)
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    /**
     * public String min()
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public String min() {
        if (this.min == null) {
            return null;
        }
        return this.min.getValue();
    }

    /**
     * public String max()
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public String max() {
        if (this.max == null) {
            return null;
        }
        return this.max.getValue();
    }

    /**
     * public int[] keysToArray()
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        int[] arr = new int[this.size];
        if (this.root == null) { //empty tree
            return arr;
        }
        int rootIndex;
        if (this.root.getLeft() == null) {
            rootIndex = 0;
        } else {
            rootIndex = this.root.getLeft().getSize(); // index of root is rank(root)-1
        }
        InOrderRec(this.root, arr, rootIndex); // Updates arr

        return arr;
    }

    // in order tree walk while inserting keys to array
    private void InOrderRec(AVLNode node, int[] arr, int i) {
        if (node == null) {
            return;
        }
        InOrderRec(node.getLeft(), arr, i - sizeLeftRight(node));
        arr[i] = node.getKey();
        InOrderRec(node.getRight(), arr, i + sizeRightLeft(node));
    }

    private int sizeLeftRight(AVLNode node) { // return the number of nodes between node and his left son +1
        if (node.getLeft() == null) {
            return 0;
        } else {
            if (node.getLeft().getRight() == null) {
                return 1;
            } else {
                return node.getLeft().getRight().getSize() + 1;
            }
        }
    }

    private int sizeRightLeft(AVLNode node) { // return the number of nodes between node and his right son+1
        if (node.getRight() == null) {
            return 0;
        } else {
            if (node.getRight().getLeft() == null) {
                return 1;
            } else {
                return node.getRight().getLeft().getSize() + 1;
            }
        }
    }

    /**
     * public String[] infoToArray()
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() // O(n)
    {
        String[] arr = new String[this.size];
        if (this.root == null) {
            return arr;
        }

        int rootIndex;
        if (this.root.getLeft() == null) {
            rootIndex = 0;
        } else {
            rootIndex = this.root.getLeft().getSize(); // index of root is rank(root)-1
        }
        InOrderRecInfo(this.root, arr, rootIndex); // Updates arr
        return arr;
    }

    // in order tree walk while inserting values to array list
    private void InOrderRecInfo(AVLNode node, String[] arr, int i) { // O(n)
        if (node == null) {
            return;
        }
        InOrderRecInfo(node.getLeft(), arr, i - sizeLeftRight(node));
        arr[i] = node.getValue();
        InOrderRecInfo(node.getRight(), arr, i + sizeRightLeft(node));
    }

    /**
     * public int size()
     * Returns the number of nodes in the tree.
     */
    public int size() // O(1)
    {
        return this.size;
    }


    /**
     * public int Rank()
     * Returns the rank of AVL node
     */
    public int Rank(AVLNode node) {  // O(logn)
        AVLNode leftSon = node.getLeft();
        int counter;
        if (leftSon == null) {
            counter = 1;
        } else {
            counter = leftSon.getSize() + 1;
        }
        AVLNode y = node.getParent();
        while (y != null) {
            if (y.getRight() == node) {
                leftSon = y.getLeft();
                if (leftSon == null) {
                    counter++;
                } else {
                    counter += leftSon.getSize() + 1;
                }
            }
            node = y;
            y = y.getParent();
        }
        return counter;

    }


    /**
     * public AVLNode Select()
     * Returns the AVL node with the rank input
     */
    public AVLNode Select(int rank) { // O(logn)
        if (rank <= 0 || rank > this.size) { // rank illegal
            return null;
        }
        return SelectRec(this.root, rank);
    }

    private AVLNode SelectRec(AVLNode node, int rank) { // O(logn)

        int r;
        if (node.getLeft() == null) {
            r = 1;
        } else {
            r = node.getLeft().getSize() + 1;
        }
        if (r == rank) {
            return node; // node with rank found
        }
        if (r > rank) {
            return SelectRec(node.getLeft(), rank);
        } else {
            return SelectRec(node.getRight(), rank - r);
        }
    }


    //////// set-get functions //////////
    public void setRoot(AVLNode root) {
        this.root = root;
    }

    public void setMax(AVLNode max) {
        this.max = max;
    }

    public void setMin(AVLNode min) {
        this.min = min;
    }

    public AVLNode getMax() {
        return max;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public AVLNode getRoot() {
        return this.root;
    }
}




