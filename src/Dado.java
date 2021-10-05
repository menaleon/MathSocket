import java.util.Random;

public class Dado {  // CLASE PARA EL COMPORTAMIENTO DEL DADO

    private static Dado instancia = null;
    private static Random random = new Random();

    public static Dado getInstancia(){
        if (instancia == null){
            instancia = new Dado();
        }
        return instancia;
    }
    public int Tirar(){ // genera números random del 1 al 3
        int resultado = random.nextInt(3) + 1;
        System.out.println(resultado);
        return resultado;
    }
}
