
public class Show implements java.io.Serializable{
	private Movie movie;
	private String sss;
	public Show(Movie m, String ss)
	{
		movie = m;
		sss = ss;
	}
	public Movie getMovie(){return movie;}
	public void printSSS(){System.out.format("%s",sss);}
}
