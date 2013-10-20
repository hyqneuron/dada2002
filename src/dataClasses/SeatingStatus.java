package dataClasses;

public class SeatingStatus  implements java.io.Serializable{
	// seat status matrix
	private Ticket[][] seats;
	private SeatingPlan seatPlan;
	// we use this entry to represent a non-seat position (an aisle or empty space
	private static Ticket noSeat = new Ticket(null, "void", PricePolicy.CustomerType.Adult, 0);
	
	public SeatingStatus(SeatingPlan seatPlan)
	{
		this.seatPlan = seatPlan;
		this.seats = new Ticket[seatPlan.getRowCount()][seatPlan.getColumnCount()];
	}
	public void AssignSeatByName(Ticket ticket, String seatName){
		int[] indices = getSeatIndex(seatName);
		AssignSeat(ticket, indices[0], indices[1]);
	}
	public void AssignSeat(Ticket ticket, int rowIdx, int columnIdx){
		seats[rowIdx][columnIdx] = ticket;
	}
	public Boolean isSeat(int rowIdx, int columnIdx){
		// empty seat
		return seatPlan.isSeat(rowIdx, columnIdx);
	}
	public Boolean isSeatEmpty(int rowIdx, int columnIdx){
		// if it's not a seat, of course it's not empty
		if(!isSeat(rowIdx, columnIdx)) return false;
		return seats[rowIdx][columnIdx] == null; // if null, seat is empty
	}
	public String getSeatName(int rowIdx, int columnIdx){
		return seatPlan.getSeatName(rowIdx, columnIdx);
	}
	public int[] getSeatIndex(String seatName){
		return seatPlan.getSeatIndex(seatName);
	}
	public int CountEmptySeats(){
		int result = 0;
		for(int i = 0; i<seatPlan.getRowCount(); i++){
			for(int j = 0; j<seatPlan.getColumnCount(); j++)
				if(isSeatEmpty(i,j))
					result++;
		}
		return result;
	}
	public int getRowCount(){
		return seatPlan.getRowCount();
	}
	public int getColumnCount(){
		return seatPlan.getColumnCount();
	}
}
