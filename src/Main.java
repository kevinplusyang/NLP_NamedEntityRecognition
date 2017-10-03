import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringJoiner;

public class Main {

    static class TrieNode {

        HashMap<String, TrieNode> children;

        String content;
        boolean isWord;
        String type;

        public TrieNode(String name) {
            children = new HashMap<String, TrieNode>();
            content = name;
            isWord = false;
            type = "";
        }
    }
    static TrieNode root = new TrieNode("");
    static ArrayList<String> testSet = new ArrayList<>();
    static HashMap<String, ArrayList<String>> map = new HashMap<>();

    public static void main(String[] args) {



        try {
            File file = new File("./src/train.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            int i = 0;
            String line1 = "";
            String line2 = "";
            String line3 = "";
            while ((line = bufferedReader.readLine()) != null) {

                if (i == 0) {
                    line1 = line.toString();
                    i++;
                } else if (i == 1) {
                    line2 = line.toString();
                    i++;
                } else if (i == 2) {
                    line3 = line.toString();
                    i++;
                }

                if (i == 3) {
                    String[] line1Array = line1.split("\t");
                    String[] line2Array = line2.split("\t");
                    String[] line3Array = line3.split("\t");

                    handleLine(line1Array, line2Array, line3Array);

                    i = 0;
                    line1 = "";
                    line2 = "";
                    line3 = "";
                }


            }
            fileReader.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            File file = new File("./src/test.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            int i = 0;
            String line1 = "";
            while ((line = bufferedReader.readLine()) != null) {


                for (String tempWord : line.split("\t")) {
                    testSet.add(tempWord);
                }

                line = bufferedReader.readLine();
                line = bufferedReader.readLine();



            }
            fileReader.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        map.put("PER", new ArrayList<>());
        map.put("LOC", new ArrayList<>());
        map.put("ORG", new ArrayList<>());
        map.put("MISC", new ArrayList<>());


        search();


        System.out.println("PER Result:");
        for (String result : map.get("PER")) {
            System.out.print(result);
            System.out.print(" ");
        }

        System.out.println("");
        System.out.println("PER LOC:");
        
        for (String result : map.get("LOC")) {
            System.out.print(result);
            System.out.print(" ");
        }

        System.out.println("");
        System.out.println("PER ORG:");


        for (String result : map.get("ORG")) {
            System.out.print(result);
            System.out.print(" ");
        }

        System.out.println("");
        System.out.println("PER MISC:");

        for (String result : map.get("MISC")) {
            System.out.print(result);
            System.out.print(" ");
        }


    }

    private static void handleLine(String[] line1Array, String[] line2Array, String[] line3Array) {
        int i = 0;
        int start = 0;
        int end = 0;
        String wordType = "";
        while (i < line1Array.length) {
            if (!line3Array[i].equals("O")) {
                if (line3Array[i].equals("B-PER")) {
                    wordType = "PER";
                } else if (line3Array[i].equals("B-LOC")) {
                    wordType = "LOC";
                } else if (line3Array[i].equals("B-ORG")) {
                    wordType = "ORG";
                } else if (line3Array[i].equals("B-MISC")) {
                    wordType = "MISC";
                }
                start = i;

                while (i < line1Array.length && !line3Array[i].equals("O")) {
                    i++;
                }

                end = i - 1;
                insertWord(line1Array, start, end, wordType);
                start = 0;
                end = 0;
                wordType = "";
            }
            i++;
        }




    }

    private static void insertWord(String[] lineArray, int start, int end, String wordType) {
        TrieNode node = root;

        for (int i = start; i <= end; i++) {
            if (!node.children.containsKey(lineArray[i])) {
                node.children.put(lineArray[i], new TrieNode(lineArray[i]));
            }
            node = node.children.get(lineArray[i]);
        }
        node.isWord = true;
        node.type = wordType;
    }

    private static void search(){

        for (int i = 0; i < testSet.size(); i++) {
            helper(i, testSet.get(i), 0, root);

        }

    }

    private static void helper(int start, String word, int count, TrieNode root) {
        TrieNode node = root;
        if (!node.children.containsKey(word)) {
            return;
        }

        while ((start + count) < testSet.size()) {
            int index = start + count;
            String testWord = testSet.get(index);
            if (!node.children.containsKey(testWord)) {
                return;
            }
            if (node.children.get(testWord).isWord) {
                map.get(node.children.get(testWord).type).add(start + "-" + index);
                return;
            } else {
                count++;
                node = node.children.get(testWord);
            }
        }

    }
}
