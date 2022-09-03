package com.example.vrp_secondedition.util;

import com.example.vrp_secondedition.RequestType.PathPlan;
import com.example.vrp_secondedition.pojo.Order;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpApi {

    /*
     *   转换pointList中的address，由地名转换为经纬度
     *
     */
    public static boolean translateAddress(PathPlan pathPlan) {
        //创建线程池，用于异步获取高德api
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(8, 16,
                1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), new ThreadPoolExecutor.CallerRunsPolicy());
        //创建httpclient
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            //创建结果集
            Future<?>[] future = new Future[pathPlan.pointList.size()+1];
            //发出请求
            for (int i = 0; i < future.length; i++) {
                int finalI = i;
                future[i] = threadPool.submit(() -> {
                    URIBuilder builder = new URIBuilder("https://restapi.amap.com/v3/geocode/geo");
                    builder.addParameter("key", "e89ce15f79f9503a8890594a4f8d2d64");
                    if(finalI==0){
                        builder.addParameter("address", pathPlan.administrator.address);
                    }else {
                        builder.addParameter("address", pathPlan.pointList.get(finalI-1).address);
                    }
                    URI uri = builder.build();
                    HttpGet httpGet = new HttpGet(uri);
                    JSONObject json = new JSONObject(EntityUtils.toString(client.execute(httpGet).getEntity(), "utf-8"));
                    if (json.getString("status").equals("1"))
                        return json.getJSONArray("geocodes").getJSONObject(0).getString("location");
                    return null;
                });
            }
            //转化address
            for (int i = 0; i < future.length; i++) {
                if (future[i].get() == null)
                    return false;
                if(i==0){
                    pathPlan.administrator.address=(String) future[i].get();
                }else {
                    pathPlan.pointList.get(i-1).address= (String) future[i-1].get();
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            threadPool.shutdown();
        }
    }

    /*
     * 返回路线长度图
     *
     */
    public static double[][] getDirection(PathPlan pathPlan){
        //创建线程池，用于异步获取高德api
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(8, 16,
                1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), new ThreadPoolExecutor.CallerRunsPolicy());
        //创建httpclient
        CloseableHttpClient client = HttpClients.createDefault();
        try{
            //创建结果集
            Future<Double>[][] future = new Future[pathPlan.pointList.size()+1][pathPlan.pointList.size()+1];
            double[][] result = new double[pathPlan.pointList.size()+1][pathPlan.pointList.size()+1];
            //发出请求
            for (int i = 0; i < future.length; i++) {
                for (int j = 0; j < future.length; j++) {
                    int finalI = i;
                    int finalJ = j;
                    future[i][j] = threadPool.submit(() -> {
                        if (finalI == finalJ) return 0.0;
                        URIBuilder builder = new URIBuilder("https://restapi.amap.com/v5/direction/driving");
                        builder.addParameter("key", "e89ce15f79f9503a8890594a4f8d2d64");
                        builder.addParameter("origin", finalI==0?pathPlan.administrator.address:pathPlan.pointList.get(finalI-1).address);
                        builder.addParameter("destination", finalJ==0?pathPlan.administrator.address:pathPlan.pointList.get(finalJ-1).address);
                        builder.addParameter("strategy",pathPlan.drivingStrategy.toString());
                        URI uri = builder.build();
                        HttpGet httpGet = new HttpGet(uri);
                        JSONObject json = new JSONObject(EntityUtils.toString(client.execute(httpGet).getEntity(), "utf-8"));
                        if (json.getString("status").equals("1")&&((json=json.getJSONObject("route").getJSONArray("paths").optJSONObject(0))!=null))
                            return json.getDouble("distance");
                        return Double.MAX_VALUE/2;
                    });
                }
            }
            //转化
            for (int i = 0; i < future.length; i++) {
                for (int j = 0; j < future.length; j++) {
                    if (Math.abs(future[i][j].get() - Double.MAX_VALUE/2)<0.00001)
                        return null;
                    result[i][j] = future[i][j].get();
                }
            }
            return result;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            threadPool.shutdown();
        }
    }
}
