# TerraMC · 让泰拉遍布我的世界

[![NeoForge](https://img.shields.io/badge/NeoForge-21.1.176-blue?style=flat-square)](https://neoforged.net/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-green?style=flat-square)](https://www.minecraft.net/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](LICENSE)

一个以 **汇流来世（Confluence / Afterlife）** 为前置，并联动各大 Minecraft 模组的 NeoForge 模组。

> 将泰拉瑞亚（Terraria）风格的饰品、装备等内容带入 Minecraft：既为汇流来世扩充更多饰品与装备，也为每一个联动的模组提供其专属的饰品与道具。

## 基本信息

| 项目 | 内容 |
| --- | --- |
| 中文名 | 让泰拉遍布我的世界 |
| 英文名 | TerraMC |
| 注册 ID（mod id） | `tm` |
| 基础包名 | `com.terramc` |
| 游戏版本 | Minecraft `1.21.1` |
| 模组加载器 | NeoForge `21.1.176` |
| 前置模组 | 汇流来世（Confluence，mod id 为 `confluence`） |

## 环境要求

- **JDK 21**（Minecraft 1.21.1 需要 Java 21 运行与编译）
- **Git**（用于版本管理与提交到 GitHub）
- 推荐 IDE：IntelliJ IDEA（自带 Gradle 支持）

> 本项目已配置 Gradle Wrapper，无需手动安装 Gradle。首次构建会自动下载 Gradle `8.14.2` 及所需依赖。

## 快速开始

### 1. 克隆仓库

```bash
git clone https://github.com/<你的用户名>/TerraMC.git
cd TerraMC
```

### 2. 配置 JDK 21

确保 `JAVA_HOME` 指向 JDK 21，或让 IDE 使用 JDK 21 作为 Gradle JVM：

```powershell
$env:JAVA_HOME = "D:\Java\java21"
```

> 项目已通过 `foojoy-resolver-convention` 插件配置了 Java 21 工具链，缺少时会自动下载。

### 3. 放置前置模组（汇流来世）

将汇流来世（Confluence）的 jar 放入 `libs/` 目录，并在 `build.gradle` 的 `dependencies` 中取消对应注释：

```gradle
implementation "blank:confluence:1.0.0"
```

### 4. 导入 IDE / 构建

IntelliJ IDEA 直接打开本目录即可。命令行常用操作：

```bash
# 首次同步依赖
./gradlew --refresh-dependencies

# 编译构建（输出 jar 到 build/libs/）
./gradlew build

# 启动客户端（开发调试）
./gradlew runClient

# 启动服务端
./gradlew runServer

# 运行数据生成
./gradlew runData

# 清理
./gradlew clean
```

> Windows 下请使用 `gradlew.bat` 代替 `./gradlew`。

## 目录结构

```
terramc
├── gradle/wrapper/                 # Gradle Wrapper（含 gradle-wrapper.jar）
├── src/main/
│   ├── java/com/terramc/tm/
│   │   ├── TerraMC.java            # 模组主类（@Mod("tm")）
│   │   ├── Config.java             # 通用配置
│   │   ├── init/                   # 注册中心
│   │   │   ├── ModItems.java       # 物品/饰品/装备注册
│   │   │   ├── ModBlocks.java      # 方块注册
│   │   │   └── ModCreativeTabs.java# 创造标签页注册
│   │   ├── api/                    # 对外 API（供联动/附属模组调用）
│   │   ├── common/                 # 基础内容（本模组自带的饰品、装备）
│   │   │   ├── item/               # 物品类
│   │   │   └── block/              # 方块类
│   │   ├── compat/                 # 各联动模组的专属内容
│   │   │   └── confluence/         # 汇流来世联动
│   │   ├── client/                 # 客户端相关（渲染、GUI）
│   │   ├── data/                   # 数据生成（datagen）
│   │   └── util/                   # 工具类
│   ├── templates/META-INF/
│   │   └── neoforge.mods.toml      # 模组元数据（含依赖声明）
│   └── resources/
│       └── assets/tm/
│           └── lang/               # 语言文件（en_us / zh_cn）
├── libs/                           # 本地第三方模组 jar（不提交到仓库）
├── build.gradle                    # 构建脚本
├── settings.gradle
├── gradle.properties               # 模组属性与版本配置
├── .gitignore
├── .gitattributes
├── LICENSE                         # MIT 开源协议
└── README.md
```

### 联动开发约定

为每个联动模组新增专属内容时，在 `com.terramc.tm.compat` 下创建以该模组 mod id 命名的子包（例如 `compat/confluence/`），并在其中存放该模组专属的饰品、装备与注册逻辑。

## 开源协议

本项目使用 [MIT License](LICENSE) 开源。
