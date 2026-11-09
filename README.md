# Livvu Insurance

Livvu is a health insurance and medical billing platform. Members (individuals, societies and companies) buy and transfer points that pay for medical care. Merchants such as hospitals and doctors bill against those points. Administrators manage tariffs, currency rates and contributing rates, and see reports across countries and continents.

> **Status: in active development.** This repository is a working snapshot shared for review. It is not yet a complete, buildable release. See [docs/STATUS.md](docs/STATUS.md) for what is in place and what is still to come.

## What the platform does

- **Member accounts:** individual, society and company registration, OTP verification, profile activation and deactivation, password reset
- **Points:** buy points (including by mobile network and card), transfer points, cash out, check balances, view statements
- **Merchant billing:** hospitals and doctors create bills from tariffs, capture patient medical details, and receive payments
- **Medical records:** patient medical history, statements and reports
- **Administration:** ancillary and admission tariffs, currency rates, country contributing rates, health institution types, system users
- **Reporting:** admissions, patients attended, cost of medicines, payments, and demographic reports by country, region and continent

## Repository layout

```
android/                      Android mobile app (Java)
  app/src/main/java/com/moyet/livvuinsurance/   app source (activities, fragments, adapters, models)
  app/src/main/res/                             layouts (phone and tablet sizes), drawables, icons, strings
server/
  api/                        PHP JSON API used by the mobile app (deployed at /phila/)
  web/                        PHP web portal (admin and member web pages)
database/                     database schema (to be added)
docs/                         project status and notes
```

## Technology

| Part | Stack |
|---|---|
| Mobile app | Android, Java, AndroidX (AppCompat, Fragments), Volley for HTTP, Gson |
| Server | PHP with PDO (MySQL) |
| Payments | PayLane card payments, mobile network purchases |
| Messaging | Email via PHPMailer (SMTP), SMS notifications |

## Configuration

Credentials are kept out of this repository.

- **Database:** in `server/api/` and `server/web/`, copy `db_config.example.php` to `db_config.php` and fill in the values. `db_config.php` is git-ignored.
- **Email (SMTP):** set the `SMTP_USER` and `SMTP_PASSWORD` environment variables on the server.
- **PayLane:** replace `YOUR_PAYLANE_PUBLIC_KEY` in `server/web/paylane_credit_card.php` and `server/web/credit_card_payment.php`.
- **Server address used by the app:** set in `android/app/src/main/java/com/moyet/livvuinsurance/FragmentActionListener.java`.

## Confidentiality

This is proprietary source code. All rights reserved. Shared for evaluation purposes only.
