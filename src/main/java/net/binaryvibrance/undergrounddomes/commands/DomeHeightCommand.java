package net.binaryvibrance.undergrounddomes.commands;

import net.binaryvibrance.undergrounddomes.configuration.ConfigurationHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by CodeWarrior on 15/06/2014.
 */
public class DomeHeightCommand extends CommandBase {
    @Override
    public String getName() {
        return "domeHeight";
    }

    @Override
    public String getUsage(ICommandSender commandSender) {
        return "domeHeight [domeHeight]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender commandSender, String[] arguments) {
        ConfigurationHandler config = ConfigurationHandler.instance();
        int domeHeight = Integer.parseInt(arguments[0]);
        config.setDomeHeight(domeHeight);
        if (commandSender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)commandSender;
            player.sendMessage(new TextComponentString("New Domes will be generated at " + domeHeight));
        }
    }
}
