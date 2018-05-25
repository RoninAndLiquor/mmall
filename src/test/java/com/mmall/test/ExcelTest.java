package com.mmall.test;

import com.google.gson.Gson;
import com.mmall.util.JsonUtil;
import com.mmall.util.PoiUtil;
import jdk.nashorn.internal.runtime.JSONListAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ExcelTest {

    public static void main(String[] args) {
        String path1 = "F:/j.xls";
        String path2 = "F:/w.xls";
        File f1 = new File(path1);
        File f2 = new File(path2);
        try{
            InputStream in1 = new FileInputStream(f1);
            InputStream in2 = new FileInputStream(f2);
            List<Map<String, List<Object>>> bankListByExcel1 = PoiUtil.getBankListByExcel(in1, path1, null);
            List<Map<String, List<Object>>> bankListByExcel2 = PoiUtil.getBankListByExcel(in2, path2, null);
            for(int i=0;i<bankListByExcel1.size();i++){
                Map<String, List<Object>> stringListMap = bankListByExcel1.get(i);
                for(Map.Entry<String,List<Object>> m:stringListMap.entrySet()){
                    System.out.println(m.getValue().size()+"  ");
                }
                System.out.println(new Gson().toJson(bankListByExcel1.get(i)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
