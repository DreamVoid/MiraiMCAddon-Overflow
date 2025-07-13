package me.dreamvoid.miraimcaddon.overflow;

import com.google.gson.Gson;
import me.dreamvoid.miraimc.api.MiraiMC;
import me.dreamvoid.miraimcaddon.overflow.objects.Overflow;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.utils.LoggerAdapters;
import top.mrxiaom.overflow.BotBuilder;

import java.io.*;
import java.nio.file.Files;

public class OverflowLifeCycle {
    private final Platform plugin;
    private Overflow overflowConfig;
    private Bot bot;

    public OverflowLifeCycle(Platform plugin) {
        this.plugin = plugin;
    }

    /**
     * 加载 Overflow 库
     */
    public void loadOverflowLibrary() {
        try {
            MiraiMC.getPlatform().getLibraryLoader().loadLibraryMaven(
                    System.getProperty("MiraiMC.Overflow.dependency.groupId", "top.mrxiaom.mirai"),
                    System.getProperty("MiraiMC.Overflow.dependency.artifactId", "overflow-core-all"),
                    System.getProperty("MiraiMC.Overflow.dependency.version", "1.0.6"),
                    System.getProperty("MiraiMC.Overflow.repo.url", "https://repo.huaweicloud.com/repository/maven/"),
                    "-all.jar",
                    plugin.getDataPath().resolve("libs"));
            System.setProperty("MiraiMC.do-not-load-mirai-core", "Overflow");
        } catch (Exception e) {
            plugin.getPluginLogger().error("无法加载 Overflow！", e);
        }
    }

    public void loadConfig() {
        File file = plugin.getDataPath().resolve("overflow.json").toFile();
        if (!file.exists()) {
            try (InputStream is = this.getClass().getResourceAsStream("/overflow.json")) {
                assert is != null;
                Files.copy(is, file.toPath());
            } catch (IOException e) {
                plugin.getPluginLogger().error("无法保存默认的 overflow.json！", e);
            }
        }

        try {
            overflowConfig = new Gson().fromJson(new FileReader(file), Overflow.class);
        } catch (FileNotFoundException e) {
            plugin.getPluginLogger().error("读取 Overflow 配置时出现异常！", e);
        }
    }

    public void connect(){
        MiraiMC.getPlatform().runTaskAsync(() -> {
            synchronized (plugin) {
                if(bot != null) {
                    plugin.getPluginLogger().info("另一个机器人进程已经存在，正在关闭其他机器人...");
                    disconnect();
                }

                BotBuilder builder = (overflowConfig.getReversedWsPort() != -1)
                        ? BotBuilder.positive(overflowConfig.getWsHost())
                        : BotBuilder.reversed(overflowConfig.getReversedWsPort());
                builder.token(overflowConfig.getToken());
                if(overflowConfig.isNoPlatform()) builder.noPlatform();
                if(overflowConfig.isUseCqCode()) builder.useCQCode();
                builder.retryTimes(overflowConfig.getRetryTimes());
                builder.retryWaitMills(overflowConfig.getRetryWaitMills());
                builder.retryRestMills(overflowConfig.getRetryRestMills());
                builder.heartbeatCheckSeconds(overflowConfig.getHeartbeatCheckSeconds());
                if(!overflowConfig.isDropEventsBeforeConnected()) builder.keepEventsBeforeConnected();

                bot = builder.modifyBotConfiguration(configuration -> {
                    if (MiraiMC.getConfig().Bot_DisableBotLogs) {
                        configuration.noBotLog();
                    } else if (MiraiMC.getConfig().Bot_UseMinecraftLogger_BotLogs) {
                        configuration.setBotLoggerSupplier(bot1 -> LoggerAdapters.asMiraiLogger(plugin.getPluginLogger()));
                    }

                    if (MiraiMC.getConfig().Bot_DisableNetworkLogs) {
                        configuration.noNetworkLog();
                    } else if (MiraiMC.getConfig().Bot_UseMinecraftLogger_NetworkLogs) {
                        configuration.setNetworkLoggerSupplier(bot1 -> LoggerAdapters.asMiraiLogger(plugin.getPluginLogger()));
                    }
                }).overrideLogger(plugin.getPluginLogger()).connect();
            }
        });
    }

    public void disconnect(){
        if(bot != null){
            plugin.getPluginLogger().info("正在关闭 Overflow 机器人");
            bot.close();
            bot = null;
        }
    }
}
