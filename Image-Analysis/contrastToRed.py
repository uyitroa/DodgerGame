import numpy as np
import pylab
import mahotas as mh

dna = mh.imread("dna.jpeg")

dnaf = mh.gaussian_filter(dna,16)
print("filtered")

dnaf = dnaf.astype(np.uint8)

T = mh.thresholding.otsu(dnaf)

dist = mh.distance(dnaf > T)
dist = dist.max() - dist
dist -= dist.min()
dist = dist/float(dist.ptp()) * 255
dist = dist.astype(np.uint8)

pylab.imshow(dist)
pylab.jet()
pylab.show()
