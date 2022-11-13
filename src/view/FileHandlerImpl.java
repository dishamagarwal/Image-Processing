package view;

import image.Pixel;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 * This class implements all the methods with exporting/importing files.
 */

public class FileHandlerImpl implements FileHandler {

  //ppm -> pixels -> jpeg
  //jpeg -> pixels -> ppm

  Pixel[][] imgToExport;
  String filename;
  String path;

  public FileHandlerImpl(Pixel[][] imgToExport) {
    this.imgToExport = imgToExport;
  }

  public FileHandlerImpl(String fileName) {
    this.filename = fileName;
  }

  @Override
  public Pixel[][] importPPM() throws FileNotFoundException {
    Scanner sc;
    try {
      sc = new Scanner(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      throw new FileNotFoundException("This file was not found!");
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
    int maxValue = sc.nextInt();
    System.out.println("Maximum value of a color in this file (usually 256): " + maxValue);

    Pixel[][] image = new Pixel[columns][rows];

    for (int i = 0; i < columns ; i++) {
      for (int j = 0; j < rows; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        //System.out.println("Color of pixel ("+j+","+i+"): "+ r+","+g+","+b);
        image[i][j] = new Pixel(i, j, r, g, b);
      }
    }
    return image;
  }

  @Override
  public Pixel[][] importImage() throws IOException {

    BufferedImage img = null;
    File imgFile = new File(filename);


    if (imgFile.exists()) {
      try {
        img = ImageIO.read(imgFile);
      } catch (IOException e) {
        throw new IOException("Unable to read file");
      }
    }

    if (img == null) {
      throw new IllegalArgumentException("Buffered Image is null.");
    }

    int rows = img.getHeight();
    int cols = img.getWidth();
    Pixel[][] imageArray = new Pixel[cols][rows];

    for (int i = 0; i < cols ; i++) {
      for (int j = 0; j < rows; j++) {

        int rgb = img.getRGB(i, j);

        Color color = new Color(rgb);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        imageArray[i][j] = new Pixel(i, j, r, g, b);
      }
    }

    return imageArray;

  }

  @Override
  public void exportPPM(String fileName) {

    //does output a file, but doesn't print the correct contents

    File output = new File(fileName + ".ppm");

    try {

      DataOutputStream out = new DataOutputStream(new FileOutputStream(output));

      //only prints when its a string...
      out.writeBytes("P3 \n ");

      out.writeBytes(String.valueOf(imgToExport.length));
      out.writeBytes("\n ");


      out.writeBytes(String.valueOf(imgToExport[0].length));
      out.writeBytes("\n ");

      //max value
      int maxValue = getMaxValue(imgToExport, imgToExport.length, imgToExport[0].length);
      out.writeBytes(String.valueOf(maxValue));
      out.writeBytes("\n ");

      Pixel[][] img = imgToExport;

      for (int currentCol = 0; currentCol < imgToExport.length; currentCol++) {
        for (int currentRow = 0; currentRow < imgToExport[0].length; currentRow++) {
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


  @Override
  public void exportImage(String filename, String type) {

    if (!type.equals("png") && !type.equals("jpeg")) {
      throw new IllegalArgumentException("Type of file is invalid");
    }

    File exportImage = new File(filename);

    int columns = imgToExport.length;
    int rows = imgToExport[0].length;

    BufferedImage imgExporting = new BufferedImage(columns, rows, BufferedImage.TYPE_INT_RGB);

    for (int i = 0;i < columns; i++) {
      for (int j = 0;j < rows; j++) {
        int r = imgToExport[i][j].getRedValue();
        int g = imgToExport[i][j].getRedValue();
        int b = imgToExport[i][j].getRedValue();
        Color currentPixelColor = new Color(r, g ,b);
        imgExporting.setRGB(i, j, currentPixelColor.getRGB());
      }
    }

    try {
      ImageIO.write(imgExporting, type, exportImage);
    } catch(IOException e) {
      throw new IllegalArgumentException("Unable to create file.");
    }

    path = exportImage.getAbsolutePath();
  }

  /**
   * This method determines the max value of any of the colors in a 2D array of pixels.
   * @param img the sequence of numbers for an image
   * @param rowLength the length of the rows
   * @param columnLength the length of the columns
   * @return the max value in the array
   *
   */
  private int getMaxValue(Pixel[][] img, int columnLength, int rowLength) {

    int max = 0;

    for (int i = 0; i < columnLength; i++) {
      for (int j = 0; j < rowLength; j++) {

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

  public String getPath() {
    return path;
  }
}
