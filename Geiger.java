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
	int Counter = 0;
	GpioPinDigitalInput GeigerSignal = null;

	public Geiger()
	{
		System.out.println("GPIO listener started for pin 7.");

		// Create GPIO Controller
		GpioController gpio = GpioFactory.getInstance();

		// Provision pin as an input pin
		//GeigerSignal = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, PinPullResistance.PULL_DOWN);
		GeigerSignal = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07);
		GeigerSignal.setShutdownOptions(true);

		// create and register pin listener listener
        GeigerSignal.addListener(new GpioPinListenerDigital() 
		{
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) 
			{
				HandleGeigerEvent(LocalDateTime.now(), 7, event.getState().toString());
            }

        });
	}

	public void HandleGeigerEvent(LocalDateTime time, int pin, String state)
	{
		//System.out.println(" --> GPIO PIN STATE CHANGE: " + pin + " = " + state);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSSSS");
		System.out.println("[" + dtf.format(time) + "]" + " - Decay detected");
	}

	public void SimulateGeiger()
	{
		while (true)
		{
			if (Counter == 2147483647)
				Counter = 0;

			Counter += 1;

			if (Counter % 10000 == 0)
			{
				Random r = new Random(Counter);
				int result = r.nextInt(25000);
				if (result == 0)
					HandleGeigerEvent(LocalDateTime.now(), 7, "HIGH");
			}
		}
	}
}