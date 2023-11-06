import java.util.Random;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class Geiger
{
	GpioPinDigitalInput geigerSignal = null;
	PostgresConnector db = null;
	int counter = 0;
	int gpio_pin = 7;

	/**
	 * Constructor for the Geiger class with dependencies injected
	 */
	public Geiger(PostgresConnector postgresConnector)
	{
		db = postgresConnector;
		System.out.println("GPIO listener started for pin " + gpio_pin + ".");

		// Create GPIO Controller
		GpioController gpio = GpioFactory.getInstance();

		// Provision pin as an input pin
		// GeigerSignal = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07);
		geigerSignal = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, PinPullResistance.PULL_DOWN);
		geigerSignal.setShutdownOptions(true);

		// Create and register to internal event when GPIO pin voltage drops
        geigerSignal.addListener(new GpioPinListenerDigital() 
		{
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) 
			{
				String state = event.getState().toString();
				handleGeigerEvent(LocalDateTime.now(), gpio_pin);

            }

        });
	}

	/**
	 * Handles the GPIO pin voltage down event
	 * 
	 * @param time		The time of the event
	 * @param pin		The integer representation of the board layout pin number
	 */
	public void handleGeigerEvent(LocalDateTime time, int pin)
	{
		// Format the current datetime to microsecond precision
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSSSS");
		String timeStamp = dtf.format(time);
		System.out.println("[" + timeStamp + "]" + " - Decay detected");

		// Insert the timestamp into the database
		String query = new StringBuilder()
			.append("INSERT INTO decays (timestamp)")
			.append("VALUES(?);")
			.toString();

		String[] params = new String[] { timeStamp };
		db.executeQuery(query, params);
	}

	/**
	 * Simulates a geiger counter for testing code in debug
	 */
	public void simulateGeiger()
	{
		while (true)
		{
			counter += 1;

			if (counter == 1000000)
			{
				counter = 0;
				Random r = new Random(System.currentTimeMillis());
				int result = r.nextInt(500);
				if (result == 0)
					handleGeigerEvent(LocalDateTime.now(), gpio_pin);
			}
		}
	}
}