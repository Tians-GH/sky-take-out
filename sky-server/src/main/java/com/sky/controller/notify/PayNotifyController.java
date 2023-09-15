package com.sky.controller.notify;

import com.alibaba.druid.support.json.JSONUtils;
import com.sky.context.BaseContext;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * 支付回调相关接口
 */
@RestController
@RequestMapping("/notify")
@Slf4j
public class PayNotifyController {
    @Autowired
    private OrderService orderService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    private WeChatProperties weChatProperties;

    /**
     * 支付成功回调
     *
     * @param
     */
    @RequestMapping("/paySuccess")
    public void paySuccessNotify() throws Exception {
        // //读取数据
        // String body = readData(request);
        // log.info("支付成功回调：{}", body);
        //
        // //数据解密
        // String plainText = decryptData(body);
        // log.info("解密后的文本：{}", plainText);
        //
        // JSONObject jsonObject = JSON.parseObject(plainText);
        Long userId = BaseContext.getCurrentId();
        // 商户平台订单号
        Orders orders = orderMapper.getNumber(userId);
        String number = orders.getNumber();
        // 微信支付交易号

        // 业务处理，修改订单状态、来单提醒
        orderService.paySuccess(number);

        // 给微信响应
        HttpServletResponse response = null;
        responseToWeixin(response);
    }


    /**
     * 给微信响应
     *
     * @param response
     */
    private void responseToWeixin(HttpServletResponse response) throws Exception {
        response.setStatus(200);  // 设置响应状态码为200，表示请求成功
        HashMap<Object, Object> map = new HashMap<>();  // 创建一个HashMap对象，用于存储要返回的数据
        map.put("code", "SUCCESS");  // 将"code"和"SUCCESS"作为键值对存入map中
        map.put("message", "SUCCESS");  // 将"message"和"SUCCESS"作为键值对存入map中

        // 设置响应头的Content-type为application/json，告诉浏览器返回的数据类型是JSON
        response.setHeader("Content-type", ContentType.APPLICATION_JSON.toString());
        // 将map对象转换为JSON字符串，并将其以UTF-8编码的字节流形式写入响应的输出流中
        response.getOutputStream().write(JSONUtils.toJSONString(map).getBytes(StandardCharsets.UTF_8));
        response.flushBuffer();  // 刷新响应缓冲区，确保数据被发送出去
    }


    // /**
    //  * 支付成功回调
    //  *
    //  * @param request
    //  */
    // @RequestMapping("/paySuccess")
    // public void paySuccessNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
    //     //读取数据
    //     String body = readData(request);
    //     log.info("支付成功回调：{}", body);
    //
    //     //数据解密
    //     String plainText = decryptData(body);
    //     log.info("解密后的文本：{}", plainText);
    //
    //     JSONObject jsonObject = JSON.parseObject(plainText);
    //     String outTradeNo = jsonObject.getString("out_trade_no");//商户平台订单号
    //     String transactionId = jsonObject.getString("transaction_id");//微信支付交易号
    //
    //     log.info("商户平台订单号：{}", outTradeNo);
    //     log.info("微信支付交易号：{}", transactionId);
    //
    //     //业务处理，修改订单状态、来单提醒
    //     orderService.paySuccess(outTradeNo);
    //
    //     //给微信响应
    //     responseToWeixin(response);
    // }
    //
    // /**
    //  * 读取数据
    //  *
    //  * @param request
    //  * @return
    //  * @throws Exception
    //  */
    // private String readData(HttpServletRequest request) throws Exception {
    //     BufferedReader reader = request.getReader();
    //     StringBuilder result = new StringBuilder();
    //     String line = null;
    //     while ((line = reader.readLine()) != null) {
    //         if (result.length() > 0) {
    //             result.append("\n");
    //         }
    //         result.append(line);
    //     }
    //     return result.toString();
    // }
    //
    // /**
    //  * 数据解密
    //  *
    //  * @param body
    //  * @return
    //  * @throws Exception
    //  */
    // private String decryptData(String body) throws Exception {
    //     JSONObject resultObject = JSON.parseObject(body);
    //     JSONObject resource = resultObject.getJSONObject("resource");
    //     String ciphertext = resource.getString("ciphertext");
    //     String nonce = resource.getString("nonce");
    //     String associatedData = resource.getString("associated_data");
    //
    //     AesUtil aesUtil = new AesUtil(weChatProperties.getApiV3Key().getBytes(StandardCharsets.UTF_8));
    //     //密文解密
    //     String plainText = aesUtil.decryptToString(associatedData.getBytes(StandardCharsets.UTF_8),
    //             nonce.getBytes(StandardCharsets.UTF_8),
    //             ciphertext);
    //
    //     return plainText;
    // }
    //
    // /**
    //  * 给微信响应
    //  * @param response
    //  */
    // private void responseToWeixin(HttpServletResponse response) throws Exception{
    //     response.setStatus(200);
    //     HashMap<Object, Object> map = new HashMap<>();
    //     map.put("code", "SUCCESS");
    //     map.put("message", "SUCCESS");
    //     response.setHeader("Content-type", ContentType.APPLICATION_JSON.toString());
    //     response.getOutputStream().write(JSONUtils.toJSONString(map).getBytes(StandardCharsets.UTF_8));
    //     response.flushBuffer();
    // }
}
