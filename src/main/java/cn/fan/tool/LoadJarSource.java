package cn.fan.tool;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 加载java源代码，返回具体的位置
 * 
 * @author fan
 *
 */
public class LoadJarSource implements LoadResourceI {
	private Path projectPath;

	/**
	 * 
	 * @param subpath such as:  src/test/java/JarSource
	 */
	public LoadJarSource(String subpath) {
		try {
			projectPath = Paths.get(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI())
								.resolve(".." + File.separator + "..").normalize().resolve(File.separator + subpath);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Path load(String filename) {
		return projectPath.resolve(filename);
	}

}
