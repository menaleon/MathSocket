import javax.swing.*;
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
        frame = new JFrame("Inicio");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextField username = new JTextField();
        username.setBounds(90, 100, 200,20);
        JLabel nombre = new JLabel("Escriba su nombre de jugador");
        nombre.setBounds(100,50, 200,30);
        JButton play = new JButton("Play");
        play.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                nombreJugador1 = username.getText();
                System.out.println(nombreJugador1);
                play.setVisible(false);
                username.setVisible(false);
                nombre.setText("Esperando otro jugador...");
                nombre.setBounds(125, 140, 200, 50);
                Servidor.getInstancia().start(); // inicia el método run de la instancia del Servidor
            }
        });
        play.setBounds(140, 200,100,40);
        frame.add(play);
        frame.add(username);
        frame.add(nombre);
        frame.setSize(400,400);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // <----- Aquí se pondría la llamada a la función que inicia la interfaz de la sala de espera.
        //Servidor.getInstancia().iniciarServer(); //Conseguir la instancia con el singleton de Server.
        Servidor.getInstancia().interfazInicio();
    }
}