import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MagicDesDeciper {

    private  static int[] shiftCount = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};

    public static void main(String[] args) throws IOException {

        int bajt;
        String word = null;
        //boolean[] key = {false,false,true,true,false,false,false,true,true,false,false,false,true,false,false,false,true,false,false,false,false,true,true,false,false,true,false,true,false,false,false,false,false,true,false,false,false,true,false,false,true,false,false,false,false,false,false,true,false,true,false,false,false,true,false,true,false,false,false,false,true,false,false,false};
        boolean[] key = {false,false,true,true,false,false,false,true};
        FileInputStream file = new FileInputStream("src/pliki/essa.bin");
        byte[] all = file.readAllBytes();
        file.close();

        ArrayList<Boolean> inputData = new ArrayList<Boolean>();
        int counter = 0;
        FileOutputStream data = new FileOutputStream("src/pliki/data1.txt");
        FileOutputStream outputBinary = new FileOutputStream("src/pliki/decoded.bin");
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

                boolean[][]keyTabs = secretKey.getKeyTables();
                boolean[] tmp ;
                for(int i =15; i>=0;  i--){
                    tmp=plainText.rightPartTable;
                    Feistel feistel = new Feistel(plainText.rightPartTable,keyTabs[i]);
                    plainText.rightPartTable = feistel.calculate();
                    for(int x =0; x<plainText.rightPartTable.length; x++){
                        plainText.leftPartTable[x] = plainText.leftPartTable[x]^plainText.rightPartTable[x];
                    }
                    plainText.rightPartTable=tmp;
                    plainText.changeTablesSide();
                }
                    plainText.changeTablesSide();
                boolean[] result = plainText.endPerm(PlainText.endPermutation);


                for(int BYTE = 0; BYTE < (result.length / 8); BYTE++){
                    byte value = 0;
                    for(int i = BYTE*8; i<(BYTE*8)+8; i++){
                        value <<= 1;
                        if(result[i]){
                            value |= 1;
                        }
                    }
                    outputBinary.write(value);
                }



                inputData = new ArrayList<Boolean>();

            }
        }
        outputBinary.close();
        data.close();


    }

    private static boolean[] intToBinaryArray(int num, int outputSize){
        final boolean[] Return = new boolean[outputSize];
        for(int i = 0; i < outputSize; i++){
            Return[outputSize - 1 - i] = (1 << i & num) != 0;
        }
        return Return;
    }

    private boolean[] xorTables (boolean[] tab1, boolean[] tab2){
        boolean[] tab = new boolean[tab1.length];

        for(int i =0; i<tab1.length; i++){
            tab[i]=tab1[i] ^ tab2[i];
        }

        return  tab;
    }
}
