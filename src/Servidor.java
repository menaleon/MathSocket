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

public class Servidor extends Thread{

    //Atributos de la Clase Servidor
    public static Servidor instancia = null; // Creación de variable para patrón de diseño singleton
    public int PUERTO = 5000; // Puerto en el que se va a crear el servidor
    public int gameState = 0; // Estado de Juego (Encendido o Apagado)
    public String nombreJugador1; // Nombre del Jugador
    public JFrame frame;
    DoublyLinkedList tablero; //= new DoublyLinkedList();
    InterfazJuego gameFrame; //= new InterfazJuego(tablero, 1);
    Dado dado = new Dado();
    Mensaje mensaje;

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
            servidor = new ServerSocket(PUERTO);       // Inicio del Servidor
            System.out.println("Servidor Iniciado");
            socket = servidor.accept();               // Un cliente ya se conectó
            //tablero = new DoublyLinkedList();
            gameFrame = new InterfazJuego(tablero, 1);
            frame.setVisible(false);
            gameState = 1;

            mensaje = new Mensaje(tablero, false, false, 0, 0);

            // Canales para enviar y recibir objetos por socket
            enviar = new ObjectOutputStream(socket.getOutputStream());
            enviar.writeObject(mensaje); // el objeto mensaje contiene el tablero = lista

            recibir = new ObjectInputStream(socket.getInputStream());
            //NewPosition nuevasPosClient;

            while(gameState != 0){

                //Enviar coordenadas de fichaServer al cliente
                int posXServer = Servidor.getInstancia().gameFrame.getPosXficha1();
                int posYServer = Servidor.getInstancia().gameFrame.getPosYficha1();
                //NewPosition newPosServer = new NewPosition(1, posXServer,posYServer); //Objeto a enviar
                mensaje = new Mensaje(null, true, false, posXServer, posYServer);
                enviar.writeObject(mensaje); //Se envía el objeto
                ////////////////////////////////////////////////////////////////////////
               
                mensaje = (Mensaje) recibir.readObject();
                int x = mensaje.getPosicionX();
                int y = mensaje.getPosicionY();
                Servidor.getInstancia().gameFrame.getFicha2().setBounds(x,y,30,30); // actualiza ventana del cliente
                

                //Recibir, leer y actualizar coordenadas de fichaCLiente. NO SIRVE
                /**nuevasPosClient = (NewPosition) recibirPosCliente.readObject();
                int x = nuevasPosClient.getNuevaX();
                int y = nuevasPosClient.getNuevaY();
                 Servidor.getInstancia().gameFrame.getFicha2().setBounds(x, y, 30, 30);
                //System.out.println("NewposCLient X: " + x + " Y: " + y);**/


            }
            socket.close();

            // | ClassNotFoundException ex

        } catch (IOException | ClassNotFoundException ex){ //Excepción al no poder crear el servidor en el puerto indicado o un fallo en la conexión.
            //ex.printStackTrace();
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

        SwingUtilities.updateComponentTreeUI(frame);
    }

    public static void main(String[] args) {
        Servidor.getInstancia().interfazInicio();

    }
}