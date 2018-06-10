import numpy
from skimage.io import imread
from pylab import imshow, show

def openFile():
	name = input("file name: ")
	image = imread(name)
	image = image[:,:,0]
	return image

class Detect:
	def __init__(self, matrix):
			self.matrix = matrix
	
	def edge(self):
		nRow = len(self.matrix)
		nCol = len(self.matrix[0])
		new = numpy.zeros((nRow, nCol))
		
		for row in range(nRow):
			for column in range(0,nCol):
				currentColor = self.matrix[row][column]
				if row + 1 < nRow:
					nextColor = self.matrix[row + 1][column]
					if not (currentColor + 15 > nextColor and currentColor - 15 < nextColor):
						new[row + 1][column] = 1
				if column + 1 < nCol:
					nextColor = self.matrix[row][column + 1]
					print(currentColor, nextColor)
					if not (currentColor + 15 > nextColor and currentColor - 15 < nextColor):
						new[row][column + 1] = 1
		return new
def test():
	myMatrix = openFile()
	print(myMatrix.shape)
	detect = Detect(myMatrix)
	myMatrix = detect.edge()
	imshow(myMatrix)
	show()
	
if __name__ == "__main__":
	test()
