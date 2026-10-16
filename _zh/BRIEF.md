# 汉化任务简报（每个切片都必须遵守）

## 背景
把 Android 网络工具 app「Net Swiss Knife」的英文资源翻译为**简体中文**。
输出会被合并成 `app/src/main/res/values-zh-rCN/strings.xml` 使用。

## 输入输出
- 输入：`C:/D/apk_hook/_zh/slice_NN.xml`（`values/strings.xml` 的切片，片段前后**没有** `<resources>` 标签）
- 输出：`C:/D/apk_hook/_zh/zh_NN.xml`（用 write 工具写入，UTF-8，无 BOM，同样**不要**加 `<resources>` 标签）

## 统一术语表（必须严格遵守）
Ping=Ping ｜ Traceroute=路由追踪 ｜ Port Scanner=端口扫描 ｜ Port=端口 ｜ LAN Scanner=局域网扫描
Subnet=子网 ｜ Subnet mask=子网掩码 ｜ CIDR=CIDR ｜ Gateway=网关 ｜ Hop=跳 ｜ Host=主机
DNS Lookup=DNS 查询 ｜ Record=记录 ｜ Resolver=解析服务器 ｜ Reverse DNS=反向 DNS
Wi-Fi Scanner=Wi-Fi 扫描 ｜ SSID=SSID ｜ BSSID=BSSID ｜ Channel=信道 ｜ Band=频段 ｜ Width=带宽
RSSI=RSSI ｜ Spectrum=频谱 ｜ Access Point=接入点 ｜ Mesh=Mesh 组网
TLS Inspector=TLS 证书检测 ｜ Certificate=证书 ｜ Chain=证书链 ｜ Chain trust=链信任状态 ｜ Cipher suite=密码套件
Topology Discovery=网络拓扑发现 ｜ Topology=拓扑 ｜ Node=节点 ｜ Neighbor=邻居 ｜ Link=链路
WHOIS=WHOIS 查询 ｜ Registrar=注册商 ｜ Registry=注册局 ｜ Nameserver=名称服务器 ｜ Abuse=滥用投诉
HTTP Probe=HTTP 探测 ｜ Header=响应头 ｜ Redirect=重定向 ｜ Security rating=安全评级
Wake-on-LAN=网络唤醒 ｜ Magic packet=魔术包 ｜ Broadcast=广播地址
mDNS Browser=mDNS 浏览 ｜ Service=服务
Speed Test=网速测试 ｜ Upload=上传 ｜ Download=下载 ｜ Throughput=吞吐量 ｜ Latency=时延
Settings=设置 ｜ Home=首页 ｜ More=更多 ｜ All Tools=全部工具 ｜ Utilities=实用工具
Diagnostics=诊断 ｜ Security=安全 ｜ Wi-Fi & LAN=Wi-Fi 与局域网 ｜ Advanced options=高级选项
Hostname=主机名 ｜ Address=地址 ｜ Timeout=超时 ｜ Retry=重试 ｜ Copy=复制 ｜ Share=分享 ｜ Export=导出
Packet loss=丢包率 ｜ Jitter=抖动 ｜ RTT=往返时延 ｜ Min/Avg/Max=最小/平均/最大 ｜ Payload=负载 ｜ TTL=TTL
Concurrency=并发数 ｜ Preset=预设 ｜ Banner grabbing=服务横幅抓取 ｜ Vendor=厂商 ｜ OUI=OUI
Onboarding=新手引导 ｜ Privacy=隐私 ｜ Permission=权限 ｜ Auto-refresh=自动刷新
Scan=扫描 ｜ Probe=探测 ｜ Start=开始 ｜ Stop=停止 ｜ Cancel=取消 ｜ Save=保存 ｜ Delete=删除 ｜ Clear=清空
Pin=固定 ｜ Unpin=取消固定 ｜ Expand=展开 ｜ Collapse=收起
Accuracy=精度 ｜ Progress=进度 ｜ Result=结果 ｜ Summary=摘要 ｜ Details=详情 ｜ Status=状态

## 硬性规则
1. 只翻译 `>...<` 之间的可见文本和 XML 注释。资源名 `name="..."` 绝对不能改动。
2. 占位符原样保留：`%1$s` `%2$s` `%3$d` `%1$d` `%1$,.0f` 等，数量/顺序/类型都不能变。
3. 转义必须保留：`\'`（单引号）保持写成 `\'`，`\"` 保持，`&amp;` 保持，`\n` 保持，反斜杠不能丢。
4. 结构完全一致：同样的空行数量、同样的 4 空格缩进、同样的顺序；一个 `<string>` / `<plurals>` / `<item>` 都不能少、不能多、不能改序。
5. `<plurals>` 的 `quantity="one"` / `quantity="other"` 属性保持原样，只翻译内容（中文两者可相同）。
6. 以下保持英文：Net Swiss Knife、NetSwiss、Google、Cloudflare、OpenDNS、Quad9、Android、Wi-Fi、DNS、TCP、UDP、ICMP、TLS、SSL、SNMP、MAC、IP、IPv4、IPv6、HTTP、HTTPS、URL、SSID、BSSID、mDNS、NetBIOS、NBSTAT、ARP、CSV、JSON、SHA-256、SAN、CN、ORG、RCODE、PTR、SRV、CAA、MX、TXT、NS、SOA、AAAA、OUI、WoL、WOL、BIOS、UEFI。
7. 使用简体中文与中文标点（，。：；、（）「」）。句末不用英文句号；冒号用全角；英文与中文之间不加空格更自然。
8. 语气：面向普通用户的产品界面文案，简洁自然，不要机翻腔。按钮用动词短语。
9. `help_*` 前缀的帮助文本要译成通顺的说明文字，不要逐字硬译。
10. 不允许残留未翻译的整句英文（专有名词除外）。
11. `content-desc` / `*_description` 这类无障碍描述要译得自然，例如 "Pin %1$s" → "固定 %1$s"。

## 工作流程
1. 用 read 完整读取切片文件（必要时分多次读到文件末尾，确认读全）。
2. 逐条翻译，用 write 写出 `zh_NN.xml`。
3. 自检脚本（必须通过）：
```bash
python C:/D/apk_hook/_zh/check.py NN
```
该脚本会检查：XML 可解析、资源名集合与顺序一致、占位符一致、无残留英文整句。
4. 不通过就修正后重跑校验，直到通过。
