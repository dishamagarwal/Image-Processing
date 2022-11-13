package image;

/**
 * This class represents a layer in an Image.
 */

public class Layer {
  private Pixel[][] img;
  private String name;
  private boolean visible;

  /**
   * The constructor for a Layer. A layer contains an image, the name of the layer,
   * and if the layer is visible.
   * @param img the image as an array of pixels
   * @param name the name assigned to the layer
   * @param visible if the layer is visible or not
   */
  public Layer(Pixel[][] img, String name, boolean visible) {
    this.img = img;
    this.name = name;
    this.visible = visible;
  }

  public boolean visibility() {
    return visible;
  }

  public void setVisibility(boolean visability) {
    this.visible = visability;
  }

  public Pixel[][] image() {
    return this.img;
  }

  public void changeName(String name) {
    this.name = name;
  }
}
