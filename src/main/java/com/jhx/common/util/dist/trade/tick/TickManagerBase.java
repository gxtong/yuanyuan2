package com.jhx.common.util.dist.trade.tick;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.jhx.common.util.*;
import com.jhx.common.util.db.SqlHelper;
import com.jhx.common.util.dist.trade.dto.TickType;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * author 钱智慧
 * date 2018/2/4 下午8:23
 */
public abstract class TickManagerBase implements IMyWebSocketMsgListener {
    protected static volatile boolean isTicking = true;
    private static String priceServer = AppPropUtil.get("tick.server");
    //生产者是行情接收，消费者是tick
    protected static BlockingQueue<TickItem> tickQueue = new LinkedBlockingQueue<>();
    private static ObjectMapper mapper=new ObjectMapper();
    private MyWebSocketClient client = new MyWebSocketClient(priceServer, "行情服务器", this);

    protected static final int TickCacheCount = 1000;
    protected static ConcurrentHashMap<String, TickItem> newestTickMap = new ConcurrentHashMap<>();
    //9类k线，每类缓存1000根,List中Map的key表示数据源编码，List Index本身就是TickType枚举值
    protected static List<ConcurrentHashMap<String, List<TickBase>>> tickContainer = new ArrayList<>();

    static {
        tickContainer.add(new ConcurrentHashMap<>());
        tickContainer.add(new ConcurrentHashMap<>());
        tickContainer.add(new ConcurrentHashMap<>());
        tickContainer.add(new ConcurrentHashMap<>());
        tickContainer.add(new ConcurrentHashMap<>());
        tickContainer.add(new ConcurrentHashMap<>());
        tickContainer.add(new ConcurrentHashMap<>());
        tickContainer.add(new ConcurrentHashMap<>());
        tickContainer.add(new ConcurrentHashMap<>());
    }

    private void startConsume() {
        ThreadUtil.start(() -> {
            while (true) {
                try {
                    TickItem tick = tickQueue.take();
                    update(tick);
                    tick(tick);
                } catch (InterruptedException e) {
                    LogUtil.err(TickManagerBase.class, e);
                }
            }
        });
    }

    protected abstract void tick(TickItem tick);

    protected void start() {
        try {
            client.start();
            startConsume();
        } catch (Exception e) {
            LogUtil.err(TickManagerBase.class, e);
        }
    }

    @Override
    public void onMessage(String message) {
        try {
            if (isTicking && StringUtils.isNotBlank(message)) {
                tickQueue.put(mapper.readValue(message, TickItem.class));
            }
        } catch (Exception e) {
            LogUtil.err(TickManagerBase.class, e);
        }
    }

    /**
     * desc 获取历史k线数据
     * author 钱智慧
     * date 2017/11/14 14:06
     *
     * @param type     k线类型
     * @param code     报价来源编码，比如货币对编码
     * @param lastTime 上次获取到的k线中最新的k线的时间
     * @return
     */
    public static List<TickBase> getTicks(TickType type, String code, int lastTime) {
        List<TickBase> ret = new ArrayList<>();
        int typeIndex = type.ordinal();
        List<TickBase> ticks = tickContainer.get(typeIndex).get(code);
        if (ticks != null) {
            ret = ticks.stream().filter(a -> a.getTime() > lastTime).collect(Collectors.toList());
        }
        return ret;
    }

    /**
     * 获取最新报价
     *
     * @param code
     * @return
     */
    public static TickItem getNewestTick(String code) {
        return newestTickMap.get(code);
    }


    /**
     * 获取最新报价(原始价格)
     *
     * @return
     */
    public static Map<String, BigDecimal> getNewestPrices() {
        Map<String, BigDecimal> map = new HashMap<>();
        newestTickMap.forEach((k, v) -> map.put(k, v.getPrice()));
        return map;
    }

    private static int getStandardTime(int time, int type) {
        int result = 0;
        LocalDateTime dateTime = DateUtil.fromUnix(time);
        LocalDateTime date = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), 0, 0);
        TickType tickType = TickType.values()[type];
        switch (tickType) {
            //1分钟
            case M1:
                result = time / 60 * 60;
                break;
            //5分钟
            case M5:
                result = time / 300 * 300;
                break;
            //15分钟
            case M15:
                result = time / 900 * 900;
                break;
            //30分钟
            case M30:
                result = time / 1800 * 1800;
                break;
            //1小时
            case H1:
                result = time / 3600 * 3600;
                break;
            //4小时
            case H4:
                result = time / 14400 * 14400;
                break;
            //日线
            case D:
                result = DateUtil.toUnix(date);
                break;
            //周线
            case W:
                //每周第一天：周日
                result = DateUtil.toUnix(date.minusDays(date.getDayOfWeek().getValue() % 7));
                break;
            //月线
            case MN:
                //月初
                result = DateUtil.toUnix(date.minusDays(date.getDayOfMonth() - 1));
                break;
            default:
        }
        return result;
    }

    /**
     * desc 停止/开启行情
     * author 钱智慧
     * date 2017/12/21 10:47
     **/
    public static void toggle(boolean isStop) {
        isTicking = !isStop;
    }

    private void update(TickItem tick) {
        //更新各类k线
        newestTickMap.put(tick.getCode(), tick);
        for (int i = 0; i < tickContainer.size(); ++i) {
            int standardTime = getStandardTime(tick.getTime(), i);
            ConcurrentHashMap<String, List<TickBase>> dict = tickContainer.get(i);
            List<TickBase> ticks = dict.computeIfAbsent(tick.getCode(), k -> new CopyOnWriteArrayList<>());
            if (ticks.size() == 0) {
                ticks.add(new TickBase(tick.getPrice(), tick.getPrice(), tick.getPrice(), tick.getPrice(), standardTime,
                        tick.getCode(), TickType.values()[i]));
            }
            TickBase lastTick = ticks.get(ticks.size() - 1);
            if (lastTick.getTime() == standardTime) {
                lastTick.setClosePrice(tick.getPrice());
                lastTick.setHighPrice(lastTick.getHighPrice().max(tick.getPrice()));
                lastTick.setLowPrice(lastTick.getLowPrice().min(tick.getPrice()));
            } else if (lastTick.getTime() < standardTime) {
                ticks.add(new TickBase(tick.getPrice(), tick.getPrice(), tick.getPrice(), tick.getPrice(), standardTime,
                        tick.getCode(), TickType.values()[i]));
            }
            if (ticks.size() - 100 > TickCacheCount) {//每隔100周期，清理多余的k线
                ticks.subList(0, ticks.size() - TickCacheCount).clear();
            }
        }
    }

    protected static void load(SqlHelper<TickBase> helper, int tickType, String code) {
        List<TickBase> list = helper.list("select * from Tick where type=:type and code=:code order by Id desc limit :limit",
                ImmutableMap.of("type", tickType, "code", code, "limit", TickCacheCount));
        if (tickType == 0 && list.size() > 0) {
            TickBase tk = list.get(0);
            newestTickMap.put(tk.getCode(), new TickItem().setCode(tk.getCode()).setPrice(tk.getClosePrice()).setTime(tk.getTime()));
        }
        CopyOnWriteArrayList<TickBase> cpList = new CopyOnWriteArrayList<>();
        Collections.reverse(list);
        cpList.addAll(list);
        tickContainer.get(tickType).put(code, cpList);
    }
}
