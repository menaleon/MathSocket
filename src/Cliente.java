import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

public class Cliente extends Thread{
    //Atributos de la Clase Cliente.
    private static Cliente instancia = null; //Creación de variable para patrón de diseño singleton.
    public int PUERTO = 5000;  //Puerto al que se va a conectar el cliente.
    public String HOST = "LocalHost";  // Dirección del host al que se va a conectar el cliente.
    public int gameState = 1; // Estado de Juego (Encendido o Apagado)
    public String nombreJugador2; // Nombre del Jugador
    InterfazJuego gameFrame;
    public DoublyLinkedList tablero;
    Boolean reto = false;
    Boolean stateDado;

    /*
    * Función que en primer lugar conecta al cliente en el puerto del server, intercambia mensajes con el
    * servidor de información del juego hasta que alguno de los jugadores gana. Luego cierra la comunicación
    */
    //public void conectarServer(){
    @Override
    public void run() {
        //Declaración de las variables usadas en la función.
        Socket socket;  // Variable que va a contener la conexión entre el cliente y el servidor.

        try {
            socket = new Socket (HOST, PUERTO); // Conexión al servidor
            //  <------ Aquí se colocaría el llamado a la función que inicia la interfaz del juego
            System.out.println("Cliente Conectado");

            // Canales para enviar y recibir
            ObjectInputStream recibir = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream enviar = new ObjectOutputStream(socket.getOutputStream());

            //recibir = new DataInputStream(socket.getInputStream());
            //enviar = new DataOutputStream(socket.getOutputStream());

            // VENTANA DE JUEGO
            Mensaje mensajeRecibido = (Mensaje) recibir.readObject();
            tablero = mensajeRecibido.getTablero();
            stateDado = mensajeRecibido.getDado();
            Boolean reto = mensajeRecibido.getReto();
            gameFrame = new InterfazJuego(tablero, 2);
            gameFrame.setVisibleDado(stateDado);

            while(gameState != 0) {
                if (Cliente.getInstancia().gameFrame.isVisibleDado())
                {
                    TimeUnit.MILLISECONDS.sleep(100);
                    //Enviar coordenadas de fichaCliente al servidor
                    int posCliente = Cliente.getInstancia().gameFrame.getPosFicha2();
                    Mensaje mensajeEnviado = new Mensaje(tablero, !Cliente.getInstancia().gameFrame.isVisibleDado(), reto, posCliente);
                    enviar.writeObject(mensajeEnviado); //Se envía el objeto
                } else {
                    TimeUnit.MILLISECONDS.sleep(100);
                    mensajeRecibido = (Mensaje) recibir.readObject();
                    stateDado = mensajeRecibido.getDado();
                    reto = mensajeRecibido.getReto();
                    int posFicha1 = mensajeRecibido.getPosicion();
                    Cliente.getInstancia().gameFrame.setPosFicha1(posFicha1);
                    gameFrame.setVisibleDado(stateDado);
                    if (reto == true){
                        gameFrame.generaReto(2);
                        reto = false;
                        mensajeRecibido.setReto();
                    }
                }
            }
            socket.close();
            // <---- Aquí iria la función que me termina el juego

        } catch (IOException | ClassNotFoundException | InterruptedException ex) { //Excepción al no poder conectarse al servidor en el puerto indicado o un fallo en la conexión
            JOptionPane.showMessageDialog(null,"No hay salas de juego disponibles, reinicia el cliente:\n" + ex.toString());
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
        esperando.setBounds(30,3,304,350);
        esperando.setVisible(false);

        //Configuración del Frame del Inicio
        JFrame frame = new JFrame();
        frame.setTitle("MathSocket - Cliente");
        frame.setSize(700, 400);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon image = new ImageIcon("imagenes/logo.png");
        frame.setIconImage(image.getImage());
        frame.getContentPane().setBackground(Color.lightGray);
        frame.setLayout(null);

        // Configuración del Botón de Jugar
        JButton play = new JButton("Jugar");
        play.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                nombreJugador2 = username.getText();
                System.out.println(nombreJugador2);
                Cliente.getInstancia().start();
                frame.setVisible(false);
            }
        });
        play.setHorizontalTextPosition(0);
        play.setBounds(70, 220,250,60);

        // Agregar los componentes al frame
        frame.add(mathimage);
        frame.add(username);
        frame.add(nombre1);
        frame.add(nombre2);
        frame.add(play);
        frame.add(esperando);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // <----- Aquí se pondría la llamada a la función que inicia la interfaz de la sala de espera
        //Cliente.getInstancia().conectarServer(); //Conseguir la instancia con el singleton de Server
        Cliente.getInstancia().interfazInicio();
        //InterfazJuego pruebilla = new InterfazJuego();
    }
}