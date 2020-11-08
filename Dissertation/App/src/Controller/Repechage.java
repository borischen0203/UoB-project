package src.Controller;

import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.imgproc.Imgproc;

import src.Model.Standard;

/**
 * This Repechage class mainly resize the fail detection item and find the best
 * match. This class build base on openCv library.
 * 
 * @author Yi-Ming Chen
 * @version 2020-07-25
 * @reference: https://github.com/opencv/opencv
 *
 */
public class Repechage {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * 
     * This is mainly find the best match of logo by scale up or scale down size.
     * 
     * @param s        the Standard
     * @param source   the source image which user import
     * @param template the template image which user import
     * @reference https://blog.csdn.net/sazass/article/details/89634427
     */
    public static void findBestMatch(Standard s, Mat source, Mat template) {
        Mat templateUp = template.clone();
        Mat templateDown = template.clone();
        double scaleDown = !s.getType().equals("text") ? 0.95 : 29.0 / 34.0;
        double scaleUp = !s.getType().equals("text") ? 1.05 : 34.0 / 29.0;
        int times = !s.getType().equals("text") ? 10 : 2;
        Mat bestTemplate = new Mat();
        double bestRate = 0.0;
        MinMaxLocResult mmr = new MinMaxLocResult();
        System.out.println("OG rows:" + template.rows() + ";cols:" + template.cols());
        for (int i = 1; i <= times; i++) {
            /**
             * To scale up the size 5%/once and re-match.
             */
            try {
                Imgproc.resize(templateUp, templateUp, new Size(), scaleUp, scaleUp, Imgproc.INTER_LINEAR_EXACT);
                int result_rows_up = source.rows() - templateUp.rows() + 1;
                int result_cols_up = source.cols() - templateUp.cols() + 1;
                Mat resultUp = Mat.zeros(result_rows_up, result_cols_up, CvType.CV_32FC1);
                int machMethod = Imgproc.TM_SQDIFF_NORMED;
                Imgproc.matchTemplate(source, templateUp, resultUp, machMethod);
                MinMaxLocResult mmr_up = Core.minMaxLoc(resultUp);
                double mmr_up_value = 1.0 - mmr_up.minVal;
                System.out.println("Scale Up " + i + " :" + mmr_up_value * 100);
                System.out.println(" rows:" + templateUp.rows() + ";cols:" + templateUp.cols() + "\n");

                /**
                 * To scale down the size 5%/once and re-match.
                 */
                Imgproc.resize(templateDown, templateDown, new Size(), scaleDown, scaleDown,
                        Imgproc.INTER_LINEAR_EXACT);
                int result_rows_down = source.rows() - templateDown.rows() + 1;
                int result_cols_down = source.cols() - templateDown.cols() + 1;
                Mat resultDown = Mat.zeros(result_rows_down, result_cols_down, CvType.CV_32FC1);
                Imgproc.matchTemplate(source, templateDown, resultDown, machMethod);
                MinMaxLocResult mmr_down = Core.minMaxLoc(resultDown);
                double mmr_down_value = 1.0 - mmr_down.minVal;
                System.out.println("Scale Down " + i + " :" + mmr_down_value * 100);
                System.out.println(" rows:" + templateDown.rows() + ";cols:" + templateDown.cols() + "\n");

                if (mmr_up_value > bestRate && mmr_up_value > mmr_down_value) {
                    bestTemplate = templateUp.clone();
                    mmr = mmr_up;
                } else if (mmr_down_value > bestRate && mmr_down_value > mmr_up_value) {
                    bestTemplate = templateDown.clone();
                    mmr = mmr_down;
                } else {

                }
                bestRate = Math.max(bestRate, Math.max(mmr_up_value, mmr_down_value));
            } catch (CvException e) {
                e.printStackTrace();
            }
        }
        Point matchLoc = mmr.minLoc;
        bestRate = bestRate * 100;
        s.setSimilarity_rate(String.format("%.2f", bestRate) + "%");
        if (bestRate < 60) {
            if (s.getItem().equals("IEC61960 type") && bestRate > 45) {
                s.setResult("Good");
                Point newPoint = new Point();
                newPoint.x = matchLoc.x - (s.getType().equals("text") ? 6 : 1);
                newPoint.y = matchLoc.y - 1;
                Mat m = new Mat(source, new Rect(newPoint,
                        new Point(matchLoc.x + bestTemplate.width() + 2, matchLoc.y + bestTemplate.height() + 2)));
                textManager.compareChar(s, m, bestTemplate);
            } else {
                s.setResult("Poor");
                s.setNote("Not exist");
                Imgproc.rectangle(source, matchLoc,
                        new Point(matchLoc.x + bestTemplate.cols() - 1, matchLoc.y + bestTemplate.rows() - 1),
                        new Scalar(0, 255, 0));
            }
        } else if (bestRate >= 60 && (s.getType().equals("text") || s.getType().equals("pattern+text"))) {
            s.setResult((bestRate >= 75) ? "Excellent" : "Good");
            if (s.getType().equals("text")) {// type is text
                Point newPoint = new Point();
                newPoint.x = matchLoc.x - (s.getType().equals("text") ? 6 : 1);
                newPoint.y = matchLoc.y - 1;
                Mat m = new Mat(source, new Rect(newPoint,
                        new Point(matchLoc.x + bestTemplate.width() + 2, matchLoc.y + bestTemplate.height() + 2)));
                textManager.compareChar(s, m, bestTemplate);

            } else {// type is pattern+text
                Mat m = new Mat(source, new Rect(matchLoc,
                        new Point(matchLoc.x + bestTemplate.width() + 2, matchLoc.y + bestTemplate.height() + 2)));
                textManager.compareChar(s, m, bestTemplate);

            }
        } else {
            s.setResult((bestRate >= 75) ? "Excellent" : "Good");
        }

        // /**
        // * Temp
        // *
        // */
        // if (s.getResult().equals("Poor")) {
        // Imgproc.rectangle(source, matchLoc,
        // new Point(matchLoc.x + bestTemplate.cols() - 1, matchLoc.y +
        // bestTemplate.rows() - 1),
        // new Scalar(0, 0, 255));
        // } else {
        // Point newPoint = new Point();
        // newPoint.x = matchLoc.x - 6;
        // newPoint.y = matchLoc.y - 1;
        // Imgproc.rectangle(source, newPoint,
        // new Point(matchLoc.x + bestTemplate.width() + 2, matchLoc.y +
        // bestTemplate.height() + 2),
        // new Scalar(0, 255, 0));
        // }
        // Imgcodecs.imwrite(filePath400 + "bestMatch.png", source);
        System.out.println("finish all");
    }

    public static void main(String[] args) {
        System.out.println("start");
        System.out.println("finish");

    }

}