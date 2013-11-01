package dataClasses;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
public class DataSystem implements java.io.Serializable {

	private ArrayList<Cineplex> cineplexes;
	private ArrayList<Cinema> cinemas;
	private ArrayList<Customer> customers;
	private ArrayList<Invoice> invoices;
	private int invoiceID = 1; // starting ID for invoice
	private ArrayList<Movie> movies;
	private int movieID = 1;
	private PricePolicy pricePolicy;
	// seating plan not needed for explicit storage, managed by Cinema
	// seating status not needed for explicit storage, managed by Show
	private ArrayList<Show> shows;
	private int showID = 1; // starting ID for show
	private ArrayList<Staff> staffs;
	private ArrayList<Ticket> tickets;
	private int ticketID = 1; // starting ID for ticket

	public DataSystem() {
		this.cineplexes = new ArrayList<Cineplex>();
		this.cinemas = new ArrayList<Cinema>();
		this.customers = new ArrayList<Customer>();
		this.invoices = new ArrayList<Invoice>();
		this.movies = new ArrayList<Movie>();
		this.pricePolicy = new PricePolicy();
		this.shows = new ArrayList<Show>();
		this.staffs = new ArrayList<Staff>();
		this.tickets = new ArrayList<Ticket>();
	}
	

	public void addCineplex(Cineplex cineplex){
		cineplexes.add(cineplex);
	}
	public int getCineplexCount(){
		return cineplexes.size();
	}
	public Cineplex getCineplex(int i){
		return cineplexes.get(i);
	}
	public void addCinema(Cinema cinema){
		cinemas.add(cinema);
		cinema.getCineplex().addCinema(cinema);
	}
	public void addCustomer(Customer customer){
		customers.add(customer);
	}
	public int getCustomerCount(){
		return customers.size();
	}
	public Customer getCustomer(int i){
		return customers.get(i);
	}
	public void addMovie(Movie movie){
		movie.setID(String.valueOf(this.movieID++));
		movies.add(movie);
	}
	public int getMovieCount(){
		return movies.size();
	}
	public Movie getMovie(int i){
		return movies.get(i);
	}
	public void removeMovie(int id){
		movies.remove(id);
	}
	public void addInvoice(Invoice invoice){
		/// add invoice and associated tickets to system
		invoice.setID(String.valueOf(invoiceID++));
		invoices.add(invoice);
		for(int i = 0; i<invoice.getTicketCount(); i++)
			addTicket(invoice.getTickets()[i]);
		// if customer account is available, associate customer account with invoice
		Customer customer = invoice.getCustomer();
		if(customer!=null)
			customer.addInvoice(invoice);
	}
	public int getInvoiceCount(){
		return invoices.size();
	}
	public Invoice getInvoice(int i){
		return invoices.get(i);
	}
	public void addShow(Show show){
		show.setID(String.valueOf(this.showID++));
		shows.add(show);
		show.getMovie().addShow(show);
		show.getCinema().getCineplex().addShow(show);
	}
	public int getShowCount(){
		return shows.size();
	}
	public Show getShow(int i){
		return shows.get(i);
	}
	public void addStaff(Staff staff){
		staffs.add(staff);
	}
	public int getStaffCount(){
		return staffs.size();
	}
	public Staff getStaff(int i){
		return staffs.get(i);
	}
	
	// ticket can only be added to the system by addInvoice()
	private void addTicket(Ticket ticket){
		ticket.setID(String.valueOf(ticketID++));
		tickets.add(ticket);
	}
	
	public int getTicketCount(){
		return tickets.size();
	}
	public Ticket getTicket(int i){
		return tickets.get(i);
	}
	public PricePolicy getPricePolicy(){
		return pricePolicy;
	}
	
	// query methods
	
	// return all the shows that are showing
	// sortted by movie
	public Show[] getAllCurrentShows()
	{
		ArrayList<Show> curShows = new ArrayList<Show>();
		for(int i = 0; i<this.getMovieCount(); i++)
		{
			Movie m = this.getMovie(i);
			Show[] current = m.getCurrentShows();
			for(int j = 0; j<current.length; j++)
				curShows.add(current[j]);
		}
		return curShows.toArray(new Show[0]);
	}

	public Movie[] getAllCurrentMovies()
	{
		ArrayList<Movie> curMovs = new ArrayList<Movie>();
		for(int i = 0; i<this.getMovieCount(); i++)
		{
			Movie m = this.getMovie(i);
			if(m.hasCurrentShow())
				curMovs.add(m);
		}
		return curMovs.toArray(new Movie[0]);
	}
	public Movie[] getAllNonEndMovies()
	{
		ArrayList<Movie> curMovs = new ArrayList<Movie>();
		for(int i = 0; i<this.getMovieCount(); i++)
		{
			Movie m = this.getMovie(i);
			if(m.getEndOfShow()==false)
				curMovs.add(m);
		}
		return curMovs.toArray(new Movie[0]);
	}
	public Movie[] findMoviesWithName(String name)
	{
		ArrayList<Movie> movs = new ArrayList<Movie>();
		for(int i = 0; i<this.getMovieCount(); i++)
		{
			Movie m = this.getMovie(i);
			if(m.getName().compareToIgnoreCase(name)==0)
				movs.add(m);
		}
		return movs.toArray(new Movie[0]);
	}
	
	public Movie findMovieWithID(String id)
	{
		for(int i = 0; i<this.getMovieCount(); i++)
		{
			Movie m = this.getMovie(i);
			if(m.getID().compareTo(id)==0)
				return m;
		}
		return null;
	}

	public Cineplex findCineplexWithName(String name)
	{
		for(int i = 0; i<this.getCineplexCount(); i++)
		{
			Cineplex c = this.getCineplex(i);
			if(c.getName().compareTo(name)==0)
				return c;
		}
		return null;
	}
	public Customer findCustomerWithUsername(String username)
	{
		for(int i = 0; i<this.getCustomerCount(); i++)
		{
			if(this.getCustomer(i).getUsername().compareTo(username)==0)
				return this.getCustomer(i);
		}
		return null;
	}
	public Staff findStaffWithUsername(String username)
	{
		for(int i = 0; i<this.getStaffCount(); i++)
		{
			if(this.getStaff(i).getUsername().compareTo(username)==0)
				return this.getStaff(i);
		}
		return null;
	}
	public Show findShowWithID(String id)
	{
		for(int i = 0; i<this.getShowCount(); i++)
		{
			if(this.getShow(i).getID().compareTo(id)==0)
				return this.getShow(i);
		}
		return null;
	}
	public Invoice findInvoiceWithID(String id)
	{
		for(int i = 0; i<this.getInvoiceCount(); i++)
			if(this.getInvoice(i).getID().compareTo(id)==0)
				return this.getInvoice(i);
		return null;
	}
	
	public float calcRevenue(Movie movie, Cineplex cineplex, Date first, Date last)
	{
		return 0;
	}
	
	// load from file and return the corresponding DataSystem object
	public static DataSystem Load(String filename)
	{
		try
		{
			FileInputStream fin = new FileInputStream(filename);
			ObjectInputStream oin = new ObjectInputStream(fin);
			DataSystem loaded = (DataSystem) oin.readObject();
			oin.close();
			fin.close();
			return loaded;
		}
		catch (Exception e){
			System.out.println("Cannot load from file");
			return null;
		}
	}
	

	// write our data to a file
	public void Store(String filename)
	{
		try
		{
			FileOutputStream file = new FileOutputStream(filename);
			ObjectOutputStream oout = new ObjectOutputStream(file);
			oout.writeObject(this);
			oout.close();
			file.close();
		}
		catch (Exception e){
			System.out.println("Cannot store to file");
		}
	}
	
	// create a new DataSystem object with our testing entries
	public static DataSystem GetTestingDataSystem()
	{
		DataSystem d = new DataSystem();
		//cineplex
		Cineplex c1 = new Cineplex("Cineplex 1", "Street 81");
		Cineplex c2 = new Cineplex("Cineplex 2", "Street 91");
		Cineplex c3 = new Cineplex("Cineplex 3", "Street 101");
		d.addCineplex(c1);
		d.addCineplex(c2);
		d.addCineplex(c3);
		// seating plans
		int[][] seats = new int[][]{
			{0,1,1,1,1,1,0,1,1,1,1,1,0},
			{0,1,1,1,1,1,0,1,1,1,1,1,0},
			{0,1,1,1,1,1,0,1,1,1,1,1,0},
			{0,1,1,1,1,1,0,1,1,1,1,1,0},
			{0,1,1,1,1,1,0,1,1,1,1,1,0},
			{0,1,1,1,1,1,0,1,1,1,1,1,0},
			{0,1,1,1,1,1,0,1,1,1,1,1,0},
		};
		SeatingPlan sp = new SeatingPlan(seats);
		// cinemas
		Cinema cm1_1 = new Cinema("Hall 1", PricePolicy.CinemaType.Normal, sp, c1);
		Cinema cm1_2 = new Cinema("Hall 2", PricePolicy.CinemaType.Normal, sp, c1);
		Cinema cm1_3 = new Cinema("Hall 3", PricePolicy.CinemaType.Normal, sp, c1);
		
		Cinema cm2_1 = new Cinema("Hall 1", PricePolicy.CinemaType.Normal, sp, c2);
		Cinema cm2_2 = new Cinema("Hall 2", PricePolicy.CinemaType.Normal, sp, c2);
		Cinema cm2_3 = new Cinema("Hall 3", PricePolicy.CinemaType.Normal, sp, c2);
		
		Cinema cm3_1 = new Cinema("Hall 1", PricePolicy.CinemaType.Normal, sp, c3);
		Cinema cm3_2 = new Cinema("Hall 2", PricePolicy.CinemaType.Normal, sp, c3);
		Cinema cm3_3 = new Cinema("Hall 3", PricePolicy.CinemaType.Premium, sp, c3);

		d.addCinema(cm1_1);
		d.addCinema(cm1_2);
		d.addCinema(cm1_3);
		d.addCinema(cm2_1);
		d.addCinema(cm2_2);
		d.addCinema(cm2_3);
		d.addCinema(cm3_1);
		d.addCinema(cm3_2);
		d.addCinema(cm3_3);
		
		// movie
		Movie kingkong = new Movie("King Kong", "a good movie", 120, 15, PricePolicy.MovieType.Blockbuster, false);
		Movie leon = new Movie("Leon", "the professional", 100, 0, PricePolicy.MovieType.Normal, false);
		Movie gravity = new Movie("Gravity", "yes, Earth is heavy", 140, 0, PricePolicy.MovieType.Blockbuster, false);
		d.addMovie(kingkong);
		d.addMovie(leon);
		d.addMovie(gravity);
		
		// shows
		Calendar cal = new GregorianCalendar();
		cal.set(2013, 10, 31, 16, 00);
		Show s1_1 = new Show(leon, cm1_1, PricePolicy.ShowType.TwoD, cal.getTime());
		Show s2_1 = new Show(leon, cm2_1, PricePolicy.ShowType.TwoD, cal.getTime());
		Show s3_1 = new Show(leon, cm3_1, PricePolicy.ShowType.TwoD, cal.getTime());
		cal.set(2013, 10, 31, 15, 00);
		Show s1_2 = new Show(kingkong, cm1_2, PricePolicy.ShowType.ThreeD, cal.getTime());
		Show s2_2 = new Show(kingkong, cm2_2, PricePolicy.ShowType.ThreeD, cal.getTime());
		Show s3_2 = new Show(kingkong, cm3_2, PricePolicy.ShowType.ThreeD, cal.getTime());
		cal.set(2013, 10, 31, 17, 00);
		Show s1_3 = new Show(gravity, cm1_3, PricePolicy.ShowType.IMAX, cal.getTime());
		Show s2_3 = new Show(gravity, cm2_3, PricePolicy.ShowType.IMAX, cal.getTime());
		Show s3_3 = new Show(gravity, cm3_3, PricePolicy.ShowType.IMAX, cal.getTime());

		d.addShow(s1_1);
		d.addShow(s1_2);
		d.addShow(s1_3);
		d.addShow(s2_1);
		d.addShow(s2_2);
		d.addShow(s2_3);
		d.addShow(s3_1);
		d.addShow(s3_2);
		d.addShow(s3_3);
		
		// customer and staff
		Customer cus1 = new Customer("cus1", "password");
		Customer cus2 = new Customer("cus2", "password");
		Staff staff1 = new Staff("staff1", "password");
		Staff staff2 = new Staff("staff2", "password");
		d.addCustomer(cus1);
		d.addCustomer(cus2);
		d.addStaff(staff1);
		d.addStaff(staff2);
		return d;
	}
}
