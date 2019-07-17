package quickRestart;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostRenderSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quickRestart.helper.RestartRunHelper;

import java.io.IOException;
import java.util.Properties;

@SpireInitializer
public class QuickRestart implements
        PostInitializeSubscriber,
        EditStringsSubscriber,
        PostRenderSubscriber {

    private static SpireConfig modConfig = null;
    private static String modID;
    public static final Logger runLogger = LogManager.getLogger(QuickRestart.class.getName());

    public static void initialize() {
        BaseMod.subscribe(new QuickRestart());
        setModID("quickRestart");

        try {
            Properties defaults = new Properties();
            defaults.put("EndRestart", Boolean.toString(true));
            defaults.put("SettingsRestart", Boolean.toString(true));
            defaults.put("RoomRestart", Boolean.toString(true));
            modConfig = new SpireConfig("quickRestart", "Config", defaults);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isER() {
        if (modConfig == null) {
            return false;
        }
        return modConfig.getBool("EndRestart");
    }

    public static boolean isSR() {
        if (modConfig == null) {
            return false;
        }
        return modConfig.getBool("SettingsRestart");
    }

    public static boolean isRR() {
        if (modConfig == null) {
            return false;
        }
        return modConfig.getBool("RoomRestart");
    }

    @Override
    public void receivePostInitialize() {
        runLogger.info("Quick Restart is active.");

        UIStrings UIStrings = CardCrawlGame.languagePack.getUIString(QuickRestart.makeID("OptionsMenu"));
        String[] TEXT = UIStrings.TEXT;

        ModPanel settingsPanel = new ModPanel();
        ModLabeledToggleButton ERBtn = new ModLabeledToggleButton(TEXT[0], 350, 700, Settings.CREAM_COLOR, FontHelper.charDescFont, isER(), settingsPanel, l -> {
        },
                button ->
                {
                    if (modConfig != null) {
                        modConfig.setBool("EndRestart", button.enabled);
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(ERBtn);

        ModLabeledToggleButton SRBtn = new ModLabeledToggleButton(TEXT[1], 350, 650, Settings.CREAM_COLOR, FontHelper.charDescFont, isSR(), settingsPanel, l -> {
        },
                button ->
                {
                    if (modConfig != null) {
                        modConfig.setBool("SettingsRestart", button.enabled);
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(SRBtn);

        ModLabeledToggleButton RRBtn = new ModLabeledToggleButton(TEXT[2], 350, 600, Settings.CREAM_COLOR, FontHelper.charDescFont, isRR(), settingsPanel, l -> {
        },
                button ->
                {
                    if (modConfig != null) {
                        modConfig.setBool("RoomRestart", button.enabled);
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(RRBtn);

        BaseMod.registerModBadge(ImageMaster.loadImage(getModID() + "Resources/img/modBadge.png"), getModID(), "erasels", "TODO", settingsPanel);
    }

    @Override
    public void receiveEditStrings() {
        loadLocStrings("eng");
        if(!languageSupport().equals("eng")) {
            loadLocStrings(languageSupport());
        }
    }

    public static String getModID() {
        return modID;
    }

    public static void setModID(String id) {
        modID = id;
    }

    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    @Override
    public void receivePostRender(SpriteBatch spriteBatch) {
        if(RestartRunHelper.queuedScoreRestart) {
            runLogger.info("Scoring and run restart has been initialized. (Settings)");
            RestartRunHelper.scoreAndRestart();
        } else if(RestartRunHelper.queuedRestart) {
            runLogger.info("Run restart has been initialized. (Death/Victory)");
            RestartRunHelper.restartRun();
        } else if(RestartRunHelper.queuedRoomRestart) {
            runLogger.info("Room restart has been initialized. (Settings)");
            RestartRunHelper.restartRoom();
        }
    }

    private String languageSupport()
    {
        switch (Settings.language) {
            case ZHS:
                return "zhs";
            default:
                return "eng";
        }
    }

    private void loadLocStrings(String language) {
        BaseMod.loadCustomStringsFile(UIStrings.class, getModID() + "Resources/localization/"+language + "/UI-Strings.json");
    }
}