
    public class HufTree{
        public byte uch;
        public int weight;
        public String code; //huffman code

        public int index;
        public int parent,lchild,rchild;

        /**
         * This methods used for print out the output;
         * @return "Character:" + (char)uch + "       Frequencies:" + weight + "         Encodings:" + code;
         */

        public String toString(){
            if((char)uch == ' '){
                return "Character:Space" + "   Frequencies:" + weight + "         Encodings:" + code;
            }
            return "Character:" + (char)uch + "       Frequencies:" + weight + "         Encodings:" + code;
        }
    }


    //temp node for counting frequencies

    class TmpNode implements Comparable<TmpNode>{
        public byte uch;
        public int weight;

        @Override
        public int compareTo(TmpNode arg0) {
            if(this.weight < arg0.weight)
                return 1;
            else if(this.weight > arg0.weight)
                return -1;
            return 0;
        }

}
