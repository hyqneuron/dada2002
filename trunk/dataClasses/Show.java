package dataClasses;

import java.util.Calendar;
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
	// get starting time in Calendar
	public Calendar getBeginCalendar(){
		Calendar c = Calendar.getInstance();
		c.setTime(time);
		return c;
	}
	// get ending time in Calendar
	public Calendar getEndCalendar(){
		Calendar c = Calendar.getInstance();
		c.setTime(time);
		c.add(Calendar.MINUTE, movie.getDuration());
		return c;
	}
	// test if two shows conflict (same time and cinema)
	public boolean ConflictsWith(Show show){
		// if comparing the same Show, no conflict
		if(show == this)
			return false;
		// not in the same cinema, certainly no conflict
		if(cinema!= show.cinema)
			return false;
		/*
		// not on the same day, certainly no conflict
		if(time.getYear()!= show.time.getYear() ||
			time.getMonth()!=show.time.getMonth()||
			time.getDay()!=show.time.getDay())
			return false;
			*/
		Calendar t1, t2;
		// we identify the one that starts earlier, label it as t1
		// if t2 starts before t1 ends, we have a conflict
		if(time.before(show.time)){
			t1 = getEndCalendar();
			t2 = show.getBeginCalendar();
		}
		else{
			t1 = show.getEndCalendar();
			t2 = getBeginCalendar();
		}
		//System.out.format("%s%n", t1.getTime().toString());
		//System.out.format("%s%n", t2.getTime().toString());
		// if t2 starts before t1 ends, conflict
		return t1.after(t2);
	}
}
