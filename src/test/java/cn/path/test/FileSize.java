package cn.path.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileSize {
	public static void main(String[] args) {
		File file = new File("E:\\AST_1.java");
		int lineNums = 1;
		long hasReaded = 0;
		if (file.exists()) {
			try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
				//加入 代码 和 行号
				while (true) {
					String line = fileReader.readLine();
					if (line == null) {
						break;
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
