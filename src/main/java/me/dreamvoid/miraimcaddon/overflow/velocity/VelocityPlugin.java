package me.dreamvoid.miraimcaddon.overflow.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.dreamvoid.miraimcaddon.overflow.OverflowLifeCycle;
import me.dreamvoid.miraimcaddon.overflow.Platform;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.Map;

@Plugin(
        id = "miraimcaddon-overflow",
        name = "MiraiMCAddon-Overflow",
        version = "PROJECT.VERSION",
        description = "Overflow support for MiraiMC",
        authors = {"DreamVoid"},
        dependencies = {@Dependency(id = "miraimc")}
)
public class VelocityPlugin implements Platform {
    private final ProxyServer proxy;
    private final Logger logger;
    private final Path dataDirectory;

    private final OverflowLifeCycle lifeCycle = new OverflowLifeCycle(this);
    private Map<String, Object> config;

    @Inject
    public VelocityPlugin(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxy = proxy;
        this.logger = logger;
        this.dataDirectory = dataDirectory;

        lifeCycle.loadOverflowLibrary();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("正在加载配置文件");
        lifeCycle.loadConfig();

        logger.info("正在注册命令");
        CommandManager manager = proxy.getCommandManager();
        CommandMeta overflow = manager.metaBuilder("overflow").build();
        manager.register(overflow, new SimpleCommand() {
            @Override
            public void execute(Invocation invocation) {
                String[] args = invocation.arguments();
                CommandSource sender = invocation.source();
                if(args.length > 0){
                    if(args[0].equalsIgnoreCase("reload")){
                        lifeCycle.loadConfig();
                        sender.sendMessage(Component.text("已重新加载Overflow配置。").color(NamedTextColor.GREEN));
                    } else if (args[0].equalsIgnoreCase("connect")){
                        sender.sendMessage(Component.text("尝试连接到Onebot，请查看控制台以了解更多信息。").color(NamedTextColor.GREEN));
                        lifeCycle.connect();
                    } else {
                        sender.sendMessage(Component.text("用法: /overflow <reload|connect>").color(NamedTextColor.RED));
                    }
                } else {
                    proxy.getPluginManager().getPlugin("miraimcaddon-overflow").ifPresent(plugin -> sender.sendMessage(Component.text("This server is running " + plugin.getDescription().getName() + " version " + plugin.getDescription().getVersion() + " by " + String.join(", ", plugin.getDescription().getAuthors()))));
                }
            }

            @Override
            public boolean hasPermission(Invocation invocation) {
                return invocation.source().hasPermission("miraimc.command.overflow");
            }
        });

        lifeCycle.connect();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event){
        lifeCycle.disconnect();
    }

    @Override
    public Logger getPluginLogger() {
        return logger;
    }

    @Override
    public Path getDataPath() {
        return dataDirectory;
    }
}
