import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.Objects;
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

        // Generación de elementos. Es aleatoria y diferente cada vez que se ejecute el Servidor, pero se envía una copia en Cliente
        for(int i=0; i<16; i++){
            tipoCasilla = random.nextInt(3);
            if(tipoCasilla == 0 && tunel < 4){
                insertLast("Tunel", 0);
                tunel++;
            }else if(tipoCasilla == 1 && trampa < 4 ){
                insertLast("Trampa", 1);
                trampa++;
            }else{
                insertLast("Reto", 2);
                reto++;
            }
            System.out.println(i);
            System.out.println(this.size);
        }
    }


    /**public void paint(Graphics g){
        super.paint(g);
        for(int i=1; i<5; i++){ // columnas: verticales
            for(int j=1; j<5; j++){ // filas: horizontales

                tipoCasilla = random.nextInt(3);

                if(tipoCasilla == 0 && tunel < 4){
                    this.insertLast("Túnel", 0);
                    g.setColor(Color.blue);
                    g.drawString("Túnel", 160 * (i-1) + 30,100 * (j-1) + 70);
                    tunel++;
                }else if(tipoCasilla == 1 && trampa < 4){
                    this.insertLast("Trampa", 1);
                    g.setColor(Color.RED);
                    g.drawString("Trampa", 160 * (i-1) + 30,100 * (j-1) + 70);
                    trampa++;
                }else if(reto < 9){
                    this.insertLast("Reto", 2);
                    g.setColor(Color.orange);
                    g.drawString("Reto", 160 * (i-1) + 30,100 * (j-1) + 70);
                }
                g.fillOval(160 * (i-1) + 27,100 * (j-1) + 64, 100, 75);
                g.drawOval(160 * (i-1) + 27,100 * (j-1) + 64, 100, 75);
            }
        }
        ImageIcon bird = new ImageIcon(Objects.requireNonNull(getClass().getResource("bird.png")));
        g.drawImage(bird.getImage(), 600, 200, 50, 50, null);

        ImageIcon fish = new ImageIcon(Objects.requireNonNull(getClass().getResource("fish.png")));
        g.drawImage(fish.getImage(), 650, 200, 50, 50, null);

    }**/
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
            System.out.println();
            current = current.next;
            cont++;
        }
        return current.getcType();
    }
}
