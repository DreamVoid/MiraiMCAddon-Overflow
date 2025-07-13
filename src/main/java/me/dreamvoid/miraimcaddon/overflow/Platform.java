package me.dreamvoid.miraimcaddon.overflow;

import org.slf4j.Logger;

import java.nio.file.Path;

public interface Platform {
    Logger getPluginLogger();

    Path getDataPath();
}
