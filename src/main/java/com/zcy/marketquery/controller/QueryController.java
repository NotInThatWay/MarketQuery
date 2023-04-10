package com.zcy.marketquery.controller;

import com.zcy.marketquery.dao.RequestItem;
import com.zcy.marketquery.dao.Tick;
import com.zcy.marketquery.service.QueryService;
import com.zcy.marketquery.service.TickService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
public class QueryController {

    @Autowired
    QueryService queryService;

    @Autowired
    TickService tickService;

    List<Long> list;

    int count;

    @PostConstruct
    public void init() {
        list = new ArrayList<>();
        count = 0;
    }

    @PostMapping("/query")
    public String query(@RequestBody RequestItem requestItem) throws Exception {
        long start = System.nanoTime();
        String result = queryService.generateResponse(requestItem);
        long end = System.nanoTime();
        list.add(end - start);
        count++;
        return result;
    }

    @GetMapping("/time")
    public String time() {
        String size = Integer.toString(list.size());
        if (size.equals("0")) return "空";
        BigInteger b = new BigInteger("0");
        for (long i : list) {
            b = b.add(new BigInteger(Long.toString(i)));
        }
        String result = b.divide(new BigInteger(size)).toString();
        return "平均时间：" + result + " 个数：" + size + " 总时间：" + b;
    }

    @GetMapping("/clear")
    public void clear() {
        list.clear();
    }

    @GetMapping("/count")
    public int count() {
        return count;
    }

}
