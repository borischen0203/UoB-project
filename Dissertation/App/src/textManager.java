package src;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.image.BufferedImage;
import net.sourceforge.tess4j.*;

/**
 * This textManager class mainly handle character recognition. This class build
 * base on tess4j library
 * 
 * @author Yi-Ming Chen
 * @version 2020-07-25
 * @reference https://github.com/nguyenq/tess4j
 * 
 */
public class textManager {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * 
     * This method is to compare the two text contents which extract from source
     * image and template image.
     * 
     * @param s           the Standard
     * @param mFromSource the Mat which extract from source image
     * @param template    the template which extract from excel
     * 
     * 
     */
    public static void compareChar(Standard s, Mat matFromInput, Mat matFromStandard) {
        String[] inputString = charRecognition(s, matFromInput).split("[\\p{Punct}\\s&&[^.]]+");
        String[] standardString = makeStandardString(s, charRecognition(s, matFromStandard));
        System.out.println("inputString   :" + Arrays.toString(inputString));
        System.out.println("standardString:" + Arrays.toString(standardString));
        int p = 0;
        int q = 0;
        boolean isFail = false;
        boolean isFirst = true;
        StringBuffer sb = new StringBuffer("Wrong area:");
        while (p < standardString.length) {
            if (q >= inputString.length) {
                String ss = (isFirst) ? "" : ", ";
                sb.append(ss + standardString[p]);
                isFail = true;
                isFirst = false;
            } else if (!standardString[p].equals(inputString[q])) {
                String ss = (isFirst) ? "" : ", ";
                sb.append(ss + standardString[p]);
                isFail = true;
                isFirst = false;
            }
            p++;
            q++;
        }
        if (isFail) {
            s.setResult("Poor");
            s.setNote(sb.toString());
        } else {
            s.setNote("");
        }
    }

    /**
     * 
     * This method return characters of Recognition with String type
     * 
     * @param s the Standard
     * @param m the mat which extract from excel and user import
     * @return characters of Recognition as String
     * @reference https://stackoverflow.com/questions/14958643/converting-bufferedimage-to-mat-in-opencv
     */
    public static String charRecognition(Standard s, Mat matFromInput) {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".png", matFromInput, mob);
        String input = null;
        try {
            BufferedImage bf = ImageIO.read(new ByteArrayInputStream(mob.toArray()));
            ITesseract instance = new Tesseract();
            instance.setDatapath("tessdata");
            switch (s.getItem()) {
                default: {
                    instance.setLanguage("hybrid.v1");
                    break;
                }
            }
            input = instance.doOCR(bf);
            String result = "";
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                if (Character.isIdeographic(c)) {
                    result = result + ' ' + c;
                } else {
                    result += c;
                }
            }
            input = result;
            System.out.println("result: ");
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return input;
    }

    /**
     * This method extract inputValue from excel and combine with Template.
     * 
     * @param s        the Standard
     * @param template the template which extract from excel
     */
    public static String[] makeStandardString(Standard s, String stringFromStandard) {
        String[] template = stringFromStandard.split("[\\p{Punct}\\s&&[^.]]+");
        if (s.getInputValue().length != 0 || s.getInputValue() != null) {
            String[] s1 = s.getInputValue();
            switch (s.getItem()) {
                case "Model":
                    template[template.length - 1] = s1[0];
                    break;
                default: {
                    int s1Index = 0;
                    // change value in template to inputValue(From s)
                    for (int i = 0; i < template.length; i++) {
                        Pattern p = Pattern.compile("V|mAh|Wh");
                        Matcher m = p.matcher(template[i]);
                        if (m.find()) {
                            template[i] = s1[s1Index];
                            s1Index++;
                        }
                    }
                    break;
                }
            }
        }
        return template;
    }

    public static void main(String[] args) {
    }
}