import java.io.Serializable;

public class Mensaje implements Serializable{

    DoublyLinkedList tablero;
    Boolean dado;
    Boolean reto;
    int posicionX;
    int posicionY;

    public Mensaje (DoublyLinkedList tablero, Boolean dado, Boolean reto, int posicionX, int posicionY){
        this.tablero = tablero;
        this.dado = dado;
        this.reto = reto;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
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

    public int getPosicionX(){
        return this.posicionX;
    }

    public int getPosicionY(){
        return this.posicionY;
    }
}