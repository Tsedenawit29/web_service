# Loan Management System

A modern RESTful web service for managing loans built with Spring Boot, featuring HATEOAS support, dual frontend implementations, and comprehensive loan management capabilities.

## Features

- **Loan Management**: Complete CRUD operations for loans
- **Search & Filter**: Advanced search by borrower name, status, or loan type
- **Statistics Dashboard**: Real-time statistics for total loans, pending loans, approved loans, and total amount
- **Loan Calculator**: Automatic calculation of monthly payments
- **Validation**: Server-side validation for all loan fields
- **Modern UI**: Beautiful, responsive web interface with blue and orange color scheme
- **Dual Frontend**: Both jQuery and AngularJS implementations available
- **HATEOAS Support**: Hypermedia-driven API with self-describing resources
- **CORS Enabled**: Cross-origin requests supported for web clients

## Technology Stack

- **Backend**: Spring Boot 3.2.0
- **Java Version**: 21
- **Database**: H2 (In-Memory Database) - No external database setup required!
- **ORM**: Spring Data JPA with Hibernate
- **REST**: Spring Data REST + Spring HATEOAS
- **Frontend**: 
  - jQuery 3.7.1 (`jquery.html`)
  - AngularJS 1.8.3 (`angularjs.html`)
- **Build Tool**: Maven 3.6+

## Local Development

### Prerequisites
- **Java 21** or higher
- **Maven 3.6+**
- **No database setup required!** (Uses H2 in-memory database)

### Running the Application

1. **Build the project**:
   ```bash
   mvn clean package
   ```

2. **Run the application**:
   ```bash
   java -jar target/loan-management-system-1.0.0.jar
   ```
   
   Or using Maven:
   ```bash
   mvn spring-boot:run
   ```

3. **Access the application**:
   - **Home Page**: http://localhost:8080/ or http://localhost:8080/index.html
   - **jQuery UI**: http://localhost:8080/jquery.html
   - **AngularJS UI**: http://localhost:8080/angularjs.html
   - **API Base**: http://localhost:8080/api/loans
   - **H2 Console** (Development): http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:loanmanagement`
     - Username: `sa`
     - Password: (leave empty)

## API Endpoints

### Loans
- `GET /api/loans` - Get all loans (with HATEOAS links)
- `GET /api/loans/{id}` - Get loan by ID
- `POST /api/loans` - Create new loan
- `PUT /api/loans/{id}` - Update loan
- `DELETE /api/loans/{id}` - Delete loan
- `GET /api/loans/search?borrowerName=...&status=...&loanType=...` - Search loans
- `GET /api/loans/stats` - Get loan statistics

### Example API Request

```bash
# Create a loan
curl -X POST http://localhost:8080/api/loans \
  -H "Content-Type: application/json" \
  -d '{
    "borrowerName": "John Doe",
    "loanAmount": 50000,
    "interestRate": 5.5,
    "loanTermMonths": 60,
    "loanType": "PERSONAL",
    "status": "PENDING"
  }'
```

### Example HATEOAS Response

```json
{
  "id": 1,
  "borrowerName": "John Doe",
  "loanAmount": 50000,
  "interestRate": 5.5,
  "loanTermMonths": 60,
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/loans/1"
    },
    "loans": {
      "href": "http://localhost:8080/api/loans"
    },
    "update": {
      "href": "http://localhost:8080/api/loans/1"
    },
    "delete": {
      "href": "http://localhost:8080/api/loans/1"
    }
  }
}
```

## Deployment on Render

### Step 1: Prepare Your Repository
1. Push your code to GitHub/GitLab/Bitbucket
2. Make sure all files are committed

### Step 2: Deploy Web Service
1. Go to [Render Dashboard](https://dashboard.render.com)
2. Click "New +" → "Web Service"
3. Connect your repository
4. Configure the service:
   - **Name**: `loan-management-system`
   - **Environment**: `Java`
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/loan-management-system-1.0.0.jar`
   - **Port**: 8080 (Render sets PORT automatically)

### Step 3: Set Environment Variables (Optional)
- `PORT`: 8080 (usually set automatically by Render)
- **Note**: No database configuration needed! H2 runs in-memory.

### Step 4: Deploy
1. Click "Create Web Service"
2. Wait for the build to complete (5-10 minutes)
3. Your application will be available at: `https://your-app-name.onrender.com`

### Important Notes for Render Deployment
- **H2 Database**: The application uses H2 in-memory database, which means data will be reset when the service restarts
- **For Production**: Consider switching to a persistent database (PostgreSQL, MySQL) if you need data persistence
- **Free Tier**: Render's free tier may spin down services after inactivity, which will reset the in-memory database

## Project Structure

```
web_service/
├── src/
│   ├── main/
│   │   ├── java/com/loanmanagement/
│   │   │   ├── LoanManagementApplication.java
│   │   │   ├── config/
│   │   │   │   └── CorsConfig.java          # CORS Configuration
│   │   │   ├── controller/
│   │   │   │   └── LoanController.java      # REST Controller with HATEOAS
│   │   │   ├── model/
│   │   │   │   └── Loan.java                 # JPA Entity
│   │   │   ├── repository/
│   │   │   │   └── LoanRepository.java      # JPA Repository
│   │   │   └── exception/
│   │   │       └── GlobalExceptionHandler.java
│   │   └── resources/
│   │       ├── application.properties        # H2 Configuration
│   │       └── static/
│   │           ├── index.html                # Landing Page
│   │           ├── jquery.html                # jQuery Client
│   │           └── angularjs.html            # AngularJS Client
│   └── test/
├── pom.xml                                    # Maven config (Java 21)
├── render.yaml                                # Render deployment config
└── README.md
```

## Testing the Application

### Using jQuery Client
1. **Access**: http://localhost:8080/jquery.html
2. **Create a Loan**: Fill in the form and click "Add Loan"
3. **View Loans**: All loans appear in the table below
4. **Search**: Use the search filters to find specific loans
5. **Edit/Delete**: Use the action buttons in the table
6. **View Stats**: Check the statistics cards at the top

### Using AngularJS Client
1. **Access**: http://localhost:8080/angularjs.html
2. **Same functionality** as jQuery client but using AngularJS framework
3. **Compare**: Both clients consume the same REST API

### Using API Directly
1. Use curl, Postman, or any HTTP client
2. All endpoints support HATEOAS links
3. Check response headers for CORS support

## Database Information

### H2 In-Memory Database
- **Type**: In-memory (data persists only during application runtime)
- **JDBC URL**: `jdbc:h2:mem:loanmanagement`
- **Console**: Available at `/h2-console` (development only)
- **Auto-Configuration**: Tables are created automatically on startup
- **No Setup Required**: Works out of the box!

### Switching to Persistent Database (Optional)
If you need data persistence, you can easily switch to PostgreSQL or MySQL:

1. **Add dependency** in `pom.xml`:
   ```xml
   <dependency>
       <groupId>org.postgresql</groupId>
       <artifactId>postgresql</artifactId>
   </dependency>
   ```

2. **Update** `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/loanmanagement
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
   ```

## Features in Detail

### HATEOAS Support
All API responses include hypermedia links for navigation:
- Self-links for individual resources
- Collection links for resource collections
- Action links for update and delete operations

### CORS Configuration
Cross-origin requests are enabled to allow web clients from different domains to access the API. Configuration supports all HTTP methods and can be customized for production environments.

### Validation
Server-side validation ensures data integrity:
- Borrower name: 2-100 characters
- Loan amount: $100 - $1,000,000
- Interest rate: 0% - 30%
- Loan term: 1-360 months

### Monthly Payment Calculation
Automatic calculation using the standard loan amortization formula:
```
Monthly Payment = P × (r(1+r)^n) / ((1+r)^n - 1)
Where:
P = Principal (loan amount)
r = Monthly interest rate (annual rate / 12 / 100)
n = Number of months
```

## Troubleshooting

### Java Version Issues
- Ensure **Java 21** is installed: `java -version`
- Update `JAVA_HOME` environment variable if needed

### Port Issues
- Render automatically sets the PORT environment variable
- The application reads `PORT` from environment or defaults to 8080
- Check if port 8080 is already in use: `lsof -i :8080`

### Build Issues
- Ensure Java 21+ is installed
- Run `mvn clean install` to verify the build locally
- Check Maven version: `mvn -version`

### Database Issues
- H2 is in-memory, so data resets on application restart (this is expected behavior)
- For persistent data, switch to PostgreSQL or MySQL (see Database Information section)

### Frontend Issues
- Clear browser cache if changes don't appear
- Check browser console for JavaScript errors
- Ensure CORS is properly configured (already done in `CorsConfig.java`)

## License

This project is created for educational purposes.
