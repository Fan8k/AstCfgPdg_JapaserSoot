package cn.fan.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * 
 * @ClassName: ExtractClassName
 * @Description: 提取所有目标的class入口，以便ast和soot进行解析
 * @author LiRongFan
 * @date 2018年7月30日 上午10:48:29
 *
 */
public class ExtractClassName {

    private static final Pattern p;

    static {
        p = Pattern.compile("package\\s+((?:\\w+\\.)+(\\w*){1})");
    }

    /**
     * 
     * @Title: findJavaFile
     * @Description: 遍历整个路径 提取所有java文件的绝对路径
     * @author LiRongFan
     * @param directory
     * @param paths
     * @throws
     */
    public void findJavaFile(File directory, HashMap<String, String> pathsToClassName, List<String> jarsPath) {
        // 对于具有src目录的文件夹，我们只需要进入src
        File[] files = directory.listFiles((File pathName) -> {
            boolean flag = false;
            if (pathName.getName().equals("src") || pathName.getName().equals("target")) {
                flag = true;
            }
            return flag;
        });
        // 不是src目录 都要递归进去了
        if (files.length == 0) {
            File[] listFiles = directory.listFiles();
            for (File file : listFiles) {
                if (file.isDirectory() && !file.getName().equals("test")) {
                    findJavaFile(file, pathsToClassName, jarsPath);
                }
                else if (file.getName().endsWith(".java")) {
                    // 只有java的文件需要处理
                    String extractdPackage = extractPackage(file);
                    pathsToClassName.put(extractdPackage + "." + file.getName().substring(0, file.getName().indexOf(".")), file.getParentFile().toString() + File.separator);
                }
                else if (file.getName().endsWith(".jar")) {
                    jarsPath.add(file.getAbsolutePath());
                }
            }
        }
        else {
            for (File file : files) {
                findJavaFile(file, pathsToClassName, jarsPath);
            }
        }
    }

    private String extractPackage(File file) {
        BufferedReader bufferedReader = null;
        String message = "";
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            message = bufferedReader.readLine();
            while (message != null) {
                Matcher matcher = p.matcher(message);
                if (matcher.find()) {
                    message = matcher.group(1);
                    break;
                }
                message = bufferedReader.readLine();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            try {
                bufferedReader.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return message;
    }

    @Test
    public void Test() {
        HashMap<String, String> paths = new HashMap<String, String>();
        List<String> jarsPath = new ArrayList<String>();
        findJavaFile(new File("D:\\JarAndSource\\clarity"), paths, jarsPath);
        for (Entry<String, String> s : paths.entrySet()) {
            System.out.println(s.getKey() + "\t" + s.getValue());
        }
        for (String s : jarsPath) {
            System.out.println(s);
        }
        System.out.println(paths.size());
    }
}
