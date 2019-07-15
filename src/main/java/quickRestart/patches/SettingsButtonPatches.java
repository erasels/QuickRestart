package quickRestart.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.screens.options.AbandonRunButton;
import com.megacrit.cardcrawl.screens.options.OptionsPanel;
import com.megacrit.cardcrawl.screens.options.SettingsScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import quickRestart.ui.SettingsRestartButton;

import java.util.ArrayList;

public class SettingsButtonPatches {
    //Create Button Object for the panel
    @SpirePatch(
            clz= OptionsPanel.class,
            method= SpirePatch.CLASS
    )
    public static class RestartButtonField {
        public static SpireField<SettingsRestartButton> restartField = new SpireField<>(() -> null);
    }

    //Add it in the ctor
    @SpirePatch(
            clz= OptionsPanel.class,
            method= SpirePatch.CONSTRUCTOR
    )
    public static class RButtonAdder {
        public static void Postfix(OptionsPanel __instance) {
            RestartButtonField.restartField.set(__instance, new SettingsRestartButton());
        }
    }

    //Call its update when needed
    @SpirePatch(clz = OptionsPanel.class, method = "update")
    public static class OptionsUpdateInjecter {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(OptionsPanel __instance) {
            RestartButtonField.restartField.get(__instance).update();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbandonRunButton.class, "update");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }

    //Rendered it when needed
    @SpirePatch(clz = OptionsPanel.class, method = "render")
    public static class OptionsRenderInjecter {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(OptionsPanel __instance, SpriteBatch sb) {
            RestartButtonField.restartField.get(__instance).render(sb);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbandonRunButton.class, "render");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }
}
