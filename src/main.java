import java.io.*;
import java.util.Scanner;

public class main {
    public static void main(String[] args) throws IOException {


        InputStream inBTC = main.class.getResourceAsStream("/BTC");
        InputStream inHTC = main.class.getResourceAsStream("/ETH");

        Scanner inputBTC;
        inputBTC = new Scanner(inBTC);

        Scanner inputETH;
        inputETH = new Scanner(inHTC);

        Moneda ETH = new Moneda(inputETH);
        Moneda BTC = new Moneda(inputBTC);

        /** Ejercicio 1-a **/
        System.out.println("Matriz de pasaje BTC:");
        BTC.printMatrizdePasaje();

        System.out.println("Matriz de pasaje ETH:");
        ETH.printMatrizdePasaje();

        /** Ejercicio 1-b **/
        System.out.println("Autocorrelacion BTC: ");
        BTC.printAutocorrelacion();
        System.out.println("Autocorrelacion ETH: ");
        ETH.printAutocorrelacion();

        /** Ejercicio 1-c **/
        BTC.printCorrelacionCruzada(ETH);

        /** Ejercicio 2-a **/
        Codificacion codificacion_BTC = new Codificacion(BTC.getCotizaciones());
        Codificacion codificacion_ETH = new Codificacion(ETH.getCotizaciones());

        System.out.println("Distribucion de probabilidad BTC:");
        codificacion_BTC.printDistProb();

        System.out.println("Distribucion de probabilidad ETH:");
        codificacion_ETH.printDistProb();

        /** Ejercicio 2-b **/
        System.out.println("Huffman semi-estático BTC:");
        codificacion_BTC.printCodificacionHSE();
        CodificacionBits cb_BTC_HSE = new CodificacionBits();
        cb_BTC_HSE.writeOutputFile(codificacion_BTC.getCodificacionHSE(), "BTC Huffman Semi-Estatico");
        //Tamaño original: 6,83 KB (6.998 bytes)
        //Tamaño comprimido: 1,17 KB (1.199 bytes)

        System.out.println("Huffman semi-estático ETH:");
        codificacion_ETH.printCodificacionHSE();
        CodificacionBits cb_ETH_HSE = new CodificacionBits();
        cb_ETH_HSE.writeOutputFile(codificacion_ETH.getCodificacionHSE(), "ETH Huffman Semi-Estatico");
        //Tamaño original: 5,39 KB (5.520 bytes)
        //Tamaño comprimido: 838 bytes (838 bytes)

        /** Ejercicio 2-c **/
        System.out.println("Codificacion RLC de BTC:");
        codificacion_BTC.printRLCBinario();
        CodificacionBits cb_BTC_RLC = new CodificacionBits();
        cb_BTC_RLC.writeOutputFile(codificacion_BTC.getCodificacionRLC(), " BTC RLC");
        //Tamaño comprimido: 2,84 KB (2.916 bytes)

        System.out.println("Codificacion RLC de ETH:");
        codificacion_ETH.printRLCBinario();
        CodificacionBits cb_ETH_RLC = new CodificacionBits();
        cb_ETH_RLC.writeOutputFile(codificacion_ETH.getCodificacionRLC(), " ETH RLC");
        //Tamaño comprimido: 444 bytes (444 bytes)

        //ETH se comprime mucho mas con RLC

        /** Ejercicio 3-a **/
        double[] distBTC = BTC.calcularDistribucionDeProbabilidadDeEstado();
        double[] distETH = ETH.calcularDistribucionDeProbabilidadDeEstado();

        Canal canal = new Canal(distBTC, distETH);

        /** Ejercicio 3-b **/

        canal.printRuido();
        canal.printPerdida();

    }
}

