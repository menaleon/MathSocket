import java.io.Serializable;

public class Mensaje implements Serializable{

    DoublyLinkedList tablero;
    Boolean dado;
    Boolean reto;
    int posicion;

    public Mensaje (DoublyLinkedList tablero, Boolean dado, Boolean reto, int posicion){
        this.tablero = tablero;
        this.dado = dado;
        this.reto = reto;
        this.posicion = posicion;
    }

    public Boolean getDado(){
        return this.dado;
    }

    public DoublyLinkedList getTablero(){
        return this.tablero;
    }

    public Boolean getReto(){
        return this.reto;
    }
}