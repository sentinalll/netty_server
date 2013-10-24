/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nettyhttpserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpRequest;

import static io.netty.handler.codec.http.HttpHeaders.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;
import nettyhttpserver.response.watcher.Info;

/**
 *
 * @author Roman
 */
public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter {

    private static int ID = 0;
    private static int count = 1;

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof DefaultHttpRequest) {
            DefaultHttpRequest request = (DefaultHttpRequest) msg;
            if (is100ContinueExpected(request)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }
            Info.setRecievedBytes(ID, request.headers().entries().toString().getBytes("UTF-8").length);
            request.headers().add("ID", ID);
            Info.putIpAddress(ID, ctx.channel().localAddress().toString());
            Info.setURLtoID(ID, request.getUri());
            Info.setTimeStamptoID(ID, System.currentTimeMillis());
            Info.incrementRequestCount(ctx.channel().localAddress().toString());
            Info.setRequestTimeStamp(ctx.channel().localAddress().toString(), System.currentTimeMillis());
            if (ID < 15) {
                ID++;
            } else {
                ID = 0;
            }
            System.out.println("2");


            new NettyHttpSendResponseHandlerFactory(ctx, request).sendResponse();
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        Info.decrementChannelCount();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        Info.incrementChannelCount();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
