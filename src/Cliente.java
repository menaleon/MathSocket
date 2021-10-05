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
    public JFrame frame; // variable que contiene la interfaz de inicio
    public String nombreJugador2; // Nombre del Jugador
    InterfazJuego gameFrame; // Variable que guarda el objeto que crea la interfaz de juego
    public DoublyLinkedList tablero; // Variable que guarda la lista doblemente enlazada con la distribución del tablero

    // Variables para saber en que parte del juego nos encontramos
    Boolean reto = false;
    Boolean enReto = false;
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

            System.out.println("Cliente Conectado");

            // Canales para enviar y recibir
            ObjectInputStream recibir = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream enviar = new ObjectOutputStream(socket.getOutputStream());

            // VENTANA DE JUEGO
            Mensaje mensajeRecibido = (Mensaje) recibir.readObject();
            tablero = mensajeRecibido.getTablero();
            stateDado = mensajeRecibido.getDado();
            reto = mensajeRecibido.getReto();
            gameFrame = new InterfazJuego(tablero, 2); // creación del objeto de la interfaz
            gameFrame.setVisibleDado(stateDado);

            while(gameState != 0) { // se mantiene una comunicación constante mediante este while mientras se esté jugando
                if (Cliente.getInstancia().gameFrame.isVisibleDado()) // si es turno del Cliente (el dado está visible)
                {
                    TimeUnit.MILLISECONDS.sleep(100); // esperar un rato
                    //Enviar coordenadas de fichaCliente al servidor
                    int posCliente = Cliente.getInstancia().gameFrame.getPosFicha2();
                    int posServer = Cliente.getInstancia().gameFrame.getPosFicha1();
                    Mensaje mensajeEnviado = new Mensaje(tablero, !Cliente.getInstancia().gameFrame.isVisibleDado(), reto, posCliente, posServer);
                    enviar.writeObject(mensajeEnviado); //Se envían las coordenadas del Cliente al Server
                    enReto = false;

                } else { // si no es turno del Cliente

                    TimeUnit.MILLISECONDS.sleep(100);
                    mensajeRecibido = (Mensaje) recibir.readObject(); // recibe las coordenadas del Server
                    stateDado = mensajeRecibido.getDado();
                    reto = mensajeRecibido.getReto();

                    int posFicha1 = mensajeRecibido.getPosServer();
                    int posFicha2 = mensajeRecibido.getPosCliente();

                    Cliente.getInstancia().gameFrame.setPosFicha1(posFicha1); // llama a una función para actualizar las coordenadas del server en esta ventana
                    Cliente.getInstancia().gameFrame.setPosFicha2(posFicha2); // también actualiza las suyas

                    gameFrame.setVisibleDado(stateDado);

                    if (reto == true && enReto == false){  // si tiene la variable "reto" activada, llama a una función para generar un reto en pantalla
                        gameFrame.generaReto(2);
                        reto = false; // lo define como falso para que no vuelva a entrar al if
                        enReto = true;
                        mensajeRecibido.setReto();
                    }
                }
            }
            socket.close(); // se termina la comunicación por sockets cuando el juego acaba

        } catch (IOException | ClassNotFoundException | InterruptedException ex) { //Excepción al no poder conectarse al servidor en el puerto indicado o un fallo en la conexión
            JOptionPane.showMessageDialog(null,"Ha ocurrido un error al conectarse, reinicia la aplicación.\n Error:" + ex.toString());
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

    /*
     * Función de la interfaz inicial
     * Esta clase es llamada al principio para crear la interfaz en donde el usuario coloca su nombre
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
        esperando.setBounds(30,3,304,350);
        esperando.setVisible(false);

        //Configuración del Frame del Inicio
        frame = new JFrame();
        frame.setTitle("MathSocket - Cliente");
        frame.setSize(700, 400);
        frame.setLocationRelativeTo(null);
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
                Cliente.getInstancia().start(); // Se inicia el thread del cliente para jugar!!!
                frame.setVisible(false); // se desactiva la ventana de inicio del Cliente
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

    /*
     * Función main de la clase
     * Se ejecuta al inicio, crea la instancia del singleton y llama a la función que crea la interfaz de inicio
     */
    public static void main(String[] args) {
        Cliente.getInstancia().interfazInicio();
    }
}