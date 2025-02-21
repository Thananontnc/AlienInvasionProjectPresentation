import java.awt.*; // นำเข้าคลาสจากแพ็คเกจ javax.swing สำหรับสร้างส่วนประกอบ GUI
import javax.swing.*;      // นำเข้าคลาสจากแพ็คเกจ java.awt สำหรับการจัดการกราฟิกและ layout

public class AlienInvasionHome extends JFrame { // ประกาศคลาส AlienInvasionHome ที่สืบทอดคุณสมบัติจาก JFrame เพื่อสร้างหน้าต่างหลักของโปรแกรม

    public AlienInvasionHome() { // คอนสตรัคเตอร์สำหรับคลาส AlienInvasionHome
        setTitle("Alien Invasion Home"); // ตั้งชื่อหน้าต่างว่า "Alien Invasion Home"
        setSize(800, 600); // กำหนดขนาดหน้าต่างกว้าง 800 พิกเซลและสูง 600 พิกเซล
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // กำหนดให้โปรแกรมปิดเมื่อหน้าต่างถูกปิด
        setLocationRelativeTo(null); // จัดตำแหน่งหน้าต่างให้อยู่ตรงกลางหน้าจอ

        // สร้าง JPanel ที่กำหนดพื้นหลัง
        JPanel backgroundPanel = new JPanel() { // สร้างอ็อบเจ็กต์ JPanel แบบนิรนาม (anonymous class)
            @Override
            protected void paintComponent(Graphics g) { // ทับเมธอด paintComponent เพื่อวาดกราฟิกบน JPanel
                super.paintComponent(g); // เรียกใช้เมธอด paintComponent ของคลาสพ่อเพื่อวาดพื้นฐานก่อน
                // โหลดและวาดรูป backgroundhome.jpg
                ImageIcon backgroundImage = loadImage("/Assets/backgroundhome.jpg", getWidth(), getHeight()); // โหลดภาพพื้นหลังและปรับขนาดให้เท่ากับความกว้างและความสูงของ JPanel
                if (backgroundImage != null) { // ตรวจสอบว่าภาพถูกโหลดสำเร็จหรือไม่
                    g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this); // วาดภาพพื้นหลังลงบน JPanel โดยเริ่มที่ตำแหน่ง (0,0)
                }
            }
        };
        backgroundPanel.setLayout(null);  // กำหนด layout ของ backgroundPanel เป็น null เพื่อจัดตำแหน่งคอมโพเนนต์แบบแมนนวล
        setContentPane(backgroundPanel);  // ตั้ง backgroundPanel เป็นเนื้อหาหลักของหน้าต่าง JFrame

        // ปรับขนาดและแสดงรูป title.png
        ImageIcon titleIcon = loadImage("/Assets/title.png", 400, 100); // โหลดภาพ title.png และปรับขนาดเป็น 400x100 พิกเซล
        if (titleIcon != null) { // ตรวจสอบว่าภาพถูกโหลดสำเร็จหรือไม่
            JLabel titleLabel = new JLabel(titleIcon); // สร้าง JLabel โดยใช้ titleIcon เป็นไอคอน
            titleLabel.setBounds(200, 50, 400, 100);  // กำหนดตำแหน่ง (x=200, y=50) และขนาด (ความกว้าง=400, ความสูง=100) ให้กับ titleLabel
            backgroundPanel.add(titleLabel); // เพิ่ม titleLabel ลงใน backgroundPanel
        }

        // ปุ่ม Start Game เปลี่ยนเป็นรูป start.png และปรับขนาด
        ImageIcon startIcon = loadImage("/Assets/start.png", 200, 120); // โหลดภาพ start.png และปรับขนาดเป็น 200x120 พิกเซล
        JButton startButton = new JButton(startIcon); // สร้างปุ่ม JButton โดยใช้ startIcon เป็นไอคอนของปุ่ม
        startButton.setBounds(300, 120, 200, 50);  // กำหนดตำแหน่ง (x=300, y=120) และขนาด (ความกว้าง=200, ความสูง=50) ให้กับ startButton
        startButton.setContentAreaFilled(false);   // ปิดการแสดงพื้นหลังของปุ่ม
        startButton.setBorderPainted(false);       // ปิดการแสดงขอบของปุ่ม
        startButton.setFocusPainted(false);        // ปิดการแสดงกรอบโฟกัสเมื่อปุ่มถูกเลือก
        backgroundPanel.add(startButton); // เพิ่ม startButton ลงใน backgroundPanel

        // ปุ่ม Character Selection เปลี่ยนเป็นรูป inventory.png และปรับขนาด
        ImageIcon characterIcon = loadImage("/Assets/inventory.png", 200, 80); // โหลดภาพ inventory.png และปรับขนาดเป็น 200x80 พิกเซล
        JButton characterButton = new JButton(characterIcon); // สร้างปุ่ม JButton โดยใช้ characterIcon เป็นไอคอนของปุ่ม
        characterButton.setBounds(300, 180, 200, 80);  // กำหนดตำแหน่ง (x=300, y=180) และขนาด (ความกว้าง=200, ความสูง=80) ให้กับ characterButton
        characterButton.setContentAreaFilled(false);   // ปิดการแสดงพื้นหลังของปุ่ม
        characterButton.setBorderPainted(false);       // ปิดการแสดงขอบของปุ่ม
        characterButton.setFocusPainted(false);        // ปิดการแสดงกรอบโฟกัสเมื่อปุ่มถูกเลือก
        backgroundPanel.add(characterButton); // เพิ่ม characterButton ลงใน backgroundPanel

        // ปุ่ม Shop เปลี่ยนเป็นรูป shophead.png และปรับขนาด
        ImageIcon shopIcon = loadImage("/Assets/shophead.png", 200, 90); // โหลดภาพ shophead.png และปรับขนาดเป็น 200x90 พิกเซล
        JButton shopButton = new JButton(shopIcon); // สร้างปุ่ม JButton โดยใช้ shopIcon เป็นไอคอนของปุ่ม
        shopButton.setBounds(300, 210, 200, 150);  // กำหนดตำแหน่ง (x=300, y=210) และขนาด (ความกว้าง=200, ความสูง=150) ให้กับ shopButton
        shopButton.setContentAreaFilled(false);   // ปิดการแสดงพื้นหลังของปุ่ม
        shopButton.setBorderPainted(false);       // ปิดการแสดงขอบของปุ่ม
        shopButton.setFocusPainted(false);        // ปิดการแสดงกรอบโฟกัสเมื่อปุ่มถูกเลือก
        backgroundPanel.add(shopButton); // เพิ่ม shopButton ลงใน backgroundPanel

        // กำหนด action listener สำหรับปุ่ม Start Game
        startButton.addActionListener(e -> { // เมื่อมีการคลิกที่ startButton
            dispose(); // ปิดหน้าต่าง AlienInvasionHome ปัจจุบัน
            JFrame gameFrame = new JFrame("Alien Invasion Game"); // สร้างหน้าต่างใหม่สำหรับเกม โดยตั้งชื่อว่า "Alien Invasion Game"
            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // กำหนดให้ปิดโปรแกรมเมื่อหน้าต่างเกมถูกปิด
            gameFrame.setSize(1024, 600); // กำหนดขนาดหน้าต่างเกมเป็น 1024x600 พิกเซล
            Game game = new Game(gameFrame); // สร้างอ็อบเจ็กต์เกม (สมมุติว่ามีคลาส Game อยู่แล้ว) โดยส่ง gameFrame เข้าไปในคอนสตรัคเตอร์
            gameFrame.add(game); // เพิ่มอ็อบเจ็กต์เกมลงในหน้าต่างเกม
            gameFrame.setVisible(true); // แสดงหน้าต่างเกมให้ผู้ใช้เห็น
        });

        // กำหนด action listener สำหรับปุ่ม Character Selection
        characterButton.addActionListener(e -> { // เมื่อมีการคลิกที่ characterButton
            dispose(); // ปิดหน้าต่าง AlienInvasionHome ปัจจุบัน
            JFrame characterFrame = new JFrame("Character Selection"); // สร้างหน้าต่างใหม่สำหรับการเลือกตัวละคร โดยตั้งชื่อว่า "Character Selection"
            characterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // กำหนดให้ปิดโปรแกรมเมื่อหน้าต่างถูกปิด
            characterFrame.setSize(800, 600); // กำหนดขนาดหน้าต่างเป็น 800x600 พิกเซล
            CharacterSelection characterSelection = new CharacterSelection(characterFrame); // สร้างอ็อบเจ็กต์สำหรับการเลือกตัวละคร (สมมุติว่ามีคลาส CharacterSelection อยู่แล้ว)
            characterFrame.add(characterSelection); // เพิ่มอ็อบเจ็กต์ characterSelection ลงในหน้าต่าง
            characterFrame.setVisible(true); // แสดงหน้าต่าง Character Selection ให้ผู้ใช้เห็น
        });

        // กำหนด action listener สำหรับปุ่ม Shop
        shopButton.addActionListener(e -> { // เมื่อมีการคลิกที่ shopButton
            dispose(); // ปิดหน้าต่าง AlienInvasionHome ปัจจุบัน
            JFrame shopFrame = new JFrame("Shop"); // สร้างหน้าต่างใหม่สำหรับร้านค้า โดยตั้งชื่อว่า "Shop"
            shopFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // กำหนดให้ปิดโปรแกรมเมื่อหน้าต่างถูกปิด
            shopFrame.setSize(800, 600); // กำหนดขนาดหน้าต่างเป็น 800x600 พิกเซล
            Shop shop = new Shop(shopFrame); // สร้างอ็อบเจ็กต์ Shop (สมมุติว่ามีคลาส Shop อยู่แล้ว)
            shopFrame.add(shop); // เพิ่มอ็อบเจ็กต์ shop ลงในหน้าต่าง
            shopFrame.setVisible(true); // แสดงหน้าต่าง Shop ให้ผู้ใช้เห็น
        });
    }

    // ฟังก์ชันช่วยในการโหลดภาพและปรับขนาด
    private ImageIcon loadImage(String path, int width, int height) { // เมธอด loadImage สำหรับโหลดภาพจาก path และปรับขนาดภาพตาม width และ height ที่กำหนด
        java.net.URL imgURL = getClass().getResource(path); // ค้นหา URL ของไฟล์ภาพตาม path ที่ระบุ
        if (imgURL != null) { // ถ้าไฟล์ภาพถูกพบ (ไม่เป็น null)
            ImageIcon icon = new ImageIcon(imgURL); // สร้างอ็อบเจ็กต์ ImageIcon จาก URL ที่ได้
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); // ปรับขนาดภาพโดยใช้วิธีการ SCALE_SMOOTH ให้ได้คุณภาพที่ดี
            return new ImageIcon(scaledImage); // คืนค่า ImageIcon ที่ถูกปรับขนาดแล้ว
        } else { // ถ้าไม่พบไฟล์ภาพ
            System.out.println("Couldn't find file: " + path); // พิมพ์ข้อความแจ้งว่าไม่พบไฟล์ในคอนโซล
            return null; // คืนค่า null
        }
    }

    public static void main(String[] args) { // เมธอด main ซึ่งเป็นจุดเริ่มต้นของโปรแกรม
        SwingUtilities.invokeLater(() -> new AlienInvasionHome().setVisible(true)); // สั่งให้สร้างและแสดงหน้าต่าง AlienInvasionHome ใน Event Dispatch Thread เพื่อความปลอดภัยของ Swing
    }
}