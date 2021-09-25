public class Mensaje{

    DoublyLinkedList tablero;
    Boolean dado;
    Boolean respuesta;

    public Mensaje (DoublyLinkedList tablero, Boolean dado, Boolean respuesta){
        this.tablero = tablero;
        this.dado = dado;
        this.respuesta = respuesta;
    }

    public Boolean getDado(){
        return this.dado;
    }

    public DoublyLinkedList getTablero(){
        return this.tablero;
    }

    public Boolean getRespuesta(){
        return this.respuesta;
    }
}