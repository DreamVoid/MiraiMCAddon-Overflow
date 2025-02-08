package me.dreamvoid.miraimcaddon.overflow;

import me.dreamvoid.miraimc.api.MiraiMC;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.LoggerAdapters;
import top.mrxiaom.overflow.BotBuilder;

public class Overflow {
    private final Platform plugin;
    private Bot bot;

    public Overflow(Platform plugin) {
        this.plugin = plugin;
    }

    public void loadOverflow() {
        try {
            MiraiMC.getPlatform().getLibraryLoader().loadLibraryMaven(
                    System.getProperty("MiraiMC.Overflow.dependency.groupId", "top.mrxiaom.mirai"),
                    System.getProperty("MiraiMC.Overflow.dependency.artifactId", "overflow-core-all"),
                    System.getProperty("MiraiMC.Overflow.dependency.version", "1.0.1"),
                    System.getProperty("MiraiMC.Overflow.repo.url", "https://repo.huaweicloud.com/repository/maven/"),
                    "-all.jar",
                    plugin.getDataPath().resolve("libs"));
            System.setProperty("MiraiMC.do-not-load-mirai-core", "Overflow");
        } catch (Exception e) {
            plugin.getPluginLogger().error("Failed to load Overflow!", e);
        }
    }

    public void connect(){
        MiraiMC.getPlatform().runTaskAsync(() -> {
            synchronized (plugin) {
                if(bot != null) {
                    plugin.getPluginLogger().info("另一个机器人进程已经存在，正在关闭其他机器人...");
                    disconnect();
                }

                switch (plugin.getConfigMap().getOrDefault("type", "none").toString()) {
                    case "positive" -> bot = BotBuilder.positive(plugin.getConfigMap().getOrDefault("host", "ws://127.0.0.1:5800").toString())
                            .modifyBotConfiguration(this::accept)
                            .token(plugin.getConfigMap().getOrDefault("token", "").toString())
                            .retryTimes(Integer.parseInt(plugin.getConfigMap().getOrDefault("retry-time", 0).toString()))
                            .overrideLogger(plugin.getPluginLogger())
                            .connect();
                    case "reversed" -> bot = BotBuilder.reversed(Integer.parseInt(plugin.getConfigMap().getOrDefault("port", 5700).toString()))
                            .modifyBotConfiguration(this::accept)
                            .token(plugin.getConfigMap().getOrDefault("token", "").toString())
                            .retryTimes(Integer.parseInt(plugin.getConfigMap().getOrDefault("retry-time", 0).toString()))
                            .overrideLogger(plugin.getPluginLogger())
                            .connect();
                    default -> plugin.getPluginLogger().error("Please check config file.");
                }
            }
        });
    }

    public void disconnect(){
        if(bot != null){
            plugin.getPluginLogger().info("Disconnecting Overflow bot.");
            bot.close();
            bot = null;
        }
    }

    private void accept(BotConfiguration configuration) {
        if (MiraiMC.getConfig().Bot_DisableBotLogs) {
            configuration.noBotLog();
        } else if (MiraiMC.getConfig().Bot_UseMinecraftLogger_BotLogs) {
            configuration.setBotLoggerSupplier(bot -> LoggerAdapters.asMiraiLogger(plugin.getPluginLogger()));
        }

        if (MiraiMC.getConfig().Bot_DisableNetworkLogs) {
            configuration.noNetworkLog();
        } else if (MiraiMC.getConfig().Bot_UseMinecraftLogger_NetworkLogs) {
            configuration.setNetworkLoggerSupplier(bot -> LoggerAdapters.asMiraiLogger(plugin.getPluginLogger()));
        }
    }
}
