# Codex Context

这个文件用于在设备切换、上下文丢失或重新打开 Codex 时，快速恢复项目背景。后续每次提交功能、修复或重要决策时，都应同步更新本文件。

## 当前目标

做一个 Android App：用户输入月薪或年薪后，App 根据工作日规则和上下班时间，折算出每秒收入，并在主屏实时显示“今天已经赚到”的金额。

当前项目已经进入 MVP 后的整理阶段：核心功能可用，正在做代码结构、UI、文档和测试体验优化。

最近状态：用户反馈最新真机 Run 看上去没有明显问题，已在 2026-05-24 生成最新 debug APK。
产品名已确定为“窝囊费计算器”，并开始替换默认 App 名称和 launcher icon。
当前正在准备第一个版本 `v0.1.0`：已更新版本号，新增隐私说明，并配置 release signing 的本地读取方式。

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
- App 显示名称使用“窝囊费计算器”。
- 第一个正式测试版本使用 `versionName = "0.1.0"` 和 `versionCode = 1`。
- release signing 从本地 `keystore.properties` 读取，真实 keystore 和密码不提交到 Git。

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
- 不要提交 `keystore.properties`、`.jks` 或 `.keystore` 文件。
- 不要把联网 API 测试放进默认单元测试流程，避免测试不稳定。
- 不要把旧年份法定日历当成当前年份数据使用。
- 不要恢复“每天工作 8 小时”的手动输入；工作时长由上下班时间自动计算。

## 下一步

优先级建议：

1. 在本机创建 release keystore，并填写未提交的 `keystore.properties`。
2. 运行 `assembleRelease` 生成 signed release APK。
3. 安装 release APK 到真机验证。
4. 打 Git tag：`v0.1.0`。
5. 后续考虑更正式的视觉设计和 README 真机截图。

## 2026-05-24 发布记录

- 已在本机生成 release/wnf-release.jks 和 keystore.properties；二者为本地忽略文件，不提交到 Git。
- 已修复 release signing 的 storeFile 路径解析，签名文件路径以项目根目录为基准。
- 已生成 app/build/outputs/apk/release/app-release.apk，并通过 apksigner 验证 v2 签名有效。
- 下一步：提交并推送签名路径修复，然后创建并推送 Git tag v0.1.0。

## 2026-05-24 v0.2.0 开始

- v0.1.0 已完成手机测试、Git tag 和 GitHub Release。
- 下一轮优先做“关于页面 + 版本号展示 + 反馈入口”。
- 版本号从 BuildConfig.VERSION_NAME 读取，反馈入口指向 GitHub Issues。

## 2026-05-24 v0.2.0 发布准备

- v0.2.0 包含“关于页面 + 版本号展示 + 反馈入口”。
- 发布版本号更新为 versionName 0.2.0，versionCode 2。
- 发布前需要运行 testDebugUnitTest 和 assembleRelease，确认 signed release APK 可用后再提交、打 tag、发布 GitHub Release。

## 2026-05-25 自适应布局修复

- 用户反馈部分手机在设置薪资界面看不到底部“返回/保存”按钮。
- 原因是设置页内容没有 verticalScroll，底部按钮在小屏或字体放大时会被挤出屏幕。
- 修复方向：设置页主体可滚动，底部操作区放入 Scaffold bottomBar，并给薪资周期/工作日规则选项增加换行能力。
- 用户进一步要求不要把按钮固定在底部，改为相对单位自适应。
- 已调整为：按钮位于可滚动内容流中；设置页边距、纵向间距、操作区间距根据屏幕约束按比例计算，并保留最小/最大值。

## 2026-05-25 深色模式和全屏显示

- 用户反馈设置薪资页面滑动时顶部有空白条，希望全屏显示。
- 设置页改为 edge-to-edge 内容，不再叠加额外 statusBarsPadding。
- 新增主题模式设置：跟随系统、浅色、深色；默认跟随系统，保存到 DataStore。
- 用户进一步反馈顶部内容会和系统时间重叠；状态栏区域不应纳入软件内容全屏区域。
- 已恢复设置页内容的 status bar 安全区，并把系统状态栏背景设为当前主题 surface 色，同时按深浅色切换状态栏图标颜色。
- 用户反馈状态栏整条背景过长，并希望设置页改为立即生效。
- 已改为透明状态栏，内容继续避开系统时间区域；设置页取消“保存/返回”按钮，设置项变化后立即保存，系统返回手势/返回键负责离开设置页。

## 2026-05-25 UI 结构重构

- 用户希望继续保持“单文件单一功能”，并按界面/功能合理组织目录。
- 已计划将 `ui/main` 拆成 app 编排、home 首页、settings 设置页、settings/components、settings/sections、common、sync、about 等包。
- 重构目标是只移动职责和包结构，不改现有交互行为。

## 2026-05-25 v0.3.0 发布准备

- v0.3.0 汇总设置页自适应、即时保存、深色模式、状态栏修复和 UI 结构重构。
- 版本号更新为 versionName 0.3.0，versionCode 3。
- README 已重写，重点介绍功能、版本迭代、项目结构、隐私和 Release 下载。
- 发布前运行 testDebugUnitTest、assembleRelease 和 apksigner 验证，再创建 tag 和 GitHub Release。

## 2026-05-25 分支策略

- `dev` 作为日常开发分支，后续新功能、修复和重构默认在 `dev` 上进行。
- `main` 只用于正式版本发布；发布时从 `dev` 合并到 `main`，更新版本号，打 tag，并创建 GitHub Release。

## 2026-05-26 图标替换

- 当前开发分支为 `dev`。
- 用户提供新的储蓄罐小猪图标，要求先替换 App launcher icon。
- 图标资源使用位图前景 + 纯色 adaptive icon 背景，保留 Android adaptive icon 配置。
