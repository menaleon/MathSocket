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

    public JFrame gameFrame;

    public DoublyLinkedList(){ // CONSTRUCTOR
        this.head = null;
        this.last = null;
        this.size = 0;

        // Generación de elementos. Es aleatoria y diferente cada vez que se ejecute el Servidor, pero se envía una copia al Cliente
        crearTablero();
    }

    // Metodo para retornar el primer nodo de la lista
    public Node getHead() {
        return this.head;
    }

    public boolean isEmpty(){ // verifies if the list is empty
        return this.head == null;
    }

    public int length(){ //getter
        return this.size;
    }

    public void insertLast(Object data, int cType){ // adds a new node to the end of the list
        Node newNode = new Node(data, cType);
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

    public int show(int elemento){ // Muestra el tipo de casilla "cType" del elemento que queramos (ese es el parámetro)
        Node current = this.head;
        int cont = 0;
        while (current != null && cont < elemento){
            current = current.next;
            cont++;
        }
        return current.getcType();
    }

    public void crearTablero(){
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

    public void crearTableroAux(){
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
