package net.za.dawes.wintexprotocol.wintexanalysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.swing.table.AbstractTableModel;

import net.za.dawes.wintexprotocol.Panel;

public class WintexTableModel extends AbstractTableModel {
    
    private Panel panel;
    
    public WintexTableModel(Panel panel) {
        this.panel = panel;
    }
    
    @Override
    public String getColumnName(int column) {
        return new String[] { "Date", "Request", "Response", "Count", "Comment" }[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return new Class[] { Date.class, String.class, String.class, Number.class,
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
            return panel.toString(record.request);
        case 2:
            return panel.toString(record.response);
        case 3:
            return record.count;
        case 4:
            return record.comment;
        }
        return null;
    }

}