Overview

The concept of ray tracing: a technique for generating an image by tracing the path of light
through pixels in an image plane and simulating the effects of its encounters with virtual
objects. The technique is capable of producing a very high degree of visual realism, usually
higher than that of typical scan-line rendering methods, but at a greater computational cost.

The objective of this exercise is to implement a ray tracing engine. A ray tracer shoots rays from
the observer’s eye through a screen and into a scene of objects. It calculates the ray’s
intersection with the objects, finds the nearest intersection and calculates the color of the
surface according to its material and lighting conditions. (This is the way you should think about it –
this will help your implementation).