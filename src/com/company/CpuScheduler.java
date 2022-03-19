import java.awt.*;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.gantt.GanttCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.ui.ApplicationFrame;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static java.awt.Color.*;

public class CpuScheduler extends ApplicationFrame {

    public static ArrayList<Process> processes = new ArrayList<>();
    static TaskSeriesCollection collection;
    static CategoryPlot plot;
    static JTabbedPane tabbedPane;
    public static int contextSwitch =0;
    public static SJF SJFschedule;
    public static SRTF SRTFschedule;
    public static PS PSschedule;
    public static AGAT AGATschedule;

    public JPanel createCTPanel(ArrayList<Process> processes,int index) {
        final GanttCategoryDataset dataset = createDataset(processes);
        final JFreeChart chart = createChart(dataset , index);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        JPanel CTPanel = new JPanel(); //Chart-Table Panel
        CTPanel.setLayout(new FlowLayout());
        setContentPane(chartPanel);
        ///////////////////////////////////////////////////////////
        String[][] data = new String[processes.size()][3];
        for (int i=0; i < processes.size(); i++){
            data[i][0] = processes.get(i).getName();
            data[i][1] = String.valueOf(processes.get(i).getWaitingTime());
            data[i][2] = String.valueOf(processes.get(i).getTurnaroundTime());
        }
        String[] column = {"Name", "Waiting", "Turnaround"};
        JTable jt = new JTable(data, column);
        jt.setBounds(30, 40, 200, 300);
        jt.setFont(new Font("Arial",Font.BOLD,15));
        jt.getTableHeader().setFont(new Font("Arial",Font.ITALIC,17));
        jt.setRowHeight(jt.getRowHeight()+10);
        JScrollPane sp = new JScrollPane(jt);
        CTPanel.add(chartPanel);
        CTPanel.add(sp);
        CTPanel.setBackground(DARK_GRAY);
        return CTPanel;
    }

    public JPanel createLabelPanel(ArrayList<Process> processes, int index) {
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        JLabel label = new JLabel("<HTML><U>Statistics</U></HTML>");
        label.setForeground(RED);
        label.setFont(new Font("Serif", Font.BOLD, 18));
        labelPanel.add(label, BorderLayout.LINE_START);
        JLabel label1 ;
        JLabel label2;
        JScrollPane scrollableInput = null;
        JTextArea textArea = null;
        switch (index){
            case 1:{
                labelPanel.removeAll();
                labelPanel.add(label, BorderLayout.LINE_START);
                String temp = "Average Waiting Time:"+ String.valueOf(PSschedule.getAverageWaiting());
                label1 = new JLabel(temp);
                temp = "Average Turnaround Time:"+ String.valueOf(PSschedule.getAverageTurnaround());
                label2 = new JLabel(temp);
                break;
            }
            case 2:{
                labelPanel.removeAll();
                labelPanel.add(label, BorderLayout.LINE_START);
                String temp = "Average Waiting Time:"+ String.valueOf(SJFschedule.getAverageWaiting());
                label1 = new JLabel(temp);
                temp = "Average Turnaround Time:"+ String.valueOf(SJFschedule.getAverageTurnaround());
                label2 = new JLabel(temp);
                break;
            }
            case 3:{
                labelPanel.removeAll();
                labelPanel.add(label, BorderLayout.LINE_START);
                String temp = "Average Waiting Time:"+ String.valueOf(SRTFschedule.getAverageWaiting());
                label1 = new JLabel(temp);
                temp = "Average Turnaround Time:"+ String.valueOf(SRTFschedule.getAverageTurnaround());
                label2 = new JLabel(temp);
                break;
            }
            case 4:{
                labelPanel.removeAll();
                labelPanel.add(label, BorderLayout.LINE_START);
                String temp = "Average Waiting Time:"+ String.valueOf(AGATschedule.getAverageWaiting());
                label1 = new JLabel(temp);
                temp = "Average Turnaround Time:"+ String.valueOf(AGATschedule.getAverageTurnaround());
                label2 = new JLabel(temp);
                temp = "";
                for (int i =0; i < AGATschedule.getAGATrocesses().size(); i++){
                    temp += AGATschedule.getAGATrocesses().get(i).getName() + ":  Quantum List: " + AGATschedule.getAGATrocesses().get(i).getQuntam().toString() + "  AGAT Factor: " +  AGATschedule.getAGATrocesses().get(i).getAGATfactor().toString() + " \n";
                }
                textArea = new JTextArea(temp);
                scrollableInput = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                scrollableInput.setViewportView(textArea);
                scrollableInput.setPreferredSize(new Dimension(100,150));
                break;
            }
            default:{
                label1 = new JLabel();
                label2 = new JLabel();
                break;
            }
        }

        label1.setForeground(white);
        label1.setFont(new Font("Serif", Font.PLAIN, 18));
        label2.setForeground(white);
        label2.setFont(new Font("Serif", Font.PLAIN, 18));
        labelPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        labelPanel.add(label1);
        labelPanel.add(label2);
        if (index==4){
            labelPanel.add(scrollableInput , BorderLayout.LINE_START);
        }
        labelPanel.setBackground(DARK_GRAY);
        labelPanel.add(Box.createRigidArea(new Dimension(5, 20)));
        return labelPanel;
    }

    public CpuScheduler(final String title) {

        super(title);
        JFrame frame = new JFrame("CPU SCHEDULING");
        frame.getContentPane().setBackground(darkGray);
        frame.setLayout(new BorderLayout());

        ///////////////////////////////////////////////////////////
        tabbedPane = new JTabbedPane();
        final JPanel  panel2, panel3,panel4,panel5;
        AddProcessPanel panel1 = new AddProcessPanel();
        panel1.setPreferredSize(new Dimension(1300,900));
        panel2 = new JPanel();
        panel3 = new JPanel();
        panel4 = new JPanel();
        panel5 = new JPanel();


        tabbedPane.addTab("Add Processes", panel1);
        tabbedPane.addTab("Priority Scheduling", panel2);
        tabbedPane.addTab("SJF Scheduling", panel3);
        tabbedPane.addTab("SRTF Scheduling", panel4);
        tabbedPane.addTab("AGAT Scheduling", panel5);
        frame.add(tabbedPane);
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JPanel CTPanel;
                JPanel labelPanel;

                int index = tabbedPane.getSelectedIndex();
                switch (index) {
                    case 0: {
                        frame.getContentPane().removeAll();
                        frame.add(tabbedPane);
                        break;
                    }
                    case 1: {
                        frame.getContentPane().removeAll();
                        frame.add(tabbedPane);
                        tabbedPane.setSelectedIndex(1);
                        PSschedule = new PS(processes);
                        PSschedule.setContextSwitch(contextSwitch);
                        PSschedule.start();
                        CTPanel = createCTPanel(PSschedule.getPSFProcesses(),1);
                        tabbedPane.setComponentAt(1,CTPanel);;
                        labelPanel = createLabelPanel(processes,1);
                        frame.getContentPane().add(labelPanel, BorderLayout.PAGE_END);
                        break;
                    }
                    case 2: {
                        frame.getContentPane().removeAll();
                        frame.add(tabbedPane);
                        tabbedPane.setSelectedIndex(2);
                        SJFschedule = new SJF(processes);
                        SJFschedule.start();
                        CTPanel = createCTPanel(SJFschedule.getSJFProcesses(),2);
                        tabbedPane.setComponentAt(2,CTPanel);
                        labelPanel = createLabelPanel(processes,2);
                        frame.getContentPane().add(labelPanel, BorderLayout.PAGE_END);
                        break;
                    }
                    case 3: {
                        frame.getContentPane().removeAll();
                        frame.add(tabbedPane);
                        tabbedPane.setSelectedIndex(3);
                        SRTFschedule = new SRTF(processes);
                        SRTFschedule.setContextSwitch(contextSwitch);
                        SRTFschedule.start();
                        CTPanel = createCTPanel(SRTFschedule.getSRTFProcesses(),3);
                        tabbedPane.setComponentAt(3,CTPanel);
                        labelPanel = createLabelPanel(processes,3);
                        frame.getContentPane().add(labelPanel, BorderLayout.PAGE_END);
                        break;
                    }
                    case 4: {
                        frame.getContentPane().removeAll();
                        frame.add(tabbedPane);
                        tabbedPane.setSelectedIndex(4);
                        AGATschedule = new AGAT(processes);
                        //AGATschedule.setContextSwitch(contextSwitch);
                        AGATschedule.start();
                        CTPanel = createCTPanel(AGATschedule.getAGATrocesses(),4);
                        tabbedPane.setComponentAt(4,CTPanel);
                        labelPanel = createLabelPanel(processes,4);
                        frame.getContentPane().add(labelPanel, BorderLayout.PAGE_END);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        });

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static void fillProcesses(String Name,int AT,int BT,int PN,int Q){
        Process temp = new Process();
        temp.setName(Name);
        temp.setBurstTime(BT);
        temp.setArrivalTime(AT);
        temp.setPriority(PN);
        temp.setQUN(Q);
        processes.add(temp);
    }

    public static GanttCategoryDataset createDataset(ArrayList<Process> processes) {

        collection = new TaskSeriesCollection();
        for (int i =0; i< processes.size(); i++){
            final TaskSeries s1 = new TaskSeries(processes.get(i).getName());
            final Task t2 = new Task(processes.get(i).getName(), new SimpleTimePeriod(0, 0));
            ArrayList<int[]> periods = processes.get(i).getPeriods();
            for (int j =0; j < periods.size(); j++){
                t2.addSubtask(new Task("", new SimpleTimePeriod(periods.get(j)[0],periods.get(j)[1])));
            }
            s1.add(t2);
            collection.add(s1);
        }

        return collection;
    }


    public JFreeChart createChart(final GanttCategoryDataset dataset, int index) {
        final JFreeChart chart = ChartFactory.createGanttChart(
                "CPU Scheduling Graph ", // chart title
                "Process", // domain axis label
                "TIME(MS)", // range axis label
                dataset,
                true, // include legend
                true, // tooltips
                false // urls
        );

        chart.getTitle().setPaint(Color.RED);
        chart.setBackgroundPaint(Color.DARK_GRAY);

        plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(darkGray);
        plot.setRangeGridlinePaint(darkGray);
        NumberAxis axis = new NumberAxis();
        axis.setTickUnit(new NumberTickUnit(1));
        plot.getDomainAxis().setTickLabelPaint(Color.white);
        axis.setLabelPaint(Color.white);
        axis.setTickLabelPaint(Color.white);
        plot.setRangeAxis(axis);
        axis.setLabel("TIME(MS)");
        int addition;
        switch (index){
            case 1:{
                addition = this.PSschedule.getTime() / 5;
                addition = (addition+1) * 5;
                break;
            }
            case 2:{
                addition = this.SJFschedule.getTime() / 5;
                addition = (addition+1) * 5;
                break;
            }
            case 3:{
                addition = this.SRTFschedule.getTime() / 5;
                addition = (addition+1) * 5;
                break;
            }
            case 4:{
                addition = this.AGATschedule.getTime() / 5;
                addition = (addition+1) * 5;
                break;
            }
            default:{
                addition = 50;
                break;
            }
        }
        axis.setRange(0, addition);
        return chart;
    }



    public static void run(int CS){
        contextSwitch = CS;
        tabbedPane.setSelectedIndex(1);
    }

    public static void main(final String[] args) {

        final CpuScheduler demo = new CpuScheduler("CPU PROCESS SCHEDULING");

    }
}
