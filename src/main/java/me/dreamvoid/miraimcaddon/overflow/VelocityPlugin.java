package me.dreamvoid.miraimcaddon.overflow;

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
import me.dreamvoid.miraimc.api.MiraiMC;
import me.dreamvoid.miraimc.internal.loader.LibraryLoader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

@Plugin(
        id = "miraimcaddon-overflow",
        name = "MiraiMCAddon-Overflow",
        version = "1.0",
        authors = {"DreamVoid"},
        dependencies = {@Dependency(id = "miraimc")}
)
public class VelocityPlugin {
    private final ProxyServer proxy;
    private final Logger logger;
    private final Path dataDirectory;

    private HashMap<String, Object> config;
    private final VelocityBridge bridge = new VelocityBridge(this);

    @Inject
    public VelocityPlugin(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxy = proxy;
        this.logger = logger;
        this.dataDirectory = dataDirectory;

        logger.info("Calling MiraiMC to load Overflow core.");
        LibraryLoader loader = MiraiMC.getPlatform().getLibraryLoader();
        try {
            loader.loadLibraryMaven("top.mrxiaom.mirai", "overflow-core-all", System.getProperty("MiraiMC.overflow-version", "1.0.0.533-7d6c17e-SNAPSHOT"), "https://s01.oss.sonatype.org/content/repositories/snapshots", "-all.jar", dataDirectory);
            System.setProperty("MiraiMC.do-not-load-mirai-core", "Overflow");
        } catch (Exception e) {
            logger.error("加载 Overflow 核心时出现异常！", e);
        }
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Loading configuration.");
        reloadConfig();

        logger.info("Registering commands.");
        CommandManager manager = proxy.getCommandManager();
        CommandMeta overflow = manager.metaBuilder("overflow").build();
        manager.register(overflow, new SimpleCommand() {
            @Override
            public void execute(Invocation invocation) {
                String[] args = invocation.arguments();
                CommandSource sender = invocation.source();
                if(args.length > 0){
                    if(args[0].equalsIgnoreCase("reload")){
                        reloadConfig();
                        sender.sendMessage(Component.text("已重新加载Overflow配置。").color(NamedTextColor.GREEN));
                    } else if (args[0].equalsIgnoreCase("connect")){
                        sender.sendMessage(Component.text("尝试连接到Onebot，请查看控制台以了解更多信息。").color(NamedTextColor.GREEN));
                        connect();
                    } else {
                        sender.sendMessage(Component.text("Usage: /overflow <reload|connect>").color(NamedTextColor.RED));
                    }
                } else {
                    proxy.getPluginManager().getPlugin("miraimcaddon-overflow").ifPresent(plugin -> sender.sendMessage(Component.text("This server is running " + plugin.getDescription().getName() + " version " + plugin.getDescription().getVersion() + " by " + plugin.getDescription().getAuthors().toString().replace("[", "").replace("]", ""))));
                }
            }

            @Override
            public boolean hasPermission(Invocation invocation) {
                return invocation.source().hasPermission("miraimc.command.overflow");
            }
        });

        connect();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event){
        bridge.shutdown();
    }

    private void reloadConfig(){
        if(!dataDirectory.toFile().exists() && !dataDirectory.toFile().mkdirs()) {
            logger.warn("Failed to create plugin data directory!");
        }

        File file = dataDirectory.resolve("config.yml").toFile();
        if (!file.exists()) {
            try (InputStream is = this.getClass().getResourceAsStream("/config.yml")) {
                assert is != null;
                Files.copy(is, file.toPath());
            } catch (IOException e) {
                logger.error("Failed to save default config file!", e);
            }
        }

        Yaml yaml = new Yaml();
        try {
            config = yaml.loadAs(new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8), HashMap.class);
        } catch (IOException e) {
            logger.error("Failed to load config file!", e);
        }
    }

    private void connect(){
        bridge.connect();
    }

    public Logger getLogger() {
        return logger;
    }

    public HashMap<String, Object> getConfig() {
        return config;
    }
}
