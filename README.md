# Overview
This project is an exercise in how to use [MetroTransit's API](http://svc.metrotransit.org/).

The goal here is to create a program that will take a BUS ROUTE, BUS STOP, and DIRECTION as its inputs, and
returns how long until the next bus arrives. Because all the inputs are strings, separation by quotation marks are necessary
For example:

* BUS ROUTE - the string name of the bus route.
* BUS STOP - the string name of the bus stop.
* DIRECTION - the direction in which the route is going. Only cardinal directions "north", "south", "east", and "west" are allowed.
I've made it so it shouldn't matter if there's crazy letter casing.

The goal is to have proper input be the following:
```
$ java -jar metronext.jar [BUS ROUTE] [BUS STOP] [DIRECTION]
```
For example (using arbitrary inputs):
```
$ java -jar metronext.jar “Express - Target - Hwy 252 and 73rd Av P&R - Mpls” “Target North Campus Building F” “south”
$ Next bus in 10 minutes
```
# Running
Development and tests were done using Eclipse. To run the project locally, you may either clone or download the repository, and then import the project to Eclipse. From there, the project may also be exported as a .jar file and run from the command line.
