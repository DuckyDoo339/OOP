import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

public class Meteor extends Thread {

    private volatile int x;
    private volatile int y;

    private int dx;
    private int dy;

    private int speed;

    private final int size = 50;

    private volatile boolean alive = true;
    private volatile boolean exploding = false;
    private final GalaxyPanel panel;
    private final Image meteorImage;
    private final Image bombImage;

    private static final Random random = new Random();

    public Meteor(int x, int y, GalaxyPanel panel,
            Image meteorImage, Image bombImage) {

        this.x = x;
        this.y = y;

        this.panel = panel;

        this.meteorImage = meteorImage;
        this.bombImage = bombImage;

        // ความเร็วเริ่มต้นไม่เท่ากัน
        speed = 1 + random.nextInt(4);

        // สุ่มทิศทาง 8 แบบ
        int direction = random.nextInt(8);

        switch (direction) {
            case 0:
                dx = 1;
                dy = 0;
                break;

            case 1:
                dx = -1;
                dy = 0;
                break;

            case 2:
                dx = 0;
                dy = 1;
                break;

            case 3:
                dx = 0;
                dy = -1;
                break;

            case 4:
                dx = 1;
                dy = 1;
                break;

            case 5:
                dx = -1;
                dy = 1;
                break;

            case 6:
                dx = 1;
                dy = -1;
                break;

            default:
                dx = -1;
                dy = -1;
                break;
        }
    }

    @Override
    public void run() {

        while (alive) {
            move();
            panel.checkCollision(this);
            panel.repaint();
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                alive = false;
            }
        }

        // ถ้าระเบิด ให้แสดง bomb.gif
        if (exploding) {
            panel.repaint();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            exploding = false;
            panel.repaint();
        }

        panel.removeMeteor(this);
    }

    private void move() {
        x += dx * speed;
        y += dy * speed;
        checkWall();
    }

    private void checkWall() {

        int width = panel.getWidth();
        int height = panel.getHeight();

        if (width <= 0 || height <= 0) {
            return;
        }

        // ชนด้านซ้าย
        if (x <= 0 && dx < 0) {
            x = 0;
            dx = -dx;
            increaseSpeed();
        }

        // ชนด้านขวา
        else if (x + size >= width && dx > 0) {
            x = width - size;
            dx = -dx;
            increaseSpeed();
        }

        // ชนด้านบน
        if (y <= 0 && dy < 0) {
            y = 0;
            dy = -dy;
            increaseSpeed();
        }

        // ชนด้านล่าง
        else if (y + size >= height && dy > 0) {
            y = height - size;
            dy = -dy;
            increaseSpeed();
        }
    }

    private void increaseSpeed() {
        if (speed < 10) {
            speed++;
        }
    }

    public void explode() {
        exploding = true;
        alive = false;
        interrupt();
    }

    public void draw(Graphics g) {
        if (alive) {
            g.drawImage(meteorImage, x, y, size, size, panel);

        } else if (exploding) {
            g.drawImage(bombImage, x, y, size, size, panel);
        }
    }

    public boolean isAliveMeteor() {
        return alive;
    }

    public boolean isExploding() {
        return exploding;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }
}