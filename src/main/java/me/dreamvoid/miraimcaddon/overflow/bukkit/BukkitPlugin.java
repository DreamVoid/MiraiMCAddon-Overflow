package me.dreamvoid.miraimcaddon.overflow.bukkit;

import me.dreamvoid.miraimcaddon.overflow.OverflowLifeCycle;
import me.dreamvoid.miraimcaddon.overflow.Platform;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;

@SuppressWarnings("unused")
public final class BukkitPlugin extends JavaPlugin implements Platform {
    private final OverflowLifeCycle lifeCycle;

    public BukkitPlugin(){
        lifeCycle = new OverflowLifeCycle(this);
        lifeCycle.loadOverflowLibrary();
    }

    @Override
    public void onLoad() {
        lifeCycle.loadConfig();
    }

    @Override
    public void onEnable() {
        lifeCycle.connect();
    }

    @Override
    public void onDisable() {
        lifeCycle.disconnect();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length > 0){
            if(args[0].equalsIgnoreCase("reload")){
                lifeCycle.loadConfig();
                Command.broadcastCommandMessage(sender, Component.text("已重新加载Overflow配置。", NamedTextColor.GREEN));
            } else if (args[0].equalsIgnoreCase("connect")){
                Command.broadcastCommandMessage(sender, Component.text("尝试连接到OneBot，请查看控制台以了解更多信息。", NamedTextColor.GREEN));
                lifeCycle.connect();
            } else if (args[0].equalsIgnoreCase("disconnect")) {
                Command.broadcastCommandMessage(sender, Component.text("正在断开OneBot的连接，请查看控制台以了解更多信息。", NamedTextColor.GREEN));
                lifeCycle.disconnect();
            } else {
                sender.sendMessage(Component.text("Usage: /" + label + " <reload|connect|disconnect>", NamedTextColor.RED));
            }
        } else {
            //noinspection UnstableApiUsage
            sender.sendMessage("This server is running " + getPluginMeta().getName() + " version " + getPluginMeta().getVersion() + " by " + String.join(", ", getPluginMeta().getAuthors()));
        }
        return true;
    }

    @Override
    public Logger getPluginLogger() {
        return getSLF4JLogger();
    }

    @Override
    public Path getDataPath() {
        return getDataFolder().toPath();
    }
}
