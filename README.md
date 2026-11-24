# aura

چت‌بات LLM کاملاً آفلاین برای اندروید — llama.cpp + Jetpack Compose.
**صفر اینترنت، در سطح باینری و اثبات‌پذیر.**

- پکیج: `com.aura.neon` · minSdk 29 · targetSdk 34 (نهایی) · compileSdk 36
- خروجی: APK امضاشده‌ی arm64-v8a، صفحه‌بندی ۱۶KB — **فقط GitHub Releases، هرگز Google Play**
- فونت: Vazirmatn (تک‌فونت، fa+en، OFL) · مجوز کد: Apache-2.0

> قرارداد کامل پروژه: [docs/SPEC.md](docs/SPEC.md) — ساختار قفل شده، فازهای ۱ تا ۵، قواعد R1–R17.

## چرا این پروژه متفاوت است

| اصل | اجرا |
| --- | --- |
| آفلاین واقعی | بدون مجوز INTERNET؛ CI پس از هر بیلد مانیفست merge شده، classpath و همه‌ی `.so` ها را گارد می‌کند |
| بدون تله‌متری | هیچ کتابخانه‌ی آنالیتیکس/کرش‌آنلاین؛ لاگ‌ها فقط محلی |
| بدون کپی فایل | مدل‌ها با SAF در همان پوشه ثبت می‌شوند (URI + size + lastModified) |
| بدون Play | توزیع GitHub Releases؛ به‌روزرسانی داخل اپ ممنوع (نیاز به اینترنت دارد) |

## وضعیت: فاز ۱ (زیرساخت + مدیریت مدل)

- [x] ساختار ۱۲ ماژولی + build-logic (convention plugins) — گراف بدون چرخه، engine بدون دسترسی به DB
- [x] Version Catalog با pinهای **web-verified** (۱۴ نوامبر ۲۰۲۶؛ بازررسی خودکار در هر CI)
- [x] CI: preflight اجباری (resolve همه‌ی pinها قبل از بیلد) + ۳ گارد post-build
- [x] Renovate (فقط stable؛ toolchain همیشه دستی؛ llama.cpp مستثناست)
- [x] workflowهای submodule-bump و keystore-bootstrap (سازگار با مدیریت فقط-با-گوشی)
- [x] انتخاب پوشه با SAF (persistable، فقط محلی، provider ابری → خطای شفاف)
- [x] اسکن `.gguf` و ثبت در Room بدون کپی + هرس خودکار موارد ناپدیدشده
- [x] فهرست مدل‌ها: فعال/غیرفعال، سنجاق، یادداشت، حذف از فهرست + تخمین RAM (R15)

فازهای بعدی: ۲) موتور llama.cpp + چت استریم · ۳) تنظیمات کامل مدل · ۴) امنیت و پایداری تکمیلی · ۵) UI نهایی و لاگ‌های محلی.

## پشته (پین‌های تاییدشده ۲۰۲۶-۱۱-۲۴)

| ابزار | نسخه |
| --- | --- |
| Gradle | 9.9.2 |
| AGP | 9.5.2 |
| Kotlin | 2.3.21 *(2.3.14 موجود نیست — توضیح در SPEC)* |
| KSP | 2.3.14 |
| Compose BOM | 2026.11.01 |
| core-ktx / activity-compose / lifecycle / navigation | 1.19.1 / 1.13.0 / 2.11.1 / 2.10.2 |
| Room / DataStore | 2.8.5 / 1.3.0 |
| Hilt / hilt-navigation-compose | 2.60.1 / 1.4.0 |
| coroutines | 1.11.0 |
| JUnit / Turbine / Robolectric | 4.13.2 / 1.2.1 / 4.18.1 |

هر CI قبل از بیلد همه را resolve می‌کند (`scripts/preflight_pins.sh`)؛ اگر چیز شکست بخورد با پیام دقیق fail-fast می‌شود. استک جایگزین verified: پیوست A در SPEC.

## ساختار

```
aura-neon/
├── build-logic/            # convention plugins: aura.android.*, aura.jvm, aura.hilt
├── gradle/libs.versions.toml
├── app/                    # Application + Compose + Hilt + nav
├── core/{common,domain,data,ui}/
├── engine/{api,llama}/     # llama.cpp از فاز ۲ (submodule در third_party)
├── runtime/sandbox/        # AIDL + کلاینت :engine (فاز ۲)
└── feature/{chat,models,settings,onboarding}/
```

قواعد کلیدی: R2 (تنها یک مکانیزم Kotlin در AGP 9 — تعریف تک‌جای build-logic) · R3 (compose plugin + BOM، بدون composeOptions) · R6 (namespace یکتا از مسیر ماژول) · R16 (استریم توکن batch شده روی Binder).

## گردش‌کار فقط-با-گوشی

1. **بیلد**: فقط GitHub Actions (تب Actions ← هر push به main یک run کامل preflight+build+guards می‌سازد).
2. **نصب**: از صفحه‌ی run آخر، artifact `aura-debug-apk` را دانلود و نصب کن (باید «نصب از منابع ناشناس» را برای مرورگر فعال کنی).
3. **مدل‌ها**: یک فایل `‎.gguf` (مثلاً Q4_K_M) را در حافظه بگذار، در تب «مدل‌ها» پوشه‌اش را انتخاب و اسکن کن.
4. **Release**: برای انتشار، فایلهای `.github/workflows/*` را از طریق web UI تغییر بده — CI هرگز workflowها را push نمی‌کند.
5. **امضای release**: یک‌بار workflow `keystore-bootstrap` را دستی اجرا کن، artifact یک‌روزه را بردار و مقدارها را در Secrets بگذار؛ بعدش run و artifact را پاک کن.

## CI چه چیزی را تضمین می‌کند؟

- **Guard 1**: هیچ `android.permission.INTERNET` در مانیفست merge شده نباشد.
- **Guard 2**: هیچ okhttp/retrofit/ktor/firebase/play-services/crashlytics در `releaseRuntimeClasspath` نباشد.
- **Guard 3**: هیچ `libcurl` (NEED) یا سمبل `curl_*` در هیچ `.so` داخل APK نباشد.

## ریسک‌های شناخته‌شده (صادقانه)

- مکانیزم Kotlin در AGP 9 (R2) تا اولین بیلد سبز قطعی نمی‌شود؛ فقط در یک جای build-logic تعریف شده تا در صورت خطا با یک تغییر فلیپ شود.
- جفت KSP↔Kotlin و سازگاری metadata AGP با Kotlin توکار Gradle — باز هم اولین run CI قاضی است.
- فونت متغیر Vazirmatn با `FontVariation` — اگر کامپایلر اختلاف API داشته باشد، اصلاح جزئی است.
- اندازه‌گیری «صفر کرش با مدل سنگین» مستلزم چک‌لیست QA دستی پیش از هر تگ است (در SPEC).

## مجوزها

- کد: Apache-2.0 ([LICENSE](LICENSE))
- فونت Vazirmatn: SIL OFL 1.1 ([docs/licenses/OFL-Vazirmatn.txt](docs/licenses/OFL-Vazirmatn.txt))
