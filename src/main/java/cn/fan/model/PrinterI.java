package cn.fan.model;

/**
 * 自定义打印类接口
 * 
 * @author fan
 *
 */
@FunctionalInterface
public interface PrinterI {
	/**
	 * 输出目标类型的文件
	 * 
	 * @param node
	 *            需要输出的数据流
	 */
	void print(Node node);
}
