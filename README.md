# MiraiMCAddon-Overflow

[Overflow](https://github.com/MrXiaoM/Overflow) 是 mirai 的替代核心，MiraiMCAddon-Overflow 为 [MiraiMC](https://github.com/DreamVoid/MiraiMC) 提供 Overflow 支持。

## 介绍

MiraiMCAddon-Overflow 是一个 Overflow 的 Minecraft 服务端实现，能够让你在使用 MiraiMC 的同时使用 Overflow 连接到 OneBot 机器人。

## 下载
* 稳定版本
  * [GitHub 发布页](https://github.com/DreamVoid/MiraiMCAddon-Overflow/releases)

## 开始使用（服务器）

你需要 MiraiMC 插件作为依赖，请前往 https://github.com/DreamVoid/MiraiMC 了解更多信息。

从“下载”部分下载适用的 MiraiMC 插件，并将插件文件放入插件/模组文件夹（取决于服务端类型）。如果服务端正在运行，请完全停止服务端。之后，启动服务端。

前往插件的配置文件夹（通常是 `plugins/MiraiMCAddon-Overflow`）调整插件的配置，之后，执行 `/overflow reload` 和 `/overflow connect` 命令。

如果不出意外，插件会自动连接到你的 OneBot 实例，开始享受优雅的 QQ 机器人服务！

## 指令和权限
### 指令
| 命令 | 描述 | 权限 |
| ---- | --- | ---- |
| /overflow  | MiraiMCAddon-Overflow 主命令 | miraimc.command.overflow |
| /overflow connect | 连接到机器人 | miraimc.command.overflow |
| /overflow reload | 重新加载配置文件 | miraimc.command.overflow |

### 权限
| 权限节点 | 描述 | 默认 |
| ------- | ---- | ---- |
| miraimc.command.overflow | 允许使用 /overflow | OP |

## 许可证

[GNU Affero General Public License v3.0](https://github.com/DreamVoid/MiraiMCAddon-Overflow/blob/main/LICENSE)

## 致谢

感谢以下人员/团队/项目为 MiraiMCAddon-Overflow 做出的贡献！

* [MrXiaoM/Overflow](https://github.com/MrXiaoM/Overflow)：mirai 的替代支持库，MiraiMCAddon-Overflow 的核心和基础。
* 你。

[DreamVoid](https://github.com/DreamVoid) 与 [MiraiMC](https://github.com/MiraiMC)，用 ❤ 制作。
