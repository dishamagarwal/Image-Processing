package view;

import image.Pixel;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import model.ImageProcessingModel;

/**
 * This class makes the text view for the image.
 */

public class ImageTextView {
  ImageProcessingModel model;
  Pixel[][] img;
  int columnLength;
  int rowLength;
  int maxValue;

  /**
   * Constructs a text view from the given model.
   * @param model model that needs a text view.
   */
  public ImageTextView(ImageProcessingModel model) {
    this.model = model;
    img = model.makeDeepCopy();
    this.rowLength = img[0].length;
    this.columnLength = img.length;
    this.maxValue = getMaxValue(img, rowLength, columnLength);
  }

  /**
   *  This method determines the max value of any of the colors in a 2D array of pixels.
   * @param img the sequence of numbers for an image
   * @param rowLength the length of the rows
   * @param columnLength the length of the columns
   * @return the max value in the array
   *
   */
  private int getMaxValue(Pixel[][] img, int rowLength, int columnLength) {

    int max = 0;

    for (int i = 0;i < columnLength; i++) {
      for (int j = 0;j < rowLength; j++) {

        if (img[i][j].getRedValue() > max) {
          max = img[i][j].getRedValue();
        }

        if (img[i][j].getGreenValue() > max) {
          max = img[i][j].getGreenValue();
        }

        if (img[i][j].getBlueValue() > max) {
          max = img[i][j].getBlueValue();
        }
      }
    }
    return max;
  }

  /**
   * This method creates a PPM file from the image given, and names it the string given.
   *
   * @param imgToExport transformed/filtered image
   * @param fileName the name of the file
   */

  //--Idk if it should be public or private
  //need to check if there is another way besides writeBytes to write to the file
  //does export a file, when converted online to jpeg it did show a checkered board :D
  public void exportPPM(ImageProcessingModel imgToExport, String fileName) {

    //does output a file, but doesn't print the correct contents

    File output = new File(fileName + ".ppm");

    try {

      DataOutputStream out = new DataOutputStream(new FileOutputStream(output));

      //only prints when its a string...
      out.writeBytes("P3 \n ");

      System.out.println(this.columnLength);

      out.writeBytes(String.valueOf(columnLength));
      out.writeBytes("\n ");

      System.out.println(rowLength);

      out.writeBytes(String.valueOf(rowLength));
      out.writeBytes("\n ");

      //max value
      out.writeBytes(String.valueOf(maxValue));
      out.writeBytes("\n ");

      for (int currentCol = 0; currentCol < columnLength; currentCol++) {
        for (int currentRow = 0; currentRow < rowLength; currentRow++) {
          out.writeBytes(String.valueOf(img[currentCol][currentRow].getRedValue()));
          out.writeBytes("\n ");
          out.writeBytes(String.valueOf(img[currentCol][currentRow].getGreenValue()));
          out.writeBytes("\n ");
          out.writeBytes(String.valueOf(img[currentCol][currentRow].getBlueValue()));
          out.writeBytes("\n ");
        }
      }
      out.close();

    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to export PPM file.");
    }
  }

  /**
   * Takes the filename and imports it into a 2D array to be used by the program.
   * @param filename the name of the file to be converted to a 2D array of pixels
   * @return         the newly formed 2D array of piixels from the image provided.
   */
  public Pixel[][] importPPM(String filename) throws FileNotFoundException {
    Scanner sc;
    try {
      sc = new Scanner(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      throw new FileNotFoundException("This file was not found!");
      //  System.out.println("File "+filename+ " not found!");
      //  return null;
    }
    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s + System.lineSeparator());
      }
    }

    //now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());
    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      System.out.println("Invalid PPM file: plain RAW file should begin with P3");
    }
    int columns = sc.nextInt();
    System.out.println("Width of image: " + columns);
    int rows = sc.nextInt();
    System.out.println("Height of image: " + rows);
    maxValue = sc.nextInt();
    System.out.println("Maximum value of a color in this file (usually 256): " + maxValue);

    Pixel[][] image = new Pixel[columns][rows];

    for (int i = 0;i < columns;i++) {
      for (int j = 0;j < rows;j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        //System.out.println("Color of pixel ("+j+","+i+"): "+ r+","+g+","+b);

        image[i][j] = new Pixel(i, j, r, g, b);
      }
    }
    return image;
  }
}
