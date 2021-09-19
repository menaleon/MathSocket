public class Adapter {
    int c1;
    int c2;
    int c3;
    int c4;
    int c5;
    int c6;
    int c7;
    int c8;
    int c9;
    int c10;
    int c11;
    int c12;
    int c13;
    int c14;
    int c15;
    int c16;

    public static void main(String[] args) {
        System.out.println("Sirve");
    }

    public Adapter (int c1, int c2, int c3, int c4, int c5, int c6, int c7, int c8, int c9, int c10, int c11, int c12, int c13, int c14, int c15, int c16) {
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.c4 = c4;
        this.c5 = c5;
        this.c6 = c6;
        this.c7 = c7;
        this.c8 = c8;
        this.c9 = c9;
        this.c10 = c10;
        this.c11 = c11;
        this.c12 = c12;
        this.c13 = c13;
        this.c14 = c14;
        this.c15 = c15;
        this.c16 = c16;
    }
    
    public String toString() {
        return String.valueOf(c1) + "," + String.valueOf(c2) + "," + String.valueOf(c3) + "," + String.valueOf(c4) + "," + String.valueOf(c5) + "," + String.valueOf(c6) + "," + String.valueOf(c7) + "," + String.valueOf(c8) + "," + String.valueOf(c9) + "," + String.valueOf(c10) + "," + String.valueOf(c11) + "," + String.valueOf(c12) + "," + String.valueOf(c13) + "," + String.valueOf(c14) + "," + String.valueOf(c15) + "," + String.valueOf(c16);
    } 
    
    public static Adapter fromString (String str) {
        String[] stringArray = str.split(",");
        int c1 = Integer.parseInt(stringArray[0]);
        int c2 = Integer.parseInt(stringArray[1]);
        int c3 = Integer.parseInt(stringArray[2]);
        int c4 = Integer.parseInt(stringArray[3]);
        int c5 = Integer.parseInt(stringArray[4]);
        int c6 = Integer.parseInt(stringArray[5]);
        int c7 = Integer.parseInt(stringArray[6]);
        int c8 = Integer.parseInt(stringArray[7]);
        int c9 = Integer.parseInt(stringArray[8]);
        int c10 = Integer.parseInt(stringArray[9]);
        int c11 = Integer.parseInt(stringArray[10]);
        int c12 = Integer.parseInt(stringArray[11]);
        int c13 = Integer.parseInt(stringArray[12]);
        int c14 = Integer.parseInt(stringArray[13]);
        int c15 = Integer.parseInt(stringArray[14]);
        int c16 = Integer.parseInt(stringArray[15]);
        return new Adapter (c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16);
    }
}