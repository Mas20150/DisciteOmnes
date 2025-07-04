# Discite Omnes – Android Lernplattform

**Version:** 1.0
**Plattform:** Android (Java, Retrofit, Supabase)
**Status:** ✅ Vollständig umgesetzt & funktional getestet
**Deployment-Link:** [Supabase Backend](https://app.supabase.com/project/pruclodpzbyfoaeuefvf)

---

## 🌟 Projektübersicht

**Discite Omnes** ist eine mobile App für Studierende, mit der sie:

* Gruppen bilden oder ihnen beitreten,
* gemeinsame Studienpläne erstellen,
* Aufgaben verwalten,
* ihren Fortschritt über Study-Steps dokumentieren können.

Die App wurde **zu 100% manuell gecodet**, inkl.

* aller Java-Klassen (Activities, DTOs, Models, Netzwerk)
* aller XML-Layouts im modernen, einheitlichen Design
* aller Retrofit-APIs & Supabase-Abfragen

>🧠 Wichtige Info zur Entstehung:
>- 100 % mit KI erstellt (ChatGPT)
>- Ich habe kein eigenes Coding gemacht
>- Verwendet wurden ausschließlich Open-Source-Komponenten 

---

## 🚀 Funktionen im Überblick

| Feature                        | Beschreibung |
|-------------------------------|--------------|
| 🔐 Registrierung & Login      | über Supabase Auth-API |
| 👥 Gruppen erstellen/beitreten | inkl. UUID-Anzeige & RecyclerView |
| ✅ Aufgabenverwaltung         | Aufgaben pro Benutzer erstellen, abhaken, löschen |
| 🗓️ Studienpläne & Schritte    | Pläne & Teilaufgaben je Gruppe |
| 🔄 Daten live per Retrofit     | Supabase REST-API mit Interceptors & Headern |

---

## 📚 Projektstruktur

```
com.example.disciteomnes
└️ data
   ├️ dto              → Request- & Update-Objekte (z.B. TaskRequest, LoginRequest)
   └️ models           → Entitäten-Modelle (Task, Group, StudyPlan, StudyStep)

├️ network
   ├️ DisciteOmnesApi   → Retrofit Interface (alle Endpoints)
   └️ DatabaseClient     → Retrofit Setup inkl. Supabase Header

├️ ui
   ├️ LoginActivity, RegisterActivity, DashboardActivity
   ├️ TasksActivity, GroupsActivity, GroupCreateActivity
   ├️ PlannerActivity, StudyPlansActivity, StudyStepsActivity
   └️ Adapters (z.B. StudyPlanAdapter, GroupAdapter)

├️ res
   ├️ layout/*.xml       → UI Layout-Dateien für jede Activity + Items
   └️ font/*.ttf         → benutzerdefinierte Schrift für Branding
```

---

## ✉ Supabase Setup (benutzt in diesem Projekt)

**Projekt-Link:** https://app.supabase.com/project/pruclodpzbyfoaeuefvf

### Tabellen

* `users`          → Authentifizierte Nutzer
* `groups`         → Studiengruppen
* `group_members`  → Verknüpfung User <-> Group
* `tasks`          → Aufgaben (pro Benutzer)
* `study_plans`    → Lernpläne (pro Gruppe)
* `study_steps`    → Einzelschritte im Plan

### API-Verbindungen

* Auth: `/auth/v1/signup`, `/token`, `/user`
* DB: alle über `/rest/v1/<table>` via Retrofit (GET, POST, PATCH, DELETE)

---

## 🏑 Deploy- & Nachweislink

### 🔐 Bestätigung des Benutzerkontos:
Für die endgültige Registrierung ist eine E-Mail-Bestätigung erforderlich.
Diese Bestätigungsseite wurde programmiert und bereitgestellt.
Den vollständigen Quellcode inklusive der Bestätigungs-URL finden Sie im Repository

**Projekt:** https://github.com/Mas20150/DisciteOmnes.gi

---

## 🔧 Verwendete Technologien
-  Android SDK (Java, API 29+)
- Supabase (Auth, REST, PostgreSQL)
- Retrofit + OkHtt
- ConstraintLayout, ListView, RecyclerView
- SharedPreferences
 - AlertDialog, EditText, Spinner, CardView

---
