package model;

import image.Pixel;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface IMultiLayer extends IEdit {

  void duplicateLayer(String name, int duplicateOfLayer);

  void addLayer(Pixel[][] layerToAdd, String name);

  public void addBlankLayer();

  void removeLayer(int num);

  void exportFirstLayer(String filename, String type);

  void exportIndexedLayer(int index, String filename, String type);

  void exportEveryLayer(String type);

  void removeTopMostLayer();

  Pixel[][] getTopMostLayer() throws IllegalStateException;

  Pixel[][] getSpecificLayer(int index) throws IllegalStateException;

}
