import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MagicDES {

    public static void main(String[] args) throws IOException {
        boolean[] key= new boolean[64];
        FileInputStream keyFile = new FileInputStream("src/pliki/kod.bin");
        byte[] keyBytes = keyFile.readNBytes(8);
        int i =0;
        for(byte B:keyBytes){

            boolean[] baj = intToBinaryArray(B,8);
            for (boolean x : baj) {
                if (x) {
                    key[i++]=true;
                }
                else {
                    key[i++]=false;
                }
            }
        }
        keyFile.close();

        FileInputStream file = new FileInputStream("src/pliki/tekst.bin");
        byte[] all = file.readAllBytes();
        file.close();

        ArrayList<Boolean> inputData = new ArrayList<Boolean>();

        FileOutputStream data = new FileOutputStream("src/pliki/data.txt");
        FileOutputStream outputBinary = new FileOutputStream("src/pliki/zaszyfrowane.bin");
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
                calculate64BitPortion(inputData,key,outputBinary);
                inputData = new ArrayList<Boolean>();

            }
        }
        if(inputData.size()!=0){
            calculate64BitPortion(inputData,key,outputBinary);
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

    public static void calculate64BitPortion (ArrayList<Boolean> inputData, boolean[]key, FileOutputStream outputBinary) throws IOException {
        boolean[] tab = new boolean[inputData.size()];
        for(int i = 0; i<tab.length; i++){
            tab[i]=inputData.get(i);
        }

        PlainText plainText = new PlainText(tab);
        SecretKey secretKey = new SecretKey(key);

        boolean[][]keyTabs = secretKey.getKeyTables();
        boolean[] tmp ;
        for(int i =0; i<16;  i++){
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
            int value = 0;
            for(int i = BYTE*8, x=7; i<(BYTE*8)+8; i++,x--){
                if(result[i]){
                    value += Math.pow(2,x);
                }
            }

            outputBinary.write(value);
        }



    }
}
