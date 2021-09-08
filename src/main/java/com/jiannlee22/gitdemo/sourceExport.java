package com.jiannlee22.gitdemo;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chaos
 * on 2018/11/2
 */

/**
 * 一个用于申请软著时源码导出的工具类
 */
class SourceExport {
    public static void main(String[] args) throws Exception {
        //文件读取路径
        File dir = new File("F:\\jiaan_project\\m-bs01\\app\\src\\main\\java\\com\\gvs\\m\\bs01\\activity");
        //文件输出路径
        File target = new File("F:\\jiannlee22\\doc\\m-bs02.docx");
        BufferedWriter bw = new BufferedWriter(new FileWriter(target));
        HashMap<String, String> map = new HashMap<String,String>();
        method1(dir,bw,map);
        method3(map.toString(), bw);
    }
    // 遍历文件夹下所有文件,对于有内容的文件全部写到一个文本文件中。
    public static void method1(File dir, Writer writer, HashMap<String,String> map){
        File[] files = dir.listFiles();
        for(File file:files){
            if(file.isDirectory()){
                method1(file, writer,map);
            }else {
                if(file.length()!=0){
                    map.put(file.getName(), method2(file));
                }
            }

        }

    }
    //读取文件里面的内容
    public static String method2(File file){
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while((line = br.readLine())!=null){
                sb.append(line);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            try {
                if(br!=null){
                    br.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
        }
        return sb.toString();

    }
    //将读取的路径以及相应的内容写入指定的文件
    public static void method3(String str,Writer writer){
        try {
            writer.write(str);
        } catch (Exception e) {
            // TODO: handle exception
        }finally{

            try {
                if(writer!=null)
                    writer.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
        }

    }
}

