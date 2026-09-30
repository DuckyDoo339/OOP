import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GalaxyPanel extends JPanel {
    private final CopyOnWriteArrayList<Meteor> meteors;
    private final Image[] meteorImages;
    private final Image bombImage;

    // ใช้เป็นตัวล็อกตอนชน
    private final Object collisionLock = new Object();
    private final Random random = new Random();

    public GalaxyPanel() {
        meteors = new CopyOnWriteArrayList<>();
        setBackground(Color.BLACK);
        meteorImages = new Image[10];
        for (int i = 0; i < 10; i++) {
            meteorImages[i] = new ImageIcon("images/" + (i + 1) + ".png").getImage();
        }

        bombImage = new ImageIcon("images/bomb.gif").getImage();
    }

    public void addMeteor(Meteor meteor) {
        meteors.add(meteor);
    }

    public void removeMeteor(Meteor meteor) {
        meteors.remove(meteor);
    }

    public List<Meteor> getMeteors() {
        return meteors;
    }

    public Image getMeteorImage(int index) {
        return meteorImages[index];
    }

    public Image getBombImage() {
        return bombImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Meteor meteor : meteors) {
            if (meteor.isAliveMeteor() || meteor.isExploding()) {
                meteor.draw(g);
            }
        }
    }

    public void checkCollision(Meteor current) {
        // ทำให้การตรวจชน + เลือกลูกที่จะระเบิด
        synchronized (collisionLock) {
            if (!current.isAliveMeteor()) {
                return;
            }

            for (Meteor other : meteors) {
                if (current == other) {
                    continue;
                }
                if (!other.isAliveMeteor()) {
                    continue;
                }

                int centerX1 = current.getX() + current.getSize() / 2;
                int centerY1 = current.getY() + current.getSize() / 2;
                int centerX2 = other.getX() + other.getSize() / 2;
                int centerY2 = other.getY() + other.getSize() / 2;

                int dx = centerX1 - centerX2;
                int dy = centerY1 - centerY2;
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance < current.getSize()) {
                    if (current.isAliveMeteor() && other.isAliveMeteor()) {

                        // สุ่มให้ลูกใดลูกหนึ่งระเบิด ปละอีกลูกไม่เปลี่ยนทิศ
                        if (random.nextBoolean()) {
                            current.explode();
                        } else {
                            other.explode();
                        }
                        return;
                    }
                }
            }
        }
    }
}