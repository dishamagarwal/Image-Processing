package view;

import javax.swing.event.ListSelectionListener;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;

/**
 * The class that constructs the image.
 */
public class ImageProcessingView extends JFrame implements ItemListener, ListSelectionListener {

  private JPanel mainPanel;
  private JScrollPane mainScrollPane;

  private JPanel imagePanel;

  private JCheckBox[] checkBoxes;
  JButton fileOpenButton;
  JButton fileSaveButton;

  /**
   * This constructs the view.
   */
  public ImageProcessingView() {
    super();
    setTitle("Swing features");
    setSize(400, 400);


    mainPanel = new JPanel();
    //for elements to be arranged vertically within this panel
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    //scroll bars around this main panel
    mainScrollPane = new JScrollPane(mainPanel);
    add(mainScrollPane);

    //text area
    JTextArea textArea = new JTextArea(5, 5);
    textArea.setBorder(BorderFactory.createTitledBorder("Regular text area"));
    mainPanel.add(textArea);

    //dialog boxes
    JPanel filePanel = new JPanel();
    filePanel.setBorder(BorderFactory.createTitledBorder("File Options"));
    filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.PAGE_AXIS));
    mainPanel.add(filePanel);

    //file open
    JPanel fileopenPanel = new JPanel();
    fileopenPanel.setLayout(new FlowLayout());
    filePanel.add(fileopenPanel);
    fileOpenButton = new JButton("Open a file");
    fileOpenButton.setActionCommand("Open file");
    fileopenPanel.add(fileOpenButton);

    //file save
    JPanel filesavePanel = new JPanel();
    filesavePanel.setLayout(new FlowLayout());
    filePanel.add(filesavePanel);
    fileSaveButton = new JButton("Save a file");
    fileSaveButton.setActionCommand("Save file");
    filesavePanel.add(fileSaveButton);

    JPanel imageEffectPanel = new JPanel();
    imageEffectPanel.setBorder(BorderFactory.createTitledBorder("Effects"));
    imageEffectPanel.setLayout(new BoxLayout(imageEffectPanel, BoxLayout.PAGE_AXIS));
    mainPanel.add(imageEffectPanel);

    //add menu for each effect
    String[] effects = {"Greyscale", "Sepia", "Blur", "Sharpen", "Mosaic", "Downscaling"};
    checkBoxes = new JCheckBox[effects.length];
    //ButtonGroup group = new ButtonGroup();
    for (int i = 0; i < checkBoxes.length; i++) {
      checkBoxes[i] = new JCheckBox(effects[i]);
      checkBoxes[i].setSelected(false);
      checkBoxes[i].setActionCommand(effects[i]);
      checkBoxes[i].addItemListener(this);
      imageEffectPanel.add(checkBoxes[i]);
    }
    mainPanel.add(imageEffectPanel);

    //show an image with a scrollbar
    imagePanel = new JPanel();
    //a border around the panel with a caption
    imagePanel.setBorder(BorderFactory.createTitledBorder("Showing selected image"));
    imagePanel.setLayout(new GridLayout(1, 0, 10, 10));
    //imagePanel.setMaximumSize(null);
    mainPanel.add(imagePanel);

  }

  @Override
  public void itemStateChanged(ItemEvent e) {
    String who = ((JCheckBox) e.getItemSelectable()).getActionCommand();

    switch (who) {

      case "Sharpen":
        if (e.getStateChange() == ItemEvent.SELECTED) {
        }
    }
  }

  /**
   * Adds an action listener to the button.
   * @param listener controller
   */
  public void setListener(ActionListener listener) {
    fileOpenButton.addActionListener(listener);
    fileSaveButton.addActionListener(listener);

     for(int i = 0; i < checkBoxes.length; i ++){
       checkBoxes[i].addActionListener(listener);
     }

  }

  public void display() {
    setVisible(true);
  }

  @Override
  public void valueChanged(ListSelectionEvent e) {
    //needs to be here bc of what is implemented
  }

  public void creatImagePanel(String path) {
    JLabel imageLabel = new JLabel();
    JScrollPane imageScrollPane = new JScrollPane(imageLabel);
    imageLabel.setIcon(new ImageIcon(path));
    imageScrollPane.setPreferredSize(new Dimension(100, 600));
    imagePanel.add(imageScrollPane);

    JPanel editedImagePanel = new JPanel();
    editedImagePanel.setBorder(BorderFactory.createTitledBorder("Image"));
    editedImagePanel.setLayout(new GridLayout(1, 0, 10, 10));
    mainPanel.add(editedImagePanel);
  }

}
