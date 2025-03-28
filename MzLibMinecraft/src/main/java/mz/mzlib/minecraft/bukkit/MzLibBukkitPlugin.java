package mz.mzlib.minecraft.bukkit;

import mz.mzlib.MzLib;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MzLibBukkitPlugin extends JavaPlugin
{
    public static MzLibBukkitPlugin instance;
    {
        instance = this;
    }

    @Override
    public void onEnable()
    {
        MzLib.instance.load();
        MzLib.instance.register(MzLibBukkit.instance);
    }

    @Override
    public void onDisable()
    {
        MzLib.instance.unload();
    }
    
    @Override
    public File getFile()
    {
        return super.getFile();
    }
}
