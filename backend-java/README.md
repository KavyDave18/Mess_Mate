# MessMate Backend (Java + MySQL)

## Requirements
- Java 17+
- MySQL
- Maven (install with `brew install maven` on Mac)

## Setup
1. **Clone or copy this backend-java folder.**
2. **Create a MySQL database:**
   ```sql
   CREATE DATABASE messmate_db;
   ```
3. **Update `src/main/resources/application.properties`** with your MySQL username and password.
4. **Build and run:**
   ```sh
   mvn spring-boot:run
   ```

## API Endpoints
- `POST /api/auth/register` — Register user
- `POST /api/auth/login` — Login
- `GET /api/user/{id}/coins` — Get coin balance
- `GET /api/user/{id}/coupons` — List user coupons
- `POST /api/redeem/{userId}/coupon/{couponId}` — Redeem coupon with coins
- `GET /api/cart/{userId}` — Get cart
- `POST /api/cart/{userId}/add` — Add item to cart
- `POST /api/cart/{userId}/checkout?userCouponId=...` — Checkout and apply coupon

## Notes
- The backend will auto-create tables on first run.
- You can pre-populate the `coupons` table with SQL or a Spring Boot data initializer. 