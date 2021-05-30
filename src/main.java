import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) throws IOException {
        Montecarlo montecarlo = new Montecarlo();

        //montecarlo.printMatrizdePasaje();

        Scanner input = new Scanner(new File("/home/sebastian/Documents/ing_en_Sistemas/teoria_informacion/teoria_informacion/BTC"));
        //Scanner input = new Scanner(new File("/home/sebastian/Documents/ing en Sistemas/teoria_informacion/tp_especial/ETH"));

        List<Nodo> distProbabilidad = montecarlo.calcularDistribucionDeProbabilidades(input);

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
