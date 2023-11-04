class HalflifeBits
{
	public static void main(String[] args) throws InterruptedException
	{
		Geiger geiger = new Geiger();
		boolean debug = true;

		// This simulates a geiger counter
		if (debug)
		{
			new Thread(() -> {
				geiger.SimulateGeiger();
			}).start();
		}

		while (true)
		{
			Thread.sleep(1000);
		}
	}
}