package quickRestart.helper;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.SeedHelper;
import com.megacrit.cardcrawl.helpers.TipTracker;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.DungeonTransitionScreen;
import com.megacrit.cardcrawl.shop.ShopScreen;
import quickRestart.QuickRestart;
import quickRestart.patches.FixAscenscionUnlockOnGameoverWinPatch;

import java.lang.reflect.Field;

import static quickRestart.QuickRestart.hasDownfall;

public class RestartRunHelper {
    public static boolean queuedRestart;
    public static boolean queuedScoreRestart;
    public static boolean queuedRoomRestart;

    //Set in receiveStartGame in the main mod file, checks for downfall
    public static boolean evilMode = false;

    public static void restartRun() {
        //Stop all lingering sounds from playing
        stopLingeringSounds();
        AbstractDungeon.getCurrRoom().clearEvent();

        //Fix Ascension unlock problem if beating third boss and not doing heart
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

        if(evilMode) {
            setDownfallMode();
        }

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
        evilMode = false;
    }

    public static void scoreAndRestart() {
        AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
        QuickRestart.runLogger.info("Run has been scored.");
        restartRun();
        queuedScoreRestart = false;
    }

    public static void restartRoom() {
        stopLingeringSounds();
        AbstractDungeon.closeCurrentScreen();
        AbstractDungeon.dungeonMapScreen.closeInstantly();
        AbstractDungeon.reset();
        CardCrawlGame.loadingSave = true;
        CardCrawlGame.mode = CardCrawlGame.GameMode.CHAR_SELECT;
        QuickRestart.runLogger.info("Room has been reset.");
        queuedRoomRestart = false;
    }

    public static void stopLingeringSounds() {
        CardCrawlGame.music.fadeAll();
        if (Settings.AMBIANCE_ON)
            CardCrawlGame.sound.stop("WIND");

        if(AbstractDungeon.scene != null) {
            AbstractDungeon.scene.fadeOutAmbiance();
        }
    }

    // Downfall compat
    private static Field evilField = null;
    public static boolean isDownfallMode() {
        if(evilField == null) {
            try {
                Class<?> clz = Class.forName("downfall.patches.EvilModeCharacterSelect");
                evilField = clz.getField("evilMode");
            } catch (ClassNotFoundException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        try {
            return evilField.getBoolean(null);
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    public static void setDownfallMode() {
        if(evilField == null) {
            try {
                Class<?> clz = Class.forName("downfall.patches.EvilModeCharacterSelect");
                evilField = clz.getField("evilMode");
            } catch (ClassNotFoundException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        try {
            evilField.set(null, true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
