import java.util.HashMap;

public class ArbolHuffman implements Comparable<ArbolHuffman>{

        private int valor;
        private double probabilidad;
        private ArbolHuffman hijoIzq;
        private ArbolHuffman hijoDer;

        public ArbolHuffman(int valor, double probabilidad, ArbolHuffman hijoIzq, ArbolHuffman hijoDer) {
            this.valor = valor;
            this.probabilidad = probabilidad;
            this.hijoIzq = hijoIzq;
            this.hijoDer = hijoDer;
        }

        public void printPreOrder(ArbolHuffman nodo) {
            if (nodo != null) {
                System.out.print("proba: " + nodo.getProbabilidad() + "\n");
                printPreOrder(nodo.getHijoIzq());
                printPreOrder(nodo.getHijoDer());
            }
        }

        public void preOrden(){
            System.out.println(this.getProbabilidad());
            if(this.hijoIzq != null){
                this.hijoIzq.preOrden();
            }
            if(this.hijoDer != null){
                this.hijoDer.preOrden();
            }

        }

        public HashMap<Integer, String> getCodigos(){
            String codificacion = "";
            HashMap<Integer, String> codigos = new HashMap<>();
            return getCodigosRecursivo(codificacion, this, codigos);
        }

        public HashMap<Integer, String> getCodigosRecursivo(String codificacion, ArbolHuffman nodoAct, HashMap<Integer, String> codigos){
            if(nodoAct.getHijoIzq() == null & nodoAct.getHijoDer() == null){ // | nodoAct.getValor() != 0){
                codigos.put(nodoAct.getValor(), codificacion);
            }
            else{
                if (nodoAct.getHijoIzq() != null){

                    codificacion = codificacion + "1";
                    getCodigosRecursivo(codificacion, nodoAct.getHijoIzq(), codigos);
                    int l = codificacion.length();
                    codificacion = codificacion.substring(0, l-1);
                }
                if (nodoAct.getHijoDer() != null){
                    codificacion = codificacion + "0";
                    getCodigosRecursivo(codificacion, nodoAct.getHijoDer(), codigos);
                    int l = codificacion.length();
                    codificacion = codificacion.substring(0, l-1);

                }
            }
            return codigos;
        }


        public void setHijoDer(ArbolHuffman hijoDer) {
            this.hijoDer = hijoDer;
        }

        public void setHijoIzq(ArbolHuffman hijoIzq) {
            this.hijoIzq = hijoIzq;
        }

        public void setProbabilidad(double probabilidad) {
            this.probabilidad = probabilidad;
        }

        public ArbolHuffman getHijoDer() {
            return hijoDer;
        }

        public ArbolHuffman getHijoIzq() {
            return hijoIzq;
        }

        public int getValor() {
            return valor;
        }

        public double getProbabilidad() {
            return probabilidad;
        }

        @Override
        public int compareTo(ArbolHuffman o) {
            if (this.getProbabilidad() < o.getProbabilidad()){
                return -1;
            }
            else if(this.getProbabilidad() > o.getProbabilidad()){
                return 1;
            }
            return 0;
        }

}