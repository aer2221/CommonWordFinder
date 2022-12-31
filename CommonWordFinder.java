import java.io.*;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Class that finds the n most common words in a document
 * Relies on BST, AVL, Hashmaps, and merge sort
 * @author Anna Reis uni aer2221
 * @version 1.0 December 16, 2022
 */

public class CommonWordFinder{

    /**
     * Merge sort as given by Dr. B, implemented to compare based on numerical frequency of words as given in map
     * I use merge sort because it preserves the stability of words already organized lexicographically
     * @param array of words already sorted lexicographically
     * @param scratch array to keep track of sorting in progress
     * @param low lowest index in array
     * @param high highest index in array
     * @param map data structure containing words and their frequencies
     */
    private static void mergesortHelper(String[] array, String[] scratch,
                                           int low, int high, MyMap<String,Integer> map) {
        if (low < high) {
            int mid = low + (high - low) / 2;
            mergesortHelper(array, scratch, low, mid,map);
            mergesortHelper(array, scratch, mid + 1, high,map);

            int i = low, j = mid + 1;
            for (int k = low; k <= high; k++) {
                //compare based on numerical frequencies of the words
                if (i <= mid && (j > high || map.get(array[i]) >= map.get(array[j]))){
                    scratch[k] = array[i++];
                } else {
                    scratch[k]= array[j++];
                }
            }
            for (int k = low; k <= high; k++) {
                array[k]=scratch[k];
            }
        }
    }


    /**
     * Typical merge sort modified to deal with array of strings
     * @param array of words to be sorted by frequency of word
     * @param map containing words and their numerical frequency
     */
    public static void mergesort(String[] array, MyMap<String,Integer> map) {
        String[] scratch = new String[array.length];
        mergesortHelper(array, scratch, 0, array.length - 1,map);
    }


    /**
     * Method to sort words by numerical frequency and lexicographically
     * @param map data structure in which words are stored
     * @param size size of data structure
     * @param limit number of words to be displayed to user
     */
    private static void sort(MyMap<String,Integer> map, int size,int limit){
        //create array to store all unique words in the document
        String[] output= new String[size];
        //integer to keep track of place in array
        int count=0;

        //parse data structure for all words in document, add each to array
        //source utilized to understand iterator:
        //https://www.w3schools.com/java/java_iterator.asp
        Iterator<Entry<String,Integer>> it= map.iterator();
        while (it.hasNext()){
            Entry i=it.next();
            output[count]= String.valueOf(i.key);
            count++;
        }

        //sort strings lexicographically
        Arrays.sort(output);
        //sort strings again based on their frequency utilizing mergesort
        mergesort(output,map);
        //once array is sorted, call function for output formatting
        outputFormat(map,output,size,limit);
    }


    /**
     * Method to neatly output results to user
     * @param map data structure in which words are stored
     * @param output array with words sorted by frequency and lexicographically
     * @param size size of data structure
     * @param limit maximum number of words to be displayed
     */
    private static void outputFormat(MyMap<String,Integer> map,String[] output, int size, int limit){
        System.out.println("Total unique words: " + size);
        //if limit exceeds total # of words in file, display all words
        if (limit>size){
            limit=size;
        }
        //keep track of the longest string length for formatting
        int maxStringLength=0;
        String maxString="";
        //keep track of maximum number of digits for formatting
        String limitString=String.valueOf(limit);
        int limitDigits=limitString.length();
        //grab newline character specific to system
        String newline=System.lineSeparator();

        //finds the longest string length among words to be displayed to user
        for (int i=0;i<limit;i++){
            if (String.valueOf(output[i]).length()>maxStringLength){
                maxStringLength=String.valueOf(output[i]).length();
                maxString=String.valueOf(output[i]);
            }
        }

        //final output to user
        for (int i=0;i<limit;i++){
            //determine number of spaces that need to be added to digit and string for formatting
            //+1 because i is always 1 less the number printed out
            int digitDifference=(limitDigits-(String.valueOf(i+1)).length());
            int stringDifference=(maxStringLength-(String.valueOf(output[i]).length()));
            if (digitDifference>0){
                System.out.printf("%" + digitDifference+ "s", " ");
            }
            System.out.print((i+1)+". " + output[i]);
            if (stringDifference>0){
                System.out.printf("%-" + stringDifference+ "s", " ");
            }
            System.out.print(" " + map.get(output[i]));
            System.out.print(newline);
        }
    }


    /**
     * Main method to take in user arguments, put words into a data structure, and call helper functions
     * @param args containing text file, data structure, and optional limit for number of words to be displayed
     */
    public static void main(String[] args){
        //if incorrect number of arguments given
        if (args.length!=2 && args.length !=3){
            System.err.println("Usage: java CommonWordFinder " +
                    "<filename> <bst|avl|hash> [limit]");
            System.exit(1);
        }

        try{
            //automatically set limit to 10, will be reset if a diff number is given
            int limit=10;
            //automatically make a BST map, reset otherwise
            MyMap<String,Integer> map= new BSTMap<>();

            //invalid file name provided
            File f= new File(args[0]);
            if (!(f.exists())){
                throw new FileNotFoundException();
            }
            //invalid data structure provided
            if ((!(args[1]).equals("bst")) && (!(args[1].equals("avl"))
            ) && (!(args[1].equals("hash")))){
                throw new IllegalArgumentException();
            }
            //if limit is given
            if (args.length>2) {
                //do not allow non-positive integers
                if (!(Integer.parseInt(args[2]) > 0)) {
                    throw new IllegalArgumentException();
                }
                else{
                    limit=Integer.parseInt(args[2]);
                }
            }
            //set map to AVL if requested
            if (args[1].equals("avl")){
                map=new AVLTreeMap<>();
            }
            //set map to hash if requested
            if (args[1].equals("hash")) {
                map = new MyHashMap<>();
            }

            //parsing file for unique words and adding to data structure
            //sources utilized to help understand buffered readers:
            //https://docs.oracle.com/javase/10/docs/api/java/io/BufferedReader.html#read()
            //https://beginnersbook.com/2014/01/how-to-read-file-in-java-using-bufferedreader/
            BufferedReader reader= new BufferedReader(new FileReader(args[0]));
            int num;
            char ch;
            StringBuilder builder= new StringBuilder();
            String endofline=System.lineSeparator();
            while ((num=reader.read())!= -1){
                ch=(char)num;
                //whitespace signals end of word
                if (Character.isWhitespace(ch) || ch=='\n' || ch=='\r'){
                    String currString= builder.toString();
                    //if word is already in map
                    if (map.get(currString) != null){
                        int value=map.get(currString);
                        map.put(currString,++value);
                    }
                    else{
                        //if word is not already in map
                        //if statement solves issue of white space being added
                        if (!(currString.equals(""))){
                            map.put(currString,1);
                        }
                    }
                    //clear string once you have added a word to data structure
                    builder.delete(0,builder.length());
                }
                //if not whitespace, continue to build word char by char
                else{
                    //legal characters
                    if (Character.isLetter(ch) || ch=='\'' || ch=='-' &&
                    builder.length()!=0){
                        if (!(ch == ' ')){
                            builder.append(Character.toLowerCase(ch));
                        }
                    }
                }
            }
            //deals with edge case of word at end of file with no space char
            //if stringbuilder is not empty after parsing file, put it into map
            if (builder.length()!=0){
                String currString= builder.toString();
                if (map.get(currString) != null){
                    int value=map.get(currString);
                    map.put(currString,++value);
                }
                else{
                    //if word is not already in map
                    //if statement solves issue of white space being added
                    if (!(currString.equals(""))){
                        map.put(currString,1);
                    }
                }
            }

            //once data structure has been created, move on to sorting and formatting
            sort(map,map.size(),limit);
        }
        //invalid file name
        catch (FileNotFoundException e){
            System.err.println("Error: Cannot open file "+ args[0] +" for input.");
            System.exit(1);
        }
        catch (IllegalArgumentException a){
            //invalid data structure
            if ((!(args[1]).equals("bst")) && (!(args[1].equals("avl"))
            ) && (!(args[1].equals("hash")))){
                System.err.println("Error: Invalid data structure " + args[1]+
                        " received.");
                System.exit(1);
            }
            //non-positive limit provided
            if (!(Integer.parseInt(args[2]) > 0 )){
                System.err.println("Error: Invalid limit " + args[2] +
                        " received.");
                System.exit(1);
            }
        }
        //error during file reading
        catch (IOException i){
            System.err.println("Error: An I/O error occurred reading " +
                    args[0]+ ".");
            System.exit(1);
        }
    }
}