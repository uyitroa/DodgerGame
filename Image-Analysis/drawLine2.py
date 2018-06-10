import numpy as np
from pylab import imshow, show

class Line:

	def __init__(self,interface,x_start,y_start,x_end, y_end):
		# Line(interface, xStart, yStart, xEnd, yEnd
		self.interface = interface
		self.x_start = x_start
		self.y_start = y_start
		self.x_end = x_end
		self.y_end = y_end
		self.unit_y, self.unit_x = 1,1

	def calculate(self):
		# get x, y value with algorithm:
		# goal: same amount of position ex: x = [0,1,2], x has 3 pos, so y need 3 pos, y = [a,b,c]
		""" amount = (distance betweeen x start and x end) divide by unit x
		    same amount = distance between y start and y end) divide by unit y
		    since we don't have 3 unknown, we need to set one unit to 1.
		    but unit can't be > 1 cuz there will be some distance between 2 pixel => no line anymore
		    so check the suitable unit (x or y) to set it 1.
		    so we calculate now the amount of pos. finally we can get the value of the other unit"""
		if (self.x_end - self.x_start)> (self.y_end - self.y_start):
			self.times = self.x_end - self.x_start
			self.unit_y = float((self.y_end - self.y_start)) / float(self.times)
 
		else:
			self.times = self.y_end - self.y_start
			self.unit_x = float((self.x_end - self.x_start)) / float(self.times)


	def get_rrCC(self):
		self.rr = [self.x_start]
		self.cc = [self.y_start]
		current_x = self.x_start
		current_y = self.y_start

		for loop in range(self.times):
			current_x += self.unit_x
			current_y += self.unit_y
	
			self.rr.append(int(current_x)) # list of horizontal position
			self.cc.append(int(current_y)) # list of vertical position

	def draw(self):
		self.calculate()
		self.get_rrCC()

		self.interface[self.cc,self.rr] = 1
		#print self.interface
		return self.rr, self.cc, self.interface

def test():
	img = np.zeros((110,110), dtype = np.uint8)
	my_line = Line(img, 1,1,1,100)
	rr,cc,img = my_line.draw()
	imshow(img)
	show()
	print img

if __name__ == "__main__":
	test()
