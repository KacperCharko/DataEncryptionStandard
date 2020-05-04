public class SecretKey {

    public boolean[] secretKey;
    public boolean[] leftPartTable;
    public boolean[] rightPartTable;

    private static int[] leftPartIndexes = {57,49,41,33,25,17,9,1,58,50,42,34,26,18,10,2,59,51,43,35,27,19,11,3,60,52,44,36};
    private static int[] rightPartIndexes = {63,55,47,39,31,23,15,7,62,54,46,38,30,22,14,6,61,53,45,37,29,21,13,5,28,20,12,4};
    private static int[] pc2permutation = { 14,17,11,24,1,5,3,28,15,6,21,10,
            23,19,12,4,26,8,16,7,27,20,13,2,
            41,52,31,37,47,55,30,40,51,45,33,48,
            44,49,39,56,34,53,46,42,50,36,29,32 };
    private  static int[] shiftCount = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
    public SecretKey(boolean[] key){
        for(int i = 0; i<key.length; i++){
            if(key[i])
                System.out.print(1);
            else
                System.out.print(0);
        }

        if(key.length!=64){
            this.secretKey = standarizeKey(key);
        }
        else
            this.secretKey=key;
        System.out.println("left tab");
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
               tab[i]=secretKey[indexesToPickUp[i]-1];
               //System.out.println("index= "+ indexesToPickUp[i] + );
           }
           return tab;
    }

    public boolean[] shiftTable(boolean[] tab, int count){


        int shitfCount=0;

        for(shitfCount = 0; shitfCount<count; shitfCount++) {
            boolean pom = tab[0];
            for (int i = 1; i<= tab.length-1; i++) {
                tab[i-1]=tab[i];
            }
            tab[tab.length-1]=pom;


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
