package model;

import image.Layer;
import image.Pixel;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the class that implements all the effects on an image from IEdit and IEditExtended.
 */


public class IEditExtendedImpl extends MultiLayeredImages implements IEditExtended {

  private final int rowLength;
  private final int columnLength;
  private List<Layer> layers;


  /**
   * Constructor for the class.
   */
  public IEditExtendedImpl() {
    //call parent class constructor
    super();
    this.columnLength = super.layers.get(0).image().length;
    this.rowLength = super.layers.get(0).image()[0].length;
    this.layers = super.layers;
  }

  /**
   * Constructor for the class with column and row of the image given.
   */

  public IEditExtendedImpl(int columnLength, int rowLength) throws FileNotFoundException {
    //call parent class constructor
    super(columnLength, rowLength);
    this.columnLength = super.layers.get(0).image().length;
    this.rowLength = super.layers.get(0).image()[0].length;
    this.layers = super.layers;
  }

  /**
   * Constructor for the class with the image itself given.
   */

  public IEditExtendedImpl(Pixel[][] image) {
    //call parent class constructor
    super(image);
    this.columnLength = super.layers.get(0).image().length;
    this.rowLength = super.layers.get(0).image()[0].length;
    this.layers = super.layers;
  }

  /**
   * Constructor for the class with the filename given.
   */

  public IEditExtendedImpl(String fileName) throws IOException {
    //call parent class constructor
    super(fileName);
    this.columnLength = super.layers.get(0).image().length;
    this.rowLength = super.layers.get(0).image()[0].length;
    this.layers = super.layers;
  }

  private boolean sameSize(List<Pixel[][]> images) {
    if (images.size() > 1) {
      for (int i = 0; i < images.size() - 1; i++) {
        if ((images.get(i).length != images.get(i + 1).length) ||
            (images.get(i)[0].length != images.get(i + 1)[0].length)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * checks for the largest image dimensions and then calls the downscaling function using those.
   * @param images the list of images that need to be downscaled
   */
  private void gettingLargestImage(List<Pixel[][]> images) {
    int width = images.get(0)[0].length;
    int height = images.get(0).length;
    int wTemp = images.get(0)[0].length;
    int hTemp = images.get(0).length;
    for (int i = 0; i < images.size() - 1; i++) {
      int img1dimensions = images.get(i).length * images.get(i)[0].length;
      int img2dimensions = images.get(i + 1).length * images.get(i + 1)[0].length;
      if (img1dimensions < img2dimensions) {
        width = images.get(i + 1)[0].length;
        height = images.get(i + 1).length;
      }
    }
    if ((width == wTemp) && (height == hTemp)) {
      //the layers are the largest
      //removing the image that was part of the layers
      images.remove(0);
      downscaling(images, rowLength, columnLength);
    }
    downscaling(width, height);
  }

  //downscales the layers to the size provided
  @Override
  public List<Pixel[][]> downscaling(int width, int height) {
    if ((width * height) > (columnLength * rowLength)) {
      throw new IllegalArgumentException("The images are already smaller than the size");
    }

    List<Pixel[][]> imagesOfLayers = new ArrayList<>();
    for (Layer layer : layers) {
      imagesOfLayers.add(layer.image());
    }
    layers.removeAll(layers);
    return downscaling(imagesOfLayers, width, height);
  }

  //downscales the img & layers to the size provided
  @Override
  public List<Pixel[][]> downscaling(List<Pixel[][]> img, int width, int height) {
    if ((columnLength * rowLength) > width * height) {
      for (Pixel[][] pixels : img) {
        if ((pixels.length * pixels[0].length) > width * height) {
          throw new IllegalArgumentException("Invalid dimensions");
        }
      }
    }
    if ((layers.get(0).image().length * layers.get(0).image()[0].length) < (width * height)) {
      for (Layer layer : layers) {
        img.add(layer.image());
      }
      layers.removeAll(layers);
    }
    List<Pixel[][]> newImages = new ArrayList<>();
    for (Pixel[][] image: img) {
      Pixel[][] imageGotton = downscalingEachImage(image, width, height);
      newImages.add(image);
      layers.add(0, new Layer(imageGotton,
          "Downscaled Layer",  true));
    }
    return newImages;
  }

  /**
   * This method downsizes the image given.
   * @param img image to be downsized
   * @param width width of the image
   * @param height height of the image
   * @return the image downsized
   */
  public Pixel[][] downscalingEachImage(Pixel[][] img, int width, int height) {
    Pixel[][] newImage = new Pixel[width][height];
    int oldColumn = img.length;
    int oldRow = img[0].length;
    float xRatio;
    float yRatio;
    float iValue;
    float jValue;
    Pixel a;
    Pixel b;
    Pixel c;
    Pixel d;
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        xRatio = i % width;
        yRatio = j % height;
        iValue = xRatio * oldColumn;
        jValue = yRatio * oldRow;
        try {
          a = img[(int) Math.floor(iValue)][(int) Math.floor(jValue)];
        } catch (IndexOutOfBoundsException e) {
            try {
              a = img[(int) Math.floor(iValue)][rowLength - 1];
            } catch (IndexOutOfBoundsException f) {
                try {
                  a = img[columnLength - 1][(int) Math.floor(jValue)];
                } catch (IndexOutOfBoundsException g) {
                  a = img[columnLength - 1][rowLength - 1];
                }
            }
        }

        try {
          b = img[(int) Math.ceil(iValue)][(int) Math.floor(jValue)];
        } catch (IndexOutOfBoundsException e) {
          try {
            b = img[(int) Math.ceil(iValue)][rowLength - 1];
          } catch (IndexOutOfBoundsException f) {
            try {
              b = img[columnLength - 1][(int) Math.floor(jValue)];
            } catch (IndexOutOfBoundsException g) {
              b = img[columnLength - 1][rowLength - 1];
            }
          }
        }

        try {
          c = img[(int) Math.floor(iValue)][(int) Math.ceil(jValue)];
        } catch (IndexOutOfBoundsException e) {
          try {
            c = img[(int) Math.floor(iValue)][rowLength - 1];
          } catch (IndexOutOfBoundsException f) {
            try {
              c = img[columnLength - 1][(int) Math.ceil(jValue)];
            } catch (IndexOutOfBoundsException g) {
              c = img[columnLength - 1][rowLength - 1];
            }
          }
        }

        try {
          d = img[(int) Math.ceil(iValue)][(int) Math.ceil(jValue)];
        } catch (IndexOutOfBoundsException e) {
          try {
            d = img[(int) Math.ceil(iValue)][rowLength - 1];
          } catch (IndexOutOfBoundsException f) {
            try {
              d = img[columnLength - 1][(int) Math.ceil(jValue)];
            } catch (IndexOutOfBoundsException g) {
              d = img[columnLength - 1][rowLength - 1];
            }
          }
        }
        int red = (int) getColorValue(a.getRedValue(), b.getRedValue(),
            c.getRedValue(), d.getRedValue(), iValue, jValue);
        int green = (int) getColorValue(a.getGreenValue(), b.getGreenValue(),
            c.getGreenValue(), d.getGreenValue(), iValue, jValue);
        int blue = (int) getColorValue(a.getBlueValue(), b.getBlueValue(),
            c.getBlueValue(), d.getBlueValue(), iValue, jValue);
        newImage[i][j] = new Pixel(i, j, red, green, blue);
      }
    }
    return newImage;
  }

  private double getColorValue(double a, double b, double c, double d, float x, float y) {
    double m = ((b * (x - Math.floor(x))) +
        (a * (Math.ceil(x) - x)));
    double n = ((d * (y - Math.floor(y))) +
        (c * (Math.ceil(y) - y)));
    return  (int) ((n * (y - Math.floor(y))) + (m * (Math.ceil(y) - y)));
  }

  @Override
  public Pixel[][] mosaic(int layerNumber, int numOfPixels) {
    if (layerNumber > layers.size()) {
      throw new IllegalArgumentException("No such layer");
    }
    if ((numOfPixels <= 0) || (numOfPixels > columnLength * rowLength)) {
      throw new IllegalArgumentException("There are more points than pixels");
    }
    Layer operatingLayer = layers.get(layerNumber);
    List<Pixel> selectedPoints = selectRandomPixels(operatingLayer.image(), numOfPixels);
    for (int i = 0; i < columnLength; i++) {
      for (int j = 0; j < rowLength; j++) {
        Pixel p = operatingLayer.image()[i][j];
        Pixel closestPixel = findingClosestPixel(p, selectedPoints);
        layers.get(layerNumber).image()[i][j].setRedValue(closestPixel.getRedValue());
        layers.get(layerNumber).image()[i][j].setGreenValue(closestPixel.getGreenValue());
        layers.get(layerNumber).image()[i][j].setBlueValue(closestPixel.getBlueValue());
      }
    }
    return layers.get(layerNumber).image();
  }

  private List<Pixel> selectRandomPixels(Pixel[][] image, int numOfPixels) {
    List<Pixel> selectedPoints = new ArrayList<>();
    for (int i = 0; i < numOfPixels; i++) {
      Pixel p = generatingRandomPixel(image, numOfPixels);
      while (selectedPoints.contains(p)) {
        p = generatingRandomPixel(image, numOfPixels);
      }
      selectedPoints.add(p);
    }
    return selectedPoints;
  }

  private Pixel findingClosestPixel(Pixel pixel, List<Pixel> points) {
    if (points.size() == 0) {
      throw new IllegalArgumentException("Invalid Arguments");
    }
    int x1 = pixel.getColumn();
    int y1 = pixel.getRow();
    int x2;
    int y2;
    double distance = 50000;
    Pixel closest = points.get(0);
    for (Pixel point : points) {
      x2 = point.getColumn();
      y2 = point.getRow();
      double newDistance = (Math.sqrt((x2 - x1) * (x2 - x1) +
          (y2 - y1) * (y2 - y1)));
      if (distance > Math.abs(newDistance)) {
        distance = Math.abs(newDistance);
        closest = point;
      }
    }
    return closest;
  }

  private Pixel generatingRandomPixel(Pixel[][] image, int numOfPixels) {
    int row = rowLength + 1, column = columnLength + 1;
    while ((column < 0) || (column >= columnLength)) {
      column = (int) (Math.random() * numOfPixels);
    }
    while ((row < 0) || (row >= rowLength)) {
      row = (int) (Math.random() * numOfPixels);
    }
    return image[column][row];
  }

  /**
   * This method provides the layers of the List of layers;
   * @return a List of Layers
   */
  public List<Layer> getLayers() {
    List<Layer> layersCopy = new ArrayList<>();
    for (int i = 0; i < layers.size(); i++) {
      layersCopy.add(layers.get(i));
    }
    return layersCopy;
  }

  public Pixel[][] getPixelArray() {
    return layers.get(0).image();
  }
}
