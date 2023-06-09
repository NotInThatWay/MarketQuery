package com.zcy.marketquery.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class Response {
    String code;
    String request_date_time;
    JSONObject express;
    List<TradeItem> trade_items;
}
