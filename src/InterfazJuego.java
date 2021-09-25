import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

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
    int elemento = 0;
    int tipoCasilla; // Puede ser 0 (tunel), 1 (trampa) ó 2 (reto)
    JLabel ficha1;
    JLabel ficha2;
    int posFicha1 = 0;
    int posFicha2 = 0;

    public InterfazJuego(DoublyLinkedList tablero, int jugador){
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
                moverFicha(jugador,Dado.getInstancia().Tirar());
            }
        });

        //Jugadores
        ImageIcon J1 = new ImageIcon("imagenes/J1.png");
        ImageIcon J2 = new ImageIcon("imagenes/J2.png");
        ficha1 = new JLabel();
        ficha2 = new JLabel();
        ficha1.setBounds(20, 50, 30, 30);
        ficha2.setBounds(50, 50, 30, 30);
        ficha1.setIcon(J1);
        ficha2.setIcon(J2);
        frame.add(ficha1);
        frame.add(ficha2);

        //Casillas del tablero
        matriz = new JLabel[filas][columnas];

        ImageIcon casillaReto = new ImageIcon("imagenes/reto.png");
        ImageIcon casillaTrampa = new ImageIcon("imagenes/trampa.png");
        ImageIcon casillaTunel = new ImageIcon("imagenes/tunel.png");
        
        for (int i=0; i<filas; i++){
            for (int j=0; j<columnas; j++){

                matriz[i][j] = new JLabel();
                matriz[i][j].setBounds(x, y, 90, 80);

                tipoCasilla = copy.show(elemento);

                if(tipoCasilla == 0 && tunel < 4){
                    matriz[i][j].setIcon(casillaTunel);
                    tunel++;
                }else if(tipoCasilla == 1 && trampa < 4){
                    matriz[i][j].setIcon(casillaTrampa);
                    trampa++;
                }else{
                    matriz[i][j].setIcon(casillaReto);
                }

                //matriz[i][j].setIcon(casilla);
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

    public void setVisibleDado(Boolean visible){
        dado.setVisible(visible);
    }

    public void moverFicha(int judador, int avance){
        if (judador == 1){
            posFicha1 = posFicha1 + avance;
            int posX = 50 + ((posFicha1) % 4) * 130;
            int posY = 50 + ((posFicha1)/4) * 130;
            ficha1.setBounds(posX, posY, 30, 30);
            int typeCasilla = copy.show(posFicha1);
            if(typeCasilla == 0){
                System.out.println("Soy server tunel");
                posFicha1 = posFicha1 + new Random().nextInt(3) + 1;
                posX = 50 + ((posFicha1) % 4) * 130;
                posY = 50 + ((posFicha1)/4) * 130;
                ficha1.setBounds(posX, posY, 30, 30);
            }
            else if(typeCasilla == 1){
                System.out.println("Soy server trampa");
                posFicha1 = posFicha1 - (new Random().nextInt(3) + 1);
                posX = 50 + ((posFicha1) % 4) * 130;
                posY = 50 + ((posFicha1)/4) * 130;
                ficha1.setBounds(posX, posY, 30, 30);
            }
            else if(typeCasilla == 2){
                System.out.println("Soy server reto");
            }

        }
        else{
            posFicha2 = posFicha2 + avance;
            int posX = 20 + ((posFicha2) % 4) * 130;
            int posY = 50 + ((posFicha2)/4) * 130;
            ficha2.setBounds(posX, posY, 30, 30);

            int typeCasilla = copy.show(posFicha2);
            if(typeCasilla == 0){
                System.out.println("Soy cliente tunel");
                posFicha2 = posFicha2 + new Random().nextInt(3) + 1;
                posX = 50 + ((posFicha2) % 4) * 130;
                posY = 50 + ((posFicha2)/4) * 130;
                ficha2.setBounds(posX, posY, 30, 30);
            }
            else if(typeCasilla == 1){
                System.out.println("Soy cliente trampa");
                posFicha2 = posFicha2 - (new Random().nextInt(3) + 1);
                posX = 50 + ((posFicha2) % 4) * 130;
                posY = 50 + ((posFicha2)/4) * 130;
                ficha2.setBounds(posX, posY, 30, 30);
            }
            else if(typeCasilla == 2){
                System.out.println("Soy cliente reto");
            }
        }
        SwingUtilities.updateComponentTreeUI(frame);
    }

    public void actualizarUI(){
        SwingUtilities.updateComponentTreeUI(frame);
    }

    public DoublyLinkedList getList(){
        return this.copy;
    }
}
