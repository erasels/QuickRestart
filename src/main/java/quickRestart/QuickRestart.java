package quickRestart;

import basemod.BaseMod;
import basemod.interfaces.*;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

@SpireInitializer
public class QuickRestart implements
        PostInitializeSubscriber {

    private static SpireConfig modConfig = null;


    public static void initialize() {
        BaseMod.subscribe(new QuickRestart());
    }

    @Override
    public void receivePostInitialize() {
        System.out.println("Quick Restart is active.");
    }
}