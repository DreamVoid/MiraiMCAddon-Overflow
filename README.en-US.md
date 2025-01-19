<div align="center">
    <h1> MiraiMCAddon-Overflow </h1>
</div>

[简体中文](README.md) | [English](README.en-US.md)

---

[Overflow](https://github.com/MrXiaoM/Overflow) is an alternative core for mirai. MiraiMCAddon-Overflow provides Overflow support for [MiraiMC](https://github.com/DreamVoid/MiraiMC).

## Introduction

MiraiMCAddon-Overflow is a Minecraft server implementation of Overflow that allows you to use Overflow to connect to a OneBot bot while using MiraiMC.

## Downloads
* Stable Versions
  * [Modrinth](https://modrinth.com/project/miraimcaddon-overflow/versions)
  * [GitHub Releases](https://github.com/DreamVoid/MiraiMCAddon-Overflow/releases)

## Getting Started (Server)

You need the MiraiMC plugin as a dependency. Please visit https://github.com/DreamVoid/MiraiMC for more information.

Download the appropriate MiraiMC plugin from the "Downloads" section and place the plugin file into the plugins/mods folder (depending on the server type). If the server is running, completely stop it. Then, start the server.

Navigate to the plugin's configuration folder (usually `plugins/MiraiMCAddon-Overflow`) to adjust the plugin configuration. Afterward, execute the `/overflow reload` and `/overflow connect` commands.

If everything works as expected, the plugin will automatically connect to your OneBot instance, and you can start enjoying elegant QQ bot services!

## Commands and Permissions
### Commands
| Command             | Description                  | Permission                |
|---------------------|------------------------------|---------------------------|
| /overflow           | Main command for MiraiMCAddon-Overflow | miraimc.command.overflow |
| /overflow connect   | Connect to the bot           | miraimc.command.overflow |
| /overflow reload    | Reload configuration files   | miraimc.command.overflow |

### Permissions
| Permission Node              | Description             | Default |
|------------------------------|-------------------------|---------|
| miraimc.command.overflow     | Allows use of /overflow | OP      |

## License

[GNU Affero General Public License v3.0](https://github.com/DreamVoid/MiraiMCAddon-Overflow/blob/main/LICENSE)

## Acknowledgements

Special thanks to the following individuals/teams/projects for their contributions to MiraiMCAddon-Overflow:

* [MrXiaoM/Overflow](https://github.com/MrXiaoM/Overflow): An alternative support library for mirai and the foundation of MiraiMCAddon-Overflow.
* You.

[DreamVoid](https://github.com/DreamVoid) and [MiraiMC](https://github.com/MiraiMC), made with ❤.
