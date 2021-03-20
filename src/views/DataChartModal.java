package views;


import coviddataapp.CovidDataType;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import model.Coviddata;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class DataChartModal extends JDialog {
    
    public DataChartModal(JPanel parent, List<Coviddata> coviddataList, int linesNum, int qntType) {
        
        final CategoryDataset dataset = createDataset(coviddataList, qntType);
        final JFreeChart chart = createChart(dataset, coviddataList.get(0).getCountry().getName(), linesNum);
        final ChartPanel chartPanel = new ChartPanel(chart);
        
        initModal(chartPanel, parent);
    }
    private void initModal(ChartPanel chartPanel, Component parent){
        //LayoutManager
        setLayout(new FlowLayout());
        //Βάλε το γράφημα στο JDialog
        add(chartPanel);
        //Παράθυρο αποκλειστικής χρήσης
        setModal(true);
        //Θέτει τον τίτλο
        setTitle("Προβολή δεδομένων σε διάγραμμα");
        //Αλλαγή εικονιδίου στο παράθυρο
        setIconImage(new ImageIcon(getClass().getResource("/img/chart.png")).getImage());
        //Αν πατηθεί Χ, κλείνει το παράθυρο, δεν το κρύβει
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        //Δεν μπορεί ο χρήστης να αλλάξει το μέγεθος
        setResizable(false);
        //Εμφανίζεται στην τοποθεσία του parent view
        setLocation(parent.getLocationOnScreen());
        //Φτιάχνει το μέγεθος του παραθύρου σύμφωνα με το περιεχόμενο
        pack();
        //Εμφανίζει το παράθυρο
        setVisible(true);
    }
    private CategoryDataset createDataset(List<Coviddata> coviddataList, int qntType) {
        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        //Βάλε όλα τα coviddata στο γράφημα
        for(Coviddata d: coviddataList)
                            //row Value
            dataset.addValue(qntType == 0? d.getQty():d.getProodqty(), 
                            //row Key 
                             CovidDataType.getNameByValue(d.getDatakind()),
                            //column key
                             Instant.ofEpochMilli(d.getTrndate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        return dataset;
                
    }
    
    private JFreeChart createChart(final CategoryDataset dataset, String country, int linesNum) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createLineChart(
            country+": διάγραμμα δεδομένων Covid",       // chart title
            "Χρόνος",                    // domain axis label
            "Ποσότητα",                   // range axis label
            dataset,                   // data
            PlotOrientation.VERTICAL,  // orientation
            true,                      // include legend
            true,                      // tooltips
            false                      // urls
        );
        chart.setBackgroundPaint(Color.white);

        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);        
        //Άξωνας Χ με τις ημερομηνίες
        final CategoryAxis catAxis = plot.getDomainAxis();
        //Τις δείχνει με γωνία 45 μοιρών
        catAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
        //Λόγω του μεγάλου πλήθους των ημερομηνιών δείχνουμε μόνο την πρώτη την τελευταία και 3 ενδιάμεσες αν το εύρος το επιτρέπει
       for(int i=1; i<dataset.getColumnCount()-1; i++) {
            if(i == (int)(dataset.getColumnCount()*0.25) || i == (int)(dataset.getColumnCount()*0.5)|| i == (int)(dataset.getColumnCount()*0.75) )
                continue;
            //Κρύβει τις υπόλοιπες ημερομηνίες
            catAxis.setTickLabelPaint(dataset.getColumnKey(i), new Color(0,0,0,0));
        }
        // customise the range axis...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);
        
        // customise the renderer...
        final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        //Φτιάχνει τις 3 γραμμές, σε πάχος και στηλ
        for(int i=0; i<linesNum; i++) {
            renderer.setSeriesStroke(
                i, new BasicStroke(
                    3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                    1.0f, new float[] {1.0f}, 0.0f
                )
            );
        }
        return chart;
    }
}
