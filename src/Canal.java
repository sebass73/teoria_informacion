import java.text.DecimalFormat;

public class Canal {

    private final double [] distribucionX;
    private final double [] distribucionY;

    DecimalFormat df = new DecimalFormat("#.000");

    public Canal(double [] x, double [] y){
        this.distribucionX = x;
        this.distribucionY = y;
    }
    private double[][] calcularMatrizConjunta(){
        double [][] matrizConjunta = new double[3][3];
        for(int i=0; i<matrizConjunta.length;i++){
            for (int j=0; j<matrizConjunta.length;j++){
                matrizConjunta[i][j] = Double.parseDouble(this.df.format(distribucionX[i] * distribucionY[j]));
            }
        }
        return matrizConjunta;
    }

    private double [][] calcularMatrizCondicionalYX(){
        double [][] matrizConjunta = this.calcularMatrizConjunta();
        double [][] matrizCondicional = new double[3][3];
        for (int j=0; j<matrizConjunta.length;j++) {
            for (int i = 0; i < matrizConjunta.length; i++) {
                matrizCondicional[i][j] = Double.parseDouble(this.df.format(matrizConjunta[i][j]/distribucionX[i]));
            }
        }
        return matrizCondicional;
    }

    public void printRuido(){
        System.out.println("Ruido");
        System.out.println("H(y/x)= " + this.ruido());
    }

    private double ruido(){
        double [] rChica = this.calcularRChica();
        double ruido = 0;
        for (int i=0; i<distribucionX.length; i++){
            ruido += distribucionX[i] * rChica[i];
        }
        return Double.parseDouble(this.df.format(ruido));
    }

    private double[] calcularRChica(){
        double [][] matrizCondicional = this.calcularMatrizCondicionalYX();
        double [] r = new double[3];
        for(int i=0; i<distribucionX.length; i++){
            r[i]=0;
            for(int j=0; j<distribucionY.length; j++) {
                double yDadox=matrizCondicional[i][j];
                r[i] += Double.parseDouble(this.df.format(yDadox * Math.log10(yDadox)/Math.log10(2)));
            }
            r[i] = r[i] * -1;
        }
        return r;
    }

    private double[][] calcularMatrizCondicionalXY(){
        double [][] matrizCondicionalYX = this.calcularMatrizCondicionalYX();
        double [][] matrizCondicionalXY = new double[3][3];

        for (int i=0; i<distribucionX.length; i++){
            for (int j=0; j<distribucionY.length; j++){
                matrizCondicionalXY[i][j] = Double.parseDouble(this.df.format((distribucionX[i]*matrizCondicionalYX[i][j])/distribucionY[j]));
            }
        }
        return matrizCondicionalXY;
    }

    private double[] calcularPi(){
        double [][] matrizCondicional = this.calcularMatrizCondicionalXY();
        double [] pi = new double[3];

        for(int j=0; j<distribucionY.length; j++){
            pi[j]=0;
            for(int i=0; i<distribucionX.length; i++) {
                double xDadoY=matrizCondicional[i][j];
                pi[j] += Double.parseDouble(this.df.format(xDadoY * Math.log10(xDadoY)/Math.log10(2)));
            }
            pi[j] = pi[j] * -1;
        }
        return pi;
    }

    private double perdida(){
        double [] pi = this.calcularPi();
        double perdida = 0;
        for (int i=0; i<distribucionY.length; i++){
            perdida += distribucionY[i] * pi[i];
        }
        return Double.parseDouble(this.df.format(perdida));
    }

    public void printPerdida(){
        System.out.println("Perdida:");
        System.out.println("H(x/y)= " + this.perdida());
    }

}
