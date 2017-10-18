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



//        try {
//            File file = new File("./src/train.txt");
//            FileReader fileReader = new FileReader(file);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            StringBuffer stringBuffer = new StringBuffer();
//            String line;
//            int i = 0;
//            String line1 = "";
//            String line2 = "";
//            String line3 = "";
//            while ((line = bufferedReader.readLine()) != null) {
//
//                if (i == 0) {
//                    line1 = line.toString();
//                    i++;
//                } else if (i == 1) {
//                    line2 = line.toString();
//                    i++;
//                } else if (i == 2) {
//                    line3 = line.toString();
//                    i++;
//                }
//
//                if (i == 3) {
//                    String[] line1Array = line1.split("\t");
//                    String[] line2Array = line2.split("\t");
//                    String[] line3Array = line3.split("\t");
//
//                    handleLine(line1Array, line2Array, line3Array);
//
//                    i = 0;
//                    line1 = "";
//                    line2 = "";
//                    line3 = "";
//                }
//
//
//            }
//            fileReader.close();
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            File file = new File("./src/test.txt");
//            FileReader fileReader = new FileReader(file);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            StringBuffer stringBuffer = new StringBuffer();
//            String line;
//            int i = 0;
//            String line1 = "";
//            while ((line = bufferedReader.readLine()) != null) {
//
//
//                for (String tempWord : line.split("\t")) {
//                    testSet.add(tempWord);
//                }
//
//                line = bufferedReader.readLine();
//                line = bufferedReader.readLine();
//
//
//
//            }
//            fileReader.close();
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        map.put("PER", new ArrayList<>());
//        map.put("LOC", new ArrayList<>());
//        map.put("ORG", new ArrayList<>());
//        map.put("MISC", new ArrayList<>());
//
//
//        search();
//
//
//        System.out.println("PER Result:");
//        for (String result : map.get("PER")) {
//            System.out.print(result);
//            System.out.print(" ");
//        }
//
//        System.out.println("");
//        System.out.println("PER LOC:");
//
//        for (String result : map.get("LOC")) {
//            System.out.print(result);
//            System.out.print(" ");
//        }
//
//        System.out.println("");
//        System.out.println("PER ORG:");
//
//
//        for (String result : map.get("ORG")) {
//            System.out.print(result);
//            System.out.print(" ");
//        }
//
//        System.out.println("");
//        System.out.println("PER MISC:");
//
//        for (String result : map.get("MISC")) {
//            System.out.print(result);
//            System.out.print(" ");
//        }

        part2();


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


    static HashMap<String,HashMap<String, HashMap<String, Integer>>> map1 = new HashMap<>();
    static HashMap<String, Integer> namedTagCountMap = new HashMap<>();
    static HashMap<String, HashMap<String, Integer>> mapStatus = new HashMap<>();
    static ArrayList<Integer> resultList = new ArrayList<>();


    private static void part2() {

        namedTagCountMap.put("B-PER", 0);
        namedTagCountMap.put("I-PER", 0);
        namedTagCountMap.put("B-LOC", 0);
        namedTagCountMap.put("I-LOC", 0);
        namedTagCountMap.put("B-ORG", 0);
        namedTagCountMap.put("I-ORG", 0);
        namedTagCountMap.put("B-MISC", 0);
        namedTagCountMap.put("I-MISC", 0);
        namedTagCountMap.put("O", 0);
        namedTagCountMap.put("*", 0);


        String[] tempArray = {"B-PER", "I-PER", "B-LOC", "I-LOC", "B-ORG", "I-ORG", "B-MISC", "I-MISC", "O", "*"};
        String[] tempArray2 = {"B-PER", "I-PER", "B-LOC", "I-LOC", "B-ORG", "I-ORG", "B-MISC", "I-MISC", "O", "*"};

        for (String temp1 : tempArray) {
            HashMap<String, Integer> tMap = new HashMap<>();
            mapStatus.put(temp1, tMap);
        }

        for (String temp1 : tempArray) {
            for (String temp2 : tempArray2) {
                mapStatus.get(temp1).put(temp2, 0);
            }
        }


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



                    i = 0;
                    line1 = "";
                    line2 = "";
                    line3 = "";
                    handleLineForPart2(line1Array, line2Array, line3Array);
                }



            }
            fileReader.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println(map1.get("B-LOC").get("NNP").get("DETROIT"));
//        System.out.println(mapStatus.get("O").get("B-LOC"));
//        System.out.println(namedTagCountMap.get("B-LOC"));



        try {
            File file = new File("./src/test.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
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



                    i = 0;
                    line1 = "";
                    line2 = "";
                    line3 = "";
                    tag(line1Array, line2Array, line3Array);
                }



            }
            fileReader.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(resultList.size());



    }




    private static void handleLineForPart2(String[] line1Array, String[] line2Array, String[] line3Array){

        namedTagCountMap.put("*", namedTagCountMap.get("*") + 1);

        for (int i = 0; i < line3Array.length; i++) {

            if (!map1.containsKey(line3Array[i])) {
                map1.put(line3Array[i], new HashMap<>());
            }

            if (!map1.get(line3Array[i]).containsKey(line2Array[i])) {
                map1.get(line3Array[i]).put(line2Array[i], new HashMap<>());
            }

            if (!map1.get(line3Array[i]).get(line2Array[i]).containsKey(line1Array[i])) {
                map1.get(line3Array[i]).get(line2Array[i]).put(line1Array[i], 0);
            }

            map1.get(line3Array[i]).get(line2Array[i]).put(line1Array[i], map1.get(line3Array[i]).get(line2Array[i]).get(line1Array[i]) + 1);

            namedTagCountMap.put(line3Array[i], namedTagCountMap.get(line3Array[i]) + 1);

            if (i == 0) {

                mapStatus.get("*").put(line3Array[i], mapStatus.get("*").get(line3Array[i]) + 1);
            } else {
                mapStatus.get(line3Array[i - 1]).put(line3Array[i], mapStatus.get(line3Array[i - 1]).get(line3Array[i]) + 1);
            }
        }



    }


    private static void tag(String[] line1Array, String[] line2Array, String[] line3Array) {

        String[] namedTagArray = {"B-PER", "I-PER", "B-LOC", "I-LOC", "B-ORG", "I-ORG", "B-MISC", "I-MISC", "O"};

        double[][] scoreMatrix = new double[9][line1Array.length];

        int[][] tagMatrix = new int[9][line1Array.length];


        for (int i = 0; i < line1Array.length; i++) {
            String word = line1Array[i];
            String posTag = line2Array[i];

            if (i == 0) {
                for (int j = 0; j < 9; j++) {
                    double tempScore;

                    try {
                        tempScore = (double)map1.get(namedTagArray[j]).get(posTag).getOrDefault(word, 1) / namedTagCountMap.get(namedTagArray[j]);

                    } catch (NullPointerException e) {
                        tempScore = (double)1 / namedTagCountMap.get(namedTagArray[j]);
                    }


//                    System.out.println(tempScore);
                    tempScore = Math.log(tempScore);


                    tempScore = tempScore + Math.log( (double)mapStatus.get("*").get(namedTagArray[j]) / namedTagCountMap.get("*"));
                    scoreMatrix[j][i] = tempScore;
                    tagMatrix[j][i] = 9;
                }
            } else {
                for (int j = 0 ; j < 9; j++) {

                    double[] tempMax = new double[9];
                    for (int k = 0; k < 9; k++) {

                        double tempInsideSocre = (double)mapStatus.get(namedTagArray[k]).get(namedTagArray[j]) / namedTagCountMap.get(namedTagArray[k]);
                        tempInsideSocre = Math.log(tempInsideSocre);
                        tempInsideSocre = tempInsideSocre + scoreMatrix[k][i - 1];
                        tempMax[k] = tempInsideSocre;
                    }
                    int maxTag = 0;
                    double maxValue = tempMax[0];
                    for (int k = 0; k < 9; k++) {
                        if (tempMax[k] > maxValue) {
                            maxValue = tempMax[k];
                            maxTag = k;
                        }
                    }
                    tagMatrix[j][i] = maxTag;

                    double tempScore = 0;

                    try {
                        tempScore = (double)map1.get(namedTagArray[j]).get(posTag).getOrDefault(word, 1);

                    } catch (NullPointerException e) {
                        tempScore = (double)1;
                    }

                    scoreMatrix[j][i] =  Math.log((double) tempScore / namedTagCountMap.get(namedTagArray[j]));

                    scoreMatrix[j][i] += maxValue;

                }

            }

        }

        int maxTag = 0;
        double maxValue = scoreMatrix[0][line1Array.length - 1];

        for (int i = 0 ; i < 9; i++) {

            if (scoreMatrix[i][line1Array.length - 1] > maxValue) {

                maxValue = scoreMatrix[i][line1Array.length - 1];
                maxTag = i;
            }
        }

//        System.out.println(maxTag);



        int[] tempResult = new int[line1Array.length];
        tempResult[line1Array.length - 1] = maxTag;

        for (int i = line1Array.length - 2; i >= 0; i--) {

            maxTag = tagMatrix[maxTag][i + 1];
            tempResult[i] = maxTag;
        }


        for (int i = 0 ; i < tempResult.length ;i++) {
            resultList.add(tempResult[i]);
        }


    }

}
