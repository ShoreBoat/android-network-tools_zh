# Net Swiss Knife · 中文二开版

Android 网络诊断工具集合的**简体中文二开分支**，基于上游
[`AieatAssam/android-network-tools`](https://github.com/AieatAssam/android-network-tools) 开发。

> 本分支 = 上游代码 + 完整简体中文本地化 + 二开增强。界面语言随系统自动切换，
> 也可在「设置 → 应用语言」里单独指定。

---

## 一、工具清单

| 分类 | 工具 | 说明 |
|------|------|------|
| 诊断 | **Ping** | ICMP 连通性与往返时延，支持连续模式、实时统计、CSV 导出 |
| 诊断 | **路由追踪** | ICMP/UDP 双协议逐跳分析，带归属地信息与 MTU 探测 |
| 诊断 | **端口扫描** | 7 组预设端口范围 + 自定义，服务识别与横幅抓取 |
| 诊断 | **DNS 查询** | A/AAAA/MX/TXT/CNAME/NS/SOA/PTR/SRV/CAA，多解析器可选 |
| Wi-Fi 与局域网 | **Wi-Fi 扫描** | 按 SSID 聚合、频谱分析、最佳信道建议、BSSID 详情 |
| Wi-Fi 与局域网 | **局域网扫描** | CIDR 网段扫描，ICMP/TCP/NetBIOS/mDNS 多重探测，厂商识别 |
| Wi-Fi 与局域网 | **网络拓扑** | 基于 SNMP 的 BFS 拓扑发现 |
| Wi-Fi 与局域网 | **mDNS 浏览** | 组播 DNS 服务发现 |
| 安全 | **TLS 证书检测** | 完整证书链、有效期、SAN、指纹、信任状态 |
| 安全 | **WHOIS 查询** | 域名/IP 注册信息、注册商、滥用投诉联系人 |
| 安全 | **HTTP 探测** | 请求测试、响应头分析、安全响应头评级 |
| 实用工具 | **子网计算器** | IPv4 子网划分、二进制拆分、表示法互转 |
| 实用工具 | **网速测试** | 下载/上传吞吐量与时延（Cloudflare 测速节点） |
| 实用工具 | **网络唤醒** | WoL 魔术包，支持广播地址与端口配置 |

---

## 二、二开改动一览

### 1. 简体中文本地化（完整）

- **`app/src/main/res/values-zh-rCN/strings.xml`**：上游 `values/strings.xml` 的**逐条**翻译，
  872 条资源与英文版**资源名、顺序、占位符（`%1$s` / `%2$d` 等）完全一致**，
  由脚本 `_zh/merge.py` 自动校验，缺一条或占位符写错都会直接报错。
- **硬编码文案资源化**：上游把一部分界面文字写死在 Kotlin 里，本分支全部改为资源引用，
  否则中文界面会中英混杂。涉及：
  - 工具名称 / 底部导航短标签 / 一句话描述（`NavRoutes.kt` → `tool_*_label` / `tool_*_short` / `tool_*_desc`）
  - 帮助面板标题（`ToolHelpSheet` → `help_sheet_how_to_use`）
  - 崩溃页全部文案（`CrashActivity` / `CrashHandler` → `crash_*`）
  - 端口扫描预设名（`PortScanPreset`）、DNS 服务器名与说明（`DnsServer`）、
    DNS 记录类型说明（`DnsRecordType`）、Wi-Fi 排序选项（`ApSortOrder`）、
    SOA 字段标签、Ping 引擎错误文本 —— 因纯 Kotlin 模块不能依赖 Android 资源，
    统一在 `:app` 层通过 `LocalizedLabels.kt` 做「模型 → 资源 ID」映射。
- **语言配置**：`res/xml/locales_config.xml` + `AndroidManifest` 的
  `android:localeConfig`，Android 13+ 系统设置里会出现「应用语言」入口。
- **应用内语言切换**：设置页顶部「应用语言」分栏（跟随系统 / 中文 / English）。
  - Android 13+ 走系统 `LocaleManager`（设置持久化由系统托管）
  - Android 12 及以下用本地存储 + `attachBaseContext` 包装，全版本可用
- **中文应用名**：中文环境下启动器显示「网络工具箱」，英文环境保持 NetSwiss。

### 2. 国内网络环境适配

- **新增国内公共 DNS 解析器预设**：阿里 DNS（223.5.5.5）、DNSPod/腾讯
  （119.29.29.29）、114DNS（114.114.114.114），与原有的系统 DNS / Google /
  Cloudflare / OpenDNS / Quad9 并列在下拉框里。

### 3. 工程化

- `build-zh.sh`：一键构建/安装/测试/lint，自动探测 JDK 21 与 Android SDK。
- `local.properties` 不入库（沿用上游 `.gitignore`）。

---

## 三、构建

### 环境要求

| 依赖 | 版本 |
|------|------|
| JDK | 21（`compileOptions` 与 `jvmToolchain` 均为 21） |
| Android SDK Platform | `platforms;android-37.0` |
| Android Build-Tools | `37.0.0` |
| Gradle | 9.7.1（wrapper 自动下载） |
| AGP / Kotlin | 9.4.1 / 2.4.20 |

### 命令

```bash
# 1) 指定 SDK 路径（首次）
echo 'sdk.dir=C:/Android/Sdk' > local.properties

# 2) 构建
./build-zh.sh              # debug APK  → app/build/outputs/apk/debug/app-debug.apk
./build-zh.sh release      # release（未配签名时输出未签名包）
./build-zh.sh install      # 直接装到已连接设备
./build-zh.sh test         # 单元测试
./build-zh.sh lint         # Android Lint
```

Windows 下也可以用原始 wrapper：

```powershell
$env:JAVA_HOME = "C:\path\to\jdk-21"
.\gradlew.bat :app:assembleDebug
```

### 安装 SDK（命令行方式）

```bash
sdkmanager --sdk_root=$ANDROID_HOME "platform-tools" "platforms;android-37.0" "build-tools;37.0.0"
```

---

## 四、汉化维护

翻译不是一个一次性动作，上游更新后需要重新对齐。流程：

```bash
# 1) 拉取上游
git remote add upstream https://github.com/AieatAssam/android-network-tools.git
git fetch upstream && git merge upstream/main

# 2) 校验中英资源是否仍然一一对应（少一条 / 占位符不一致会报错）
cd _zh && python merge.py     # 会重新生成 values-zh-rCN/strings.xml 并校验

# 3) 手工补齐新出现的条目（merge.py 会打印 MISSING 列表）
```

`_zh/` 目录里保留了切片工具链（`split.py` / `check.py` / `merge.py`）与
翻译简报（`BRIEF.md`，含术语表），便于后续增量翻译时保持用词一致。

**术语表约定**（节选）：Traceroute=路由追踪、Port Scanner=端口扫描、
LAN Scanner=局域网扫描、Subnet=子网、Gateway=网关、Hop=跳、
TLS Inspector=TLS 证书检测、WHOIS=WHOIS 查询、Wake-on-LAN=网络唤醒、
Packet loss=丢包率、Jitter=抖动、RTT=往返时延。
完整表见 `_zh/BRIEF.md`。

---

## 五、许可证

沿用上游 LICENSE。上游为原作者的版权作品，本分支仅做本地化与增强，
分发时请遵守上游许可条款并保留原始版权声明。
