# HalflifeBits
This java program takes input from a geiger counter over GPIO pin 7 and uses it to generate true randomness. It also provides some simple functions for choosing numbers randomly. This application is intended to be run on a raspberry pi and requires sudo access to install.

# Installation
To install dependencies use the command `bash cmd.sh -i`
To build the application use the command `bash cmd.sh -b`
To run the application use the command `bash cmd.sh -r`

# pi4j Information
The application uses WiringPi and the pi4j library to access the GPIO pins.
WiringPi: http://wiringpi.com
pi4j: https://pi4j.com/
Sample applications from pi4j can be found here: https://github.com/Pi4J/pi4j-v1/tree/master/pi4j-example/src/main/java

