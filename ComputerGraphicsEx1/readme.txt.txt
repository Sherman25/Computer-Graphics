- Implementation of the "showSeams" method.
When user calls the showSeams method, the output should be original image
with red colored vertical seams. We defined seamsMap a 2 Dimesional array that keeps
boolean variables. If pixel(i, j) was choosen in seam path, then the value in the
seamsMap(i, j) will be True, otherwise False.
Then during building the result image to the output, we will check in the seamsMap
if the pixel(i, j) is in the seam, if True, we will color pixel(i, j) to the Red color,
otherwise it will keep the original color.
