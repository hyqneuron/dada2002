import java.io.*;


public class App {
	public static void main(String[] args)
	{
		Movie m = new Movie(null, "bad movie");
		Show s = new Show(m, "My show");
		m.setShows(new Show[]{s});
		try{
			FileOutputStream file = new FileOutputStream("data");
			ObjectOutputStream oout = new ObjectOutputStream(file);
			oout.writeObject(m);
			oout.close();
			file.close();
			// now try to read
			FileInputStream fin = new FileInputStream("data");
			ObjectInputStream oin = new ObjectInputStream(fin);
			Movie min = (Movie) oin.readObject();
			oin.close();
			fin.close();
			min.printName();
			min.getShows()[0].printSSS();
		}
		catch(IOException | ClassNotFoundException e){
			System.out.println("File error");
			return;
		}
		
	}
}
