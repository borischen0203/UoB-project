package src;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * 
 * This fileManager class mainly handle excel file, such as extract data from
 * excel and input data into excel. And the class build base on Apache POI
 * library.
 * 
 * @author Yi-ming Chen
 * @version 2020-06-23
 * @reference https://github.com/apache/poi
 */
public class fileManager {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public static int No = 0;
    public static int Item = 1;
    public static int Type = 3;
    public static int InputValue = 4;;
    public static int Result = 5;
    public static int Similarity_rate = 6;
    public static int Note = 7;
    public static List<Standard> standardList = new ArrayList<>();

    /**
     * This method return a value of image size which user import.
     * 
     * @param excelPath the excel file path which user import
     * @return A double value with image size
     */
    public static double getImgSize(String excelPath) {
        File file = new File(excelPath);
        double size = file.length() / 1024.0;
        return size;
    }

    /**
     * This method return a boolean vale that whether the import excel file is
     * opening.
     * 
     * @param excelPath the excel file path which user import
     * @return a boolean whether file is opening
     */
    public static boolean checkOpening(String excelPath) {
        Boolean result = false;
        File file = new File(excelPath);
        try {
            FileInputStream readFile = new FileInputStream(file);
            readFile.close();
            FileOutputStream checkOpenFile = new FileOutputStream(file, true);
            checkOpenFile.flush();
            checkOpenFile.close();
        } catch (IOException e) {
            result = true;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * This method return a boolean value to check whether user import a excel file
     * with empty content.
     * 
     * @param excelPath the excel file path which user import
     * @return a boolean whether file content is empty
     */
    public static boolean checkContent(String excelPath) {
        Boolean result = true;
        File file = new File(excelPath);
        try {
            FileInputStream readFile = new FileInputStream(file);
            HSSFWorkbook workbook = (HSSFWorkbook) WorkbookFactory.create(readFile);
            HSSFSheet sheet = workbook.getSheetAt(0);
            if (sheet.getLastRowNum() == 0 || sheet.getPhysicalNumberOfRows() == 0) {
                result = false;
            }
            workbook.close();
            readFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 
     * This method extract the data from the excel and store in a Array list.
     * 
     * @param excelPath the excel file path which user import
     * @reference https://reurl.cc/ex4roj
     */
    public static void getDataFromExcel(String excelPath) {
        File file = new File(excelPath);
        try {
            FileInputStream readFile = new FileInputStream(file);
            HSSFWorkbook workbook = (HSSFWorkbook) WorkbookFactory.create(readFile);
            HSSFSheet sheet = workbook.getSheetAt(0);
            int rowLength = sheet.getLastRowNum();
            for (int i = 1; i <= rowLength; i++) {
                Row row = sheet.getRow(i);
                Standard s = new Standard();
                s.setNo((int) row.getCell(No).getNumericCellValue());
                s.setItem(row.getCell(Item).getStringCellValue());
                s.setType(row.getCell(Type).getStringCellValue());
                String input = (row.getCell(InputValue).getStringCellValue());
                s.setInputValue((input.split("/|:|=|,|\\n+|\\s+")));
                standardList.add(s);// store the data in array list
                System.out.println(s.toString());
            }
            getImageMat(workbook, sheet);
            workbook.close();
            readFile.close();
            System.out.println("Read file Success!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method extract the image from excel and make them into a Mat type.
     * 
     * @param workbook workbook of excel
     * @param sheet    sheet of excel
     * @reference 1.https://blog.csdn.net/yh880610/article/details/9237269
     *            2.https://reurl.cc/Xk4xxe
     */
    public static void getImageMat(HSSFWorkbook workbook, HSSFSheet sheet) {
        List<HSSFPictureData> pictures = workbook.getAllPictures();
        for (HSSFShape shape : sheet.getDrawingPatriarch().getChildren()) {
            HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();
            HSSFPicture pic = (HSSFPicture) shape;
            int Number = anchor.getRow1();
            int pictureIndex = pic.getPictureIndex() - 1;// To get the index in list<>
            HSSFPictureData picData = pictures.get(pictureIndex);
            byte[] data = picData.getData();
            Mat mat = Imgcodecs.imdecode(new MatOfByte(data), Imgcodecs.CV_LOAD_IMAGE_COLOR);
            Standard s = standardList.get(Number - 1);
            s.setSample(mat);
        }
        System.out.println("Get Image Mat Success");
    }

    /**
     * 
     * This method input the reference image into excel .
     * 
     * @param exCelPath the excel file path which user import
     * @param m         the reference image of result
     * @reference https://blog.csdn.net/joyous/article/details/9664739
     */
    public static void writeImg(String exCelPath, Mat m) {
        File file = new File(exCelPath);
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".png", m, mob);
        try {
            FileInputStream readFile = new FileInputStream(file);
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            BufferedImage bf = ImageIO.read(new ByteArrayInputStream(mob.toArray()));
            ImageIO.write(bf, "png", byteArrayOut);// make image into byte array
            HSSFWorkbook workbook = (HSSFWorkbook) WorkbookFactory.create(readFile);
            readFile.close();
            Sheet sheet = workbook.getSheetAt(1);
            HSSFPatriarch patriarch = (HSSFPatriarch) sheet.createDrawingPatriarch();
            int col = 1;
            int row = 5;
            HSSFClientAnchor anchor = new HSSFClientAnchor(10, 10, 1013, 245, (short) col, row, (short) (col + 12),
                    row + 13);
            patriarch.createPicture(anchor,
                    workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));

            FileOutputStream writeFile = new FileOutputStream(file);
            workbook.write(writeFile);
            writeFile.flush();
            workbook.close();
            System.out.println("Write image Success!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method input the feedback into excel.
     * 
     * @reference https://reurl.cc/d0jYeV
     */
    public static void writeExcel(Standard s, String exCelPath) {
        File file = new File(exCelPath);
        try {
            FileInputStream readFile = new FileInputStream(file);
            // transfer file for POI to operate
            HSSFWorkbook workbook = (HSSFWorkbook) WorkbookFactory.create(readFile);
            readFile.close();
            // get the sheet from excel
            Sheet sheet = workbook.getSheetAt(0);
            // get the row
            Row row = sheet.getRow(s.getNo());
            // get the cell
            Cell res = row.getCell(Result);
            Cell rate = row.getCell(Similarity_rate);
            Cell note = row.getCell(Note);

            // set value for cell;
            res.setCellValue(s.getResult());

            /**
             * set color need to set other font together Or the form will change
             * 
             */
            CellStyle style = setCol(s, workbook);
            res.setCellStyle(style);

            rate.setCellValue(s.getSimilarity_rate());
            note.setCellValue(s.getNote());

            // auto width size
            if (s.getNote() != null) {
                sheet.autoSizeColumn(Note);
                sheet.setColumnWidth(Note, sheet.getColumnWidth(Note) * 11 / 10);
            }
            FileOutputStream writeFile = new FileOutputStream(file);
            workbook.write(writeFile);
            workbook.close();
            writeFile.flush();
            writeFile.close();
            System.out.println("Write Success!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set font style for the Result colum, such as Font name, Font size and Font
     * color
     * 
     * @param s        the Standard
     * @param workbook workbook of excel
     * @return A CellStyle
     */
    public static CellStyle setCol(Standard s, HSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 11); // Height
        font.setFontName("新細明體"); // font
        font.setBold(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        if (s.getResult().equals("Excellent")) {
            font.setColor(IndexedColors.GREEN.getIndex()); // color
        } else if (s.getResult().equals("Good")) {
            font.setColor(IndexedColors.BLUE.getIndex());
        } else {
            font.setColor(IndexedColors.RED.getIndex());
        }
        style.setFont(font);
        return style;
    }
}