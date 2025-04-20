package entity;

public class Ids {
    private static int productId = 0;
    private static int orderId = 0;
    private static int workerId = 0;

    public static int getProductId() {
        return ++productId;
    }

    public static int getOrderId() {
        return ++orderId;
    }

    public static int getWorkerId() {
        return ++workerId;
    }
}
