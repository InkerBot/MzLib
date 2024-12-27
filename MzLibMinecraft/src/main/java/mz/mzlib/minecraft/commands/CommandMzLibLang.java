package mz.mzlib.minecraft.commands;

import mz.mzlib.minecraft.MzLibMinecraft;
import mz.mzlib.minecraft.command.Command;
import mz.mzlib.minecraft.i18n.I18nMinecraft;
import mz.mzlib.minecraft.permission.Permission;
import mz.mzlib.minecraft.text.Text;
import mz.mzlib.module.MzModule;

public class CommandMzLibLang extends MzModule
{
    public static CommandMzLibLang instance = new CommandMzLibLang();
    
    public Permission permission=new Permission("mzlib.command.mzlib.lang");
    
    public Command command;
    
    @Override
    public void onLoad()
    {
        this.register(this.permission);
        MzLibMinecraft.instance.command.addChild(this.command=new Command("lang").setPermissionChecker(Command.permissionChecker(this.permission)).addChild(new Command("loadmc").setHandler(context->
        {
            if(context.argsReader.hasNext())
                context.successful=false;
            if(!context.successful || !context.doExecute)
                return;
            if(I18nMinecraft.instance.taskLoading!=null)
            {
                context.sender.sendMessage(Text.literal(I18nMinecraft.getTranslation(context.sender, "mzlib.commands.mzlib.lang.loadmc.loading")));
                return;
            }
            I18nMinecraft.instance.loadLanguages();
            context.sender.sendMessage(Text.literal(I18nMinecraft.getTranslation(context.sender, "mzlib.commands.mzlib.lang.loadmc.begin")));
        })));
    }
    
    @Override
    public void onUnload()
    {
        MzLibMinecraft.instance.command.removeChild(this.command);
    }
}
