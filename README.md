# College Search 

## Overview

This app is a Java Spring Boot service designed to provide detailed information about colleges in the United States. Its core functionality is to allow users or client applications to search for colleges based on a variety of parameters such as year, degree types, location (city, state, zip), and pagination options, and to return structured results with comprehensive college details.

## Architecture and Core Components

### 1. **Controller Layer**

- **`CollegeSearchController`**
  - This is the entry point for HTTP requests related to college search.
  - It exposes a RESTful endpoint `/colleges` via the `@GetMapping` annotation.
  - The endpoint supports multiple query parameters, including:
    - `year` (required)
    - `degrees`, `zipcode`, `city`, `state`, `per_page`, `page` (all optional)
  - The controller delegates query processing to the `CollegeSearchService`.

### 2. **Service Layer**

- **`CollegeSearchService`**
  - This is the main business logic layer.
  - It uses configuration values (`api.key`, `api.url`) to connect to an external data source (typically a college scorecard API).
  - Major responsibilities include:
    - Constructing API queries dynamically based on user input.
    - Fetching college data via Spring's `RestTemplate`.
    - Mapping raw API results into internal models.
    - Handling exceptions and returning safe, empty responses in case of errors.

#### Key Methods:
  - `getInfo`: Orchestrates the search, builds API requests, fetches results, and transforms them.
  - `getUriComponentsBuilder`: Dynamically builds the external API URI with the necessary filters and fields.
  - `mapResultToResponse`: Converts raw API map results into a list of `College` model objects, handling missing or null fields gracefully.
  - `getStudentSizeField`: Returns the correct field for student size, depending on the year parameter.

### 3. **Model Layer**

- **`College`**
  - Represents a college entity with fields such as:
    - `collegeName`, `city`, `zip`, `state`, `website`, `accreditor`, `feeCalaculationUrl`, `totalStudents`.
- **`CollegeSearchResponse`**
  - Wraps the overall API response, including a list of `College` objects and metadata.

### 4. **Utilities**

- **`CollegeConstants`**
  - Centralizes string constants for mapping API fields to internal model attributes.
  - Ensures consistency and reduces string duplication.

## Data Flow

1. **Request:** A client makes a GET request to `/colleges` with specific query parameters.
2. **Processing:** The controller passes these parameters to the service, which constructs a query to the external college scorecard API.
3. **Fetching Data:** The service receives a JSON response, parses metadata and college results, and maps them to internal models.
4. **Response:** A structured `CollegeSearchResponse` object is returned, containing metadata and a sorted list of colleges (sorted by college name).

## Error Handling

- Service methods use try-catch blocks to handle errors during API calls or data transformation.
- In case of exceptions, an empty response is returned and the error is logged.

## Unit Testing

- The repository includes comprehensive unit tests (see `CollegeSearchServiceTest`) using JUnit and Mockito.
- Tests verify correct mapping, API construction, error handling, and edge cases (e.g., null or empty results).

## Example

A typical API call might look like:

```
GET /colleges?year=2020&degrees=3&zipcode=12345&city=Springfield&state=IL&per_page=20&page=1
```

The response includes for each college:
- Name
- City, ZIP, State
- Website
- Accreditor
- Fee calculation URL
- Total number of students

## Extensibility

- The service is designed to be easily extended:
  - New search parameters can be added by extending the controller and updating the URI builder.
  - Additional fields can be mapped in the `College` model and constants.

## Technologies Used

- Java 11+
- Spring Boot (Web)
- Lombok (for data classes)
- Mockito/JUnit (for testing)

## Summary

This College search app  is an API-centric backend service for querying and retrieving structured college data, focusing on flexibility, robust error handling, and clear, testable separation of concerns.
