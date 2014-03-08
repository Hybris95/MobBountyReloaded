package org.nunnerycode.bukkit.mobbountyreloaded.listeners;

import net.nunnerycode.bukkit.libraries.ivory.utils.MessageUtils;
import net.nunnerycode.bukkit.libraries.ivory.utils.RandomRangeUtils;

import org.apache.commons.lang.math.DoubleRange;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.nunnerycode.bukkit.mobbountyreloaded.MobBountyReloadedPlugin;

import java.text.DecimalFormat;

public final class EntityListener implements Listener {

  private MobBountyReloadedPlugin plugin;
  private DecimalFormat decimalFormat = new DecimalFormat("#.##");

  public EntityListener(MobBountyReloadedPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onEntityDeathEvent(EntityDeathEvent event) {
    LivingEntity livingEntity = event.getEntity();
    Player player = livingEntity.getKiller();
    if (player == null) {
      return;
    }
    DoubleRange rewardRange = plugin.getMobHandler().getReward(livingEntity.getType());
    double d =
        RandomRangeUtils.randomRangeDoubleInclusive(rewardRange.getMinimumDouble(),
                                                    rewardRange.getMaximumDouble());
    plugin.getEconomyHandler().transaction(player, d);
    if (d > 0.0) {
      MessageUtils.sendColoredArgumentMessage(player, plugin.getIvorySettings()
          .getString("language.messages.reward", "language.messages.reward"),
                                              new String[][]{{"%amount%", plugin.getEconomyHandler().format(d)},
                                                             {"%mob%",
                                                              livingEntity.getType().name()}});
    } else if (d < 0.0) {
      MessageUtils.sendColoredArgumentMessage(player, plugin.getIvorySettings()
          .getString("language.messages.fine", "language.messages.fine"),
                                              new String[][]{{"%amount%", plugin.getEconomyHandler().format(Math.abs(d))},
                                                             {"%mob%",
                                                              livingEntity.getType().name()}});
    }
  }

}
