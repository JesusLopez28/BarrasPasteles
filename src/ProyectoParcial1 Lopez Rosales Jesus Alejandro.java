import javax.swing.*;

class BarrasPasteles extends JFrame {

    public BarrasPasteles() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setLocationRelativeTo(null);
        setSize(800, 600);
        setResizable(false);
        setTitle("Barras y Pasteles");
    }

    public static void main(String[] args) {
        BarrasPasteles ventana = new BarrasPasteles();
        ventana.setVisible(true);
    }
}
