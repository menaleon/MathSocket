import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor{
    //Atributos de la Clase Cliente.
    private static Servidor instancia = null; // Creación de variable para patrón de diseño singleton.
    public int PUERTO = 5000; // Puerto en el que se va a crear el servidor.
    public int gameState = 0; // Estado de Juego (Encendido o Apagado)
    public String nombreJugador; // Nombre del Jugador

    /*
     * Función que en primer lugar crea el servidor en el puerto escogido, luego intercambia mensajes con el
     * cliente de información del juego hasta que alguno de los jugadores gana. Luego cierra la comunicación.
     */
    public void iniciarServer(){
        //Declaración de las variables usadas en la función.
        ServerSocket servidor; // Variable en donde se guarda el servidor que usaremos.
        Socket socket; // Variable que va a contener la conexión entre el cliente y el servidor.
        DataInputStream recibir; // Variable que funciona para recibir mensajes del cliente.
        DataOutputStream enviar; // Variable que funciona para enviar mensajes hacia el servidor.
        String mensaje; // variable donde se guarda el mensaje escrito por el usuario.
        String recibo;  // variable donde se guarda el mensaje recibido por el cliente.

        try {
            servidor = new ServerSocket(PUERTO); // Inicio del Servidor
            System.out.println("Servidor Iniciado");

            while(true){ // Se queda a la espera de que un  cliente se conecte
                socket = servidor.accept(); //un cliente ya se conectó

                // Canales para enviar y recibir
                recibir = new DataInputStream(socket.getInputStream());
                enviar = new DataOutputStream(socket.getOutputStream());

                gameState = 1;
                //  <------ Aquí se colocaría el llamado a la función que inicia la interfaz del juego

                while(gameState!=0){ // Aquí se hace un ciclo para poder enviar y recibir mensajes indefinidamente hasta que el juego se acabe

                    // Sección que envía los mensajes al cliente
                    mensaje = JOptionPane.showInputDialog("Mensaje para el Server");
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
        JFrame frame = new JFrame("Inicio");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextField username = new JTextField();
        username.setBounds(90, 100, 200,20);
        JLabel nombre = new JLabel("Escriba su nombre de jugador");
        nombre.setBounds(100,50, 200,30);
        JButton play = new JButton("Play");
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
        Servidor.getInstancia().iniciarServer(); //Conseguir la instancia con el singleton de Server.
        //Servidor.getInstancia().interfazInicio();
    }
}
