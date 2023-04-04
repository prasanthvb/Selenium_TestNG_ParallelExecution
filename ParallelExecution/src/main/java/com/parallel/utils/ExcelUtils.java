package com.parallel.utils;


import com.parallel.utils.FrameworkConstant;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.parallel.base.TestBase.setPropertyValue;
import static com.parallel.listeners.ListenerBase.moduleName;

public class ExcelUtils {

    private static XSSFSheet ExcelWSheet;
    private static XSSFWorkbook ExcelWBook;
    private static XSSFCell Cell;
    public static String[] summaryHeader = new String[]{"Module Name", "Tests Passed", "Tests Failed", "Tests Skipped", "Tests Steps Passed", "Tests Steps Failed"};
    public static String[] testResultsFileHeader = new String[]{"File Path", "File Name"};
    public static String[] emailAddressFileHeader = new String[]{"Email Address"};
    public static int suiteCount = 0;


    public static void createExcel(String excelFileNameWithPath) throws Exception {
        try {
            File fileName = new File(excelFileNameWithPath);
            if((!fileName.exists()||suiteCount==0)&&!moduleName.equalsIgnoreCase("sendemail")){
                XSSFWorkbook workbook = new XSSFWorkbook();
                FileOutputStream fos = new FileOutputStream(fileName);
                workbook.write(fos);
                fos.flush();
                fos.close();
                workbook.close();
                createSheetInExcel(excelFileNameWithPath,"Summary");
                createSheetInExcel(excelFileNameWithPath,"TestResultsFile");
                createSheetInExcel(excelFileNameWithPath,"EmailAddress");
                updateExcelSheetWithArray(excelFileNameWithPath,"Summary",0, summaryHeader);
                updateExcelSheetWithArray(excelFileNameWithPath,"TestResultsFile",0, testResultsFileHeader);
                updateExcelSheetWithArray(excelFileNameWithPath,"EmailAddress",0, emailAddressFileHeader);
                setPropertyValue("moduleNameRowCountForExcel","0", FrameworkConstant.PROPERTYFILE_PATH);
                setPropertyValue("testResultsFileRowCountForExcel","0", FrameworkConstant.PROPERTYFILE_PATH);
                setPropertyValue("emailAddressRowCountForExcel","0", FrameworkConstant.PROPERTYFILE_PATH);
                setPropertyValue("moduleName","onStart",FrameworkConstant.PROPERTYFILE_PATH);
                setPropertyValue("testResultsFileName", "onStart", FrameworkConstant.PROPERTYFILE_PATH);
                suiteCount = suiteCount+1;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void createSheetInExcel(String excelFileNameWithPath, String sheetName) throws IOException {
        try {
            FileInputStream ExcelFile = new FileInputStream(excelFileNameWithPath);
            ExcelWBook = new XSSFWorkbook(ExcelFile);
            ExcelWSheet = ExcelWBook.createSheet(sheetName);
            ExcelFile.close();
            FileOutputStream outFile = new FileOutputStream(new File(excelFileNameWithPath));
            ExcelWBook.write(outFile);
            outFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateExcelSheetWithArray(String excelFileNameWithPath, String sheetName, int row, String[] columnValues) throws IOException {

        FileInputStream ExcelFile = new FileInputStream(excelFileNameWithPath);
        // Access the required test data sheet
        ExcelWBook = new XSSFWorkbook(ExcelFile);
        ExcelWSheet = ExcelWBook.getSheet(sheetName);
        XSSFRow sheetrow = ExcelWSheet.getRow(row);
        int i=0;
        if(sheetrow == null){sheetrow = ExcelWSheet.createRow(row);}
        org.apache.poi.ss.usermodel.Cell cell = null;

        for (String tableColumns : columnValues) {
            //Update the value of cell
            cell = sheetrow.getCell(i);
            if(cell == null){cell = sheetrow.createCell(i);}
            //Update Cell Value
            cell.setCellValue(tableColumns);
            i++;
        }
        ExcelFile.close();
        FileOutputStream outFile =new FileOutputStream(new File(excelFileNameWithPath));
        ExcelWBook.write(outFile);
        outFile.close();
    }

    public static void updateExcelSheetRowColumn(String excelFileNameWithPath, String sheetName, int row, int column, String cellValue) {
        try {
            FileInputStream ExcelFile = new FileInputStream(excelFileNameWithPath);
            // Access the required test data sheet
            ExcelWBook = new XSSFWorkbook(ExcelFile);
            ExcelWSheet = ExcelWBook.getSheet(sheetName);
            XSSFRow sheetrow = ExcelWSheet.getRow(row);
            if(sheetrow == null){sheetrow = ExcelWSheet.createRow(row);}
            org.apache.poi.ss.usermodel.Cell cell = null;
            //Update the value of cell
            cell = sheetrow.getCell(column);
            if(cell == null){cell = sheetrow.createCell(column);}
            //Update Cell Value
            cell.setCellValue(cellValue);
            ExcelFile.close();
            FileOutputStream outFile =new FileOutputStream(new File(excelFileNameWithPath));
            ExcelWBook.write(outFile);
            outFile.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void updateTestSummary(String excelFileNameWithPath, String sheetName, int row, int column) {
        try {
            FileInputStream ExcelFile = new FileInputStream(excelFileNameWithPath);
            // Access the required test data sheet
            ExcelWBook = new XSSFWorkbook(ExcelFile);
            ExcelWSheet = ExcelWBook.getSheet(sheetName);
            XSSFRow sheetrow = ExcelWSheet.getRow(row);
            if(sheetrow == null){sheetrow = ExcelWSheet.createRow(row);}
            org.apache.poi.ss.usermodel.Cell cell = null;
            cell = sheetrow.getCell(column);
            if(cell == null){
                cell = sheetrow.createCell(column);
                cell.setCellValue("1");
            } else {
                cell.setCellValue(String.valueOf(Integer.parseInt(getCellData(row,column))+1));
            }
            ExcelFile.close();
            FileOutputStream outFile =new FileOutputStream(new File(excelFileNameWithPath));
            ExcelWBook.write(outFile);
            outFile.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getCellData(int RowNum, int ColNum) throws Exception {
        try {
            Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
            DataFormatter formatter = new DataFormatter();
            return formatter.formatCellValue(Cell);
        }catch(Exception e){
            System.out.println(e.getMessage());
            throw (e);
        }
    }

    public static List<Map> getSheetDataInListMap(String fileName, String sheetName) throws Exception {

        List<Map> listMap = new ArrayList<Map>();

        try {

            FileInputStream ExcelFile = new FileInputStream(fileName);
            // Access the required test data sheet
            ExcelWBook = new XSSFWorkbook(ExcelFile);
            ExcelWSheet = ExcelWBook.getSheet(sheetName);
            int startRow = 1;
            int startCol = 0;
            int totalCols = ExcelWSheet.getRow(0).getPhysicalNumberOfCells();
            for (int i=startRow;i<=ExcelWSheet.getLastRowNum();i++) {
                Map map = new HashMap<>();
                for (int j=startCol;j<totalCols;j++){
                    map.put(getCellData(0,j),getCellData(i,j).equals("") ? "0" : getCellData(i,j));
                }
                listMap.add(map);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return listMap;

    }

    public static ArrayList getColumnValuesInArray(String fileName, String sheetName,int startRow, int columnNumber) throws Exception {

        ArrayList columnValues = new ArrayList<Map>();

        try {

            FileInputStream ExcelFile = new FileInputStream(fileName);
            // Access the required test data sheet
            ExcelWBook = new XSSFWorkbook(ExcelFile);
            ExcelWSheet = ExcelWBook.getSheet(sheetName);
            for (int i=startRow;i<=ExcelWSheet.getLastRowNum();i++) {
                columnValues.add(getCellData(i,columnNumber).equals("") ? "0" : getCellData(i,columnNumber));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return columnValues;

    }

}

