package com.zcy.marketquery.controller;

import com.zcy.marketquery.dao.RequestItem;
import com.zcy.marketquery.dao.Response;
import com.zcy.marketquery.service.QueryService;
import com.zcy.marketquery.service.TickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
public class QueryController {

    @Autowired
    QueryService queryService;

    @Autowired
    TickService tickService;

    List<Long> durationList;

    List<Long> timestampList;


    @PostConstruct
    public void init() {
        durationList = new ArrayList<>();
        timestampList = new ArrayList<>();
    }


//    @PostMapping("/query")
//    public String query(@RequestBody RequestItem requestItem) throws Exception {
//        long start = System.nanoTime();
//        String result = queryService.generateResponse(requestItem);
//        long end = System.nanoTime();
//        durationList.add(end - start);
//        timestampList.add(start);
//        timestampList.add(end);
//        return result;
//    }

    @PostMapping("/query")
    public Response query(@RequestBody RequestItem requestItem) throws Exception {
        return queryService.generateResponse(requestItem);
    }

    @GetMapping("/time")
    public String time() {
        String size = Integer.toString(durationList.size());
        if (size.equals("0")) return "空";
        BigInteger b = new BigInteger("0");
        for (long i : durationList) {
            b = b.add(new BigInteger(Long.toString(i)));
        }
        String result = b.divide(new BigInteger(size)).toString();
        String duration = Long.toString(timestampList.get(timestampList.size() - 1) - timestampList.get(0));
        return "平均时间：" + result + " 个数：" + size + " 总和：" + b + " 总耗时：" + duration;
    }

    @GetMapping("/clear")
    public void clear() {
        durationList.clear();
    }

}
