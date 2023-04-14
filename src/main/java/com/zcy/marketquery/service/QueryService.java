package com.zcy.marketquery.service;

import com.alibaba.fastjson.JSONObject;
import com.zcy.marketquery.dao.*;
import mmaputil.MmapUtil;
import mmaputil.read.rule.FieldSpan;
import mmaputil.read.rule.ReadRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class QueryService {
    /**
     * 文件路径
     */
    @Value("${config.dir}")
    private String directory;
    /**
     * 缓冲区大小
     */
    @Value("${config.bufferSize}")
    private long bufferSize;
    /**
     * 文件格式
     */
    @Value("${config.fileType}")
    private String fileType;
    /**
     * 内存映射工具类
     */
    MmapUtil<Tick> mmap;
    /**
     * 日期格式转化工具类
     */
    SimpleDateFormat dateFormat;

    @PostConstruct
    public void init() {
        mmap = new MmapUtil<>(directory, bufferSize, fileType);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }

    /**
     * 生成返回的结果
     *
     * @param requestItem 客户端的请求
     * @return 读取结果
     * @throws Exception 读取失败
     */
    public Response generateResponse(RequestItem requestItem) throws Exception {
        Response response = new Response();
        response.setRequest_date_time(dateFormat.format(System.currentTimeMillis()));
        response.setCode(requestItem.getCode());

        List<Tick> list = queryByTick(requestItem);

        if (requestItem.getStats_type() == 1) {  // By tick
            response.setTrade_items(ticksToTradeItems(list));
        } else {    // By price
            response.setTrade_items(priceSort(list));
        }
        return response;
    }

    /**
     * 内存映射读取符合条件的对象
     *
     * @param requestItem 客户端的请求
     * @return Tick 数据列表
     * @throws Exception 读取失败
     */
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

    /**
     * 分价列表构建
     *
     * @param ticks 所有读取到的 Tick 数据
     * @return 分价后的列表
     */
    public List<TradeItem> priceSort(List<Tick> ticks) {
        Map<Integer, Integer> map = new TreeMap<>();
        List<TradeItem> result = new LinkedList<>();
        for (int i = 0; i < ticks.size(); i++) {
            int price = ticks.get(i).getPrice();
            map.put(price, map.getOrDefault(price, 0) + 1);
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            TradeItem item = new TradeItem();
            item.setPrice(entry.getKey());
            item.setVolume(entry.getValue());
            item.setBid_ask_flag(0);
            result.add(item);
        }
        return result;
    }

    /**
     * 将 Tick 转化成分笔查询返回结果的样式
     *
     * @param ticks 所有读取到的符合要求的 Tick 信息
     * @return 分笔后的列表
     */
    private List<TradeItem> ticksToTradeItems(List<Tick> ticks) {
        List<TradeItem> list = new LinkedList<>();
        for (int i = 0; i < ticks.size(); i++) {
            TradeItem item = new TradeItem();
            item.setPrice(ticks.get(i).getPrice());
            item.setVolume(1);
            item.setBid_ask_flag(0);
            list.add(item);
        }
        return list;
    }


}
