import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import controller.TextController;
import image.Pixel;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.IEditExtended;
import model.IEditExtendedImpl;
import org.junit.Test;

/**
 * testing for the IEditExtendedImpl class.
 */
public class IEditExtendedTest {

  IEditExtended model = new IEditExtendedImpl("src/Koala.ppm");

  public IEditExtendedTest() throws IOException {
    //used to throw an exception in case for the above made deceleration
  }

  @Test
  public void testConstructor() throws IOException {
    IEditExtended modelTry = new IEditExtendedImpl();
    model.mosaic(0, 1000);
    TextController controller1 = new TextController(model.makeDeepCopy(), "res/MosaicKoala",
        "ppm");
    assertEquals(((Pixel) modelTry.makeDeepCopy()[0][0]).getGreenValue(), 255);
    //controller1.goToExport();
  }

  @Test
  public void testDownscaling() throws IOException {
    IEditExtendedImpl scaledModel = new IEditExtendedImpl("src/Koala.ppm");
    Pixel[][] img = model.downscalingEachImage(model.makeDeepCopy(), 950, 600);
    assertEquals(model.makeDeepCopy().length, 1024);
  }

  @Test
  public void downscalingWithOnlyList() throws IOException {
    Pixel[][] pixels = model.downscalingEachImage(model.makeDeepCopy() ,952, 600);
    TextController controller = new TextController(pixels, "res/DownscaledKoala", "ppm");
    controller.goToExport();
    assertEquals(model.makeDeepCopy().length, 1024);
  }

  @Test
  public void downscalingImage() {
    Pixel[][] pixels = model.downscalingEachImage(model.makeDeepCopy() ,952, 600);
    assertEquals(pixels.length, 952);
    assertEquals(pixels[0].length, 600);
    assertEquals(model.makeDeepCopy().length, 952);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNoArgument() {
    model.downscaling(null, 600, 600);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyList() {
    List<Pixel[][]> p = new ArrayList<>();
    model.downscaling(p, 600, 600);
  }

  @Test
  public void mosaicImport() throws IOException {
    IEditExtended model2 = new IEditExtendedImpl("src/Koala.ppm");
    TextController controller = new TextController(model2.mosaic(0, 1000),
        "res/MosaicKoala", "ppm");
    //controller.goToExport();
    assertEquals(model.makeDeepCopy().length, model2.makeDeepCopy().length);
    assertEquals(model2.makeDeepCopy()[0].length, model2.makeDeepCopy()[0].length);
  }

  @Test
  public void mosaic() throws IOException {
    IEditExtended model2 = new IEditExtendedImpl("src/Koala.ppm");
    TextController controller = new TextController(model2.mosaic(0, 1000), 
        "res/Testing", "ppm");
    
    assertEquals(model.makeDeepCopy().length, model2.makeDeepCopy().length);
    assertEquals(model.makeDeepCopy()[0].length, model2.makeDeepCopy()[0].length);
  }

  @Test(expected = IllegalArgumentException.class)
  public void improperLayers() {
    model.mosaic(3, 500);
  }

  @Test(expected = IllegalArgumentException.class)
  public void morePoints() {
    model.mosaic(0, 50000000);
  }

  @Test(expected = IllegalArgumentException.class)
  public void lessPoints() {
    model.mosaic(0, -30);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noPoints() {
    model.mosaic(0, 0);
  }
  
  //-------------------------------

  @Test
  public void greyScaleTesting(){

    IEditExtendedImpl model = new IEditExtendedImpl();
    List<Pixel[][]> multiLayers = new ArrayList<>();
    multiLayers.add(model.makeDeepCopy());
    IEditExtendedImpl multilayerModel = new IEditExtendedImpl(model.makeDeepCopy());

    Pixel[][] greyBoard = multilayerModel.greyscale();

    //checking if all the colors are the same, since for grey scale they all have to be the same
    assertEquals(greyBoard[0][0].getRedValue(), greyBoard[0][0].getGreenValue());
    assertEquals(greyBoard[0][0].getGreenValue(), greyBoard[0][0].getBlueValue());

    assertEquals(greyBoard[1][0].getRedValue(), greyBoard[1][0].getGreenValue());
    assertEquals(greyBoard[1][0].getGreenValue(), greyBoard[1][0].getBlueValue());
  }

  @Test
  public void testAlternateChecks() throws FileNotFoundException {
    IEditExtendedImpl model = new IEditExtendedImpl(4, 4);

    List<Pixel[][]> multiLayers = new ArrayList<>();
    multiLayers.add(model.makeDeepCopy());

    IEditExtendedImpl multilayerModel = new IEditExtendedImpl(model.makeDeepCopy());

    //checking if all three colors have the same value
    Pixel[][] img = multilayerModel.getTopMostLayer();
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        assertEquals(img[i][j].getRedValue(), img[i][j].getGreenValue());
        assertEquals(img[i][j].getBlueValue(), img[i][j].getGreenValue());
        assertEquals(img[i][j].getRedValue(), img[i][j].getBlueValue());
      }
    }
  }

  @Test
  public void testExport() throws FileNotFoundException {
    IEditExtendedImpl model = new IEditExtendedImpl(100, 100);

    List<Pixel[][]> multiLayers = new ArrayList<>();
    multiLayers.add(model.makeDeepCopy());

    IEditExtendedImpl multilayerModel = new IEditExtendedImpl(model.makeDeepCopy());

    Pixel[][] modelGrey = multilayerModel.greyscale();

    assertEquals(100, modelGrey[0].length);
  }

  @Test
  public void testSepia() throws FileNotFoundException {
    IEditExtendedImpl model = new IEditExtendedImpl(5, 6);

    List<Pixel[][]> multiLayers = new ArrayList<>();
    multiLayers.add(model.makeDeepCopy());

    IEditExtendedImpl multilayerModel = new IEditExtendedImpl(model.makeDeepCopy());

    Pixel[][] sepiaModel = multilayerModel.sepiaTone();

    int sepiaRedValue = 345;
    int sepiaGreenValue = 307;
    int sepiaBlueValue = 239;

    for (int i = 0; i < 5 - 1; i += 2) {
      for (int j = 0; j < 6 - 1; j += 2) {
        //checking alternate elements for the changed color
        //as it is a checker board all the alternate colors will be the
        // same because they were originally black
        assertEquals(sepiaRedValue, sepiaModel[i][j].getRedValue());
        assertEquals(sepiaGreenValue, sepiaModel[i][j].getGreenValue());
        assertEquals(sepiaBlueValue, sepiaModel[i][j].getBlueValue());
        //alternative element of a row is the same color
        assertEquals(0, sepiaModel[i][j + 1].getRedValue());
        assertEquals(0, sepiaModel[i][j + 1].getGreenValue());
        assertEquals(0, sepiaModel[i][j + 1].getBlueValue());
        //alternate row starts with a white
        assertEquals(0, sepiaModel[i + 1][j].getRedValue());
        assertEquals(0, sepiaModel[i + 1][j].getGreenValue());
        assertEquals(0, sepiaModel[i + 1][j].getBlueValue());
        //alternative element of a row is the same color
        assertEquals(sepiaRedValue, sepiaModel[i + 1][j + 1].getRedValue());
        assertEquals(sepiaGreenValue, sepiaModel[i + 1][j + 1].getGreenValue());
        assertEquals(sepiaBlueValue, sepiaModel[i + 1][j + 1].getBlueValue());
      }
    }
  }

  @Test
  public void testBlur() throws FileNotFoundException {

    IEditExtendedImpl model = new IEditExtendedImpl(3, 3);

    List<Pixel[][]> multiLayers = new ArrayList<>();
    multiLayers.add(model.makeDeepCopy());

    IEditExtendedImpl multilayerModel = new IEditExtendedImpl(model.makeDeepCopy());


    assertEquals(model.makeDeepCopy().length, multilayerModel.blur().length);
    Pixel[][] img = multilayerModel.getTopMostLayer();
    assertEquals(79, img[0][0].getRedValue());
    assertEquals(79, img[0][0].getGreenValue());
    assertEquals(79, img[0][0].getBlueValue());
    assertEquals(95, img[0][1].getRedValue());
    assertEquals(95, img[0][1].getGreenValue());
    assertEquals(95, img[0][1].getBlueValue());
    assertEquals(127, img[1][1].getRedValue());
    assertEquals(127, img[1][1].getGreenValue());
    assertEquals(127, img[1][1].getBlueValue());
    assertEquals(95, img[2][1].getRedValue());
    assertEquals(95, img[2][1].getGreenValue());
    assertEquals(95, img[2][1].getBlueValue());
  }

  @Test
  public void testSharpen() throws FileNotFoundException {

    IEditExtended model = new IEditExtendedImpl(3, 3);
    List<Pixel[][]> multiLayers = new ArrayList<>();
    multiLayers.add(model.makeDeepCopy());
    IEditExtendedImpl multilayerModel = new IEditExtendedImpl(model.makeDeepCopy());

    assertEquals(model.makeDeepCopy().length, multilayerModel.sharpen().length);
    Pixel[][] img = model.makeDeepCopy();
    assertEquals(223, img[0][0].getRedValue());
    assertEquals(223, img[0][0].getGreenValue());
    assertEquals(223, img[0][0].getBlueValue());
    assertEquals(127, img[0][1].getRedValue());
    assertEquals(127, img[0][1].getGreenValue());
    assertEquals(127, img[0][1].getBlueValue());
    assertEquals(255, img[1][1].getRedValue());
    assertEquals(255, img[1][1].getGreenValue());
    assertEquals(255, img[1][1].getBlueValue());
    assertEquals(127, img[2][1].getRedValue());
    assertEquals(127, img[2][1].getGreenValue());
    assertEquals(127, img[2][1].getBlueValue());
  }

  @Test
  public void testCheetahImportSepiaExport() {

    IEditExtended model;

    try {
      model = new IEditExtendedImpl("src/cheetahs.ppm");
    } catch(FileNotFoundException f) {
      throw new IllegalArgumentException("File not found.");
    } catch (IOException e) {
      throw new IllegalArgumentException("File not found.");
    }

    IEditExtendedImpl multilayerModel = new IEditExtendedImpl();

    IEditExtendedImpl greyKoala = new IEditExtendedImpl(multilayerModel.sepiaTone());
    assertEquals(greyKoala.getLayers().get(0).image().length,
        multilayerModel.getLayers().get(0).image().length);
  }
}
