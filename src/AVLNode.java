public class AVLNode {

    private int key;
    private String value;
    private AVLNode left;
    private AVLNode right;
    private AVLNode parent;
    private int height;
    private int size;

    public AVLNode(int key, String value){
        this.key=key;
        this.value=value;
        this.height = 0;
        this.right=null;
        this.left=null;
        this.parent=null;
        this.size=1;
    }

    public AVLNode(){
        this.height = -1;
    }

    public int getKey()
    {
        return this.key;
    }

    public String getValue()
    {
        return this.value;
    }

    public void setLeft(AVLNode node)
    {
        this.left= node;
    }

    public AVLNode getLeft()
    {
        return this.left;
    }

    public void setRight(AVLNode node)
    {
        this.right=node;
    }

    public AVLNode getRight()
    {
        return this.right;
    }

    public void setParent(AVLNode node)
    {
        this.parent=node;
    }
    public AVLNode getParent()
    {
        return this.parent;
    }

    public void setHeight(int height)
    {
        this.height=height;
    }

    public int getHeight()
    {
        return this.height;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
