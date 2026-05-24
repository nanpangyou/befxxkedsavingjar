# Codex Context

这个文件用于在设备切换、上下文丢失或重新打开 Codex 时，快速恢复项目背景。后续每次提交功能、修复或重要决策时，都应同步更新本文件。

## 当前目标

做一个 Android App：用户输入月薪或年薪后，App 根据工作日规则和上下班时间，折算出每秒收入，并在主屏实时显示“今天已经赚到”的金额。

当前项目已经进入 MVP 后的整理阶段：核心功能可用，正在做代码结构、UI、文档和测试体验优化。

最近状态：用户反馈最新真机 Run 看上去没有明显问题，已在 2026-05-24 生成最新 debug APK。

## 已做决定

- 技术栈使用 Kotlin + Jetpack Compose + Material 3。
- 本地设置使用 Android DataStore 保存。
- 开发阶段每次提交前至少运行单元测试：
  - `.\gradlew.bat testDebugUnitTest`
- 开发阶段不需要每次都打 debug APK。
- 只有在用户明确需要 APK、改动 Gradle/Manifest/资源配置、或准备真机完整测试时，才运行：
  - `.\gradlew.bat assembleDebug`
- 每次功能完成后使用规范提交信息并推送 GitHub。
- GitHub 远程仓库：
  - `git@github.com:nanpangyou/BeFxxkedSavingJar.git`
- 工作日规则默认使用“中国法定”。
- 中国法定工作日优先使用当前系统年份的数据。
- App 打开时先读取当前年份本地法定日历；命中则直接使用，不再联网同步。
- 只有本地没有当前年份法定日历时，才尝试联网查询并保存。
- 不允许把上一年的法定日历误用到下一年。
- 2026 年中国法定节假日/调休数据已内置，网络同步失败时仍可用。
- 2027 年及以后：
  - App 会先尝试联网同步对应年份数据。
  - 如果没有内置数据且联网失败，则回退到固定周一到周五。
- “每周固定”模式下，年工作日总数由所选星期自动计算，不再让用户手动输入。
- 每秒收入和今日已赚使用同一套小数位精度，低金额时也要能看到数字变化。

## 运行方式

推荐用 Android Studio 打开项目根目录：

```text
D:\Code\Money
```

日常开发测试：

1. Android Studio 选择真机或模拟器。
2. 点击 Run。
3. 在手机上测试主屏、设置页、工作日规则和持久化。

当前 JDK 路径：

```text
C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot
```

如果在 Codex/PowerShell 里运行 Gradle，需要先设置：

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

## 常用命令

运行单元测试：

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot'; $env:Path="$env:JAVA_HOME\bin;$env:Path"; .\gradlew.bat testDebugUnitTest
```

生成 debug APK：

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot'; $env:Path="$env:JAVA_HOME\bin;$env:Path"; .\gradlew.bat assembleDebug
```

查看 Git 状态：

```powershell
git status --short --branch
```

提交和推送示例：

```powershell
git add <files>
git commit -m "feat: describe change"
git push
```

## 不要改的地方

- 不要把 `local.properties` 提交到 Git。
- 不要提交 Android Studio 个人配置目录 `.idea/`。
- 不要在没有用户要求时生成 release 签名配置。
- 不要把真实密钥、token、签名密码写进仓库。
- 不要把联网 API 测试放进默认单元测试流程，避免测试不稳定。
- 不要把旧年份法定日历当成当前年份数据使用。
- 不要恢复“每天工作 8 小时”的手动输入；工作时长由上下班时间自动计算。

## 下一步

优先级建议：

1. 用户安装最新 debug APK 后继续收集真机反馈。
2. 后续考虑 App 名称、图标和更正式的视觉设计。
3. 如继续重构，可进一步拆分 `UserSettingsMapper.kt` 或补充更多边界测试。
4. 准备 release 签名和版本号规划。
