package quickRestart.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import quickRestart.QuickRestart;
import quickRestart.helper.RestartRunHelper;

public class RoomRestartButton {
    public static final String ID = QuickRestart.makeID("RoomRestartButton");
    public static UIStrings UIStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = UIStrings.TEXT;
    private static final int W = 512;
    private static final int H = 256;
    private static final Color HOVER_BLEND_COLOR = new Color(1.0F, 1.0F, 1.0F, 0.4F);
    private static final float HITBOX_W = 300.0F * Settings.scale;
    private static final float HITBOX_H = 100.0F * Settings.scale;
    private static final float SHOW_X = 256.0F * Settings.scale;
    private static final float DRAW_Y = (128.0F + (HITBOX_H *1.25f)) * Settings.scale;
    public static final float HIDE_X = SHOW_X - 400.0F * Settings.scale;
    public float current_x = HIDE_X;
    private float target_x = this.current_x;
    public boolean isHidden = true;
    private float glowAlpha = 0.0F;
    private Color glowColor = Settings.GOLD_COLOR.cpy();
    public String buttonText = "NOT_SET";
    private static final float TEXT_OFFSET_X = -136.0F * Settings.scale;
    private static final float TEXT_OFFSET_Y = 57.0F * Settings.scale;
    public Hitbox hb = new Hitbox(0.0F, 0.0F, HITBOX_W, HITBOX_H);

    public RoomRestartButton() {
        this.hb.move(SHOW_X - 106.0F * Settings.scale, DRAW_Y + (60.0F) * Settings.scale);
    }

    public void update() {
        if (!this.isHidden) {
            updateGlow();
            this.hb.update();
            if ((InputHelper.justClickedLeft) && (this.hb.hovered)) {
                this.hb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
            }
            if (this.hb.justHovered) {
                CardCrawlGame.sound.play("UI_HOVER");
            }
            if (hb.clicked) {
                hb.clicked = false;
                hide();
                RestartRunHelper.queuedRoomRestart = true;
            }
        }
    }

    private void updateGlow() {
        this.glowAlpha += Gdx.graphics.getDeltaTime() * 3.0F;
        if (this.glowAlpha < 0.0F) {
            this.glowAlpha *= -1.0F;
        }
        float tmp = MathUtils.cos(this.glowAlpha);
        if (tmp < 0.0F) {
            this.glowColor.a = (-tmp / 2.0F + 0.3F);
        } else {
            this.glowColor.a = (tmp / 2.0F + 0.3F);
        }
    }

    public boolean hovered() {
        return hb.hovered;
    }

    public void hide() {
        if (!this.isHidden) {
            this.hb.hovered = false;
            InputHelper.justClickedLeft = false;
            this.target_x = HIDE_X;
            this.isHidden = true;
        }
    }

    public void hideInstantly() {
        if (!this.isHidden) {
            this.hb.hovered = false;
            InputHelper.justClickedLeft = false;
            this.target_x = HIDE_X;
            this.current_x = this.target_x;
            this.isHidden = true;
        }
    }

    public void show(String buttonText) {
        buttonText = TEXT[0];
        if (this.isHidden) {
            this.glowAlpha = 0.0F;
            this.current_x = HIDE_X;
            this.target_x = SHOW_X;
            this.isHidden = false;
            this.buttonText = buttonText;
        } else {
            this.current_x = HIDE_X;
            this.buttonText = buttonText;
        }
        this.hb.hovered = false;
    }

    public void showInstantly(String buttonText) {
        this.current_x = SHOW_X;
        this.target_x = SHOW_X;
        this.isHidden = false;
        this.buttonText = TEXT[0];
        this.hb.hovered = false;
    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        renderShadow(sb);
        sb.setColor(this.glowColor);
        renderOutline(sb);
        sb.setColor(Color.YELLOW);
        renderButton(sb);
        if ((this.hb.hovered) && (!this.hb.clickStarted)) {
            sb.setBlendFunction(770, 1);
            sb.setColor(HOVER_BLEND_COLOR);
            renderButton(sb);
            sb.setBlendFunction(770, 771);
        }
        Color tmpColor = Settings.LIGHT_YELLOW_COLOR;
        if (this.hb.clickStarted) {
            tmpColor = Color.LIGHT_GRAY;
        }
        FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, this.buttonText, this.current_x + TEXT_OFFSET_X, DRAW_Y + TEXT_OFFSET_Y, tmpColor);
        if (!this.isHidden) {
            hb.render(sb);
        }
    }

    private void renderShadow(SpriteBatch sb) {
        sb.draw(ImageMaster.CANCEL_BUTTON_SHADOW, this.current_x - 256.0F, DRAW_Y - 128.0F, 256.0F, 128.0F, 512.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 512, 256, false, false);
    }

    private void renderOutline(SpriteBatch sb) {
        sb.draw(ImageMaster.CANCEL_BUTTON_OUTLINE, this.current_x - 256.0F, DRAW_Y - 128.0F, 256.0F, 128.0F, 512.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 512, 256, false, false);
    }

    private void renderButton(SpriteBatch sb) {
        sb.draw(ImageMaster.CANCEL_BUTTON, this.current_x - 256.0F, DRAW_Y - 128.0F, 256.0F, 128.0F, 512.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 512, 256, false, false);
    }
}