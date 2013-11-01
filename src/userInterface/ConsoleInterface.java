package userInterface;
import dataClasses.*;

import java.util.Date;
import java.awt.List;
import java.util.ArrayList;
import java.util.Stack;


public class ConsoleInterface {

	/*
	 * This class handles our console UI, which is a hierarchical menu system
	 * 
	 * Our menu system are based on 3 objects: Menu, MenuOption, and MenuAction
	 * 
	 * A Menu, when shown, would look like this:
	 * 
	 * Home > Listing > Cineplex 2
	 * 1. List all movies
	 * 2. List all shows
	 * 3. List shows for specific movie
	 * 4. Book movie
	 * 5. Leave
	 * 
	 * In the example above, we have a title bar on top, and 5 menu options
	 * 
	 * Our menu is hierarchical, so a stack is used to represent the layers (e.g. Home > Listing > ...)
	 * The stack object we use is menuStack
	 * 
	 * Each Menu has 3 components:
	 * 1. a name
	 * 2. a title
	 * 3. a set of options
	 * Name and title are both String, and are only shown in the title bar. Name is a shorter version of 
	 * title to keep the title bar short
	 * 
	 * menu options are built using the MenuOption object,
	 * each with a String to be shown in its containing menu, a MenuAction that is executed
	 * when the option is chosen, and an optional parameter that may be passed to MenuAction
	 * when it is executed.
	 * 
	 * 
	 */
	/*
	 * Home > Listing > Cineplex 2
	 * 1. List all movies
	 * 2. List all shows
	 * 3. List shows for specific movie
	 * 4. Book movie
	 * 5. Leave
	 */
	private class MenuOption
	{
		public String optionString; // eg "List all shows"
		public MenuAction action;
		public Object parameter;
		
		public MenuOption(String optionStr, MenuAction action)
		{
			this(optionStr, action, null);
		}
		public MenuOption(String optionStr, MenuAction action, Object param)
		{
			this.optionString = optionStr;
			this.action = action;
			this.parameter = param;
		}
	}
	

	private interface MenuAction 
	{
		public void Show(Object o);
	}
	private class Menu
	{
		public String name;  // e.g. "Home"
		public String title; // e.g. "Customer Main Menu"
		public MenuOption[] options;
		public Menu(String name, String title, MenuOption[] options)
		{
			this.name = name;
			this.title = title;
			this.options = options;
		}	
	}
	
	
	
	public ConsoleInterface(DataSystem d)
	{
		this.dataMgr = d;
		this.menuStack = new Stack<Menu>();
		this.scanner = new java.util.Scanner(System.in);
	}
	// enter our console interface
	// this is the only function that our app initializer should call. Everything else
	// will be handled by 
	public void EnterInterface(){
		PrintLine("Welcome to our MOBIBLAHBLAH!");
		menuLogin.Show(null);
		DoMenu();
	}
	//=================data members=======================
	// our data source
	DataSystem dataMgr;
	// our hierarchical menu stack. See DoMenu() for more details
	private Stack<Menu> menuStack;
	// fixed scanner for input
	java.util.Scanner scanner;
	
	//===================convenience functions=====================
	// short hand to println
	private static void PrintLine(String s)
	{
		System.out.println(s);
	}
	private static void Print(String s)
	{
		System.out.print(s);
	}
	// short hand for format
	private static void Format(String format, Object...args)
	{
		System.out.format(format, args);
	}

	// ask for user input, valid from min to max, otherwise ask to reenter
	private int AskForChoice(int min, int max){
		return AskForChoice(min, max, "Please enter your option: ");
	}
	private int AskForChoice(int min, int max, String prompt)
	{
		int result=min-1;
		String input;
		while(result<min || result>max)
		{
			Print(prompt);
			input = scanner.nextLine();
			// first time we print prompt passed in, second time and on we print error msg below
			prompt = "Invalid choice. Please re-enter: ";
			try	{
				result = Integer.parseInt(input);
			}
			catch(Exception e){}
		}
		return result;
	}
	// ask for user input, a non-empty string
	private String AskForString(){
		return AskForString("");
	}
	private String AskForString(String prompt)
	{
		String result;
		Print(prompt);
		result = scanner.nextLine();
		while(result=="")
		{
			Print("Please enter a non-empty string:");
			result = scanner.nextLine();
		}
		return result;
	}
	private void PrintMovie(Movie movie)
	{
		Format("Name: %s%n", movie.getName());
		if(staffLogin!=null)
			Format("ID: %s %n", movie.getID());
		Format("Description: %s%nDuration: %d minutes, Available showings: %d%n%n",
				movie.getDescription(), 
				movie.getDuration(), movie.getShows().size());
	}
	private void PrintShow(Show show)
	{
		Movie m = show.getMovie();
		Format("Name: %s%nShow ID: %s, Duration: %4d minutes, Available seats: %d%n%n", 
				m.getName(), show.getID(), m.getDuration(), show.CountEmptySeats());
	}
	private void PrintSeats(SeatingStatus status){
		for(int i = 0; i<status.getRowCount(); i++){
			for(int j = 0; j<status.getColumnCount(); j++){
				if(!status.isSeat(i, j)) // void space
					Print("    ");
				else if(status.isSeatEmpty(i, j)) //empty
					Format("%4s", status.getSeatName(i, j));
				else // occupied
					Print("  X ");
			}
			PrintLine("");
		}
	}
	private void PrintShowWithSeats(Show show){
		PrintShow(show);
		PrintSeats(show.getSeatingStatus());
	}
	private void PrintTicket(Ticket ticket)
	{
		// ticket number, movie name, cineplex name, cinema name, seat name, price
		PrintLine("");
		Format("Ticket number: %s%n", ticket.getID());
		Format("Movie: %s%n", ticket.getShow().getMovie().getName());
		Format("Cineplex: %s%n", ticket.getShow().getCinema().getCineplex().getName());
		Format("Cinema: %s%n", ticket.getShow().getCinema().getName());
		Format("Seat: %4s   Price: %4.2f%n", ticket.getSeatName(), ticket.getPrice());
	}
	private void PrintInvoice(Invoice invoice)
	{
		// print invoice number, total amount, and ticket details
		PrintLine("=========Invoice begins=========");
		Format("Invoice ID: %s%n", invoice.getID());
		for(int i = 0; i<invoice.getTicketCount(); i++)
			PrintTicket(invoice.getTickets()[i]);
		Format("Total amount: %6.2f%n", invoice.getTotalPrice());
		PrintLine("=========Invoice ends===========");
	}
	
	// We use this method to show a list of options
	// Each option is a string array of length 2 to 3, with third element optional
	// ["String to show in menu","name of function to call when chosen"]
	// such as: ["Continue as guest", "GuestMainMenu"]
	private void ShowMenu(Menu menu)
	{
		menuStack.push(menu);
	}
	private void LeaveSubMenu()
	{
		menuStack.pop();
	}
	private void DoMenu()
	{
		while(menuStack.size()!=0)
		{
			Menu menu = menuStack.peek();
			String title = menu.title;
			MenuOption[] options = menu.options;
			// print where we are, ignore the login menu
			PrintLine("===========================================");
			if(menuStack.size()>2){
				// we do not print the first (login) and the last (current, whose title is printed later)
				java.util.List<Menu> stackToPrint = menuStack.subList(1, menuStack.size()-1);
				for(int i = 0; i<stackToPrint.size(); i++)
					Format("%s > ", stackToPrint.get(i).name);
			}
			// print title
			if(title!=null)
				PrintLine(title);
			// print the options
			for(int i = 0; i<options.length; i++)
			{
				Format("%d: %s%n", i+1, options[i].optionString);
			}
			// ask for choice
			int choice = AskForChoice(1, options.length, "Please enter an option: ") - 1;
			// call target function
			options[choice].action.Show(options[choice].parameter);
		}
	}
	
	
	//=================login     ==================
	
	// who is logged in, customer or staff?
	private enum LoginType {None, Customer, Staff};
	private LoginType loginType = LoginType.None;
	// which customer or staff is logged in
	private Customer customerLogin;
	private Staff staffLogin;
	
	// ask if user wants to login
	private MenuAction menuLogin = new MenuAction(){
	public void Show(Object o)
	{
		Menu menu = new Menu("", "Continue as guest, or log in now?", new MenuOption[]{
			new MenuOption("Continue as guest", menuMain),
			new MenuOption("Login as customer", actionLogin, true),
			new MenuOption("Login as staff", 	actionLogin, false)
		});
		ShowMenu(menu);
	}};



	private MenuAction actionLogin = new MenuAction(){
	public void Show(Object o)
	{
		Boolean isCustomer = (Boolean) o;
		Customer customer=null;
		Staff staff=null;
		// ask for username, and validate such user exists
		String username = AskForString("Please enter username: ");
		if(isCustomer)
			customer = dataMgr.findCustomerWithUsername(username);
		else
			staff = dataMgr.findStaffWithUsername(username);
		if(customer==null && staff==null)
		{
			PrintLine("Such user does not exist.");
			return;
		}
		// check for password
		String password = AskForString("Please entr password: ");
		if(isCustomer)
		{
			if(customer.getPassword().compareTo(password)!=0)
			{
				PrintLine("Incorrect password.");
				return;
			}
			customerLogin = customer;
			staffLogin = null;
			loginType = LoginType.Customer;
		}
		else
		{
			if(staff.getPassword().compareTo(password)!=0)
			{
				PrintLine("Incorrect password.");
				return;
			}
			staffLogin = staff;
			customerLogin = null;
			loginType = LoginType.Staff;
		}
		// login passed, show another main menu
		LeaveSubMenu();
		menuMain.Show(null);
		
	}};
	//=============== main menu============================================
	private MenuAction menuMain = new MenuAction(){
		public void Show(Object o)
		{
			Menu menu;
			if(loginType==LoginType.Customer)
				menu = new Menu("Home", "Customer Main Menu", new MenuOption[]{
						new MenuOption("List Movies", 			menuListMovies),
						new MenuOption("Book Movie", 			actionBookMovie),
						new MenuOption("Check Booking Status", 	actionCheckBooking),
						new MenuOption("Check Booking History",	actionCheckHistory),
						new MenuOption("Leave", 				actionLeave)
					});
			else if(loginType==LoginType.None)
				menu = new Menu("Home", "Guest Main Menu", new MenuOption[]{
						new MenuOption("List Movies", 			menuListMovies),
						new MenuOption("Book Movie", 			actionBookMovie),
						new MenuOption("Register Account", 		actionRegisterCustomer),
						new MenuOption("Log in", 				actionLogin, true),
						new MenuOption("Check Booking Status", 	actionCheckBooking),
						new MenuOption("Leave", 				actionLeave)
					});
			else // staff
				menu = new Menu("Home", "Staff Main Menu", new MenuOption[]{
						new MenuOption("List Movies", 			menuListMovies),
						new MenuOption("Edit Movies", 			menuEditMovies),
						new MenuOption("Edit Shows", 			menuEditShows),
						new MenuOption("Reveues", 				menuRevenues),
						new MenuOption("Change Pricing Policy", menuChangePricingPolicy),
						new MenuOption("Book movie",			actionBookMovie),
						new MenuOption("Leave", 				actionLeave)
				});
			ShowMenu(menu);
		}
	};
	
	// ==================list movies======================================
	private MenuAction menuListMovies = new MenuAction(){
	public void Show(Object o)
	{
		ArrayList<MenuOption> arr = new ArrayList<MenuOption>();
		if(staffLogin!=null)
			arr.add(new MenuOption("List all non-end-of-show movies",	actionListNonEndMovies));
		else 
			arr.add(new MenuOption("List all currently showing movies",	actionListCurrentMovies));
		arr.add(new MenuOption("List a specific movie", 			actionListSpecificMovie));
		// add entries for each cineplex
		for(int i = 0; i<dataMgr.getCineplexCount(); i++)
			arr.add(new MenuOption(dataMgr.getCineplex(i).getName(), menuCineplex, dataMgr.getCineplex(i)));
		// add remaining entries
		arr.add(new MenuOption("Book movie",						actionBookMovie));
		arr.add(new MenuOption("Back",							  	actionBack));
		MenuOption[] options = new MenuOption[arr.size()];
		for(int i = 0; i<options.length; i++)
			options[i] = arr.get(i);
		Menu menu = new Menu("Listing", "Movie Listing Menu", options);
		ShowMenu(menu);
	}};

	private MenuAction actionListNonEndMovies = new MenuAction(){
		public void Show(Object o)
		{
			Cineplex cineplex = (Cineplex)o;
			Movie[] all = dataMgr.getAllNonEndMovies();
			// print them all
			for(int i = 0; i<all.length; i++)
				PrintMovie(all[i]);
		}
	};
	private MenuAction actionListCurrentMovies = new MenuAction(){
		public void Show(Object o)
		{
			// print all current movies without printing shows
			// if o is not null, it is a cineplex and we should show results only from that cineplex
			Cineplex cineplex = (Cineplex)o;
			Movie[] all;
			if(cineplex==null)
				all = dataMgr.getAllCurrentMovies();
			else
				all = cineplex.getCurrentMovies();
			// print them all
			for(int i = 0; i<all.length; i++)
				PrintMovie(all[i]);
		}
	};
	
	private MenuAction actionListCurrentShows = new MenuAction(){
		public void Show(Object o)
		{
			// print all current movies without printing shows
			// if o is not null, it is a cineplex and we should show results only from that cineplex
			Cineplex cineplex = (Cineplex)o;
			Show[] all;
			if(cineplex==null)
				all = dataMgr.getAllCurrentShows();
			else
				all = cineplex.getCurrentShows();
			// print them all
			for(int i = 0; i<all.length; i++)
				PrintShow(all[i]);
		}
	};

	private MenuAction actionListSpecificMovie = new MenuAction(){
	public void Show(Object o){
		// get a movie, then print its shows
		// if o is not null, it is a cineplex and we should show results only from that cineplex
		Cineplex cineplex = (Cineplex)o;
		
		Print("Please enter the name of the movie: ");
		String name = AskForString();
		
		// find the movies
		Movie[] found;
		if(cineplex==null)
			found = dataMgr.findMoviesWithName(name);
		else
			found = cineplex.findCurrentMoviesWithName(name);
		if(found.length==0)
			PrintLine("No movie with such name exists");
		for(int i = 0; i<found.length; i++)
		{
			Show[] curShows;
			if(cineplex==null)
					curShows = found[i].getCurrentShows();
			else
				curShows = found[i].getCurrentShowsInCineplex(cineplex);
			for(int j = 0; j<curShows.length; j++)
				PrintShow(curShows[j]);
		}
	}};

	private MenuAction menuCineplex = new MenuAction(){
	public void Show(Object o){
		Cineplex cineplex = (Cineplex)o;
		String title = cineplex.getName()+" Menu";
		Menu menu = new Menu(cineplex.getName(), title, new MenuOption[]{
				new MenuOption("List All Movies", 		actionListCurrentMovies, cineplex),
				new MenuOption("List All Shows", 		actionListCurrentShows,  cineplex),
				new MenuOption("List Shows for specific movie", actionListSpecificMovie, cineplex),
				new MenuOption("Book movie", 			actionBookMovie, 		 cineplex),
				new MenuOption("Back", 					actionBack)
			});
		ShowMenu(menu);
	}};
	
	// =================book movie========================================

	private MenuAction actionBookMovie = new MenuAction(){
	public void Show(Object o)
	{
		PrintLine("Please enter the ID of the show: ");
		String id = AskForString();
		Show show = dataMgr.findShowWithID(id);
		if(show==null){
			PrintLine("The specified show does not exist");
			return;
		}
		if(show.isCurrent()==false){
			PrintLine("Booking for the specified show has ended.");
			return;
		}
		if(show.getSeatingStatus().CountEmptySeats()==0){
			PrintLine("The show is fully booked.");
			return;
		}
		// check if we are booking inside a specific cineplex
		// if we are, then o is the specific cineplex
		Cineplex cineplex = (Cineplex)o;
		if(cineplex != null && show.getCinema().getCineplex()!= cineplex)
		{
			PrintLine("The specified show does not exist in the current cineplex");
			return;
		}
		
		// now we print the details of the show, then ask for number of tickets
		PrintShowWithSeats(show);
		PrintLine("How many seats would you like to book? (Enter 0 to cancel booking)");
		int seatCount = AskForChoice(0, show.getSeatingStatus().CountEmptySeats());
		if(seatCount==0)
			return;
		// now grab the seats one by one
		Ticket[] tickets = new Ticket[seatCount];
		for(int i = 0; i<seatCount; i++){
			Format("Bookingseat %d/%d%n", i+1, seatCount);
			String seatName=AskForString("Please enter a seat number (0 to cancel booking):");
			if(seatName=="0")
				return;
			SeatingStatus status = show.getSeatingStatus();
			int[] indices;
			// check to make sure seatName is valid and seat is not occupied
			while((indices = status.getSeatIndex(seatName))==null||
					!status.isSeatEmpty(indices[0], indices[1]))
			{
				String prompt;
				if(indices==null)
					prompt = "Invalid seat number. Please re-enter: ";
				else
					prompt = "Seat is occupied. Please choose another one: ";
				seatName = AskForString(prompt);
			}
			// ask for age
			int age = AskForChoice(0, 150, "Please enter customer's age: ");
			// make sure age is legal
			if(age<show.getMovie().getAgeLimit()){
				PrintLine("Customer has not reached age limit. Booking failed.");
				return;
			}
			// generate ticket
			PricePolicy.CustomerType cusType = dataMgr.getPricePolicy().getCustomerType(age);
			tickets[i] = new Ticket(show, seatName, cusType, 
					dataMgr.getPricePolicy().getPrice(cusType, show));
			// assign ticket to seat
			status.AssignSeat(tickets[i], indices[0], indices[1]);
			
		}
		// generate invoice and add invoice to system
		Invoice invoice = new Invoice(tickets, customerLogin);
		dataMgr.addInvoice(invoice);
		// print invoice and complete transaction
		PrintInvoice(invoice);
		PrintLine("Booking finished");
		
	}};
	
	// 3. check booking status
	private MenuAction actionCheckBooking = new MenuAction(){
		public void Show(Object o)
		{
			// ask for invoice number or ticket number
			String id = AskForString("Please enter your invoice ID: ");
			Invoice invoice = dataMgr.findInvoiceWithID(id);
			if(invoice==null){
				PrintLine("Invoice with such ID cannot be found.");
				return;
			}
			PrintInvoice(invoice);
		}
	};
	// 4. Register customer
	private MenuAction actionRegisterCustomer = new MenuAction(){
	public void Show(Object o)
	{
		String username = AskForString("Please Enter new username: ");
		while(dataMgr.findCustomerWithUsername(username)!=null)
		{
			Print("Username already exists, please choose a new one:");
			username = AskForString();
		}
		Print("Please Enter password: ");
		String password = AskForString();
		Customer cus = new Customer(username, password);
		dataMgr.addCustomer(cus);
		PrintLine("Registration successful");
	}};
	// 6. Check history
	private MenuAction actionCheckHistory = new MenuAction(){
		public void Show(Object o)
		{
			// only availabe to customers who have logged in
			Customer customer = customerLogin;
			Format("Printing previous invoices for %s%n", customer.getUsername());
			Invoice[] invoices = customer.getInvoices();
			for(int i = 0; i<invoices.length; i++)
				PrintInvoice(invoices[i]);
		}
	};
	// 7. leave
	private MenuAction actionLeave = new MenuAction(){
		public void Show(Object o){
			PrintLine("Have a nice day!");
			menuStack.clear();
			return;
		}
	};
	//8. Change pricing policy
	private MenuAction menuChangePricingPolicy = new MenuAction(){
		public void Show(Object o){
			Menu menu = new Menu("Price Policy", "Price Policy Menu", new MenuOption[]{
					new MenuOption("Change base price", actionChangePolicy, 0),
					new MenuOption("Change student discount", actionChangePolicy, 1),
					new MenuOption("Change senior discount", actionChangePolicy, 2),
					new MenuOption("Change increment for blockbuster", actionChangePolicy, 3),
					new MenuOption("Change increment for 3D", actionChangePolicy, 4),
					new MenuOption("Change increment for IMAX", actionChangePolicy, 5),
					new MenuOption("Change increment for premium", actionChangePolicy, 6),
					new MenuOption("Back",actionBack)
			});
			ShowMenu(menu);
		}
	};

	private MenuAction menuEditMovies = new MenuAction(){
		public void Show(Object o){
			
		}
	};

	private MenuAction menuEditShows= new MenuAction(){
		public void Show(Object o){
			
		}
	};

	private MenuAction menuRevenues = new MenuAction(){
		public void Show(Object o){
			Menu menu = new Menu("Revenue Printing", "Revenue Menu", new MenuOption[]{
					new MenuOption("Movie", actionPrintRevenue, 0),
					new MenuOption("Cinema", actionPrintRevenue, 1),
					new MenuOption("Day", actionPrintRevenue, 2),
					new MenuOption("Month", actionPrintRevenue, 3),
					new MenuOption("Back",actionBack)
			});
			ShowMenu(menu);
		}
	};
	
	private MenuAction actionBack = new MenuAction(){
		public void Show(Object o){
			LeaveSubMenu();
		}
	};
	
	private MenuAction actionPrintRevenue = new MenuAction(){
		public void Show(Object o){
		}
	};
	
	private MenuAction actionChangePolicy = new MenuAction(){

		public void Show(Object o){
			int choice = (int) o;
			float f;
			switch (choice){
				case 0:
					PrintLine("Please enter the new base price: ");
					f = scanner.nextFloat();
					dataMgr.getPricePolicy().setBasePrice(f);
					PrintLine("Current base price is: " + dataMgr.getPricePolicy().getBasePrice());
					break;
				case 1:
					PrintLine("Please enter the new discount for student: ");
					f = scanner.nextFloat();
					dataMgr.getPricePolicy().setStudentDiscount(f);
					PrintLine("Current discount for student is: " + dataMgr.getPricePolicy().getStudentDiscount());
					break;
				case 2:
					PrintLine("Please enter the new discount for senior: ");
					f = scanner.nextFloat();
					dataMgr.getPricePolicy().setSeniorDiscount(f);
					PrintLine("Current discount for senior is: " + dataMgr.getPricePolicy().getSeniorDiscount());
					break;
				case 3:
					PrintLine("Please enter the new increment for blockbuster: ");
					f = scanner.nextFloat();
					dataMgr.getPricePolicy().setBlockbusterInc(f);
					PrintLine("Current increment for blockbuster is: " + dataMgr.getPricePolicy().getBlockbusterInc());
					break;
				case 4:
					PrintLine("Please enter the new increment for 3D: ");
					f = scanner.nextFloat();
					dataMgr.getPricePolicy().setThreeDInc(f);
					PrintLine("Current increment for 3D is: " + dataMgr.getPricePolicy().getThreeDInc());
					break;
				case 5:
					PrintLine("Please enter the new increment for IMAX ");
					f = scanner.nextFloat();
					dataMgr.getPricePolicy().setiMAXInc(f);
					PrintLine("Current increment for IMAX is: " + dataMgr.getPricePolicy().getiMAXInc());
					break;
				case 6:
					PrintLine("Please enter the new increment for premiun: ");
					f = scanner.nextFloat();
					dataMgr.getPricePolicy().setPremiumInc(f);
					PrintLine("Current increment for premiun is: " + dataMgr.getPricePolicy().getPremiumInc());
					break;
				default:
					break;
			}
		}
	};

}
