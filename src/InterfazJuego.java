import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfazJuego{
    int trampa = 0;
    int tunel = 0;

    JFrame frame;
    DoublyLinkedList copy;
    JButton [][] matriz;
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

        //Casillas del tablero
        matriz = new JButton[filas][columnas];

        ImageIcon casilla = new ImageIcon("imagenes/hexagon.png");

        for (int i=0; i<filas; i++){
            for (int j=0; j<columnas; j++){

                matriz[i][j] = new JButton();
                matriz[i][j].setBounds(x, y, 80, 80);

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
                matriz[i][j].setVisible(true);

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
