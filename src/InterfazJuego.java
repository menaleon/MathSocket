import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class InterfazJuego extends Component {
    JFrame frame; // pestaña donde se genera la interfaz
    DoublyLinkedList tablero; // lista doblemente enlazada que contiene la distribución del tablero
    JLabel [][] matriz; // matriz de labels (esta la hacemos para simplificar el codigo usando reiteración y poder hacer el tablero como una serpiente)
    JButton dado; // botón para tirar el dado
    JButton responder; // botón para enviar respuesta
    int jugador; // variable que identifica si es el servidor o el cliente quien quiere realizar una acción
    int x = 20; // Coordenada x variable para cada botón en la matriz
    int y = 100; // Coordenada y variable para botón en la matriz
    int filas = 4; // Cantidad de filas del tablero
    int columnas = 4; // Cantidad de columnas del tablero
    int elemento = 0; //
    int tipoCasilla; // Puede ser 0 (reto), 1 (trampa), 2 (tunel), 3 (inicio) o 4 (final)
    JLabel ficha1; // ficha del jugador 1
    JLabel ficha2; // ficha del jugador 2
    int posFicha1 = 0; // posicion del jugador 1
    int posFicha2 = 0; // posicion del jugador 2
    int posXficha1; // posicion en x en la interfaz de la ficha 1
    int posYficha1; // posicion en y en la interfaz de la ficha 1
    int posXficha2; // posicion en x en la interfaz de la ficha 2
    int posYficha2; // posicion en y en la interfaz de la ficha 2

    public InterfazJuego(DoublyLinkedList tablero, int jugador){
        // Definir las imagenes que vamos a utilizar
        this.tablero = tablero;
        this.jugador = jugador;

        // Declaración de las imagenes de las casillas del tablero
        ImageIcon casillaInicio = new ImageIcon("imagenes/inicio.png");
        ImageIcon casillaReto = new ImageIcon("imagenes/reto.png");
        ImageIcon casillaTrampa = new ImageIcon("imagenes/trampa.png");
        ImageIcon casillaTunel = new ImageIcon("imagenes/tunel.png");
        ImageIcon casillaFinal = new ImageIcon("imagenes/final.png");

        // Ventana de juego
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
                if (responder== null || responder.isVisible()==false){
                    moverFicha(jugador,Dado.getInstancia().Tirar());
                    setVisibleDado(false);
                }
                else{
                    JOptionPane.showMessageDialog(frame,"Debe responder el reto para continuar");
                }
            }
        });

        // Imagen de las Fichas de los Jugadores
        ImageIcon J1 = new ImageIcon("imagenes/J1.png");
        ImageIcon J2 = new ImageIcon("imagenes/J2.png");

        // Creación y posicionamiento de las fichas de los jugadores
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

        // Se rellena la matriz usando como base la lista doblemente enlazada y en forma de serpiente (zig zag)
        for (int i=0; i<filas; i++){
            for (int j=0; j<columnas; j++){

                int nuevoJ = j;
                if(i%2==1){
                    nuevoJ = 3-j;
                }
                matriz[i][nuevoJ] = new JLabel();
                tipoCasilla = tablero.show(elemento);

                if(tipoCasilla == 0){ // se añade la casilla reto
                    matriz[i][nuevoJ].setIcon(casillaReto);
                }else if(tipoCasilla == 1){ // se añade casilla trampa
                    matriz[i][nuevoJ].setIcon(casillaTrampa);
                }else if(tipoCasilla == 2){ // se añade casilla tunel
                    matriz[i][nuevoJ].setIcon(casillaTunel);
                }else if(tipoCasilla == 3){ // se añade casilla inicio
                    matriz[i][nuevoJ].setIcon(casillaInicio);
                }else { // se añade casilla "final"
                    matriz[i][nuevoJ].setIcon(casillaFinal);
                }
                frame.add(matriz[i][nuevoJ]); // se coloca el Label en la ventana para poder verlo
                elemento++;
            }
        }
        // Se agregan los componentes de la matriz en el frame y se acomodan
        for (int i=0; i<filas; i++){
            for (int j=0; j<columnas; j++){
                matriz[i][j].setBounds(x, y, 150, 150);
                x += 142;
            }
            x = 20;
            y += 145;
        }

        // Se actualiza el frame para evitar problemas con los componentes
        SwingUtilities.updateComponentTreeUI(frame);
    }

    public void setVisibleDado(Boolean visible){ // activa o desactiva el dado según se necesite
        dado.setVisible(visible);
    }

    public Boolean isVisibleDado(){ // informa si el dado es visible (activo) o no
        return dado.isVisible();
    }

    public void moverFicha(int jugador, int avance){ // mueve las fichas de los jugadores dependiendo de cuál sea

        if (jugador == 1) { // ES EL JUGADOR 1 (SERVER)
            setPosFicha1(posFicha1+avance);
            refrescarFichas(); // se llama al método que mueve las fichas

            int typeCasilla = tablero.show(posFicha1);

            if(typeCasilla == 2){ // si la casilla es de túnel, avanza ciertas casillas en el Server
                int nuevoAvance = new Random().nextInt(3) + 1;
                System.out.println("Soy server tunel " + nuevoAvance);
                setPosFicha1(posFicha1+nuevoAvance);
                refrescarFichas();
            }
            else if(typeCasilla == 1){ // si la casilla es de trampa, se devuelve varias o una casilla (máximo 3)
                int nuevoAvance = new Random().nextInt(3) + 1;
                System.out.println("Soy server trampa " + nuevoAvance);
                setPosFicha1(posFicha1-nuevoAvance); // ajusta la posición del jugador Server, lo devuelve en el tablero
                refrescarFichas();
            }
            else if(typeCasilla == 0){ // si la casilla es de reto, se establece la variable "reto" como verdadera en el Server
                setReto(1, true);
                System.out.println("Soy server reto");
            }
        }
        else if (jugador == 2){ // ES EL SEGUNDO JUGADOR (CLIENTE)
            setPosFicha2(posFicha2+avance);
            refrescarFichas();

            int typeCasilla = tablero.show(posFicha2);

            if(typeCasilla == 2){ // si la casilla es de túnel, avanza ciertas casillas en el Server
                int nuevoAvance = new Random().nextInt(3) + 1;
                System.out.println("Soy cliente tunel " + nuevoAvance);
                setPosFicha2(posFicha2+nuevoAvance);
                refrescarFichas();
            }
            else if(typeCasilla == 1){ // si la casilla es de trampa, se devuelve varias o una casilla (máximo 3)
                int nuevoAvance = new Random().nextInt(3) + 1;
                System.out.println("Soy cliente trampa " + nuevoAvance);
                setPosFicha2(posFicha2-nuevoAvance); // ajusta la posición del jugador Cliente, lo devuelve en el tablero
                refrescarFichas();
            }
            else if(typeCasilla == 0){ // si la casilla es de reto, se establece la variable "reto" como verdadera en el Cliente
                setReto(2, true);
                System.out.println("Soy cliente reto");
            }
        }
        SwingUtilities.updateComponentTreeUI(frame);
    }

    public void refrescarFichas(){ // actualiza la posición de las fichas
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

    public int getPosFicha1(){ // devuelve el label del jugador 1 (Server)
        return this.posFicha1;
    }
    public int getPosFicha2(){ // devuelve el label del jugador 1 (Server)
        return this.posFicha2;
    }
    public void setPosFicha1(int posicion){ // configura la posición del jugador1 (Server)
        if (posicion<0){ // por si se pasa de los límites de la pantalla, esto lo posiciona en la primera casilla disponible
            posicion =0;
        }
        else if (posicion > 15){ // por si se pasa de los límites de la pantalla, esto lo posiciona en la última casilla disponible
            posicion = 15;
        }
        this.posFicha1 = posicion;
        this.refrescarFichas(); // refresca las posiciones de las fichas

        // En caso de que alguien haya ganado el juego
        if (posFicha1 == 15){
            Servidor.getInstancia().gameState = 0;
            Cliente.getInstancia().gameState = 0;
            String ganador = Servidor.getInstancia().nombreJugador1;
            if (ganador == null){ // si no gané yo, entonces ganó mi rival
                ganador = "Su rival";
            }
            JOptionPane.showMessageDialog(frame, ganador +" gano");
        }
    }
    public void setPosFicha2(int posicion){ // configura la posición del jugador 2 (Cliente)

        if (posicion<0){ // por si se pasa de los límites de la pantalla, esto lo posiciona en la primera casilla disponible
            posicion =0;
        }
        else if (posicion > 15){ // por si se pasa de los límites de la pantalla, esto lo posiciona en la última casilla disponible
            posicion = 15;
        }
        this.posFicha2 = posicion;
        this.refrescarFichas(); // refresca las posiciones de las fichas

        // En caso de que alguien haya ganado el juego
        if (posFicha2 == 15){
            Servidor.getInstancia().gameState = 0;
            Cliente.getInstancia().gameState = 0;
            String ganador = Cliente.getInstancia().nombreJugador2;

            if (ganador == null){ // si no gané yo, entonces ganó mi rival
                ganador = "Su rival";
            }
            JOptionPane.showMessageDialog(frame, ganador +" gano");
        }
    }

    public void setVisible(boolean visibility){ // settea la visibilidad de la ventana
        frame.setVisible(visibility);
    }

    public void setReto(int jugador, Boolean onReto){ // settea la variable reto de un jugador, dependiendo de cuál sea
        if(jugador == 1){
            Servidor.getInstancia().reto = onReto;
            Servidor.getInstancia().stateDado = !onReto;
        }
        if(jugador == 2){
            Cliente.getInstancia().reto = onReto;
            Cliente.getInstancia().stateDado = !onReto;
        }
    }

    public void generaReto(int jugador){     // genera aleatoriamente el reto

        int num1 = new Random().nextInt(50) + 1;
        int num2 = new Random().nextInt(50) + 1;
        int operador = new Random().nextInt(4);
        String num1String = Integer.toString(num1); // convierte números a String para poder mostrarlos en la ventana de juego
        String num2String = Integer.toString(num2);
        String respuesta;
        JLabel Operacion;

        // Genera operaciones aleatorias
        if(operador==0){
            Operacion = new JLabel(num1String + " + " + num2String);
            Operacion.setBounds(100, 20, 100, 50);
            respuesta = Integer.toString(num1+num2);
        }
        else if (operador==1){
            Operacion = new JLabel(num1String + " - " + num2String);
            Operacion.setBounds(100, 20, 100, 50);
            respuesta = Integer.toString(num1-num2);
        }
        else if(operador==2){
            Operacion = new JLabel(num1String + " x " + num2String);
            Operacion.setBounds(100, 20, 100, 50);
            respuesta = Integer.toString(num1*num2);
        }
        else {
            Operacion = new JLabel(num1String + " / " + num2String);
            Operacion.setBounds(100, 20, 100, 50);
            respuesta = Integer.toString(num1/num2);
        }

        JTextField write = new JTextField();
        write.setBounds(200, 20, 50, 20);
        frame.add(write);
        responder = new JButton("Enviar");
        responder.setBounds(350, 20, 80, 50);
        frame.add(responder);

        // Añade los componentes del reto en la ventana de un jugador, dependiendo de cuál jugador sea
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
                if(write.getText().equals(respuesta)==false){
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
                responder.setVisible(false);
                write.setVisible(false);
                Operacion.setVisible(false);
            }
        });
    }

    public DoublyLinkedList getList(){
        return this.tablero;
    }
}
