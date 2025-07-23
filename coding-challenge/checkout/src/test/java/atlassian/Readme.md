Imagine you are given a stream of data points consisting of <timestamp, commodityPrice> you are supposed to return the maxCommodityPrice at any point in time.
The timestamps in the stream can be out of order, or there can be duplicate timestamps, we need to update the commodityPrice at that particular timestamp if an entry for the timestamp already exists.
Create an in-memory solution tailored to prioritize frequent reads and writes for the given problem statement.
interface RunningCommodityPrice {
void upsertCommodityPrice(int timestamp, int commodityPrice);
int getMaxCommodityPrice();
}
RunningCommodityPrice r = new RunningCommodityPriceImpl();
r.upsertCommodityPrice(4, 27);
r.upsertCommodityPrice(6, 26);
r.upsertCommodityPrice(9, 25);
r.getMaxCommodityPrice();         // output should be 27 which is at timestamp 4
r.upsertCommodityPrice(4, 28);    // timestamps can come out of order and there can be duplicates
// the commodity price at timestamp 4 got updated to 28, so the max commodity price is 28
r.getMaxCommodityPrice(); // output should be 28 from timestamp 4

