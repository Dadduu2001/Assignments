

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCounter {

    public static void main(String[] args) throws IOException {
        String currentDir = System.getProperty("user.dir"); 
        // Read URLs from urls.txt
        List<String> urls = readLinesFromFile(currentDir + "/urls.txt");

        // Read words from words.txt
        List<String> wordsToCount = readLinesFromFile(currentDir + "/words.txt");

        // Store word counts for each URL and overall
        Map<String, Map<String, Integer>> urlWordCounts = new HashMap<>();
        Map<String, Integer> totalWordCounts = new HashMap<>();

        for (String word : wordsToCount) {
            totalWordCounts.put(word, 0); 
        }



        for (String url : urls) {
            String content = fetchURLContent(url);
            Map<String, Integer> wordCounts = countWords(content, wordsToCount);
            urlWordCounts.put(url, wordCounts);

            
            for (String word : wordsToCount) {
                totalWordCounts.put(word, totalWordCounts.get(word) + wordCounts.getOrDefault(word, 0));
            }

        
            System.out.println("URL: " + url);
            printTopWords(wordCounts, 3);


        }


        System.out.println("\nTotal word counts across all URLs:");
        printTopWords(totalWordCounts, totalWordCounts.size());
    }



     static void printTopWords(Map<String, Integer> wordCounts, int topN) {
            List<Map.Entry<String, Integer>> sortedWordCounts = new ArrayList<>(wordCounts.entrySet());


            sortedWordCounts.sort(Map.Entry.<String, Integer>comparingByValue().reversed());



        for (int i = 0; i < Math.min(topN, sortedWordCounts.size()); i++) {

                Map.Entry<String, Integer> entry = sortedWordCounts.get(i);
                System.out.println("- " + entry.getKey() + ": " + entry.getValue());


            }


    }





    static String fetchURLContent(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append(" ");
        }
        reader.close();
        return content.toString();
    }

    static Map<String, Integer> countWords(String text, List<String> wordsToCount) {
        Map<String, Integer> wordCounts = new HashMap<>();

        Pattern pattern = Pattern.compile("\\b(" + String.join("|", wordsToCount) + ")\\b", Pattern.CASE_INSENSITIVE); //Case insensitive
        Matcher matcher = pattern.matcher(text);
        while(matcher.find()){
            String matchedWord = matcher.group(1).toLowerCase();

                int currentCount = wordCounts.getOrDefault(matchedWord,0);
                wordCounts.put(matchedWord,currentCount+1);
        }

        return wordCounts;
    }

    static List<String> readLinesFromFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        return lines;
    }
}
