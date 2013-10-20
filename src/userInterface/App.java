package userInterface;
import dataClasses.*;


public class App {
	public static void main(String[] args)
	{
		// We attempt to load from file, when fail, use testing data
		String filename = "data";
		DataSystem d;
		try{
			d = DataSystem.Load(filename);
			if(d==null)
				d = DataSystem.GetTestingDataSystem();
		}
		catch(Exception e)
		{
			d = DataSystem.GetTestingDataSystem();
		}
		ConsoleInterface ci = new ConsoleInterface(d);
		ci.EnterInterface();
		// we have left console interface, now save data
		d.Store(filename);
	}
}
