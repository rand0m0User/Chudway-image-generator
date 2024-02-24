package ChudwayMapGenerator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ChudwayLogoGenerator {

    public static void main(String[] args) {
        if (args.length < 1) {
            //args = new String[]{"text"};
            System.out.println("Please provide a text value.");
            return;
        }

        int wW = 4000; // Set the desired width of the logo
        int width = wW / 2;
        int height = wW / 2;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        double fontSize = (height * 0.19) * 0.8;
        double circleWidth = height * 0.17;

        // Draw red circle
        int circleSize = (int) (height * 0.7);
        int circleX = (width - circleSize) / 2;
        int circleY = (height - circleSize) / 2;
        g2d.setColor(Color.decode("#DC241F"));
        g2d.setStroke(new BasicStroke((float) circleWidth));
        g2d.drawArc(circleX, circleY, circleSize, circleSize, 0, 360);

        // Draw blue bar
        g2d.setColor(Color.decode("#0019A8"));
        g2d.fillRect(0, height / 2 - (int) (height * 0.095), width, (int) (height * 0.19));

        // Draw text
        g2d.setColor(Color.WHITE);
        Font font = new Font("Cabin", Font.BOLD, (int) fontSize);
        g2d.setFont(font);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fontMetrics = g2d.getFontMetrics();
        String textVal = args[0];
        int textWidth = fontMetrics.stringWidth(textVal.toUpperCase());
        int textX = (width - textWidth) / 2;
        int textY = (height - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();

        // Scale down the font size if the text width exceeds the image width
        while (textWidth > width) {
            fontSize -= 1;
            font = font.deriveFont((float) fontSize);
            g2d.setFont(font);
            fontMetrics = g2d.getFontMetrics();
            textWidth = fontMetrics.stringWidth(textVal.toUpperCase());
            textX = (width - textWidth) / 2; // Recalculate the textX position
        }

        // Calculate the new textY position to center the text vertically
        int textHeight = fontMetrics.getHeight();
        textY = (height - textHeight) / 2 + fontMetrics.getAscent();

        g2d.drawString(textVal.toUpperCase(), textX, textY);
        g2d.dispose();

        try {
            File outputFile = new File("logo.png");
            ImageIO.write(image, "png", outputFile);
            System.out.println("Logo saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving the logo: " + e.getMessage());
        }
    }
}
