# this will run the program in the background

if [ $(pgrep -f "gpiotest.py") ]; then
	echo "gpiotest.py already running."
else
	nohup python3 gpiotest.py &
	echo "gpiotest.py started"
fi
