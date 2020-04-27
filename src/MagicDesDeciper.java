import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MagicDesDeciper {


    public static void main(String[] args) throws IOException {

        int bajt;
        String word = null;
        boolean[] key= new boolean[64];
        FileInputStream keyFile = new FileInputStream("src/pliki/kod.bin");
        byte[] keyBytes = keyFile.readNBytes(8);
        for(byte B:keyBytes){
            int i =0;
            boolean[] baj = intToBinaryArray(B,8);
            for (boolean x : baj) {
                if (x) {
                    key[i++]=true;
                }
                else {
                    key[i]=false;
                }
            }
        }
        keyFile.close();

        FileInputStream file = new FileInputStream("src/pliki/zaszyfrowane.bin");
        byte[] all = file.readAllBytes();
        file.close();

        ArrayList<Boolean> inputData = new ArrayList<Boolean>();

        FileOutputStream data = new FileOutputStream("src/pliki/data1.txt");
        FileOutputStream outputBinary = new FileOutputStream("src/pliki/odkodowane.bin");
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

    }
}
