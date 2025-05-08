package simulation;

import java.awt.geom.Point2D;
import java.util.Random;

public class Data {
    // Queue
    public static final Point2D QUEUE_START = new Point2D.Double(150, 860);
    public static final Point2D QUEUE_END = new Point2D.Double(150, 200);

    // Storage
    public static final Point2D STORAGE_POSITION = new Point2D.Double(200, 200); // Top-left corner of the storage area
    public static final int STORAGE_WIDTH = 500;
    public static final int STORAGE_HEIGHT = 400;

    // Text
    public static final Point2D TEXT_SIMULATIONTIME_POSITION = new Point2D.Double(100, 150);
    public static final Point2D TEXT_QUEUE_POSITION = new Point2D.Double(100, 100);

    // Workstation
    public static final int WORKSTATION_WIDTH = 150;
    public static final int WORKSTATION_HEIGHT = 120;

    public static final int WORKSTATION_HORIZONTAL_MARGIN_FROM_STORAGE = 70;
    public static final int WORKSTATION_VERTICAL_GAP_BETWEEN_ROWS = 50;
    public static final int MAX_WORKSTATIONS_PER_ROW = 10;
    public static final int WORKSTATION_X_OFFSET = 20;

    public static final int WORKSTATION_X_START = (int) (STORAGE_POSITION.getX() + STORAGE_WIDTH + WORKSTATION_HORIZONTAL_MARGIN_FROM_STORAGE);

    public static final int WORKSTATION_Y_START = (int) (STORAGE_POSITION.getY());

    public static final int WORKSTATION_X_ROW_END_BOUNDARY = WORKSTATION_X_START + ((WORKSTATION_WIDTH + WORKSTATION_X_OFFSET) * MAX_WORKSTATIONS_PER_ROW);

    public static final int WORKSTATION_Y_OFFSET_PER_ROW = WORKSTATION_HEIGHT + WORKSTATION_VERTICAL_GAP_BETWEEN_ROWS;

    public static final int WORKSTATION_PRODUCT_OFFSET_X = 30;
    public static final int WORKSTATION_PRODUCT_OFFSET_Y = 20;

    public static final int WORKER_WORKSTATION_OFFSET_Y = 50;
    public static final int WORKER_WORKSTATION_OFFSET_X = 55;

    public static final int WORKER_WIDTH = 50;
    public static final int WORKER_HEIGHT = 65;


    public static Point2D getRandomStoragePoiunt() {
        Random r = new Random();
        int xMin = (int) (Data.STORAGE_POSITION.getX());
        int xMax = (int) (Data.STORAGE_POSITION.getX() + Data.STORAGE_WIDTH - WORKSTATION_WIDTH);
        int yMin = (int) (Data.STORAGE_POSITION.getY() + Data.STORAGE_HEIGHT - 200);
        int yMax = (int) (Data.STORAGE_POSITION.getY() + Data.STORAGE_HEIGHT - WORKSTATION_HEIGHT);

        double x = (xMin >= xMax) ? xMin : r.nextInt(xMin, xMax);
        double y = (yMin >= yMax) ? yMin : r.nextInt(yMin, yMax);
        return new Point2D.Double(x, y);
    }

    // Workers
    public static final String WORKER_A = "models/worker_cutting.png";
    public static final String WORKER_B = "models/worker_assembly.png";
    public static final String WORKER_C = "models/worker_painter.png";

    // Products
    public static final String CHAIR = "models/chair.png";
    public static final String TABLE = "models/table.png";
    public static final String CUPBOARD = "models/cupboard.png";

    // Structures
    public static final String WORKSTATION = "models/workstation.png";
    public static final String STORAGE = "models/storage.jpg";
}