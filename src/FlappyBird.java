import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;
    // Images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    // Bird
    int birdX = boardWidth / 8;
    int birdY = boardHeight / 2;
    int birdWidth = 45;
    int birdHeight = 40;

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    // Pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64; // scaled by 1/6
    int pipeHeight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    // Game logic
    Bird bird;
    int velocityX = -4; // moves pipes to the left speed (simulates bird moving right)
    int velocityY = 0; // move bird up/down speed
    int gravity = 1;
    ArrayList<Pipe> pipes;
    Random random = new Random();
    Timer gameLoop;
    Timer placePipesTimer;
    boolean gameOver = false;
    double score = 0;
    Clip gameOverSound;
    Clip hitSound;
    Clip pointSound;
    Clip swooshingSound;

    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        // Load images
        backgroundImg = new ImageIcon(getClass().getResource("/resources/bg2.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("/resources/bird1.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("/resources/toppipe2.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("/resources/bottompipe2.png")).getImage();

        // Load sounds
        try {
            InputStream dieInputStream = getClass().getResourceAsStream("/resources/sfx_die.wav");
            if (dieInputStream == null) {
                throw new RuntimeException("Sound resource not found: sfx_die.wav");
            }
            AudioInputStream die = AudioSystem.getAudioInputStream(dieInputStream);
            gameOverSound = AudioSystem.getClip();
            gameOverSound.open(die);

            InputStream hitInputStream = getClass().getResourceAsStream("/resources/sfx_hit.wav");
            if (hitInputStream == null) {
                throw new RuntimeException("Sound resource not found: sfx_hit.wav");
            }
            AudioInputStream hit = AudioSystem.getAudioInputStream(hitInputStream);
            hitSound = AudioSystem.getClip();
            hitSound.open(hit);

            InputStream pointInputStream = getClass().getResourceAsStream("/resources/sfx_point.wav");
            if (pointInputStream == null) {
                throw new RuntimeException("Sound resource not found: sfx_point.wav");
            }
            AudioInputStream point= AudioSystem.getAudioInputStream(pointInputStream);
            pointSound = AudioSystem.getClip();
            pointSound.open(point);

            InputStream swooshingInputStream = getClass().getResourceAsStream("/resources/sfx_swooshing.wav");
            if (swooshingInputStream == null) {
                throw new RuntimeException("Sound resource not found: sfx_swooshing.wav");
            }
            AudioInputStream swooshing= AudioSystem.getAudioInputStream(swooshingInputStream);
            swooshingSound = AudioSystem.getClip();
            swooshingSound.open(swooshing);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        // Place pipes timer
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipesTimer.start();

        // Game timer
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    public void placePipes() {
        int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = boardHeight / 4;

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        // Bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        // Pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        // Score
        g.setColor(Color.white);
        g.setFont(new Font("Algerian", Font.PLAIN, 32));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over" , 10, 35);
            g.setColor(Color.green);
            g.drawString("Score: "  + String.valueOf((int) score), 10, 70);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move() {
        // Bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);
        playSound(swooshingSound);


        // Pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;
            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score += 0.5; // 0.5 because there are 2 pipes! so 0.5*2 = 1, 1 for each set of pipes
                playSound(pointSound);
            }
            if (collision(bird, pipe)) {
                gameOver = true;
                playSound(hitSound);
            }
        }

        if (bird.y > boardHeight) {
            gameOver = true;
            playSound(gameOverSound);
        }
    }

    public boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width && // a's top left corner doesn't reach b's top right corner
                a.x + a.width > b.x && // a's top right corner passed b's top left corner
                a.y < b.y + b.height && // a's top left corner doesn't reach b's bottom left corner
                a.y + a.height > b.y; // a's bottom left corner passed b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();

        repaint();
        if (gameOver) {
            playSound(gameOverSound);
            placePipesTimer.stop();
            gameLoop.stop();

        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -11;
            if (gameOver) {
                // Restart the game by resetting the conditions
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipesTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    private void playSound(Clip sound) {
        if (sound.isRunning()) {
            sound.stop(); // Stop the sound if it is still running
        }
        sound.setFramePosition(0); // Reset the sound to the beginning
        sound.start(); // Start playing the sound
    }
}
