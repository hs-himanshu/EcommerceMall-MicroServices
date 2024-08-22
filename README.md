# Spring Boot Microservices Monorepo

This project is a monorepo that contains multiple Spring Boot microservices. It includes services such as UserService and EcommerceMall, and is integrated with Kafka for event streaming and Razorpay for payment processing.

## Prerequisites

To run this project, make sure you have the following installed on your system:

- Java 11 or later
- Maven 3.6+
- Zookeeper
- Kafka
- IntelliJ IDEA (optional but recommended)
### 3. Set Environment Variables in IntelliJ

Configure the following environment variables for your services:

- **User Service**:
    - `SERVER_PORT=4040`
  
- **Ecommerce Mall**:
    - `SERVER_PORT=8090`
  
- **Razorpay Keys**:
    - `RAZORPAY_KEY_ID=your_razorpay_test_key_id`
    - `RAZORPAY_KEY_SECRET=your_razorpay_key_secret`

You can set these in IntelliJ by going to `Run > Edit Configurations...` and adding these environment variables under the appropriate configuration.

## API Gateway (Example API's)

### **GET** - Call UserService (token User)
- **URL**: `http://localhost:8888/api/v1/auth/hello`
- **Authorization**: Bearer Token
  - Token: `<token>`

### **POST** - Log In
- **URL**: `http://localhost:8888/api/v1/auth/login`
- **Body** (raw JSON):
  ```json
  {
      "username": "abc",
      "email": "abc@gmail.com",
      "password": "abc@123"
  }

## EcommerceMall

### Admin APIs

#### **POST** - Create Product (token with Role Admin)
- **URL**: `http://localhost:8080/api/v1/admin/product`
- **Authorization**: Bearer Token
  - Token: `<token>`
- **Body** (raw JSON):
  ```json
  {
    "name": "Sample2 Product",
    "description": "This is a description of the sample product.",
    "price": 29.99,
    "userId": "3",
    "imageUrl": "http://ttt.com/image.jpg",
    "stock": 100,
    "category": {
      "id": 2
    }
  }

#### **GET** - My Products (token with Role Admin)
- **URL**: `localhost:8080/api/v1/admin/my-products/7`
- **Authorization**: Bearer Token
  - Token: `<token>`

#### **DELETE** - Delete Product by UUID (token with Role Admin)
- **URL**: `localhost:8080/api/v1/admin/product/3c436f33-c699-458b-8417-9adbe491e67b`
- **Authorization**: Bearer Token
  - Token: `<token>`

#### **GET** - Received Order (token with Role Admin)
- **URL**: `localhost:8080/api/v1/admin/received-orders/3`
- **Authorization**: Bearer Token
  - Token: `<token>`

### User APIs

#### **GET** - All Products Pagination (token with Role User)
- **URL**: `localhost:8080/api/v1/products?page=1&size=3`
- **Authorization**: Bearer Token
  - Token: `<token>`

#### **GET** - Product by Id (token with Role User)
- **URL**: `localhost:8080/api/v1/product/82772da4-9ab4-4e99-aac1-357eb6f9b93c`
- **Authorization**: Bearer Token
  - Token: `<token>`

#### **GET** - All Categories (token with Role User)
- **URL**: `localhost:8080/api/v1/categories`
- **Authorization**: Bearer Token
  - Token: `<token>`

#### **POST** - Place Order (token with Role User)
- **URL**: `localhost:8080/api/v1/place-order`
- **Authorization**: Bearer Token
  - Token: `<token>`
- **Body** (raw JSON):
  ```json
  {
  "userId": 3,
  "total": 99.99,
  "orderItems": [
    {
      "productId": "0a98b2fe-03e5-44f5-885d-662acf1c1e03",
      "quantity": 2
    },
    {
      "productId": "1772acca-ea7e-4465-8b04-1668e3308935",
      "quantity": 1
    }
    ]
  }

## PaymentService
#### **POST** - Razorpay 
- **URL**: `localhost:9090/api/v1/payments/razorpay`
- - **Body** (raw JSON):
  ```json
  {
    "orderId":"order-1343-sdf23kdfgsf22ertert3k",
    "amount":"1000",
    "paymentProvider":"razorpay"
  }

## UserService
#### **POST** - SignUp 
- **URL**: `localhost:4040/api/v1/auth/signup`
- **Body** (raw JSON):
  ```json
  {
  "firstName":"test",
  "lastName":"test",
  "email":"test@gmail.com",
  "password":"test@123"
  }

#### **POST** - Log In 
- **URL**: `localhost:4040/api/v1/auth/login`
- **Body** (raw JSON):
  ```json
  {
    "username":"abc",
    "email":"abc@gmail.com",
    "password":"abc@123"
  }
  
#### **POST** - Sign Out (Send Bearer Token) 
- **URL**: `localhost:4040/api/v1/auth/logout`
- **Authorization**: Bearer Token
  - Token: `<token>`

#### **POST** - Refresh Token (token with Role User)
- **URL**: `localhost:4040/api/v1/auth/refresh-token`
- **Authorization**: Bearer Token
  - Token: `<token>`
- **Body** (raw JSON):
  ```json
  {
    "refreshToken":"2b66a263-e9d6-4590-a111-f2187190f980"
  }
