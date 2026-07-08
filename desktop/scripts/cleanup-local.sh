#!/bin/bash
set -euo pipefail

APP_PATH="/Applications/CloudDM.app"
USER_DATA_DIR="$HOME/.cgdm-desktop"
ELECTRON_DEV_SUPPORT="$HOME/Library/Application Support/cgdm-desktop"
ELECTRON_PROD_SUPPORT="$HOME/Library/Application Support/CloudDM"
PREF_DESKTOP="$HOME/Library/Preferences/com.clougence.cgdm-desktop.plist"
PREF_LEGACY="$HOME/Library/Preferences/com.clougence.clouddm.plist"

PORTS=(18222 3307 18008)

echo "CloudDM Desktop 本地数据清理"
echo
echo "此脚本会删除本机 CloudDM Desktop 的应用与缓存，包括："
echo "  - ${APP_PATH}"
echo "  - ${USER_DATA_DIR}（内置 MySQL、配置、日志、应用数据）"
echo "  - ${ELECTRON_DEV_SUPPORT}（开发版 Electron 缓存）"
echo "  - ${ELECTRON_PROD_SUPPORT}（正式版 Electron 缓存）"
echo "  - ${PREF_DESKTOP}"
echo "  - ${PREF_LEGACY}"
echo
echo "删除后本地数据源、SQL 历史、管理员密码等数据将无法恢复。"
echo "重新安装并首次启动后，会按当前版本默认账号重新初始化。"
echo

read -r -p "确认继续清理？输入 yes 执行删除，其他任意键取消: " confirm
if [[ "$confirm" != "yes" ]]; then
  echo "已取消。"
  exit 0
fi

echo
echo "正在停止 CloudDM 相关进程..."
pkill -f "CloudDM.app" 2>/dev/null || true
pkill -f "cgdm-desktop" 2>/dev/null || true

for port in "${PORTS[@]}"; do
  if lsof -ti :"$port" >/dev/null 2>&1; then
    lsof -ti :"$port" | xargs kill -9 2>/dev/null || true
  fi
done

echo "正在删除本地数据..."
rm -rf "$APP_PATH"
rm -rf "$USER_DATA_DIR"
rm -rf "$ELECTRON_DEV_SUPPORT"
rm -rf "$ELECTRON_PROD_SUPPORT"
rm -f "$PREF_DESKTOP"
rm -f "$PREF_LEGACY"

echo
echo "清理完成。可以重新安装 CloudDM Desktop。"
