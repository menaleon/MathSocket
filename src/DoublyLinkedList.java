public class DoublyLinkedList { // Atributes: head (inicio), last (último) and size (tamaño)
    private Node head;
    private Node last;
    private int size;

    public DoublyLinkedList(){ // CONSTRUCTOR
        this.head = null;
        this.last = null;
        this.size = 0;
    }
    /***********************************************************************/
    public boolean isEmpty(){ // verifies if the list is empty
        return this.head == null;
    }

    public int size(){ //getter
        return this.size;
    }
    /*********************+**/
    public void insertLast(Object data){ // adds a new node to the end of the list
        Node newNode = new Node(data);
        if (this.isEmpty()){
            this.head = newNode;
            this.last = this.head;
        }else{
            Node current = this.head; // auxiliary node
            while (current.next != null){ // recorre the list
                current = current.next;
            }
            current.next = newNode;
            newNode.prev = current;
            this.last = newNode;
        }
        this.size++;
    }
    /*********************+**/
    public void deleteFirst(){ // deletes the first node in the list, if the list is NOT empty
        if (!this.isEmpty()){
            this.head = this.head.next;
            this.head.prev = null;
            this.size--;
        }
    }
    /*********************+**/
    public Node getLast(){ // returns the last (último) node. This is used in the "peak" method
        return this.last;
    }
    /*********************+**/
    public Object display(){ // prints the list and its size
        Node current = this.head;
        while (current != null){
            System.out.println(current.getData());
            current = current.next;
        }
        System.out.println("Lista impresa con un tamaño de: " + this.size + " elementos");
        return 1;
    }
}
