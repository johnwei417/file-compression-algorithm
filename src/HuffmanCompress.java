/**
 * @author Honglin Wei
 */


import java.util.*;
import java.io.*;

public class HuffmanCompress {
    private PriorityQueue<HufTree> queue = null;

    /**
     * "compress" method used for compress the original file
     * @param inputFile which is input file
     * @param outputFile which is the file after compress;
     */
    public void compress(File inputFile, File outputFile) {

        Compare cmp = new Compare();
        queue = new PriorityQueue<HufTree>(12, cmp);
        HashMap<Byte, String> map = new HashMap<Byte, String>();

        //number of char types in the file
        int i, char_kinds = 0;
        int char_temp, file_len = 0;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        //number of node in the huffman tree
        int node_num;
        HufTree[] huf_tree = null;
        String code_buf = null;

        //array used for storing temp char frequencies;
        TmpNode[] tmp_nodes = new TmpNode[256];

        for (i = 0; i < 256; ++i) {
            tmp_nodes[i] = new TmpNode();
            tmp_nodes[i].weight = 0;
            tmp_nodes[i].uch = (byte) i;
        }
        try {
            fis = new FileInputStream(inputFile);
            fos = new FileOutputStream(outputFile);
            oos = new ObjectOutputStream(fos);

            // calculate file length and char frequencies;
            while ((char_temp = fis.read()) != -1) {
                ++tmp_nodes[char_temp].weight;
                ++file_len;
            }
            fis.close();
            Arrays.sort(tmp_nodes);

            for (i = 0; i < 256; ++i) {
                if (tmp_nodes[i].weight == 0)
                    break;
            }
            char_kinds = i;


            if (char_kinds == 1) {
                oos.writeInt(char_kinds);
                oos.writeByte(tmp_nodes[0].uch);
                oos.writeInt(tmp_nodes[0].weight);
                //for more than one char type;
            } else {
                node_num = 2 * char_kinds - 1;
                huf_tree = new HufTree[node_num];
                for (i = 0; i < char_kinds; ++i) {
                    huf_tree[i] = new HufTree();
                    huf_tree[i].uch = tmp_nodes[i].uch;
                    huf_tree[i].weight = tmp_nodes[i].weight;
                    huf_tree[i].parent = 0;

                    huf_tree[i].index = i;
                    queue.add(huf_tree[i]);
                }
                tmp_nodes = null;

                for (; i < node_num; ++i) {
                    huf_tree[i] = new HufTree();
                    huf_tree[i].parent = 0;
                }


                //create huffman tree;
                createTree(huf_tree, char_kinds, node_num, queue);
                //generate huffman code here;

                hufCode(huf_tree, char_kinds);
                // write type of char
                oos.writeInt(char_kinds);
                for (i = 0; i < char_kinds; ++i) {
                    oos.writeByte(huf_tree[i].uch);
                    oos.writeInt(huf_tree[i].weight);

                    map.put(huf_tree[i].uch, huf_tree[i].code);
                }
                oos.writeInt(file_len);
                fis = new FileInputStream(inputFile);
                code_buf = "";

                // save huffman code which have covert into binary into the bin file
                while ((char_temp = fis.read()) != -1) {

                    code_buf += map.get((byte) char_temp);

                    while (code_buf.length() >= 8) {
                        char_temp = 0;
                        for (i = 0; i < 8; ++i) {
                            char_temp <<= 1;
                            if (code_buf.charAt(i) == '1')
                                char_temp |= 1;
                        }
                        oos.writeByte((byte) char_temp);
                        code_buf = code_buf.substring(8);
                    }
                }

                //if the length of last code is not 8 bit, add 0 to fill up;
                if (code_buf.length() > 0) {
                    char_temp = 0;
                    for (i = 0; i < code_buf.length(); ++i) {
                        char_temp <<= 1;
                        if (code_buf.charAt(i) == '1')
                            char_temp |= 1;
                    }
                    char_temp <<= (8 - code_buf.length());
                    oos.writeByte((byte) char_temp);
                }
            }
            for(int j=0; j< huf_tree.length; j++ ){
                if(huf_tree[j].uch !=0 && huf_tree[j].uch != -1 && huf_tree[j].uch != -2)
                    System.out.println(huf_tree[j].toString());
            }
            System.out.println();
            oos.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //decompress the file

    /**
     * this method used for decompressing the file into "decompressed_file.txt"
     * @param inputFile which is the compressed bin file
     * @param outputFile which is the "decompressed_file.txt"
     */
        public void extract(File inputFile, File outputFile){
            Compare cmp = new Compare();
            queue = new PriorityQueue<HufTree>(12, cmp);

            int i;
            int file_len = 0;
            int writen_len = 0;
            FileInputStream fis = null;
            FileOutputStream fos = null;
            ObjectInputStream ois = null;

            int char_kinds = 0;
            int node_num;
            HufTree[] huf_tree = null;
            byte code_temp;
            int root;
            try {
                fis = new FileInputStream(inputFile);
                ois = new ObjectInputStream(fis);
                fos = new FileOutputStream(outputFile);

                char_kinds = ois.readInt();

                //for case which has only one byte;
                if (char_kinds == 1) {
                    code_temp = ois.readByte();
                    file_len = ois.readInt();
                    while ((file_len--) != 0) {
                        fos.write(code_temp);
                    }
                    //for cases which has more than one byte;
                } else {
                    node_num = 2 * char_kinds - 1;  //calculate the number of node of huffman trees
                    huf_tree = new HufTree[node_num];
                    for (i = 0; i < char_kinds; ++i) {
                        huf_tree[i] = new HufTree();
                        huf_tree[i].uch = ois.readByte();
                        huf_tree[i].weight = ois.readInt();
                        huf_tree[i].parent = 0;

                        huf_tree[i].index = i;
                        queue.add(huf_tree[i]);
                    }
                    for (; i < node_num; ++i) {
                        huf_tree[i] = new HufTree();
                        huf_tree[i].parent = 0;
                    }
                    createTree(huf_tree, char_kinds, node_num, queue);

                    file_len = ois.readInt();
                    root = node_num - 1;
                    while (true) {
                        code_temp = ois.readByte();
                        for (i = 0; i < 8; ++i) {
                            if ((code_temp & 128) == 128) {
                                root = huf_tree[root].rchild;
                            } else {
                                root = huf_tree[root].lchild;
                            }

                            if (root < char_kinds) {
                                fos.write(huf_tree[root].uch);
                                ++writen_len;
                                if (writen_len == file_len) break;
                                root = node_num - 1; //recover next node of the root
                            }
                            code_temp <<= 1;
                        }

                        //add 0 if the last digit of huffman code isn't enough for 8 bit
                        //the o added will be distribute to other node properly when compressing,
                        // so once adding 0 finished, the length of compressed file is still equal to the file before;
                        if (writen_len == file_len) break;
                    }
                }
                fis.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    /**
     * this method used for building the Huffman tree
     * @param huf_tree array of HufTree
     * @param char_kinds which is the types of characters;
     * @param node_num which is node number of the huffman tree
     * @param queue which the priorityqueue for huftree;
     */
    //build huffman tree
    public void createTree(HufTree[] huf_tree, int char_kinds, int node_num,PriorityQueue<HufTree> queue){
        int i;
        int[] arr = new int[2];
        for(i = char_kinds; i < node_num; ++i){
            arr[0] = queue.poll().index;
            arr[1] = queue.poll().index;
            huf_tree[arr[0]].parent = huf_tree[arr[1]].parent = i;
            huf_tree[i].lchild = arr[1];
            huf_tree[i].rchild = arr[0];
            huf_tree[i].weight = huf_tree[arr[0]].weight + huf_tree[arr[1]].weight;

            huf_tree[i].index = i;
            queue.add(huf_tree[i]);

        }


    }

    /**
     * this method used for getting the huffman code from the huffman tree
     * @param huf_tree huffman tree
     * @param char_kinds types of characters
     */
    //get huffman code
    public void hufCode(HufTree[] huf_tree, int char_kinds) {
        int i;
        int cur, next;

        for (i = 0; i < char_kinds; ++i) {
            String code_tmp = "";
            for (cur = i, next = huf_tree[i].parent; next != 0; cur = next, next = huf_tree[next].parent) {
                if (huf_tree[next].lchild == cur)
                    code_tmp += "0";
                else
                    code_tmp += "1";
            }
            huf_tree[i].code = (new StringBuilder(code_tmp)).reverse().toString();
        }

    }
}
