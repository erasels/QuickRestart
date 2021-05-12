package quickRestart.helper;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.SeedHelper;
import com.megacrit.cardcrawl.helpers.TipTracker;
import com.megacrit.cardcrawl.metrics.MetricData;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.DungeonTransitionScreen;
import com.megacrit.cardcrawl.shop.ShopScreen;
import quickRestart.QuickRestart;
import quickRestart.patches.FixAscenscionUnlockOnGameoverWinPatch;

public class RestartRunHelper {
    public static boolean queuedRestart;
    public static boolean queuedScoreRestart;
    public static boolean queuedRoomRestart;

    public static void restartRun() {
        CardCrawlGame.music.fadeAll();
        if (Settings.AMBIANCE_ON)
            CardCrawlGame.sound.stop("WIND");
        AbstractDungeon.getCurrRoom().clearEvent();

        //Fix Ascenscion unlock problem if beating third boss and not doing heart
        if(FixAscenscionUnlockOnGameoverWinPatch.updateAscProgress) {
            if(AbstractDungeon.screen == AbstractDungeon.CurrentScreen.DEATH) {
                ReflectionHacks.privateMethod(DeathScreen.class, "updateAscensionProgress").invoke(AbstractDungeon.deathScreen);
            }
        }

        //Safety check to not call this method when the player restarts from the death/victory screen. This may cause crashes otherwise
        if(!queuedRestart) {
            AbstractDungeon.closeCurrentScreen();
        }
        //AbstractDungeon.dungeonMapScreen.closeInstantly();
        CardCrawlGame.dungeonTransitionScreen = new DungeonTransitionScreen(Exordium.ID);
        //AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;

        AbstractDungeon.reset();
        Settings.hasEmeraldKey = false;
        Settings.hasRubyKey = false;
        Settings.hasSapphireKey = false;
        ShopScreen.resetPurgeCost();
        CardCrawlGame.tips.initialize();
        CardCrawlGame.metricData.clearData();
        CardHelper.clear();
        TipTracker.refresh();
        System.gc();
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
        queuedRestart = false;
    }

    public static void scoreAndRestart() {
        AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
        QuickRestart.runLogger.info("Run has been scored.");
        restartRun();
        queuedScoreRestart = false;
    }

    public static void restartRoom() {
        CardCrawlGame.music.fadeAll();
        AbstractDungeon.closeCurrentScreen();
        AbstractDungeon.dungeonMapScreen.closeInstantly();
        AbstractDungeon.reset();
        CardCrawlGame.loadingSave = true;
        CardCrawlGame.mode = CardCrawlGame.GameMode.CHAR_SELECT;
        QuickRestart.runLogger.info("Room has been reset.");
        queuedRoomRestart = false;
    }
}
