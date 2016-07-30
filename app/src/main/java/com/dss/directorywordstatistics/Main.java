package com.dss.directorywordstatistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dusensong_ on 2016/7/30.
 */
public class Main {

    public static class Word {
        String word;
        Integer amount;

        public Word(String word, Integer amount) {
            this.word = word;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "Word{" +
                    "word='" + word + '\'' +
                    ", amount=" + amount +
                    '}';
        }
    }

    static HashMap<String, Integer> wordsMap = new HashMap<>();


    public static void main(String[] args) {
        System.out.println("==========单词统计========");

        // 待统计目录
        String inputDir = "C:\\Dss\\android-studio\\sdk\\docs";
//        String inputDir = "C:\\1";
        List<File> files = recurseDirs(new File(inputDir), ".*\\.html");
        System.out.println("待统计文件数:" + files.size());

        System.out.println("\n开始文件统计");
        int total = files.size();
        for (int i = 0; i < files.size(); ++i) {
            statisticsFile(files.get(i));

            System.out.println("已统计数：" + (i + 1));
        }
        System.out.println("统计完毕");

        System.out.println("\n分析统计结果，并输出");
        List<Word> wordList = getOrderWordList();
        outputWordList(wordList);
    }

    public static List<Word> getOrderWordList() {
        List<Word> wordList = new ArrayList<>();

        // map to list
        Iterator<String> iterator = wordsMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            wordList.add(new Word(key, wordsMap.get(key)));
        }

        // 单词按频率排序，从大到小
        Collections.sort(wordList, new Comparator<Word>() {
            @Override
            public int compare(Word lhs, Word rhs) {
                return Integer.compare(rhs.amount, lhs.amount);
            }
        });

        return wordList;
    }

    public static void statisticsFile(File file) {
        try {
            BufferedReader in = null;
            try {
                // 读文件，统计单词
                in = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file)));

                String line;
                while ((line = in.readLine()) != null) {
                    // 获取每一行中包含的所有单词，并更新统计信息
                    String[] words = line.split("[^a-zA-Z]");
                    for (String item : words) {
                        String word = item.toLowerCase();
                        Integer wordNum = wordsMap.get(word);
                        wordsMap.put(word, wordNum == null ? 1 : wordNum + 1);
                    }
                }

            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void outputWordList(List<Word> words) {
        String outPath = "D:/WordStatics.txt";

        // 格式化输出
        StringBuffer sb = new StringBuffer();
        for (Word item : words) {
            sb.append(item.word + "    " + item.amount + "\r\n");
        }

        try {
            PrintWriter printWriter = null;
            try {
                // 输出单词统计信息
                printWriter = new PrintWriter(new File(outPath));
                printWriter.write(sb.toString());
            } finally {
                if (printWriter != null) {
                    printWriter.flush();
                    printWriter.close();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 遍历整个目录树，返回所有名字匹配的文件
     *
     * @param startDir
     * @param regex
     * @return
     */
    public static List<File> recurseDirs(File startDir, String regex) {
        List<File> result = new ArrayList<>();
        for (File item : startDir.listFiles()) {
            if (item.isDirectory()) {
                // 目录递归
                result.addAll(recurseDirs(item, regex));
            } else {
                // 名字匹配
                if (item.getName().matches(regex)) {
                    result.add(item);
                }
            }
        }
        return result;
    }
}
