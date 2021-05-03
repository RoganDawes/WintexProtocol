package net.za.dawes.wintexprotocol;

import java.util.Arrays;
import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class WintexClientHandler extends ChannelInboundHandlerAdapter {

    private enum State {
        LOGGING_IN, GET_ZONE_NAMES, GET_USER_NAMES, HEARTBEAT, READING_LCD, HANGING_UP, LOGGED_OUT
    };

    private byte[] udl;
    private State state = State.LOGGED_OUT;
    private Panel panel = null;
    private int zone = 0;
    private String[] zones = null;
    private int user = 0;
    private String[] users = null;
    
    public WintexClientHandler(byte[] udl) {
        this.udl = udl;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("Logging in");
        state = State.LOGGING_IN;
        WintexMessage msg = WintexMessage.login(udl);
        ctx.writeAndFlush(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) {
        WintexMessage msg = null;
        if (obj instanceof WintexMessage)
            msg = (WintexMessage) obj;
        else {
            System.err.println("Unexpected message: " + obj);
            ctx.close();
            return;
        }
        state = processMessage(state, msg);
        WintexMessage nextMessage = nextMessage(state);
        if (nextMessage != null) {
            ctx.writeAndFlush(nextMessage);
        } else {
            ctx.close();
        }
    }

    private State processMessage(State state, WintexMessage msg) {
        if (msg.getType() == 0x0F)
            return State.LOGGED_OUT;
        switch (state) {
        case LOGGING_IN:
            if (msg.getData().length == 0) {
                System.err.println("Login failed");
                return null;
            } else {
                System.out.println("Logged in");
                String panelVersion = WintexMessage.getPanelVersion(msg);
                System.out.println("Panel version " + panelVersion);
                this.panel = Panel.fromString(panelVersion);
                zone = 0;
                zones = new String[panel.zones];
                user = 0;
                users = new String[panel.users];
                return State.GET_ZONE_NAMES;
            }
        case GET_ZONE_NAMES:
            zone += WintexMessage.updateZoneNames(zones, msg);
            if (zone >= panel.zones) {
                List<String> zones = Arrays.asList(this.zones);
                System.out.println("Zones: " + zones);
                user = 0;
                return State.GET_USER_NAMES;
            }
            return State.GET_ZONE_NAMES;
        case GET_USER_NAMES:
            user += WintexMessage.updateUserNames(users, msg);
            if (user >= panel.users) {
                List<String> users = Arrays.asList(this.users);
                System.out.println("Users: " + users);
                return State.READING_LCD;
            }
            return State.GET_USER_NAMES;
        case HEARTBEAT:
            if (WintexMessage.isHeartbeatResponse(msg)) {
                System.out.println("PING");
                return State.READING_LCD;
            }
            return State.HANGING_UP;
        case READING_LCD:
            String lcdText = WintexMessage.getLCDText(msg);
            System.out.println("LCD: '" + lcdText + "'");
            return State.HANGING_UP;
        case HANGING_UP:
            if (WintexMessage.isLoggedOut(msg)) {
                System.err.println("Logged out");
                return State.LOGGED_OUT;
            }
            return State.HANGING_UP;
        default:
            return State.HANGING_UP;
        }
    }

    private WintexMessage nextMessage(State state) {
        switch (state) {
        case LOGGING_IN:
            return WintexMessage.login(udl);
        case READING_LCD:
            return WintexMessage.readLCDText();
        case HANGING_UP:
            return WintexMessage.hangup();
        case GET_ZONE_NAMES:
            return WintexMessage.getZoneNames(zone);
        case GET_USER_NAMES:
            return WintexMessage.getUserNames(user);
        case HEARTBEAT:
            return WintexMessage.heartbeat();
        case LOGGED_OUT:
            return null;
        default:
            return WintexMessage.hangup();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
