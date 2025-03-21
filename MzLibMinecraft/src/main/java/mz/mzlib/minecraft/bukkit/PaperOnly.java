package mz.mzlib.minecraft.bukkit;

import mz.mzlib.util.ElementSwitcher;
import mz.mzlib.util.ElementSwitcherClass;

import java.lang.annotation.*;
import java.lang.reflect.AnnotatedElement;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD})
@ElementSwitcherClass(PaperOnly.Switcher.class)
public @interface PaperOnly
{
    class Switcher implements ElementSwitcher
    {
        public boolean isEnabled(Annotation annotation, AnnotatedElement element)
        {
            return MinecraftPlatformBukkit.instance.isPaper;
        }
    }
}
