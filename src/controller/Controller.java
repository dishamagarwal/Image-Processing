package controller;

import image.Pixel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.IEditExtended;
import model.IEditExtendedImpl;
import view.FileHandler;
import view.FileHandlerImpl;
import view.ImageProcessingView;

public class Controller implements ActionListener {

  private IEditExtended model;
  private ImageProcessingView view;
  String openPath;

  public Controller(ImageProcessingView view) throws IOException {
    this.view = view;

    view.setListener(this);
    view.display();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "Greyscale":
        Pixel[][] i = model.greyscale();
        FileHandler img = new FileHandlerImpl(i);
        img.exportImage("greyscaleEffect", "jpeg");
        String greyPath = img.getPath();
        view.creatImagePanel(greyPath);
        break;
      case "Sepia":
        Pixel[][] sepia = model.sepiaTone();
        FileHandler sepiaFile = new FileHandlerImpl(sepia);
        sepiaFile.exportImage("sepiaEffect", "jpeg");
        String sepiaPath = sepiaFile.getPath();
        view.creatImagePanel(sepiaPath);
        break;
      case "Blur":
        Pixel[][] blur = model.blur();
        FileHandler blurFile = new FileHandlerImpl(blur);
        blurFile.exportImage("blurEffect", "jpeg");
        String blurPath = blurFile.getPath();
        view.creatImagePanel(blurPath);
        break;
      case "Sharpen":
        Pixel[][] sharpen = model.sharpen();
        FileHandler sharpenFile = new FileHandlerImpl(sharpen);
        sharpenFile.exportImage("sharpenEffect", "jpeg");
        String sharpenPath = sharpenFile.getPath();
        view.creatImagePanel(sharpenPath);
        break;
      case "Mosaic":
        Pixel[][] m = model.getPixelArray();
        int col = m.length;
        int row = m[0].length;
        int numOfPixels = (col - 2) * (row - 2);
        Pixel[][] mosaic = model.mosaic(0, numOfPixels);
        FileHandler mosaicFile = new FileHandlerImpl(mosaic);
        mosaicFile.exportImage("mosaicEffect", "jpeg");
        String mosaicPath = mosaicFile.getPath();
        view.creatImagePanel(mosaicPath);
        break;
      case "Downscaling":
        Pixel[][] d = model.getPixelArray();
        int c = d.length;
        int r = d[0].length;
        model.downscaling((c - 2), (r - 2));
        Pixel[][] ds = model.getLayers().get(0).image();
        FileHandler downsizeFile = new FileHandlerImpl(ds);
        downsizeFile.exportImage("downscalingEffect", "jpeg");
        String downsizePath = downsizeFile.getPath();
        view.creatImagePanel(downsizePath);
        break;
      case "Open file": {
        final JFileChooser fchooser = new JFileChooser(".");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "JPG & GIF Images", "jpg", "gif");
        fchooser.setFileFilter(filter);
        int retvalue = fchooser.showOpenDialog(this.view);
        if (retvalue == JFileChooser.APPROVE_OPTION) {
          File f = fchooser.getSelectedFile();
          openPath = f.getAbsolutePath();
          try {
            this.model = new IEditExtendedImpl(openPath);
          } catch (IOException ioException) {
            ioException.printStackTrace();
          }
          FileHandler imgToOpen = null;
          imgToOpen = new FileHandlerImpl(openPath);
          try {
            imgToOpen.importImage();
          } catch (IOException ioException) {
            ioException.printStackTrace();
          }
          view.creatImagePanel(openPath);
        }
      }
      break;
    case "Save file": {
      final JFileChooser fchooser = new JFileChooser(".");
      int retvalue = fchooser.showSaveDialog(this.view);
      if (retvalue == JFileChooser.APPROVE_OPTION) {
        File f = fchooser.getSelectedFile();
        String savePath = f.getAbsolutePath();
        FileHandler imgToSave = null;
        imgToSave = new FileHandlerImpl(savePath);

        int index = savePath.lastIndexOf('.');
        String filename = savePath.substring(0, index);
        String type = savePath.substring(index + 1);
        imgToSave.exportImage(filename, type);

        JOptionPane.showMessageDialog(view, "Image was saved at: "
            + imgToSave.getPath(), "Message", JOptionPane.PLAIN_MESSAGE);
        break;
      }
    }
    break;
    }
  }
}