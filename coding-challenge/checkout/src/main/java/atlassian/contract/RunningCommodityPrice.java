package atlassian.contract;

public interface RunningCommodityPrice {
    void upsertCommodityPrice(int timestamp, int commodityPrice);
    int getMaxCommodityPrice();
    int getMostRecentCommodityPrice();

    int getCurrentPrice(int timestamp);
}
