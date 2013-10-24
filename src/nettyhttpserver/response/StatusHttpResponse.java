/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nettyhttpserver.response;

import nettyhttpserver.response.watcher.Info;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Roman
 */
class StatusHttpResponse {

    private final StringBuilder buf = new StringBuilder();
    public static Charset charset = Charset.forName("UTF-8");
    public static CharsetEncoder encoder = charset.newEncoder();

    public static ByteBuffer str_to_bt(String msg) {
        try {
            return encoder.encode(CharBuffer.wrap(msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    FullHttpResponse getResponse() {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        buf.append("Request Count : " + Info.getRequestCount() + "\r\n");
        buf.append("Request Unique Count : " + Info.getCountUniqueRequest() + "\r\n");
        buf.append("Active channel : " + Info.getChannelCount() + "\r\n");



        HashMap<String, Integer> requestCountForAllIp = Info.getRequestCountForAllIp();
        HashMap<String, Long> timeStampIP = Info.getRequestTimeStampForAllIp();
        Set<String> Ip = requestCountForAllIp.keySet();
        buf.append("Ip                  |count_connection  |  timestamp \r\n");
        for (String ip : Ip) {
            buf.append(ip + "            " + requestCountForAllIp.get(ip) + "         " + timeStampIP.get(ip) + "\r\n");
        }
        Set<String> url = Info.getRedirectCountForAllUrl().keySet();
        buf.append("\r\nRedirect URL \r\n");
        buf.append("count|     URL \r\n");
        System.out.println("URL | " + url.toString());
        for (String u : url) {
            buf.append(Info.getRedirectCountForAllUrl().get(u) + "    | " + u + "\r\n");
        }

        Set<Integer> ID = Info.getAllID();
        buf.append("\r\nscr_IP              | received_bytes | timestamp    |   send_bytes | URL \r\n");
        for (Integer i : ID) {
            if (i > 16) {
                break;
            }
            buf.append(Info.getIpAddressById(i) + "  "
                    + Info.getRecievedBytes(i) + "               "
                    + Info.getTimeStampbyID(i) + "  "
                    + Info.getSendBytes(i) + "            "
                    + Info.getURLbyID(i) + "\r\n");
        }

        response.content().writeBytes(str_to_bt(buf.toString()));

        response.headers().set(CONTENT_TYPE, "text/plain");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }
}
