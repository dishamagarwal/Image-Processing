package view;

import controller.Controller;
import java.io.IOException;

/**
 * This class makes the GUI for the application.
 */
public class ImageProcessingGUI {

  /**
   * The main class to be called.
   * @param args String array
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {

    //model initiallized in the controller
    ImageProcessingView view = new ImageProcessingView();

    Controller controller = new Controller(view);

  }
}
