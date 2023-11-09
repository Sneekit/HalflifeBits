#!/usr/bin/env python

from time import sleep
import datetime
import RPi.GPIO as GPIO
import json
import os 

results_dict = {}
if os.path.getsize('dictionary.txt') == 0:
	for i in range(100):
		results_dict[i + 1] = 0
else:
	with open('dictionary.txt') as f: 
		loaded_dict = json.load(f)
		results_dict = {int(key): value for key, value in loaded_dict.items()}
		open('dictionary.txt', 'w').close()

# Set's GPIO pins to BCM GPIO numbering
GPIO.setmode(GPIO.BOARD) 
INPUT_PIN = 15
GPIO.setup(INPUT_PIN, GPIO.IN)

# Create a function to run when the input is high
def geigerHit(channel):
	timestamp = datetime.datetime.now()
	rnd_nmbr = int(timestamp.strftime('%f')[-2:])
	if rnd_nmbr == 0:
		rnd_nmbr = 100

	results_dict[rnd_nmbr] += 1

	print(f"[{timestamp}] - Decay detected! . . . Random Number: {rnd_nmbr}")

# Wait for the input to go low, run the function when it does
GPIO.add_event_detect(INPUT_PIN, GPIO.FALLING, callback = geigerHit)

try:
	while True:
		sleep(100)
except KeyboardInterrupt:
	print(results_dict)
	with open('dictionary.txt', 'w') as convert_file: 
		convert_file.write(json.dumps(results_dict))
# Start a loop that never ends
