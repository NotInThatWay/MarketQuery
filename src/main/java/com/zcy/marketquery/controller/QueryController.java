package com.zcy.marketquery.controller;

import com.zcy.marketquery.dao.RequestItem;
import com.zcy.marketquery.dao.Tick;
import com.zcy.marketquery.service.QueryService;
import com.zcy.marketquery.service.TickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryController {

    @Autowired
    QueryService queryService;

    @Autowired
    TickService tickService;

    @PostMapping("/query")
    public String query(@RequestBody RequestItem requestItem) throws Exception {
        System.out.println(queryService.generateResponse(requestItem));
        return queryService.generateResponse(requestItem);
    }
}
