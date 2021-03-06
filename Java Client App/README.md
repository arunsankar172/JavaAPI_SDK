# org.wso2.client.EmployeeAPIM

Employee APIM
- API version: 1.0.0
  - Build date: 2022-04-07T10:04:07.733592Z[GMT]

Employee services


*Automatically generated by the [OpenAPI Generator](https://openapi-generator.tech)*


## Requirements

Building the API client library requires:
1. Java 1.7+
2. Maven/Gradle

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn clean install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn clean deploy
```

Refer to the [OSSRH Guide](http://central.sonatype.org/pages/ossrh-guide.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
  <groupId>org.wso2</groupId>
  <artifactId>org.wso2.client.EmployeeAPIM</artifactId>
  <version>1.0.0</version>
  <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "org.wso2:org.wso2.client.EmployeeAPIM:1.0.0"
```

### Others

At first generate the JAR by executing:

```shell
mvn clean package
```

Then manually install the following JARs:

* `target/org.wso2.client.EmployeeAPIM-1.0.0.jar`
* `target/lib/*.jar`

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

// Import classes:
import org.wso2.client.api.ApiClient;
import org.wso2.client.api.ApiException;
import org.wso2.client.api.Configuration;
import org.wso2.client.api.auth.*;
import org.wso2.client.api.models.*;
import org.wso2.client.api.EmployeeAPIM.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure OAuth2 access token for authorization: default
    OAuth default = (OAuth) defaultClient.getAuthentication("default");
    default.setAccessToken("YOUR ACCESS TOKEN");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    Employee employee = new Employee(); // Employee | New Employee
    try {
      Employee result = apiInstance.createPost(employee);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#createPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

## Documentation for API Endpoints

All URIs are relative to *http://localhost*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*DefaultApi* | [**createPost**](docs/DefaultApi.md#createPost) | **POST** /create | Add new Empoyee
*DefaultApi* | [**deleteEidDelete**](docs/DefaultApi.md#deleteEidDelete) | **DELETE** /delete/{eid} | Delete a Employees
*DefaultApi* | [**downloadEidGet**](docs/DefaultApi.md#downloadEidGet) | **GET** /download/{eid} | Download Employee Resume
*DefaultApi* | [**listGet**](docs/DefaultApi.md#listGet) | **GET** /list | List all Employees
*DefaultApi* | [**updatePut**](docs/DefaultApi.md#updatePut) | **PUT** /update | Update a employee
*DefaultApi* | [**uploadEidPost**](docs/DefaultApi.md#uploadEidPost) | **POST** /upload/{eid} | Upload Employee resume


## Documentation for Models

 - [Employee](docs/Employee.md)
 - [InlineObject](docs/InlineObject.md)
 - [Update](docs/Update.md)


## Documentation for Authorization

Authentication schemes defined for the API:
### default

- **Type**: OAuth
- **Flow**: implicit
- **Authorization URL**: https://test.com
- **Scopes**: N/A


## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author



