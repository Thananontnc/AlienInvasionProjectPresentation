import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;  // เพิ่มบรรทัดนี้
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

// คลาสสำหรับจัดการทหารศัตรูในเกม
public class Soldier {
    private int x, y, width, height;
    private BufferedImage soldierImage;
    private ArrayList<Bullet> bullets;
    private int shootCooldown = 100; // ลดค่าคูลดาวน์เพื่อให้ยิงบ่อยขึ้น
    private int shootTimer = 0;

    // คอนสตรักเตอร์สำหรับสร้างทหารใหม่
    public Soldier(int x, int width, int height) {
        this.x = x;
        this.y = 500 - height;
        this.width = width;
        this.height = height;
        bullets = new ArrayList<>();
        loadSoldierImage();
    }

    // เมธอดสำหรับโหลดรูปภาพของทหาร
    private void loadSoldierImage() {
        try {
            soldierImage = ImageIO.read(new File("src/Assets/soldier.png"));
        } catch (IOException e) {
            System.out.println("Error loading soldier image: " + e.getMessage());
        }
    }

    // เมธอดสำหรับการเคลื่อนที่และการยิงของทหาร
    public void move(BufferedImage bulletImage, int speedMultiplier) {
        x -= speedMultiplier;

        shootTimer++;
        if (shootTimer >= shootCooldown) {
            shootTimer = 0;
            shoot(bulletImage);
        }

        // เคลื่อนที่กระสุนของทหาร
        for (Iterator<Bullet> itr = bullets.iterator(); itr.hasNext();) {
            Bullet bullet = itr.next();
            bullet.move();
            if (bullet.isOffScreen()) {
                itr.remove();
            }
        }
    }

    // เมธอดสำหรับการยิงกระสุนของทหาร
    private void shoot(BufferedImage bulletImage) {
        int bulletWidth = 25;
        int bulletHeight = 20;
        bullets.add(new Bullet(x, y + height / 2, -10, bulletWidth, bulletHeight, bulletImage));
    }

    // เมธอดสำหรับวาดทหารและกระสุนบนหน้าจอ
    public void paint(Graphics g) {
        if (soldierImage != null) {
            g.drawImage(soldierImage, x, y, width, height, null);
        } else {
            g.setColor(Color.GRAY);
            g.fillRect(x, y, width, height);
        }

        // วาดกระสุนของทหาร
        for (Bullet bullet : bullets) {
            bullet.paint(g);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public int getX() {
        return x;
    }
}
