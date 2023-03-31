package com.bright.commoncode.utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yuanzhouhui
 * @date 2023-03-31 13:24
 * @description TextUtil
 */
public class TextUtil {

    public static void main(String[] args) {
        try {
            // 要替换的文本
            String filePath = "D:\\DownLoad\\这个网游策划果然有问题.txt";
            // 替换之后要保存的路径
            String tmpFilePath = "D:\\DownLoad\\这个网游策划果然有问题temp.txt";
            // 读取文件内容
            List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
            // 匹配 开头为数字的行
            Pattern pattern = Pattern.compile("\\d+、");
            List<String> finalLines = new ArrayList<>();
            String temp;
            String newLine;
            for (String line : lines) {
                Matcher matcher = pattern.matcher(line);
                newLine = line;
                if (matcher.find()) {
                    // 提取数字
                    temp = matcher.group();
                    String replace = "第" + temp.replace("、", "") + "章 ";
                    // 进行替换
                    newLine = line.replaceFirst(temp, replace);
                }
                finalLines.add(newLine);
            }

            // 输出
            Files.write(Paths.get(tmpFilePath), finalLines, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
