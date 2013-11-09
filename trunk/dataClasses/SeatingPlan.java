package dataClasses;

public class SeatingPlan  implements java.io.Serializable{
	// we use 0 to represent void space, 1 to represent seat
	private int[][] seats;
	// number of rows of seat matrix
	private int rowCount;
	// number of columns of the seat matrix
	private int columnCount;
	public SeatingPlan(int[][] seats)
	{
		this.seats = seats;
		rowCount = seats.length;
		columnCount = 0;
		for(int i = 0; i<rowCount; i++){
			if(seats[i].length>columnCount)
				columnCount = seats[i].length;
		}
	}
	public int getColumnCount(){
		return columnCount;
	}
	public int getRowCount(){
		return rowCount;
	}
	// return whether the indexed position is a seat or void space
	// true for seat, false for void space
	public Boolean isSeat(int rowIdx, int columnIdx){
		if(rowIdx < 0 || rowIdx >= getRowCount())
			return false;
		if(columnIdx < 0 || columnIdx >= getColumnCount())
			return false;
		return seats[rowIdx][columnIdx]==1;
	}

	// convert the seat index into a seat name string like A10 or F2
	public String getSeatName(int rowIdx, int columnIdx){
		if(!isSeat(rowIdx, columnIdx))
			return "";
		char row = (char)(rowIdx + (int)'A');
		int colNameIdx=0;
		for(int i = 0; i<= columnIdx ;i++)
			if(isSeat(rowIdx, i))
				colNameIdx++;
		return row + String.valueOf(colNameIdx);
	}
	
	// convert a string seatName to int[]{rowIdx, colIdx} that can be used to address a specific seat
	public int[] getSeatIndex(String seatName){
		// remove extra stuffs in seatName (spaces)
		seatName.trim();
		// check that seatName is of length 2 or 3, and is of correct format Xnn
		// (X is an upper case letter, nn is a 1 or 2-digit number) 
		if(seatName.length()<2 || seatName.length()>3)
			return null;
		if(seatName.charAt(0)<'A' || seatName.charAt(0)>'Z')
			return null;
		if(seatName.charAt(1)<'0' || seatName.charAt(1)>'9')
			return null;
		if(seatName.length()==3 && (seatName.charAt(2)<'0' || seatName.charAt(2)>'9'))
			return null;
		// result is what we'll return
		int[] result = new int[2];
		int rowIdx, colIdx=-1;
		rowIdx = (int)seatName.charAt(0)-(int)'A';
		int seatNameColIdx = Integer.parseInt(seatName.substring(1));
		int seatCount = 0;
		for(int i = 0; i< getColumnCount(); i++){
			if(isSeat(rowIdx, i))
				seatCount++;
			if(seatCount==seatNameColIdx){
				colIdx = i;
				break;
			}
		}
		if(colIdx==-1)
			return null;
		result[0] = rowIdx;
		result[1] = colIdx;
		return result;
	}
}
