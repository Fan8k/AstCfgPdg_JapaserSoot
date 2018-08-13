package cn.fan.ast.visitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * csv 文件处理方式
 * 
 * @ClassName: CSVHandAdapter
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author LiRongFan
 * @date 2018年7月31日 下午1:55:41
 *
 */
public class CSVHandAdapter implements PatternI {
    private BufferedWriter nodeWriter;
    private BufferedWriter edgeWriter;

    public CSVHandAdapter(File nodeFile, File edgeFile) {
        try {
            if (!nodeFile.exists()) {
                nodeFile.createNewFile();
            }
            if (!edgeFile.exists()) {
                edgeFile.createNewFile();
            }
            nodeWriter = new BufferedWriter(new FileWriter(nodeFile));
            edgeWriter = new BufferedWriter(new FileWriter(edgeFile));
            // csv文件的格式要求
            initFile();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("CSVHandAdapter类初始化异常！");
            e.printStackTrace();

        }
    }

    public void initFile() throws IOException {
        nodeWriter.write("Id,Name" + System.lineSeparator());
        edgeWriter.write("startNode,endNode,label" + System.lineSeparator());
    }

    @Override
    public boolean dealNode(String nodelabel, String nodeName) {
        // TODO Auto-generated method stub
        boolean flag = false;
        try {
            nodeWriter.write(nodelabel + "," + nodeName + System.lineSeparator());
            flag = true;
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean dealPrimaryEdge(String startNode, String endNode, String remark) {
        // TODO Auto-generated method stub
        boolean flag = false;
        try {
            edgeWriter.write(startNode + "," + endNode + "," + remark + System.lineSeparator());
            flag = true;
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean dealCfgEdge(String startNode, String endNode, String remark) {
        // TODO Auto-generated method stub
        boolean flag = false;
        try {
            edgeWriter.write(startNode + "," + endNode + "," + remark + System.lineSeparator());
            flag = true;
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean dealPdgEdge(String startNode, String endNode, String remark) {
        // TODO Auto-generated method stub
        boolean flag = false;
        try {
            edgeWriter.write(startNode + "," + endNode + "," + remark + System.lineSeparator());
            flag = true;
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return flag;
    }

    public void close() {
        try {
            nodeWriter.close();
            edgeWriter.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
