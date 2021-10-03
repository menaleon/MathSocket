import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

public class Servidor extends Thread{
    //Atributos de la Clase Servidor
    public static Servidor instancia = null; // Creación de variable para patrón de diseño singleton.
    public int PUERTO = 5000; // Puerto en el que se va a crear el servidor.
    public int gameState = 1; // Estado de Juego (Encendido o Apagado)
    public String nombreJugador1; // Nombre del Jugador
    public JFrame frame;
    Dado dado = new Dado();
    InterfazJuego gameFrame;
    DoublyLinkedList tablero;
    Boolean reto = false;
    Boolean enReto = false;
    Boolean stateDado;

    /*
     * Función que en primer lugar crea el servidor en el puerto escogido, luego intercambia mensajes con el
     * cliente de información del juego hasta que alguno de los jugadores gana. Luego cierra la comunicación.
     */
    //public void iniciarServer(){
    @Override
    public void run() {
        //Declaración de las variables usadas en la función.
        tablero = new DoublyLinkedList();
        ServerSocket servidor; // Variable en donde se guarda el servidor que usaremos.
        Socket socket; // Variable que va a contener la conexión entre el cliente y el servidor.
        ObjectOutputStream enviar; // Variable que funciona para enviar el tablero al cliente.
        ObjectInputStream recibir;

        try {
            servidor = new ServerSocket(PUERTO); // Inicio del Servidor
            System.out.println("Servidor Iniciado");
            socket = servidor.accept(); // un cliente ya se conectó
            // Canal para enviar el tablero en el socket. Sólo debe ejecutarse una vez, por eso va afuera del While
            enviar = new ObjectOutputStream(socket.getOutputStream());
            recibir = new ObjectInputStream(socket.getInputStream());
            gameFrame = new InterfazJuego(tablero, 1);
            frame.setVisible(false);
            Mensaje mensajeEnviado = new Mensaje(tablero, false, false, 0,0);
            enviar.writeObject(mensajeEnviado); // el tablero es una lista, es decir, aquí se envía una lista**/
            while(gameState != 0) {
                if (Servidor.getInstancia().gameFrame.isVisibleDado())
                {
                    TimeUnit.MILLISECONDS.sleep(100);
                    //Enviar coordenadas de fichaServer al cliente
                    int posServer = Servidor.getInstancia().gameFrame.getPosFicha1();
                    int posCliente = Servidor.getInstancia().gameFrame.getPosFicha2();
                    mensajeEnviado = new Mensaje(tablero, !Servidor.getInstancia().gameFrame.isVisibleDado(), reto, posCliente, posServer);
                    enviar.writeObject(mensajeEnviado); //Se envía el objeto
                    enReto = false;
                } else 
                {
                    TimeUnit.MILLISECONDS.sleep(100);
                    Mensaje mensajeRecibido = (Mensaje) recibir.readObject();
                    stateDado = mensajeRecibido.getDado();
                    reto = mensajeRecibido.getReto();
                    int posFicha2 = mensajeRecibido.getPosCliente();
                    int posFicha1 = mensajeRecibido.getPosServer();
                    Servidor.getInstancia().gameFrame.setPosFicha2(posFicha2);
                    Servidor.getInstancia().gameFrame.setPosFicha1(posFicha1);
                    gameFrame.setVisibleDado(stateDado);
                    if (reto == true && enReto == false){
                        gameFrame.generaReto(1);
                        reto = false;
                        enReto = true;
                        mensajeRecibido.setReto();
                    }
                }
            }
           socket.close();
        } catch (IOException | ClassNotFoundException | InterruptedException ex){ //Excepción al no poder crear el servidor en el puerto indicado o un fallo en la conexión.
            JOptionPane.showMessageDialog(null,"No se pudo iniciar el servidor correctamente, reinicia la aplicación:\n" + ex.toString());
        }
    }

    /*
    * Función del patrón de Diseño Singleton.
    * Funciona para mantener una única instancia de la clase servidor que se guarda dentro de los atributos
    * de la clase. Esta función retorna esta instancia y si aún no hay, la crea y la guarda.
    */
    public static Servidor getInstancia(){
        if (instancia == null){
            instancia = new Servidor();
        }
        return instancia;
    }

    /*
    * Función que me genera la interfaz de la pantalla inicial (nombre del juego, etc)
    */
    public void interfazInicio(){
        //Configuración del Label con Imagen
        JLabel mathimage = new JLabel();
        Border borde = BorderFactory.createLineBorder(Color.green, 10);
        ImageIcon mathsocket = new ImageIcon("imagenes/mathsocket.png");
        mathimage.setIcon(mathsocket);
        mathimage.setBorder(borde);
        mathimage.setBounds(387,0,298,360);

        // Configuracion del label del nombre del usuario
        JLabel nombre1 = new JLabel("Escriba su nombre");
        nombre1.setForeground(Color.black);
        nombre1.setFont(new Font("Arial Rounded MT Bold", Font.BOLD , 20));
        nombre1.setBounds(42,60, 300,40);
        nombre1.setHorizontalAlignment(JLabel.CENTER);
        nombre1.setHorizontalTextPosition(JLabel.CENTER);
        JLabel nombre2 = new JLabel("de jugador");
        nombre2.setForeground(Color.black);
        nombre2.setFont(new Font("Arial Rounded MT Bold", Font.BOLD , 20));
        nombre2.setBounds(42,80, 300,40);
        nombre2.setHorizontalAlignment(JLabel.CENTER);
        nombre2.setHorizontalTextPosition(JLabel.CENTER);
        // Configuración del campo de texto
        JTextField username = new JTextField();
        username.setBounds(42, 130, 300,40);

        //Configuración de la Imagen de Espera
        ImageIcon esqueleto = new ImageIcon("imagenes/espera.png");
        JLabel esperando = new JLabel();
        esperando.setIcon(esqueleto);
        esperando.setBounds(40,3,304,350);
        esperando.setVisible(false);

        // Configuración del Botón de Jugar
        JButton play = new JButton("Jugar");
        play.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                nombreJugador1 = username.getText();
                System.out.println(nombreJugador1);
                play.setVisible(false);
                username.setVisible(false);
                nombre1.setVisible(false);
                nombre2.setVisible(false);
                esperando.setVisible(true);
                Servidor.getInstancia().start(); // inicia el método run de la instancia del Servidor
            }
        });
        play.setHorizontalTextPosition(0);
        play.setBounds(70, 220,250,60);
        //Configuración del Frame del Inicio
        frame = new JFrame();
        frame.setTitle("MathSocket - Servidor");
        frame.setSize(700, 400);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon image = new ImageIcon("imagenes/logo.png");
        frame.setIconImage(image.getImage());
        frame.getContentPane().setBackground(Color.lightGray);
        frame.setLayout(null);
       // Agregar los componentes al frame
        frame.add(mathimage);
        frame.add(username);
        frame.add(nombre1);
        frame.add(nombre2);
        frame.add(play);
        frame.add(esperando);
    }

    public static void main(String[] args) {
        Servidor.getInstancia().interfazInicio();
    }
}