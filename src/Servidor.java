import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor extends Thread{
    //Atributos de la Clase Cliente.
    private static Servidor instancia = null; // Creación de variable para patrón de diseño singleton.
    public int PUERTO = 5000; // Puerto en el que se va a crear el servidor.
    public int gameState = 0; // Estado de Juego (Encendido o Apagado)
    public String nombreJugador1; // Nombre del Jugador
    private JFrame frame;
    private JFrame winGame;
    private DoublyLinkedList tablero;

    /*
     * Función que en primer lugar crea el servidor en el puerto escogido, luego intercambia mensajes con el
     * cliente de información del juego hasta que alguno de los jugadores gana. Luego cierra la comunicación.
     */
    //public void iniciarServer(){
    @Override
    public void run() {
        //Declaración de las variables usadas en la función.
        ServerSocket servidor; // Variable en donde se guarda el servidor que usaremos.
        Socket socket; // Variable que va a contener la conexión entre el cliente y el servidor.

        DataInputStream recibir; // Variable que funciona para recibir mensajes del cliente.
        DataOutputStream enviar; // Variable que funciona para enviar mensajes hacia el servidor.
        ObjectOutputStream enviarTablero; // Variable que funciona para enviar el tablero al cliente.

        String mensaje; // variable donde se guarda el mensaje escrito por el usuario.
        String recibo;  // variable donde se guarda el mensaje recibido por el cliente.

        try {
            servidor = new ServerSocket(PUERTO); // Inicio del Servidor
            System.out.println("Servidor Iniciado");
            socket = servidor.accept(); // un cliente ya se conectó
            frame.setVisible(false);

            // VENTANA DE JUEGO
            winGame = new JFrame("SERVIDOR");
            winGame.setVisible(true);
            tablero = new DoublyLinkedList(); // lista que dibuja la interfaz
            winGame.add(tablero);

            winGame.getContentPane().setBackground(Color.darkGray);
            winGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            winGame.setBounds(100, 100, 800, 550);

            winGame.setResizable(false);

            // Canal para enviar el tablero en el socket. Sólo debe ejecutarse una vez, por eso va afuera del While
            enviarTablero = new ObjectOutputStream(socket.getOutputStream());
            enviarTablero.writeObject(tablero); // el tablero es una lista, es decir, aquí se envía una lista

            while(true){ // Se queda a la espera de que un  cliente se conecte

                // Canales para enviar y recibir
                recibir = new DataInputStream(socket.getInputStream());
                enviar = new DataOutputStream(socket.getOutputStream());

                gameState = 1;
                //  <------ Aquí se colocaría el llamado a la función que inicia la interfaz del juego

                while(gameState!=0){ // Aquí se hace un ciclo para poder enviar y recibir mensajes indefinidamente hasta que el juego se acabe

                    // Sección que envía los mensajes al cliente
                    mensaje = JOptionPane.showInputDialog("Mensaje desde Server para el Cliente");
                    enviar.writeUTF(mensaje);

                    // Sección que recibe el mensaje del Cliente y lo interpreta.
                    recibo = recibir.readUTF();
                    System.out.println(recibo);
                    // <-- Aquí se pondría la función para interpretar el mensaje.

                }
                // <---- Aquí iria la función que me termina el juego.
                socket.close();
            }
        } catch (IOException ex){ //Excepción al no poder crear el servidor en el puerto indicado o un fallo en la conexión.
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
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
        JFrame frame = new JFrame();
        frame.setTitle("MathSocket");
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
        // <----- Aquí se pondría la llamada a la función que inicia la interfaz de la sala de espera.
        //Servidor.getInstancia().iniciarServer(); //Conseguir la instancia con el singleton de Server.
        Servidor.getInstancia().interfazInicio();
    }
}