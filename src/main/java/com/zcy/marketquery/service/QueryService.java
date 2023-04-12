package com.zcy.marketquery.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zcy.marketquery.dao.RequestItem;
import com.zcy.marketquery.dao.Response;
import com.zcy.marketquery.dao.Tick;
import jakarta.annotation.PostConstruct;
import mmaputil.MmapUtil;
import mmaputil.read.rule.FieldSpan;
import mmaputil.read.rule.ReadRule;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class QueryService {

    @Value("${config.dir}")
    private String directory;

    @Value("${config.bufferSize}")
    private long bufferSize;

    @Value("${config.fileType}")
    private String fileType;

    @Autowired
    Response response;


    MmapUtil<Tick> mmap;
    SimpleDateFormat dateFormat;

    @PostConstruct
    public void init() {
        mmap = new MmapUtil<>(directory, bufferSize, fileType);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }


    public String generateResponse(RequestItem requestItem) throws Exception {
        List<Tick> list = queryByTick(requestItem);
        if (requestItem.getStats_type() == 1) {  // By tick
            return JSON.toJSONString(queryByTick(requestItem));
        } else {    // By price
            return JSON.toJSONString(priceSort(list));
        }
    }

    public List<Tick> queryByTick(RequestItem requestItem) throws Exception {
        JSONObject obj = requestItem.getLimit_by_coordinate();
        String code = requestItem.getCode();

        if (obj != null) {  // limit by coordinate
            int index = (int) obj.get("index");
            int count = (int) obj.get("count");

            ReadRule rule = new FieldSpan(code, index, count);
            return mmap.readFromFile(code, rule, Tick.class);

        } else {    // limit by time
            ReadRule rule = new FieldSpan(code);
            List<Tick> result = new LinkedList<>();
            List<Tick> list = mmap.readFromFile(code, rule, Tick.class);

            Date start = dateFormat.parse(obj.get("start").toString());
            Date end = dateFormat.parse(obj.get("end").toString());
            for (int i = 0; i < list.size(); i++) {
                Tick tick = list.get(i);
                Date curr = new Date(tick.getTime());
                if (start.after(curr)) continue;
                if (end.before(curr)) break;
                result.add(tick);
            }
            return result;
        }
    }

    public Map<Integer, Integer> priceSort(List<Tick> ticks) {
        Map<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < ticks.size(); i++) {
            int price = ticks.get(i).getPrice();
            map.put(price, map.getOrDefault(price, 0) + 1);
        }
        return map;
    }


}
