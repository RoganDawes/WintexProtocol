package net.za.dawes.wintexprotocol.wintexanalysis;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;

class RowHighlighter implements TableModelListener, ActionListener {
    
    private static int STEPS = 50;
    private static int PERIOD = 2000;
    
    private Map<Integer, Integer> rowHighlights = new HashMap<>();
    private JTable table;
    
    public RowHighlighter(JTable table) {
        this.table = table;
        table.getModel().addTableModelListener(this);
        Timer timer = new Timer(PERIOD/STEPS, this);
        timer.start();
    }
    
    @Override
    public void tableChanged(TableModelEvent e) {
        int first = e.getFirstRow();
        int last = e.getLastRow();
        for (int i=first; i<=last; i++) {
            rowHighlights.put(i, STEPS);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Iterator<Map.Entry<Integer, Integer>> it = rowHighlights.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = it.next();
            int v = entry.getValue();
            if (v > 1) {
                entry.setValue(v - 1);
            } else {
                it.remove();
            }
        }
        table.repaint();
    }
    
    public TableCellRenderer wrap(TableCellRenderer delegate) {
        return new HighlightingTableCellRenderer(rowHighlights, delegate);
    }
    
    private static class HighlightingTableCellRenderer implements TableCellRenderer {

        private Map<Integer, Integer> rowMap;
        private TableCellRenderer delegate;
        
        public HighlightingTableCellRenderer(Map<Integer, Integer> rowMap, TableCellRenderer delegate) {
            this.rowMap = rowMap;
            this.delegate = delegate;
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            Component c = delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            Integer v = rowMap.get(row);
            if (v != null) {
                Color background = c.getBackground();
                Color highlight = getColor(v, Color.PINK, background);
                c.setBackground(highlight);
            }
            return c;
        }
        
        private Color getColor(int n, Color from, Color to) {
            float p = ((float)n)/STEPS;
            
            return new Color((int)(from.getRed() * p + to.getRed() * (1-p)),
                    (int)(from.getGreen() * p + to.getGreen() * (1-p)),
                    (int)(from.getBlue() * p + to.getBlue() * (1-p)));
        }
        
    }

}