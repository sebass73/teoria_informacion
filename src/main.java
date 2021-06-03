/*import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;*/

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.io.*;
import java.util.Scanner;

public class main {
    public static void main(String[] args) throws IOException {

        Scanner inputBTC;
        inputBTC = new Scanner(new File("./BTC"));

        Scanner inputETH;
        inputETH = new Scanner(new File("./ETH"));

        Moneda ETH = new Moneda(inputETH);
        Moneda BTC = new Moneda(inputBTC);

        //System.out.println("Matriz de pasaje BTC:");
        //BTC.calcularMatrizdePasaje();

        //System.out.println("Matriz de pasaje ETH:");
        //ETH.calcularMatrizdePasaje();

        //BTC.printAutocorrelacion();
        //ETH.printAutocorrelacion();

        //BTC.printCorrelacionCruzada(ETH);

    }
}
        //List<Nodo> distProbabilidad = montecarlo.calcularDistribucionDeProbabilidades(input);
        /*System.out.println();
        moneda.printMatrizdePasaje();
        System.out.println();


        double[] acETH = moneda.autocorrelacion("ETH");
        System.out.println("Autocorrelacion ETH: ");
        for (int i=0; i< acETH.length; i++)
            System.out.println(i+": "+acETH[i]);
        System.out.println();

        double[] cc = moneda.correlacionCruzada();
        System.out.println("Correlacion Cruzada: ");
        for (int i=0; i< cc.length; i++)
            System.out.println((i*50)+": "+cc[i]);
        System.out.println();

        DefaultPieDataset data = new DefaultPieDataset();
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
        frame.setVisible(true);
    }
}*/
