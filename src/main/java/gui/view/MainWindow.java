package gui.view;

import OSPStat.Stat;
import config.Constants;
import config.Group;
import config.Helper;
import entity.order.Order;
import entity.product.Product;
import entity.worker.Worker;
import entity.workstation.Workstation;
import gui.model.SimulationData;
import gui.view.talbemodel.*;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainWindow extends JFrame {
    private JPanel panel1;
    private JTextField fieldReplicationCount;
    private JTextField fieldWorkerA;
    private JTextField fieldWorkerB;
    private JTextField fieldWorkerC;
    private JButton startButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JTable tableARep;
    private JTable tableBRep;
    private JTable tableCRep;
    private JLabel labelTime;
    private JLabel labelReplication;
    private JTable tableOrder;
    private JTable tableWorkstation;
    private JSlider sliderSpeed;
    private JLabel labelSpeed;
    private JLabel labelA;
    private JLabel labelB;
    private JLabel labelC;
    private JLabel labelAverageTimeInSystem;
    private JLabel labelAverageTimeInSystemTotal;
    private JTabbedPane tabbedPanel1;
    private JTable tableATotal;
    private JTabbedPane tabbedPanel2;
    private JTabbedPane tabbedPanel3;
    private JTable tableBTotal;
    private JTable tableCTotal;
    private JLabel labelOrderNotWorkedOn;
    private JTabbedPane mainTabbedPanel;
    private ChartPanel chartPanel1;
    private JLabel currentRepLabel;
    private JProgressBar progressBar1;
    private JLabel labelAverageTimeReplication;
    private JLabel labelQueueLengthReplication;
    private JLabel labelGraph;
    private JList list1;
    private JCheckBox showStatsCheckBox;
    private JTextField fieldWorkstation;
    private JLabel labelFitting;
    private JLabel labelAverageProductTotal;
    private JTable tableWorkstationTotal;
    private JLabel workstationLabel;
    private JLabel labelAverageProductReplication;
    private JFreeChart chart1;
    private Chart chart;

    private WorkerTable workerTableARep, workerTableBRep, workerTableCRep;
    private WorkerTotalTable workerTableATotal, workerTableBTotal, workerTableCTotal;
    private ProductTable productTable;
    private WorkstationTable workstationTable;
    private WorkstationTotalTable workstationTotalTable;

    public MainWindow() {
        setTitle("Diskretna simulacia");
        setMinimumSize(new Dimension(1800, 950));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.add(mainTabbedPanel);

        this.sliderSpeed.setMaximum(Constants.MAX_SPEED);
        this.sliderSpeed.setValue(Constants.DEFAULT_SPEED);

        Icon home = new ImageIcon("home.png");
        Icon graph = new ImageIcon("diagram.png");
        mainTabbedPanel.setIconAt(0, home);
        mainTabbedPanel.setIconAt(1, graph);

        this.initTables();
    }

    public void createUIComponents() {
        chart = new Chart("Average time", "Average time in system");
        chart1 = chart.getChart();

        chartPanel1 = new ChartPanel(chart1);
    }

    private void initTables() {
        workerTableARep = new WorkerTable();
        workerTableBRep = new WorkerTable();
        workerTableCRep = new WorkerTable();

        workerTableATotal = new WorkerTotalTable();
        workerTableBTotal = new WorkerTotalTable();
        workerTableCTotal = new WorkerTotalTable();

        productTable = new ProductTable();
        workstationTable = new WorkstationTable();
        workstationTotalTable = new WorkstationTotalTable();

        tableARep.setModel(workerTableARep);
        tableBRep.setModel(workerTableBRep);
        tableCRep.setModel(workerTableCRep);

        tableATotal.setModel(workerTableATotal);
        tableBTotal.setModel(workerTableBTotal);
        tableCTotal.setModel(workerTableCTotal);

        tableOrder.setModel(productTable);
        tableWorkstation.setModel(workstationTable);
        tableWorkstationTotal.setModel(workstationTotalTable);
    }

    public void updateData(SimulationData simData) {
        if (getSpeed() < Constants.MAX_SPEED) {
            updateTime(simData);
            updateWorkersReplication(simData);
            updateWorkstationOrderTable(simData);
            updateAverageTimeReplication(simData);
            updateAverageQueueLengthReplication(simData);
        }
        updateQueueSize(simData);
        updateWorkersTotal(simData);
        updateWorkstationTotal(simData);
        updateAverageTimeInSystemTotal(simData);
        updateAverageCountOfNotWorkedOnOrder(simData);
        if (showStatsCheckBox.isSelected()) updateList(simData);

        labelReplication.setText(simData.currentReplication() + "");
        currentRepLabel.setText(String.format("Replication: %d", simData.currentReplication()));
    }

    private void updateWorkstationTotal(SimulationData simData) {
        workstationTotalTable.addRows(simData.workstations());
        double[] is = simData.statWorkstationWorkloadTotal().sampleSize() > 2 ? simData.statWorkstationWorkloadTotal().confidenceInterval_95() : new double[]{0,0};
        workstationLabel.setText(String.format("<html>Workstations <br>%.2f%%<br>%.2f%% [%.2f%% | %.2f%%]</html>",
                getSpeed() < Constants.MAX_SPEED ? calculateWorkloadForWorkstation(simData) * 100 : 0.0,
                simData.statWorkstationWorkloadTotal().mean() * 100,
                is[0] * 100, is[1] * 100
                ));
    }

    private Double calculateWorkloadForWorkstation(SimulationData simData) {
        ArrayList<Workstation> workstations = simData.workstations();
        double sum = 0.0;
        for (Workstation workstation : workstations) {
            sum += workstation.getStatWorkload().mean();
        }
        return sum / workstations.size();
    }

    private void updateList(SimulationData simData) {
        DefaultListModel<String> tempModel = new DefaultListModel<>();
        // ORDERS
        String[] orderNames = new String[]{"Order time replication", "Order time total"};
        for (int i = 0; i < simData.statOrder().length; i++) {
            Stat stat = simData.statOrder()[i];
            String text = String.format("%s: %s", orderNames[i], stat.toString());
            tempModel.addElement(text);
        }

        // PRODUCT
        String[] productNames = new String[]{"Product time replication", "Product time total"};
        for (int i = 0; i < simData.statProduct().length; i++) {
            Stat stat = simData.statProduct()[i];
            String text = String.format("%s: %s", productNames[i], stat.toString());
            tempModel.addElement(text);
        }

        // QUEUE
        String[] queueNames = new String[]{"Queue A", "Queue B", "Queue C", "Fitting"};
        for (int i = 0; i < simData.groups().length; i++) {
            Stat stat = simData.groups()[i].getStatQueueLength();
            Stat statTotal = simData.groups()[i].getStatQueueLengthTotal();
            String text = String.format("%s: %s", queueNames[i] + " replication", stat.toString());
            tempModel.addElement(text);
            text = String.format("%s: %s", queueNames[i] + " total", statTotal.toString());
            tempModel.addElement(text);
        }

        // WORKERS
        if (simData.workers() != null) {
            Worker[][] workers = simData.workers();
            for (int i = 0; i < workers.length; i++) {
                for (int j = 0; j < workers[i].length; j++) {
                    Stat stat = workers[i][j].getStatWorkload();
                    Stat statTotal = workers[i][j].getStatWorkloadTotal();
                    String text = String.format("%s: %s", workers[i][j] +  " replication", stat.toString());
                    tempModel.addElement(text);
                    text = String.format("%s: %s", workers[i][j] + " total", statTotal.toString());
                    tempModel.addElement(text);
                }
            }
        }

        this.list1.setModel(tempModel);
    }

    private void updateAverageQueueLengthReplication(SimulationData simData) {
        if (simData.groups() != null) {
            this.labelQueueLengthReplication.setText("<html>" + String.format("A: %.2f, B: %.2f, C: %.2f" + "</html>",
                    simData.groups()[0].getStatQueueLength().mean(),
                    simData.groups()[1].getStatQueueLength().mean(),
                    simData.groups()[2].getStatQueueLength().mean()));
        }
    }

    private void updateAverageTimeReplication(SimulationData simData) {
        if (simData.statOrder() != null && simData.statOrder()[0] != null) {
            this.labelAverageTimeReplication.setText("<html>" + String.format("%.2fh (%.2fs)" + "</html>",
                    (simData.statOrder()[0].mean() / 60 / 60),
                    (simData.statOrder()[0].mean())));
        }

        if (simData.statProduct() != null && simData.statProduct()[0] != null) {
            this.labelAverageProductReplication.setText("<html>" + String.format("%.2fh (%.2fs)" + "</html>",
                    (simData.statProduct()[0].mean() / 60 / 60),
                    (simData.statProduct()[0].mean())));
        }
    }

    public void updateChart(SimulationData simData, int replicationCount) {
        SwingUtilities.invokeLater(() -> {
            if (simData.updateChart() && simData.currentReplication() >= (replicationCount * Constants.PERCENTAGE_CUT_DATA) &&
                    simData.currentReplication() % Math.ceil((replicationCount * Constants.PERCENTAGE_UPDATE_DATA)) == 0) {
                XYSeriesCollection dataset = (XYSeriesCollection) chart1.getXYPlot().getDataset();

                XYSeries seriesMain = dataset.getSeries(0);


                int rep = simData.currentReplication();
                Stat ds = simData.statOrder()[1];
                double[] is = ds.confidenceInterval_95();

                seriesMain.add(rep, ds.mean());

                if (simData.currentReplication() > 30) {
                    XYSeries seriesBottom = dataset.getSeries(1);
                    XYSeries seriesTop = dataset.getSeries(2);
                    seriesBottom.add(rep, is[0]);
                    seriesTop.add(rep, is[1]);
                }

                chart.updateRange(Constants.OFFSET_FACTOR);
                chart1.fireChartChanged();
                progressBar1.setMaximum(replicationCount);
                progressBar1.setValue(simData.currentReplication());
            }
        });
    }

    private void updateAverageCountOfNotWorkedOnOrder(SimulationData simData) {
        if (simData.statOrderNotWorkerOnTotal() != null) {
            double[] is = simData.statOrderNotWorkerOnTotal().sampleSize() > 2 ? simData.statOrderNotWorkerOnTotal().confidenceInterval_95() : new double[] {0,0};
            labelOrderNotWorkedOn.setText(String.format("<html>%.4f<br>[%.4f | %.4f]</html>",
                    simData.statOrderNotWorkerOnTotal().mean(),
                    is[0], is[1]));
        }
    }

    private void updateAverageTimeInSystemTotal(SimulationData simData) {
        if (simData.statOrder() != null && simData.statOrder()[1] != null) {
            double[] is = simData.statOrder()[1].sampleSize() > 2 ? simData.statOrder()[1].confidenceInterval_95() : new double[]{0.0,0.0};
            this.labelAverageTimeInSystemTotal.setText("<html>" + String.format("%.2fh (%.2fs)<br>[%.2f | %.2f]" + "</html>",
                    (simData.statOrder()[1].mean() / 60 / 60),
                    (simData.statOrder()[1].mean()),
                    is[0],
                    is[1]));
            this.labelGraph.setText(String.format("%.2fh (%.2fs) [%.2f | %.2f]",
                    simData.statOrder()[1].mean() / 60 / 60,
                    simData.statOrder()[1].mean(),
                    is[0],
                    is[1]));
        }

        if (simData.statProduct() != null && simData.statProduct()[1] != null) {
            double[] is = simData.statProduct()[1].sampleSize() > 2 ? simData.statProduct()[1].confidenceInterval_95() : new double[]{0.0,0.0};
            this.labelAverageProductTotal.setText("<html>" + String.format("%.2fh (%.2fs)<br>[%.2f | %.2f]" + "</html>",
                    (simData.statProduct()[1].mean() / 60 / 60),
                    (simData.statProduct()[1].mean()),
                    is[0],
                    is[1]));
            this.labelGraph.setText(String.format("%.2fh (%.2fs) [%.2f | %.2f]",
                    simData.statProduct()[1].mean() / 60 / 60,
                    simData.statProduct()[1].mean(),
                    is[0],
                    is[1]));
        }
    }

    private void updateQueueSize(SimulationData simData) {
        JLabel[] labels = new JLabel[]{labelA, labelB, labelC};

        Group[] groups = simData.groups();
        for (int i = 0; i < labels.length; i++) {
            double[] isW = groups[i].getWorkloadGroupTotal().sampleSize() > 2 ? groups[i].getWorkloadGroupTotal().confidenceInterval_95() : new double[]{0.0, 0.0};
            double[] isQ = groups[i].getStatQueueLengthTotal().sampleSize() > 2 ? groups[i].getStatQueueLengthTotal().confidenceInterval_95() : new double[]{0.0, 0.0};
            labels[i].setText(String.format("<html>Group %c<br>%.2f%% | %.2f%% [%.2f%% | %.2f%%]<br>%d | %.3f [%.3f | %.3f]</html>",
                    (i + 'A'),
                    getSpeed() < Constants.MAX_SPEED ? calculateWorkloadForGroupReplication(simData, i) * 100 : 0.0,
                    groups[i].getWorkloadGroupTotal() != null ? groups[i].getWorkloadGroupTotal().mean() * 100 : 0.0,
                    isW[0] * 100, isW[1] * 100,
                    groups[i] != null && getSpeed() < Constants.MAX_SPEED ? groups[i].queueSize() : 0,
                    groups[i] != null ? groups[i].getStatQueueLengthTotal().mean() : 0.0,
                    isQ[0], isQ[1]));
        }

        labelFitting.setText(String.format("Fitting: %d", groups[3].queueSize()));
    }

    private double calculateWorkloadForGroupReplication(SimulationData simData, int i) {
        return Arrays.stream(simData.workers()[i])
                .mapToDouble(w -> w.getStatWorkload().mean())
                .average().getAsDouble();
    }

    public void updateWorkersTotal(SimulationData simData) {
        if (simData.workers() != null) {
            Worker[][] workers = simData.workers();
            for (int i = 0; i < workers.length; i++) {
                switch (i) {
                    case 0 -> workerTableATotal.addRows(List.of(workers[i]));
                    case 1 -> workerTableBTotal.addRows(List.of(workers[i]));
                    case 2 -> workerTableCTotal.addRows(List.of(workers[i]));
                }
            }
        }
    }

    public void updateWorkersReplication(SimulationData simData) {
        if (simData.workers() == null && getSpeed() >= Constants.MAX_SPEED)
            return;

        Worker[][] workers = simData.workers();
        for (int i = 0; i < workers.length; i++) {
            switch (i) {
                case 0 -> workerTableARep.addRows(List.of(workers[i]));
                case 1 -> workerTableBRep.addRows(List.of(workers[i]));
                case 2 -> workerTableCRep.addRows(List.of(workers[i]));
            }
        }
    }

    public void updateWorkstationOrderTable(SimulationData simData) {
        if (simData.workstations() != null)
            workstationTable.addRows(simData.workstations());
        if (simData.orders() != null) {
            List<Product> products = new ArrayList<>();
            for (Order order : simData.orders()) {
                products.addAll(order.getProducts());
            }
            productTable.addRows(products);
        }
    }

    public void updateTime(SimulationData simData) {
        this.labelTime.setText(Helper.timeToDateString(simData.time(), 6));
    }

    public int getSpeed() {
        return getSliderSpeed().getValue();
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public JTextField getFieldReplicationCount() {
        return fieldReplicationCount;
    }

    public JTextField getFieldWorkerA() {
        return fieldWorkerA;
    }

    public JTextField getFieldWorkerB() {
        return fieldWorkerB;
    }

    public JTextField getFieldWorkerC() {
        return fieldWorkerC;
    }

    public JTextField getFieldWorkstation() {
        return fieldWorkstation;
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getPauseButton() {
        return pauseButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public JTable getTable4() {
        return tableOrder;
    }

    public JTable getTable5() {
        return tableWorkstation;
    }

    public JLabel getLabelSpeed() {
        return labelSpeed;
    }

    public WorkerTable getWorkerTable1() {
        return workerTableARep;
    }

    public WorkerTable getWorkerTable2() {
        return workerTableBRep;
    }

    public WorkerTable getWorkerTable3() {
        return workerTableCRep;
    }

    public ProductTable getOrderTable() {
        return productTable;
    }

    public WorkstationTable getWorkstationTable() {
        return workstationTable;
    }

    public JTable getTable1() {
        return tableARep;
    }

    public JTable getTable2() {
        return tableBRep;
    }

    public JTable getTable3() {
        return tableCRep;
    }

    public JLabel getLabelTime() {
        return labelTime;
    }

    public JLabel getLabelReplication() {
        return labelReplication;
    }

    public JSlider getSliderSpeed() {
        return sliderSpeed;
    }

    public Chart getChart() {
        return chart;
    }

    public JFreeChart getChart1() {
        return chart1;
    }

    public ChartPanel getChartPanel1() {
        return chartPanel1;
    }

    public JLabel getCurrentRepLabel() {
        return currentRepLabel;
    }

    public JTabbedPane getTabbedPanel2() {
        return tabbedPanel2;
    }

    public JTabbedPane getTabbedPanel3() {
        return tabbedPanel3;
    }

    public JTabbedPane getTabbedPanel1() {
        return tabbedPanel1;
    }
}
