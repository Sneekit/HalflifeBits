class HalflifeBits
{
	public static void main(String[] args) throws InterruptedException
	{
		// Instantiate classes
		PostgresConnector connector = new PostgresConnector();
		Geiger geiger = new Geiger(connector);

		// Set current debug state
		boolean debug = true;

		// Simulates a geiger counter in debug mode
		if (debug)
		{
			new Thread(() -> { geiger.simulateGeiger(); }).start();
		}

		// Keep the program alive
		while (true)
		{
			Thread.sleep(1000);
		}
	}
}