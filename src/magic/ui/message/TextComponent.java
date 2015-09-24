package magic.ui.message;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import magic.model.MagicMessage;

public class TextComponent extends TComponent {

    public static MessageStyle messageStyle = MessageStyle.PLAIN;

    private final String text;
    private final Font font;
    private final Color fontColor;
    private final FontMetrics metrics;
    private final boolean newLine;
    private final String cardInfo;

    TextComponent(
        final String text,
        final JComponent component,
        final Font aFont,
        final boolean isChoice,
        final String aCardInfo,
        final Color choiceColor) {

        this.text = text;
        this.cardInfo = aCardInfo;

        this.fontColor = getTextColor(isChoice, choiceColor);
        this.font = getTextFont(aFont);
        this.metrics = component.getFontMetrics(this.font);
        
        this.newLine = !(".".equals(text) || ",".equals(text));

    }
    
    private Color getTextColor(boolean isChoice, Color choiceColor) {
        return isCardId() & !isChoice
            ? Color.DARK_GRAY
            : isInteractive() && messageStyle != MessageStyle.PLAINBOLDMONO
                ? Color.BLUE
                : isChoice
                    ? choiceColor
                    : Color.BLACK;        
    }

    private Font getTextFont(final Font aFont) {
        final boolean isBoldFont =
            messageStyle != MessageStyle.PLAIN
            && (isInteractive() || messageStyle == MessageStyle.BOLD)
            && !isCardId();
        return messageStyle == MessageStyle.PLAIN
            ? aFont
            : isBoldFont
                ? aFont.deriveFont(Font.BOLD)
                : aFont;
    }

    private boolean isCardId() {
        return text.startsWith("#");
    }

    @Override
    boolean requiresNewLine() {
        return newLine;
    }

    @Override
    Dimension getPreferredSize() {
        return new Dimension(metrics.stringWidth(text) + 1, metrics.getHeight());
    }

    @Override
    void paint(final JComponent com, final Graphics g, final int x, final int y) {
        g.setColor(fontColor);
        g.setFont(font);
        g.drawString(text, lx + x, ly + y + metrics.getAscent());
    }

    @Override
    Rectangle getBounds() {
        return new Rectangle(
                lx,
                ly,
                metrics.stringWidth(text) + 1,
                metrics.getHeight());
    }

    @Override
    final boolean isInteractive() {
        return cardInfo.isEmpty() == false && getCardId() > 0;
    }

    long getCardId() {
        final String[] info = cardInfo.split(String.valueOf(MagicMessage.CARD_ID_DELIMITER));
        if (info.length > 1) {
            return Long.parseLong(info[1]);
        } else {
            return 0L;
        }
    }

}