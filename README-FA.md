# Jili V1 — Android

این پروژه برای نصب روی گوشی‌های Android از جمله Samsung Galaxy A14 آماده شده است.

## وضعیت
- رابط اصلی Jili
- ورودی تایپی
- ورودی صوتی فارسی با SpeechRecognizer
- خروجی صوتی فارسی با TextToSpeech
- درخواست Permissionها از داخل برنامه
- پایه Local/Offline
- جایگاه Sync و Settings
- آماده توسعه برای Agent، Cloud، Backup و سرویس‌های واقعی

## ساخت APK
این پروژه به Android SDK و Gradle نیاز دارد. در Android Studio پروژه را باز کنید و:
Build > Build APK(s)

APK معمولاً در:
app/build/outputs/apk/debug/app-debug.apk

قرار می‌گیرد.

## نکته
کلیدهای API و اطلاعات Cloud عمداً داخل پروژه قرار داده نشده‌اند و باید بعداً از طریق تنظیمات امن اضافه شوند.
