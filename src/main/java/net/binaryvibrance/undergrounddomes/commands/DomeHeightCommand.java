package net.binaryvibrance.undergrounddomes.commands;

import net.binaryvibrance.undergrounddomes.Configuration;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

/**
 * Created by CodeWarrior on 15/06/2014.
 */
public class DomeHeightCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "domeHeight";
    }

    @Override
    public String getCommandUsage(ICommandSender commandSender) {
        return "domeHeight [domeHeight]";
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] arguments) {
        Configuration config = Configuration.getConfiguration();
        int domeHeight = Integer.parseInt(arguments[0]);
        config.setDomeHeight(domeHeight);
        if (commandSender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)commandSender;
            player.addChatComponentMessage(new ChatComponentText("New Domes will be generated at " + domeHeight));
        }
    }
}
