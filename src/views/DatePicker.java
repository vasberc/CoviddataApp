package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 *Πηγή https://www.roseindia.net/tutorial/java/swing/datePicker.html
 * 
 */
public class DatePicker extends JDialog{
    private LocalDate fromDate;//Ελάχιστη ημερομηνία
    private LocalDate toDate;//Μέγιστη ημερομηνία
    private LocalDate cal;//Ημερομηνία εκκίνησης
    private LocalDate selectedDate;
    private int month; //μεταβλητή που διαχειρίζεται τον μήνα
    private int year;//μεταβλητή που μεταχειρίζεται τον χρόνο

    private JLabel l = new JLabel("", JLabel.CENTER);//label που γράφει μήνα και έτος
    private String day = "";//μεταβλητή που διαχειρίζεται την ημέρα
    
    private JButton[] button = new JButton[42];
    private JLabel dayLabel;
    private Border blackline = BorderFactory.createLineBorder(Color.GRAY);
    private JButton previous;
    private JButton next;

    public DatePicker(Component parent, LocalDate fromDate, LocalDate toDate, LocalDate currentDate) {
        //Αρχικοποίηση τιμών
        this.fromDate = fromDate;
        this.toDate = toDate;
        if(currentDate.isBefore(fromDate))
            this.cal = fromDate;
        else this.cal = currentDate;
        this.month = cal.getMonthValue();
        this.year  = cal.getYear();
        this.selectedDate = currentDate; 
        initDatePicker(parent);
    }
    private void initDatePicker(Component parent) {
        setTitle("Επιλέξτε ημερομηνία");
        //Αλλαγή εικονιδίου στο παράθυρο
        setIconImage(new ImageIcon(getClass().getResource("/img/calendar.png")).getImage());
        setModal(true);//Αποκλειστικό παράθυρο
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        String[] header = {  "Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"  };
        //Πάνελ που θα μπουν τα περιεχόμενα
        JPanel p1 = new JPanel(new GridLayout(7, 7));
        //Επιθυμητές διαστάσεις πάνελ
        p1.setPreferredSize(new Dimension(430, 120));
        //Αρχικοποίηση label με τις μέρες
        for(int x = 0; x < 7; x++){
            dayLabel = new JLabel(header[x], SwingConstants.CENTER);
            dayLabel.setForeground(Color.red);
            dayLabel.setBackground(Color.white);
            dayLabel.setBorder(blackline);
            p1.add(dayLabel);
        }
        //Αρχικοποίηση κουμπιών
        for (int x = 0; x < button.length; x++) {
                final int selection = x;
                button[x] = new JButton();
                button[x].setFocusPainted(false);
                button[x].setBackground(Color.white);			
                button[x].addActionListener(new ActionListener() {
                    //Αν πατηθεί το κουμπί, θέτει το day και κλείνει το παράθυρο
                    public void actionPerformed(ActionEvent ae) {
                            day = button[selection].getActionCommand();
                            dispose();
                    }
                });
                p1.add(button[x]);
        }
        //Αρχικοποίηση κουμπιού previous
        JPanel p2 = new JPanel(new GridLayout(1, 3));
        previous = new JButton("<< Previous");
        //Αν πατηθεί το κουμπί αφαιρεί έναν μήν από το cal και το χτίζει πάλι
        previous.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                        cal = cal.minusMonths(1);
                        next.setEnabled(true);
                        displayDate();
                }
        });
        p2.add(previous);
        p2.add(l);
        //Αρχικοποίηση του κουμπιού next
        next = new JButton("Next >>");
        //Αν πατηθεί αυξάνει 1 μήνα το cal και το χτίζει πάλι
        next.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                        cal = cal.plusMonths(1);
                        previous.setEnabled(true);
                        displayDate();
                }
        });
        p2.add(next);
        //τοποθέτηση του πάνελ με τα κουμπιά
        this.add(p1, BorderLayout.CENTER);
        //τοποθέτηση του πάνελ με τα previews next
        this.add(p2, BorderLayout.SOUTH);
        this.pack();
        this.setLocationRelativeTo(parent);
        displayDate();
        this.setVisible(true);
    }
    //Εμφανίζει τις μέρες στα κουμπιά για κάποιο μήνα και κάποιο έτος
    public void displayDate() {
        month = cal.getMonthValue();
        year = cal.getYear();  
        //Ξεκινάω από 1 του μήνα για να γράψω τις τιμές
        cal = LocalDate.of(year, month, 1);
        int dayOfWeek = cal.getDayOfWeek().getValue();
        int daysInMonth = cal.lengthOfMonth();
        //Για κάθε κουμπί
        for (int x = 0; x < button.length; x++){
            //Αν ο δείκτης είναι πριν την πρώτη μέρα του μήνα ή μετά την τελευταία μέρα
            if(x < dayOfWeek-1 || x > dayOfWeek+daysInMonth-2){
                button[x].setEnabled(false);//απενεργοποίησε το κουμπί
                button[x].setText("");//Σβήσε το κείμενο
            }
            //Αλλιώς
            else {
                button[x].setText("");//Σβήσε το κείμενο
                button[x].setEnabled(true);//Ενεργοποίησε το κουμπί                    
            }
            //Σε κάθε κουμπί το χρώμα της γραμματοσειράς να είναι μαύρο
            button[x].setForeground(Color.BLACK);
        }
        //Διαπερνάω τα κουμπιά μόνο που ο δείκτης τους είναι μέσα σε ημέρα του μήνα
        for (int x = dayOfWeek-1, day1 = 1; day1 <= daysInMonth; x++, day1++) {
            //Γράψε τη μέρα στο κουμπί
            button[x].setText("" + day1);
            //Αν η μέρα αντιστοιχεί με την προηγούμενη επιλογή του χρήστη
            if(selectedDate.getDayOfMonth() == day1 && selectedDate.getMonthValue() == month && selectedDate.getYear() == year) {
                button[x].setForeground(Color.red);//Κάνε κόκκινα τα γράμματα
                button[x].setEnabled(false);//Απενεργοποίησε το κουμπί
            }
        }
        //Γράψε στο label μήνα και έτος
        l.setText(cal.getMonth()+" "+String.valueOf(cal.getYear())) ;
        //Εάν ο μήνας και το έτος είναι ίσα με το fromDate
        if ((month == fromDate.getMonthValue()) && (year == fromDate.getYear())) {
            previous.setEnabled(false);//Απενεργοποίησε το κουμπί previews
            //Διαπέρασε όλα τα κουμπιά πρίν την αποδεκτή ημερομηνία
            for(int x = 0; x < dayOfWeek+fromDate.getDayOfMonth()-2; x++)                
                button[x].setEnabled(false);//απενεργοποίησε τα 
        }
        //Εάν ο μήνας και το έτος είναι ίσα με το toDate
        if (month == toDate.getMonthValue() && year == toDate.getYear()){
            next.setEnabled(false);//απενεργοποίησε το next
            //Διαπέρασε όλα τα κουμπιά μετά την αποδεκτή ημερομηνία
            for(int x = dayOfWeek+toDate.getDayOfMonth()-1; x < button.length; x++)                
                button[x].setEnabled(false);//απενεργοποίησε τα
        }
    }
    //Καλείται μαζί με τον constructor
    public String setPickedDate() {
        //Αν ο χρήστης έχει πατήσει Χ για να κλείσει το παράθυρο
        if (day.equals(""))
                return day;//Επιστρέφει το κενό
        //Ημερομηνία που επέλεξε ο χρήστης
        LocalDate date = LocalDate.of(year, month, Integer.parseInt(day));
        //Την επιστρέφει στο επιθυμητό format
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
