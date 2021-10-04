import java.io.Serializable;

public class Node implements Serializable {
    private int cType; // tipo de casilla representado por un entero
    public Object data; // tipo de casilla representado por un String
    public Node prev;
    public Node next;

    public Node(Object data, int cType) {
        this.data = data;
        this.next = null;
        this.prev = null;
        this.cType = cType;
    }

    // Retorna el dato vinculado al nodo
    public Object getData() {
        return this.data;
    }

    // Retorna el tipo de dato (int reto, casilla, trampa, inicio o final)
    public int getcType(){
        return this.cType;
    }

    // Modificar el dato vinculado al nodo
    public void setData(Object data) {
        this.data = data;
    }

    // Obtener el nodo vinculado en el puntero siguiente
    public Node getNext() {
        return this.next;
    }

    // Cambiar el nodo vinculado en el puntero siguiente
    public void setNext(Node node) {
        this.next = node;
    }

    // Obtener el nodo vinculado en el puntero anterior
    public Node getPrev() {
        return this.prev;
    }

    // Cambiar el nodo vinculado en el puntero anterior
    public void setPrev(Node node) {
        this.prev = node;
    }
}