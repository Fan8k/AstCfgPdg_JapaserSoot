package cn.fan.model;

/**
 * 资源 子路径抽出来  减少编辑出错的情况
 * @author fan
 *
 */
public enum SourceSubPathEnum {
	/**
	 * SOURCE_CODE_PATH 描述了java源代码存放的路径
	 * JAR_SOURCE_PATH 描述了jar包存放的路径
	 */
	SOURCE_CODE_PATH("src/main/resource/SourceCode/"), JAR_SOURCE_PATH("src/main/resource/JarSource/");
	public String path;

	private SourceSubPathEnum(String path) {
		this.path = path;
	}

}