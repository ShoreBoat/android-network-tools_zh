#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────────────────────────
# 构建脚本（二开分支）
#
#   ./build-zh.sh                # 打 debug APK
#   ./build-zh.sh release        # 打 release APK（未签名，若未配置签名参数）
#
# 依赖：JDK 21、Android SDK（platforms;android-37.0、build-tools;37.0.0）。
# SDK 路径写在 local.properties 的 sdk.dir。
# ─────────────────────────────────────────────────────────────────────────────
set -euo pipefail

VARIANT="${1:-debug}"
HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# JDK 21 —— 没有设置 JAVA_HOME 时尝试 scoop 安装的 Temurin 21
if [ -z "${JAVA_HOME:-}" ] && [ -d "/c/Users/Administrator/scoop/apps/temurin21-jdk/current" ]; then
  export JAVA_HOME="C:/Users/Administrator/scoop/apps/temurin21-jdk/current"
fi
[ -n "${JAVA_HOME:-}" ] && export PATH="$JAVA_HOME/bin:$PATH"
export ANDROID_HOME="${ANDROID_HOME:-C:/Android/Sdk}"

if [ ! -f "$HERE/local.properties" ]; then
  printf 'sdk.dir=%s\n' "$ANDROID_HOME" > "$HERE/local.properties"
  echo "已生成 local.properties: sdk.dir=$ANDROID_HOME"
fi

cd "$HERE"

case "$VARIANT" in
  debug)
    ./gradlew :app:assembleDebug --console=plain
    echo
    echo "APK: $HERE/app/build/outputs/apk/debug/app-debug.apk"
    ;;
  release)
    ./gradlew :app:assembleRelease --console=plain
    echo
    echo "APK: $HERE/app/build/outputs/apk/release/"
    ;;
  install)
    ./gradlew :app:installDebug --console=plain
    ;;
  test)
    ./gradlew :app:testDebugUnitTest :core-network:test :core-domain:test --console=plain
    ;;
  lint)
    ./gradlew :app:lintDebug --console=plain
    ;;
  *)
    echo "用法: $0 [debug|release|install|test|lint]" >&2
    exit 1
    ;;
esac
