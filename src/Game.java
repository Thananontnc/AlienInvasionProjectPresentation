import java.awt.*; // นำเข้าคลาสสำหรับจัดการกราฟิกและสี
import java.awt.event.*; // นำเข้าคลาสสำหรับจัดการเหตุการณ์ เช่น การกดแป้นพิมพ์และการคลิกเมาส์
import java.awt.image.BufferedImage; // นำเข้าคลาสสำหรับจัดการภาพในหน่วยความจำ
import java.io.IOException; // นำเข้าคลาสสำหรับจัดการข้อผิดพลาดในการอ่านไฟล์
import java.util.ArrayList; // นำเข้าคลาสสำหรับจัดการลิสต์ของวัตถุที่มีขนาดเปลี่ยนแปลงได้
import java.util.Iterator; // นำเข้าคลาสสำหรับวนลูปและจัดการกับองค์ประกอบในคอลเล็กชัน
import java.util.Random; // นำเข้าคลาสสำหรับสุ่มค่า
import javax.imageio.ImageIO; // นำเข้าคลาสสำหรับอ่านและเขียนไฟล์รูปภาพ
import javax.swing.*; // นำเข้าคลาสทั้งหมดจาก Swing สำหรับสร้าง GUI

// คลาสหลักของเกม ควบคุมการทำงานของเกมทั้งหมด
public class Game extends JPanel implements ActionListener { // ประกาศคลาส Game ซึ่งสืบทอดคุณสมบัติจาก JPanel และ implements ActionListener เพื่อจัดการกับ Timer

    private Timer timer; // ตัวแปร Timer สำหรับควบคุมการอัปเดตและวาดภาพเกม
    private Player player; // ตัวแปรสำหรับอ็อบเจ็กต์ผู้เล่น
    private ArrayList<Obstacle> obstacles; // ลิสต์สำหรับเก็บอุปสรรคในเกม
    private ArrayList<Soldier> soldiers; // ลิสต์สำหรับเก็บทหารในเกม
    private ArrayList<Coin> coins; // ลิสต์สำหรับเก็บเหรียญในเกม
    private int score; // ตัวแปรเก็บคะแนนของผู้เล่น
    private int hearts; // ตัวแปรเก็บจำนวนชีวิต (หัวใจ) ของผู้เล่น
    private int distance; // ตัวแปรเก็บระยะทางที่ผ่านไปในเกม
    private int obstacleCooldown = 100; // ตัวแปรสำหรับหน่วงการสร้างอุปสรรคใหม่
    private int soldierCooldown = 150; // ตัวแปรสำหรับหน่วงการสร้างทหารใหม่
    private int speedMultiplier = 2; // ตัวคูณความเร็วของเกม

    public static int totalCoins = 0; // ตัวแปร static สำหรับเก็บจำนวนเหรียญทั้งหมดที่สะสมได้
    public static int highScore = 0; // ตัวแปร static สำหรับเก็บคะแนนสูงสุด
    public static String selectedCharacter = "alien"; // ตัวแปร static สำหรับเก็บตัวละครที่ถูกเลือก (ค่าเริ่มต้น "alien")
    private BufferedImage backgroundImage, heartImage, coinImage; // ตัวแปรสำหรับเก็บภาพพื้นหลัง, หัวใจ, และเหรียญ
    private BufferedImage bulletImage, groundImage; // ตัวแปรสำหรับเก็บภาพกระสุนและพื้น

    private int coinCooldown = 200; // ตัวแปรสำหรับหน่วงการสร้างเหรียญใหม่
    private int groundX = 0; // ตัวแปรสำหรับตำแหน่งแกน x ของภาพพื้น

    // คอนสตรักเตอร์ของคลาส Game สำหรับสร้างและตั้งค่าเริ่มต้นของเกม
    public Game(JFrame frame) { // คอนสตรักเตอร์รับอ็อบเจ็กต์ JFrame ที่ใช้แสดงเกม
        setFocusable(true); // กำหนดให้ JPanel นี้สามารถรับโฟกัสเพื่อรับคีย์อินพุตได้
        setPreferredSize(new Dimension(1024, 600)); // กำหนดขนาดที่ต้องการของเกมเป็น 1024x600 พิกเซล
        addKeyListener(new KeyAdapter() { // เพิ่ม KeyListener เพื่อรับเหตุการณ์จากคีย์บอร์ด
            public void keyPressed(KeyEvent e) { // เมธอดที่เรียกเมื่อมีการกดแป้นพิมพ์
                player.keyPressed(e); // ส่งเหตุการณ์การกดแป้นพิมพ์ไปยังผู้เล่น
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // ถ้ากดปุ่ม Enter
                    player.shoot(); // ให้ผู้เล่นยิงกระสุน
                }
            }

            public void keyReleased(KeyEvent e) { // เมธอดที่เรียกเมื่อมีการปล่อยแป้นพิมพ์
                player.keyReleased(e); // ส่งเหตุการณ์การปล่อยแป้นพิมพ์ไปยังผู้เล่น
            }
        });

        System.out.println("Selected Character in Game: " + selectedCharacter); // พิมพ์ตัวละครที่ถูกเลือกลงคอนโซล
        player = new Player(selectedCharacter); // สร้างอ็อบเจ็กต์ Player โดยใช้ตัวละครที่ถูกเลือก
        obstacles = new ArrayList<>(); // สร้างลิสต์สำหรับเก็บอุปสรรค
        soldiers = new ArrayList<>(); // สร้างลิสต์สำหรับเก็บทหาร
        coins = new ArrayList<>(); // สร้างลิสต์สำหรับเก็บเหรียญ
        score = 0; // กำหนดคะแนนเริ่มต้นเป็น 0
        hearts = 3; // กำหนดจำนวนชีวิตเริ่มต้นเป็น 3 หัวใจ
        distance = 0; // กำหนดระยะทางเริ่มต้นเป็น 0

        try { // พยายามโหลดไฟล์รูปภาพต่างๆ
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/Assets/background.jpg")); // โหลดภาพพื้นหลัง
            heartImage = ImageIO.read(getClass().getResourceAsStream("/Assets/heart.png")); // โหลดภาพหัวใจ
            coinImage = ImageIO.read(getClass().getResourceAsStream("/Assets/coin.png")); // โหลดภาพเหรียญ
            bulletImage = ImageIO.read(getClass().getResourceAsStream("/Assets/ammunition.png")); // โหลดภาพกระสุน
            groundImage = ImageIO.read(getClass().getResourceAsStream("/Assets/rock.jpg")); // โหลดภาพพื้น (rock.jpg)
        } catch (IOException | IllegalArgumentException e) { // จับข้อผิดพลาดที่อาจเกิดขึ้นระหว่างการโหลดไฟล์ภาพ
            System.out.println("Error loading image files: " + e.getMessage()); // แสดงข้อความข้อผิดพลาดในคอนโซล
            e.printStackTrace(); // แสดงรายละเอียดข้อผิดพลาด
        }

        timer = new Timer(20, this); // สร้าง Timer ที่จะส่งเหตุการณ์ทุก 20 มิลลิวินาทีให้กับ actionPerformed
        timer.start(); // เริ่ม Timer เพื่อให้เกมเริ่มทำงาน
    }

    @Override
    public void actionPerformed(ActionEvent e) { // เมธอด actionPerformed ที่จะถูกเรียกเมื่อ Timer ส่งเหตุการณ์เข้ามา
        player.move(0); // ให้ผู้เล่นเคลื่อนที่ (พารามิเตอร์ 0 หมายถึงไม่มีการเพิ่มความเร็วเพิ่มเติม)
        moveObstacles(); // เคลื่อนที่อุปสรรคทั้งหมด
        moveSoldiers(); // เคลื่อนที่ทหารทั้งหมด
        moveCoins(); // เคลื่อนที่เหรียญทั้งหมด
        moveBullets(); // เคลื่อนที่กระสุนของผู้เล่น
        checkCollision(); // ตรวจสอบการชนกันระหว่างวัตถุในเกม
        spawnObstacles(); // สร้างอุปสรรคใหม่ถ้าผ่านเวลาหน่วง
        spawnCoins(); // สร้างเหรียญใหม่ถ้าผ่านเวลาหน่วง
        spawnSoldiers(); // สร้างทหารใหม่ถ้าผ่านเวลาหน่วง
        distance += 1; // เพิ่มค่าระยะทางที่ผ่านไปทีละ 1
        adjustSpeed(); // ปรับความเร็วของเกมตามระยะทางที่ผ่านไป

        // อัปเดตตำแหน่งของพื้น
        groundX -= speedMultiplier; // เลื่อนตำแหน่งภาพพื้นไปทางซ้ายตามความเร็วที่คูณด้วย speedMultiplier
        if (groundImage != null && groundX <= -groundImage.getWidth()) { // ถ้าภาพพื้นมีอยู่และพื้นเลื่อนจนออกนอกหน้าจอ
            groundX = 0; // รีเซ็ตตำแหน่งพื้นกลับไปที่เริ่มต้น
        }

        repaint(); // เรียก repaint เพื่อให้หน้าจอเกมวาดใหม่ตามการอัปเดต
    }

    // เมธอดสำหรับปรับความเร็วของเกมตามระยะทาง
    private void adjustSpeed() {
        if (distance >= 3000) speedMultiplier = 6; // ถ้าระยะทางผ่านไปมากกว่า 3000 ให้ความเร็วเท่ากับ 6
        else if (distance >= 2000) speedMultiplier = 5; // ถ้าระยะทางผ่านไปมากกว่า 2000 ให้ความเร็วเท่ากับ 5
        else if (distance >= 1000) speedMultiplier = 4; // ถ้าระยะทางผ่านไปมากกว่า 1000 ให้ความเร็วเท่ากับ 4
        // ถ้าน้อยกว่า 1000 จะคงความเร็วเริ่มต้นไว้ (2)
    }

    // เมธอดสำหรับเคลื่อนที่สิ่งกีดขวาง
    private void moveObstacles() {
        for (Iterator<Obstacle> itr = obstacles.iterator(); itr.hasNext();) { // วนลูปผ่านอุปสรรคทั้งหมดในลิสต์
            Obstacle obstacle = itr.next(); // ดึงอุปสรรคตัวถัดไปจาก Iterator
            obstacle.move(speedMultiplier); // เคลื่อนที่อุปสรรคโดยใช้ speedMultiplier
            if (obstacle.getX() < 0) { // ถ้าอุปสรรคออกนอกหน้าจอ (ตำแหน่ง x น้อยกว่า 0)
                itr.remove(); // ลบอุปสรรคออกจากลิสต์
            }
        }
    }

    // เมธอดสำหรับเคลื่อนที่ทหาร
    private void moveSoldiers() {
        for (Iterator<Soldier> itr = soldiers.iterator(); itr.hasNext();) { // วนลูปผ่านทหารทั้งหมดในลิสต์
            Soldier soldier = itr.next(); // ดึงทหารตัวถัดไปจาก Iterator
            soldier.move(bulletImage, speedMultiplier); // เคลื่อนที่ทหารโดยส่งภาพกระสุนและความเร็วเข้าไป

            // เคลื่อนที่กระสุนของทหาร
            ArrayList<Bullet> soldierBullets = soldier.getBullets(); // รับลิสต์กระสุนของทหาร
            for (Iterator<Bullet> bulletItr = soldierBullets.iterator(); bulletItr.hasNext();) { // วนลูปผ่านกระสุนของทหาร
                Bullet bullet = bulletItr.next(); // ดึงกระสุนตัวถัดไปจาก Iterator
                bullet.move(); // เคลื่อนที่กระสุนของทหาร
                if (bullet.isOffScreen()) { // ถ้ากระสุนออกนอกหน้าจอ
                    bulletItr.remove(); // ลบกระสุนออกจากลิสต์
                }
            }

            if (soldier.getX() < 0) { // ถ้าทหารออกนอกหน้าจอ (ตำแหน่ง x น้อยกว่า 0)
                itr.remove(); // ลบทหารออกจากลิสต์
            }
        }
    }

    // เมธอดสำหรับเคลื่อนที่เหรียญ
    private void moveCoins() {
        for (Iterator<Coin> itr = coins.iterator(); itr.hasNext();) { // วนลูปผ่านเหรียญทั้งหมดในลิสต์
            Coin coin = itr.next(); // ดึงเหรียญตัวถัดไปจาก Iterator
            coin.move(speedMultiplier); // เคลื่อนที่เหรียญโดยใช้ speedMultiplier
            if (coin.getX() < 0) { // ถ้าเหรียญออกนอกหน้าจอ (ตำแหน่ง x น้อยกว่า 0)
                itr.remove(); // ลบเหรียญออกจากลิสต์
            }
        }
    }

    // เมธอดสำหรับเคลื่อนที่กระสุนของผู้เล่น
    private void moveBullets() {
        ArrayList<Bullet> playerBullets = player.getBullets(); // รับลิสต์กระสุนของผู้เล่น
        for (Iterator<Bullet> itr = playerBullets.iterator(); itr.hasNext();) { // วนลูปผ่านกระสุนทั้งหมดในลิสต์
            Bullet bullet = itr.next(); // ดึงกระสุนตัวถัดไปจาก Iterator
            bullet.move(); // เคลื่อนที่กระสุน
            if (bullet.isOffScreen()) { // ถ้ากระสุนออกนอกหน้าจอ
                itr.remove(); // ลบกระสุนออกจากลิสต์
            }
        }
    }

    // เมธอดสำหรับตรวจสอบการชนกันระหว่างวัตถุในเกม
    private void checkCollision() {
        // การชนระหว่างกระสุนของผู้เล่นและทหาร
        for (Iterator<Soldier> soldierItr = soldiers.iterator(); soldierItr.hasNext();) { // วนลูปผ่านทหารทั้งหมดในลิสต์
            Soldier soldier = soldierItr.next(); // ดึงทหารตัวถัดไปจาก Iterator
            ArrayList<Bullet> playerBullets = player.getBullets(); // รับลิสต์กระสุนของผู้เล่น
            for (Iterator<Bullet> bulletItr = playerBullets.iterator(); bulletItr.hasNext();) { // วนลูปผ่านกระสุนของผู้เล่น
                Bullet bullet = bulletItr.next(); // ดึงกระสุนตัวถัดไปจาก Iterator
                if (bullet.getBounds().intersects(soldier.getBounds())) { // ตรวจสอบว่ากระสุนชนกับทหารหรือไม่
                    soldierItr.remove(); // ลบทหารออกจากลิสต์เมื่อเกิดการชน
                    bulletItr.remove(); // ลบกระสุนออกจากลิสต์เมื่อเกิดการชน
                    score += 50; // เพิ่มคะแนน 50 เมื่อกำจัดทหาร
                    break; // ออกจากลูปของกระสุนเมื่อพบการชน
                }
            }
        }

        // การชนระหว่างผู้เล่นและเหรียญ
        for (Iterator<Coin> coinItr = coins.iterator(); coinItr.hasNext();) { // วนลูปผ่านเหรียญทั้งหมดในลิสต์
            Coin coin = coinItr.next(); // ดึงเหรียญตัวถัดไปจาก Iterator
            if (player.getBounds().intersects(coin.getBounds())) { // ตรวจสอบว่าผู้เล่นชนกับเหรียญหรือไม่
                score += 10; // เพิ่มคะแนน 10 เมื่อเก็บเหรียญ
                totalCoins += 10; // เพิ่มจำนวนเหรียญสะสม
                coinItr.remove(); // ลบเหรียญออกจากลิสต์เมื่อเก็บได้
            }
        }

        // การชนระหว่างผู้เล่นและอุปสรรค
        for (Iterator<Obstacle> obstacleItr = obstacles.iterator(); obstacleItr.hasNext();) { // วนลูปผ่านอุปสรรคทั้งหมดในลิสต์
            Obstacle obstacle = obstacleItr.next(); // ดึงอุปสรรคตัวถัดไปจาก Iterator
            if (player.getBounds().intersects(obstacle.getBounds())) { // ตรวจสอบว่าผู้เล่นชนกับอุปสรรคหรือไม่
                hearts--; // ลดจำนวนชีวิตลง 1 เมื่อเกิดการชน
                obstacleItr.remove(); // ลบอุปสรรคออกจากลิสต์เมื่อชนกัน
            }
        }

        // การชนระหว่างผู้เล่นและทหาร
        for (Iterator<Soldier> soldierItr = soldiers.iterator(); soldierItr.hasNext();) { // วนลูปผ่านทหารทั้งหมดในลิสต์
            Soldier soldier = soldierItr.next(); // ดึงทหารตัวถัดไปจาก Iterator
            if (player.getBounds().intersects(soldier.getBounds())) { // ตรวจสอบว่าผู้เล่นชนกับทหารหรือไม่
                hearts--; // ลดจำนวนชีวิตลง 1 เมื่อชนกับทหาร
                soldierItr.remove(); // ลบทหารออกจากลิสต์เมื่อชนกัน
            }

            // การชนระหว่างผู้เล่นและกระสุนของทหาร
            for (Iterator<Bullet> bulletItr = soldier.getBullets().iterator(); bulletItr.hasNext();) { // วนลูปผ่านกระสุนของทหาร
                Bullet bullet = bulletItr.next(); // ดึงกระสุนตัวถัดไปจาก Iterator
                if (player.getBounds().intersects(bullet.getBounds())) { // ตรวจสอบว่าผู้เล่นชนกับกระสุนของทหารหรือไม่
                    hearts--; // ลดจำนวนชีวิตลง 1 เมื่อชนกับกระสุน
                    bulletItr.remove(); // ลบกระสุนออกจากลิสต์เมื่อชนกัน
                }
            }
        }

        if (hearts <= 0) gameOver(); // ถ้าจำนวนชีวิตเหลือ 0 หรือน้อยกว่า ให้เรียกเมธอด gameOver()
    }

    // เมธอดสำหรับสร้างสิ่งกีดขวางใหม่
    private void spawnObstacles() {
        if (obstacleCooldown > 0) { // ถ้ายังไม่ครบเวลาหน่วงสำหรับสร้างอุปสรรคใหม่
            obstacleCooldown--; // ลดค่าหน่วงลงทีละ 1
            return; // ออกจากเมธอดโดยไม่สร้างอุปสรรคใหม่
        }

        Random rand = new Random(); // สร้างอ็อบเจ็กต์ Random สำหรับสุ่มค่า
        if (rand.nextInt(100) < 5) { // มีโอกาส 5% ที่จะสร้างอุปสรรคใหม่
            String[] obstacleTypes = { "ufo", "spike", "car", "thorn" }; // กำหนดประเภทอุปสรรคที่เป็นไปได้
            String randomType = obstacleTypes[rand.nextInt(obstacleTypes.length)]; // เลือกประเภทอุปสรรคแบบสุ่ม

            int yPos; // ตัวแปรสำหรับตำแหน่ง y ของอุปสรรค
            int obstacleWidth; // ตัวแปรสำหรับความกว้างของอุปสรรค
            int obstacleHeight; // ตัวแปรสำหรับความสูงของอุปสรรค

            if (randomType.equals("ufo")) { // ถ้าประเภทอุปสรรคคือ "ufo"
                yPos = 400; obstacleWidth = 100; obstacleHeight = 50; // กำหนดตำแหน่งและขนาดสำหรับ ufo
            } else if (randomType.equals("car")) { // ถ้าประเภทอุปสรรคคือ "car"
                yPos = 430; obstacleWidth = 120; obstacleHeight = 100; // กำหนดตำแหน่งและขนาดสำหรับรถ
            } else { // สำหรับประเภทอุปสรรคอื่นๆ (เช่น spike, thorn)
                yPos = 450; obstacleWidth = 50; obstacleHeight = 50; // กำหนดตำแหน่งและขนาดทั่วไป
            }

            obstacles.add(new Obstacle(1024, yPos, obstacleWidth, obstacleHeight, randomType)); // สร้างอุปสรรคใหม่ที่ตำแหน่ง x เริ่มต้นที่ 1024 และเพิ่มลงในลิสต์
            obstacleCooldown = 100; // รีเซ็ตค่าหน่วงสำหรับสร้างอุปสรรคใหม่
        }
    }

    // เมธอดสำหรับสร้างทหารใหม่
    private void spawnSoldiers() {
        if (soldierCooldown > 0) { // ถ้ายังไม่ครบเวลาหน่วงสำหรับสร้างทหารใหม่
            soldierCooldown--; // ลดค่าหน่วงลงทีละ 1
            return; // ออกจากเมธอดโดยไม่สร้างทหารใหม่
        }

        Random rand = new Random(); // สร้างอ็อบเจ็กต์ Random สำหรับสุ่มค่า
        if (rand.nextInt(100) < 5) { // มีโอกาส 5% ที่จะสร้างทหารใหม่
            int soldierWidth = 60; // กำหนดความกว้างของทหารเป็น 60
            int soldierHeight = 100; // กำหนดความสูงของทหารเป็น 100
            int yPos = 500 - soldierHeight; // คำนวณตำแหน่ง y ของทหารให้ชิดพื้น (500 คือระดับพื้น)
            soldiers.add(new Soldier(1024, soldierWidth, soldierHeight)); // สร้างทหารใหม่ที่ตำแหน่ง x เริ่มต้นที่ 1024 และเพิ่มลงในลิสต์
            soldierCooldown = 150; // รีเซ็ตค่าหน่วงสำหรับสร้างทหารใหม่
        }
    }

    // เมธอดสำหรับสร้างเหรียญใหม่
    private void spawnCoins() {
        if (coinCooldown > 0) { // ถ้ายังไม่ครบเวลาหน่วงสำหรับสร้างเหรียญใหม่
            coinCooldown--; // ลดค่าหน่วงลงทีละ 1
            return; // ออกจากเมธอดโดยไม่สร้างเหรียญใหม่
        }

        Random rand = new Random(); // สร้างอ็อบเจ็กต์ Random สำหรับสุ่มค่า
        if (rand.nextInt(100) < 3) { // มีโอกาส 3% ที่จะสร้างเหรียญใหม่
            int xPos = 1024; // กำหนดตำแหน่งเริ่มต้น x ของเหรียญเป็น 1024 (ด้านขวาของหน้าจอ)
            int yPos = 460; // กำหนดตำแหน่ง y ของเหรียญเป็น 460
            coins.add(new Coin(xPos, yPos, 20, 20)); // สร้างเหรียญใหม่ที่มีขนาด 20x20 และเพิ่มลงในลิสต์
        }
    }

    // เมธอดจัดการเมื่อเกมจบ
    private void gameOver() {
        if (score > highScore) highScore = score; // หากคะแนนปัจจุบันสูงกว่าคะแนนสูงสุด ให้ปรับค่า highScore เป็นคะแนนปัจจุบัน

        int option = JOptionPane.showOptionDialog( // แสดงหน้าต่างตัวเลือกเมื่อเกมจบ
                this, // พาเนลที่ใช้แสดงข้อความในหน้าต่าง
                "Game Over! Your score: " + score + "\nHigh Score: " + highScore + "\nTotal Coins: " + totalCoins + "\nWhat do you want to do?", // ข้อความที่จะแสดงในหน้าต่าง
                "Game Over", // ชื่อหัวข้อของหน้าต่าง
                JOptionPane.YES_NO_OPTION, // ตัวเลือกให้ผู้เล่นเลือกแบบ Yes/No
                JOptionPane.QUESTION_MESSAGE, // ไอคอนแสดงคำถาม
                null, // ไม่มีไอคอนเพิ่มเติม
                new String[]{"Restart", "Back to Home"}, // ตัวเลือกให้ผู้เล่นเลือก "Restart" หรือ "Back to Home"
                "Restart" // ตัวเลือกเริ่มต้น
        );

        if (option == JOptionPane.YES_OPTION) restartGame(); // หากผู้เล่นเลือก "Restart" ให้เรียกเมธอด restartGame()
        else backToHome(); // หากไม่ใช่ ให้เรียกเมธอด backToHome()
    }

    // เมธอดสำหรับเริ่มเกมใหม่
    private void restartGame() {
        score = 0; // รีเซ็ตคะแนนเป็น 0
        hearts = 3; // รีเซ็ตจำนวนชีวิตเป็น 3 หัวใจ
        player = new Player(selectedCharacter); // สร้างผู้เล่นใหม่โดยใช้ตัวละครที่ถูกเลือก
        obstacles.clear(); // ลบอุปสรรคทั้งหมดออกจากลิสต์
        soldiers.clear(); // ลบทหารทั้งหมดออกจากลิสต์
        coins.clear(); // ลบเหรียญทั้งหมดออกจากลิสต์
        distance = 0; // รีเซ็ตระยะทางเป็น 0
        groundX = 0; // รีเซ็ตตำแหน่งของพื้นกลับไปที่จุดเริ่มต้น
        timer.start(); // เริ่ม Timer เพื่อเริ่มเกมใหม่
    }

    // เมธอดสำหรับกลับไปยังหน้าหลัก
    private void backToHome() {
        timer.stop(); // หยุด Timer เพื่อหยุดการทำงานของเกม
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this); // รับหน้าต่างหลักที่มี JPanel นี้อยู่
        topFrame.dispose(); // ปิดหน้าต่างหลัก

        SwingUtilities.invokeLater(() -> { // สร้างและแสดงหน้าหลักใหม่ใน thread ของ Swing
            JFrame homeFrame = new AlienInvasionHome(); // สร้างหน้าต่างหลักใหม่ (AlienInvasionHome)
            homeFrame.setVisible(true); // แสดงหน้าต่างหลักใหม่
            homeFrame.setSize(800, 600); // กำหนดขนาดหน้าต่างหลักใหม่เป็น 800x600 พิกเซล
            homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // กำหนดให้โปรแกรมปิดเมื่อหน้าต่างหลักถูกปิด
        });
    }

    @Override
    // เมธอดสำหรับวาดกราฟิกทั้งหมดของเกม
    public void paintComponent(Graphics g) { // เมธอด paintComponent สำหรับวาดองค์ประกอบทั้งหมดของเกมลงบนหน้าจอ
        super.paintComponent(g); // เรียกเมธอด paintComponent ของคลาสพ่อเพื่อเคลียร์พื้นหลัง
        if (backgroundImage != null) { // ถ้าภาพพื้นหลังมีอยู่
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // วาดภาพพื้นหลังให้ครอบคลุมหน้าจอทั้งหมด
        }

        // วาดพื้น
        if (groundImage != null) { // ถ้าภาพพื้นมีอยู่
            int groundY = 500; // กำหนดตำแหน่งแกน y ของพื้นเป็น 500
            int groundHeight = 100; // กำหนดความสูงของพื้นเป็น 100 พิกเซล
            int groundWidth = groundImage.getWidth(); // รับความกว้างของภาพพื้น

            // วาดภาพพื้นซ้ำ ๆ เพื่อให้ครอบคลุมหน้าจอ
            for (int i = 0; i <= getWidth() / groundWidth + 1; i++) { // วนลูปเพื่อวาดภาพพื้นหลายๆ ครั้งจนเต็มหน้าจอ
                g.drawImage(groundImage, groundX + i * groundWidth, groundY, groundWidth, groundHeight, this); // วาดภาพพื้นในตำแหน่งที่คำนวณได้
            }
        } else { // หากไม่พบภาพพื้น
            g.setColor(new Color(139, 69, 19)); // ตั้งค่าสีเป็นสีน้ำตาล
            g.fillRect(0, 500, 1024, 100); // วาดสี่เหลี่ยมแทนพื้นด้วยสีที่ตั้งไว้
        }

        player.paint(g); // วาดผู้เล่นลงบนหน้าจอ

        for (Obstacle obstacle : obstacles) { // วนลูปผ่านอุปสรรคทั้งหมดในลิสต์
            obstacle.paint(g); // วาดอุปสรรคลงบนหน้าจอ
        }

        for (Soldier soldier : soldiers) { // วนลูปผ่านทหารทั้งหมดในลิสต์
            soldier.paint(g); // วาดทหารลงบนหน้าจอ

            // วาดกระสุนของทหาร
            for (Bullet bullet : soldier.getBullets()) { // วนลูปผ่านกระสุนของทหารแต่ละตัว
                bullet.paint(g); // วาดกระสุนลงบนหน้าจอ
            }
        }

        for (Coin coin : coins) { // วนลูปผ่านเหรียญทั้งหมดในลิสต์
            coin.paint(g); // วาดเหรียญลงบนหน้าจอ
        }

        int rightEdge = getWidth(); // รับตำแหน่งขอบขวาของหน้าจอ
        for (int i = 0; i < hearts; i++) { // วนลูปตามจำนวนชีวิตที่เหลือ
            if (heartImage != null) { // ถ้าภาพหัวใจมีอยู่
                g.drawImage(heartImage, rightEdge - 100 + i * 30, 10, 25, 25, this); // วาดภาพหัวใจในตำแหน่งที่คำนวณจากขอบขวาของหน้าจอ
            }
        }

        g.setColor(Color.RED); // ตั้งค่าสีสำหรับข้อความเป็นสีแดง
        g.drawString("Coins: " + totalCoins, rightEdge - 150, 50); // วาดข้อความแสดงจำนวนเหรียญที่สะสมได้

        if (coinImage != null) { // ถ้าภาพเหรียญมีอยู่
            g.drawImage(coinImage, rightEdge - 50, 35, 25, 25, this); // วาดภาพเหรียญลงบนหน้าจอในตำแหน่งที่กำหนด
        }

        g.drawString("Highscore: " + highScore, 10, 80); // วาดข้อความแสดงคะแนนสูงสุดที่มุมซ้ายบน
        g.drawString("Distance (Km): " + distance / 100, 10, 100); // วาดข้อความแสดงระยะทาง (คำนวณเป็นกิโลเมตร) ที่มุมซ้ายบน
    }
}