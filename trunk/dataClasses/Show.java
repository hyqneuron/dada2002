package dataClasses;

import java.util.Date;
import java.util.GregorianCalendar;

public class Show implements java.io.Serializable{
	private Movie movie;
	private Cinema cinema;
	private SeatingStatus seatStatus;
	private PricePolicy.ShowType showType;
	private Date time;
	private String id;
	public Show(Movie movie, Cinema cinema, PricePolicy.ShowType showType, Date time)
	{
		this.movie = movie;
		this.cinema = cinema;
		this.seatStatus = new SeatingStatus(cinema.getSeatingPlan());
		this.showType = showType;
		this.time = time;
	}
	public Movie getMovie() {
		return movie;
	}
	public Cinema getCinema() {
		return cinema;
	}
	public void setCinema(Cinema cinema){
		this.cinema = cinema;
	}
	public PricePolicy.ShowType getShowType() {
		return showType;
	}
	public void setShowType(PricePolicy.ShowType showType){
		this.showType = showType;
	}
	public SeatingStatus getSeatingStatus(){
		return seatStatus;
	}
	public int CountEmptySeats(){
		return seatStatus.CountEmptySeats();
	}
	public Date getTime(){
		return time;
	}
	public void setTime(Date time){
		this.time = time;
	}
	public String getID(){
		return id;
	}
	public void setID(String id){
		this.id = id;
	}
	public Boolean isCurrent(){
		return this.time.after(GregorianCalendar.getInstance().getTime());
	}

	public boolean hasSales(DataSystem d){
		for(int i = 0; i<d.getInvoiceCount(); i++){
			if(d.getInvoice(i).getTickets()[0].getShow()==this)
				return true;
		}
		return false;
	}
}
