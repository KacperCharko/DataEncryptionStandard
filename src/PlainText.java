public class PlainText {
    private boolean[] plainText;
    private boolean[] leftPartTable;
    private boolean[] rightPartTable;
    private static int[] permutation = {58,50,42,34,26,18,10,2,60,52,44,36,28,20,12,4,62,54,46,38,30,22,14,6,64,56,48,40,32,24,16,8,57,49,41,33,25,17,9,1,59,51,43,35,27,19,11,3,61,53,45,37,29,21,13,5,63,55,47,39,31,23,15,7};


    public PlainText(boolean[] text){
        if(text.length!=64){
            text = standarizeKey(text);
        }
        this.plainText = text;

        this.plainText = permutate(permutation);

        this.leftPartTable = calculateTable(0,32);
        this.rightPartTable = calculateTable(32,64);
    }
    private boolean[] permutate(int[] permutation) {

        boolean[] tab = new boolean[permutation.length];
        for(int i = 0; i < permutation.length; i++){
            tab[i]=plainText[permutation[i]-1];
        }
        return tab;
    }
    private boolean[] standarizeKey(boolean[] key){

        boolean[] k = new boolean[64];

        for(int i = 0; i<key.length && i<64; i++) {
            k[i] = key[i];
        }

        for(int i=key.length; i<64; i++){
            k[i]=false;
        }
        return k;
    }
    private boolean[] calculateTable(int begin, int end){
        boolean[] tab = new boolean[end-begin];

        for(int i = 0; i < tab.length; i++){
            System.out.println(i);
            tab[i]=plainText[i+begin];
        }
        return tab;
    }
    private void changeTablesSide(){
       boolean[] tab= leftPartTable;
       leftPartTable = rightPartTable;
       rightPartTable = tab;
    }
}
