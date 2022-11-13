import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import image.Pixel;
import java.io.FileNotFoundException;
import java.io.IOException;
import model.ImageProcessingModel;
import org.junit.Test;
import view.ImageTextView;

/**
 * testing for the ImageProcessingModel class.
 */
public class ImageProcessingModelTest {

  @Test
  public void imageProcessingModelTestConstructor() {

    ImageProcessingModel model = new ImageProcessingModel(4, 4);

    Pixel[][] img = model.makeDeepCopy();
    //passes the length correctly
    assertEquals(4, model.getColumnLength());
    assertEquals(4, model.getRowLength());
    assertEquals(4, img[0].length);

    //---testing checkerBoard---

    //checking if all three colors have the same value
    assertEquals(255, img[0][0].getRedValue());
    assertEquals(255, img[0][0].getGreenValue());
    assertEquals(255, img[0][0].getBlueValue());

    //checking if it is checkered
    assertEquals(0, img[0][1].getRedValue());
    assertEquals(255, img[0][2].getRedValue());
    assertEquals(0, img[1][0].getRedValue());
    assertEquals(255, img[2][0].getRedValue());
  }

  @Test
  public void greyScaleTesting() {

    ImageProcessingModel model = new ImageProcessingModel();
    Pixel[][] greyBoard = model.greyscale();

    //checking if all the colors are the same, since for grey scale they all have to be the same
    assertEquals(greyBoard[0][0].getRedValue(), greyBoard[0][0].getGreenValue());
    assertEquals(greyBoard[0][0].getGreenValue(), greyBoard[0][0].getBlueValue());

    assertEquals(greyBoard[1][0].getRedValue(), greyBoard[1][0].getGreenValue());
    assertEquals(greyBoard[1][0].getGreenValue(), greyBoard[1][0].getBlueValue());

    ImageTextView view = new ImageTextView(model);
    view.exportPPM(model, "res/CheckerBoardgreyTest");
  }

  @Test
  public void clampTesting() {
    Pixel[][] toBeClamped = new Pixel[2][2];

    toBeClamped[0][0] = new Pixel(0, 0, 257, 257, 257);
    toBeClamped[0][1] = new Pixel(0, 1, 0, 0, 0);
    toBeClamped[1][0] = new Pixel(1, 0, -1, 255, -10);
    toBeClamped[1][1] = new Pixel(1, 1, 0, 289, 0);

    ImageProcessingModel model = new ImageProcessingModel(toBeClamped);
    Pixel[][] img = model.makeDeepCopy();

    //check if it clamps the values necessary
    assertEquals(255, img[0][0].getRedValue());
    assertEquals(255, img[0][0].getGreenValue());
    assertEquals(255, img[0][0].getBlueValue());

    assertEquals(0, img[1][0].getRedValue());
    assertEquals(0, img[1][0].getBlueValue());

    assertEquals(255, img[1][1].getGreenValue());

    //check if it leaves normal values alone

    assertEquals(0, img[0][1].getRedValue());
    assertEquals(0, img[0][1].getGreenValue());
    assertEquals(0, img[0][1].getBlueValue());

    assertEquals(255, img[1][0].getGreenValue());

    assertEquals(0, img[1][1].getRedValue());
    assertEquals(0, img[1][1].getBlueValue());
  }

  @Test
  public void testAlternateChecks() {
    ImageProcessingModel model = new ImageProcessingModel(4, 4);
    //checking if all three colors have the same value
    Pixel[][] img = model.makeDeepCopy();
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        assertEquals(img[i][j].getRedValue(), img[i][j].getGreenValue());
        assertEquals(img[i][j].getBlueValue(), img[i][j].getGreenValue());
        assertEquals(img[i][j].getRedValue(), img[i][j].getBlueValue());
      }
    }

    //checking if it is checkered
    for (int i = 0; i < 4 - 2; i++) {
      for (int j = 0; j < 4 - 2; j++) {
        //consecutive element of a row is not the same color
        assertNotEquals(img[i][j].getRedValue(), img[i][j + 1].getRedValue());
        //alternative element of a row is the same color
        assertEquals(img[i][j].getRedValue(), img[i][j + 2].getRedValue());
        //consecutive element of a column is not the same color
        assertNotEquals(img[i][j].getRedValue(), img[i + 1][j].getRedValue());
        //alternative element of a column is the same color
        assertEquals(img[i][j].getRedValue(), img[i + 2][j].getRedValue());
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void nullImageConstructorTest() {
    ImageProcessingModel model = new ImageProcessingModel((Pixel[][]) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noFileName() throws IOException {
    ImageProcessingModel model = new ImageProcessingModel("");
  }

  @Test(expected = FileNotFoundException.class)
  public void incorrectFileName() throws IOException {
    ImageProcessingModel model = new ImageProcessingModel("src/hello.ppm");
  }


  @Test(expected = IllegalArgumentException.class)
  public void nullPixelArrayConstructorTest() {

    Pixel[][] nullImage = null;
    ImageProcessingModel model = new ImageProcessingModel(nullImage);
  }

  @Test(expected = IllegalArgumentException.class)
  public void nullStringConstructorTest() throws IOException {

    String nullString = null;

    try {
      ImageProcessingModel model = new ImageProcessingModel(nullString);
    } catch(FileNotFoundException f) {
      throw new IllegalArgumentException("File given is null");
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidColumnTest() {
    ImageProcessingModel model = new ImageProcessingModel(-3, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidRowTest() {
    ImageProcessingModel model = new ImageProcessingModel(2, -10);
  }

  @Test
  public void testExport() {
    ImageProcessingModel model = new ImageProcessingModel(100, 100);
    Pixel[][] modelGrey = model.greyscale();

    assertEquals(100, modelGrey[0].length);
  }

  @Test
  public void testSepia() {
    ImageProcessingModel model = new ImageProcessingModel(5, 6);
    Pixel[][] sepiaModel = model.sepiaTone();

    int sepiaRedValue = 345;
    int sepiaGreenValue = 307;
    int sepiaBlueValue = 239;

    for (int i = 0; i < 5 - 1; i += 2) {
      for (int j = 0; j < 6 - 1; j += 2) {
        //checking alternate elements for the changed color
        //as it is a checker board all the alternate colors will
        // be the same because they were originally black
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
  public void testBlur() {

    ImageProcessingModel model = new ImageProcessingModel(3, 3);

    assertEquals(model.getColumnLength(), model.blur().length);
    Pixel[][] img = model.makeDeepCopy();
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
  public void testSharpen() {

    ImageProcessingModel model = new ImageProcessingModel(3, 3);

    assertEquals(model.getColumnLength(), model.sharpen().length);
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
  public void testCheetahImportBlurringExport() {

    ImageProcessingModel model;

    try {
      model = new ImageProcessingModel("src/cheetahs.ppm");
    } catch(FileNotFoundException f) {
      throw new IllegalArgumentException("File not found.");
    } catch (IOException e) {
      throw new IllegalArgumentException("File not found.");
    }
    ImageProcessingModel greyKoala = new ImageProcessingModel(model.blur());
    ImageTextView view = new ImageTextView(model);
    view.exportPPM(greyKoala, "res/cheetahsBlur");
    assertEquals(model.getColumnLength(), greyKoala.getColumnLength());
  }

  @Test
  public void testCheetahImportSharpeningExport() throws IOException {

    ImageProcessingModel model;

    try {
      model = new ImageProcessingModel("src/cheetahs.ppm");
    } catch(FileNotFoundException f) {
      throw new IllegalArgumentException("File not found.");
    }

    ImageProcessingModel greyKoala = new ImageProcessingModel(model.sharpen());
    ImageTextView view = new ImageTextView(model);
    view.exportPPM(greyKoala, "res/cheetahsSharpening");
    assertEquals(model.getColumnLength(), greyKoala.getColumnLength());
  }

  @Test
  public void testCheetahImportGreyScaleExport() throws IOException {

    ImageProcessingModel model;

    try {
      model = new ImageProcessingModel("src/cheetahs.ppm");
    } catch(FileNotFoundException f) {
      throw new IllegalArgumentException("File not found.");
    }

    ImageProcessingModel greyKoala = new ImageProcessingModel(model.greyscale());
    ImageTextView view = new ImageTextView(model);
    view.exportPPM(greyKoala, "res/cheetahsGrey");
    assertEquals(model.getColumnLength(), greyKoala.getColumnLength());
  }

  @Test
  public void testCheetahImportSepiaExport() {

    ImageProcessingModel model;

    try {
      model = new ImageProcessingModel("src/cheetahs.ppm");
    } catch(FileNotFoundException f) {
      throw new IllegalArgumentException("File not found.");
    } catch (IOException e) {
      throw new IllegalArgumentException("File not found.");
    }

    ImageProcessingModel greyKoala = new ImageProcessingModel(model.sepiaTone());
    ImageTextView view = new ImageTextView(model);
    view.exportPPM(greyKoala, "res/cheetahsSepia");
    assertEquals(model.getColumnLength(), greyKoala.getColumnLength());
  }

  @Test
  public void testHighwayImportBlurringExport() throws IOException {

    ImageProcessingModel model;

    try {
      model = new ImageProcessingModel("src/highway.ppm");
    } catch(FileNotFoundException f) {
      throw new IllegalArgumentException("File not found.");
    }

    ImageProcessingModel greyKoala = new ImageProcessingModel(model.blur());
    ImageTextView view = new ImageTextView(model);
    view.exportPPM(greyKoala, "res/highwayBlur");
    assertEquals(model.getColumnLength(), greyKoala.getColumnLength());
  }

  @Test
  public void testHighwayImportSharpeningExport() throws IOException {

    ImageProcessingModel model;

    try {
      model = new ImageProcessingModel("src/highway.ppm");
    } catch(FileNotFoundException f) {
      throw new IllegalArgumentException("File not found.");
    }

    ImageProcessingModel greyKoala = new ImageProcessingModel(model.sharpen());
    ImageTextView view = new ImageTextView(model);
    view.exportPPM(greyKoala, "res/highwaySharpening");
    assertEquals(model.getColumnLength(), greyKoala.getColumnLength());
  }

  @Test
  public void testHighwayImportGreyExport() throws IOException {

    ImageProcessingModel model;

    try {
      model = new ImageProcessingModel("src/highway.ppm");
    } catch(FileNotFoundException f) {
      throw new IllegalArgumentException("File not found.");
    }

    ImageProcessingModel greyKoala = new ImageProcessingModel(model.greyscale());
    ImageTextView view = new ImageTextView(model);
    view.exportPPM(greyKoala, "res/highwayGrey");
    assertEquals(model.getColumnLength(), greyKoala.getColumnLength());
  }

  @Test
  public void testHighwayImportSepiaExport() throws IOException {

    ImageProcessingModel model;

    try {
      model = new ImageProcessingModel("src/highway.ppm");
    } catch(FileNotFoundException f) {
      throw new IllegalArgumentException("File not found.");
    }

    ImageProcessingModel greyKoala = new ImageProcessingModel(model.sepiaTone());
    ImageTextView view = new ImageTextView(model);
    view.exportPPM(greyKoala, "res/highwaySepia");
    assertEquals(model.getColumnLength(), greyKoala.getColumnLength());
  }
}
