package quickRestart.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;
import quickRestart.QuickRestart;
import quickRestart.ui.RoomRestartButton;

public class CancelButtonPatches {
    //Create Button Object for the panel
    @SpirePatch(
            clz = CancelButton.class,
            method = SpirePatch.CLASS
    )
    public static class RoomRestartButtonField {
        public static SpireField<RoomRestartButton> restartField = new SpireField<>(() -> null);
    }

    //Add it in the ctor
    @SpirePatch(
            clz = CancelButton.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class RButtonAdder {
        public static void Postfix(CancelButton __instance) {
            RoomRestartButtonField.restartField.set(__instance, new RoomRestartButton());
        }
    }

    //Call its update when needed
    @SpirePatch(clz = CancelButton.class, method = "update")
    public static class OptionsUpdateInjecter {
        public static void Postfix(CancelButton __instance) {
            if (QuickRestart.isRR() && CardCrawlGame.isInARun() && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.SETTINGS && (AbstractDungeon.getCurrRoom() instanceof MonsterRoom || AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite || AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) && !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                RoomRestartButtonField.restartField.get(__instance).update();
            }
        }
    }

    //Call its render when needed
    @SpirePatch(clz = CancelButton.class, method = "render")
    public static class OptionsRenderInjecter {
        public static void Postfix(CancelButton __instance, SpriteBatch sb) {
            if (QuickRestart.isRR() && CardCrawlGame.isInARun() && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.SETTINGS && (AbstractDungeon.getCurrRoom() instanceof MonsterRoom || AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite || AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) && !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                RoomRestartButtonField.restartField.get(__instance).render(sb);
            }
        }
    }

    //Show when needed
    @SpirePatch(clz = CancelButton.class, method = "show")
    @SpirePatch(clz = CancelButton.class, method = "showInstantly")
    public static class OptionsShowInjecter {
        public static void Postfix(CancelButton __instance, String text) {
            if (QuickRestart.isRR() && CardCrawlGame.isInARun() && (AbstractDungeon.getCurrRoom() instanceof MonsterRoom || AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite || AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) && !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                RoomRestartButtonField.restartField.get(__instance).showInstantly(text);
            }
        }
    }

    //Hide when needed
    @SpirePatch(clz = CancelButton.class, method = "hide")
    @SpirePatch(clz = CancelButton.class, method = "hideInstantly")
    public static class OptionsHideInjecter {
        public static void Postfix(CancelButton __instance) {
            RoomRestartButtonField.restartField.get(__instance).hideInstantly();
        }
    }
}
