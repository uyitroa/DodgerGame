print "importing module please wait..."
import numpy
import pylab
import mahotas


image = raw_input("image name: ")
img = mahotas.imread(image)
print " filtering"
imgf = mahotas.gaussian_filter(img,8)
print "gaussian filtered now thresholding"

if str(imgf.dtype) != 'uint8':
	imgf = imgf.astype(numpy.uint8)

	T = mahotas.thresholding.otsu(imgf)


labeled, nbr = mahotas.label(imgf > T)
print "objet : ",nbr

pylab.imshow(labeled)

pylab.show()
