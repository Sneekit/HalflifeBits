#!/usr/bin/env python

from time import sleep
import datetime
import RPi.GPIO as GPIO
import json
import os 


FILE_NAME = 'Output/random_number_results.txt'
RESULTS_DICT = {}

if not os.path.exists(FILE_NAME) or os.path.getsize(FILE_NAME) == 0:
	for i in range(100):
		RESULTS_DICT[i + 1] = 0
else:
	with open(FILE_NAME) as f: 
		loaded_dict = json.load(f)
		RESULTS_DICT = {int(key): value for key, value in loaded_dict.items()}
		print("Successfully loaded existing results.")
		print(RESULTS_DICT)

INPUT_PIN = 15
GPIO.setmode(GPIO.BOARD) 
GPIO.setup(INPUT_PIN, GPIO.IN)
print(f"Started listener on Board Pin {INPUT_PIN}.")

# Create a function to run when the input is high
def geigerHit(channel):
	timestamp = datetime.datetime.now()
	rnd_nmbr = int(timestamp.strftime('%f')[-2:])
	if rnd_nmbr == 0:
		rnd_nmbr = 100

	RESULTS_DICT[rnd_nmbr] += 1

	print(f"[{timestamp}] - Decay detected! . . . Random Number: {rnd_nmbr}")

def saveFile():
	with open(FILE_NAME, 'w') as file: 
		file.write(json.dumps(RESULTS_DICT))

# Wait for the input to go low, run the function when it does
GPIO.add_event_detect(INPUT_PIN, GPIO.FALLING, callback = geigerHit)

try:
	while True:
		# Start a loop that never ends, saving the file every 100 seconds
		saveFile()
		sleep(10)

except KeyboardInterrupt:
	print(RESULTS_DICT)
	saveFile()
