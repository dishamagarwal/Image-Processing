package image;

/**
 * represents a pixel of an image along with its rgb values.
 */
public class Pixel {

  private int column;
  private int row;
  private int redValue;
  private int greenValue;
  private int blueValue;

  /**
   * constructor for the pixel class.
   * @param column      the colum numbrr of the pixel.
   * @param row         the row to which the pixel belongs
   * @param redValue    red value
   * @param greenValue  green value
   * @param blueValue   blue value
   */
  public Pixel(int column, int row, int redValue, int greenValue, int blueValue) {
    this.column = column;
    this.row = row;
    this.redValue = redValue;
    this.blueValue = blueValue;
    this.greenValue = greenValue;
  }

  /**
   * gives the red value.
   */
  public int getRedValue() {
    return redValue;
  }

  /**
   * gives the green value.
   */
  public int getGreenValue() {
    return greenValue;
  }

  /**
   * gives the blue value.
   */
  public int getBlueValue() {
    return blueValue;
  }

  /**
   * gives the red value.
   */
  public void setRedValue(int value) {
    this.redValue = value;
  }

  /**
   * gives the green value.
   */
  public void setGreenValue(int value) {
    this.greenValue = value;
  }

  /**
   * sets the blue value.
   */
  public void setBlueValue(int value) {
    this.blueValue = value;
  }

  public int getRow() {
    return this.row;
  }

  public int getColumn() {
    return this.column;
  }
}
