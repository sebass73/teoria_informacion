import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodificacionBits {
        private static int bufferLength = 8;
        // private String secuence;

        private static List<Byte> encodeSecuence (String secuence){
            List<Byte> result = new ArrayList<Byte>();
            byte buffer = 0;
            int bufferPos = 0;
            int i = 0;
            while (i < secuence.length()){
                buffer = (byte) (buffer << 1);
                bufferPos++;
                if (secuence.charAt(i) == '1'){
                    buffer = (byte) (buffer | 1);
                }
                if (bufferPos == bufferLength){
                    result.add(buffer);
                    buffer = 0;
                    bufferPos = 0;
                }
                i++;
            }

            if ((bufferPos < bufferLength) && (bufferPos != 0)){
                buffer = (byte) (buffer << (bufferLength - bufferPos));
                result.add(buffer);
            }

            return result;
        }

        private byte[] convertListtoArray(String secuence) {
            List<Byte> byteList = encodeSecuence(secuence);
            byte[] byteArray = new byte[byteList.size()];
            for (int i = 0; i < byteList.size(); i++){
                byteArray[i] = byteList.get(i);
            }
            return byteArray;
        }

        //public void writeOutputFile(String secuence, HashMap<Integer, String> cabecera, String outputFilePath){ ???
        public void writeOutputFile(String secuence, String outputFilePath){

            try {
                byte[] byteArray = convertListtoArray(secuence);
                FileOutputStream fos = new FileOutputStream(outputFilePath);
                //fos.write(cabecera); ???????
                fos.write(byteArray);
                fos.close();

            } catch (IOException e){
                e.printStackTrace();
            }
        }

}
