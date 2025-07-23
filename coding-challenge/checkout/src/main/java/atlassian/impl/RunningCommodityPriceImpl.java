package atlassian.impl;

import atlassian.contract.RunningCommodityPrice;

import java.util.Comparator;
import java.util.TreeMap;

public class RunningCommodityPriceImpl implements RunningCommodityPrice {

    private final TreeMap<Integer, Integer> commodityLookup = new TreeMap<>();
    @Override
    public void upsertCommodityPrice(int timestamp, int commodityPrice) {
        commodityLookup.put(timestamp, commodityPrice);
    }

    @Override
    public int getMaxCommodityPrice() { //O(nlogn)
        return commodityLookup.values().stream().max(Comparator.naturalOrder()).orElse(0);
    }

    @Override
    public int getMostRecentCommodityPrice() { //o(1)
        return commodityLookup.lastEntry().getValue();
    }

    @Override
    public int getCurrentPrice(int timestamp) { //O(log(n))
        return commodityLookup.get(timestamp);
    }
}
