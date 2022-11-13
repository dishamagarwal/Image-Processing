package controller;

import image.Pixel;
import java.io.IOException;
import view.FileHandlerImpl;

/**
 * This class is the text for the controller.
 */

public class TextController {
  private Pixel[][] img;
  private String fileName;
  private String type;

  //-------------------------------used to import/export-------------------------------

  public TextController(Pixel[][] imgToExport, String fileName, String type) {
    this.img = imgToExport;
    this.fileName = fileName;
    this.type = type;
  }

  public TextController(String fileName) {
    this.fileName = fileName;
  }

  //----------------------------------implementation----------------------------------

  public Pixel[][] goToImport() throws IllegalArgumentException, IOException {
    String extension = "";
    for (int i = (fileName.length()-1); i >= 0; i--) {
      if (fileName.charAt(i) == '.') {
        break;
      } else {
        if (i == 0) {
          throw new IllegalArgumentException("No File Found!");
        }
        extension+= fileName.charAt(i);
      }
    }
    String orderedExtension = "";
    for (int i = (extension.length()-1); i >= 0; i--) {
      orderedExtension+= extension.charAt(i);
    }

    //calling the view
    FileHandlerImpl view = new FileHandlerImpl(fileName);
    if (orderedExtension.equals("ppm")) {
      return view.importPPM();
    } else if ((orderedExtension.equals("png")) || (orderedExtension.equals("jpg")) || (orderedExtension.equals("jpeg"))) {
      return view.importImage();
    } else {
      throw new IOException("Unable to read file");
    }
  }

  public void goToExport() {
    FileHandlerImpl view = new FileHandlerImpl(img);
    if (type == "ppm") {
      view.exportPPM(fileName);
    } else {
      view.exportImage(fileName, type);
    }
  }
}
