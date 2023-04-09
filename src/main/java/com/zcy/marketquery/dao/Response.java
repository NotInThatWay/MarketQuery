package com.zcy.marketquery.dao;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class Response {
    String date_time;
    List<ResponseItem> items;
}
