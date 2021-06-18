package net.za.dawes.wintexprotocol.wintexanalysis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import net.za.dawes.wintexprotocol.Panel;
import net.za.dawes.wintexprotocol.WintexMessage;

public class Main extends JFrame {
    private JTable table;
    Panel panel;
    private WintexTableModel tableModel;

    public Main(Panel panel) {
        this.panel = panel;
        tableModel = new WintexTableModel(panel);
        
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
        RowHighlighter highlighter = new RowHighlighter(table);
        table.setDefaultRenderer(Date.class, highlighter.wrap(new DateRenderer()));
        table.setDefaultRenderer(String.class, highlighter.wrap(new MultiLineCellRenderer()));
        table.setDefaultRenderer(Number.class, highlighter.wrap(new MultiLineCellRenderer()));
        table.setFont(new Font("monospaced", Font.PLAIN, 16));
        scrollPane.setViewportView(table);
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main(Panel.PREMIER_832);
        main.setVisible(true);
        final MessageParser parser = new MessageParser(main.tableModel);
        if (args.length  == 1) {
            String id = "WintexAnalysis";
            IMqttClient subscriber = new MqttClient(args[0], id);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            subscriber.connect(options);
    
            subscriber.subscribe("alarm/logs", new IMqttMessageListener() {
    
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    parser.parse(message);
                }
    
            });
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String op = null, line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(">")) {
                    op = "Write";
                } else if (line.startsWith("<")) {
                    op = "Read";
                } else if (op != null) {
                    parser.parse(op, line);
                }
            }
        }
    }
}
