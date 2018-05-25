package com.mmall.util;

import com.mmall.pojo.ChangetOfBalance;
import com.mmall.pojo.IncomeAndExpend;
import com.mmall.pojo.LoanAndArrears;
import com.mmall.pojo.TransferAccounts;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoiUtil {

    private final static String excel2003L =".xls";    //2003- 版本的excel
    private final static String excel2007U =".xlsx";   //2007+ 版本的excel

    /**
     * 描述：获取IO流中的数据，组装成List<List<Object>>对象
     * @param in,fileName
     * @return
     * @throws Exception
     */
    public static <T>  List<Map<String,List<T>>> getBankListByExcel(InputStream in, String fileName, Class<T> clazz) throws Exception{
        List<Map<String,List<T>>> list = null;

        //创建Excel工作薄
        Workbook work = getWorkbook(in,fileName);
        if(null == work){
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        list = new ArrayList<Map<String,List<T>>>();
        //遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            String sheetName = sheet.getSheetName();
            if(sheet==null){continue;}
            if(clazz == null){
                if("收入支出".equals(sheetName)){
                    list.add(sheetOpearte(sheet,list,row,cell, IncomeAndExpend.class));
                }
                if("转账".equals(sheetName)){
                    list.add(sheetOpearte(sheet,list,row,cell, TransferAccounts.class));
                }
                if("余额变更".equals(sheetName)){
                    list.add(sheetOpearte(sheet,list,row,cell, ChangetOfBalance.class));
                }
                if("借出欠款".equals(sheetName)){
                    list.add(sheetOpearte(sheet,list,row,cell, LoanAndArrears.class));
                }
            }else{
                list.add(sheetOpearte(sheet,list,row,cell,clazz));
            }
        }
        work.close();
        return list;
    }

    private static <T> Map<String,List<T>> sheetOpearte(Sheet sheet,List<Map<String,List<T>>> list,Row row,Cell cell,Class clazz){
        Map<String,List<T>> map = new HashMap<String,List<T>>();
        //遍历当前sheet中的所有行
        List<T> listt = new ArrayList<T>();
        for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
            row = sheet.getRow(j);
            if(row==null||row.getFirstCellNum()==j){continue;}

            //遍历所有的列
            List<Object> li = new ArrayList<Object>();
            for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                cell = row.getCell(y);
                //CellStyle cellStyle = cell.getCellStyle();
                //String stringCellValue = cell.getStringCellValue();
                //String cellFormula = cell.getCellFormula();
                //System.out.println("stringCellValue"+stringCellValue);
                //System.out.println("cellFormula"+cellFormula);
                li.add(getCellValue(cell));
            }
            Object obj = ReflectUtil.set(li, clazz);
            listt.add((T)obj);
        }
        map.put(sheet.getSheetName(),listt);
        return map;
    }

    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     * @param inStr,fileName
     * @return
     * @throws Exception
     */
    public static  Workbook getWorkbook(InputStream inStr,String fileName) throws Exception{
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if(excel2003L.equals(fileType)){
            wb = new HSSFWorkbook(inStr);  //2003-
        }else if(excel2007U.equals(fileType)){
            wb = new XSSFWorkbook(inStr);  //2007+
        }else{
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }

    /**
     * 描述：对表格中数值进行格式化
     * @param cell
     * @return
     */
    public static Object getCellValue(Cell cell){
        String value = null;
        DecimalFormat df = new DecimalFormat("0");  //格式化number String字符
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");  //日期格式化
        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if("General".equals(cell.getCellStyle().getDataFormatString())){
                value = df.format(cell.getNumericCellValue());
            }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
                value = sdf.format(cell.getDateCellValue());
            }else{
                value = df2.format(cell.getNumericCellValue());
            }
            break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue()+"";
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }

}
