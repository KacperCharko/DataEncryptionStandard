public class PlainText {
    public boolean[] plainText;
    public boolean[] leftPartTable;
    public boolean[] rightPartTable;
    public static int[] permutation = { 58,50,42,34,26,18,10,2,60,52,44,36,28,20,12,4,
            62,54,46,38,30,22,14,6,64,56,48,40,32,24,16,8,
            57,49,41,33,25,17,9,1,59,51,43,35,27,19,11,3,
            61,53,45,37,29,21,13,5,63,55,47,39,31,23,15,7 };
    public static int[] endPermutation = { 40,8,48,16,56,24,64,32,39,7,47,15,55,23,63,31,
            38,6,46,14,54,22,62,30,37,5,45,13,53,21,61,29,
            36,4,44,12,52,20,60,28,35,3,43,11,51,19,59,27,
            34,2,42,10,50,18,58,26,33,1,41,9,49,17,57,25 };
    public PlainText(boolean[] text){

        if(text.length!=64){
            text = standarizeKey(text);
        }
        this.plainText = text;

        this.plainText = permutate(permutation);

        this.leftPartTable = calculateTable(0,32);
        this.rightPartTable = calculateTable(32,64);
    }
    public PlainText(boolean[] text, int x){
        if(text.length!=64){
            text = standarizeKey(text);
        }
        this.plainText = text;
        this.plainText = permutate(endPermutation);


        this.leftPartTable = calculateTable(0,32);
        this.rightPartTable = calculateTable(32,64);
    }
    public boolean[] permutate(int[] permutation) {

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

            tab[i]=plainText[i+begin];
        }
        return tab;
    }
    public void changeTablesSide(){
       boolean[] tab= leftPartTable;
       leftPartTable = rightPartTable;
       rightPartTable = tab;
    }

    public boolean[] endPerm(int[] perm) {

        boolean[] tab = new boolean[64];

        for (int i = 0; i <leftPartTable.length; i++){
            tab[i] = leftPartTable[i];
            tab[i+32]=rightPartTable[i];
        }
        boolean[] finall = new boolean[tab.length];
        for(int i = 0; i< finall.length; i++){
            finall[i] = tab[perm[i]-1];
        }
        return finall;

    }

}
