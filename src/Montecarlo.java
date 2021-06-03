import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.time.Clock;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Montecarlo {
    private static final int DECRECE=0;
    private static final int MANTIENE=1;
    private static final int CRECE=2;

    private static final double EPSILON=0.01;

    private double [][] matrizPasajeBTC = new double[3][3];
    private double [][] matrizPasajeETH = new double[3][3];

    public Montecarlo(){
    }

    public void printMatrizdePasaje(String moneda) throws FileNotFoundException {
        for (int i = 0; i < matrizPasajeBTC.length; i++) {
            for (int j = 0; j < matrizPasajeBTC[i].length; j++) {
                if (moneda == "BTC")
                    System.out.print(matrizPasajeBTC[i][j] + " ");
                if (moneda == "ETH")
                    System.out.print(matrizPasajeETH[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void calcularMatrizdePasaje(String moneda) throws FileNotFoundException {
        Scanner input;
        input = new Scanner(new File("C:/Users/juani/IdeaProjects/teoria_informacion/"+moneda));
        double [][] matriz = new double[3][3];
        double [] exitos = {0,0,0};
        int valor_anterior = Integer.parseInt(input.nextLine());
        int valor_actual;
        int estado = MANTIENE;

        while (input.hasNextLine()){
            valor_actual = Integer.parseInt(input.nextLine());
            if (valor_actual > valor_anterior) {
                matriz[CRECE][estado]++;
                estado=CRECE;
            }
            if (valor_actual == valor_anterior) {
                matriz[MANTIENE][estado]++;
                estado=MANTIENE;
            }
            if (valor_actual < valor_anterior) {
                matriz[DECRECE][estado]++;
                estado=DECRECE;
            }
            exitos[estado]++;
            valor_anterior = valor_actual;
        }

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                DecimalFormat df = new DecimalFormat("#.000");
                if (moneda == "BTC")
                    matrizPasajeBTC[i][j] = Double.parseDouble(df.format(matriz[i][j]/exitos[j]));
                if (moneda == "ETH")
                    matrizPasajeETH[i][j] = Double.parseDouble(df.format(matriz[i][j]/exitos[j]));
            }
        }
        input.close();
    }

    public double[] autocorrelacion(String moneda) throws FileNotFoundException {
        double [] vector = new double[51];
        for(int i=0; i<vector.length;i++)
            vector[i] = 0;
        int emisiones = 0;
        Scanner input;
        input = new Scanner(new File("C:/Users/juani/IdeaProjects/teoria_informacion/"+moneda));
        List<Integer> cotizaciones = getCotizaciones(input);
        for (int i=0; i<cotizaciones.size(); i++){
            int valor1 = cotizaciones.get(i);
            emisiones++;
            for(int j=i; j<=i+50 && j<cotizaciones.size();j++){
                int valor2 = cotizaciones.get(j);
                vector[j-i] = vector[j-i] + (valor1 * valor2);
            }
        }
        for (int t=0; t< vector.length; t++) {
            DecimalFormat df = new DecimalFormat("#.000");
            vector[t] = Double.parseDouble(df.format((vector[t] / (emisiones - t))));
        }
        input.close();
        return vector;
    }

    public double[] correlacionCruzada() throws FileNotFoundException {
        double [] vector = new double[5];
        for(int i=0; i<vector.length;i++)
            vector[i] = 0;
        int emisiones = 0;
        Scanner inputBTC;
        Scanner inputETH;
        inputBTC = new Scanner(new File("C:/Users/juani/IdeaProjects/teoria_informacion/BTC"));
        inputETH = new Scanner(new File("C:/Users/juani/IdeaProjects/teoria_informacion/ETH"));
        List<Integer> cotizacionesBTC = getCotizaciones(inputBTC);
        List<Integer> cotizacionesETH = getCotizaciones(inputETH);
        for (int i=0; i<cotizacionesBTC.size(); i++){
            int valor1 = cotizacionesBTC.get(i);
            emisiones++;
            for(int j=i; j<=i+200 && j<cotizacionesETH.size();j=j+50){
                int valor2 = cotizacionesETH.get(j);
                vector[(j-i)/50] = vector[(j-i)/50] + (valor1 * valor2);
            }
        }
        for (int t=0; t< vector.length; t++) {
            DecimalFormat df = new DecimalFormat("#.000");
            vector[t] = Double.parseDouble(df.format((vector[t] / (emisiones - t))));
        }
        inputBTC.close();
        inputETH.close();
        return vector;
    }

    public List<Nodo> calcularDistribucionDeProbabilidades(Scanner input){
        //PREPROCESAMIENTO
        List<Integer> arrayValues = getCotizaciones(input);

        List<Integer> arrayValuesSinRepetidos = arrayValues.stream().distinct().collect(Collectors.toList());
        int arrayLength = arrayValuesSinRepetidos.size();

        AtomicInteger n = new AtomicInteger();
        List<Nodo> exitos = new ArrayList<>();
        List<Nodo> pAnt = new ArrayList<>();
        List<Nodo> pAct = new ArrayList<>();

        for (int i=0; i<arrayLength; i++){
            exitos.add(new Nodo(arrayValuesSinRepetidos.get(i),0));
            pAnt.add(new Nodo(arrayValuesSinRepetidos.get(i),-1));
            pAct.add(new Nodo(arrayValuesSinRepetidos.get(i),0));
        }

        //EJECUCION DEL ALGORITMO
        while (!converge(pAnt, pAct, arrayLength)){
            arrayValues.stream().forEach(value -> {
                sumaExitos(exitos, value);
                n.getAndIncrement();
                for(int i=0; i<arrayLength; i++){
                    Nodo ant = pAct.get(i);
                    pAnt.set(i, ant);

                    AtomicReference<Double> aciertos = getAciertos(exitos, pAct, i);

                    Nodo act = new Nodo(pAct.get(i).valor, aciertos.get()/n.get());
                    pAct.set(i, act);
                }
            });
        }
        input.close();
        return pAct;
    }

    private AtomicReference<Double> getAciertos(List<Nodo> exitos, List<Nodo> pAct, int i) {
        int finalI = i;
        AtomicReference<Double> aciertos = new AtomicReference<>((double) 0);
        exitos.stream().forEach(v -> {
            if(v.valor == pAct.get(finalI).valor){
                aciertos.set(v.acierto);
            }
        });
        return aciertos;
    }

    public List<Integer> getCotizaciones(Scanner input) {
        List<Integer> arrayValues = new ArrayList<>();

        while (input.hasNext()){
            int valorActual = Integer.parseInt(input.nextLine());
            arrayValues.add(valorActual);
        }
        return arrayValues;
    }

    private void sumaExitos(List<Nodo> exitos, Integer value) {
        List cantidadExitos = exitos.stream().filter(v -> v.valor == value).collect(Collectors.toList());
        if (cantidadExitos.size()>0){
            exitos.forEach(v -> {
                if (v.valor == value){
                    v.acierto++;
                }
            });
        }else{
            exitos.add(new Nodo(value, 1));
        }
    }

    private boolean converge(List<Nodo> pAnterior, List<Nodo> pActual, int cantidad_cotizaciones) {
        for(int i=0; i<cantidad_cotizaciones; i++){
            if(Math.abs(pAnterior.get(i).acierto-pActual.get(i).acierto) > EPSILON){
                return false;
            }
        }
        return true;
    }
}
/*
       public void printMatrizdePasaje() throws FileNotFoundException {
            for (int i = 0; i < matrizPasaje.length; i++) {
                for (int j = 0; j < matrizPasaje[i].length; j++) {
                    System.out.print(matrizPasaje[i][j] + " ");
                }
                System.out.println();
            }
        }

    private void leerArchivo() throws FileNotFoundException {
        //Scanner input = new Scanner(new File("/home/sebastian/Documents/ing_en_Sistemas/teoria_informacion/teoria_informacion/BTC"));
        //Scanner input = new Scanner(new File("/home/sebastian/Documents/ing en Sistemas/teoria_informacion/tp_especial/ETH"));

        Scanner input;
        for (int n=0;n<2;n++){
            input = new Scanner(new File("C:/Users/juani/IdeaProjects/teoria_informacion/BTC"));
            if(n==1){
                input = new Scanner(new File("C:/Users/juani/IdeaProjects/teoria_informacion/ETH"));
            }

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
                valor_anterior = valor_actual;
            }

            for (int i = 0; i < matrizPasaje.length; i++) {
                for (int j = 0; j < matrizPasaje[i].length; j++) {
                    DecimalFormat df = new DecimalFormat("#.000");
                    matrizPasaje[i][j]= Double.parseDouble(df.format(matrizPasaje[i][j]/total));
                }
            }
            input.close();
        }

    }
*/

