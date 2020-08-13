import java.io.*;
import java.util.*;
//test
public class Test {

    public static void main(String[] args) {
 /**

        final String alphabet = "0123456789ABCDE ";
        final int N = alphabet.length();
        int charlen = 0;
        Scanner input = new Scanner(System.in);
        System.out.println("==================\n" +
                "Huffman Code –\n" +
                "==================");
        System.out.println("> Enter the number of characters: ");
        charlen = input.nextInt();
        System.out.println("> Writing to file... [Done]\n" +
                "Compressing “file.txt” into “compressed_file.bin”... [Done]\n" +
                "Decompressing “compressed_file.bin” into “decompressed_file.txt”... [Done]\n" +
                "\nResults...\n");

        Random r = new Random();
        String s = "";
        for (int i = 0; i < charlen; i++) {
            s += alphabet.charAt(r.nextInt(N));
        }
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("file.txt"), "utf-8"));
            writer.write(s);
        } catch (IOException ex) {
            // Report
        } finally {
            try {writer.close();
            } catch (Exception ex) {

            }
        }
        System.out.println();
  **/
        HuffmanCompress sample = new HuffmanCompress();
        File inputFile = new File("input_pic.jpg");
        File outputFile = new File("compressed_file.zip");
        //System.out.println("Total Characters: " + charlen);
        System.out.println();
        //long startTime = System.currentTimeMillis();
        sample.compress(inputFile, outputFile);
        //long endTime = System.currentTimeMillis();


       // if(charlen >=100)
        //     System.out.println("Time: " + (endTime - startTime)/(charlen/100) + " ms per 100 characters to compress a file. " );
        //else
       //     System.out.println("Time: " + (endTime - startTime)*(100/charlen) + " ms per 100 characters to compress a file. " );

        File inputFile1 = new File("compressed_file.zip");
        File outputFile1 = new File("output_pic.jpg");

        //long startTime1 = System.currentTimeMillis();
        sample.extract(inputFile1, outputFile1);
        //long endTime1 = System.currentTimeMillis();
        //if(charlen >=100)
          //  System.out.println("Time: " + (endTime1 - startTime1)/(charlen/100) + " ms per 100 characters to decompress a file. " );
        //else
          //  System.out.println("Time: " + (endTime1 - startTime1)*(100/charlen) + " ms per 100 characters to decompress a file. " );


    }
}