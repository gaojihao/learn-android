## Learn Android






## 架构速览
- 分层结构：`UI(Fragment/Activity)` → `ViewModel` → `Domain(Models)` / `Engine` / `Repository` → `Data(Room/DataStore)` → `Service/Notification`。
- 数据流驱动：`StateFlow` 提供界面状态；`repeatOnLifecycle` 在 `STARTED` 期间安全收集，避免泄漏。
- 依赖注入：使用 `Hilt`，`AppModule` 提供数据库、仓库、设置存储与引擎的单例。
- 前台服务：`PomodoroService` 承载引擎并更新常驻通知（含 Android 13+ 通知权限判断与降级策略）。
- 持久化：`Room` 管理任务与会话；`DataStore` 存储设置（工作/短休/长休时长）。
- 领域模型：`Task`、`PomodoroSession`、`Settings` 与枚举 `Priority`、`SessionType`。

## 模块职责
- `ui/`：`MainActivity` 与各 `Fragment`（`TimerFragment`、`TaskListFragment`、`SettingsFragment`、`StatisticsFragment`）负责展示与交互。
- `ui/viewmodel/`：`TimerViewModel` 等暴露 `StateFlow` 状态与调用引擎/仓库的用例方法。
- `timer/`：`PomodoroEngine` 实现番茄钟核心逻辑（开始/暂停/继续/停止、倒计时、自动切换下一会话）；`PomodoroService` 作为前台服务订阅引擎并刷新通知。
- `data/local/`：`AppDatabase` 与 `TaskDao`、`SessionDao`（Room 表、查询与变更）。
- `data/repository/`：`TaskRepository`、`SessionRepository` 封装 DAO 并提供领域对象流与基本操作。
- `data/settings/`：`SettingsDataStore` 映射偏好到领域 `Settings` 并提供更新与一次性读取。
- `domain/model/`：领域数据类与枚举，供 UI/引擎/仓库共享。
- `util/notification/`：`NotificationHelper` 创建/更新/取消通知与频道。
- `di/`：`AppModule` 提供依赖绑定与单例。

## 关键交互流程
- 启动会话：`TimerFragment` 检查 Android 13+ 通知权限 → 无权限申请；通过或低版本直接启动 `PomodoroService` 前台服务 → 服务解析 `Action` 调用 `PomodoroEngine.start(type)`。
- 运行中：`PomodoroEngine` 通过 `remainingSeconds`/`isRunning`/`isPaused` 状态流驱动 UI；`PomodoroService` 订阅并更新通知。
- 结束与切换：当前会话完成时根据 `SessionType` 与已完成次数自动决定下一个会话（短休/长休/工作），并在需要时重置计数。

## 开发提示
- Room Schema 导出：构建时若出现 `Room cannot export the schema` 警告，可使用 `id "androidx.room"` 插件配置 `room.schemaLocation` 或在 `@Database(exportSchema = false)` 关闭导出（开发阶段可暂时关闭，发布前建议开启）。
- 注释风格：公共 API 使用 `KDoc` 描述行为与参数；复杂内部逻辑在关键方法上追加说明块，便于快速理解与维护。


## 参考链接
1. [Android官方文档](https://developer.android.google.cn/guide/index.html)
2. [凤邪摩羯](https://juejin.cn/user/3949101500094471)
3. [Jetpack Compose 使用入门](https://developer.android.google.cn/develop/ui/compose/documentation?hl=zh-cn)