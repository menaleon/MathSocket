import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfazJuego{
    int trampa = 0;
    int tunel = 0;

    JFrame frame;
    DoublyLinkedList copy;
    JLabel [][] matriz;
    JButton dado;

    int x = 10; // Coordenada x variable para cada botón en la matriz
    int y = 10; // Coordenada y variable para botón en la matriz
    int filas = 4;
    int columnas = 4;
    int elemento = 1;
    int tipoCasilla; // Puede ser 0 (tunel), 1 (trampa) ó 2 (reto)

    public InterfazJuego(DoublyLinkedList tablero){
        this.copy = tablero;

        //Ventana de juego
        frame = new JFrame();
        frame.setTitle("Lets Play");
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Dado
        dado = new JButton("Tirar dado");
        dado.setBounds(550, 200, 100, 50);
        frame.add(dado);
        dado.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Dado.getInstancia().Tirar();
            }
        });

        //Jugadores
        ImageIcon J1 = new ImageIcon("imagenes/J1.png");
        ImageIcon J2 = new ImageIcon("imagenes/J2.png");
        JLabel ficha1 = new JLabel();
        JLabel ficha2 = new JLabel();
        ficha1.setBounds(550, 100, 30, 30);
        ficha2.setBounds(550, 500, 30, 30);
        ficha1.setIcon(J1);
        ficha2.setIcon(J2);
        frame.add(ficha1);
        frame.add(ficha2);

        //Casillas del tablero
        matriz = new JLabel[filas][columnas];

        ImageIcon casilla = new ImageIcon("imagenes/hexagon.png");
        
        for (int i=0; i<filas; i++){
            for (int j=0; j<columnas; j++){

                matriz[i][j] = new JLabel();
                matriz[i][j].setBounds(x, y, 90, 80);

                tipoCasilla = copy.show(elemento);

                if(tipoCasilla == 0 && tunel < 4){
                    matriz[i][j].setBackground(Color.green);
                    tunel++;
                }else if(tipoCasilla == 1 && trampa < 4){
                    matriz[i][j].setBackground(Color.orange);
                    trampa++;
                }else{
                    matriz[i][j].setBackground(Color.blue);
                }

                matriz[i][j].setIcon(casilla);
                frame.add(matriz[i][j]);
                //matriz[i][j].setVisible(true);

                x += 130;
                elemento++;
            }
            x = 10;
            y += 130;
        }
        
        SwingUtilities.updateComponentTreeUI(frame);
    }

    public DoublyLinkedList getList(){
        return this.copy;
    }
}
