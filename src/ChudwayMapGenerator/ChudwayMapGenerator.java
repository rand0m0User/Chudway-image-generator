package ChudwayMapGenerator;

import static ChudwayMapGenerator.ChudwayMapGenerator.drawLine;
import static ChudwayMapGenerator.ChudwayMapGenerator.drawSprite;
import static ChudwayMapGenerator.ChudwayMapGenerator.drawString;
import static ChudwayMapGenerator.ChudwayMapGenerator.junctions;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.imageio.ImageIO;

public class ChudwayMapGenerator {

    public static int width = 1080; // Width of the image
    public static int height = 1080; // Height of the image

    // Create a BufferedImage object with the specified width and height
    public static BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    // Get the Graphics2D object from the BufferedImage
    public static Graphics2D g2d = image.createGraphics();

    public static junction[] junctions = {};
    //FORMAT: 
    //vec2 image position, 
    //vec2 offset (from postition where the icon is to put the text)
    //icon (or any image in ".\\sprites\\" really)
    //name of location

    public static line[] lines = {};
    //FORMAT: <from location> <to location> color

    public static String version = "null";

    public static String[][] lineKeys = new String[][]{};

    public static void main(String[] args) throws Exception {
        if (args.length != 0) {
            ChudwayLogoGenerator.main(args); //multiple entry points
            return;
        }
        // Set the background color of the image
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        drawSprite(100, 100, "compas");
        //information
        //jak
        drawSprite(width - 100, (height / 2) - 200, "class37");

        //chudway logo
        drawSprite(width / 2, height - 125, "logo");

        String[] file = (new String(Files.readAllBytes(new File("chudway.map").getCanonicalFile().toPath()), "UTF-8")).split("\n");

        //parse the map file
        for (String l : file) {
            String[] sp = l.trim().split(":");
            if (l.startsWith("//")) {
                //ignore coments
                continue;
            }
            if (l.startsWith("STATION")) {
                junctions = addJunction(junctions, new junction(
                        new int[]{
                            Integer.parseInt(sp[1]),
                            Integer.parseInt(sp[2])},
                        new int[]{
                            Integer.parseInt(sp[3]),
                            Integer.parseInt(sp[4])},
                        sp[5], sp[6]));
            }
            if (l.startsWith("TUBECOMBO")) {
                lines = addLine(lines, new line(sp[1], sp[2], sp[3], Integer.parseInt(sp[4].trim())));
                continue;
            }
            if (l.startsWith("TUBE")) {
                lines = addLine(lines, new line(sp[1], sp[2], sp[3]));
            }
            if (l.startsWith("COLORS")) {
                lineKeys = push(lineKeys, new String[]{sp[1], sp[2]});
            }
            if (l.startsWith("VERSION")) {
                version = sp[1];
            }
        }

        //draw all of the lines
        for (line l : lines) {
            l.draw();
        }

        //draw stations and location text over lines
        for (junction j : junctions) {
            j.draw();
        }

        //outlines
        drawLine(6, 1073, 6, 709, 5, Color.decode("-27034"));
        drawLine(6, 1073, 392, 1073, 5, Color.decode("-27034"));
        drawLine(392, 709, 6, 709, 5, Color.decode("-27034"));
        drawLine(392, 709, 392, 1073, 5, Color.decode("-27034"));

        drawLine(1073, 1073, 683, 1073, 5, Color.decode("-27034"));
        drawLine(1073, 1073, 1073, 858, 5, Color.decode("-27034"));
        drawLine(1073, 858, 682, 858, 5, Color.decode("-27034"));
        drawLine(682, 1073, 682, 858, 5, Color.decode("-27034"));

        int y = 694;
        int space = 35;
        for (String[] l : lineKeys) {
            drawString(150, y, l[0], Color.decode(l[1]), 36);
            y += space;
        }

        //drawString(150, y, "Line Legend", Color.BLACK, 36);
        int iy = 893;
        int ispace = 35;
        drawString(785, iy - 50, "Icon Legend", Color.BLACK, 36);
        new junction(new int[]{712, iy}, new int[]{-120, 0}, "interchange", "interchange", true).draw();
        iy += ispace;
        new junction(new int[]{712, iy}, new int[]{-120, 0}, "station", "station", true).draw();
        iy += ispace;
        new junction(new int[]{712, iy}, new int[]{-120, 0}, "closed", "closed", true).draw();

        drawString(998, 1000, "V", Color.BLACK, 36);
        drawString(998, 1049, version, Color.BLACK, 36);

        // Save the BufferedImage to a file
        try {
            File output = new File("output.png");
            ImageIO.write(image, "png", output);
            System.out.println("Image saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving image: " + e.getMessage());
        }

        // Dispose the Graphics2D object
        g2d.dispose();
    }

    public static /* utility */ junction[] addJunction(junction[] in, junction value) {
        junction[] a = new junction[in.length + 1];
        System.arraycopy(in, 0, a, 0, in.length);
        a[in.length] = value;
        return a;
    }

    public static /* utility */ line[] addLine(line[] in, line value) {
        line[] a = new line[in.length + 1];
        System.arraycopy(in, 0, a, 0, in.length);
        a[in.length] = value;
        return a;
    }

    public static /* utility */ String[][] push(String[][] in, String[] value) {
        String[][] a = new String[in.length + 1][];
        System.arraycopy(in, 0, a, 0, in.length);
        a[in.length] = value;
        return a;
    }

    public static void drawString(int x, int y, String text, Color color, int size) {
        g2d.setColor(color);
        g2d.setFont(new Font("Cabin", Font.BOLD, size));

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        int textX = x - textWidth / 2;
        int textY = y - textHeight / 2 + fm.getAscent();

        g2d.drawString(text, textX, textY);
    }

    public static void drawLine(int fX, int fY, int tX, int tY, int size, Color color) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(size));
        g2d.drawLine(fX, fY, tX, tY);
    }

    //drawing of sprites starts from the bottom left 
    public static void drawSprite(int x, int y, String name) {
        try {
            BufferedImage sprite = ImageIO.read(new File("sprites\\" + name + ".png"));

            // Calculate the position to draw the image centered on (x, y)
            int spriteX = x - sprite.getWidth() / 2;
            int spriteY = y - sprite.getHeight() / 2;

            // Draw the image onto the BufferedImage
            g2d.drawImage(sprite, spriteX, spriteY, null);
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
        }
    }
}

class junction {

    int[] pos;
    int[] oset;
    String sprite;
    String name;
    boolean raw = false;

    junction(int[] position, int[] offset, String icon, String name) {
        pos = position;
        oset = offset;
        sprite = icon;
        this.name = name;
    }

    //custom constructor tobypass the name detection to draw sprites
    junction(int[] position, int[] offset, String icon, String name, boolean raw) {
        pos = position;
        oset = offset;
        sprite = icon;
        this.name = name;
        this.raw = raw;
    }

    void draw() {

        drawSprite(pos[0], pos[1], sprite);
        if (!"dummy".equals(sprite) && !"closed".equals(sprite)) {
            //do not draw anything for track turns or closed lines, it isnt even a station!
            drawString(pos[0] - oset[0], pos[1] - oset[1], name, Color.BLACK, 32);
        }
        if (raw) {
            drawString(pos[0] - oset[0], pos[1] - oset[1], name, Color.BLACK, 32);
        }
    }
}

class line {

    String from;
    private int[] f;
    String to;
    private int[] t;
    String color;
    int width = 10;

    line(String pontA, String pointB, String color) {
        from = pontA;
        to = pointB;
        this.color = color;
    }

    line(String pontA, String pointB, String color, int size) {
        from = pontA;
        to = pointB;
        this.color = color;
        width = size;
    }

    void draw() {
        //find the 2 locations this line describes and save their points
        for (junction j : junctions) {
            if (j.name.equals(from)) {
                f = j.pos;
            }
            if (j.name.equals(to)) {
                t = j.pos;
            }
        }
        if (f == null || t == null) {
            System.err.println("line error");
            return;
        }
        drawLine(f[0], f[1], t[0], t[1], width, Color.decode(color));
    }
}
