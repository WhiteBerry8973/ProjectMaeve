package Gui.AdminGui;

import Gui.MainGui.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;

public class AdminCatalogPanel extends JPanel {

    private final MaeveCoffeeUI ui;

    private final JComboBox<MenuCatalogPanel.Catalog> cbCatalog;
    private final JTable table;
    private final Model model;

    public AdminCatalogPanel(MaeveCoffeeUI ui) {
        this.ui = ui;

        setLayout(new BorderLayout());
        setOpaque(false);

        // ==== HEADER ====
        JLabel title = new JLabel("ADMIN CATALOG", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(12, 0, 8, 0));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        top.setOpaque(false);
        cbCatalog = new JComboBox<>(MenuCatalogPanel.Catalog.values());
        JButton btnLoad = new JButton("Load");
        JButton btnSave = new JButton("Save");
        btnLoad.addActionListener(e -> load());
        btnSave.addActionListener(e -> save());

        top.add(new JLabel("Catalog:"));
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> ui.show(MenuCatalogPanel.COFFEE));

        top.add(cbCatalog);
        top.add(btnLoad);
        top.add(btnSave);
        top.add(btnBack);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(top, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        model = new Model();
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        load();
    }

    private void load() {
        MenuCatalogPanel.Catalog cat = (MenuCatalogPanel.Catalog) cbCatalog.getSelectedItem();
        if (cat == null)
            return;

        java.util.List<MaeveCoffeeUI.MenuDrink> items = ui.getMenuByCatalog(cat);
        Map<String, Boolean> stockMap = readStockMap(ui.getMenuCsvPath(cat));

        java.util.List<Row> rows = new ArrayList<>();
        for (MaeveCoffeeUI.MenuDrink d : items) {
            Row r = new Row();
            r.name = d.name;
            Boolean in = stockMap.get(d.name);
            r.inStock = (in == null) ? Boolean.TRUE : in;
            rows.add(r);
        }
        model.setData(rows);
    }

    private void save() {
        MenuCatalogPanel.Catalog cat = (MenuCatalogPanel.Catalog) cbCatalog.getSelectedItem();
        if (cat == null)
            return;

        Map<String, Boolean> out = new LinkedHashMap<>();
        for (Row r : model.getData())
            out.put(r.name, r.inStock);

        if (writeStockMap(ui.getMenuCsvPath(cat), out)) {
            JOptionPane.showMessageDialog(this, "Saved stock to CSV for " + cat);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===== HELPER =====
    private static Map<String, Boolean> readStockMap(String filePath) {
        Map<String, Boolean> out = new LinkedHashMap<>();
        if (filePath == null)
            return out;
        Path p = Paths.get(filePath);
        if (!Files.exists(p))
            return out;

        try (BufferedReader br = Files.newBufferedReader(p)) {
            String first = br.readLine();
            if (first == null)
                return out;

            String[] cols = split(first);
            boolean hasHeader = hasName(cols);
            int nameIdx = hasHeader ? indexOf(cols, "name") : 0;
            int stockIdx = hasHeader ? indexOf(cols, "stock") : -1;

            if (!hasHeader)
                return out; // ไม่มี header = ยังไม่มีstock

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#"))
                    continue;
                String[] f = split(line);
                String name = getSafe(f, nameIdx);
                if (name.isEmpty())
                    continue;
                boolean in = stockIdx >= 0 && stockIdx < f.length ? parseStock(getSafe(f, stockIdx)) : true;
                out.put(name, in);
            }
        } catch (IOException ignored) {
        }
        return out;
    }

    private static boolean writeStockMap(String filePath, Map<String, Boolean> stockMap) {
        if (filePath == null) {
            System.err.println("[ADMIN] filePath is null");
            return false;
        }
        Path p = Paths.get(filePath);
        if (!Files.exists(p)) {
            System.err.println("[ADMIN] CSV not found: " + p.toAbsolutePath());
            return false;
        }
        try {
            List<String> lines = Files.readAllLines(p);
            if (lines.isEmpty()) {
                System.err.println("[ADMIN] CSV empty");
                return false;
            }

            String first = lines.get(0);
            String[] cols = split(first);
            boolean hasHeader = hasName(cols);

            String[] header;
            int stockIdx;

            if (hasHeader) {
                header = ensureStockHeader(cols);
                stockIdx = indexOf(header, "stock");
            } else {
                header = new String[] { "name", "hotPrice", "icedPrice", "icedAvailable", "shotPrice", "imagePath",
                        "stock" };
                stockIdx = header.length - 1;
            }

            List<String[]> rows = new ArrayList<>();
            for (int i = hasHeader ? 1 : 0; i < lines.size(); i++) {
                String[] f = split(lines.get(i));
                if (f.length < header.length) {
                    String[] g = new String[header.length];
                    for (int k = 0; k < g.length; k++)
                        g[k] = (k < f.length ? f[k] : "");
                    f = g;
                }
                String name = hasHeader ? getSafe(f, indexOf(cols, "name")) : getSafe(f, 0);
                Boolean in = stockMap.get(name);
                f[stockIdx] = (in != null && in) ? "1" : "0";
                rows.add(f);
            }

            if (p.getParent() != null)
                Files.createDirectories(p.getParent());
            try (BufferedWriter bw = Files.newBufferedWriter(p)) {
                bw.write(join(header));
                bw.newLine();
                for (String[] f : rows) {
                    bw.write(join(f));
                    bw.newLine();
                }
            }
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static String[] split(String s) {
        return s == null ? new String[0] : s.split(",", -1);
    }

    private static boolean hasName(String[] cols) {
        for (String c : cols)
            if ("name".equalsIgnoreCase(c == null ? "" : c.trim()))
                return true;
        return false;
    }

    private static int indexOf(String[] arr, String key) {
        for (int i = 0; i < arr.length; i++)
            if (key.equalsIgnoreCase(arr[i] == null ? "" : arr[i].trim()))
                return i;
        return -1;
    }

    private static String getSafe(String[] a, int i) {
        return (i >= 0 && i < a.length) ? (a[i] == null ? "" : a[i].trim()) : "";
    }

    private static boolean parseStock(String s) {
        String t = (s == null ? "" : s.trim().toLowerCase());
        if (t.equals("1") || t.equals("true") || t.equals("yes"))
            return true;
        if (t.equals("0") || t.equals("false") || t.equals("no"))
            return false;
        return true;
    }

    private static String join(String[] f) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < f.length; i++) {
            if (i > 0)
                sb.append(',');
            sb.append(f[i] == null ? "" : f[i]);
        }
        return sb.toString();
    }

    private static String[] ensureStockHeader(String[] cols) {
        for (String c : cols)
            if (c != null && "stock".equalsIgnoreCase(c.trim()))
                return cols;
        String[] h = java.util.Arrays.copyOf(cols, cols.length + 1);
        h[h.length - 1] = "stock";
        return h;
    }

    private static class Row {
        String name;
        boolean inStock;
    }

    private static class Model extends AbstractTableModel {
        private final String[] cols = { "name", "in_stock (tick = 1)" };
        private java.util.List<Row> data = new ArrayList<>();

        void setData(java.util.List<Row> rows) {
            data = new ArrayList<>(rows);
            fireTableDataChanged();
        }

        java.util.List<Row> getData() {
            return data;
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return cols.length;
        }

        @Override
        public String getColumnName(int c) {
            return cols[c];
        }

        @Override
        public Class<?> getColumnClass(int c) {
            return c == 1 ? Boolean.class : String.class;
        }

        @Override
        public boolean isCellEditable(int r, int c) {
            return c == 1;
        }

        @Override
        public Object getValueAt(int r, int c) {
            Row x = data.get(r);
            return c == 0 ? x.name : x.inStock;
        }

        @Override
        public void setValueAt(Object v, int r, int c) {
            if (c != 1)
                return;
            Row x = data.get(r);
            x.inStock = (v instanceof Boolean) ? ((Boolean) v) : "true".equalsIgnoreCase(String.valueOf(v));
            fireTableCellUpdated(r, c);
        }
    }
}