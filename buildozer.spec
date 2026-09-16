[app]
title = HTTP Test App
package.name = httptestapp
package.domain = org.piyushmod
source.dir = .
source.include_exts = py,png,jpg,kv,atlas
version = 1.0
requirements = python3,kivy,requests
orientation = portrait
fullscreen = 0

[buildozer]
log_level = 2
warn_on_root = 1

[android]
android.api = 35
android.minapi = 23
android.archs = arm64-v8a
android.permissions = INTERNET
android.ndk = 27c
android.accept_sdk_license = True