/*import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;*/

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) throws IOException {
        Montecarlo montecarlo = new Montecarlo();

        //montecarlo.printMatrizdePasaje();

        //Scanner input = new Scanner(new File("C:/Users/juani/IdeaProjects/teoria_informacion/BTC"));
        //Scanner input = new Scanner(new File("/home/sebastian/Documents/ing en Sistemas/teoria_informacion/tp_especial/ETH"));

        //List<Nodo> distProbabilidad = montecarlo.calcularDistribucionDeProbabilidades(input);

        montecarlo.calcularMatrizdePasaje("BTC");
        montecarlo.calcularMatrizdePasaje("ETH");
        System.out.println("Matriz de pasaje BTC:");
        montecarlo.printMatrizdePasaje("BTC");
        System.out.println();
        System.out.println("Matriz de pasaje ETH:");
        montecarlo.printMatrizdePasaje("ETH");
        System.out.println();

        double[] acBTC = montecarlo.autocorrelacion("BTC");
        System.out.println("Autocorrelacion BTC: ");
        for (int i=0; i< acBTC.length; i++)
            System.out.println(i+": "+acBTC[i]);
        System.out.println();
        double[] acETH = montecarlo.autocorrelacion("ETH");
        System.out.println("Autocorrelacion ETH: ");
        for (int i=0; i< acETH.length; i++)
            System.out.println(i+": "+acETH[i]);
        System.out.println();

        double[] cc = montecarlo.correlacionCruzada();
        System.out.println("Correlacion Cruzada: ");
        for (int i=0; i< cc.length; i++)
            System.out.println((i*50)+": "+cc[i]);
        System.out.println();

        /*DefaultPieDataset data = new DefaultPieDataset();
        data.setValue("C", 40);
        data.setValue("Java", 45);
        data.setValue("Python", 15);
        // Creando el Grafico
        JFreeChart chart = ChartFactory.createPieChart(
                "Ejemplo Rapido de Grafico en un ChartFrame",
                data,
                true,
                true,
                false);

        // Mostrar Grafico
        ChartFrame frame = new ChartFrame("JFreeChart", chart);
        frame.pack();
        frame.setVisible(true);*/
    }
}
