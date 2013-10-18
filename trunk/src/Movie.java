
public class Movie implements java.io.Serializable{
	private Show[] shows;
	private String name;
	public Movie(Show[] ss, String _name)
	{
		shows = ss;
		name = _name;
	}
	public Show[] getShows(){return shows;}
	public void setShows(Show[] ss){shows = ss;}
	public void printName(){System.out.format("%s", name);}
}
