package com.zcy.marketquery.dao;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class TradeItem {
    int price;
    int volume;
    int bid_ask_flag;
    JSONObject trade_detail;
    int index;
}
