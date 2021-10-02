import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class InterfazJuego extends Component {
    JFrame frame;
    DoublyLinkedList tablero;
    JLabel [][] matriz;
    JButton dado;

    int jugador;
    int x = 20; // Coordenada x variable para cada botón en la matriz
    int y = 100; // Coordenada y variable para botón en la matriz
    int filas = 4;
    int columnas = 4;
    int elemento = 0;
    int tipoCasilla; // Puede ser 0 (reto), 1 (trampa), 2 (tunel), 3 (inicio) o 4 (final)
    JLabel ficha1;
    JLabel ficha2;
    int posFicha1 = 0;
    int posFicha2 = 0;
    int posXficha1;
    int posYficha1;
    int posXficha2;
    int posYficha2;

    public InterfazJuego(DoublyLinkedList tablero, int jugador){
        // Definir las imagenes que vamos a utilizar
        this.tablero = tablero;
        this.jugador = jugador;

        ImageIcon casillaInicio = new ImageIcon("imagenes/inicio.png");
        ImageIcon casillaReto = new ImageIcon("imagenes/reto.png");
        ImageIcon casillaTrampa = new ImageIcon("imagenes/trampa.png");
        ImageIcon casillaTunel = new ImageIcon("imagenes/tunel.png");
        ImageIcon casillaFinal = new ImageIcon("imagenes/final.png");

        //Ventana de juego
        frame = new JFrame();
        if (jugador == 1){
            frame.setTitle("Tablero - Servidor");
        } else if (jugador == 2) {
            frame.setTitle("Tablero - Cliente");
        }
        frame.setSize(620, 735);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon image = new ImageIcon("imagenes/logo.png");
        frame.setIconImage(image.getImage());

        // Creacion del Componente Dado
        dado = new JButton("Tirar dado");
        dado.setBounds(500, 25, 100, 50);
        frame.add(dado);
        dado.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                moverFicha(jugador,Dado.getInstancia().Tirar());
                setVisibleDado(false);
            }
        });

        // Imagen de las Fichas de los Jugadores
        ImageIcon J1 = new ImageIcon("imagenes/J1.png");
        ImageIcon J2 = new ImageIcon("imagenes/J2.png");

        // Imagen de las Casillas del Tablero

        ficha1 = new JLabel();
        ficha2 = new JLabel();
        ficha1.setBounds(26, 106, 30, 30);
        ficha2.setBounds(58, 106, 30, 30);
        ficha1.setIcon(J1);
        ficha2.setIcon(J2);
        frame.add(ficha1);
        frame.add(ficha2);

        //Casillas del tablero
        matriz = new JLabel[filas][columnas];


        for (int i=0; i<filas; i++){
            for (int j=0; j<columnas; j++){
                int nuevoJ = j;
                if(i%2==1){
                    nuevoJ = 3-j;
                }
                matriz[i][nuevoJ] = new JLabel();
                tipoCasilla = tablero.show(elemento);
                if(tipoCasilla == 0){
                    matriz[i][nuevoJ].setIcon(casillaReto);
                }else if(tipoCasilla == 1){
                    matriz[i][nuevoJ].setIcon(casillaTrampa);
                }else if(tipoCasilla == 2){
                    matriz[i][nuevoJ].setIcon(casillaTunel);
                }else if(tipoCasilla == 3){
                    matriz[i][nuevoJ].setIcon(casillaInicio);
                }else {
                    matriz[i][nuevoJ].setIcon(casillaFinal);
                }
                frame.add(matriz[i][nuevoJ]);
                elemento++;
            }
        }
        
        for (int i=0; i<filas; i++){
            for (int j=0; j<columnas; j++){
                matriz[i][j].setBounds(x, y, 150, 150);
                x += 142;
            }
            x = 20;
            y += 145;
        }

        SwingUtilities.updateComponentTreeUI(frame);
    }

    public void setVisibleDado(Boolean visible){
        dado.setVisible(visible);
    }

    public Boolean isVisibleDado(){
        return dado.isVisible();
    }

    public void moverFicha(int jugador, int avance){ // NO se está tomando en cuenta la casilla gráfica en donde está el jugador!!!
        if (jugador == 1) {
            posFicha1 = posFicha1 + avance;
            if (posFicha1 > 15){
                posFicha1 = 15;
            }
            else if (posFicha1 < 0){
                posFicha1 = 0;
            }
            posXficha1 = 26 + (posFicha1%4) * 142;
            posYficha1 = 106 + (posFicha1/4) * 145;
            if ((posFicha1/4)%2 == 1) {
                posXficha1 = 26 + (3-(posFicha1%4)) * 142;
            }
            ficha1.setBounds(posXficha1, posYficha1, 30, 30);
            int typeCasilla = tablero.show(posFicha1);
            if(typeCasilla == 2){
                int nuevoAvance = new Random().nextInt(3) + 1;
                System.out.println("Soy server tunel " + nuevoAvance);
                posFicha1 = posFicha1 + nuevoAvance;
                posXficha1 = 26 + (posFicha1%4) * 142;
                posYficha1 = 106 + (posFicha1/4) * 145;
                if((posFicha1/4)%2 == 1) {
                    posXficha1 = 26 + (3-(posFicha1%4)) * 142;
                }
                ficha1.setBounds(posXficha1, posYficha1, 30, 30);
            }
            else if(typeCasilla == 1){
                int nuevoAvance = new Random().nextInt(3) + 1;
                System.out.println("Soy server trampa " + nuevoAvance);
                posFicha1 = posFicha1 - nuevoAvance;
                posXficha1 = 26 + (posFicha1%4) * 142;
                posYficha1 = 106 + (posFicha1/4) * 145;
                if((posFicha1/4)%2 == 1) {
                    posXficha1 = 26 + (3-(posFicha1%4)) * 142;
                }
                ficha1.setBounds(posXficha1, posYficha1, 30, 30);
            }
            else if(typeCasilla == 0){
                setReto(1, true);
                System.out.println("Soy server reto");
            }
        }
        else if (jugador == 2){ // ES EL SEGUNDO JUGADOR
            posFicha2 = posFicha2 + avance;
            if(posFicha2 > 15){
                posFicha2 = 15;
            }
            else if (posFicha2 < 0){
                posFicha2 = 0;
            }
            int posXficha2 = 58 + (posFicha2%4) * 142;
            int posYficha2 = 106 + (posFicha2/4) * 145;
            if((posFicha2/4)%2 == 1) {
                posXficha2 = 58 + (3-(posFicha2%4)) * 142;
            }
            ficha2.setBounds(posXficha2, posYficha2, 30, 30);

            int typeCasilla = tablero.show(posFicha2);
            if(typeCasilla == 0){
                int nuevoAvance = new Random().nextInt(3) + 1;
                System.out.println("Soy cliente tunel " + nuevoAvance);
                posFicha2 = posFicha2 + nuevoAvance;
                posXficha2 = 58 + (posFicha2%4) * 142;
                posYficha2 = 106 + (posFicha2/4) * 145;
                if((posFicha2/4)%2 == 1) {
                    posXficha2 = 58 + (3-(posFicha2%4)) * 142;
                }
                ficha2.setBounds(posXficha2, posYficha2, 30, 30);
            }
            else if(typeCasilla == 1){
                int nuevoAvance = new Random().nextInt(3) + 1;
                System.out.println("Soy cliente trampa " + nuevoAvance);
                posFicha2 = posFicha2 - nuevoAvance;
                posXficha2 = 58 + (posFicha2%4) * 142;
                posYficha2 = 106 + (posFicha2/4) * 145;
                if((posFicha2/4)%2 == 1) {
                    posXficha2 = 58 + (3-(posFicha2%4)) * 142;
                }
                ficha2.setBounds(posXficha2, posYficha2, 30, 30);
            }
            else if(typeCasilla == 2){
                setReto(2, true);
                System.out.println("Soy cliente reto");
            }
        }
        SwingUtilities.updateComponentTreeUI(frame);
    }

    public void refrescarFichas(){ 
        posXficha1 = 26 + (this.posFicha1%4) * 142;
        posYficha1 = 106 + (this.posFicha1/4) * 145;
        if((this.posFicha1/4)%2 == 1) {
            posXficha1 = 26 + (3-(this.posFicha1%4)) * 142;
        }
        ficha1.setBounds(posXficha1, posYficha1, 30, 30);
        int posXficha2 = 58 + (this.posFicha2%4) * 142;
        int posYficha2 = 106 + (this.posFicha2/4) * 145;
        if((posFicha2/4)%2 == 1) {
            posXficha2 = 58 + (3-(this.posFicha2%4)) * 142;
        }
        ficha2.setBounds(posXficha2, posYficha2, 30, 30);
        SwingUtilities.updateComponentTreeUI(frame);
    }

    public JLabel getFicha1(){
        return ficha1;
    }
    public JLabel getFicha2(){
        return ficha2;
    }
    public int getPosFicha1(){
        return this.posFicha1;
    }
    public int getPosFicha2(){
        return this.posFicha2;
    }
    public void setPosFicha1(int posicion){
        this.posFicha1 = posicion;
        this.refrescarFichas();
    }
    public void setPosFicha2(int posicion){
        this.posFicha2 = posicion;
        this.refrescarFichas();
    }

    public void setVisible(boolean visibility){
        frame.setVisible(visibility);
    }

    public void setReto(int jugador, Boolean onReto){
        if(jugador == 1){
            Servidor.getInstancia().reto = onReto;
            Servidor.getInstancia().stateDado = !onReto;
        }
        if(jugador == 2){
            Cliente.getInstancia().reto = onReto;
            Cliente.getInstancia().stateDado = !onReto;
        }
    }

    public void generaReto(int jugador){
        int num1 = new Random().nextInt(50) + 1;
        int num2 = new Random().nextInt(50) + 1;
        int operador = new Random().nextInt(4);
        String num1String = Integer.toString(num1);
        String num2String = Integer.toString(num2);
        String respuesta;
        JLabel Operacion;
        if(operador==0){
            Operacion = new JLabel(num1String + " + " + num2String);
            Operacion.setBounds(550, 30, 100, 50);
            respuesta = Integer.toString(num1+num2);
        }
        else if (operador==1){
            Operacion = new JLabel(num1String + " - " + num2String);
            Operacion.setBounds(550, 30, 100, 50);
            respuesta = Integer.toString(num1-num2);
        }
        else if(operador==2){
            Operacion = new JLabel(num1String + " x " + num2String);
            Operacion.setBounds(550, 30, 100, 50);
            respuesta = Integer.toString(num1*num2);
        }
        else {
            Operacion = new JLabel(num1String + " / " + num2String);
            Operacion.setBounds(550, 30, 100, 50);
            respuesta = Integer.toString(num1/num2);
        }
        JTextField write = new JTextField();
        write.setBounds(550, 400, 50, 20);
        frame.add(write);
        JButton responder = new JButton("Enviar");
        responder.setBounds(550, 100, 100, 50);
        frame.add(responder);
        if(jugador == 1){
            Servidor.getInstancia().gameFrame.frame.add(write);
            Servidor.getInstancia().gameFrame.frame.add(responder);
            Servidor.getInstancia().gameFrame.frame.add(Operacion);
        }
        else if(jugador == 2){
            Cliente.getInstancia().gameFrame.frame.add(write);
            Cliente.getInstancia().gameFrame.frame.add(responder);
            Cliente.getInstancia().gameFrame.frame.add(Operacion);
        }
        SwingUtilities.updateComponentTreeUI(frame);
        responder.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(write.getText() != respuesta){
                    if(jugador == 1){
                        setPosFicha1(posFicha1-1);
                        setPosFicha2(posFicha2+1);
                    }
                    if(jugador == 2){
                        setPosFicha2(posFicha2-1);
                        setPosFicha1(posFicha1+1);
                    }
                }
                else{
                    if(jugador == 1){
                        setPosFicha2(posFicha2+1);
                    }
                    if(jugador == 2){
                        setPosFicha1(posFicha1+1);
                    }
                }
            }
        });
    }

    public void actualizarUI(int jugadorPorActualizar, int newX, int newY){
        if (jugadorPorActualizar == 1){
            this.posXficha1 = newX;
            this.posYficha1 = newY;
            ficha1.setBounds(posXficha1, posYficha1, 30, 30);
            SwingUtilities.updateComponentTreeUI(frame);

        }
        else{
            this.posXficha2 = newX;
            this.posYficha2 = newY;
            ficha2.setBounds(posXficha2, posYficha2, 30, 30);
            SwingUtilities.updateComponentTreeUI(frame);
        }
    }

    public DoublyLinkedList getList(){
        return this.tablero;
    }
}
