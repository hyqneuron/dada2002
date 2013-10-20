package dataClasses;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class Invoice  implements java.io.Serializable{
	private Ticket[] tickets;
	private String id;
	private Customer customer;
	private Date time;
	public Invoice(Ticket[] tickets, Customer customer){
		this.tickets = tickets;
		this.customer = customer;
		this.time = java.util.GregorianCalendar.getInstance().getTime();
	}
	public float getTotalPrice(){
		float result = 0;
		for(int i = 0; i<tickets.length; i++)
			result += tickets[i].getPrice();
		return result;
	}
	public int getTicketCount(){
		return tickets.length;
	}
	public Ticket[] getTickets(){
		return tickets;
	}
	public Customer getCustomer(){
		return customer;
	}
	public Date getTime(){
		return time;
	}
	public String getID(){
		return id;
	}
	public void setID(String id){
		this.id = id;
	}
}
