import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MagicDES {

    public static void main(String[] args) throws IOException {

        int bajt;
        String word = null;
        boolean[] key = {true,false,true,true,false,false,false,true};
        FileInputStream file = new FileInputStream("src/pliki/test3.bin");
        byte[] all = file.readAllBytes();
        file.close();

        ArrayList<Boolean> inputData = new ArrayList<Boolean>();
        int counter = 0;
        FileOutputStream data = new FileOutputStream("src/pliki/data.txt");
        for(byte B : all){

            boolean[] baj = intToBinaryArray(B,8);
            for (boolean x : baj) {
                if (x) {
                    data.write(49);
                    inputData.add(true);
                }
                else {
                    data.write(48);
                    inputData.add(false);
                }
            }
            if(inputData.size()==64)
            {
                boolean[] tab = new boolean[inputData.size()];
                for(int i = 0; i<64; i++){
                    tab[i]=inputData.get(i);
                }

                    PlainText plainText = new PlainText(tab);
                    SecretKey secretKey = new SecretKey(key);
//                    boolean[] tabs = secretKey.leftPartTable;
//                    for(int i =0; i<tabs.length; i++){
//                        System.out.println(tabs[i]);
//                    }



                inputData = new ArrayList<Boolean>();

            }
        }
        data.close();


    }

    private static boolean[] intToBinaryArray(int num, int outputSize){
        final boolean[] Return = new boolean[outputSize];
        for(int i = 0; i < outputSize; i++){
            Return[outputSize - 1 - i] = (1 << i & num) != 0;
        }
        return Return;
    }

}
