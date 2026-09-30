import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main extends JFrame {
        private GalaxyPanel galaxyPanel;
        private Random random = new Random();

        public Main() {
                setTitle("Meteor Collision");
                setSize(1000, 700);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setLocationRelativeTo(null);

                galaxyPanel = new GalaxyPanel();
                add(galaxyPanel);
                setVisible(true);
                createMeteors();
        }

        private void createMeteors() {
                int numberMeteor;
                while (true) {
                        String input = JOptionPane.showInputDialog(this, "Please enter numbers.");
                        if (input == null) {
                                System.exit(0);
                                return;
                        }
                        try {
                                numberMeteor = Integer.parseInt(input);
                                if (numberMeteor > 0) {
                                        break;
                                }
                                JOptionPane.showMessageDialog(this, "number greater than 0.");
                        } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(this, "Please enter a number.");
                        }
                }
                for (int i = 0; i < numberMeteor; i++) {
                        int x;
                        int y;
                        int attempts = 0;
                        do {
                                x = 50 + random.nextInt(Math.max(1, galaxyPanel.getWidth() - 100));
                                y = 50 + random.nextInt(Math.max(1, galaxyPanel.getHeight() - 100));
                                attempts++;

                        } while (galaxyPanelHasMeteorNear(x, y) && attempts < 100);
                        int imageIndex = random.nextInt(10);
                        Meteor meteor = new Meteor(x, y, galaxyPanel, galaxyPanel.getMeteorImage(imageIndex),
                                        galaxyPanel.getBombImage());

                        galaxyPanel.addMeteor(meteor);
                        meteor.start();
                }
        }

        private boolean galaxyPanelHasMeteorNear(int x, int y) {
                for (Meteor meteor : galaxyPanel.getMeteors()) {
                        int centerX = meteor.getX() + meteor.getSize() / 2;
                        int centerY = meteor.getY() + meteor.getSize() / 2;

                        int dx = x - centerX;
                        int dy = y - centerY;

                        double distance = Math.sqrt(dx * dx + dy * dy);
                        if (distance < 70) {
                                return true;
                        }
                }

                return false;
        }

        public static void main(String[] args) {
                new Main();
        }
}