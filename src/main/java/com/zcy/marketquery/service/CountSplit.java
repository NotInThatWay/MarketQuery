package com.zcy.marketquery.service;

import mmaputil.write.rule.SplitRule;

import java.util.HashMap;
import java.util.Map;

public class CountSplit extends SplitRule {
    Map<Integer, Integer> map;
    int count;
    int interval;

    public CountSplit(int interval){
        count = 0;
        map = new HashMap<>();
        this.interval = interval;
    }

    @Override
    public void split(Object o) {
        int digit = count++ / interval;
        name = Integer.toString(digit);
    }
}
