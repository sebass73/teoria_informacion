import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Moneda {
    private static final int DECRECE=0;
    private static final int MANTIENE=1;
    private static final int CRECE=2;

    private static final double EPSILON=0.01;

    private double [][] matrizPasaje = new double[3][3];

    private List<Integer> cotizaciones;

    public class Nodo {
        int valor;
        double acierto;

        public Nodo(Integer valor, double acierto) {
            this.valor=valor;
            this.acierto=acierto;
        }
    }

    public Moneda(Scanner moneda){
        this.cotizaciones = calcularCotizaciones(moneda);
    }

    public List<Integer> getCotizaciones(){
        return this.cotizaciones;
    }

    public void printMatrizdePasaje() throws FileNotFoundException {
        calcularMatrizdePasaje();
        for (int i = 0; i < matrizPasaje.length; i++) {
            for (int j = 0; j < matrizPasaje[i].length; j++) {
                    System.out.print(matrizPasaje[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printAutocorrelacion() {
        double[] ac = autocorrelacion();
        System.out.println("Autocorrelacion BTC: ");
        for (int i=0; i< ac.length; i++)
            System.out.println(i+": "+ac[i]);
        System.out.println();
    }

    public void printCorrelacionCruzada(Moneda moneda){
        double[] cc = moneda.correlacionCruzada(moneda);
        System.out.println("Correlacion Cruzada: ");
        for (int i=0; i< cc.length; i++)
            System.out.println((i*50)+": "+cc[i]);
        System.out.println();
    }

    public void calcularMatrizdePasaje() {
        double [][] matriz = new double[3][3];
        double [] exitos = {0,0,0};
        int valor_anterior;
        int valor_actual;
        int estado = MANTIENE;

        for (int i=1; i<cotizaciones.size()-1;i++){
            valor_anterior = cotizaciones.get(i-1);
            valor_actual = cotizaciones.get(i);

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
        }

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                DecimalFormat df = new DecimalFormat("#.000");
                matrizPasaje[i][j] = Double.parseDouble(df.format(matriz[i][j]/exitos[j]));
            }
        }
    }

    private List<Integer> calcularCotizaciones(Scanner input) {
        List<Integer> arrayValues = new ArrayList<>();

        while (input.hasNext()){
            int valorActual = Integer.parseInt(input.nextLine());
            arrayValues.add(valorActual);
        }
        return arrayValues;
    }

    private double[] autocorrelacion() {
        double [] vector = new double[51];
        for(int i=0; i<vector.length;i++)
            vector[i] = 0;
        int emisiones = 0;
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
        return vector;
    }

    public double[] correlacionCruzada(Moneda moneda) {
        double [] vector = new double[5];
        List<Integer> cotizacionesMoneda = moneda.getCotizaciones();
        for(int i=0; i<vector.length;i++)
            vector[i] = 0;
        int emisiones = 0;
        for (int i=0; i<cotizaciones.size(); i++){
            int valor1 = cotizaciones.get(i);
            emisiones++;
            for(int j=i; j<=i+200 && j<cotizacionesMoneda.size();j=j+50){
                int valor2 = cotizacionesMoneda.get(j);
                vector[(j-i)/50] = vector[(j-i)/50] + (valor1 * valor2);
            }
        }
        for (int t=0; t< vector.length; t++) {
            DecimalFormat df = new DecimalFormat("#.000");
            vector[t] = Double.parseDouble(df.format((vector[t] / (emisiones - t))));
        }
        return vector;
    }

    public List<Nodo> calcularDistribucionDeProbabilidades(){
        //PREPROCESAMIENTO
        List<Integer> arrayValuesSinRepetidos = cotizaciones.stream().distinct().collect(Collectors.toList());
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
            cotizaciones.stream().forEach(value -> {
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