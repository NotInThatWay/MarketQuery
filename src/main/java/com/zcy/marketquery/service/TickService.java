package com.zcy.marketquery.service;

import com.zcy.marketquery.dao.Tick;
import lombok.extern.slf4j.Slf4j;
import mmaputil.MmapUtil;
import mmaputil.write.rule.FieldSplit;
import mmaputil.write.rule.SplitRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@Slf4j
public class TickService {
    public static BlockingQueue<Tick> tickQ = new LinkedBlockingQueue<>();  // 产生的 Tick 存放于此，等待被写入文件或内存
    @Value("${config.generationCount}")
    private int generationCount;    // 每支股票生成的总 Tick 数
    @Value("${config.frequency}")
    private int frequency;  // 生成 Tick 的频率
    @Value("${config.numStock}")
    private int numStock;   // 总共股票的数量
    @Value("${config.numThread}")
    private int numThread;  // 线程数量
    @Value("${config.bufferSize}")
    private int bufferSize;
    @Value("${config.dir}")
    private String directory;
    @Value("${config.fileType}")
    private String fileType;

    MmapUtil<Tick> mmap;

    SplitRule rule;

    /**
     * 定期生成 Tick 数据
     */
    private void generateTick() {
        for (int g = 0; g < generationCount; g++) {
            long time = System.currentTimeMillis();
            for (int i = 0; i < numStock; i++) {
                Tick tick = new Tick();
                String code = "SH" + String.format("%06d", i);
                tick.setCode(code);
                tick.setTime(time);
                tick.setId(code + time);
                tickQ.offer(tick);
            }
            try {
                Thread.sleep(frequency);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 生成 Tick 线程
     */
    @PostConstruct
    private void generateTickThread() {
        log.info("开始生成Tick");
        ThreadPoolService.singleThreadExecutor.execute(this::generateTick);
    }

    /**
     * 初始化
     */
    @PostConstruct
    private void init() {
        rule = new FieldSplit("code");
//        rule = new CountSplit(2000);
        mmap = new MmapUtil<>(directory, bufferSize, fileType);
        receiveToFileThread();
    }

    /**
     * 将 Tick 写入磁盘
     */
    private void receiveToFile() throws Exception {
        while (true) {
            Tick tick = tickQ.take();
            mmap.writeToFile(tick, rule);
        }
    }

    /**
     * 将 Tick 数据写入磁盘线程
     */
    private void receiveToFileThread() {
        for (int i = 0; i < numThread; i++) {
            ThreadPoolService.receiveToFileExecutor.execute(() -> {
                try {
                    receiveToFile();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
