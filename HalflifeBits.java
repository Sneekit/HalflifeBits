class HalflifeBits
{
	public static void main(String[] args) throws InterruptedException
	{

		// Set current debug state
		boolean Debug = true;

		// Instantiate classes
		PostgresConnector Connector = new PostgresConnector(Debug);
		Geiger Geiger = new Geiger(Connector);

		// Simulates a geiger counter in debug mode
		if (Debug)
		{
			new Thread(() -> { Geiger.simulateGeiger(); }).start();
		}

		// Keep the program alive
		while (true)
		{
			Thread.sleep(1000);
		}
	}
}