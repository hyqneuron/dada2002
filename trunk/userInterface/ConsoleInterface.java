package userInterface;
import dataClasses.*;

import java.util.Date;
import java.awt.List;
import java.util.ArrayList;
import java.util.Calendar;
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
	private interface MenuPriorAction
	{
		public void Show(Object o);
	}
	private class Menu
	{
		public String name;  // e.g. "Home"
		public String title; // e.g. "Customer Main Menu"
		public MenuOption[] options;
		public MenuPriorAction priorAction;
		public Object priorParam;
		public Menu(String name, String title, MenuOption[] options)
		{
			this.name = name;
			this.title = title;
			this.options = options;
		}
		public Menu(String name, String title, MenuOption[] options, MenuPriorAction priorAction, Object o)
		{
			this.name = name;
			this.title = title;
			this.options = options;
			this.priorAction = priorAction;
			this.priorParam = o;
		}	
	}
	
	
	
	//===========entry point and constructor=========================
	
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
	
	//===================convenience functions for input/output=====================
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
	private Date AskForDate()
    {
        Calendar cal1 = Calendar.getInstance();
        int month, day, year;
         
        day = AskForChoice(1,31,"Please enter a month MM: ");
        month = AskForChoice(1,12,"Please enter a month MM: ");
        year = AskForChoice(2000,2020,"Please enter a month MM: ");
        
        System.out.println("You chose: " + month + " " + day + " " + year);
        cal1.set(year, month-1, day);
        return cal1.getTime();
    }
	private Date AskForTime()
	{
        Calendar cal1 = Calendar.getInstance();
        int month, day, year, hour, minute;
         
        month = AskForChoice(1,12,"Please enter a month MM: ");
        day   = AskForChoice(1,31,"Please enter a day  DD: ");
        year   = AskForChoice(2000,2020,"Please enter a year YYYY: ");
        hour   = AskForChoice(0,23,"Please enter hour: ");
        minute   = AskForChoice(0,59,"Please enter minute: ");
        cal1.set(year, month-1, day, hour, minute);
        return cal1.getTime();
	}

	
	private int AskForChoice(int min, int max){
		return AskForChoice(min, max, "");
	}
	// ask for user input, valid from min to max, otherwise ask to reenter
	private int AskForChoice(int min, int max, String prompt)
	{
		int result=min-1;
		String input;
		while(result<min || result>max)
		{
			Print(prompt);
			input = scanner.nextLine();
			// first time we print prompt passed in, second time and on we print error msg below
			prompt = "Invalid number. Please re-enter: ";
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
	
	
	// =========================== printing data objects ========================
	private void PrintMovie(Movie movie)
	{
		Format("Name: %s%n", movie.getName());
		if(staffLogin!=null)
			Format("ID: %s %n", movie.getID());
		Format("Description: %s%nDuration: %d minutes, Available showings: %d%n",
				movie.getDescription(), 
				movie.getDuration(), movie.getShows().size());
		Format("End of show: %s%n%n", movie.getEndOfShow()?"Yes":"No");
	}
	private void PrintShow(Show show)
	{
		Movie m = show.getMovie();
		Format("Name: %s%nShow ID: %s, Duration: %4d minutes, Available seats: %d%n", 
				m.getName(), show.getID(), m.getDuration(), show.CountEmptySeats());
		Format("Cineplex: %s%n", show.getCinema().getCineplex().getName());
		Format("Cinema: %s%n", show.getCinema().getName());
		Format("Time: %s%n", show.getTime().toString());
		PrintLine("");
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
		Format("Customer type: %s%n", ticket.getCustomerType());
		Format("Show type: %s%n", ticket.getShow().getShowType());
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
	
	// ==========================MENU  ====================================
	// We use this method to show a list of options
	// Each option is a string array of length 2 to 3, with third element optional
	// ["String to show in menu","name of function to call when chosen"]
	// such as: ["Continue as guest", "GuestMainMenu"]
	private void ShowMenu(Menu menu)
	{
		menuStack.push(menu);
	}
	private void ClearMenu()
	{
		menuStack.clear();
	}
	private void LeaveSubMenu()
	{
		menuStack.pop();
	}
	// print a menu and ask user for choice
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
			// if menu has a priorAction, execute it first
			if(menu.priorAction!=null)
				menu.priorAction.Show(menu.priorParam);
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
	


	//=================Miscellaneous menu function==================
	private MenuAction actionBack = new MenuAction(){
		public void Show(Object o){
			LeaveSubMenu();
		}
	};
	private MenuAction actionLeave = new MenuAction(){
		public void Show(Object o){
			PrintLine("Have a nice day!");
			ClearMenu();
			return;
		}
	};
	private MenuAction actionShowGUI = new MenuAction(){
		public void Show(Object o){
			GUI gui = new GUI(dataMgr);
		}
	};
	
	
	//=================login     ==================
	
	// who is logged in, customer or staff?
	private enum LoginType {None, Customer, Staff};
	private LoginType loginType = LoginType.None;
	// which customer or staff is logged in
	private Customer customerLogin;
	private Staff staffLogin;
	
	// ask if user wants to login
	// used as the entry point of ConsoleInterface
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


	private MenuAction actionLogout = new MenuAction(){
		public void Show(Object o){
			customerLogin = null;
			staffLogin = null;
			loginType = LoginType.None;
			ClearMenu();
			menuLogin.Show(null);
		}
	};

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
		String password = AskForString("Please enter password: ");
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
		//LeaveSubMenu();
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
						new MenuOption("Check Account History",	actionCheckHistory),
						new MenuOption("Show GUI",				actionShowGUI),
						new MenuOption("Log out", 				actionLogout),
						new MenuOption("Exit system", 			actionLeave)
					});
			else if(loginType==LoginType.None)
				menu = new Menu("Home", "Guest Main Menu", new MenuOption[]{
						new MenuOption("List Movies", 			menuListMovies),
						new MenuOption("Book Movie", 			actionBookMovie),
						new MenuOption("Check Booking Status", 	actionCheckBooking),
						new MenuOption("Register Account", 		actionRegisterCustomer),
						new MenuOption("Show GUI",				actionShowGUI),
						// actionLogout will exit to login menu, so it is used for log in option
						new MenuOption("Log in", 				actionLogout, true),
						new MenuOption("Exit System", 			actionLeave)
					});
			else // staff
				menu = new Menu("Home", "Staff Main Menu", new MenuOption[]{
						new MenuOption("List Movies", 			menuListMovies),
						new MenuOption("Book movie",			actionBookMovie),
						new MenuOption("Add/Edit Movies", 		menuEditMovies),
						new MenuOption("Add/Edit Shows", 		menuEditShows),
						new MenuOption("Query Reveues(get total)", 		actionPrintRevenue1),
						new MenuOption("Query Reveues(get list)", 		actionPrintRevenue2),
						new MenuOption("Change Pricing Policy", menuChangePricingPolicy),
						new MenuOption("Show GUI",				actionShowGUI),
						new MenuOption("Log out", 				actionLogout),
						new MenuOption("Exit system", 			actionLeave)
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
	
	//====================check booking status===========================
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
	// =============================Register customer==================
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
	// ============== Check booking history ===============================
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
	
	
	

	//============================================================
	// Staff-only section starts here
	//============================================================


	// ================= Add/Edit Movies =========================
	private MenuAction menuEditMovies = new MenuAction(){
		public void Show(Object o){
			ArrayList<MenuOption> arr = new ArrayList<MenuOption>();
			arr.add(new MenuOption("Creat a movie", actionCreateMovie));
			arr.add(new MenuOption("Edit a movie",	actionEditMovie));
			arr.add(new MenuOption("Back",			actionBack));
			MenuOption[] options = new MenuOption[arr.size()];
			for(int i = 0; i<options.length; i++)
				options[i] = arr.get(i);
			Menu menu = new Menu("Editing", "Movie Editing menu", options);
			ShowMenu(menu);
		}
	};
	private MenuAction actionCreateMovie = new MenuAction(){
		public void Show(Object o){
			Movie movie;
			String name = AskForString("Please enter the name of the movie: ");
			String description = AskForString("Please enter the description of the movie: ");
			int duration = AskForChoice(0, 100000, "Please enter the duration of the movie: ");
			int ageLimit = AskForChoice(0, 200, "Please enter the age limit of the movie: ");
			String movieType = AskForString("Please enter the movie type of the movie: ").toLowerCase();
			while((movieType.compareTo("normal") != 0) && (movieType.compareTo("blockbuster")!= 0 )){
				movieType = AskForString("Please enter the movie type of the movie(normal or blockbuster): ").toLowerCase();
			}
			if(movieType == "normal")
				movie = new Movie(name, description, duration, ageLimit, 
						PricePolicy.MovieType.Normal, false);
			else
				movie = new Movie(name, description, duration, ageLimit,
						PricePolicy.MovieType.Blockbuster, false);
			
			dataMgr.addMovie(movie);
			PrintMovie(movie);
		}
	};
	
	private MenuAction actionEditMovie = new MenuAction(){
		public void Show(Object o){
			String id = AskForString("Please enter the id of the movie: ");
			Movie movie = dataMgr.findMovieWithID(id);
			if(movie==null)
			{
				PrintLine("Invalid ID given.");
				return;
			}
			ArrayList<MenuOption> arr = new ArrayList<MenuOption>();
			arr.add(new MenuOption("Change description", actionChangeMovieDescription, movie));
			arr.add(new MenuOption("Change duration",	actionChangeMovieDuration, movie));
			arr.add(new MenuOption("Change age limit",  actionChangeMovieAgeLimit, movie));
			arr.add(new MenuOption("Change movie type", actionChangeMovieType, movie));
			arr.add(new MenuOption("Change EndOfShow status: ", actionChangeEndOfShow, movie));
			arr.add(new MenuOption("Back",			actionBack));
			MenuOption[] options = new MenuOption[arr.size()];
			for(int i = 0; i<options.length; i++)
				options[i] = arr.get(i);
			// movie will be printed before the options above are shown
			MenuPriorAction priorEditMovie = new MenuPriorAction(){
				public void Show(Object o){
					PrintMovie((Movie)o);
				}
			};
			Menu menu = new Menu("Edit Movie", "Specific Movie Editing Menu", options, priorEditMovie, movie);
			ShowMenu(menu);
		}
	};
	
	
	private MenuAction actionChangeMovieDescription = new MenuAction(){
		public void Show(Object o){
			PrintLine("Please enter the description of the movie: ");
			String description = AskForString();
			((Movie)o).setDescription(description);
		}
	};
	
	private MenuAction actionChangeMovieDuration = new MenuAction(){
		public void Show(Object o){
			PrintLine("Please enter the duration of the movie: ");
			int duration = AskForChoice(0, 100000);
			((Movie)o).setDuration(duration);
		}
	};
	
	private MenuAction actionChangeMovieAgeLimit = new MenuAction(){
		public void Show(Object o){
			PrintLine("Please enter the age limit of the movie: ");
			int ageLimit = AskForChoice(0, 200);
			((Movie)o).setAgeLimit(ageLimit);
		}
	};
	
	private MenuAction actionChangeMovieType = new MenuAction(){
		public void Show(Object o){
			PrintLine("Please enter the type of the movie: ");
			String movieType = AskForString().toLowerCase();
			if(movieType == "normal")
				((Movie)o).setMovieType(PricePolicy.MovieType.Normal);
			else
				((Movie)o).setMovieType(PricePolicy.MovieType.Blockbuster);
		}
	};
	
	private MenuAction actionChangeEndOfShow = new MenuAction(){
		public void Show(Object o){
			if(((Movie)o).hasCurrentShow() == false)
				((Movie)o).setEndOfShow(false);
		}
	};
	
	

	// =================Add/Edit Shows============================
	private MenuAction menuEditShows= new MenuAction(){
		public void Show(Object o){
			Menu menu = new Menu("Edit Shows", "Edit Shows", new MenuOption[]{
					new MenuOption("Edit", menuEditShow),
					new MenuOption("Add", actionAddShow),
					//new MenuOption("Delete",actionChangeEndOfShow),
					new MenuOption("Back",actionBack)
			});
			ShowMenu(menu);
		}
	};
	private Show showEditing; // the show we're editing
	private MenuAction menuEditShow= new MenuAction(){
		public void Show(Object o){
			PrintLine("Input the Show ID:");
			String showID = AskForString();
			Show show = dataMgr.findShowWithID (showID);
			if(show==null)
			{
				PrintLine("The show you specified does not exist.");
				return;
			}
			if(show.hasSales(dataMgr))
			{
				PrintLine("The show already has sales and cannot be edited.");
				return;
			}
			MenuPriorAction priorA = new MenuPriorAction(){
				public void Show(Object o){
					PrintShow(showEditing);
				}
			};
			// we use parameter to indicate choice, so gotta save show in a member variable
			showEditing = show;
			Menu menu = new Menu("Edit Specific Show", "Edit Specific Show", new MenuOption[]{
					new MenuOption("Change Cinema", actionEditShow, 0), 
					new MenuOption("Change Time", actionEditShow, 1), 
					new MenuOption("Change Type", actionEditShow, 2), 
					new MenuOption("Remove Show", actionEditShow, 3)
				},
				priorA, show
			);
			ShowMenu(menu);
		}
	};
	private MenuAction actionEditShow = new MenuAction(){
		
		public void Show(Object o){
			int choice = (int)o;
			switch (choice){
			case 0: // cinema
				String cinemaName = AskForString("Input the Cineplex:");
				Cinema cinema = showEditing.getCinema().getCineplex().findCinemaWithName(cinemaName);
				if(cinema==null)
				{
					PrintLine("Invalid cinema name entered.");
					return;
				}
				showEditing.setCinema(cinema);
				break;
			case 1: // time
				Date time = AskForTime();
				showEditing.setTime(time);
				break;
			case 2: // type
				String type = AskForString("Please enter the show type (2D, 3D, or IMAX): ");
				type = type.toLowerCase();
				PricePolicy.ShowType showType;
				if(type.compareTo("2d")==0)
					showType = PricePolicy.ShowType.TwoD;
				else if(type.compareTo("3d")==0)
					showType = PricePolicy.ShowType.ThreeD;
				else if(type.compareTo("imax")==0)
					showType = PricePolicy.ShowType.IMAX;
				else
				{
					PrintLine("You have entered an invalid show type.");
					return;
				}
				showEditing.setShowType(showType);
				break;
			case 3: // delete
				dataMgr.removeShow(showEditing);
				LeaveSubMenu();
				break;
			}
		}
	};
	private MenuAction actionAddShow = new MenuAction(){
		public void Show(Object o){
			String movieID = AskForString("Input the Movie ID:");
			Movie movie = dataMgr.findMovieWithID(movieID);
			if(movie==null){
				PrintLine("The movie with specified ID cannot be found.");
				return;
			}
			String cineplexName = AskForString("Input the Cineplex name:");
			Cineplex cineplex = dataMgr.findCineplexWithName(cineplexName);
			if(cineplex==null){
				PrintLine("The cineplex with specified name cannot be found.");
				return;
			}
			String cinemaName = AskForString("Input the Ciname name:");
			Cinema cinema = cineplex.findCinemaWithName(cinemaName);
			if(cinema==null){
				PrintLine("The cinema with specified name cannot be found.");
				return;
			}
			String type = AskForString("Input the showType (2D, 3D, or IMAX):");
			PricePolicy.ShowType showType;
			while(true){
				if(type.compareToIgnoreCase("2D")==0)
					showType=PricePolicy.ShowType.TwoD;
				else if(type.compareToIgnoreCase("3D")==0)
					showType=PricePolicy.ShowType.ThreeD;
				else if(type.compareToIgnoreCase("IMAX")==0)
					showType=PricePolicy.ShowType.IMAX;
				else{
					type=AskForString("Invalid type. Please re-enter (2D, 3D, or IMAX):");
					continue;
				}
				break;
			}
			PrintLine("Please enter the time of the show:");
			Show show = new Show (movie,cinema,showType,AskForTime());
			dataMgr.addShow(show);
			PrintLine("Add Succesfully!");
			PrintShow(show);
		}
	};
	//====================Revenues ============================
	private MenuAction actionPrintRevenue1 = new MenuAction(){
		public void Show(Object o){
			String s;
			Movie m = null;
			Cineplex c = null;
			Date cal1 = null ,cal2 = null;
			m = dataMgr.findMovieWithID(AskForString("Please enter the movie ID: (0: choose all the movie)"));
			c = dataMgr.findCineplexWithName(AskForString("Please enter the cineplex name: (0: choose all the cineplex)"));
			s = AskForString("Do you want to check a certain period? (Y/N)");
			if (s.toLowerCase().startsWith("y")){
				Print("Please enter the starting date: ");
				cal1 = AskForDate();
				Print("Please enter the end date: ");
				cal2 = AskForDate();
			}
			if (cal1==null)
				Format("Revenue:  SGD %5.1f%n", dataMgr.calcRevenue(m, c, cal1, cal2));
			else if (cal2.compareTo(cal1)>=0)
				Format("Revenue:  SGD %5.1f%n", dataMgr.calcRevenue(m, c, cal1, cal2));
			else
				PrintLine("Wrong input!!!");
		}
	};
	
	private MenuAction actionPrintRevenue2 = new MenuAction(){
		public void Show(Object o){
			Movie m[] = dataMgr.getAllCurrentMovies();
			Cineplex c = null;
			Date cal1 = null ,cal2 = null;
			c = dataMgr.findCineplexWithName(AskForString("Please enter the cineplex name: "));
			for (int i = 0;i < dataMgr.getAllCurrentMoviesCount();i++)
				Format("Revenue:  %s SGD %6.1f%n", 
								m[i].getName(), 
								dataMgr.calcRevenue(m[i], c, cal1, cal2));
		}
	};

	//=================Change pricing policy======================
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
					new MenuOption("Change GST", actionChangePolicy, 7),
					new MenuOption("Print current pricing policy", actionChangePolicy, 8),
					new MenuOption("Back",actionBack)
			});
			ShowMenu(menu);
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
				case 7:
					PrintLine("Please enter the new GST: ");
					f = scanner.nextFloat();
					dataMgr.getPricePolicy().setGST(f);
					PrintLine("Current GST is: " + dataMgr.getPricePolicy().getGST());
					break;
				case 8:
					PrintLine("Current pricing police: ");
					PrintLine("Current base price is: " + dataMgr.getPricePolicy().getBasePrice());
					PrintLine("Current discount for student is: " + dataMgr.getPricePolicy().getStudentDiscount());
					PrintLine("Current discount for senior is: " + dataMgr.getPricePolicy().getSeniorDiscount());
					PrintLine("Current increment for blockbuster is: " + dataMgr.getPricePolicy().getBlockbusterInc());
					PrintLine("Current increment for 3D is: " + dataMgr.getPricePolicy().getThreeDInc());
					PrintLine("Current increment for IMAX is: " + dataMgr.getPricePolicy().getiMAXInc());
					PrintLine("Current increment for premiun is: " + dataMgr.getPricePolicy().getPremiumInc());
					PrintLine("Current GST is: " + dataMgr.getPricePolicy().getGST());
					break;
				default:
					break;
			};
		}
	};
}
