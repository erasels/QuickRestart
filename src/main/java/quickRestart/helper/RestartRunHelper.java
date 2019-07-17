package quickRestart.helper;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.helpers.SeedHelper;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.DungeonTransitionScreen;
import com.megacrit.cardcrawl.shop.ShopScreen;
import quickRestart.QuickRestart;

public class RestartRunHelper {
    public static boolean queuedRestart;
    public static boolean queuedScoreRestart;
    public static boolean queuedRoomRestart;

    public static void restartRun() {
        queuedRestart = false;
        CardCrawlGame.music.fadeAll();
        AbstractDungeon.getCurrRoom().clearEvent();
        AbstractDungeon.closeCurrentScreen();
        //AbstractDungeon.dungeonMapScreen.closeInstantly();
        CardCrawlGame.dungeonTransitionScreen = new DungeonTransitionScreen(Exordium.ID);
        //AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;

        AbstractDungeon.reset();
        Settings.hasEmeraldKey = false;
        Settings.hasRubyKey = false;
        Settings.hasSapphireKey = false;
        ShopScreen.resetPurgeCost();
        QuickRestart.runLogger.info("Dungeon has been reset.");

        if (CardCrawlGame.chosenCharacter == null) {
            CardCrawlGame.chosenCharacter = AbstractDungeon.player.chosenClass;
        }

        if (!Settings.seedSet) {
            QuickRestart.runLogger.info("Seed wasn't set, rerandomizing seed.");
            Long sTime = System.nanoTime();
            Random rng = new Random(sTime);
            Settings.seedSourceTimestamp = sTime;
            Settings.seed = SeedHelper.generateUnoffensiveSeed(rng);
            SeedHelper.cachedSeed = null;
        }
        AbstractDungeon.generateSeeds();
        QuickRestart.runLogger.info("Seeds have been reset.");

        CardCrawlGame.mode = CardCrawlGame.GameMode.CHAR_SELECT;
        QuickRestart.runLogger.info("Run has been reset.");
    }

    public static void scoreAndRestart() {
        queuedScoreRestart = false;
        AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
        QuickRestart.runLogger.info("Run has been scored.");
        restartRun();
    }

    public static void restartRoom() {
        queuedRoomRestart = false;
        CardCrawlGame.music.fadeAll();
        AbstractDungeon.closeCurrentScreen();
        AbstractDungeon.dungeonMapScreen.closeInstantly();
        CardCrawlGame.loadingSave = true;
        CardCrawlGame.mode = CardCrawlGame.GameMode.CHAR_SELECT;
        QuickRestart.runLogger.info("Room has been reset.");
    }
}
