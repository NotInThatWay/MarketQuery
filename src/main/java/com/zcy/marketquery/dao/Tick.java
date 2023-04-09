package com.zcy.marketquery.dao;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Tick {
    // 股票代码
    String code;

    // 当前时间戳
    long time;

    // 昨日收盘价
    float last = 14.1f;

    // 开盘价
    float open = 14.1f;

    // 今日最高点
    float high = 14.1f;

    // 今日最低点
    float low = 14.1f;

    // 实时价格
    int price = 500;

    // 成交量
    long volT = 41000;

    // 成交金额
    float amtT = 578100.0f;

    // 成交量增量
    long vol = 41000;

    String id;

    long outsideVol = 0;
    long insideVol = 41000;
    float amt = 578100.0f;
    int num = 0;
    int numT = 0;
    float wb = -0.43f;
    String bs = "B";

    float buyAmt = 137370.3f;
    float sellAmt = 440729.7f;
    String phase = "T111";
    int sn1 = 2500;
    int sn2 = 13300;
    int sn3 = 13300;
    int sn4 = 15900;
    int sn5 = 72300;
    int bn1 = 29000;
    int bn2 = 3100;
    int bn3 = 2800;
    int bn4 = 2000;
    int bn5 = 9800;
    float sp1 = 14.11f;
    float sp2 = 14.12f;
    float sp3 = 14.13f;
    float sp4 = 14.14f;
    float sp5 = 14.15f;
    float bp1 = 14.1f;
    float bp2 = 14.09f;
    float bp3 = 14.08f;
    float bp4 = 14.07f;
    float bp5 = 14.06f;
}
