package model;

import controller.TextController;
import image.Pixel;
import java.io.IOException;


/**
 * This class is a model that contains the methods to
 * apply Filters and/or Transformations on an image.
 * This class is extended the ability to import/export different
 * image files such as PPM, PNG, and JPEG.
 */

public class ImageProcessingModel implements IEdit  {

  private Pixel[][] img;
  private int rowLength;
  private int columnLength;
  private int maxValue;

  /**
   * Contructor that makes a checkerboard image.
   */
  public ImageProcessingModel() {
    this.columnLength = 100;
    this.rowLength = 100;

    //255 bc a checkerboard is only black/white
    this.maxValue = 255;
    this.img = generateCheckerBoard(columnLength, rowLength);
  }

  /**
   * Contructor that makes a checkerboard image with the specified dimensions.
   */
  public ImageProcessingModel(int columnLength, int rowLength) throws IllegalArgumentException {
    if (columnLength < 0) {
      throw new IllegalArgumentException("Invalid: Column length is less than 0.");
    }
    if (rowLength < 0) {
      throw new IllegalArgumentException("Invalid: Row length is less than 0.");
    }

    this.columnLength = columnLength;
    this.rowLength = rowLength;
    //255 bc a checkerboard is only black/white
    this.maxValue = 255;
    //randomly generates a checkerboard when no image is given
    this.img = generateCheckerBoard(columnLength, rowLength);
  }

  /**
   * Contructor with an image given.
   */
  public ImageProcessingModel(Pixel[][] img) throws IllegalArgumentException {
    if (img == null) {
      throw new IllegalArgumentException("Image given is null");
    }

    try {
      this.rowLength = img[0].length;
      this.columnLength = img.length;
      this.maxValue = getMaxValue(img, rowLength, columnLength);
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Invalid Image was provided");
    }

    this.img = clamp(img);
  }

  public int getRowLength() {
    return rowLength;
  }

  public int getColumnLength() {
    return columnLength;
  }

  public int getMaxValue() {
    return maxValue;
  }

  /**
   * Contructor that makes an image from the filename given.
   */
  public ImageProcessingModel(String filename) throws IllegalArgumentException, IOException {
    if (filename == null || filename.equals("")) {
      throw new IllegalArgumentException("File name given is null");
    }
    //should return the model to the controller to pass to the view
    TextController controller = new TextController(filename);
    this.img = controller.goToImport();
    this.rowLength = img[0].length;
    this.columnLength = img.length;
    this.maxValue = maxValue;
  }

  /**
   * This method determines the max value of any of the colors in a 2D array of pixels.
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
   *  This method creates a Checker Board.
   * @param column column length
   * @param row row length
   * @return an array of pixels that represent the image of a checkered board
   * @throws IllegalArgumentException when column or row is negative
   */

  private Pixel[][] generateCheckerBoard(int column, int row)
      throws IllegalArgumentException {

    if ((row <= 0) || (column <= 0)) {
      throw new IllegalArgumentException("The width and height entered is incorrect");
    }

    //assuming each pixel is 1cm and color alternates at each pixel
    Pixel[][] checkBoard = new Pixel[column][row];
    for (int currentCol = 0; currentCol < column; currentCol++) {
      for (int currentRow = 0; currentRow < row; currentRow++) {
        Pixel pixel;
        //if the column is even...
        if (currentCol % 2 == 0) {
          //...and the row is even -- black
          if (currentRow % 2 == 0) {
            pixel = new Pixel(currentCol, currentRow, 255, 255, 255);
          } else {   //...and the row is even -- white
            pixel = new Pixel(currentCol, currentRow, 0, 0, 0);
          }
          //else, the column is odd...
        } else {
          //...and the row is even -- white
          if (currentRow % 2 == 0) {
            pixel = new Pixel(currentCol, currentRow, 0, 0, 0);
            //...and the row is even -- black
          } else {
            pixel = new Pixel(currentCol, currentRow, 255, 255, 255);
          }
        }
        checkBoard[currentCol][currentRow] = pixel;
      }
    }
    return checkBoard;
  }

  @Override
  public Pixel[][] blur() {
    double[][] kernel = makeKernalForBlur();
    this.img = clamp(applyingKernelToEachPixel(img, kernel));
    return this.img;
  }

  @Override
  public Pixel[][] sharpen() {
    double[][] kernel = makeKernelForSharpen();
    this.img = clamp(applyingKernelToEachPixel(img, kernel));
    return this.img;
  }

  /**
   * This is a helper method for blur() and sharpen() to apply a kernel to each pixel.
   * @param img the 2D array of an image
   * @param kernel kernel to apply
   * @return the image with the kernel applied
   */
  private Pixel[][] applyingKernelToEachPixel(Pixel[][] img, double[][] kernel) {
    int kernelRows = kernel[0].length;
    int kernelColumns = kernel.length;
    Pixel[][] newImage = new Pixel[columnLength][rowLength];
    int kernelRadius = kernelColumns / 2;

    for (int currentCol = 0; currentCol < columnLength; currentCol++) {
      for (int currentRow = 0; currentRow < rowLength; currentRow++) {
        double redSum = 0;
        double greenSum = 0;
        double blueSum = 0;


        for (int currentKernelCol = 0; currentKernelCol < kernelColumns; currentKernelCol++) {
          for (int currentKernelRow = 0; currentKernelRow < kernelRows; currentKernelRow++) {

            int colTemp = currentCol + currentKernelCol - kernelRadius;
            int rowTemp = currentRow + currentKernelRow - kernelRadius;

            if ((colTemp >= 0)
                && (rowTemp >= 0)
                && (colTemp < columnLength)
                && (rowTemp < rowLength)) {
              redSum += img[colTemp][rowTemp].getRedValue()
                  * kernel[currentKernelRow][currentKernelCol];
              greenSum += (img[colTemp][rowTemp].getGreenValue()
                  * kernel[currentKernelRow][currentKernelCol]);
              blueSum += (img[colTemp][rowTemp].getBlueValue()
                  * kernel[currentKernelRow][currentKernelCol]);
            }
          }
        }

        newImage[currentCol][currentRow] = new Pixel(currentCol, currentRow,
            (int)redSum,
            (int)(greenSum),
            (int)(blueSum));
      }
    }
    return newImage;
  }

  /**
   * Makes the default kernel for blurring the image.
   * @return the kernel
   */
  private double[][] makeKernalForBlur() {
    double[][] kernel = new double[3][3];
    double oneOverSixteen = 0.0625;
    double oneOverEight = 0.125;
    double oneOverFour = 0.25;

    kernel[0][0] = oneOverSixteen;
    kernel[0][1] = oneOverEight;
    kernel[0][2] = oneOverSixteen;

    kernel[1][0] = oneOverEight;
    kernel[1][1] = oneOverFour;
    kernel[1][2] = oneOverEight;

    kernel[2][0] = oneOverSixteen;
    kernel[2][1] = oneOverEight;
    kernel[2][2] = oneOverSixteen;

    return kernel;
  }

  /**
   * makes the default kernel for sharpening the image.
   * @return the kernel
   */
  private double[][] makeKernelForSharpen() {
    double[][] kernel = new double[5][5];
    double oneOverFour = 0.25;
    double negOneOverEight = -0.125;

    for (int currentCol = 0; currentCol < 5; currentCol ++) {
      for (int currentRow = 0; currentRow < 5; currentRow++) {
        if ((currentCol == 0) || (currentRow == 0) || (currentCol == 4) || (currentRow == 4)) {
          kernel[currentCol][currentRow] = negOneOverEight;
        } else if ((currentCol == 2) && (currentRow == 2)) {
          kernel[currentCol][currentRow] = 1;
        } else {
          kernel[currentCol][currentRow] = oneOverFour;
        }
      }
    }
    return kernel;
  }

  /**
   * clamping the values of the image to stay within the range of 0-255.
   * @param  img the 2D list of pixels given as an image.
   * @return clamped values of each r,g,b components of each pixel of the image.
   */
  public Pixel[][] clamp(Pixel[][] img) {
    for (int i = 0; i < columnLength; i++) {
      for (int j = 0; j < rowLength; j++) {
        //clamping the red values
        if (img[i][j].getRedValue() < 0) {
          img[i][j].setRedValue(0);
        } else if (img[i][j].getRedValue() > 255) {
          img[i][j].setRedValue(255);
        }
        //clamping the green values
        if (img[i][j].getGreenValue() < 0) {
          img[i][j].setGreenValue(0);
        } else if ( img[i][j].getGreenValue() > 255) {
          img[i][j].setGreenValue(255);
        }
        //clamping the blue values
        if (img[i][j].getBlueValue() < 0) {
          img[i][j].setBlueValue(0);
        } else if ( img[i][j].getBlueValue() > 255) {
          img[i][j].setBlueValue(255);
        }
      }
    }
    return img;
  }

  @Override
  public Pixel[][] greyscale() {
    Pixel[][] newImage = new Pixel[columnLength][rowLength];
    for (int i = 0; i < columnLength; i++) {
      for (int j = 0; j < rowLength; j++) {
        Pixel pixel = img[i][j];
        //0.2126𝑟+0.7152𝑔+0.0722𝑏
        int red = (int) Math.round(pixel.getRedValue() * 0.2126);
        int green = (int) Math.round(pixel.getGreenValue() * 0.7152);
        int blue = (int) Math.round(pixel.getBlueValue() * 0.0722);
        int total = red + blue + green;
        Pixel temp = new Pixel(i, j, total, total, total);
        newImage[i][j] = temp;
      }
    }
    return clamp(newImage);
  }

  @Override
  public Pixel[][] sepiaTone() {
    return separatePixels(img);
  }

  /**
   * extract the next three pixels in order and sends them to the.
   * classes respectively based on their position to be altered.
   * @param img the image on which the filter needs to be applied.
   * @return    the new image which has been altered according to the guidelines
   */
  private Pixel[][] separatePixels(Pixel[][] img) {
    Pixel[][] newImage = new Pixel[columnLength][rowLength];
    for (int i = 0; i < columnLength; i++) {
      for (int j = 0; j < rowLength; j++) {
        newImage[i][j] = applySepiaPixel(img[i][j], i, j);
      }
    }
    return newImage;
  }

  /**
   * helper method that applies the set of values to the rgb pixel and returns a new pixel.
   * @param pixel receives ever first pixel from the image
   * @return      returns a new pixel formed with the altered colors of the older one
   */
  private Pixel applySepiaPixel(Pixel pixel, int i, int j) {
    int redValue = (int) Math.round((pixel.getRedValue() * 0.393)
        + (pixel.getGreenValue() * 0.769)
        + (pixel.getBlueValue() * 0.189));
    int greenValue = (int) Math.round((pixel.getRedValue() * 0.349)
        + (pixel.getGreenValue() * 0.686)
        + (pixel.getBlueValue() * 0.168));
    int blueValue = (int) Math.round((pixel.getRedValue() * 0.272)
        + (pixel.getGreenValue() * 0.534)
        + (pixel.getBlueValue() * 0.131));

    return new Pixel(i, j, redValue, greenValue, blueValue);
  }

  /**
   * makes a deep copy of _this_ img.
   * @return returns the deep copy created
   */
  public Pixel[][] makeDeepCopy() {
    Pixel[][] imgCopy = new Pixel[columnLength][rowLength];
    for (int i = 0; i < columnLength; i++) {
      for (int j = 0; j < rowLength; j++) {
        imgCopy[i][j] = new Pixel(i, j,
            img[i][j].getRedValue(),
            img[i][j].getGreenValue(),
            img[i][j].getBlueValue());
      }
    }
    return imgCopy;
  }

}
