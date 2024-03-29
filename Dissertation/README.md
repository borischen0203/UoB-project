# **Detection App of Laptop Battery Rating Label drawing**

```
  The engineers wasted too much time to check Label contents by human review. Moreover, human review is not perfect. It 
could not guarantee 100% accurate. Even if the Label has a wrong alphabet, it may cause cost lost for the company. 
Therefore, using a tool to help detection is important—this project aimed to develop an application to detect the label 
drawing. With saving time, engineers can spend more time on product development.
```

## Instruction on how to run the app:

Step 1: Download [run.bat] and [DetectionApp.jar]

Step 2: Double click run.bat file

Step 3: Follow the Instruction to import an excel checklist and Label drawing with PNG format 
<p align="left">
    <img src="https://i.imgur.com/cx3dWfy.png" alt="Sample"  width="519" height="328">
    <p align="left">
</p>


Step 4: Push start Detection button after import the file, then you will see below display
<p align="left">
    <img src="https://i.imgur.com/9zyKefZ.png" alt="Sample"  width="591" height="201">
    <p align="left">
</p>


Step 5: Wait until the detection finished.

![Image of step5](https://i.imgur.com/EATqhyy.png)

Step 6: Close the scene and App. Check the feedback in checklist.


## Explanation for the checklist content in the first sheet

Result: This cell presents three different types which are excellent, good and poor for the detection result. 

Similarity rate: It shows the match rate of the template.

Note: This area displays incorrect text content of Label.
<p align="left">
    <img src="https://i.imgur.com/EB9eaZZ.png" alt="Sample"  width="602" height="142">
    <p align="left">
</p>

## Explanation for the checklist content in the second sheet
In the second sheet which presented a picture that the user can quickly find the correct and incorrect area in different colours.
<p align="left">
    <img src="https://i.imgur.com/9QzXhWA.png" alt="Sample"  width="587" height="327">
    <p align="left">
</p>


## Tech stack
- Java
- OpenCV
- Tesseract OCR
- Tess4j
- Apache POI
