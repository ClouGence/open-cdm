# 离线企业插件

CloudDM 的企业功能由独立闭源工程 `open-cdm-plugin` 提供，前端页面与公共 SDK 保持开源。当前交付普通混淆 JAR，不加密、不签名、不验签，也不需要部署密钥。

## 工程与加载方式

```text
open-cdm/
  backend/clouddm-platform/cgdm-plugin-sdk/      # 公开 DsPlugin、Binder 和业务 SPI
  backend/clouddm-platform/cgdm-plugin-loader/   # 原有插件加载与能力查询
  frontend/                                   # 开源页面及版本标识
open-cdm-plugin/
  plugin-api/                                 # 统一 EnterprisePlugin 接口
  plugins/plus-enterprise-edition/             # 企业版标识实现
  gradle/private-plugin.gradle                # 公共混淆、打包和检查任务
  scripts/local_plugins.py                    # 本地构建安装、移除
  build_plugins.sh
  remove_plugins.sh
```

Alone、Console、Sidecar 均使用原有 `PluginLoadHelper.loadPlugins(...)`。企业插件不声明进程角色，不单独扫描或激活：与其他插件一样读取普通 JAR，只将带 `@Plugin` 注解的类型作为候选，按原有 `order` 排序，通过 `DsPlugin.loadPlugin(binder)` 注册能力。未带注解的 `EnterprisePlugin` 公共接口不进入候选列表；实际加载时仍校验入口实现 `DsPlugin`。目录遍历、驱动加载、实例化和返回计数沿用原实现。

没有 `PrivatePluginLoader`、加密容器、内存解密 ClassLoader、专用依赖调度或暂存注册。宿主不根据插件元数据执行独立的版本、重复 ID、依赖或签名校验。安装时应使用匹配的 SDK 构建，替换前删除同 ID 旧包，避免重复注册。

## 接口与能力注册

闭源工程统一入口 `EnterprisePlugin extends DsPlugin`，由所有业务插件复用：

```java
public interface EnterprisePlugin extends DsPlugin {
    Set<String> supportedFeatures();
    void register(DsPluginBinder binder);

    @Override
    default void loadPlugin(DsPluginBinder binder) {
        Set<String> features = Set.copyOf(supportedFeatures());
        register(binder);
        binder.addGlobalFeature(features.toArray(String[]::new));
    }
}
```

`plus-enterprise-edition` 仅声明版本标识，不需要业务 SPI，完整入口如下：

```java
import java.util.Set;

import com.clougence.clouddm.privateplugins.api.EnterprisePlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;

@Plugin(name = "Enterprise edition")
public class EnterpriseEditionPlugin implements EnterprisePlugin {
    @Override
    public Set<String> supportedFeatures() {
        return Set.of("ENTERPRISE_EDITION");
    }

    @Override
    public void register(DsPluginBinder binder) {
    }
}
```

共享入口将声明的能力加入宿主全局注册表。Console 的 `/globalSettings` 直接读取 `PluginManager.getFeatures()`，前端通过此 REST 接口中的 `features.ENTERPRISE_EDITION` 判断版本。

`POST /globalSettings`（JSON 请求体 `{}`）成功响应包含：

| 字段 | 类型 | 语义 |
| --- | --- | --- |
| `data.features` | object | 已注册的全局能力 |
| `data.features.ENTERPRISE_EDITION` | boolean | 安装并完成能力注册后为 true；未安装时省略 key |

普通工作区和 SQL 工作台的左下角根据该能力展示企业版/社区版，紧邻原版本号。能力请求未完成时显示加载中；成功返回后按能力显示企业版/社区版，查询失败显示重试状态。该标识不授予用户权限，也不自动开启其他企业业务能力。

## 安装与移除

宿主扫描 `<app.home>/plugins/` 和 `<app.data>/plugins/`。本地脚本固定操作 `~/clouddm/plugins/`，仅接受可选模块名；不支持 `--app-home`、`--app-data` 或路径环境变量。

```bash
cd ../open-cdm-plugin
./build_plugins.sh
# 重启后端：能力出现，页面显示企业版。
./remove_plugins.sh
# 再次重启后端：能力消失，页面显示社区版。
```

脚本按普通 JAR 中的 `META-INF/clouddm-plugin.json` 的 `pluginId` 管理本工程安装包。该文件只供构建和安装脚本识别，宿主加载不依赖它。构建保留混淆和产物检查，成功后才安装；移除仅删除插件包，保留业务数据。历史加密包直接删除，不作为普通 JAR 继续安装。

新增插件时，在闭源工程注册模块，实现 `EnterprisePlugin`，复用公共构建脚本。仅声明能力的插件可将 `register()` 留空；需要扩展宿主业务时，在此方法中注册相应的公开业务 SPI。脚本可按 `requiredPlugins` 自动选择需一同构建安装的模块；这不是宿主运行时依赖校验。运行顺序继续使用原有 `@Plugin.order`。

混淆用于提高分析成本，不保证代码无法反编译或破解。当前实现不提供加密或完整性保护。
