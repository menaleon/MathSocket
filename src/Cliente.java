import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente extends Thread{
    //Atributos de la Clase Cliente.
    private static Cliente instancia = null; //Creación de variable para patrón de diseño singleton.
    public int PUERTO = 5000;  //Puerto al que se va a conectar el cliente.
    public String HOST = "LocalHost";  // Dirección del host al que se va a conectar el cliente.
    public int gameState = 0; // Estado de Juego (Encendido o Apagado)
    public String nombreJugador2; // Nombre del Jugador

    /*
    * Función que en primer lugar conecta al cliente en el puerto del server, intercambia mensajes con el
    * servidor de información del juego hasta que alguno de los jugadores gana. Luego cierra la comunicación
    */
    //public void conectarServer(){ @Override
    public void run() {
        //Declaración de las variables usadas en la función.
        Socket socket;  // Variable que va a contener la conexión entre el cliente y el servidor.
        DataInputStream recibir;  // Variable que funciona para recibir mensajes del servidor.
        DataOutputStream enviar;  // Variable que funciona para enviar mensajes hacia el servidor.
        String mensaje; // variable donde se guarda el mensaje escrito por el usuario.
        String recibo; // variable donde se guarda el mensaje recibido por el servidor.

        try {
            socket = new Socket (HOST, PUERTO); // Conexión al servidor
            gameState = 1;
            //  <------ Aquí se colocaría el llamado a la función que inicia la interfaz del juego
            System.out.println("Cliente Conectado");

            // Canales para enviar y recibir
            recibir = new DataInputStream(socket.getInputStream());
            enviar = new DataOutputStream(socket.getOutputStream());

            while(gameState != 0){ // Aquí se hace un ciclo para poder enviar y recibir mensajes indefinidamente hasta que el juego se acabe

                // Sección que recibe el mensaje del Servidor y lo interpreta.
                recibo = recibir.readUTF();
                System.out.println(recibo);
                // <-- Aquí se pondría la función para interpretar el mensaje.

                // Sección que envía los mensajes al servidor
                mensaje = JOptionPane.showInputDialog("Mensaje para el Cliente");
                enviar.writeUTF(mensaje);
            }
            socket.close();
            // <---- Aquí iria la función que me termina el juego

        } catch (IOException ex){ //Excepción al no poder conectarse al servidor en el puerto indicado o un fallo en la conexión
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Función del patrón de Diseño Singleton
     * Funciona para mantener una única instancia de la clase cliente que se guarda dentro de los atributos
     * de la clase. Esta función retorna esta instancia y si aún no hay, la crea y la guarda.
     */
    public static Cliente getInstancia(){
        if (instancia == null){
            instancia = new Cliente();
        }
        return instancia;
    }

    public void interfazInicio(){
        JFrame frame = new JFrame("Inicio");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextField username = new JTextField();
        username.setBounds(90, 100, 200,20);
        JLabel nombre = new JLabel("Escriba su nombre de jugador");
        nombre.setBounds(100,50, 200,30);
        JButton play = new JButton("Play");
        play.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                nombreJugador2 = username.getText();
                System.out.println(nombreJugador2);
                Cliente.getInstancia().start();
                frame.setVisible(false);
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
        // <----- Aquí se pondría la llamada a la función que inicia la interfaz de la sala de espera
        //Cliente.getInstancia().conectarServer();//Conseguir la instancia con el singleton de Server
        Cliente.getInstancia().interfazInicio();
    }
}
