package src;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import src.Controller.fileManager;
import src.Model.Standard;

/**
 * This class main to test the function requirement
 * 
 * @author Yi-Ming Chen
 * @version 2020-08-31
 */

public class FunctionalTest {
    public String testPath;
    public Standard s1;
    public Standard s2;

    @BeforeEach
    public void beforeEach() {
        s1 = new Standard(1, "EU", "pattern", "Excellent", "95%", "");
        s2 = new Standard(3, "s2", "text", "Poor", "80%", "wrong area:voltage");
        testPath = "Label_checklist_test";

    }

    // test No getter and setter
    @Test
    public void testNo() {
        int expected_1 = 1;
        int actual_1 = s1.getNo();
        assertEquals(expected_1, actual_1);
        s1.setNo(2);
        int expected_2 = 2;
        int actual_2 = 2;
        assertEquals(expected_2, actual_2);
    }

    // test No getter and setter
    @Test
    public void testItem() {
        String expected_1 = "EU";
        String actual_1 = s1.getItem();
        assertEquals(expected_1, actual_1);
        s1.setItem("BSMI");
        String expected_2 = "BSMI";
        String actual_2 = "BSMI";
        assertEquals(expected_2, actual_2);
    }

    // test Type getter and setter
    @Test
    public void testType() {
        String expected_1 = "pattern";
        String actual_1 = s1.getType();
        assertEquals(expected_1, actual_1);
        s1.setType("pattern");
        String expected_2 = "pattern";
        String actual_2 = "pattern";
        assertEquals(expected_2, actual_2);
    }

    // test Result getter and setter
    @Test
    public void testResult() {
        String expected_1 = "Excellent";
        String actual_1 = s1.getResult();
        assertEquals(expected_1, actual_1);
        s1.setType("Poor");
        String expected_2 = "Poor";
        String actual_2 = "Poor";
        assertEquals(expected_2, actual_2);
    }

    // test Rate getter and setter
    @Test
    public void testRate() {
        String expected_1 = "95%";
        String actual_1 = s1.getSimilarity_rate();
        assertEquals(expected_1, actual_1);
        s1.setSimilarity_rate("");
        String expected_2 = "75%";
        String actual_2 = "75%";
        assertEquals(expected_2, actual_2);
    }

    // test Note getter and setter
    @Test
    public void testNote() {
        String expected_1 = "wrong area:voltage";
        String actual_1 = s2.getNote();
        assertEquals(expected_1, actual_1);
        s2.setNote("wrong area:discharge");
        String expected_2 = "wrong area:discharge";
        String actual_2 = "wrong area:discharge";
        assertEquals(expected_2, actual_2);
    }

    // test getDataFromExcel
    @Test
    public void getDataFromExcel() throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        fileManager.getDataFromExcel("Label_checklist_test.xls");
        Standard actual_1 = fileManager.standardList.get(0);
        Standard expected_1 = new Standard();
        expected_1.setNo(7);
        expected_1.setItem("Rating");
        expected_1.setType("text");
        expected_1.setInputValue(new String[] { "11.52V", "3660mAh", "42Wh", "358mAh", "4Wh" });
        assertEquals(expected_1, actual_1);
    }

}