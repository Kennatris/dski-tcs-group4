# Sorting Comparer: Projektleitfaden

[Einstieg](../README.md) · [Agentenregeln](../AGENTS.md) · Inventarstand 22.10.2026.

## Zweck und vorhandener Stand

Java-Projekt zum Vergleich von Sortierverfahren mit Anwendungs- und Testquellen im Unterprojekt sorting-comparer.

Geprüfter Branch: `main`; Ausgangscommit `a8dbf3aba3ae9af467e3c7c4eaf7d68fd47cc802`. Die folgende Struktur stammt aus dem vorhandenen Git-Baum, nicht aus einer behaupteten erfolgreichen Installation.

## Aufbau und Einstieg

- [sorting-comparer/pom.xml](../sorting-comparer/pom.xml)

Weitere vorhandene Hauptbereiche: `sorting-comparer`.

## Einrichtung und Prüfungen

Zuerst die vorhandenen Projekt-/Paketdateien und Agentenregeln lesen. Verfügbare Toolchain und Versionen vor Installation feststellen. Nur vorhandene Manifeste/Lockfiles verwenden; keine Abhängigkeiten aus dem Projektnamen erfinden.

```bash
python3 .github/scripts/check-project-docs.py
cd sorting-comparer
./mvnw test
```

Das Dokumentationsgate arbeitet ohne Anwendungsausführung oder externe APIs. Weitere aufgeführte Befehle sind aus dem Projekt abgeleitete nächste Prüfungen; ihr erfolgreicher Lauf auf der Zielumgebung wird durch diesen Leitfaden nicht behauptet. Bei geräte-, GUI-, Broker- oder Datenbankabhängigen Prüfungen gelten die folgenden Grenzen.

## Daten, Betrieb und Sicherheit

Algorithmen anhand Korrektheit und vergleichbarer Eingaben prüfen; Laufzeitmessung, Warm-up und Zufallsseed festhalten. Extrem langsame Verfahren nicht unbegrenzt auf großen Eingaben starten. Generierte target-Dateien sind keine führenden Quellen.

Konfigurationen, Datenquellen, erzeugte Artefakte und ihre Aufbewahrung dokumentieren. Produktiv-/Nutzerdaten haben eine getrennte Sicherung und einen geprüften Rückweg. Keine Cloudübertragung, Kosten, Releases oder Deployments als Nebenwirkung eines Dokumentationstests.

## Änderungen und Abnahme

[Beitragsablauf](../CONTRIBUTING.md) beschreibt Branch, PR und Tests; [Sicherheitsmeldungen](../SECURITY.md) beschreibt den vertraulichen Meldeweg. Änderungen an Schnittstellen, Datenformaten oder Betrieb aktualisieren die Dokumentation im selben Change. Kleine nachvollziehbare Änderungen und ein datenkompatibler Rückweg gehören bei relevanten Eingriffen zur Abnahme.

## Lizenz und Herkunft

Im geprüften Baum wurde keine eigenständige Lizenzdatei nachgewiesen. Vor externer Wiederverwendung Rechte und Lizenz mit den Rechteinhabern klären; dieser Change wählt keine neue Lizenz.

## Offene Nachweise

- Branchschutz aktuell nicht als aktiv gemeldet; konkrete Regeln, Secret Scanning, Push Protection und Code Scanning wurden mit diesem Dokumentationschange nicht als vollständig eingerichtet nachgewiesen.
