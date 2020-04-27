public class SecretKey {

    public boolean[] secretKey;
    public boolean[] leftPartTable;
    public boolean[] rightPartTable;

    private static int[] leftPartIndexes = {57,49,41,33,25,17,9,1,58,50,42,34,26,18,10,2,59,51,43,35,27,19,11,3,60,52,44,36};
    private static int[] rightPartIndexes = {63,55,47,39,31,23,15,7,62,54,46,38,30,22,14,6,61,53,45,37,29,21,13,5,28,20,12,4};
    private static int[] pc2permutation = {14,17,11,24,1,5,3,28,15,6,21,10,23,19,12,4,26,8,16,7,27,20,13,2,41,52,31,37,47,55,30,40,51,45,33,48,44,49,39,56,34,53,46,42,50,36,29,32};
    private  static int[] shiftCount = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
    public SecretKey(boolean[] key){
        if(key.length!=64){
            this.secretKey = standarizeKey(key);
        }
        else
            this.secretKey=key;

        this.leftPartTable = calculateTable(leftPartIndexes);
        this.rightPartTable = calculateTable(rightPartIndexes);
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

    private boolean[] calculateTable(int[] indexesToPickUp){
        boolean[] tab = new boolean[indexesToPickUp.length];
           for(int i = 0; i < indexesToPickUp.length; i++){
               tab[i]=secretKey[indexesToPickUp[i]];
           }
           return tab;
    }

    public void shiftTabLeft(int count){
        for(int shitfCount = 0; shitfCount<count; shitfCount++) {
            this.rightPartTable[0] = this.rightPartTable[this.rightPartTable.length - 1];
            this.leftPartTable[0] = this.leftPartTable[this.leftPartTable.length - 1];
            for (int i = this.rightPartTable.length - 1; i > 0; i--) {
                this.rightPartTable[i] = this.rightPartTable[i - 1];
                this.leftPartTable[i] = this.leftPartTable[i-1];
            }
        }
    }
    public void shiftTableRight(int count){
        for(int shitfCount = 0; shitfCount<count; shitfCount++) {
            this.rightPartTable[rightPartTable.length-1] = rightPartTable[0];
            leftPartTable[leftPartTable.length-1] = leftPartTable[0];
            for (int i = 0; i < leftPartTable.length-1; i++) {
                rightPartTable[i] = rightPartTable[i + 1];
                leftPartTable[i] = leftPartTable[i+1];
            }
        }
    }
    public boolean[] shiftTable(boolean[] tab, int count){
        //boolean[] t = new boolean[64];
        for(int shitfCount = 0; shitfCount<count; shitfCount++) {
            tab[tab.length-1] = tab[0];
            for (int i = 0; i < tab.length-1; i++) {
                tab[i] = tab[i + 1];
            }
        }
        return tab;
    }

    public boolean[][] getKeyTables (){
        boolean[][] tabs = new boolean[16][64];
        boolean[] left = this.leftPartTable;
        boolean[] right = this.rightPartTable;
        for (int i =0; i<16; i++){
            left = shiftTable(left,shiftCount[i]);
            right = shiftTable(right,shiftCount[i]);
            tabs[i] = getpc2permutatedTable(left,right);
        }
        return  tabs;
    }
    public boolean[] getpc2permutatedTable (boolean[] l, boolean[] r){

        boolean[] table = new boolean[l.length+r.length];
        for (int i = 0; i <l.length; i++){
            table[i] = l[i];
            table[i+r.length]=r[i];
        }
        boolean[] tab = new boolean[pc2permutation.length];
        for (int i = 0; i< tab.length; i++){
            tab[i] = table[pc2permutation[i]-1];
        }

        return tab;
    }


}
