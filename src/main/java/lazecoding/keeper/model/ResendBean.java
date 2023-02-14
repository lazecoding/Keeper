package lazecoding.keeper.model;

import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

/**
 *
 *  重发实体
 *
 * @author lazecoding
 */
public class ResendBean implements Serializable {

    private static final long serialVersionUID = -1L;

    private ChannelHandlerContext ctx;

    private String content = "";

    private int retry = 0;

    public ResendBean() {
    }

    public ResendBean(ChannelHandlerContext ctx, String content) {
        this.ctx = ctx;
        this.content = content;
    }

    public ResendBean(ChannelHandlerContext ctx, String content, int retry) {
        this.ctx = ctx;
        this.content = content;
        this.retry = retry;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    @Override
    public String toString() {
        return "ResendBean{" +
                "ctx=" + ctx +
                ", content='" + content + '\'' +
                ", retry=" + retry +
                '}';
    }
}
