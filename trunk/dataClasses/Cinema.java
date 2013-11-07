package dataClasses;

public class Cinema implements java.io.Serializable{
	private String name;
	private SeatingPlan seatPlan;
	private Cineplex cineplex;
	private PricePolicy.CinemaType cinemaType;
	
	public Cinema(String name, PricePolicy.CinemaType cinemaType, SeatingPlan seatPlan, Cineplex cineplex){
		this.name = name;
		this.cinemaType = cinemaType;
		this.seatPlan = seatPlan;
		this.cineplex = cineplex;
	}
	public String getName(){
		return name;
	}
	public void setName(String Name){
		name = Name;
	}
	public SeatingPlan getSeatingPlan(){
		return seatPlan;
	}
	public void setSeatingPlan(SeatingPlan SPlan){
		seatPlan = SPlan;
	}
	public Cineplex getCineplex(){
		return cineplex;
	}
	// do not implement setter for cineplex, since it can't be changed
	public PricePolicy.CinemaType getCinemaType(){
		return cinemaType;
	}
	public void setCinemaType(PricePolicy.CinemaType cinemaType){
		this.cinemaType = cinemaType;
	}
}
