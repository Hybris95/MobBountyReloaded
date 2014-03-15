package org.nunnerycode.bukkit.mobbountyreloaded.commands;

import net.nunnerycode.bukkit.libraries.ivory.utils.MessageUtils;

import org.apache.commons.lang.math.DoubleRange;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.nunnerycode.bukkit.mobbountyreloaded.MobBountyReloadedPlugin;

import se.ranzdo.bukkit.methodcommand.Arg;
import se.ranzdo.bukkit.methodcommand.Command;

import java.text.DecimalFormat;

public class MobBountyCommands {

  private MobBountyReloadedPlugin plugin;
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

  public MobBountyCommands(MobBountyReloadedPlugin plugin) {
    this.plugin = plugin;
  }

  @Command(identifier = "mb list", permissions = "mobbountyreloaded.command.list")
  public void listSubcommand(CommandSender sender,
                             @Arg(name = "world", def = "") String worldName) {
    if (worldName.equals("")) {
      if (!(sender instanceof Player)) {
        MessageUtils.sendColoredMessage(sender, plugin.getIvorySettings()
            .getString("language.messages.no-access", "language.messages.no-access"));
        return;
      }
      worldName = ((Player) sender).getWorld().getName();
    }
    World world = Bukkit.getWorld(worldName);
    if (world == null) {
      MessageUtils.sendColoredMessage(sender, plugin.getIvorySettings()
          .getString("language.messages.no-access", "language.messages.world-does-not-exist"));
      return;
    }
    for (EntityType entityType : EntityType.values()) {
      DoubleRange doubleRange = plugin.getMobHandler().getReward(entityType, world);
      if (doubleRange.getMaximumDouble() < 0.0) {
        if (doubleRange.getMaximumDouble() == doubleRange.getMinimumDouble()) {
          MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings()
              .getString("language.messages.list-fine", ""), new String[][]{
              {"%value%", DECIMAL_FORMAT.format(doubleRange.getMaximumDouble())}});
        } else {
          MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings()
              .getString("language.messages.list-fine-range", ""), new String[][]{
              {"%value1%", DECIMAL_FORMAT.format(doubleRange.getMinimumDouble())},
              {"%value2%", DECIMAL_FORMAT.format(doubleRange.getMaximumDouble())}});
        }
      } else if (doubleRange.getMaximumDouble() > 0.0) {
        if (doubleRange.getMaximumDouble() == doubleRange.getMinimumDouble()) {
          MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings()
              .getString("language.messages.list-reward", ""), new String[][]{
              {"%value%", DECIMAL_FORMAT.format(doubleRange.getMaximumDouble())}});
        } else {
          MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings()
              .getString("language.messages.list-reward-range", ""), new String[][]{
              {"%value1%", DECIMAL_FORMAT.format(doubleRange.getMaximumDouble())},
              {"%value2%", DECIMAL_FORMAT.format(doubleRange.getMinimumDouble())}});
        }
      }
    }
  }


}
