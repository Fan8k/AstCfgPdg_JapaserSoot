package cn.fan.tool;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import cn.fan.model.SourceSubPathEnum;

/**
 * 加载java源代码，返回具体的位置
 * 
 * @author fan
 *
 */
public class LoadSourceCode {

	public static Path load(SourceSubPathEnum subpath, String filename) throws URISyntaxException {
		return Paths.get(LoadSourceCode.class.getProtectionDomain().getCodeSource().getLocation().toURI()).resolve(".." + File.separator + "..")
							.normalize().resolve(subpath.path).resolve(filename);
	}
}
