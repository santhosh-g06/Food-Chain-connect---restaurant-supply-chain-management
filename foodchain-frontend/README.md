# 🌾 FoodChain Connect — Setup Guide
> Spring Boot + Supabase PostgreSQL + React Frontend

---

## 📁 Project Structure

```
foodchain-backend/
├── src/main/java/com/foodchain/backend/
│   ├── FoodchainBackendApplication.java   ← Main entry point
│   ├── controller/FoodController.java     ← REST endpoints
│   ├── model/FoodItem.java               ← Entity / DB table
│   ├── repository/FoodRepository.java    ← DB queries
│   └── service/FoodService.java          ← Business logic
├── src/main/resources/
│   └── application.properties            ← ✅ Updated for Supabase
└── pom.xml                               ← ✅ Updated: PostgreSQL driver

frontend/
└── index.html                            ← Complete React frontend (no build needed)
```

---

## STEP 1 — Create Your Supabase Database

1. Go to **https://supabase.com** → Sign up / Log in
2. Click **"New Project"** → Name it `foodchain`
3. Set a strong DB password (save it!)
4. Wait ~2 minutes for the project to provision

### Get Your Connection String
1. In your project → **Settings** → **Database**
2. Under **Connection string**, choose **JDBC**
3. Copy the string. It looks like:
   ```
   jdbc:postgresql://aws-0-ap-south-1.pooler.supabase.com:6543/postgres
   ```
4. Also note your **Project Ref** (the string in your project URL, e.g. `abcdefghijklm`)

---

## STEP 2 — Configure the Backend

Open `src/main/resources/application.properties` and replace:

```properties
# Replace YOURPROJECTREF with your actual Supabase project ref
# Replace YOURPASSWORD with your Supabase DB password

spring.datasource.url=jdbc:postgresql://aws-0-ap-south-1.pooler.supabase.com:6543/postgres?user=postgres.YOURPROJECTREF&password=YOURPASSWORD&sslmode=require
spring.datasource.username=postgres.YOURPROJECTREF
spring.datasource.password=YOURPASSWORD
```

### ⚠️ Region Note
If your Supabase project is NOT in `ap-south-1`, update the host accordingly.
Find the correct host in Supabase → Settings → Database → Connection string.

---

## STEP 3 — Run the Backend

Make sure you have **Java 17+** and **Maven** installed.

```bash
# From your project root folder
./mvnw spring-boot:run
```

You should see:
```
Started FoodchainBackendApplication on port 8080
```

### Test It
Open your browser and visit:
- http://localhost:8080/api/food/available  → Should return `[]`
- http://localhost:8080/api/food/urgent     → Should return `[]`

---

## STEP 4 — Open the Frontend

No Node.js, no npm, no build step needed.

Just **double-click** `index.html` to open it in your browser.

Or, for CORS to work properly, serve it simply:

```bash
# Python (if installed)
python -m http.server 3000
# then visit http://localhost:3000
```

---

## STEP 5 — Use the App

| Role       | Tab        | What you can do                                  |
|------------|------------|--------------------------------------------------|
| Restaurant | 🍽️ Restaurant | Add surplus food with name, qty, unit, expiry |
| NGO        | 🌱 NGO        | Browse all available food, claim urgent items |
| Admin      | 📊 Admin      | See all listings, stats, waste prevention rate|

---

## 🔌 REST API Reference

| Method | URL                         | Description                          |
|--------|-----------------------------|--------------------------------------|
| POST   | `/api/food/add`             | Add a new food donation              |
| GET    | `/api/food/available`       | Get all AVAILABLE food items         |
| GET    | `/api/food/urgent`          | Get items expiring within 12 hours   |
| PATCH  | `/api/food/{id}/status`     | Update item status (claim/donate)    |

### POST `/api/food/add` — Example Body
```json
{
  "name": "Biryani",
  "quantity": 5.0,
  "unit": "kg",
  "expiryDate": "2026-06-09T18:00:00",
  "status": "AVAILABLE"
}
```

---

## 🔧 Adding the PATCH Endpoint (Claim Feature)

The claim button in the NGO view needs this endpoint in `FoodController.java`:

```java
// Add this import
import java.util.Map;

// Add this method to FoodController
@PatchMapping("/{id}/status")
public ResponseEntity<FoodItem> updateStatus(
        @PathVariable Long id,
        @RequestBody Map<String, String> body) {
    FoodItem updated = foodService.updateStatus(id, body.get("status"));
    return ResponseEntity.ok(updated);
}
```

And in `FoodService.java`:

```java
public FoodItem updateStatus(Long id, String status) {
    FoodItem item = foodRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Item not found"));
    item.setStatus(status);
    return foodRepository.save(item);
}
```

---

## 🗄️ Database Table (Auto-Created by Hibernate)

Hibernate creates the `food_item` table automatically on first run:

| Column       | Type          | Notes                          |
|--------------|---------------|--------------------------------|
| id           | BIGINT        | Auto-incremented primary key   |
| name         | VARCHAR       | Food name                      |
| quantity     | DOUBLE        | Amount                         |
| unit         | VARCHAR       | kg, packets, portions, etc.    |
| expiry_date  | TIMESTAMP     | When it expires                |
| status       | VARCHAR       | AVAILABLE, CLAIMED, DONATED    |

You can also see your live data in Supabase → Table Editor.

---

## ❓ Troubleshooting

| Problem | Fix |
|---------|-----|
| `Connection refused` on frontend | Start Spring Boot first: `./mvnw spring-boot:run` |
| `CORS error` in browser | Add `@CrossOrigin(origins="*")` is already on your controller ✅ |
| `password authentication failed` | Double-check Supabase password in `application.properties` |
| Port 8080 in use | Add `server.port=8081` to `application.properties`, update `API_BASE` in `index.html` |
| Blank page on `index.html` | Open browser DevTools (F12) → Console for errors |
