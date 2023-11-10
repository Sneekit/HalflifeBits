# run
if [ $1 = "-r" -o $1 = "r" -o $1 = "run" ]; then
	python gpiotest.py
	exit 0
fi

# run in background
if [ $1 = "-b" -o $1 = "b" -o $1 = "background" ]; then
	if [ $(pgrep -f "gpiotest.py") ]; then
		echo "Halflife Bits is already running."
	else
		nohup python3 gpiotest.py background &
		echo "Halflife Bits started"
	fi
	exit 0
fi

# kill
if [ $1 = "-k" -o $1 = "k" -o $1 = "kill" ]; then
	pid=$(pgrep -f "gpiotest.py")
	kill $pid
	exit 0
fi

# kill
if [ $1 = "-c" -o $1 = "c" -o $1 = "cneck" ]; then
	if [ $(pgrep -f "gpiotest.py") ]; then
		echo "Halflife Bits is running."
	else
		echo "Halflife Bits is not running."
	fi
	exit 0
fi

echo "Usage: sudo bash cmd.sh <argument>"
echo "r: run locally"
echo "b: run in background"
echo "k: kill processes"
echo "c: check if running"