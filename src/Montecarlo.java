import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Montecarlo {
    private static final int DECRECE=0;
    private static final int MANTIENE=1;
    private static final int CRECE=2;

    private static final double EPSILON=0.01;

    private double [][] matrizPasaje = new double[3][3];

    public Montecarlo(){
    }

    public void printMatrizdePasaje() throws FileNotFoundException {
        this.leerArchivo();
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
            input = new Scanner(new File("/home/sebastian/Documents/ing_en_Sistemas/teoria_informacion/teoria_informacion/BTC"));
            if(n==1){
                input = new Scanner(new File("/home/sebastian/Documents/ing_en_Sistemas/teoria_informacion/tp_especial/ETH"));
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

    public void autocorrelacion(){

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

    private List<Integer> getCotizaciones(Scanner input) {
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
