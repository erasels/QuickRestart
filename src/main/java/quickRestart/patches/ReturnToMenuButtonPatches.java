package quickRestart.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.rooms.VictoryRoom;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import com.megacrit.cardcrawl.ui.buttons.ReturnToMenuButton;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import quickRestart.ui.EndRestartButton;

import java.util.ArrayList;

public class ReturnToMenuButtonPatches {
    //Create Button Object for the panel
    @SpirePatch(
            clz= ReturnToMenuButton.class,
            method= SpirePatch.CLASS
    )
    public static class EndRestartButtonField {
        public static SpireField<EndRestartButton> restartField = new SpireField<>(() -> null);
    }

    //Add it in the ctor
    @SpirePatch(
            clz= ReturnToMenuButton.class,
            method= SpirePatch.CONSTRUCTOR
    )
    public static class RButtonAdder {
        public static void Postfix(ReturnToMenuButton __instance) {
            EndRestartButtonField.restartField.set(__instance, new EndRestartButton());
        }
    }

    //Appear when needed
    @SpirePatch(clz = ReturnToMenuButton.class, method = "appear")
    public static class AppearInjecter {
        public static void Postfix(ReturnToMenuButton __instance, float x, float y, String label) {
            EndRestartButtonField.restartField.get(__instance).appear(x, y, label);
        }
    }

    //Hide
    @SpirePatch(clz = ReturnToMenuButton.class, method = "hide")
    public static class HideInjecter {
        public static void Postfix(ReturnToMenuButton __instance) {
            EndRestartButtonField.restartField.get(__instance).hide();
        }
    }

    //Call its update when needed
    @SpirePatch(clz = ReturnToMenuButton.class, method = "update")
    public static class OptionsUpdateInjecter {
        public static void Postfix(ReturnToMenuButton __instance) {
            EndRestartButtonField.restartField.get(__instance).update();
        }
    }

    //Rendered it when needed
    @SpirePatch(clz = ReturnToMenuButton.class, method = "render")
    public static class OptionsRenderInjecter {
        public static void Postfix(ReturnToMenuButton __instance, SpriteBatch sb) {
            EndRestartButtonField.restartField.get(__instance).render(sb);
        }
    }

    //Remove it when not used
    /*@SpirePatch(clz = ReturnToMenuButton.class, method = "update")
    public static class DynamicHider {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(ReturnToMenuButton __instance) {
            EndRestartButtonField.restartField.get(__instance).moved = true;
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SoundMaster.class, "play");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }*/

    //Follow the fancy slide animation
    @SpirePatch(clz = VictoryScreen.class, method = "updateStatsScreen")
    public static class DynamicSliderVictory {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(VictoryScreen __instance) {
            EndRestartButtonField.restartField.get(__instance).y = Interpolation.pow3In.apply(Settings.HEIGHT * 0.1F, Settings.HEIGHT * 0.15F, ((float)ReflectionHacks.getPrivate(__instance, VictoryScreen.class, "statsTimer"))* 1.0F / 0.5F);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Interpolation.PowIn.class, "apply");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }

    @SpirePatch(clz = DeathScreen.class, method = "updateStatsScreen")
    public static class DynamicSliderDeath {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(DeathScreen __instance) {
            EndRestartButtonField.restartField.get(__instance).y = Interpolation.pow3In.apply(Settings.HEIGHT * 0.1F, Settings.HEIGHT * 0.15F, ((float)ReflectionHacks.getPrivate(__instance, DeathScreen.class, "statsTimer"))* 1.0F / 0.5F);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Interpolation.PowIn.class, "apply");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }
}
