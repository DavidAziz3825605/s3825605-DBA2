import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Arrays;
import java.util.Date;
import java.io.File;
import java.io.RandomAccessFile;
import java.math.BigInteger;

public class btindex {
    public static void main(String[] args) throws IOException {
        long initialT = System.nanoTime();
        // put the heap size that was generated before

        int pageSize = getPageSize(args);
        String fileName= getFileName(args);
        System.out.println("fileeeeee"+fileName);
        String[] result = fileName.split(".");

        int acturlIndexSize = Integer.parseInt(result[1]);
        if (acturlIndexSize != pageSize)
        {
            System.out.println("page size must be  same as index page size"+ acturlIndexSize +" "+ pageSize);
            System.exit(0);
        }


        int recordSizeLength = 256;
        int recordSizeLengthOnIndexFile = 17;


        // add its path here
        File heapFile = new File("src/"+fileName);

        RandomAccessFile RandomAccessFile = new RandomAccessFile(heapFile, "r");

        Path path = Paths.get("src/"+fileName);
        byte[] bytes = Files.readAllBytes(path);
        long  count = bytes.length;
        int numberofpages = (int) (count/pageSize);
        int counter=0;
        int sizeOfNode = pageSize/recordSizeLengthOnIndexFile;
        BTree<Long, String> bpt;
        bpt = new BTree<>(128);

        for (int j=0;j< numberofpages;j++)
        {
            byte[] buf = new byte[pageSize];
            RandomAccessFile.seek(j*pageSize);
            RandomAccessFile.readFully(buf,0,pageSize);

            for (int i=0;i<pageSize;i += recordSizeLength)
            {
                byte[] PersonName = Arrays.copyOfRange(buf, i, i + 12);
                String name = new String(PersonName);
                byte[] birthdate = Arrays.copyOfRange(buf, i + 12, i + 20);
                long test = convertToLong(birthdate);
                Date bd = new Date(test);
                // System.out.println("record number:" + counter + " and Date is : " + bd);
                String n = "";
                if (j==0)
                {
                    n =   j+","+ counter;
                }
                else
                {
                    int record=  counter - ((counter / j) * j);
                    n =   j+","+ record;
                }

                bpt.put(test, n);
                //   System.out.println("record number:"+ counter);
                counter++;
            }
        }
        RandomAccessFile.close();
        System.out.println("Number of records indexed :    " + bpt.size());
        System.out.println("Number of index pages created:  " + bpt.height());
        System.out.println("The tree height:  " + bpt.height());
        DataOutputStream writer = new DataOutputStream(new FileOutputStream("heap." + pageSize));
        System.out.println(bpt.toString());
        System.out.println();
        long endingT = System.nanoTime();
        long totalTime = (endingT - initialT) / 1000000;
        System.out.println("File is created in: " + totalTime + "ms");
    }

    private static String getFileName(String[] args) {
        int pos = args.length - 1;
        try {
            return args[pos];
        } catch (Exception e) {
            System.out.println("Please Upload full file Path..."+ args.length);
            System.exit(0);
        }
        return "";
    }

    private static int getPageSize(String[] args) {
        int pos = args.length - 2;
        try {
            int x = Integer.parseInt(args[pos]);
            return x;
        } catch (Exception e) {
            System.out.println("page size must be a multiple of 2 eg: 1024,2048,4096..."+ args.length);
            System.exit(0);
        }
        return 0;
    }
    static long convertToLong(byte[] bytes)
    {
        // Using longValue() from BigInteger
        return new BigInteger(bytes).longValue();
    }
}