import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.Timer;

public class Player {
    private int groundLevel = 500;
    private int x = 100; // ตำแหน่งเริ่มต้นของผู้เล่น
    private int y;
    // ความกว้างและความสูงของตัวละคร
    private int width, height;
    // รูปภาพตัวละคร
    private BufferedImage characterImage;
    private BufferedImage loadingImage; // รูป loading.png
    // ตัวแปรเก็บสถานะการกระโดด
    private boolean jumping = false;
    // สถานะการหมอบ
    private boolean crouching = false;
    // ความแรงในการกระโดด
    private int jumpStrength = 18;
    // จำนวนครั้งที่สามารถกระโดดได้สูงสุด
    private int maxJumps = 2;
    private int jumpCount = 0;
    // ค่าแรงโน้มถ่วง
    private int gravity = 1;
    // ความเร็วในแนวแกน Y
    private int velocityY = 0;
    // เก็บรายการกระสุนที่ยิงออกไป
    private ArrayList<Bullet> bullets;
    private int shotCount = 0; // จำนวนกระสุนที่ยิงไปแล้ว
    private boolean isLoading = false; // สถานะการแสดง loading
    private Timer cooldownTimer; // Timer สำหรับคูลดาวน์ 2 วินาที

    public Player(String character) {
        width = 50;
        height = 70;
        y = groundLevel - height;
        bullets = new ArrayList<>();
        System.out.println("Loading character: " + character);
        loadCharacterImage(character);
        loadLoadingImage(); // โหลดภาพ loading.png
    }

    // เมธอดสำหรับโหลดรูปภาพตัวละครตามตัวเลือกที่ผู้เล่นเลือก
    public void loadCharacterImage(String character) {
        try {
            switch (character) {
                case "alien":
                    characterImage = ImageIO.read(new File("src/Assets/alien.png"));
                    break;
                case "alien_green":
                    characterImage = ImageIO.read(new File("src/Assets/aliengreen.png"));
                    break;
                case "alien_red":
                    characterImage = ImageIO.read(new File("src/Assets/alienred.png"));
                    break;
                default:
                    characterImage = ImageIO.read(new File("src/Assets/alien.png"));
                    break;
            }
        } catch (IOException e) {
            System.out.println("Error loading character image: " + e.getMessage());
        }
    }

    // เมธอดสำหรับโหลดรูปภาพแสดงสถานะโหลด
    public void loadLoadingImage() {
        try {
            loadingImage = ImageIO.read(new File("/src/Assets/loading.png"));
        } catch (IOException e) {
            System.out.println("Error loading loading image: " + e.getMessage());
        }
    }

    // เมธอดสำหรับการเคลื่อนที่ของตัวละคร
    public void move(int speed) {
        x += speed;

        // จัดการการกระโดด
        if (jumping) {
            y += velocityY;
            velocityY += gravity;

            // ตรวจสอบการชนกับพื้น
            if (y >= groundLevel - height) {
                y = groundLevel - height;
                jumping = false;
                velocityY = 0;
                jumpCount = 0;
            }
        }

        // เคลื่อนที่กระสุน
        for (Iterator<Bullet> itr = bullets.iterator(); itr.hasNext();) {
            Bullet bullet = itr.next();
            bullet.move();
            if (bullet.isOffScreen()) {
                itr.remove();
            }
        }
    }

    // เมธอดจัดการการกดปุ่มของผู้เล่น
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            if (jumpCount < maxJumps) {
                jumping = true;
                velocityY = -jumpStrength;
                jumpCount++;
            }
        } else if (key == KeyEvent.VK_ENTER && !isLoading) {
            shoot(); // เรียกฟังก์ชันยิง
        } else if (key == KeyEvent.VK_DOWN) {
            crouching = true;
        }
    }

    // เมธอดจัดการเมื่อปล่อยปุ่มที่กด
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_DOWN) {
            crouching = false;
        }
    }

    // เมธอดสำหรับวาดตัวละครและองค์ประกอบอื่นๆ
    public void paint(Graphics g) {
        if (crouching) {
            g.drawImage(characterImage, x, y + height / 2, width, height / 2, null);
        } else {
            g.drawImage(characterImage, x, y, width, height, null);
        }

        // วาดกระสุนของผู้เล่น
        for (Bullet bullet : bullets) {
            bullet.paint(g);
        }

        // แสดง loading.png ถ้าอยู่ในสถานะกำลังโหลด
        if (isLoading && loadingImage != null) {
            g.drawImage(loadingImage, x, y - 50, 50, 50, null);
        }
    }

    // เมธอดสำหรับรับพื้นที่การชนของตัวละคร
    public Rectangle getBounds() {
        if (crouching) {
            return new Rectangle(x, y + height / 2, width, height / 2);
        } else {
            return new Rectangle(x, y, width, height);
        }
    }

    // เมธอดสำหรับการยิงกระสุน
    public void shoot() {
        if (shotCount < 4) {
            BufferedImage bulletImage = null;
            try {
                bulletImage = ImageIO.read(new File("src/Assets/ammunition.png"));
            } catch (IOException e) {
                System.out.println("Error loading bullet image: " + e.getMessage());
            }

            int bulletWidth = 20;
            int bulletHeight = 15;
            bullets.add(new Bullet(x + width, y + height / 2, 10, bulletWidth, bulletHeight, bulletImage));
            shotCount++;

            if (shotCount == 4) {
                startCooldown(); // เริ่มการคูลดาวน์เมื่อยิงครบ 2 นัด
            }
        }
    }

    // เมธอดสำหรับเริ่มการนับเวลาคูลดาวน์หลังยิงครบ
    private void startCooldown() {
        isLoading = true; // เปิดสถานะการโหลด
        cooldownTimer = new Timer(2000, new ActionListener() { // ตั้งเวลา 2 วินาที
            @Override
            public void actionPerformed(ActionEvent e) {
                isLoading = false; // ปิดสถานะการโหลด
                shotCount = 0; // รีเซ็ตจำนวนการยิง
                cooldownTimer.stop(); // หยุดตัวจับเวลา
            }
        });
        cooldownTimer.start();
    }

    // เมธอดสำหรับเรียกดูรายการกระสุนทั้งหมด
    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    // เมธอดสำหรับดึงค่าตำแหน่ง X ของตัวละคร
    public int getX() {
        return x;
    }

    // เมธอดสำหรับกำหนดค่าตำแหน่ง X ของตัวละคร
    public void setX(int x) {
        this.x = x;
    }
}
