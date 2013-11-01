package dataClasses;

public class PricePolicy  implements java.io.Serializable{
	public enum CustomerType {Student, Adult, Senior};
	public enum MovieType{Blockbuster, Normal};
	public enum ShowType{TwoD, ThreeD, IMAX};
	public enum CinemaType{Premium, Normal};
	
	private float basePrice = 8;
	private float studentDiscount = 0.7f;
	private float seniorDiscount = 0.7f;
	private float blockbusterInc = 4; // extra amount for blockbusters
	private float threeDInc = 2; // extra amount for 3D
	private float iMAXInc = 8; // extra amount for IMAX
	private float premiumInc = 10; // extra amount for premium cinema 
	private float gst = 7;//by %
	
	
	public CustomerType getCustomerType(int age)
	{
		if(age<18)
			return CustomerType.Student;
		else if(age < 60)
			return CustomerType.Adult;
		else
			return CustomerType.Senior;
	}
	public float getPrice(CustomerType cusType, Show show)
	{	
		// price start at basePrice
		float price = basePrice;
		
		// MovieType: blockbuster increment
		if(show.getMovie().getMovieType()==MovieType.Blockbuster)
			price += blockbusterInc;
		
		// ShowType: 2D is default, 3D and IMAX add increment
		if(show.getShowType()==ShowType.ThreeD)
			price += threeDInc;
		else if(show.getShowType()==ShowType.IMAX)
			price += iMAXInc;
		
		// CinemaType: premium add increment
		if(show.getCinema().getCinemaType()==CinemaType.Premium)
			price += premiumInc;
		
		// CustomerType: students and senior get discount
		if(cusType==CustomerType.Student)
			price *= studentDiscount;
		else if(cusType==CustomerType.Senior)
			price *= seniorDiscount;
		
		// round down to multiple of 50 cents
		return (float)(Math.floor(price*2)/2);
	}
	public float getBasePrice() {
		return basePrice;
	}
	public void setBasePrice(float basePrice) {
		this.basePrice = basePrice;
	}
	public float getStudentDiscount() {
		return studentDiscount;
	}
	public void setStudentDiscount(float studentDiscount) {
		this.studentDiscount = studentDiscount;
	}
	public float getSeniorDiscount() {
		return seniorDiscount;
	}
	public void setSeniorDiscount(float seniorDiscount) {
		this.seniorDiscount = seniorDiscount;
	}
	public float getBlockbusterInc() {
		return blockbusterInc;
	}
	public void setBlockbusterInc(float blockbusterInc) {
		this.blockbusterInc = blockbusterInc;
	}
	public float getThreeDInc() {
		return threeDInc;
	}
	public void setThreeDInc(float threeDInc) {
		this.threeDInc = threeDInc;
	}
	public float getiMAXInc() {
		return iMAXInc;
	}
	public void setiMAXInc(float iMAXInc) {
		this.iMAXInc = iMAXInc;
	}
	public float getPremiumInc() {
		return premiumInc;
	}
	public void setPremiumInc(float premiumInc) {
		this.premiumInc = premiumInc;
	}
	public float getGST() {
		return gst;
	}
	public void setGST(float gst) {
		this.gst = gst;
	}
}
