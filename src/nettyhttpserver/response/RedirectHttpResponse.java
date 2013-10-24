/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nettyhttpserver.response;

import nettyhttpserver.response.watcher.Info;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.util.List;

/**
 *
 * @author Roman
 */
class RedirectHttpResponse {

       public FullHttpResponse getResponse(QueryStringDecoder qsd) {
        List<String> url = qsd.parameters().get("url");
       System.out.println("5" +url.get(0));
       Info.redirectCount(url.get(0));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.MOVED_PERMANENTLY);
        response.headers().set(LOCATION, url.get(0));
        return response;
    }
}
