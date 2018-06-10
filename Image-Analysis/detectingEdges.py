import numpy
from skimage.io import imread
from skimage.feature import canny
import mahotas as mh # for thresholding only 

from pylab import imshow, show

name = raw_input("Name file: ")
image = imread(name)

if len(image.shape) == 3:
	image = image[:,:,0] # convert to 2d array

T = mh.thresholding.otsu(image)

edges = canny(image, sigma = 1, low_threshold = T - 30, high_threshold = T + 20)
imshow(edges)
show()
