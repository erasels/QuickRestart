package quickRestart.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.TintEffect;
import quickRestart.QuickRestart;
import quickRestart.helper.RestartRunHelper;

public class EndRestartButton {
    public static final int RAW_W = 512;
    private static final float BUTTON_W = 240.0F * Settings.scale;
    private static final float BUTTON_H = 160.0F * Settings.scale;
    private static final float LERP_SPEED = 9.0F;
    private static final Color TEXT_SHOW_COLOR = new Color(0.9F, 0.9F, 0.9F, 1.0F);
    private static final Color HIGHLIGHT_COLOR = new Color(1.0F, 1.0F, 1.0F, 1.0F);
    private static final Color IDLE_COLOR = new Color(0.7F, 0.7F, 0.7F, 1.0F);
    private static final Color FADE_COLOR = new Color(0.3F, 0.3F, 0.3F, 1.0F);
    public String label;
    public float x;
    public float y;
    public Hitbox hb;
    protected TintEffect tint = new TintEffect();
    protected TintEffect textTint = new TintEffect();
    public boolean pressed = false;
    public boolean isMoving = false;
    public boolean show = false;
    public int height;
    public int width;

    public static final String ID = QuickRestart.makeID("EndRestartButton");
    public static UIStrings UIStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = UIStrings.TEXT;

    public EndRestartButton() {
        tint.color.a = 0.0F;
        textTint.color.a = 0.0F;
        hb = new Hitbox(-10000.0F, -10000.0F, BUTTON_W, BUTTON_H);
    }

    public void appear(float x, float y, String label) {
        if(!Settings.isDailyRun) {
            this.x = x + (BUTTON_W * 1.25f);
            this.y = y;
            this.label = TEXT[0];
            pressed = false;
            isMoving = true;
            show = true;
            tint.changeColor(IDLE_COLOR, LERP_SPEED);
            textTint.changeColor(TEXT_SHOW_COLOR, LERP_SPEED);
        }
    }

    public void hide() {
        show = false;
        isMoving = false;
        tint.changeColor(FADE_COLOR, LERP_SPEED);
        textTint.changeColor(FADE_COLOR, LERP_SPEED);
    }

    public void update() {
        tint.update();
        textTint.update();
        if (show) {
            hb.move(x, y);
            hb.update();
            if ((InputHelper.justClickedLeft) && (hb.hovered)) {
                hb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
                RestartRunHelper.restartRun();
            }
            if (hb.justHovered) {
                CardCrawlGame.sound.play("UI_HOVER");
            }
            if ((hb.hovered)) {
                tint.changeColor(HIGHLIGHT_COLOR, 18.0F);
            } else {
                tint.changeColor(IDLE_COLOR, LERP_SPEED);
            }
        }
    }

    public void render(SpriteBatch sb) {
        if ((textTint.color.a == 0.0F) || (label == null)) {
            return;
        }
        if (hb.clickStarted) {
            sb.setColor(Color.LIGHT_GRAY);
        } else {
            sb.setColor(tint.color);
        }
        sb.draw(ImageMaster.DYNAMIC_BTN_IMG2, x - 256.0F, y - 256.0F, 256.0F, 256.0F, RAW_W, RAW_W, Settings.scale, Settings.scale, 0.0F, 0, 0, 512, 512, false, false);
        if (hb.clickStarted) {
            FontHelper.renderFontCentered(sb, FontHelper.panelEndTurnFont, label, x, y, Color.LIGHT_GRAY);
        } else {
            FontHelper.renderFontCentered(sb, FontHelper.panelEndTurnFont, label, x, y, tint.color);
        }
        if ((!pressed) && (show)) {
            hb.render(sb);
        }
    }
}
