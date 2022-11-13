package model;

import controller.TextController;
import image.Layer;
import image.Pixel;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a multilayered image that applies effects such as blue, sharpen, greyscale,
 * and sepia.
 */

public class MultiLayeredImages implements IMultiLayer {

  //current layer
  private int current;
  private int rowLength;
  private int columnLength;
  private ImageProcessingModel model;
  //current will always be the value at [0]
  List<Layer> layers = new ArrayList<>();

  /**
   * Constructor where nothing is given and makes a checkerboard.
   */
  public MultiLayeredImages() {
    this.model = new ImageProcessingModel();
    this.columnLength = model.getColumnLength();
    this.rowLength = model.getRowLength();

    //255 bc a checkerboard is only black/white
    this.current = 0;
    layers.add(new Layer(model.makeDeepCopy(), "First Layer", true));
  }

  /**
   * Constructor that makes a checkerboard of specified dimensions.
   */

  public MultiLayeredImages(int columnLength, int rowLength) throws IllegalArgumentException {
    this.model = new ImageProcessingModel(columnLength, rowLength);

    this.columnLength = model.getColumnLength();
    this.rowLength = model.getRowLength();
    //255 bc a checkerboard is only black/white
    //randomly generates a checkerboard when no image is given
    this.current = 0;
    layers.add(new Layer(model.makeDeepCopy(), "First Layer", true));
  }

  /**
   * Constructor of a image where the image is given.
   */
  public MultiLayeredImages(Pixel[][] img) throws IllegalArgumentException {
    this.model = new ImageProcessingModel(img);
    this.columnLength = model.getColumnLength();
    this.rowLength = model.getRowLength();
    //255 bc a checkerboard is only black/white
    //randomly generates a checkerboard when no image is given
    this.current = 0;
    layers.add(new Layer(model.makeDeepCopy(), "First Layer", true));
  }

  /**
   * Constructor of a multilayered image as a list of layers..
   */

  public MultiLayeredImages(List<Pixel[][]> img) throws IllegalArgumentException {
    if ((img.size() == 0) || (img == null)) {
      throw new IllegalArgumentException("Images given are empty");
    }
    this.model = new ImageProcessingModel(img.get(0));
    this.columnLength = model.getColumnLength();
    this.rowLength = model.getRowLength();
    this.current = 0;
    for (int i = 0; i < img.size(); i++) {
      layers.add(new Layer(clamp(img.get(i)), "First Layer", true));
    }
  }

  /**
   * Constructor of an image of the given file name.
   */

  public MultiLayeredImages(String filename) throws IllegalArgumentException, IOException {
    this.model = new ImageProcessingModel(filename);
    this.columnLength = model.getColumnLength();
    this.rowLength = model.getRowLength();
    this.current = 0;
    layers.add(new Layer(model.makeDeepCopy(), "First Layer", true));
  }


  @Override
  public Pixel[][] blur() {
    return model.blur();
  }

  @Override
  public Pixel[][] sharpen() {
    return model.sharpen();
  }

  /**
   * clamping the values of the image to stay within the range of 0-255.
   * @param  img the 2D list of pixels given as an image.
   * @return clamped values of each r,g,b components of each pixel of the image.
   */
  private Pixel[][] clamp(Pixel[][] img) {
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

  /**
   * turns the image into a greyscale image by converting the values r g b accordingly.
   * @return the newly made greyscale 2D array of pixels
   */
  @Override
  public Pixel[][] greyscale() {
    return model.greyscale();
  }

  @Override
  public Pixel[][] sepiaTone() {
    return model.sepiaTone();
  }

  //------------------------------------------------------------------------------------------

  /**
   * sets the visibility to true if it is false, if not, doesn't do anything.
   * @param layerNumber the layer to be made visible
   */
  public void makeVisible(int layerNumber) {
    if (layerNumber < layers.size()) {
      if (!layers.get(layerNumber).visibility()) {
        layers.get(layerNumber).setVisibility(true);
      }
    }
  }

  /**
   * sets the visibility to false if it is true, if not, doesn't do anything.
   * @param layerNumber the layer to be made invisible
   */
  public void makeInvisible(int layerNumber) {
    if (layerNumber < layers.size()) {
      if (layers.get(layerNumber).visibility()) {
        layers.get(layerNumber).setVisibility(false);
      }
    }
  }
  /**
   * duplicates a layer and adds it on the top as a new layer.
   * @param name              the name of the layer as the user wants
   * @param duplicateOfLayer  the layer position in the stack of the layer
   */
  public void duplicateLayer(String name, int duplicateOfLayer) {

    if ((duplicateOfLayer < 0) || (duplicateOfLayer >= layers.size())) {
      throw new IllegalArgumentException("The layer to be duplicated does not exist");
    }
    Pixel[][] img = layers.get(duplicateOfLayer).image();
    ImageProcessingModel model = new ImageProcessingModel(img);
    Layer newLayer = new Layer(model.makeDeepCopy(), name, true);
    layers.add(0, newLayer);
  }

  /**
   * imports a new image as a layer.
   * @param name      the name of the layer as the user wants
   * @param fileName  the filename/location of the image to be imported
   */
  public void addLayer(String name, String fileName) throws IOException {
    ImageProcessingModel model = new ImageProcessingModel(fileName);
    Pixel[][] image = model.makeDeepCopy();
    int importedImageColumns = image.length;
    int importedImageRow = image[0].length;
    if (layers.size() != 0) {
      if ((this.columnLength != importedImageColumns) || (this.rowLength != importedImageRow)) {
        throw new IllegalArgumentException("The image imported is not of the same size as "
            + "the others. Please try again with a different image.");
      } else {
        Layer newLayer = new Layer(image, name, true);
        layers.add(0, newLayer);
        if ((columnLength == 0) || (rowLength == 0)) {
          this.columnLength = importedImageColumns;
          this.rowLength = importedImageRow;
        }
      }
    } else {
      Layer newLayer = new Layer(image, name, true);
      layers.add(newLayer);
      if ((columnLength == 0) || (rowLength == 0)) {
        this.columnLength = importedImageColumns;
        this.rowLength = importedImageRow;
      }
    }
  }

  /**
   * Method adds a layer to the image.
   * @param layerToAdd the layer to add
   * @param name the name of the layer
   */
  public void addLayer(Pixel[][] layerToAdd, String name) {

    int importedImageColumns = layerToAdd.length;
    int importedImageRow = layerToAdd[0].length;
    if (layers.size() != 0) {
      if ((this.columnLength != importedImageColumns) || (this.rowLength != importedImageRow)) {
        throw new IllegalArgumentException("The image imported is not of the "
            + "same size as the others. Please try again with a different image.");
      }
    } else {
      Layer newLayer = new Layer(layerToAdd, name, true);
      layers.add(0, newLayer);
      if ((columnLength == 0) || (rowLength == 0)) {
        this.columnLength = importedImageColumns;
        this.rowLength = importedImageRow;
      }
    }

    Layer newLayer = new Layer(layerToAdd, name, true);

    ImageProcessingModel model = new ImageProcessingModel(layerToAdd);
    layers.add(0, newLayer);
  }

  /**
   * This method adds a blank layer to the image.
   */
  public void addBlankLayer() {

    int column = model.getColumnLength();
    int row = model.getRowLength();

    Pixel[][] blankImage = new Pixel[column][row];
    for (int i = 0; i < column; i++) {
      for (int j = 0; j < row; j++) {
        blankImage[i][j] = new Pixel(i, j, 0, 0, 0);
      }
    }

    Layer newLayer = new Layer(blankImage, "Blank Layer", true);

    ImageProcessingModel model = new ImageProcessingModel(blankImage);
    layers.add(0, newLayer);
  }


  /**
   * takes in the layer number from the user and removes that particular layer.
   * @param num the layer number that needs to be removed
   */
  public void removeLayer(int num) {
    try {
      layers.remove(num);
    } catch (IllegalArgumentException e) {
      System.out.println("No such layer exists hence cannot be removed");
    }
  }

  /**
   * removes the topmost layer from the arraylist.
   */
  public void removeLayer() {
    try {
      layers.remove(0);
    } catch (IllegalArgumentException e) {
      System.out.println("No such layer exists hence cannot be removed");
    }
  }

  //------------------------------------------------

  public void exportFirstLayer(String filename, String type) {
    TextController controller = new TextController(layers.get(0).image(), filename, type);
    controller.goToExport();
  }

  public void exportIndexedLayer(int index, String filename, String type) {
    TextController controller = new TextController(layers.get(index).image(), filename, type);
    controller.goToExport();
  }

  /**
   * Exports every layer of the multilayered image.
   * @param type the type of file
   */
  public void exportEveryLayer(String type) {

    for (int i = 0; i < layers.size(); i++) {
      String filename = "Layer" + String.valueOf(i);
      TextController controller = new TextController(layers.get(i).image(), filename, type);
    }
  }

  //------------------------------------------------

  public void removeTopMostLayer() {
    layers.remove(0);
  }

  /**
   * the list of images, which is a multilayered image.
   * @return the multilayered image as a list of pixels
   * @throws IllegalStateException if there are no more layers
   */
  public List<Pixel[][]> getlayers() throws IllegalStateException {
    if (layers.isEmpty()) {
      throw new IllegalStateException("No more layers");
    }
    List<Pixel[][]> layers = new ArrayList();
    for (int i = 0; i < layers.size(); i++) {
      layers.add(layers.get(i));
    }
    return layers;
  }

  /**
   * The top most layer.
   * @return the top most layer of the mulitlayered image.
   * @throws IllegalStateException if there are no more layers
   */

  public Pixel[][] getTopMostLayer() throws IllegalStateException {
    if (layers.isEmpty()) {
      throw new IllegalStateException("No more layers");
    }
    return layers.get(layers.size() - 1).image();
  }

  /**
   * The specific layer indicated by the index given.
   * @param index the number of the layer that is wanted
   * @return the layer specified bu the number
   * @throws IllegalStateException if there are no layers
   */
  public Pixel[][] getSpecificLayer(int index) throws IllegalStateException {
    if (layers.size() <= index) {
      throw new IllegalStateException("No more layers");
    }
    return layers.get(index).image();
  }


  /**
   * Makes a deep copy of the multilayered image.
   * @return the multilayered image as a deep copy
   */
  public Pixel[][] makeDeepCopy() {
    Pixel[][] imgCopy = new Pixel[columnLength][rowLength];
    for (int layerNumber = 0; layerNumber < layers.size(); layerNumber++) {
      if (layers.get(layerNumber).visibility()) {
        for (int i = 0; i < columnLength; i++) {
          for (int j = 0; j < rowLength; j++) {
            imgCopy[i][j] = new Pixel(i, j,
                layers.get(layerNumber).image()[i][j].getRedValue(),
                layers.get(layerNumber).image()[i][j].getGreenValue(),
                layers.get(layerNumber).image()[i][j].getBlueValue());
          }
        }
        break;
      }
    }
    return imgCopy;
  }
}
