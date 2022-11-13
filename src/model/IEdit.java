package model;

import image.Pixel;

/**
 * The interface that contains the methods for applying filters and transformations to the image.
 */
public interface IEdit {

  /**
   * adjusts the rgb value of the image based on the surrounding values and results in
   * blurring the image.
   * @return the blurred image that was created in this method
   */
  Pixel[][] blur();

  /**
   * changes the rgb value of the image by applying a kernel to the surrounding pixels
   * and changing the pixel value accordingly.
   * @return the sharpened image resulted from the above mentioned method
   */
  Pixel[][] sharpen();

  /**
   * turns the image into b/w by adjusting the rgb values.
   * @return the new image formed in black and white
   */
  Pixel[][] greyscale();

  /**
   * gives a sepia hue to the image by changing the rgb value of the pixels.
   * @return the new image formed in sepia
   */
  Pixel[][] sepiaTone();

  Pixel[][] makeDeepCopy();
}
