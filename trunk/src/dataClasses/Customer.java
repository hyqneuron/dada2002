package dataClasses;

import java.util.LinkedList;
import java.util.Queue;

public class Customer  implements java.io.Serializable{
	private String username;
	private String password;
	// we only associate 5 of the most recent invoices for a customer object
	// all invoices are stalled in our DataSystem object
	private Queue<Invoice> invoices;
	public Customer(String username, String password){
		this.username = username;
		this.password = password;
		this.invoices = new LinkedList<Invoice>();
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void addInvoice(Invoice invoice)
	{
		if(invoices.size()==5)
			invoices.remove();
		invoices.add(invoice);
	}
	public int getInvoiceCount(){
		return invoices.size();
	}
	public Invoice[] getInvoices(){
		return invoices.toArray(new Invoice[0]);
	}
	
}
