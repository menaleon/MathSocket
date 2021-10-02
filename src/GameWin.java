/**import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Random;


// ESTA CLASE YA NO SE USA. LA INTERFAZ SE TRASLADÓ A LA CLASE DE LA DOUBLYLINKEDLIST

public class GameWin extends JComponent{
    DoublyLinkedList list = new DoublyLinkedList();
    private static Random random = new Random();
    private int tipoCasilla;
    private int tunel = 0; // máx 4 (25%)
    private int trampa = 0; // máx 4 (25%)
    private int reto = 0; // máx 8 (50%)

    public void paint(Graphics g){
        super.paint(g);
        for(int i=1; i<5; i++){ // columnas: verticales
            for(int j=1; j<5; j++){ // filas: horizontales
                //ImageIcon img = new ImageIcon(Objects.requireNonNull(getClass().getResource("hexagon.png")));
                //g.drawImage(img.getImage(), 160 * (i-1) + 20, 100 * (j-1) + 50, 140, 86, null);
                tipoCasilla = random.nextInt(3);
                if(tipoCasilla == 0 && tunel < 4){
                    list.insertLast("Túnel", 0);
                    g.setColor(Color.blue);
                    g.drawString("Túnel", 160 * (i-1) + 30,100 * (j-1) + 70);
                    tunel++;
                }else if(tipoCasilla == 1 && trampa < 4){
                    list.insertLast("Trampa", 1);
                    g.setColor(Color.RED);
                    g.drawString("Trampa", 160 * (i-1) + 30,100 * (j-1) + 70);
                    trampa++;
                }else if(reto < 9){
                    list.insertLast("Reto", 2);
                    g.setColor(Color.orange);
                    g.drawString("Reto", 160 * (i-1) + 30,100 * (j-1) + 70);
                }
                g.fillOval(160 * (i-1) + 30,100 * (j-1) + 70, 140, 86);
                g.drawOval(160 * (i-1) + 30,100 * (j-1) + 70, 140, 86);
            }
        }
        //list.display(); // shows data of each Node

        ImageIcon bird = new ImageIcon(Objects.requireNonNull(getClass().getResource("bird.png")));
        g.drawImage(bird.getImage(), 700, 200, 100, 100, null);

        ImageIcon fish = new ImageIcon(Objects.requireNonNull(getClass().getResource("fish.png")));
        g.drawImage(fish.getImage(), 860, 200, 100, 100, null);

    }

    public DoublyLinkedList getList(){
        return this.list;
    }
    public GameWin getGameWin(){ return this;}

    /**public static void main(String[] args){
        /**VENTANA DE JUEGO
        JFrame win = new JFrame("Math Socket");
        GameWin imagenes = new GameWin();
        win.add(imagenes);
        win.getContentPane().setBackground(Color.darkGray);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setBounds(100, 100, 1000, 600);
        win.setVisible(true);
        win.setResizable(false);
        // DADO
        JButton dice = new JButton("Tirar dado");
        dice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int num = random.nextInt(4);
                System.out.println("Te moverás " + num + " espacios");
            }
        });
        dice.setBounds(800, 40, 100, 30);
        win.add(dice);
    }
}**/