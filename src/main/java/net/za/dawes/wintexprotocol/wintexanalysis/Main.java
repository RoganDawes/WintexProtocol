package net.za.dawes.wintexprotocol.wintexanalysis;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import net.za.dawes.wintexprotocol.WintexMessage;

public class Main extends JFrame {
    private JTable table;
    private WintexTableModel tableModel = new WintexTableModel();

    private static class WintexRecord {
        WintexMessage request = null;
        WintexMessage response = null;
        Date date = new Date();
        String comment = null;
        long count = 1;

        public String toString() {
            return date + " >> " + request + " !! << " + response + " !! " + (comment == null ? "" : comment);
        }
    }

    private static class WintexTableModel extends AbstractTableModel {
        @Override
        public String getColumnName(int column) {
            return new String[] { "Date", "Request", "Response", "Count", "Comment" }[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return new Class[] { Date.class, WintexMessage.class, WintexMessage.class, Number.class,
                    String.class }[columnIndex];
        }

        private LinkedHashMap<String, WintexRecord> data = new LinkedHashMap<>();

        public void add(WintexRecord record) {
            String key = record.request.toString() + ":" + record.response.toString();
            if (data.containsKey(key)) {
                record = data.get(key);
                record.count++;
            } else {
                data.put(key, record);
            }
            int i = new ArrayList<String>(data.keySet()).indexOf(key);
            if (i > 0) {
                if (record.count > 1)
                    fireTableRowsUpdated(i, i);
                else
                    fireTableRowsInserted(i, i);
            }
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public Object getValueAt(int row, int column) {
            String key = new ArrayList<String>(data.keySet()).get(row);
            WintexRecord record = data.get(key);
            switch (column) {
            case 0:
                return record.date;
            case 1:
                return record.request;
            case 2:
                return record.response;
            case 3:
                return record.count;
            case 4:
                return record.comment;
            }
            return null;
        }

    }

    public Main() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(Main.this, "Are you sure you want to close this window?",
                        "Close Window?", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        table = new JTable(tableModel);
        table.setDefaultRenderer(Date.class, new DateRenderer());
        table.setDefaultRenderer(WintexMessage.class, new MultiLineCellRenderer());
        table.setFont(new Font("monospaced", Font.PLAIN, 12));
        scrollPane.setViewportView(table);
    }

    private static class MessageParser {
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

        private void parse(MqttMessage message) {
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
                byte[] data = toByteArray(text);
                if (op.equals("Write")) {
                    if (response.size() > 0) {
                        WintexRecord record = new WintexRecord();
                        record.request = WintexMessage.fromBytes(request.toByteArray());
                        request.reset();
                        record.response = WintexMessage.fromBytes(response.toByteArray());
                        response.reset();
                        System.out.println(record);
                        tableModel.add(record);
                        record = new WintexRecord();
                    }
                    request.write(data);
                } else if (op.equals("Read") && request.size() > 0) {
                    response.write(data);
                }
                System.out.println(op + ": " + text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class MultiLineCellRenderer extends JTextArea implements TableCellRenderer, ListCellRenderer {

        private static final long serialVersionUID = 8068299330081793937L;

        public MultiLineCellRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setFont(table.getFont());
            if (hasFocus) {
                setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
                if (table.isCellEditable(row, column)) {
                    setForeground(UIManager.getColor("Table.focusCellForeground"));
                    setBackground(UIManager.getColor("Table.focusCellBackground"));
                }
            } else {
                setBorder(new EmptyBorder(1, 2, 1, 2));
            }
            setText((value == null) ? "" : value.toString());

            // FIXME : this is not the entire solution, but is good enough for the moment
            // fails when resizing to smaller than the text width, if we are using line
            // wrapping
            int height_wanted = (int) getPreferredSize().getHeight();
            if (height_wanted > table.getRowHeight(row))
                table.setRowHeight(row, height_wanted);
            return this;
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            if (isSelected) {
                setForeground(list.getSelectionForeground());
                setBackground(list.getSelectionBackground());
            } else {
                setForeground(list.getForeground());
                setBackground(list.getBackground());
            }
            setFont(list.getFont());
            if (cellHasFocus) {
                setBorder(UIManager.getBorder("List.focusCellHighlightBorder"));
                /*
                 * if (list.isCellEditable(row, column)) { setForeground(
                 * UIManager.getColor("List.focusCellForeground") ); setBackground(
                 * UIManager.getColor("List.focusCellBackground") ); }
                 */
            } else {
                // setBorder(new EmptyBorder(1, 2, 1, 2));
                setBorder(new javax.swing.border.LineBorder(java.awt.Color.LIGHT_GRAY));
            }
            setText((value == null) ? "" : value.toString());

            /*
             * // FIXME : this is not the entire solution, but is good enough for the moment
             * // fails when resizing to smaller than the text width, if we are using line
             * wrapping int height_wanted = (int)getPreferredSize().getHeight(); if
             * (height_wanted > list.getRowHeight(row)) table.setRowHeight(row,
             * height_wanted);
             */

            return this;
        }

    }

    public static class DateRenderer extends DefaultTableCellRenderer {

        /**
             * 
             */
        private static final long serialVersionUID = 4146923038862167831L;
        private static SimpleDateFormat[] formats = new SimpleDateFormat[] {
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S"), new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"),
                new SimpleDateFormat("MM/dd HH:mm:ss"), new SimpleDateFormat("HH:mm:ss"),
                new SimpleDateFormat(":mm:ss") };

        public DateRenderer() {
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof Date) {
                Date date = (Date) value;
                FontMetrics fm = getFontMetrics(getFont());
                int i = 0;
                String text;
                int textWidth;
                int targetWidth = table.getColumnModel().getColumn(column).getWidth() - 4;
                do {
                    text = formats[i++].format(date);
                    textWidth = fm.stringWidth(text);
                } while (textWidth > targetWidth && i < formats.length);
                setText(text);
            }
            return this;
        }

    }

    public static void main(String[] args) throws Exception {
        String publisherId = UUID.randomUUID().toString();
        IMqttClient subscriber = new MqttClient(args[0], publisherId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        subscriber.connect(options);

        Main main = new Main();
        final MessageParser parser = new MessageParser(main.tableModel);
        subscriber.subscribe("alarm/logs", new IMqttMessageListener() {

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                parser.parse(message);
            }

        });
        main.setVisible(true);
    }
}
