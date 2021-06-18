package net.za.dawes.wintexprotocol.wintexanalysis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import net.za.dawes.wintexprotocol.WintexMessage;

class MessageParser {
    private WintexTableModel tableModel;
    private ByteArrayOutputStream request = new ByteArrayOutputStream();
    private ByteArrayOutputStream response = new ByteArrayOutputStream();

    MessageParser(WintexTableModel tableModel) {
        this.tableModel = tableModel;
    }

    private byte[] toByteArray(String text) {
        text = text.replace(" ", "");
        int len = text.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(text.charAt(i), 16) << 4)
                    + Character.digit(text.charAt(i + 1), 16));
        }
        return data;
    }

    void parse(String op, String text) throws IOException {
        byte[] data = toByteArray(text);
        if (op.equals("Write")) {
            if (response.size() > 0) {
                WintexRecord record = new WintexRecord();
                record.request = WintexMessage.fromBytes(request.toByteArray());
                request.reset();
                record.response = WintexMessage.fromBytes(response.toByteArray());
                response.reset();
//                System.out.println(record);
                tableModel.add(record);
                record = new WintexRecord();
            }
            request.write(data);
        } else if (op.equals("Read") && request.size() > 0) {
            response.write(data);
        }
        System.out.println(op + ": " + text);
    }
    
    void parse(MqttMessage message) {
        try {
            byte[] payload = message.getPayload();
            String text = new String(payload);
            int i;
            if ((i = text.indexOf("Read :")) >= 0) {
                text = text.substring(i);
            } else if ((i = text.indexOf("Write:")) > 0) {
                text = text.substring(i);
            } else
                return;
            int p = text.indexOf('|');
            if (p > 0) {
                text = text.substring(0, p).trim();
            }
            int c = text.indexOf(':');
            String op = text.substring(0, c).trim();
            text = text.substring(c + 1).trim();
            parse(op, text);
        } catch (Exception e) {
            e.printStackTrace();
            request.reset();
            response.reset();
        }
    }
}