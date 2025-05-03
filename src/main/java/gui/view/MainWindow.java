package gui.view;

import config.Constants;
import config.Helper;
import entity.order.Order;
import entity.product.Product;
import entity.worker.Worker;
import gui.model.SimulationData;
import gui.view.talbemodel.ProductTable;
import gui.view.talbemodel.WorkerTable;
import gui.view.talbemodel.WorkstationTable;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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
    private JFreeChart chart1;
    private Chart chart;

    private WorkerTable workerTableARep, workerTableBRep, workerTableCRep;
    private ProductTable productTable;
    private WorkstationTable workstationTable;

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


        productTable = new ProductTable();
        workstationTable = new WorkstationTable();

        tableARep.setModel(workerTableARep);
        tableBRep.setModel(workerTableBRep);
        tableCRep.setModel(workerTableCRep);

        tableOrder.setModel(productTable);
        tableWorkstation.setModel(workstationTable);
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
        updateAverageTimeInSystemTotal(simData);
        updateAverageCountOfNotWorkedOnOrder(simData);
        if (showStatsCheckBox.isSelected()) updateList(simData);

        labelReplication.setText(simData.currentReplication() + "");
        currentRepLabel.setText(String.format("Replication: %d", simData.currentReplication()));
    }

    private void updateList(SimulationData simData) {
//        DefaultListModel<String> tempModel = new DefaultListModel<>();
//        for (Statistic stat : simData.orderTimeInSystem()) tempModel.addElement(stat.toString());
//        if (simData.orderTimeInSystem()[1] != null) {
//            double[] is = simData.orderTimeInSystem()[1].getConfidenceInterval();
//            tempModel.addElement(String.format("%s: %.4f h <%.4f | %.4f>\n",
//                    simData.orderTimeInSystem()[1].getName(),
//                    simData.orderTimeInSystem()[1].getMean() / 60 / 60,
//                    is[0] / 60 / 60, is[1] / 60 / 60));
//        }
//
//        for (Statistic stat : simData.queueLengthReplication()) tempModel.addElement(stat.toString());
//        for (Statistic stat : simData.queueLengthTotal()) tempModel.addElement(stat.toString());
//        if (simData.workstationSizeTotal() != null) tempModel.addElement(simData.workstationSizeTotal().toString());
//
//        if (simData.workerWorkloadTotal() != null) {
//            for (DiscreteStatistic[] stats : simData.workerWorkloadTotal())
//                for (Statistic stat : stats) tempModel.addElement(stat.toString());
//
//            for (Statistic stat : simData.workloadForGroupTotal()) tempModel.addElement(stat.toString());
//            tempModel.addElement(simData.orderNotWorkedOnTotal().toString() + "\n");
//        }
//
//        this.list1.setModel(tempModel);
    }

    private void updateAverageQueueLengthReplication(SimulationData simData) {
//        if (simData.queueLengthReplication() != null) {
//            this.labelQueueLengthReplication.setText("<html>" + String.format("A: %.2f, B: %.2f, C: %.2f" + "</html>",
//                    simData.queueLengthReplication()[0].getMean(),
//                    simData.queueLengthReplication()[1].getMean(),
//                    simData.queueLengthReplication()[2].getMean()));
//        }
    }

    private void updateAverageTimeReplication(SimulationData simData) {
//        if (simData.orderTimeInSystem() != null && simData.orderTimeInSystem()[0] != null) {
//            this.labelAverageTimeReplication.setText("<html>" + String.format("%.2fh (%.2fs)" + "</html>",
//                    (simData.orderTimeInSystem()[0].getMean() / 60 / 60),
//                    (simData.orderTimeInSystem()[0].getMean())));
//        }
    }

    public void updateChart(SimulationData simData, int replicationCount) {
//        SwingUtilities.invokeLater(() -> {
//            if (simData.updateChart() && simData.currentReplication() >= (replicationCount * Constants.PERCENTAGE_CUT_DATA) &&
//                    simData.currentReplication() % Math.ceil((replicationCount * Constants.PERCENTAGE_UPDATE_DATA)) == 0) {
//                XYSeriesCollection dataset = (XYSeriesCollection) chart1.getXYPlot().getDataset();
//
//                XYSeries seriesMain = dataset.getSeries(0);
//
//
//                int rep = simData.currentReplication();
//                DiscreteStatistic ds = simData.orderTimeInSystem()[1];
//                double[] is = ds.getConfidenceInterval();
//
//                seriesMain.add(rep, ds.getMean());
//
//                if (simData.currentReplication() > 30) {
//                    XYSeries seriesBottom = dataset.getSeries(1);
//                    XYSeries seriesTop = dataset.getSeries(2);
//                    seriesBottom.add(rep, is[0]);
//                    seriesTop.add(rep, is[1]);
//                }
//
//                chart.updateRange(Constants.OFFSET_FACTOR);
//                chart1.fireChartChanged();
//                progressBar1.setMaximum(replicationCount);
//                progressBar1.setValue(simData.currentReplication());
//            }
//        });
    }

    private void updateAverageCountOfNotWorkedOnOrder(SimulationData simData) {
//        if (simData.orderNotWorkedOnTotal() != null) {
//            double[] is = simData.orderNotWorkedOnTotal().getConfidenceInterval();
//            labelOrderNotWorkedOn.setText(String.format("<html>%.4f<br>[%.4f | %.4f]</html>",
//                    simData.orderNotWorkedOnTotal().getMean(),
//                    is[0], is[1]));
//        }
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
        JLabel[] labels = new JLabel[]{labelA, labelB, labelC, labelFitting};

        for (int i = 0; i < labels.length; i++) {
//            double[] isW = simData.workloadForGroupTotal() != null ? simData.workloadForGroupTotal()[i].getConfidenceInterval() : new double[]{0.0, 0.0};
//            double[] isQ = simData.workloadForGroupTotal() != null ? simData.queueLengthTotal()[i].getConfidenceInterval() : new double[]{0.0, 0.0};
//            labels[i].setText(String.format("<html>Group %c<br>(%.2f%% | %.2f%% [%.2f%% | %.2f%%])<br>%d | %.3f [%.3f | %.3f]</html>",
//                    (i + 'A'),
//                    getSpeed() < Constants.MAX_SPEED ? calculateWorkloadForGroupReplication(simData, i) * 100 : 0.0,
//                    simData.workloadForGroupTotal() != null ? simData.workloadForGroupTotal()[i].getMean() * 100 : 0.0,
//                    isW[0] * 100, isW[1] * 100,
//                    simData.queues() != null && getSpeed() < Constants.MAX_SPEED ? simData.queues()[i] : 0,
//                    simData.queueLengthTotal() != null ? simData.queueLengthTotal()[i].getMean() : 0.0,
//                    isQ[0], isQ[1]));

            labels[i].setText(String.format("<html>Group %c<br>(%d)</html>",
                    (i + 'A'),
                    simData.queues() != null && getSpeed() < Constants.MAX_SPEED ? simData.queues()[i] : 0));
        }
    }

    private double calculateWorkloadForGroupReplication(SimulationData simData, int i) {
//        return Arrays.stream(simData.workers()[i])
//                .mapToDouble(w -> w.getStatisticWorkload().getMean())
//                .average().getAsDouble();
        return 0.0;
    }

    public void updateWorkersTotal(SimulationData simData) {
//        if (simData.workerWorkloadTotal() != null) {
//            DiscreteStatistic[][] stats = simData.workerWorkloadTotal();
//            for (int i = 0; i < stats.length; i++) {
//                switch (i) {
//                    case 0 -> workerTableATotal.addRows(List.of(stats[i]));
//                    case 1 -> workerTableBTotal.addRows(List.of(stats[i]));
//                    case 2 -> workerTableCTotal.addRows(List.of(stats[i]));
//                }
//            }
//        }
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
