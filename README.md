# ITP-Fachbegriffe-Trainer

Ein Desktop-Tool zum Lernen, Wiederholen und Prüfen von ITP-Fachbegriffen.  
Entwickelt als Java-Swing-Anwendung (MVC) im Rahmen der Projektkooperation  
**Informationstechnische Projekte (ITP)** und **Softwareentwicklung (SEW)**.

## 📌 Überblick

Der ITP-Fachbegriffe-Trainer unterstützt Lernende beim nachhaltigen Erarbeiten  
von Fachbegriffen aus dem ITP-Bereich. Die Anwendung funktioniert vollständig  
offline und verwendet CSV/UTF-8-Dateien für alle Fragenpools und Statistiken.

**Drei Hauptmodule stehen zur Verfügung:**

- **Verwaltung** – Erstellen, Importieren, Bearbeiten und Exportieren von Fragenpools  
- **Quizmodus** – Punkte, Statistiken, Sofortbewertungen, Sitzungsfortschritt  
- **Hangman-Spielmodus** – Spielerischer Zugang zum Lernen von Fachbegriffen

Basierend auf den Vorgaben aus dem offiziellen Lastenheft und der Machbarkeitsstudie  
(Versionen 1.1 & 1.4) :contentReference[oaicite:2]{index=2} :contentReference[oaicite:3]{index=3}.

---

## 🚀 Funktionen

### Verwaltung
- Fragenpools erstellen, umbenennen und löschen  
- CSV/UTF-8 Import & Export  
- Validierung fehlerhafter Zeilen, Duplikaterkennung  
- Text- & Bildfragen (Bild via URL)  
- Fragevorschau inkl. Bildanzeige  
- Massenaktionen (mehrere Fragen gleichzeitig bearbeiten/exportieren)

### Quizmodus
- Zufällige Fragen
- Antwortprüfung & Punktevergabe
- Auswertung: Richtig/Falsch, Genauigkeit, Trefferquote
- Zeitlimit, Fragenanzahl, Poolauswahl konfigurierbar
- Quiz pausieren, fortsetzen oder abbrechen
- Session-Speicherung

### Hangman-Spielmodus
- Ratebegriffe aus den Fragenpools
- Einstellbare Fehlversuche
- Punkte- und Fortschrittsspeicherung
- Möglichkeit zu Neustart & Aufgabe

### Statistiken
- Darstellung von Lernfortschritt & Trefferquote
- Export als CSV
- Filter nach Zeitraum, Fragetyp und Pool

---

## 🧱 Technische Details

- **Sprache:** Java 24  
- **Framework:** Swing (MVC-Architektur)  
- **Build-System:** Maven  
- **Datenhaltung:** CSV/UTF-8 Dateien  
- **Ladezeit:** max. 2 Sekunden pro Fragenpool  
- **Cross-Platform:** Windows, Linux, macOS  
- **Open Source & erweiterbar**

# ITP Terminology Trainer

A desktop tool for learning, reviewing, and testing terminology from the subject *Information Technology Projects (ITP)*.  
Developed as a Java Swing application (MVC) as part of the ITP/SEW project cooperation.

## 📌 Overview

The ITP Terminology Trainer helps students learn and reinforce technical terms efficiently.  
The application works fully offline and uses CSV/UTF-8 files for all question pools and statistics.

**Three main modules are included:**

- **Management** – Create, import, edit, and export question pools  
- **Quiz Mode** – Points, instant evaluation, statistics, session progress  
- **Hangman Mode** – A playful way of learning terminology  

Based on the official requirements and feasibility documentation.

---

## 🚀 Features

### Management
- Create, rename, duplicate, delete question pools  
- CSV/UTF-8 import & export with validation  
- Duplicate handling and error reporting  
- Text & image questions (image via URL)  
- Question preview with image  
- Bulk actions (delete/export multiple questions)

### Quiz Mode
- Random question selection  
- Immediate answer checking & scoring  
- Evaluation: correct/wrong, accuracy, hit rate  
- Configurable time limit, question count, pool selection  
- Pause, resume, or cancel quizzes  
- Session saving and continuation  

### Hangman Mode
- Guess terms from question pools  
- Adjustable max mistakes  
- Score and progress saving  
- Restart or give up options  

### Statistics
- Learning progress, accuracy, hit rate  
- CSV export  
- Filters by time range, pool, and question type  

---

## 🧱 Technical Details

- **Language:** Java 24  
- **Framework:** Swing (MVC architecture)  
- **Build System:** Maven  
- **Data Storage:** CSV/UTF-8 files  
- **Load Time:** Max 2 seconds per pool  
- **Cross-platform:** Windows, Linux, macOS  
- **Open Source & extendable**
