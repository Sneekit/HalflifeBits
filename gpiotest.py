#!/usr/bin/env python

from time import sleep
import datetime
import RPi.GPIO as GPIO

# Set's GPIO pins to BCM GPIO numbering
GPIO.setmode(GPIO.BOARD) 
INPUT_PIN = 15
GPIO.setup(INPUT_PIN, GPIO.IN)

# Create a function to run when the input is high
def geigerHit(channel):
	timestamp = datetime.datetime.now()
	print(f"[{timestamp}] - Decay detected!")

# Wait for the input to go low, run the function when it does
GPIO.add_event_detect(INPUT_PIN, GPIO.FALLING, callback = geigerHit)

# Start a loop that never ends
while True:
	# Sleep for a full second before restarting our loop
	sleep(1)