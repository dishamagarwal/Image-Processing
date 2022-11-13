![Overview Image](https://github.com/dishamagarwal/Image-Processing/blob/main/res/MosaicCheetah1000.ppm?raw=true)

--------------------------------------------------------------
--------------------Changes made to Design--------------------
--------------------------------------------------------------
corrected mistakes of older design:
1. Made and returned deep copies in public methods like sepia an blur
   instead of passing direct references.
2. Wrote javadoc for interfaces and for methods with missing explanation
3. Made the image (this.img) field in the model private and used the public
   deep copy method to generate copies of the images created after a function
   is applied in order to test the function.
4. Moved import and export public functions to view in order to maintain
   the MVC format.
5. Created packages to help organise the classes and interfaces into a
   model view and controller in the src folder and the images in the res folder.
6. I made an interface Filehandler which are all the methods that are needed to export, and import
   files of type PPM, JPEG, and PNG. The class FileHandlerImp is the class that applies all these
   methods.
7. For ImageProcessingModel, I made it so it extended FileHandlerImp instead of having the methods
   for dealing with files. For the last assignment, it wasn't too much disturbance having two
   methods to deal with the creation and exportation of files, but with added functionalities it is
   easier and more organized to have the Model inherit a class with needed functionalities already
   defined.
8. I also compressed interfaces ITransform and IFilter into the interface ImageProcessing. Since
   there might be more we might add to process an image, and with the increasing number of files,
   it is more organized to have the Model implement just one interface instead of two.
9. added methods which were not present earlier in the multiLayers class such as changing the
   visibility of a layer.
10.Added deepCopy to the interface in order to return the current image when prompted
11.Extracted the interface for multiLayers as we did not have that earlier.
12.Changed the view according to the new gui to incorporate it


--------------------------------------------------------------
------------Changes made for Extra Credit Features------------
--------------------------------------------------------------
we didn't change anything as the class extended the earlier multilayer class!


Pixel is class with private fields of the pixel's position - where Pixel position is an int for column and row.
Also Pixel class contains ints with redValue, 
the greenValue, and the blueValue - that represent the value of that color value.

In the IFilters interface, it contains all the methods for the filters - such 
as blur and sharpen. In ITransform, it contains the methods for the transformations - such
as greyscale and sepia. We did this in order to contain everything that is considered 
a Filter, and/or a Transformation methods separate and in their own categories.

The model is where IFilter and ITransform get implemented, along with a 2D Pixel array being
the main way to represent a sequence of pixels. 

--------------------------------------------------------------------
--------------------------Image Citations:--------------------------
--------------------------------------------------------------------
The cheetahs image comes from: https://pixabay.com/photos/cheetahs-animals-safari-5689873/
The highway image comes from: https://pixabay.com/photos/highway-wilderness-yellow-line-5314645/

We designed two interfaces, one with the methods of the filters and the other with the methods for the transformation.

--------------------------------------------------------------------
-------------------------Design Explanation:------------------------
--------------------------------------------------------------------
Our ImageProcessingModel implemented these two interfaces along with some other private helper method and one public export method.

There are a total of four constructors to the class
1. With no parameters
A default constructor which generates a random checked board of size 100 by 100
2. a constructor that takes in the height and with of an image
and it generates a randomised computer image
3. A constructor which takes in the 2D array of pixels
and provides it to the class for manipulation
4. A constructor that takes in an image location
this string locates the image in the folder and converts the ppm image to a 2D array of pixels

private Pixel[][] generateCheckerBoard(int column, int row)
generates a randomised board with alternative checks
@return an array of pixels that represent the image of a checkered board
@throws IllegalArgumentException when column or row is negative

public Pixel[][] blur()
using a pre defined mathematical formula, it blurs the image accordingly

public Pixel[][] sharpen()
using a pre defined mathematical formula, it sharpens the image accordingly

private Pixel[][] applyingKernelToEachPixel(Pixel[][] img, double[][] kernel)
helper method used by blur and sharpen to apply the given filter to each pixel and form a new image according to that.
returns the 2D array of pixels after applying the given formula

private double[][] makeKernelForBlur()
makes a kernel for the blur function

private double[][] makeKernelForSharpen()
makes a kernel for the sharpen function

private Pixel[][] clamp(Pixel[][] img)
clamps the values of the image to stay within the range of 0-255.

public Pixel[][] greyscale()
turns the image into a greyscale image by converting the values r g b accordingly.
returns the newly made greyscale 2D array of pixels

public Pixel[][] sepiaTone()
adds a filter to the colors of the image using the 2D array of the class.
and returns a new 2D array with the pixels with an added value

private Pixel[][] separatePixels(Pixel[][] img)
extract the next three pixels in order and sends them to the classes respectively based on their position to be altered.
returns the new image which has been altered according to the guidelines

private Pixel applySepiaPixel(Pixel pixel, int i, int j)
helper method that applies the set of values to the rgb pixel and returns a new pixel.
returns a new pixel formed with the altered colors of the older one.

public void exportPPM(ImageProcessingModel imgToExport, String fileName)
This method creates a PPM file from the image given, and names it the string given.

private Pixel[][] importPPM(String filename)
Takes the filename and imports it into a 2D array to be used by the program.
returns the newly formed 2D array of pixels from the image provided.
