package cn.path.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HashClone {
    static HashMap<String, List<String>> resultHashMap = new HashMap<String, List<String>>();

    public static <E> void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("1");
        resultHashMap.put("1", arrayList);
        HashMap<String, List<String>> clone = deepClone(resultHashMap);
        arrayList.add("b");
        resultHashMap.put("1", arrayList);
        System.out.println(resultHashMap);
        System.out.println(clone);
    }

    @SuppressWarnings("unchecked")
    private static HashMap<String, List<String>> deepClone(HashMap<String, List<String>> variableToLines) {
        HashMap<String, List<String>> result = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(variableToLines);
            bis = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bis);
            result = (HashMap<String, List<String>>) ois.readObject();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            try {
                bos.close();
                oos.close();
                bis.close();
                ois.close();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }
}
