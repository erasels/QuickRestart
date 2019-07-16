package quickRestart.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import quickRestart.QuickRestart;
import quickRestart.helper.RestartRunHelper;

public class SettingsRestartButton {
    public static final String ID = QuickRestart.makeID("SettingsRestartButton");
    public static UIStrings UIStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = UIStrings.TEXT;
    private int W = 440;
    private int H = 100;
    private Hitbox hb;
    private float x;
    private float y;

    public SettingsRestartButton() {
        hb = new Hitbox(340.0F * Settings.scale, 70.0F * Settings.scale);
        x = (1430.0F * Settings.scale);
        y = (Settings.OPTION_Y + 440.0F * Settings.scale);
        hb.move(x, y);
    }

    public void update() {
        if(!Settings.isDailyRun) {
            hb.update();
            if (hb.justHovered) {
                CardCrawlGame.sound.play("UI_HOVER");
            }
            if ((InputHelper.justClickedLeft) && (hb.hovered)) {
                CardCrawlGame.sound.play("UI_CLICK_1");
                hb.clickStarted = true;
            }
            if ((hb.clicked) || (CInputActionSet.proceed.isJustPressed())) {
                CInputActionSet.proceed.unpress();
                hb.clicked = false;
                RestartRunHelper.scoreAndRestart();
            }
        }
    }

    public void render(SpriteBatch sb) {
        //TODO: Better way
        if(!Settings.isDailyRun) {
            sb.setColor(Color.GREEN);
            sb.draw(ImageMaster.OPTION_ABANDON, x - W / 2.0F, y - H / 2.0F, W / 2.0F, H / 2.0F, W, H, Settings.scale, Settings.scale, 0.0F, 0, 0, W, H, false, false);

            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, TEXT[0], x + 15.0F * Settings.scale, y + 5.0F * Settings.scale, Settings.GOLD_COLOR);
            if (hb.hovered) {
                sb.setBlendFunction(770, 1);
                sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.2F));
                sb.draw(ImageMaster.OPTION_ABANDON, x - W / 2.0F, y - H / 2.0F, W / 2.0F, H / 2.0F, W, H, Settings.scale, Settings.scale, 0.0F, 0, 0, W, H, false, false);

                sb.setBlendFunction(770, 771);
            }
            hb.render(sb);
        }
    }
}
