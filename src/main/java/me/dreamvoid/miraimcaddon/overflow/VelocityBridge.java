package me.dreamvoid.miraimcaddon.overflow;

import me.dreamvoid.miraimc.api.MiraiMC;
import top.mrxiaom.overflow.BotBuilder;

public class VelocityBridge {
    private final VelocityPlugin plugin;
    private net.mamoe.mirai.Bot bot = null;

    public VelocityBridge(VelocityPlugin plugin) {
        this.plugin = plugin;
    }

    public void connect(){
        MiraiMC.getPlatform().runTaskAsync(() -> {
            switch (String.valueOf(plugin.getConfig().getOrDefault("type", "none"))){
                case "positive":{
                    bot = BotBuilder.positive(String.valueOf(plugin.getConfig().getOrDefault("host", "ws://127.0.0.1:5800")))
                            .modifyBotConfiguration(configuration -> {
                                if(MiraiMC.getConfig().Bot_DisableBotLogs){
                                    configuration.noBotLog();
                                } else if(MiraiMC.getConfig().Bot_UseMinecraftLogger_BotLogs){
                                    configuration.setBotLoggerSupplier(bot -> net.mamoe.mirai.utils.LoggerAdapters.asMiraiLogger(plugin.getLogger()));
                                }

                                if(MiraiMC.getConfig().Bot_DisableNetworkLogs){
                                    configuration.noNetworkLog();
                                } else if(MiraiMC.getConfig().Bot_UseMinecraftLogger_NetworkLogs){
                                    configuration.setNetworkLoggerSupplier(bot -> net.mamoe.mirai.utils.LoggerAdapters.asMiraiLogger(plugin.getLogger()));
                                }
                            })
                            .token(String.valueOf(plugin.getConfig().getOrDefault("token", "")))
                            .retryTimes(Integer.parseInt(String.valueOf(plugin.getConfig().getOrDefault("retry-time", 0))))
                            .overrideLogger(plugin.getLogger())
                            .connect();
                    break;
                }
                case "reversed":{
                    bot = BotBuilder.reversed(Integer.parseInt(String.valueOf(plugin.getConfig().getOrDefault("port", 5700))))
                            .modifyBotConfiguration(configuration -> {
                                if(MiraiMC.getConfig().Bot_DisableBotLogs){
                                    configuration.noBotLog();
                                } else if(MiraiMC.getConfig().Bot_UseMinecraftLogger_BotLogs){
                                    configuration.setBotLoggerSupplier(bot -> net.mamoe.mirai.utils.LoggerAdapters.asMiraiLogger(plugin.getLogger()));
                                }

                                if(MiraiMC.getConfig().Bot_DisableNetworkLogs){
                                    configuration.noNetworkLog();
                                } else if(MiraiMC.getConfig().Bot_UseMinecraftLogger_NetworkLogs){
                                    configuration.setNetworkLoggerSupplier(bot -> net.mamoe.mirai.utils.LoggerAdapters.asMiraiLogger(plugin.getLogger()));
                                }
                            })
                            .token(String.valueOf(plugin.getConfig().getOrDefault("token", "")))
                            .retryTimes(Integer.parseInt(String.valueOf(plugin.getConfig().getOrDefault("retry-time", 0))))
                            .overrideLogger(plugin.getLogger())
                            .connect();
                    break;
                }
                case "none":
                default:{
                    plugin.getLogger().error("Please check config file.");
                    break;
                }
            }
        });
    }

    public void shutdown(){
        if(bot != null){
            bot.close();
        }
    }
}
