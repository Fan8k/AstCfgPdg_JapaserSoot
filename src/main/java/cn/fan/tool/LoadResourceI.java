package cn.fan.tool;

import java.nio.file.Path;

/**
 * 加载资源类，抽象这个类目的就是为了用户只需要输入resource中文件名不需要输入很长的目录
 * 
 * @author fan
 *
 */
public interface LoadResourceI {
	/**
	 * 只是目录
	 * 
	 * @param filename
	 * @return 返回该文件返回的具体Path
	 */
	Path load(String filename);
}
