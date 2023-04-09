package com.zcy.marketquery.dao;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class RequestItem {
    String code;
    String date_time;
    JSONObject limit_by_coordinate;
    JSONObject limit_by_time;
    int stats_type;
}
