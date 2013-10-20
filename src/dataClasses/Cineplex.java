package dataClasses;

import java.util.ArrayList;

public class Cineplex  implements java.io.Serializable{
	private String name;
	private String location;
	private ArrayList<Cinema> cinemas;
	private ArrayList<Show> currentShows;
	private ArrayList<Movie> currentMovies;
	public Cineplex(String Name, String Location)
	{
		cinemas = new ArrayList<Cinema>();
		name = Name;
		location = Location;
		currentShows = new ArrayList<Show>();
		currentMovies = new ArrayList<Movie>();
	}
	public String getName(){
		return name;
	}
	public void setName(String Name){
		name = Name;
	}
	public String getLocation(){
		return location;
	}
	public void setLocation(String Location){
		location = Location;
	}
	public ArrayList<Cinema> getCinemas(){
		return cinemas;
	}
	public void addCinema(Cinema cinema){
		cinemas.add(cinema);
	}
	public void removeCinema(Cinema cinema){
		cinemas.remove(cinema);
	}
	public void addShow(Show show){
		if(show.isCurrent())
			currentShows.add(show);
		Movie m = show.getMovie();
		if(!currentMovies.contains(m))
			currentMovies.add(m);
	}
	private void removeOldShows()
	{
		for(int i = currentShows.size() -1 ; i>=0; i--)
			if(currentShows.get(i).isCurrent()==false)
				currentShows.remove(i);
	}
	private void removeOldMovies()
	{
		for(int i = currentMovies.size() - 1; i >= 0; i--)
			if(currentMovies.get(i).hasCurrentShowInCineplex(this) == false)
				currentMovies.remove(i);
	}
	public Show[] getCurrentShows(){
		removeOldShows();
		return currentShows.toArray(new Show[0]);
	}
	public Movie[] getCurrentMovies(){
		removeOldMovies();
		return currentMovies.toArray(new Movie[0]);
	}
	public Movie[] findCurrentMoviesWithName(String name)
	{
		removeOldMovies();
		ArrayList<Movie> movs = new ArrayList<Movie>();
		for(int i = 0; i<currentMovies.size(); i++)
		{
			Movie m = currentMovies.get(i);
			if(m.getName().compareToIgnoreCase(name)==0)
				movs.add(m);
		}
		return movs.toArray(new Movie[0]);
	}
}
