package com.heeexy.example.util.api;

import com.heeexy.example.util.StringTools;
import lombok.extern.slf4j.Slf4j;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.*;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zl
 * @Package: com.heeexy.example.util.api
 * @Name: tianapiUtil
 * @Date: 2023/8/31 17:46
 *
 *
 *接口地址：https://apis.tianapi.com/finance/index
 *请求示例：https://apis.tianapi.com/finance/index?key=bdaed4f7b2039e9e1fbc127988a9a046&code=hk00700&list=1
 *支持协议：http/https
 *请求方式：get/post
 *返回格式：utf-8 json

 *key	string	是	您自己的APIKEY（注册账号后获得）	API密钥
 *code	string	是	hk00700	股票代码，多个以半角逗号,分隔，最多10个
 *list	int	是	1	单个0、批量1

 *
 * #########
 * 由于股票行情分析接口返回的是一个列表（newslist），按照数组下标取值（下表中的字段序号）
 * 字段序号	内容
 * 1	证券简称
 * 2	今日开盘价
 * 3	昨日收盘价
 * 4	最近成交价
 * 5	最高成交价
 * 6	最低成交价
 * 7	买入价
 * 8	卖出价
 * 9	成交数量
 * 10	成交金额
 * 11	买数量一
 * 12	买价位一
 * 13	买数量二
 * 14	买价位二
 * 15	买数量三
 * 16	买价位三
 * 17	买数量四
 * 18	买价位四
 * 19	买数量五
 * 20	买价位五
 * 21	卖数量一
 * 22	卖价位一
 * 23	卖数量二
 * 24	卖价位二
 * 25	卖数量三
 * 26	卖价位三
 * 27	卖数量四
 * 28	卖价位四
 * 29	卖数量五
 * 30	卖价位五
 * 31	行情日期
 * 32	行情时间
 * 33	停牌状态
 * 停牌状态：
 * 状态码	状态
 * 00	正常
 * 01	停牌一小时
 * 02	停牌一天
 * 03	连续停牌
 * 04	盘中停牌
 * 05	停牌半天
 * 07	暂停
 * -1	无该记录
 * -2	未上市
 * -3	退市
 * #########
 **/
@Slf4j
public class tianapiUtil {


    public static String getApi(List<String> account){
        String tianapi_data = "";
        String accountStr = StringTools.arrToString(account);
        if(!"".equals(accountStr)){
            try {
                URL url = new URL( "https://apis.tianapi.com/finance/index");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setDoOutput(true);
                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                OutputStream outputStream = conn.getOutputStream();
                String content = "key=bdaed4f7b2039e9e1fbc127988a9a046&code="+ accountStr +"&list=1";
                outputStream.write(content.getBytes());
                outputStream.flush();
                outputStream.close();
                InputStream inputStream = conn.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader (inputStream,"utf-8");
                BufferedReader bufferedReader = new BufferedReader (inputStreamReader);
                StringBuilder tianapi = new StringBuilder();
                String temp = null;
                while ( null != (temp = bufferedReader.readLine())){
                    tianapi.append(temp);
                }
                tianapi_data = tianapi.toString();
                inputStream.close();
            } catch (Exception e) {
                log.info("执行异常：apis.tianapi.com");
            }
        }
        return "ok";
    }

    public static void main(String[] args){

        List<String> account = new ArrayList<String>();
        account.add("sh600291");
        account.add("sh600291");

        String ll = getApi(account);

    }
}
