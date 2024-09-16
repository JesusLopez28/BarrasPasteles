import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class BarrasPasteles extends JFrame {

    protected DefaultTableModel modeloTabla;
    protected JPanel panelGrafica;
    protected JTable tablaDatos;
    protected JButton btnAgregarFila;
    protected JButton btnEliminarFila;
    protected JComboBox<String> comboTipoGrafica;
    protected static final int MAX_FILAS = 5;
    protected static final int MIN_FILAS = 3;
    protected BufferedImage imageBackground1;
    protected BufferedImage imageBackground2;

    public BarrasPasteles() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setLocationRelativeTo(null);
        setSize(800, 600);
        setResizable(false);
        setTitle("Barras y Pasteles");

        try {
            imageBackground1 = ImageIO.read(new File("src/img/fondo1.jpg"));
            imageBackground2 = ImageIO.read(new File("src/img/fondo2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(new GridLayout(1, 2));

        JPanel panelCaptura = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imageBackground1 != null) {
                    g.drawImage(imageBackground1, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panelCaptura.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Captura de Datos");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelCaptura.add(lblTitulo, gbc);

        modeloTabla = new DefaultTableModel(new Object[][]{
                {"Item 1", "1"},
                {"Item 2", "1"},
                {"Item 3", "1"}
        }, new String[]{"Ítem", "Valor"});

        tablaDatos = new JTable(modeloTabla);
        tablaDatos.setPreferredScrollableViewportSize(new Dimension(400, 150));
        tablaDatos.setFillsViewportHeight(true);
        tablaDatos.getTableHeader().setReorderingAllowed(false);
        tablaDatos.getTableHeader().setResizingAllowed(false);

        JScrollPane scrollTabla = new JScrollPane(tablaDatos);
        scrollTabla.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelCaptura.add(scrollTabla, gbc);

        btnAgregarFila = new JButton("Agregar Fila");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelCaptura.add(btnAgregarFila, gbc);

        btnEliminarFila = new JButton("Eliminar Fila");
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        panelCaptura.add(btnEliminarFila, gbc);

        JLabel lblTipoGrafica = new JLabel("Tipo de Gráfica:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        panelCaptura.add(lblTipoGrafica, gbc);

        String[] tiposGrafica = {"Barras", "Pastel"};
        comboTipoGrafica = new JComboBox<>(tiposGrafica);
        gbc.gridx = 1;
        gbc.gridy = 5;
        panelCaptura.add(comboTipoGrafica, gbc);

        JButton btnGenerarGrafica = new JButton("Generar Gráfica");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panelCaptura.add(btnGenerarGrafica, gbc);

        panelGrafica = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imageBackground2 != null) {
                    g.drawImage(imageBackground2, 0, 0, getWidth(), getHeight(), this);
                }
                dibujarGrafica(g);
            }
        };
        panelGrafica.setBackground(Color.WHITE);

        add(panelCaptura);
        add(panelGrafica);

        btnAgregarFila.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (modeloTabla.getRowCount() < MAX_FILAS) {
                    modeloTabla.addRow(new Object[]{"Item " + (modeloTabla.getRowCount() + 1), "1"});
                } else {
                    JOptionPane.showMessageDialog(null, "No puedes agregar más de 5 registros.");
                }
            }
        });

        btnEliminarFila.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (modeloTabla.getRowCount() > MIN_FILAS) {
                    modeloTabla.removeRow(modeloTabla.getRowCount() - 1);
                } else {
                    JOptionPane.showMessageDialog(null, "Debes tener al menos 3 registros.");
                }
            }
        });

        btnGenerarGrafica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validarDatos()) {
                    panelGrafica.repaint();
                }
            }
        });
    }

    private boolean validarDatos() {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String nombre = (String) modeloTabla.getValueAt(i, 0);
            String valor = (String) modeloTabla.getValueAt(i, 1);

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre del ítem en la fila " + (i + 1) + " no puede estar vacío.");
                return false;
            }

            try {
                Double.parseDouble(valor);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El valor en la fila " + (i + 1) + " debe ser un número.");
                return false;
            }
        }
        return true;
    }

    private void dibujarGrafica(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int width = panelGrafica.getWidth();
        int height = panelGrafica.getHeight();
        int margin = 50;

        int rowCount = modeloTabla.getRowCount();
        String[] items = new String[rowCount];
        double[] valores = new double[rowCount];
        double total = 0;

        for (int i = 0; i < rowCount; i++) {
            items[i] = (String) modeloTabla.getValueAt(i, 0);
            valores[i] = Double.parseDouble((String) modeloTabla.getValueAt(i, 1));
            total += valores[i];
        }

        String tipoGrafica = (String) comboTipoGrafica.getSelectedItem();

        if ("Barras".equals(tipoGrafica)) {
            int barWidth = (width - 2 * margin) / rowCount;
            double maxValor = valores[0];
            for (double valor : valores) {
                if (valor > maxValor) maxValor = valor;
            }

            for (int i = 0; i < rowCount; i++) {
                int barHeight = (int) ((valores[i] / maxValor) * (height - 2 * margin));
                int x = margin + i * barWidth;
                int y = height - margin - barHeight - 10;

                g2d.setColor(new Color((int) (Math.random() * 0x1000000)));
                g2d.fillRect(x, y, barWidth - 5, barHeight);

                g2d.fillRect(x + barWidth / 2 - 25, height - margin, 10, 10);
                g2d.setColor(Color.BLACK);
                g2d.drawString(items[i], x + barWidth / 2 - 10, height - margin + 10);

                g2d.setColor(Color.BLACK);
                g2d.drawString(String.valueOf(valores[i]), x + barWidth / 2 - 10, y - 5);
            }
        } else if ("Pastel".equals(tipoGrafica)) {
            int diameter = Math.min(width, height) - 2 * margin;
            int x = (width - diameter) / 2;
            int y = (height - diameter) / 2;

            double startAngle = 0;
            for (int i = 0; i < rowCount; i++) {
                double arcAngle = 360 * (valores[i] / total);
                double midAngle = startAngle + arcAngle / 2;
                g2d.setColor(new Color((int) (Math.random() * 0x1000000)));
                g2d.fill(new Arc2D.Double(x, y, diameter, diameter, startAngle, arcAngle, Arc2D.PIE));

                g2d.fillRect(margin + i * 65, height - margin, 10, 10);
                g2d.setColor(Color.BLACK);
                g2d.drawString(items[i], margin + i * 65 + 15, height - margin + 10);

                g2d.setColor(Color.BLACK);
                g2d.drawString(String.valueOf(valores[i]), margin + i * 65 + 15, height - margin + 25);

                startAngle += arcAngle;
            }
        }
    }

    public static void main(String[] args) {
        BarrasPasteles ventana = new BarrasPasteles();
        ventana.setVisible(true);
    }
}
