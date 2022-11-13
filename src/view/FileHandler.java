package view;

import image.Pixel;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This interface handles all the operations that have to do with importing / expoprting files.
 */

public interface FileHandler {

  /**
   * This method turns a PPM file into a 2D array of Pixels.
   * @return 2D Pixel array with info from the PPM
   * @throws FileNotFoundException when the filename is invalid
   */
  Pixel[][] importPPM() throws FileNotFoundException;

  /**
   * This method imports an image of type JPEG or PNG into a 2D array of Pixels.
   * @return 2D pixel array with info from the image
   * @throws IOException when unable to read file
   */
  Pixel[][] importImage() throws IOException;


  /**
   * This method exports a valid PPM format.
   * @param fileName name of the file to be newly made
   */
  void exportPPM(String fileName);

  /**
   * This method exports an image file of the type given.
   * @param filename name of the new file to be made
   * @param type type of file to be exported (JPEG or PNG)
   */
  void exportImage(String filename, String type);

  String getPath();
}
