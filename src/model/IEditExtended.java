package model;

import image.Layer;
import image.Pixel;
import java.util.List;

/**
 * This is the extended interface of the effects you can apply on an image.
 */

public interface IEditExtended extends IEdit {

  List<Pixel[][]> downscaling(int width, int height);

  List<Pixel[][]> downscaling(List<Pixel[][]> img, int width, int height);

  Pixel[][] mosaic(int layerNumber, int numOfPixels);

  Pixel[][] downscalingEachImage(Pixel[][] img, int width, int height);

  List<Layer> getLayers();

  Pixel[][] getPixelArray();
}
