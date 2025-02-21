import java.awt.*; // นำเข้าคลาสจาก java.awt สำหรับการทำงานกับกราฟิกและค่าสี
import java.awt.image.BufferedImage; // นำเข้าคลาส BufferedImage สำหรับจัดการรูปภาพในหน่วยความจำ
import java.io.File; // นำเข้าคลาส ImageIO สำหรับอ่านและเขียนไฟล์รูปภาพ
import java.io.IOException; // นำเข้าคลาส File สำหรับจัดการไฟล์ในระบบ
import javax.imageio.ImageIO; // นำเข้าคลาส IOException สำหรับจัดการข้อผิดพลาดในการอ่านไฟล์

// คลาสสำหรับจัดการสิ่งกีดขวางในเกม
public class Obstacle { // ประกาศคลาส Obstacle เพื่อแทนสิ่งกีดขวางในเกม
    private int x, y, width, height; // ประกาศตัวแปรส่วนตัวสำหรับตำแหน่ง (x,y) และขนาด (width, height) ของอุปสรรค
    private BufferedImage obstacleImage; // ประกาศตัวแปรสำหรับเก็บภาพของอุปสรรค

    // Constructor ที่รับตำแหน่ง x, y, ขนาด width, height และประเภทของอุปสรรค (type)
    // คอนสตรักเตอร์สำหรับสร้างสิ่งกีดขวางใหม่
    public Obstacle(int x, int y, int width, int height, String type) { // คอนสตรักเตอร์รับพารามิเตอร์สำหรับกำหนดตำแหน่ง, ขนาด และประเภทของอุปสรรค
        this.x = x;       // กำหนดค่า x ของอุปสรรคจากพารามิเตอร์ที่ส่งเข้ามา
        this.y = y;       // กำหนดค่า y ของอุปสรรคจากพารามิเตอร์ที่ส่งเข้ามา
        this.width = width;   // กำหนดความกว้างของอุปสรรคจากพารามิเตอร์ที่ส่งเข้ามา
        this.height = height; // กำหนดความสูงของอุปสรรคจากพารามิเตอร์ที่ส่งเข้ามา

        // โหลดภาพอุปสรรคตามชนิด (type)
        try { // เริ่มบล็อก try เพื่อพยายามโหลดภาพ
            obstacleImage = ImageIO.read(new File("src/Assets/" + type + ".png")); // โหลดภาพจากไฟล์ที่อยู่ใน "src/Assets/" ตามประเภทที่ระบุ (type)
        } catch (IOException e) { // จับข้อผิดพลาดหากไม่สามารถโหลดภาพได้
            System.out.println("Error loading obstacle image for type: " + type); // แสดงข้อความข้อผิดพลาดที่คอนโซล
        }
    }

    // เมธอด move รับพารามิเตอร์ speedMultiplier สำหรับปรับความเร็ว
    // เมธอดสำหรับการเคลื่อนที่ของสิ่งกีดขวาง
    public void move(int speedMultiplier) { // เมธอดสำหรับปรับตำแหน่งอุปสรรคเพื่อให้เคลื่อนที่
        x -= 2 * speedMultiplier;  // ลดค่า x โดยคูณด้วย 2 และ speedMultiplier เพื่อเคลื่อนที่ไปทางซ้าย
    }

    // เมธอด paint ใช้วาดอุปสรรคลงบนหน้าจอ
    // เมธอดสำหรับวาดสิ่งกีดขวางบนหน้าจอ
    public void paint(Graphics g) { // เมธอดสำหรับวาดอุปสรรคด้วยกราฟิกที่ส่งเข้ามา
        if (obstacleImage != null) { // ตรวจสอบว่าภาพอุปสรรคถูกโหลดสำเร็จหรือไม่
            g.drawImage(obstacleImage, x, y, width, height, null); // วาดภาพอุปสรรคที่ตำแหน่ง (x,y) ด้วยขนาดที่กำหนด
        } else { // ถ้าไม่สามารถโหลดภาพได้
            g.setColor(Color.RED); // ตั้งค่าสีสำหรับวาดเป็นสีแดง
            g.fillRect(x, y, width, height);  // วาดสี่เหลี่ยมสีแดงแทนอุปสรรคที่ไม่สามารถโหลดภาพได้
        }
    }

    // คืนค่า Rectangle ที่ครอบอุปสรรค เพื่อใช้ในการตรวจสอบการชน (collision)
    // เมธอดสำหรับรับพื้นที่การชนของสิ่งกีดขวาง
    public Rectangle getBounds() { // เมธอดสำหรับสร้างและคืนค่า Rectangle ที่ครอบอุปสรรค
        return new Rectangle(x, y, width, height); // สร้าง Rectangle ใหม่ที่มีตำแหน่งและขนาดเดียวกับอุปสรรค
    }

    // Getter สำหรับตำแหน่ง x ของอุปสรรค
    public int getX() { // เมธอดสำหรับดึงค่า x ของอุปสรรค
        return x; // คืนค่า x ของอุปสรรค
    }

    // Setter สำหรับตำแหน่ง x ของอุปสรรค
    public void setX(int x) { // เมธอดสำหรับกำหนดค่า x ใหม่ให้กับอุปสรรค
        this.x = x; // กำหนดค่า x ของอุปสรรคเป็นค่าที่ส่งเข้ามา
    }
}