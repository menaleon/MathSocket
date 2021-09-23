import java.util.Random;
public class Dado {
    private static Dado instancia = null;
    private static Random random = new Random();
    public static Dado getInstancia(){
        if (instancia == null){
            instancia = new Dado();
        }
        return instancia;
    }
    public int Tirar(){
        int resultado = random.nextInt(3) + 1;
        System.out.println(resultado);
        return resultado;
    }
}
