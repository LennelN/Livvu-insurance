# Project status

_Last updated: November 2026_

## In this repository

| Component | Contents |
|---|---|
| Android app source | 280 Java classes covering all main user journeys |
| Android resources | About 3,000 layout and resource files, with layouts for small phones through to 10" tablets, in portrait and landscape |
| Mobile API (`server/api`) | 154 PHP endpoints returning JSON |
| Web portal (`server/web`) | 322 PHP pages |

## Still to be added

- **Android build configuration:** `AndroidManifest.xml`, Gradle build files and the Gradle wrapper, so the app can be opened and built in Android Studio
- **Shared server includes:** the `server/web/include/` folder (config, functions, header, footer and menu) used by the web portal
- **Database schema:** `database/schema.sql`, structure only
- **Missing endpoints:** the app calls 7 endpoints that are not yet in `server/api`:
  `get_currency_rates_report.php`, `get_currency_type_code.php`, `get_currency_types.php`, `get_currency_types_report.php`, `insert_currency_types.php`, `spinner_get_ancilliary_tariffs.php`, `update_currency_type.php`

## Planned technical improvements

- Move the remaining queries that insert values directly into SQL text over to prepared statements, which most of the code base already uses
- Upgrade password hashing from MD5 to `password_hash()` / bcrypt
- Move the app's server address into build configuration (separate debug and release URLs)
- Bring the shared endpoints in `server/api` and `server/web` together into one code base
