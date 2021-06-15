package net.za.dawes.wintexprotocol;

import java.util.Arrays;

public class WintexMessage {

    private static final int ADDR_KEYPADLCD = 0x000668;

    private final byte type;
    private final byte[] data;

    public WintexMessage(char type, byte[] data) {
        this((byte) type, data);
    }

    public WintexMessage(byte type, byte[] data) {
        this.type = type;
        this.data = data == null ? new byte[0] : data;
    }

    public byte getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }

    private static int addr(byte[] data, int off, int len) {
        int addr = 0;
        for (int i = off; i < len; i++) {
            addr = (addr << 8) + ((int) data[i] & 0xFF);
        }
        return addr;
    }

    public String toString() {
        try {
            if (type == 'O' || type == 'R') {
                String location = (type == 'O' ? "Config" : "Transient");
                return "Read " + location + " @ (0x" + Integer.toHexString(addr(data, 0, 3)) + "/" + addr(data, 0, 3) + ") "
                        + ((int) (data[3] & 0xFF)) + " bytes";
            }
            if (type == 'I' || type == 'W') {
                String location = (type == 'I' ? "Config" : "Transient");
                return "Read " + location + " @ (0x" + Integer.toHexString(addr(data, 0, 3)) + "/" + addr(data, 0, 3) + ") "
                        + ((int) (data[3] & 0xFF)) + " bytes\n" + Xxd.dump(getMemory(this));
            }
            return String.format("%c:%s", (char) type, Xxd.dump(data));
        } catch (Exception e) {
            return e.getLocalizedMessage() + " printing type " + ((char) type);
        }
    }

    public static WintexMessage fromBytes(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("data");
        }
        if (bytes.length < 3) {
            throw new IndexOutOfBoundsException(
                    "Expected a minimum length of 3, got " + bytes.length + " : " + Xxd.dump(bytes));
        }
        if (bytes.length != bytes[0]) {
            throw new IndexOutOfBoundsException(
                    "Expected " + bytes[0] + ", but got " + bytes.length + " : " + Xxd.dump(bytes));
        }
        char type = (char) bytes[1];
        byte[] data = new byte[bytes.length - 3];
        System.arraycopy(bytes, 2, data, 0, data.length);
        return new WintexMessage(type, data);
    }

    public static WintexMessage login(byte[] udl) {
        return new WintexMessage('Z', udl);
    }

    public static String getPanelVersion(WintexMessage msg) {
        if (msg.getType() == (byte) 'Z' && msg.getData().length == 0x10) {
            return new String(msg.getData());
        }
        return "";
    }

    public static WintexMessage heartbeat() {
        return new WintexMessage('Z', null);
    }

    public static boolean isHeartbeatResponse(WintexMessage msg) {
        return msg.getType() == ((byte) 'Z')
                && Arrays.equals(msg.getData(), new byte[] { 0x06, 0x01, 0x00, 0x00, 0x03, 0x08, 0x04, 0x03 });
    }

    public static WintexMessage hangup() {
        return new WintexMessage('H', null);
    }

    public static boolean isLoggedOut(WintexMessage msg) {
        return msg.getType() == 0x06 && msg.getData().length == 0;
    }

    private static WintexMessage readMemory(char type, int addr, int len) {
        byte A = (byte) ((addr >> 16));
        byte B = (byte) ((addr >> 8));
        byte C = (byte) ((addr >> 0));
        return new WintexMessage(type, new byte[] { A, B, C, (byte) (len & 0xFF) });
    }

    private static WintexMessage writeMemory(char type, int addr, byte[] data) {
        byte A = (byte) ((addr >> 16));
        byte B = (byte) ((addr >> 8));
        byte C = (byte) ((addr >> 0));
        byte[] msg = new byte[4 + data.length];
        System.arraycopy(new byte[] { A, B, C, (byte) (data.length & 0xFF) }, 0, msg, 0, 4);
        System.arraycopy(data, 0, msg, 4, data.length);
        return new WintexMessage(type, msg);
    }

    private static byte[] getMemory(WintexMessage msg) {
        if (msg.getType() == 'W' || msg.getType() == 'I') {
            int len = msg.getData()[3] & 0xFF;
            byte[] data = new byte[len];
            System.arraycopy(msg.getData(), 4, data, 0, data.length);
            return data;
        }
        throw new RuntimeException("Wrong message type: " + ((char) msg.getType()));
    }

    private static WintexMessage readTransientMemory(int addr, int len) {
        return readMemory('R', addr, len);
    }

    private static WintexMessage writeTransientMemory(int addr, byte[] data) {
        return writeMemory('W', addr, data);
    }

    public static WintexMessage readConfigurationMemory(int addr, int len) {
        return readMemory('O', addr, len);
    }

    public static WintexMessage login1() {
        return readConfigurationMemory(0x000650, 1);
    }

    public static WintexMessage login2() {
        return readConfigurationMemory(0x001FE5, 1);
    }

    public static WintexMessage login3() {
        return readConfigurationMemory(0x0004F2, 1);
    }

    public static WintexMessage readLCDText() {
        return readTransientMemory(ADDR_KEYPADLCD, 0x22);
    }

    public static String getLCDText(WintexMessage msg) {
        if (msg.getType() == (byte) 'W' && msg.getData().length == (0x04 + 0x22)
                && Arrays.equals(msg.getData(), 0, 4, new byte[] { 0x00, 0x06, 0x68, 0x22 }, 0, 4)) {
            return new String(msg.getData(), 4, 0x20);
        }
        return "";
    }

    public static WintexMessage getZoneNames(int zone) {
        int base = 0; // Not sure if this applies to other panels, works for Premier 832
        int count = 8; // may need to be configured per panel, but 8 seems to be the Lowest Common
                       // Denominator
        return readConfigurationMemory(base + (zone * 0x10), count * 0x10);
    }

    private static int getStrings(String[] strings, int off, byte[] data, int len) {
        int i = 0;
        for (i = 0; i < strings.length - off && i * len < data.length; i++) {
            strings[off + i] = new String(data, i * len, len).trim();
        }
        return i;
    }

    public static int updateZoneNames(String[] zones, WintexMessage msg) {
        int base = 0; // again, maybe only the 832?
        byte[] data = getMemory(msg);
        int zoneBase = (addr(msg.getData(), 0, 3) - base) / 0x10;
        if (zoneBase < 0 || zoneBase > zones.length)
            throw new RuntimeException("Out of range zone name query, zoneBase = " + zoneBase);
        return getStrings(zones, zoneBase, data, 0x10);
    }

    public static WintexMessage getUserNames(int user) {
        int base = 0x002000; // Not sure if this applies to other panels, works for Premier 832
        int count = 8; // may need to be configured per panel
        return readConfigurationMemory(base + (user * 0x08), count * 0x08);
    }

    public static int updateUserNames(String[] users, WintexMessage msg) {
        int base = 0x002000; // again, maybe only the 832?
        byte[] data = getMemory(msg);
        int userBase = (addr(msg.getData(), 0, 3) - base) / 0x08;
        if (userBase < 0 || userBase > users.length)
            throw new RuntimeException("Out of range username query, userBase = " + userBase);
        return getStrings(users, userBase, data, 0x08);
    }

    public static WintexMessage setLCDText(String text) {
        if (text.length() > 0x20)
            text = text.substring(0, 0x20);
        else if (text.length() < 0x20)
            for (int i = text.length(); i < 20; i++)
                text = text + " ";
        return writeTransientMemory(0x000C5C, text.getBytes());
    }
}
