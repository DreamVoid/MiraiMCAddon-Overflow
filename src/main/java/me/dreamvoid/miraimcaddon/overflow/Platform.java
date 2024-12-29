package me.dreamvoid.miraimcaddon.overflow;

import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.Map;

public interface Platform {
    Logger getPluginLogger();

    Path getDataPath();

    Map<String, Object> getConfigMap();

    void reloadConfig();
}
