import java.io.Serializable;

public class Mensaje implements Serializable{ // esta clase permite enviar datos al servidor o al cliente, por sockets

    DoublyLinkedList tablero;
    Boolean dado;
    Boolean reto;
    int posCliente;
    int posServer;

    public Mensaje (DoublyLinkedList tablero, Boolean dado, Boolean reto, int posCliente, int posServer){
        this.tablero = tablero;
        this.dado = dado;
        this.reto = reto;
        this.posCliente = posCliente;
        this.posServer = posServer;
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

    public int getPosCliente(){
        return this.posCliente;
    }

    public int getPosServer(){
        return this.posServer;
    }

    public void setReto(){
        this.reto = false;
    }

    public void setDado(){
        this.dado = false;
    }
}