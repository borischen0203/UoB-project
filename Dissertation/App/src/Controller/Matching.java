package src.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.highgui.HighGui;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javafx.concurrent.Task;
import src.Model.Standard;
import src.View.App;

/**
 * 
 * This matching class mainly handle matching function, such as template
 * matching. This class build base on openCv library
 * 
 * @author Yi-Ming Chen
 * @version 2020-06-23
 * @reference 1.https://github.com/opencv/opencv
 *            2.https://riptutorial.com/opencv/example/22915/template-matching-with-java
 *            3.https://ppt.cc/fulB2x
 * 
 */

public class Matching extends Task<List<Standard>> {
    public static String filePath = "C:\\Users\\Boris\\Desktop\\Computer science\\Final Project\\UOB_Projects\\yxc1016\\Application\\";
    public static String filePath400 = "C:\\Users\\Boris\\Desktop\\Computer science\\Final Project\\UOB_Projects\\yxc1016\\Application\\Standard logo 400\\";
    public String excelPath = App.excel;
    public String pngPath = App.png;
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * 
     * This method mainly handle Template matching function
     * 
     * @reference https://reurl.cc/odem4q
     * 
     */
    @Override
    protected List<Standard> call() throws Exception {
        // fileManager.getDataFromExcel("Label_checklist v4.xls");
        fileManager.getDataFromExcel(excelPath);
        final List<Standard> sl = fileManager.standardList;
        System.out.println(sl.size());
        // Mat source = Imgcodecs.imread(filePath400 + "Label_noSRGB.png");
        Mat source = Imgcodecs.imread(pngPath);
        int i = 0;
        int count = sl.size();
        for (Standard s : sl) {
            if (isCancelled()) {
                count = 0;
                break;
            }
            Mat template = s.getSample();
            templateMatch(s, source, template);
            fileManager.writeExcel(s, excelPath);
            System.out.println(s.toString());
            i++;
            this.updateProgress(i, count);
            this.updateMessage("Items" + "(" + i + "/" + count + "):" + s.getItem());
            // System.out.println(map.size());
        }
        fileManager.writeImg(excelPath, source);
        // Imgcodecs.imwrite(filePath400 + "OutputExcelImg.png", source);
        return sl;
    }

    /**
     * (For report image purpose)This method mainly handle Template matching
     * function
     * 
     * @param excelPath  the Template image which extract from excel
     * @param SourcePath the png file path which user import
     */
    public static void match(String excelPath, String SourcePath) {
        fileManager.getDataFromExcel(excelPath);
        List<Standard> sl = fileManager.standardList;
        System.out.println(sl.size());
        Mat source = Imgcodecs.imread(SourcePath);
        for (Standard s : sl) {
            long startTime = System.currentTimeMillis();
            Mat template = s.getSample();
            templateMatch(s, source, template);
            fileManager.writeExcel(s, excelPath);
            long endTime = System.currentTimeMillis();
            System.out.println((endTime - startTime) / 1000.000);
            System.out.println(s.toString());
        }
        fileManager.writeImg(excelPath, source);
        // Imgcodecs.imwrite(filePath400 + "OutputExcelImg.png", source);

    }

    /**
     * (For report image purpose)This method mainly draw duplicate Matching Drawing.
     * 
     * @param excelPath  the Template image which extract from excel
     * @param SourcePath the png file path which user import
     */
    public void duplicateMatchDrawing(String excelPath, String SourcePath) {
        fileManager.getDataFromExcel(excelPath);
        List<Standard> sl = fileManager.standardList;
        System.out.println(sl.size());
        Mat source = Imgcodecs.imread(SourcePath);
        for (Standard s : sl) {
            Mat template = s.getSample();
            Mat result = Mat.zeros(source.rows() - template.rows() + 1, source.cols() - template.cols() + 1,
                    CvType.CV_32FC1);

            int machMethod = Imgproc.TM_SQDIFF_NORMED;
            Imgproc.matchTemplate(source, template, result, machMethod);
            MinMaxLocResult mmr = Core.minMaxLoc(result);
            Point matchLoc = mmr.minLoc;
            double rate = (1.0 - mmr.minVal) * 100;

            s.setSimilarity_rate(String.format("%.2f", rate) + "%");
            if (rate > 80) {
                s.setResult("Pass");
                Imgproc.rectangle(source, matchLoc,
                        new Point(matchLoc.x + template.cols(), matchLoc.y + template.rows()), new Scalar(0, 0, 255));
                fileManager.writeExcel(s, excelPath);
            } else {
                s.setResult("Fail");
                s.setNote("Not exist");
                fileManager.writeExcel(s, excelPath);
            }
            System.out.println(s.toString());
        }
        Imgcodecs.imwrite(filePath + "result.png", source);
    }

    /**
     * This method mainly handle Template matching function.
     * 
     * @param s        The Standard object
     * @param source   source image which user import
     * @param template template image which extract from excel
     */
    public static void templateMatch(Standard s, Mat source, Mat template) {
        Mat result = Mat.zeros(source.rows() - template.rows() + 1, source.cols() - template.cols() + 1,
                CvType.CV_32FC1);
        int machMethod = Imgproc.TM_SQDIFF_NORMED;
        Imgproc.matchTemplate(source, template, result, machMethod);
        MinMaxLocResult mmr = Core.minMaxLoc(result);
        Point matchLoc = mmr.minLoc;
        double rate = (1.0 - mmr.minVal) * 100;
        s.setSimilarity_rate(String.format("%.2f", rate) + "%");

        if (rate < 75) {
            // s.setResult("Poor");
            // s.setNote("Not exist");
            Repechage.findBestMatch(s, source, template);
        } else if (rate >= 75 && (s.getType().equals("text") || s.getType().equals("pattern+text"))) {
            s.setResult("Excellent");
            if (s.getType().equals("text")) {
                Point newPoint = new Point();
                newPoint.x = matchLoc.x - 2;
                newPoint.y = matchLoc.y - 2;
                Mat m = new Mat(source, new Rect(newPoint,
                        new Point(matchLoc.x + template.width() + 2, matchLoc.y + template.height() + 2)));
                textManager.compareChar(s, m, template);// check character and re-setResult
                // to poor(if Wrong)

            } else {// type is pattern+text
                Mat m = new Mat(source, new Rect(matchLoc,
                        new Point(matchLoc.x + template.width() + 1, matchLoc.y + template.height() + 1)));
                textManager.compareChar(s, m, template);// check character and re-setResult
                // to poor(if Wrong)
            }
        }
        // else if (s.getInputValue() != null && s.getType().equals("pattern")) {
        // s.setResult("Fail");
        // s.setNote("Please check type again.");
        // }
        else {
            s.setResult("Excellent");

        }
        if (s.getResult().equals("Poor") && s.getSimilarity_rate().compareTo("60%") > 0) {
            Imgproc.rectangle(source, matchLoc,
                    new Point(matchLoc.x + template.cols() - 1, matchLoc.y + template.rows() - 1),
                    new Scalar(0, 255, 0));
        } else if (s.getResult().equals("Excellent")) {
            Imgproc.rectangle(source, matchLoc,
                    new Point(matchLoc.x + template.cols() - 1, matchLoc.y + template.rows() - 1),
                    new Scalar(0, 255, 0));
        } else if (s.getResult().equals("Good")) {
            Imgproc.rectangle(source, matchLoc,
                    new Point(matchLoc.x + template.cols() - 1, matchLoc.y + template.rows() - 1),
                    new Scalar(255, 0, 0));
        }
    }

    /**
     * (For report image purpose)This method mainly handle Template matching
     * function
     * 
     * @param excelPath  the Template image which extract from excel
     * @param SourcePath the png file path which user import
     */
    public void singleMatch(String templatePath, String SourcePath) {
        try {
            Mat source = Imgcodecs.imread(SourcePath);
            Mat template = Imgcodecs.imread(templatePath);
            Mat result = Mat.zeros(source.rows() - template.rows() + 1, source.cols() - template.cols() + 1,
                    CvType.CV_32FC1);

            // Mat result = new Mat();
            // int result_cols = source.cols() - template.cols() + 1;
            // int result_rows = source.rows() - template.rows() + 1;
            // result.create(result_rows, result_cols, CvType.CV_32FC1);

            // int machMethod = Imgproc.TM_SQDIFF;
            int machMethod = Imgproc.TM_SQDIFF_NORMED;
            // int machMethod = Imgproc.TM_CCORR;
            // int machMethod = Imgproc.TM_CCORR_NORMED;
            // int machMethod = Imgproc.TM_CCOEFF;
            // int machMethod = Imgproc.TM_CCOEFF_NORMED;
            // for (int i = 0; i < 6; i++) {
            source = Imgcodecs.imread(SourcePath);
            Imgproc.matchTemplate(source, template, result, machMethod);
            // Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
            MinMaxLocResult mmr = Core.minMaxLoc(result);
            // Point matchLoc = i == 0 || i == 1 ? mmr.minLoc : mmr.maxLoc;
            Point matchLoc = mmr.minLoc;
            double rate = (1 - mmr.minVal) * 100;
            System.out.println(String.format("%.2f", rate) + "%");
            Imgproc.rectangle(source, matchLoc, new Point(matchLoc.x + template.cols(), matchLoc.y + template.rows()),
                    new Scalar(0, 0, 255), 3);
            Imgproc.rectangle(result, matchLoc, new Point(matchLoc.x + template.cols(), matchLoc.y + template.rows()),
                    new Scalar(0, 255, 0), 3);
            // result.convertTo(result, CvType.CV_32FC1, 255.0);
            // HighGui.namedWindow("source", 0);
            // HighGui.imshow("source", source);
            // HighGui.waitKey();
            // Imgcodecs.imwrite(filePath400 + "TM_CCOEFF_NORMED_Result.png", result);
            // Imgcodecs.imwrite(filePath400 + "\\test\\" + "0820" + ".png", source);
            // }
        } catch (CvException e) {
            e.printStackTrace();
        }
    }

    /**
     * (For report image purpose)This method mainly handle duplicate Template
     * matching function
     * 
     * @param excelPath  the Template image which extract from excel
     * @param SourcePath the png file path which user import
     */
    public void duplicateMatch(String templatePath, String SourcePath) {
        try {
            Mat source = Imgcodecs.imread(SourcePath);
            Mat template = Imgcodecs.imread(filePath + "kc 300.png");
            Mat result = Mat.zeros(source.rows() - template.rows() + 1, source.cols() - template.cols() + 1,
                    CvType.CV_32FC1);
            int machMethod = Imgproc.TM_SQDIFF_NORMED;
            // first match
            Imgproc.matchTemplate(source, template, result, machMethod);
            MinMaxLocResult mmr = Core.minMaxLoc(result);
            Point matchLoc = mmr.minLoc;
            double rate = (1.0 - mmr.minVal) * 100;
            System.out.println(String.format("%.2f", rate) + "%");
            Imgproc.rectangle(source, matchLoc, new Point(matchLoc.x + template.cols(), matchLoc.y + template.rows()),
                    new Scalar(0, 0, 255));
            // second match
            Mat template2 = Imgcodecs.imread(filePath + "Lenovo.png");
            Imgproc.matchTemplate(source, template2, result, machMethod);
            MinMaxLocResult mmr2 = Core.minMaxLoc(result);
            Point matchLoc2 = mmr2.minLoc;
            double rate2 = (1.0 - mmr.minVal) * 100;
            System.out.println(String.format("%.2f", rate2) + "%");
            Imgproc.rectangle(source, matchLoc2,
                    new Point(matchLoc2.x + template2.cols(), matchLoc2.y + template2.rows()), new Scalar(0, 0, 255));
            // HighGui.imshow("source", source);
            // HighGui.waitKey();
            Imgcodecs.imwrite(filePath + "result.png", source);
        } catch (CvException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("start");
        // String filePath = "C:\\Users\\Boris\\Desktop\\Computer science\\Final
        // Project\\UOB_Projects\\yxc1016\\Application\\";
        Matching mmm = new Matching();
        // mmm.singleMatch(filePath400 + "Lenovo.png", filePath400 + "Empty label.png");
        // mmm.duplicateMatch(filePath400 + "DANGER Warnings-word.png", filePath +
        // "Label_final.png");
        // mmm.match("Label_checklist v4.xls", filePath400 + "Label_noSRGB.png");
        // mmm.duplicateMatchDrawing("label_checklist.xls", filePath +
        // "Label_final.png");
        System.out.println("Finish");
    }
}
