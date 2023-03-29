# Vehicle_Range_Comparator
This is a software that is capable of comparing the range of both electric vehicles ( denoted by EV) and internal combustion engine vehicles ( denoted by ICE).

The application takes the details of a pair of veicles, one at a time, compares their range and prints out which has the largest range, and by what margin.

Imput format : type (ice/ev) year name (one word) capacity (fuel tank or battery) consumption (mpg or Whpm).

The range of an ICE vehicle is defined as the number of of miles that can be driven on a single tank of fuel. I calculated it as the product of the tank's capacity, in gallons, by the vehicle's fuel consumption, in miles per gallon (mpg).

The range of an electric vehicle (EV) is defined as the number of miles that can be driven on a single charge. I calculated it as the quotient of the division of the battery's capacity, in Watt-hour (Wh), by the vehicle's fuel consumption, in Watt-hours per mile (Whpm).

When running the application, an example of valid vehicle descriptions include ice 1928 FordModelA 11 14 and ev 1902 StudebakerElectric 10000 500.

This application is mainly/entirely developed in Java and it is a consolidation of my skills in data Structures and Object Oriented Programming with an implementation of interfaces and abstract classes. 
