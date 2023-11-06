# install
if [ $1 = "-i" -o $1 = "i" -o $1 = "install" ]; then
	echo "Installing dependencies"

	# install wiringPi
	git clone https://github.com/WiringPi/WiringPi.git
	cd WiringPi/
	sudo ./build
	cd ..
	rm -rf WiringPi

	# install pi4j
	curl -sSL https://pi4j.com/install | sudo bash
	
	echo "Done"
	
	exit 0
fi

# build
if [ $1 = "-b" -o $1 = "b" -o $1 = "build" ]; then
	echo "Building HalflifeBits"
	
	javac -classpath .:classes:/opt/pi4j/lib/'*':./Libraries/'*' HalflifeBits.java

	echo "Done"

	exit 0
fi

# run
if [ $1 = "-r" -o $1 = "r" -o $1 = "run" ]; then
	echo "Running HalflifeBits"

	java -classpath .:classes:/opt/pi4j/lib/'*':./Libraries/'*' HalflifeBits

	exit 0
fi

# clean
if [ $1 = "-c" -o $1 = "c" -o $1 = "clean" ]; then
	rm *.class > /dev/null 2>&1

	echo "Done removing compiled files"
	
	exit 0
fi

echo "Invalid Command."
echo "-i = install"
echo "-b = build"
echo "-r = run"
echo "-c = clean"
