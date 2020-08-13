import java.io.*;
import java.util.*;
//test
public class Test {

    public static void main(String[] args) {

        HuffmanCompress sample = new HuffmanCompress();
        File inputFile = new File("input_pic.jpg");
        File outputFile = new File("compressed_file.zip");
        System.out.println("=====================================");
        System.out.println("            Huffman Coding           ");
        System.out.println("=====================================");
        System.out.println();
        long startTime = System.currentTimeMillis();
        sample.compress(inputFile, outputFile);
        long endTime = System.currentTimeMillis();
        System.out.println("Time cost for compression: "+ (endTime - startTime)+" ms");
        File inputFile1 = new File("compressed_file.zip");
        File outputFile1 = new File("output_pic.jpg");

        long startTime1 = System.currentTimeMillis();
        sample.extract(inputFile1, outputFile1);
        long endTime1 = System.currentTimeMillis();
        System.out.println("Time cost for decompression: "+ (endTime1 - startTime1)+" ms");
    }
}