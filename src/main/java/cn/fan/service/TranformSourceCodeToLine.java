package cn.fan.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.HashMap;

import org.apache.log4j.Logger;

import cn.fan.model.SourceSubPathEnum;
import cn.fan.tool.LoadSourceCode;

/**
 * 转换java 源代码 进入对应的行号  such as  key：int a = 10 value：10
 * @author fan
 * 考虑是否需要引入数据库 而非都加载到hashmap
 */
public class TranformSourceCodeToLine {
	private Path path;
	private Logger logger = Logger.getLogger(TranformSourceCodeToLine.class);

	/**
	 * @param filename  XXX.java
	 */
	public TranformSourceCodeToLine(String filename) {
		try {
			this.path = LoadSourceCode.load(SourceSubPathEnum.SOURCE_CODE_PATH, filename);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			logger.error(e.getStackTrace());
			e.printStackTrace();
		}
	}

	/**
	 * 为代码分配 linenum的核心 
	 * @return hashmap<>{"int a=10 #备注信息":2,"public construct(){":3}
	 */
	public HashMap<Integer, String> transform() {
		HashMap<Integer, String> result = new HashMap<Integer, String>(16);
		File file = new File(path.toString());
		int lineNums = 1;
		if (file.exists()) {
			try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
				//加入 代码 和 行号
				result.put(lineNums++, fileReader.readLine().trim());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			logger.error("需要读取的源代码文件不存在，请仔细检查源文件" + path, new FileNotFoundException());
		}
		return result;
	}
}
