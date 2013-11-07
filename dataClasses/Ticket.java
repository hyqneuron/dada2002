package dataClasses;

public class Ticket  implements java.io.Serializable{
	private Show show;
	private String seatName;
	private float price;
	private PricePolicy.CustomerType customerType;
	private String id;
	
	public Ticket(Show show, String seatName,
			PricePolicy.CustomerType customerType, float price){
		this.show = show;
		this.seatName = seatName;
		this.customerType = customerType;
		this.price = price;
	}

	public Show getShow() {
		return show;
	}

	public String getSeatName() {
		return seatName;
	}


	public float getPrice() {
		return price;
	}

	public PricePolicy.CustomerType getCustomerType() {
		return customerType;
	}

	public String getID(){
		return id;
	}
	public void setID(String id){
		this.id = id;
	}
}
