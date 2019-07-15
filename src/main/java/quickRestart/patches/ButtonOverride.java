package quickRestart.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.screens.options.AbandonRunButton;
import com.megacrit.cardcrawl.screens.options.SettingsScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

import static quickRestart.helper.RestartRunHelper.restartRun;

public class ButtonOverride {
    /*@SpirePatch(clz = AbandonRunButton.class, method = "update")
    public static class AbadonRunOverride {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> Insert(AbandonRunButton __instance) {
            if (true) {
                restartRun();
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SettingsScreen.class, "popup");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }*/
}