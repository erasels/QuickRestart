package quickRestart;

import basemod.BaseMod;
import basemod.interfaces.*;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.localization.UIStrings;

@SpireInitializer
public class QuickRestart implements
        PostInitializeSubscriber,
        EditStringsSubscriber{

    private static SpireConfig modConfig = null;
    private static String modID;

    public static void initialize() {
        BaseMod.subscribe(new QuickRestart());
        setModID("quickRestart");
    }

    @Override
    public void receivePostInitialize() {
        System.out.println("Quick Restart is active.");
    }

    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(UIStrings.class,
                getModID() + "Resources/localization/eng/UI-Strings.json");
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
}