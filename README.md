# ad-metrics-processor
A dual-mode (CLI + REST) Spring Boot application for processing advertising **impressions** and **clicks**, computing business metrics, and generating advertiser recommendations.

---

## ✨ Features

✔ Reads JSON files containing **impressions** and **clicks**  
✔ Supports **two modes of operation**:
- **CLI Mode** – run using file paths
- **REST API Mode** – upload files via HTTP

✔ Computes metrics per (`app_id`, `country_code`):
- impression count
- click count
- total revenue

✔ Recommends **top 5 advertisers** per (`app_id`, `country_code`) based on revenue-per-impression

✔ Pure in-memory processing (no DB, no entities)

---

## 🚀 Requirements
- Java 17+
- Maven 3.8+
- (Optional) Postman or curl for REST mode

---

## 🏗️ Build the Project
```bash
mvn clean package
```
Artifact will be generated at:
```
target/ad-metrics-processor-0.1.0.jar
```

---
# 🖥️ CLI Mode
CLI mode expects **two groups of files** separated by `--`:
```
(impression files...) -- (click files...)
```
### ▶ Example:
```bash
java -jar target/ad-metrics-processor-0.1.0.jar \
    impressions.json imp2.json \
    -- \
    clicks.json clicks2.json
```
### 📦 Output:
```
metrics.json
recommendations.json
```
If no CLI arguments are provided, the application runs in **REST mode**.

---

# 🌐 REST API Mode
Start the application without arguments:
```bash
java -jar target/ad-metrics-processor-0.1.0.jar
```

### POST `/api/process`
Upload files via multipart/form-data.

#### Example using curl:
```bash
curl -X POST http://localhost:8080/api/process \
  -F "impressions=@impressions.json" \
  -F "clicks=@clicks.json"
```

#### Example JSON Response:
```json
{
  "metrics": [ ... ],
  "recommendations": [ ... ]
}
```

---

# 📂 Input File Format
### impressions.json
```json
[
  {
    "id": "UUID",
    "app_id": 1,
    "country_code": "US",
    "advertiser_id": 42
  }
]
```

### clicks.json
```json
[
  {
    "impression_id": "UUID",
    "revenue": 0.42
  }
]
```

---
# 📊 Output Files
### metrics.json
```json
[
  {
    "app_id": 1,
    "country_code": "US",
    "impressions": 120,
    "clicks": 15,
    "revenue": 12.5
  }
]
```

### recommendations.json
```json
[
  {
    "app_id": 1,
    "country_code": "US",
    "recommended_advertiser_ids": [32, 12, 45, 4, 1]
  }
]
```

---

# 🧩 Project Structure
```
src/main/java/com/example/adprocessor
├── AdProcessorApplication.java
├── config/AppConfig.java
├── controller/ProcessingController.java
├── domain/
│   ├── Impression.java
│   ├── Click.java
│   ├── MetricsResult.java
│   └── RecommendationResult.java
├── service/
│   ├── FileReaderService.java
│   ├── MetricsService.java
│   └── RecommendationService.java
└── util/JsonWriter.java
```

---

# 🧪 Testing
Run all tests:
```bash
mvn test
```

---

# 📜 License
MIT

---

# 🤝 Contributions
Pull requests and suggestions are welcome.
