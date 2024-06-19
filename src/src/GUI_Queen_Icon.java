import javax.swing.*;
import java.awt.*;

public class GUI_Queen_Icon implements Icon
{
    private Color color;
    private boolean filled;

    public GUI_Queen_Icon(Color color, boolean filled)
    {
        this.color = color;
        this.filled = filled;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.DARK_GRAY);
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(5));
        g2d.drawOval(x + 5, y + 5, getIconWidth() - 10, getIconHeight() - 10);
        g2d.setStroke(oldStroke);
        g2d.setColor(color);

        if (filled)
            g2d.fillOval(x + 5, y + 5, getIconWidth() - 10, getIconHeight() - 10);
        else
            g2d.drawOval(x + 5, y + 5, getIconWidth() - 10, getIconHeight() - 10);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(x + 5, y + getIconHeight() / 2, x + getIconWidth() - 5, y + getIconHeight() / 2);
        g2d.drawLine(x + getIconWidth() / 2, y + 5, x + getIconWidth() / 2, y + getIconHeight() - 5);

        g2d.dispose();
    }

    @Override
    public int getIconWidth()
    {
        return (45);
    }

    @Override
    public int getIconHeight()
    {
        return (45);
    }
}