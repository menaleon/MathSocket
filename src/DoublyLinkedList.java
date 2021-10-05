import javax.swing.*;
import java.io.Serializable;
import java.util.Random;

public class DoublyLinkedList implements Serializable { // Atributes: head (inicio), last (último) and size (tamaño)
    public Node head;
    public Node last;
    public int size;

    private static Random random = new Random();
    private int tipoCasilla;
    private int tunel = 0; // máx 4 (25%)
    private int trampa = 0; // máx 4 (25%)
    private int reto = 0; // máx 8 (50%)

    public DoublyLinkedList(){ // CONSTRUCTOR
        this.head = null;
        this.last = null;
        this.size = 0;

        // Generación de elementos aleatoria y diferente cada vez que se ejecute el Servidor,
        // pero se envía una copia al Cliente
        crearTablero();
    }

    // Metodo para retornar el primer nodo de la lista (la cabeza)
    public Node getHead() {
        return this.head;
    }

    public boolean isEmpty(){ // verifies if the list is empty
        return this.head == null;
    }

    public int length(){ //getter
        return this.size;
    }

    public void insertLast(Object data, int cType){ // añade un nuevo nodo al final de la lista
        Node newNode = new Node(data, cType);

        if (this.isEmpty()){ // si la lista está vacía se define el nuevo nodo como la cabeza como y el último a la vez
            this.head = newNode;
            this.last = this.head;

        }else{
            Node current = this.head; // nodo auxiliar o temporal
            while (current.next != null){ // recorre la lista mientras haya elementos
                current = current.next;
            }
            current.next = newNode; // se define el "siguiente" como el nuevo nodo
            newNode.prev = current; // se define el nodo "anterior" como el nodo auxiliar
            this.last = newNode; // se define el último nodo como el que acabamos de agregar
        }
        this.size++;
    }

    public int show(int elemento){ // Muestra el tipo de casilla "cType" del elemento que queramos (ese es el parámetro)
        Node current = this.head;
        int cont = 0;
        while (current != null && cont < elemento){ // recorre la lista hasta que llegue al # de elemento buscado
            current = current.next;
            cont++;
        }
        return current.getcType();
    }

    public void crearTablero(){ // llama al método insertar nodo dependiendo del tipo de casilla que sea
        for (int key = 0; key < 16; key++) {
            if (key == 0) {
                insertLast("Inicio",3);
            }
            else if (key == 15) {
                insertLast("Final",4);
            }
            else {
                crearTableroAux();
            }
        }
    }

    public void crearTableroAux(){ // inserta nodos en la lista en caso de que la casilla sea reto, tunel o trampa
        int numAleatorio = (int) (Math.random()*4+1);
        if (numAleatorio == 1 || numAleatorio == 2 && reto < 7) {
            reto++;
            insertLast("Reto",0);
        }
        else if (numAleatorio == 3 && trampa < 4) {
            trampa++;
            insertLast("Trampa",1);
        }
        else if (numAleatorio == 4 && tunel < 3) {
            tunel++;
            insertLast("Tunel", 2);
        }
        else{
            crearTableroAux();
        }
    }
}
