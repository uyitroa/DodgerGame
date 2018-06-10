import numpy as np
import mahotas as mh
import pylab
import random

print "imported, now creating"

img = np.zeros((1700,1500))

for x in range(0,1700,100):
	for y in range(0,1500,100):
		black = random.randint(0,1)
		x_shape = random.randint(25,100)
		y_shape = random.randint(25,100)
		if black:
			color = random.randint(0,255)
			img[x : x + x_shape, y : y + y_shape] = color
		else:
			img[x : x + x_shape, y : y + y_shape] = 0
		print img[x,y]
print img
img = img.astype(np.uint8)
pylab.imshow(img)
pylab.show()
mh.imsave("download.jpeg",img)
