import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Montecarlo {
    //private double [] vector = {0,0,0};
    private static final int DECRECE=0;
    private static final int MANTIENE=1;
    private static final int CRECE=2;
    private double [][] matrizPasaje = new double[3][3];

    public Montecarlo(){
    }

    public void printMatriz() throws FileNotFoundException {
        this.leerArchivo();
        for (int i = 0; i < matrizPasaje.length; i++) {
            for (int j = 0; j < matrizPasaje[i].length; j++) {
                System.out.print(matrizPasaje[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void leerArchivo() throws FileNotFoundException {
        Scanner input = new Scanner(new File("/home/sebastian/Documents/ing en Sistemas/teoria_informacion/tp_especial/BTC"));
        //Scanner input = new Scanner(new File("/home/sebastian/Documents/ing en Sistemas/teoria_informacion/tp_especial/ETH"));

        int valor_anterior = Integer.parseInt(input.nextLine());
        int total = 0;
        //Estados: 0="Decrece", 1="Mantiene", 2="Crece". Por default será "Mantiene"
        int estado=1;

        while (input.hasNextLine()) {
            int valor_actual = Integer.parseInt(input.nextLine());
            if (valor_actual > valor_anterior) {
                matrizPasaje[CRECE][estado]++;
                estado=CRECE;
            }
            if (valor_actual == valor_anterior) {
                matrizPasaje[MANTIENE][estado]++;
                estado=MANTIENE;
            }
            if (valor_actual < valor_anterior) {
                matrizPasaje[DECRECE][estado]++;
                estado=DECRECE;
            }
            total++;
        }

        for (int i = 0; i < matrizPasaje.length; i++) {
            for (int j = 0; j < matrizPasaje[i].length; j++) {
                //matrizPasaje[i][j]=matrizPasaje[i][j]/total;
                DecimalFormat df = new DecimalFormat("#.000");
                matrizPasaje[i][j]= Double.parseDouble(df.format(matrizPasaje[i][j]/total));
            }
        }
        input.close();
    }
}
