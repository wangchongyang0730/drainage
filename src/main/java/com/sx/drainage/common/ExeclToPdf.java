package com.sx.drainage.common;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/10/28
 * Time: 16:55
 */
public class ExeclToPdf {
    //合并
    public static PdfPCell mergeCell(String str, Font font, int i, int j) {
        PdfPCell cell = new PdfPCell(new Paragraph(str, font));
        cell.setMinimumHeight(25);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRowspan(i);
        cell.setColspan(j);
        return cell;
    }

    //获取指定内容与字体的单元格
    public static PdfPCell getPDFCell(String string, Font font) {
        //创建单元格对象，将内容与字体放入段落中作为单元格内容
        PdfPCell cell = new PdfPCell(new Paragraph(string, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //设置最小单元格高度
        cell.setMinimumHeight(25);
        return cell;
    }
    /*
    * xlsx文件转pdf
    * @params filePath 文件全路径  f:/文件/test.xlsx
    * @params pdfUrl 生成文件全路径 f:/文件/test.pdf
    * */
    public static void xlsxToPdf(String filePath,String pdfUrl) throws DocumentException, IOException {
        Rectangle rectPageSize = new Rectangle(PageSize.A4);// 定义A3页面大小
        rectPageSize = rectPageSize.rotate(); //横版
        Document document = new Document(rectPageSize, -80, -80, 50, 0); //边距
        PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream(pdfUrl));

        //字体设置
        Font font = null;
        BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
        font = new Font(bf, 13, Font.NORMAL);
        int rowNum = 0;
        int colNum = 0;
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            XSSFSheet sheet = workbook.getSheetAt(0);
            int column = sheet.getRow(0).getLastCellNum();
            int row = sheet.getPhysicalNumberOfRows();

            //下面是找出表格中的空行和空列
            List<Integer> nullCol = new ArrayList<>();
            List<Integer> nullRow = new ArrayList<>();
            for (int j = 0; j < column; j++) {
                int nullColNum = 0;
                for (int i = 0; i < row; i++) {
                    XSSFCell cell = sheet.getRow(i).getCell(j);
                    if (cell == null || (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) && "".equals(cell.getStringCellValue())) {
                        nullColNum++;
                    }
                }
                if (nullColNum == row) {
                    nullCol.add(j);
                }
            }

            for (int i = 0; i < row; i++) {
                int nullRowNum = 0;
                for (int j = 0; j < column; j++) {
                    XSSFCell cell = sheet.getRow(i).getCell(j);
//					String str = cell.getStringCellValue();
                    if (cell == null || (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) && "".equals(cell.getStringCellValue())) {
                        nullRowNum++;
                    }
                }
                if (nullRowNum == column) {
                    nullRow.add(i);
                }
            }
            PdfPTable table = new PdfPTable(column - sheet.getRow(0).getFirstCellNum());
            List<CellRangeAddress> ranges = sheet.getMergedRegions();

            PdfPCell cell1 = null;
            String str = null;
            for (int i = sheet.getFirstRowNum(); i < row; i++) {
                if (nullRow.contains(i)) { //如果这一行是空行，这跳过这一行
                    continue;
                }
                for (int j = sheet.getRow(0).getFirstCellNum(); j < column; j++) {
                    if (nullCol.contains(j)) { //如果这一列是空列，则跳过这一列
                        continue;
                    }
                    boolean flag = true;
                    XSSFCell cell = sheet.getRow(i).getCell(j);
                    if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                        str = cell.getNumericCellValue() + "";
                    } else {
                        str = cell.getStringCellValue();
                    }
                    for (CellRangeAddress range : ranges) { //合并的单元格判断和处理
                        if (j >= range.getFirstColumn() && j <= range.getLastColumn() && i >= range.getFirstRow()
                                && i <= range.getLastRow()) {
                            if (str == null || "".equals(str)) {
                                flag = false;
                                break;
                            }
                            rowNum = range.getLastRow() - range.getFirstRow() + 1;
                            colNum = range.getLastColumn() - range.getFirstColumn() + 1;
                            cell1 = mergeCell(str, font, rowNum, colNum);
                            table.addCell(cell1);
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        table.addCell(getPDFCell(str, font));
                    }
                }
            }

            workbook.close();
            document.open();
            document.add(table);
            document.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
     * xls文件转pdf
     * */
    public static void xlsToPdf(String filePath,String pdfUrl) throws DocumentException, IOException {
        //设置页面大小
        Rectangle rectPageSize = new Rectangle(PageSize.A4);// 定义A3页面大小
        rectPageSize = rectPageSize.rotate(); //横版
        Document document = new Document(rectPageSize, -80, -80, 50, 0); //边距
        PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream(pdfUrl));

        //字体设置
        Font font = null;
        BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
        font = new Font(bf, 13, Font.NORMAL);
        int rowNum = 0;
        int colNum = 0;
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(
                    new FileInputStream(new File(filePath)));
            HSSFSheet sheet = workbook.getSheetAt(0);
            int column = sheet.getRow(0).getLastCellNum();
            int row = sheet.getPhysicalNumberOfRows();

            //下面是找出表格中的空行和空列
            List<Integer> nullCol = new ArrayList<>();
            List<Integer> nullRow = new ArrayList<>();
            for (int j = 0; j < column; j++) {
                int nullColNum = 0;
                for (int i = 0; i < row; i++) {
                    HSSFCell cell = sheet.getRow(i).getCell(j);
                    if (cell == null || (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) && "".equals(cell.getStringCellValue())) {
                        nullColNum++;
                    }
                }
                if (nullColNum == row) {
                    nullCol.add(j);
                }
            }

            for (int i = 0; i < row; i++) {
                int nullRowNum = 0;
                for (int j = 0; j < column; j++) {
                    HSSFCell cell = sheet.getRow(i).getCell(j);
//					String str = cell.getStringCellValue();
                    if (cell == null || (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) && "".equals(cell.getStringCellValue())) {
                        nullRowNum++;
                    }
                }
                if (nullRowNum == column) {
                    nullRow.add(i);
                }
            }
            PdfPTable table = new PdfPTable(column - sheet.getRow(0).getFirstCellNum());
            List<CellRangeAddress> ranges = sheet.getMergedRegions();

            PdfPCell cell1 = null;
            String str = null;
            for (int i = sheet.getFirstRowNum(); i < row; i++) {
                if (nullRow.contains(i)) { //如果这一行是空行，这跳过这一行
                    continue;
                }
                for (int j = sheet.getRow(0).getFirstCellNum(); j < column; j++) {
                    if (nullCol.contains(j)) { //如果这一列是空列，则跳过这一列
                        continue;
                    }
                    boolean flag = true;
                    HSSFCell cell = sheet.getRow(i).getCell(j);
                    if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                        str = cell.getNumericCellValue() + "";
                    } else {
                        str = cell.getStringCellValue();
                    }
                    for (CellRangeAddress range : ranges) { //合并的单元格判断和处理
                        if (j >= range.getFirstColumn() && j <= range.getLastColumn() && i >= range.getFirstRow()
                                && i <= range.getLastRow()) {
                            if (str == null || "".equals(str)) {
                                flag = false;
                                break;
                            }
                            rowNum = range.getLastRow() - range.getFirstRow() + 1;
                            colNum = range.getLastColumn() - range.getFirstColumn() + 1;
                            cell1 = mergeCell(str, font, rowNum, colNum);
                            table.addCell(cell1);
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        table.addCell(getPDFCell(str, font));
                    }
                }
            }

            workbook.close();
            document.open();
            document.add(table);
            document.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    * xlsx文件转pdf流
    * */
    public static byte[] xlsxToPdfStream(String filePath) throws DocumentException, IOException {
        byte[] result=null;
        Rectangle rectPageSize = new Rectangle(PageSize.A4);// 定义A3页面大小
        rectPageSize = rectPageSize.rotate(); //横版
        Document document = new Document(rectPageSize, -80, -80, 50, 0); //边距
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//构建字节输出流
        PdfWriter writer = PdfWriter.getInstance(document,baos);

        //字体设置
        Font font = null;
        BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
        font = new Font(bf, 13, Font.NORMAL);
        int rowNum = 0;
        int colNum = 0;
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            XSSFSheet sheet = workbook.getSheetAt(0);
            int column = sheet.getRow(0).getLastCellNum();
            int row = sheet.getPhysicalNumberOfRows();

            //下面是找出表格中的空行和空列
            List<Integer> nullCol = new ArrayList<>();
            List<Integer> nullRow = new ArrayList<>();
            for (int j = 0; j < column; j++) {
                int nullColNum = 0;
                for (int i = 0; i < row; i++) {
                    XSSFCell cell = sheet.getRow(i).getCell(j);
                    if (cell == null || (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) && "".equals(cell.getStringCellValue())) {
                        nullColNum++;
                    }
                }
                if (nullColNum == row) {
                    nullCol.add(j);
                }
            }

            for (int i = 0; i < row; i++) {
                int nullRowNum = 0;
                for (int j = 0; j < column; j++) {
                    XSSFCell cell = sheet.getRow(i).getCell(j);
//					String str = cell.getStringCellValue();
                    if (cell == null || (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) && "".equals(cell.getStringCellValue())) {
                        nullRowNum++;
                    }
                }
                if (nullRowNum == column) {
                    nullRow.add(i);
                }
            }
            PdfPTable table = new PdfPTable(column - sheet.getRow(0).getFirstCellNum());
            List<CellRangeAddress> ranges = sheet.getMergedRegions();

            PdfPCell cell1 = null;
            String str = null;
            for (int i = sheet.getFirstRowNum(); i < row; i++) {
                if (nullRow.contains(i)) { //如果这一行是空行，这跳过这一行
                    continue;
                }
                for (int j = sheet.getRow(0).getFirstCellNum(); j < column; j++) {
                    if (nullCol.contains(j)) { //如果这一列是空列，则跳过这一列
                        continue;
                    }
                    boolean flag = true;
                    XSSFCell cell = sheet.getRow(i).getCell(j);
                    if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                        str = cell.getNumericCellValue() + "";
                    } else {
                        str = cell.getStringCellValue();
                    }
                    for (CellRangeAddress range : ranges) { //合并的单元格判断和处理
                        if (j >= range.getFirstColumn() && j <= range.getLastColumn() && i >= range.getFirstRow()
                                && i <= range.getLastRow()) {
                            if (str == null || "".equals(str)) {
                                flag = false;
                                break;
                            }
                            rowNum = range.getLastRow() - range.getFirstRow() + 1;
                            colNum = range.getLastColumn() - range.getFirstColumn() + 1;
                            cell1 = mergeCell(str, font, rowNum, colNum);
                            table.addCell(cell1);
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        table.addCell(getPDFCell(str, font));
                    }
                }
            }

            workbook.close();
            document.open();
            document.add(table);
            document.close();
            writer.close();
            result=baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    /*
     * xls文件转pdf流
     * */
    public static byte[] xlsToPdfStream(String filePath) throws DocumentException, IOException {
        byte[] result=null;
        //设置页面大小
        Rectangle rectPageSize = new Rectangle(PageSize.A4);// 定义A3页面大小
        rectPageSize = rectPageSize.rotate(); //横版
        Document document = new Document(rectPageSize, -80, -80, 50, 0); //边距
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//构建字节输出流
        PdfWriter writer = PdfWriter.getInstance(document,baos);

        //字体设置
        Font font = null;
        BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
        font = new Font(bf, 13, Font.NORMAL);
        int rowNum = 0;
        int colNum = 0;
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(
                    new FileInputStream(new File(filePath)));
            HSSFSheet sheet = workbook.getSheetAt(0);
            int column = sheet.getRow(0).getLastCellNum();
            int row = sheet.getPhysicalNumberOfRows();

            //下面是找出表格中的空行和空列
            List<Integer> nullCol = new ArrayList<>();
            List<Integer> nullRow = new ArrayList<>();
            for (int j = 0; j < column; j++) {
                int nullColNum = 0;
                for (int i = 0; i < row; i++) {
                    HSSFCell cell = sheet.getRow(i).getCell(j);
                    if (cell == null || (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) && "".equals(cell.getStringCellValue())) {
                        nullColNum++;
                    }
                }
                if (nullColNum == row) {
                    nullCol.add(j);
                }
            }

            for (int i = 0; i < row; i++) {
                int nullRowNum = 0;
                for (int j = 0; j < column; j++) {
                    HSSFCell cell = sheet.getRow(i).getCell(j);
//					String str = cell.getStringCellValue();
                    if (cell == null || (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) && "".equals(cell.getStringCellValue())) {
                        nullRowNum++;
                    }
                }
                if (nullRowNum == column) {
                    nullRow.add(i);
                }
            }
            PdfPTable table = new PdfPTable(column - sheet.getRow(0).getFirstCellNum());
            List<CellRangeAddress> ranges = sheet.getMergedRegions();

            PdfPCell cell1 = null;
            String str = null;
            for (int i = sheet.getFirstRowNum(); i < row; i++) {
                if (nullRow.contains(i)) { //如果这一行是空行，这跳过这一行
                    continue;
                }
                for (int j = sheet.getRow(0).getFirstCellNum(); j < column; j++) {
                    if (nullCol.contains(j)) { //如果这一列是空列，则跳过这一列
                        continue;
                    }
                    boolean flag = true;
                    HSSFCell cell = sheet.getRow(i).getCell(j);
                    if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                        str = cell.getNumericCellValue() + "";
                    } else {
                        str = cell.getStringCellValue();
                    }
                    for (CellRangeAddress range : ranges) { //合并的单元格判断和处理
                        if (j >= range.getFirstColumn() && j <= range.getLastColumn() && i >= range.getFirstRow()
                                && i <= range.getLastRow()) {
                            if (str == null || "".equals(str)) {
                                flag = false;
                                break;
                            }
                            rowNum = range.getLastRow() - range.getFirstRow() + 1;
                            colNum = range.getLastColumn() - range.getFirstColumn() + 1;
                            cell1 = mergeCell(str, font, rowNum, colNum);
                            table.addCell(cell1);
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        table.addCell(getPDFCell(str, font));
                    }
                }
            }

            workbook.close();
            document.open();
            document.add(table);
            document.close();
            writer.close();
            result=baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
