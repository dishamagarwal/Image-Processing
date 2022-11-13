import image.Pixel;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.ImageProcessingModel;
import model.MultiLayeredImages;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * testing for the MultiLayer class.
 */
public class MultiLayeredImagesTest {

  @Test
  public void testConstructor() {
    ImageProcessingModel model = new ImageProcessingModel();
    
    List<Pixel[][]> multiLayers = new ArrayList<>();
    multiLayers.add(model.makeDeepCopy());
    
    MultiLayeredImages multilayerModel = new MultiLayeredImages();
    assertEquals(model.getColumnLength(), multiLayers.get(0).length);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullDelegate() {
    ImageProcessingModel model = new ImageProcessingModel();
    List<Pixel[][]> multiLayers = new ArrayList<>();
    multiLayers.add(model.makeDeepCopy());
    
    Pixel[][] pixels = null;
    MultiLayeredImages multilayerModel = new MultiLayeredImages(pixels);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullLayers() {
    ImageProcessingModel model = new ImageProcessingModel();
    
    
    List<Pixel[][]> pixels = null;
    MultiLayeredImages multilayerModel = new MultiLayeredImages(pixels);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNonSameSizeImages() throws IOException {
    ImageProcessingModel model = new ImageProcessingModel();
    List<ImageProcessingModel> modelList = new ArrayList<>();
    ImageProcessingModel model2 = new ImageProcessingModel("src/cheetahs.ppm");
    Pixel[][] img1 = model.makeDeepCopy();
    Pixel[][] img2 = model2.makeDeepCopy();
    List<Pixel[][]> multiLayers = new ArrayList<>();
    multiLayers.add(img1);
    multiLayers.add(img2);

    MultiLayeredImages multilayerModel = new MultiLayeredImages(multiLayers);
  }

  //tests all the export functions from the class
  @Test
  public void testExportFirstLayer() throws IOException {
    ImageProcessingModel model;
    try {
      model = new ImageProcessingModel("src/highway.ppm");
    } catch(FileNotFoundException f) {
      throw new IllegalArgumentException("File not found.");
    }

    
    List<Pixel[][]> multiLayers = new ArrayList<>();
    multiLayers.add(model.makeDeepCopy());
    MultiLayeredImages multilayerModel = new MultiLayeredImages(multiLayers);
    multilayerModel.exportFirstLayer("res/highwayBlur", "png");
    assertEquals(multilayerModel.getlayers().get(0).length, model.getColumnLength());
  }

  @Test
  public void testDublicateLayer() {
    ImageProcessingModel model = new ImageProcessingModel();
    
    List<Pixel[][]> multiLayers = new ArrayList<>();
    multiLayers.add(model.makeDeepCopy());
    
    MultiLayeredImages multilayerModel = new MultiLayeredImages(multiLayers);
    multilayerModel.duplicateLayer("Duplicate", 1);
    assertEquals(multilayerModel.getSpecificLayer(1), multilayerModel.getSpecificLayer(0));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testRemoveLayerAndLayerException() {
    ImageProcessingModel model = new ImageProcessingModel();
    
    List<Pixel[][]> multiLayers = new ArrayList<>();
    multiLayers.add(model.makeDeepCopy());
    
    MultiLayeredImages multilayerModel = new MultiLayeredImages(multiLayers);
    multilayerModel.removeLayer(0);
    multilayerModel.getTopMostLayer();
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testRemoveTopMostLayerAndLayerException() {
    ImageProcessingModel model = new ImageProcessingModel();
    
    List<Pixel[][]> multiLayers = new ArrayList<>();
    multiLayers.add(model.makeDeepCopy());
    
    MultiLayeredImages multilayerModel = new MultiLayeredImages(multiLayers);
    multilayerModel.removeTopMostLayer();
    multilayerModel.getTopMostLayer();
  }

  @Test
  public void testGetLayers() {
    ImageProcessingModel model = new ImageProcessingModel();
    
    List<Pixel[][]> multiLayers = new ArrayList<>();
    multiLayers.add(model.makeDeepCopy());
    
    MultiLayeredImages multilayerModel = new MultiLayeredImages(multiLayers);
    multilayerModel.addLayer(model.makeDeepCopy(), "New Layer");
    assertEquals(multilayerModel.getTopMostLayer(), multilayerModel.getSpecificLayer(1));
    List<Pixel[][]> layers = new ArrayList<>();
    layers.add(multilayerModel.getSpecificLayer(0));
    layers.add(multilayerModel.getSpecificLayer(1));
    assertEquals(multilayerModel.getlayers(),layers );
  }

  @Test
  public void testAddBlankLayer() {
    ImageProcessingModel model = new ImageProcessingModel();
    
    List<Pixel[][]> multiLayers = new ArrayList<>();
    multiLayers.add(model.makeDeepCopy());
    
    MultiLayeredImages multilayerModel = new MultiLayeredImages(multiLayers);
    multilayerModel.addBlankLayer();
    assertNotEquals(multilayerModel.getSpecificLayer(1), multilayerModel.getSpecificLayer(0));
  }

  @Test
  public void testAddLayer() {
    ImageProcessingModel model = new ImageProcessingModel();
    
    List<Pixel[][]> multiLayers = new ArrayList<>();
    multiLayers.add(model.makeDeepCopy());
    
    MultiLayeredImages multilayerModel = new MultiLayeredImages(multiLayers);
    multilayerModel.addLayer(model.makeDeepCopy(), "Layer");
    assertNotEquals(multilayerModel.getSpecificLayer(1), multilayerModel.getSpecificLayer(0));
  }

  //export functions test -----------------------------------------------------------------------

  @Test
  public void testExportEveryLayer() throws IOException {
    ImageProcessingModel model;
    try {
      model = new ImageProcessingModel("src/highway.ppm");
    } catch(FileNotFoundException f){
      throw new IllegalArgumentException("File not found.");
    }

    
    List<Pixel[][]> multiLayers = new ArrayList<>();
    multiLayers.add(model.makeDeepCopy());
    
    MultiLayeredImages multilayerModel = new MultiLayeredImages(multiLayers);
    multilayerModel.exportEveryLayer("png");
  }

  @Test
  public void testExportIndexedLayer() throws IOException {
    ImageProcessingModel model;
    try {
      model = new ImageProcessingModel("src/highway.ppm");
    } catch(FileNotFoundException f){
      throw new IllegalArgumentException("File not found.");
    }

    
    List<Pixel[][]> multiLayers = new ArrayList<>();
    multiLayers.add(model.makeDeepCopy());
    
    MultiLayeredImages multilayerModel = new MultiLayeredImages(multiLayers);
    multilayerModel.exportIndexedLayer(0, "res/highwayBlur", "png");
  }

  //----------------------------------------------------------------------------------------------
  // testing for old functionality working properly with this model
  //removed due to time and space constraint
  //was there before
  //check old submissions for reference
}
