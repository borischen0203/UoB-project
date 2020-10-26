package src;

import java.util.Arrays;
import org.opencv.core.Mat;

/**
 * 
 * This is Standard class.
 * 
 * @author Yi-ming Chen
 * @version 2020-06-23
 * 
 */
public class Standard {
    public int No;
    public String Item;
    public Mat Sample;
    public String Type;
    public String[] InputValue;
    public String Result;
    public String Similarity_rate;
    public String Note;

    /**
     * This constructor creates a Standard from the eight parts: No, Item,
     * sample,Type,InputValue, Result ,similarity rate and Note, which are an int, a
     * String, a Mat,a String ,a String[],a String,a String and a String,
     * respectively.
     * 
     * @param No              the No of standard as a integer
     * @param Item            the Item of standard as a String
     * @param sample          the sample logo of standard as a Mat
     * @param Type            the Type of standard as a String
     * @param InputValue      the inputValue of standard as a String[]
     * @param Result          the result of standard as a String
     * @param Similarity_rate the Note of standard as a String
     * @param Note            the Note of standard as a String
     */
    public Standard(int No, String Item, Mat Sample, String Type, String[] InputValue, String Result,
            String Similarity_rate, String Note) {
        this.No = No;
        this.Item = Item;
        this.Sample = Sample;
        this.Type = Type;
        this.InputValue = InputValue;
        this.Result = Result;
        this.Similarity_rate = Similarity_rate;
        this.Note = Note;
    }

    public Standard(int No, String Item, Mat Sample, String Result) {
        this.No = No;
        this.Item = Item;
        this.Sample = Sample;
        this.Result = Result;
    }

    public Standard() {

    }

    public Standard(int No, String Item, String Type, String Result, String Similarity_rate, String Note) {
        this.No = No;
        this.Item = Item;
        this.Type = Type;
        this.Result = Result;
        this.Similarity_rate = Similarity_rate;
        this.Note = Note;
    }

    public Standard(int No, String Item, String Type, String[] InputValue) {
        this.No = No;
        this.Item = Item;
        this.Type = Type;
        this.InputValue = InputValue;
    }

    /**
     * Getter for the No
     * 
     * @return The No of the standard is return
     */
    public int getNo() {
        return this.No;
    }

    /**
     * Setter for the No
     * 
     * @param No The new No of the update Standard
     */
    public void setNo(int No) {
        this.No = No;
    }

    /**
     * Getter for the Item
     * 
     * @return The Item of the standard is return
     */
    public String getItem() {
        return this.Item;
    }

    /**
     * Setter for the Item
     * 
     * @param Item The new Item of the update Standard
     */
    public void setItem(String Item) {
        this.Item = Item;
    }

    /**
     * Getter for the Sample
     * 
     * @return The sample of the standard is return
     */
    public Mat getSample() {
        return this.Sample;
    }

    /**
     * Setter for the Sample
     * 
     * @param Sample The new sample of the update Standard
     */
    public void setSample(Mat Sample) {
        this.Sample = Sample;
    }

    /**
     * Getter for the Type
     * 
     * @return The Type of the standard is return
     */
    public String getType() {
        return this.Type;
    }

    /**
     * Setter for the Type
     * 
     * @param Type The new type of the update Standard
     */
    public void setType(String Type) {
        this.Type = Type;
    }

    /**
     * Getter for the inputValue
     * 
     * @return The inputValue of the standard is return
     */
    public String[] getInputValue() {
        return this.InputValue;
    }

    /**
     * Setter for the inputValue
     * 
     * @param InputValue The new inputValue of the update Standard
     */
    public void setInputValue(String[] InputValue) {
        this.InputValue = InputValue;
    }

    /**
     * Getter for the result
     * 
     * @return The result of the standard is return
     */
    public String getResult() {
        return this.Result;
    }

    /**
     * Setter for the Result
     * 
     * @param Result The new result of the update Standard
     */
    public void setResult(String Result) {
        this.Result = Result;
    }

    /**
     * Getter for the similarity rate
     * 
     * @return The similarity rate of the standard is return
     */
    public String getSimilarity_rate() {
        return this.Similarity_rate;
    }

    /**
     * Setter for the similarity rate
     * 
     * @param Similarity_rate The new similarity rate of the update Standard
     */
    public void setSimilarity_rate(String Similarity_rate) {
        this.Similarity_rate = Similarity_rate;
    }

    /**
     * Getter for the Note
     * 
     * @return The Note of the standard is return
     */
    public String getNote() {
        return this.Note;
    }

    /**
     * Setter for the note
     * 
     * @param Note The new note of the update Standard
     */
    public void setNote(String Note) {
        this.Note = Note;
    }

    /**
     * A human readable description of the standard in form of the seven field
     * variables specifying it.
     */
    @Override
    public String toString() {
        return "{" + " No='" + getNo() + "'" + ", Item='" + getItem() + "'" + ", Type='" + getType() + "'"
                + ", InputValue='" + Arrays.asList(getInputValue()) + "'" + ", Result='" + getResult() + "'"
                + ", Similarity_rate='" + getSimilarity_rate() + "'" + ", Note='" + getNote() + "'" + "}";
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("Core.NATIVE_LIBRARY_NAME"));
    }
}