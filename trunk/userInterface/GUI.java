package userInterface;
import dataClasses.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

// import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI{
	private DataSystem dataMgr;
	// our window
	private JFrame frame;
	// we have two major panels
	private JPanel pTop, pBottom;
	
	// p1 for browsing
	private JPanel p1;
	// cineplex list
	private JList<String> cineList;
	private Cineplex chosenCineplex;
	private Cineplex[] cinesInList;
	private DefaultListModel<String> cineplexes;
	// show list
	private JList<String> showList;
	private Show chosenShow;
	// the shows we put in show list
	private Show[] showsInList;
	private DefaultListModel<String> shows;
	private JTextArea showInfo;
	
	// p2 for searching
	private JPanel p2;
	private JTextField movieName;
	private JButton searchBut;
	
	private void InitP1(){
		p1 = new JPanel();
		p1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Browse in Cineplex"),
                BorderFactory.createEmptyBorder(5,5,5,5)));

		p1.setLayout(new GridLayout(0, 1));
		
		// create list of cineplexes
		p1.add(new JLabel("Choose Cineplex:"));
		cineplexes = new DefaultListModel<String>();
		cinesInList = new Cineplex[dataMgr.getCineplexCount()];
		for(int i = 0; i<dataMgr.getCineplexCount(); i++)
		{
			cineplexes.addElement(dataMgr.getCineplex(i).getName());
			cinesInList[i] = dataMgr.getCineplex(i);
		}
		cineList = new JList<String>(cineplexes);
		cineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cineList.setLayoutOrientation(JList.VERTICAL);
		cineList.addListSelectionListener(selectCineplex);
		p1.add(cineList);
	}
	private void InitP2(){
		p2 = new JPanel();
		p2.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Search For Movie"),
                BorderFactory.createEmptyBorder(5,5,5,5)));
		p2.setLayout(new GridLayout(0, 1));
		p2.add(new JLabel("Movie Name:"));
		movieName = new JTextField();
		p2.add(movieName);
		searchBut = new JButton("Search");
		searchBut.addActionListener(search);
		p2.add(searchBut);

		//p2.add(new JLabel("Information of Show:"));
	}
	private void InitBottom(){
		pBottom.add(new JLabel("Choose Show:"));
		shows = new DefaultListModel<String>();
		showList = new JList<String>(shows);
		showList.addListSelectionListener(selectShow);
		showList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		showList.setLayoutOrientation(JList.VERTICAL);
		pBottom.add(showList);
		
		// show info display
		pBottom.add(new JLabel("Information of Show:"));
		showInfo = new JTextArea();
		pBottom.add(showInfo);
	}
	public GUI(DataSystem d){
		dataMgr = d;
		frame = new JFrame("Movie System");
		frame.setSize(800, 600);
		frame.getContentPane().setLayout(new GridLayout(0,1));
		
		// we have two columns in top, one for p1 and one for p2
		pTop = new JPanel();
		pTop.setLayout(new GridLayout(0, 2));
		InitP1();
		InitP2();
		pTop.add(p1);
		pTop.add(p2);
		
		// we have one columns, and two rows, one for show selection, one for show info display
		pBottom=new JPanel();
		pBottom.setLayout(new GridLayout(0, 1));
		InitBottom();
		
		frame.getContentPane().add(pTop);
		frame.getContentPane().add(pBottom);
		// frame.pack();
		frame.setVisible(true);
	}
	private ListSelectionListener selectCineplex = new ListSelectionListener(){
		public void valueChanged(ListSelectionEvent arg0) {
			int index = cineList.getSelectedIndex();
			if(index==-1)return;
			// grab shows and update showList
			chosenCineplex = cinesInList[index];
			showsInList = chosenCineplex.getCurrentShows();
			shows.clear();
			for(int i = 0; i<showsInList.length; i++)
				shows.addElement(showsInList[i].getMovie().getName());
		}
	};
	private ListSelectionListener selectShow = new ListSelectionListener(){
		public void valueChanged(ListSelectionEvent arg0) {
			int index = showList.getSelectedIndex();
			if(index==-1)return;
			// grab show, update show info display
			chosenShow = showsInList[index];
			
			String info = "";
			info += "ID: "+chosenShow.getID()+"\n";
			info += "Name: "+chosenShow.getMovie().getName()+"\n";
			info += "Location: "+chosenShow.getCinema().getName()+", "+
					chosenShow.getCinema().getCineplex().getName()+"\n";
			info += "Time: "+chosenShow.getTime().toString();
			showInfo.setText(info);
		}
	};
	private ActionListener search = new ActionListener(){
		public void actionPerformed(ActionEvent arg0) {
			String name = movieName.getText();
			Movie[] movies = dataMgr.findMoviesWithName(name);
			if(movies==null || movies.length==0){
				JOptionPane.showMessageDialog(frame, "No movie with such name exists.");
				return;
			}
			// FIXME right now we only show the first returned result
			showsInList = movies[0].getCurrentShows();
			shows.clear();
			for(int i = 0; i<showsInList.length; i++)
				shows.addElement(showsInList[i].getMovie().getName()+"(in "+
					showsInList[i].getCinema().getCineplex().getName()+")");
		}
	};
}
