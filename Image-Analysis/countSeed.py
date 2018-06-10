import numpy as np
import mahotas as mh
import pylab

dna = mh.imread("dna.jpeg")

dnaf = mh.gaussian_filter(dna,16)
print "filtered"
if str(dnaf.dtype) != 'uint8':
	dnaf = dnaf.astype('uint8')

T = mh.thresholding.otsu(dnaf)

#labeled, nbr = mh.label(dnaf > T)

rmax = mh.regmax(dnaf)
#pylab.imshow(mh.overlay(dna, rmax))
#pylab.show()
seeds,nr_nuclei = mh.label(rmax)
print nr_nuclei
