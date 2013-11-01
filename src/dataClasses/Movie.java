package dataClasses;

import java.util.ArrayList;


public class Movie implements java.io.Serializable{
	// all of the shows of this movie
	private ArrayList<Show> shows;
	private String name;
	private String description;
	private int duration; // in minutes
	private int ageLimit; // minimum age required to watch
	private PricePolicy.MovieType movieType;
	private String id;
	
	public Movie(String name, String description, int duration, int ageLimit, PricePolicy.MovieType movieType)
	{
		this.shows = new ArrayList<Show>();
		this.name = name;
		this.description = description;
		this.duration = duration;
		this.ageLimit = ageLimit;
		this.movieType = movieType;
	}
	public void addShow(Show show){
		shows.add(show);
	}
	public ArrayList<Show> getShows(){
		return shows;
	}
	public int getShowCount(){
		return shows.size();
	}
	public Show getShow(int i){
		return shows.get(i);
	}
	public void removeShow(Show show){
		shows.remove(show);
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getAgeLimit() {
		return ageLimit;
	}
	public void setAgeLimit(int ageLimit) {
		this.ageLimit = ageLimit;
	}
	public PricePolicy.MovieType getMovieType() {
		return movieType;
	}
	public void setMovieType(PricePolicy.MovieType movieType) {
		this.movieType = movieType;
	}
	public Show[] getCurrentShows(){
		ArrayList<Show> current = new ArrayList<Show>();
		for(int i = 0; i<this.getShowCount(); i++)
			if(this.getShow(i).isCurrent())
				current.add(this.getShow(i));
		return current.toArray(new Show[0]);
	}
	public Boolean hasCurrentShow(){
		for(int i = 0; i<this.getShowCount(); i++)
			if(this.getShow(i).isCurrent())
				return true;
		return false;
	}
	public Boolean hasCurrentShowInCineplex(Cineplex cineplex){
		for(int i = 0; i<this.getShowCount(); i++)
			if(this.getShow(i).getCinema().getCineplex()==cineplex &&
				this.getShow(i).isCurrent())
				return true;
		return false;
	}
	public Show[] getCurrentShowsInCineplex(Cineplex cineplex){
		ArrayList<Show> current = new ArrayList<Show>();
		for(int i = 0; i<this.getShowCount(); i++)
			if(this.getShow(i).getCinema().getCineplex()==cineplex &&
				this.getShow(i).isCurrent())
				current.add(this.getShow(i));
		return current.toArray(new Show[0]);
		
	}

	public String getID(){
		return id;
	}
	public void setID(String id){
		this.id = id;
	}
}
