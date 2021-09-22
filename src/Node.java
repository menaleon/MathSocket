import java.io.Serializable;

public class Node implements Serializable {
    private int cType;
    public Object data; // tipo de casilla representado por un String
    //public int cType; // tipo de casilla representado por un entero
    public Node prev;
    public Node next;

    public Node(Object data, int cType) {
        this.data = data;
        this.next = null;
        this.prev = null;
        this.cType = cType;
    }

    public Object getData() {
        return this.data;
    }

    public int getcType(){
        return this.cType;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Node getNext() {
        return this.next;
    }

    public void setNext(Node node) {
        this.next = node;
    }

    public Node getPrev() {
        return this.prev;
    }

    public void setPrev(Node node) {
        this.prev = node;
    }
}