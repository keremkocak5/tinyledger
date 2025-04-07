# Tiny Ledger API

A simple in-memory app for storing accounts, and transactions.


## Running the application

The app can be started either with Maven or Docker.

### a. Docker
Make sure you have Java 17, Maven, and Docker installed on your device. Then run the following commands at the root folder (where Dockerfile resides) of your project.

```
mvn clean package

docker build -t tinyledger .

docker run -p 8080:8080 tinyledger
```


### b. Maven

Make sure you have Java 17 and Maven installed on your device. Then run the following command at the root folder (where pom.xml resides) of your project.

```
mvn spring-boot:run
```

## Accessing the APIs

Swagger is enabled.

http://localhost:8080/ledger/swagger-ui/index.html


## Dependencies
Due to its in-memory database, this app is not horizontally scalable.



# API Documentation

### Base URL

The base URL for all API requests is:

http://localhost:8080/ledger


---

# Ledger API Documentation

- **API Version**: v0
- **Base URL**: `http://localhost:8080/ledger`
- **OpenAPI Version**: 3.0.1

---

## Endpoints

### 1. Get Transactions of an Account

- **Path**: `/v1/transactions/account/{accountId}`
- **Method**: `GET`
- **Tags**: `transaction-controller`
- **Summary**: Retrieves the transaction history for a specific account.
- **Operation ID**: `getTransactions`

#### Parameters

| Name       | In   | Type   | Required | Description           |
|------------|------|--------|----------|-----------------------|
| `accountId`| Path | String | Yes      | UUID of the account   |

#### Responses

- **200 OK**
  - **Description**: Successfully retrieved transactions.
  - **Content Type**: `*/*`
  - **Schema**: `TransactionBaseResponse`
    ```json
    {
      "transactions": [
        {
          "id": "string (uuid)",
          "amount": "number",
          "currencyCode": "string",
          "transactionType": "string (DEPOSIT | WITHDRAW)",
          "transactionDate": "string (date-time)"
        }
      ]
    }
    ```

#### Example Request
```
GET /v1/transactions/account/123e4567-e89b-12d3-a456-426614174000
```

#### Example Response
```json
{
  "transactions": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "amount": 100.50,
      "currencyCode": "USD",
      "transactionType": "DEPOSIT",
      "transactionDate": "2025-04-07T10:00:00Z"
    }
  ]
}
```

---

### 2. Create a Transaction

- **Path**: `/v1/transactions/account/{accountId}`
- **Method**: `POST`
- **Tags**: `transaction-controller`
- **Summary**: Creates a new transaction (deposit or withdrawal) for an account.
- **Operation ID**: `createTransaction`

#### Parameters

| Name       | In   | Type   | Required | Description           |
|------------|------|--------|----------|-----------------------|
| `accountId`| Path | String | Yes      | UUID of the account   |

#### Request Body

- **Content Type**: `application/json`
- **Required**: Yes
- **Schema**: `TransactionRequest`
  ```json
  {
    "amount": "number (minimum: >0)",
    "transactionType": "string (DEPOSIT | WITHDRAW)"
  }
  ```

#### Responses

- **200 OK**
  - **Description**: Transaction created successfully.
  - **Content Type**: `*/*`
  - **Schema**: `TransactionResponse`
    ```json
    {
      "id": "string (uuid)",
      "amount": "number",
      "currencyCode": "string (1-4 characters)",
      "transactionType": "string (DEPOSIT | WITHDRAW)",
      "transactionDate": "string (date-time)"
    }
    ```

#### Example Request
```
POST /v1/transactions/account/123e4567-e89b-12d3-a456-426614174000
Content-Type: application/json

{
  "amount": 50.75,
  "transactionType": "WITHDRAW"
}
```

#### Example Response
```json
{
  "id": "987fcdeb-12d3-4e5b-89ab-426614174000",
  "amount": 50.75,
  "currencyCode": "USD",
  "transactionType": "WITHDRAW",
  "transactionDate": "2025-04-07T12:00:00Z"
}
```

---

### 3. Create an Account

- **Path**: `/v1/accounts`
- **Method**: `POST`
- **Tags**: `account-controller`
- **Summary**: Creates a new account.
- **Operation ID**: `createAccount`

#### Request Body

- **Content Type**: `application/json`
- **Required**: Yes
- **Schema**: `AccountRequest`
  ```json
  {
    "accountOwnerName": "string (1-100 characters)"
  }
  ```

#### Responses

- **200 OK**
  - **Description**: Account created successfully.
  - **Content Type**: `*/*`
  - **Schema**: `AccountResponse`
    ```json
    {
      "id": "string (uuid)",
      "accountOwnerName": "string",
      "balance": "number"
    }
    ```

#### Example Request
```
POST /v1/accounts
Content-Type: application/json

{
  "accountOwnerName": "John Doe"
}
```

#### Example Response
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "accountOwnerName": "John Doe",
  "balance": 0.0
}
```

---

### 4. Get Balance of an Account

- **Path**: `/v1/accounts/id/{accountId}/balance`
- **Method**: `GET`
- **Tags**: `account-controller`
- **Summary**: Retrieves the current balance of a specific account.
- **Operation ID**: `getBalance`

#### Parameters

| Name       | In   | Type   | Required | Description           |
|------------|------|--------|----------|-----------------------|
| `accountId`| Path | String | Yes      | UUID of the account   |

#### Responses

- **200 OK**
  - **Description**: Successfully retrieved account balance.
  - **Content Type**: `*/*`
  - **Schema**: `BalanceResponse`
    ```json
    {
      "balance": "number"
    }
    ```

#### Example Request
```
GET /v1/accounts/id/123e4567-e89b-12d3-a456-426614174000/balance
```

#### Example Response
```json
{
  "balance": 150.25
}
```

---

## Schemas

### TransactionRequest
```json
{
  "type": "object",
  "properties": {
    "amount": {
      "type": "number",
      "minimum": 0.0,
      "exclusiveMinimum": true
    },
    "transactionType": {
      "type": "string",
      "enum": ["DEPOSIT", "WITHDRAW"]
    }
  }
}
```

### TransactionResponse
```json
{
  "type": "object",
  "required": ["amount", "currencyCode", "id", "transactionDate", "transactionType"],
  "properties": {
    "id": {
      "type": "string",
      "format": "uuid"
    },
    "amount": {
      "type": "number",
      "minimum": 0.0
    },
    "currencyCode": {
      "type": "string",
      "maxLength": 4,
      "minLength": 1
    },
    "transactionType": {
      "type": "string",
      "enum": ["DEPOSIT", "WITHDRAW"]
    },
    "transactionDate": {
      "type": "string",
      "format": "date-time"
    }
  }
}
```

### AccountRequest
```json
{
  "type": "object",
  "required": ["accountOwnerName"],
  "properties": {
    "accountOwnerName": {
      "type": "string",
      "maxLength": 100,
      "minLength": 1
    }
  }
}
```

### AccountResponse
```json
{
  "type": "object",
  "properties": {
    "id": {
      "type": "string",
      "format": "uuid"
    },
    "accountOwnerName": {
      "type": "string"
    },
    "balance": {
      "type": "number"
    }
  }
}
```

### TransactionBaseResponse
```json
{
  "type": "object",
  "properties": {
    "transactions": {
      "type": "array",
      "items": {
        "$ref": "#/components/schemas/TransactionResponse"
      }
    }
  }
}
```

### BalanceResponse
```json
{
  "type": "object",
  "properties": {
    "balance": {
      "type": "number"
    }
  }
}
```

