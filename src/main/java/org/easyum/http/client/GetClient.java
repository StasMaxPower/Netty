package org.easyum.http.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.net.URISyntaxException;

public class GetClient {

    private static final int PORT = 8080;
    private static final String HOST = "localhost";

    public static void main(String[] args) throws Exception {

        var group = new NioEventLoopGroup();
        try {
            var bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new HttpClientInitializer());

            var channel = bootstrap.connect(HOST, PORT).sync().channel();
            sendGetRequest(channel);
            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    private static void sendGetRequest(Channel channel) throws URISyntaxException {
        var uri = new URI("http://" + HOST + ":" + PORT + "/?name=Ivan");
        var request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.toString());
        channel.writeAndFlush(request);
    }
}
