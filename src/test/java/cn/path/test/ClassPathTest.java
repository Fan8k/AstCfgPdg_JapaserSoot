package cn.path.test;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ClassPathTest {
	@Test
	public void test() {
		System.out.println(this.getClass().getCanonicalName());
		try {
			System.out.println(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
			Path path = Paths.get(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
			System.out.println(path);
			Path resolve = path.resolve("../..");
			System.out.println(resolve.normalize() + File.separator);
			List<String> resultStrings = new ArrayList<String>();
			resultStrings.add("fafa");
			List<String> resultString = resultStrings;
			resultString.add("fafafdasfas");

			System.out.println(resultStrings);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
