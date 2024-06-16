package me.dreamvoid.miraimcaddon.overflow;

import me.dreamvoid.miraimc.LifeCycle;
import me.dreamvoid.miraimc.internal.loader.LibraryLoader;
import net.mamoe.mirai.Bot;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import top.mrxiaom.overflow.BotBuilder;

@SuppressWarnings("unused")
public final class BukkitPlugin extends JavaPlugin {
    Bot bot = null;

    public BukkitPlugin(){
        // 抢先在MiraiMC预加载之前加载Overflow，所以必须在插件实例化后就进行
        getLogger().info("Calling MiraiMC to load Overflow core.");
        LibraryLoader loader = LifeCycle.getPlatform().getLibraryLoader();
        try {
            loader.loadLibraryMaven("top.mrxiaom", "overflow-core-all", "2.16.0-db61867-SNAPSHOT", "https://s01.oss.sonatype.org/content/repositories/snapshots", "-all.jar", getDataFolder().toPath());
            System.setProperty("MiraiMC.do-not-load-mirai-core", "Overflow");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        connect();
    }

    @Override
    public void onDisable() {
        if(bot != null){
            bot.close();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length > 0){
            if(args[0].equalsIgnoreCase("reload")){
                reloadConfig();
                Command.broadcastCommandMessage(sender, ChatColor.GREEN + "已重新加载Overflow配置。");
            } else if (args[0].equalsIgnoreCase("connect")){
                Command.broadcastCommandMessage(sender, ChatColor.GREEN + "尝试连接到Onebot，请查看控制台以了解更多信息。");
                connect();
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <reload|connect>");
            }
        } else {
            sender.sendMessage("This server is running "+ getDescription().getName() +" version "+  getDescription().getVersion() +" by "+ getDescription().getAuthors().toString().replace("[","").replace("]",""));
        }
        return true;
    }

    private void connect(){
        new BukkitRunnable() {
            @Override
            public void run() {
                switch (getConfig().getString("type", "none")){
                    case "positive":{
                        bot = BotBuilder.positive(getConfig().getString("host"))
                                .token(getConfig().getString("token"))
                                .retryTimes(0)
                                .overrideLogger(getSLF4JLogger())
                                .connect();
                        break;
                    }
                    case "reversed":{
                        bot = BotBuilder.reversed(getConfig().getInt("port"))
                                .token(getConfig().getString("token"))
                                .retryTimes(0)
                                .overrideLogger(getSLF4JLogger())
                                .connect();
                        break;
                    }
                    case "none":{
                        getLogger().severe("Please check config file.");
                    }
                }
            }
        }.runTaskAsynchronously(this);
    }
}
