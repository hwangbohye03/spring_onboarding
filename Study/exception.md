# 🌟 요약
- **예외 (Exception):** 코드로 복구 및 제어가 가능한 애플리케이션 수준의 문제
- **에러 (Error):** `OutOfMemoryError` 등 코드로 수습 불가능한 JVM/시스템 수준의 문제
- **예외 처리:** 예외 발생 시 프로그램이 강제 종료되지 않고 정해진 흐름대로 계속 실행되도록 제어하는 작업 (예: `try-catch`등)
- **언체크 예외 (실행 예외):** `RuntimeException`을 상속받은 하위 클래스로, 컴파일러가 예외 처리를 강제하지 않는 예외
- **체크 예외 (일반 예외):** `Exception` 하위 중 `RuntimeException`클래스 상속을 제외한 예외로, 컴파일 시점에 예외 처리가 강제됨
- **try-catch:** 예외를 직접 잡아서 처리하는 블록
- **throw & throws:** `throw`는 예외를 직접 발생시키는, `throws`는 발생한 예외를 상위 메서드로 전가하는 키워드
- **전역 예외 처리기:** `@RestControllerAdvice`와 `@ExceptionHandler`를 활용해 컨트롤러 예외를 한곳에서 공통 관리하는 스프링 기능

</br>

## 📌 예외 개념 정의
* **개념:** 프로그램 실행 중 발생하는 예기치 못한 문제나 오류 상태
* **특징:** 예외가 발생하더라도 적절한 예외 처리가 있다면, 프로그램의 비정상 종료를 막고 정상 실행을 유지할 수 있음

</br>

## 📌 에러 VS 예외
* **에러 (Error):** 시스템/JVM 수준의 치명적 문제, 코드로 수습 불가능
  * *예: `OutOfMemoryError`, `StackOverflowError`*
* **예외 (Exception):** 애플리케이션 수준의 문제, 코드로 복구 및 대응 가능
  * *예: `NullPointerException`, `IOException`*

</br>

## 📌 예외 구분

### 언체크 예외 (실행 예외)
* 컴파일러가 예외 처리를 검사하지 않는 예외 → 실행 시점(Runtime)에 발생하는 예외
* 주로 프로그램 실행 중 잘못된 로직으로 인해 발생하며, 명시적인 예외 처리가 없어도 컴파일이 정상 완료됨
* 예: 
    * `NullPointerException`
    * `IllegalArgumentException`
    * `IndexOutOfBoundsException`
    * `ArithmeticException`
    * `ClassCastException`
    * `IllegalStateException`

### 체크 예외 (일반 예외)
* 컴파일 시점에 예외 처리가 검사되는 예외
* `try-catch`로 잡거나 `throws` 선언을 통한 예외 처리 필요 (미처리 시 컴파일 에러)
* 예: 
    * `IOException`
    * `FileNotFoundException`
    * `SQLException`
    * `ClassNotFoundException`

</br>

## 📌 예외 처리 방식
* **try-catch:** 예외를 직접 잡아서 처리하는 방식
  ```java
  try {
      // 예외 발생 가능 코드
  } catch (Exception e) {
      // 예외 처리 코드
  }
  ```

* **throw**: 예외를 직접 발생시키는 키워드
  ```java
  throw new IllegalArgumentException("잘못된 요청입니다.");`
  ```

* **throws (예외 전가)**: 발생한 예외를 직접 처리하지 않고, 자신을 호출한 상위 메소드로 넘기는 키워드
  ``` java
  void method() throws IOException { ... }
  ```

</br>

## 📌 Spring Boot 전역 예외 처리기

* **개념:** 컨트롤러 곳곳에서 발생하는 예외를 한 곳에 모아서 공통으로 처리하는 스프링 부트 기능
* **장점:**
  * 예외 처리 로직이 비즈니스 로직과 분리되어 코드가 명료
  * 일관된 에러 응답(JSON)을 클라이언트에 전달 가능
* **핵심 어노테이션:**
  * `@ControllerAdvice`: 전역 예외 처리 클래스로 지정
    * 애플리케이션 전반에 걸쳐 모든 컨트롤러 계층에서 발생하는 예외 포착 
    * 각 예외마다 별도 HTTP 상태 코드와 메세지 반환 (ex: `IllegalArgumentException`→`httpStatus.BAD_REQUEST`)
  * `@ExceptionHandler(예외클래스.class)`: 특정 예외 발생 시, 실행할 메소드 지정
    * 특정 컨트롤러 내 단독 선언 시, 해당 컨트롤러에서 발생하는 예외만 처리 할 수 있음
    * `@ControllerAdvice`와 사용 시, 애플리케이션 전역의 컨트롤러에서 발생하는 예외 처리 가능
  * `@ControllerAdvice`는 기본적으로 모든 컨트롤러에 적용되며, 개별 컨트롤러에 `@ExceptionHandler`선언 시 우선 처리됨 


</br>

## 📌 예외 클래스 상속 구조

```text
               Object
                  │
              Throwable
             ┌────┴────┐
          Error     Exception
                       │
            ┌──────────┴──────────┐
    OtherExceptions          RuntimeException
       (Checked)              (Unchecked)
```
* **`Throwable`:** 모든 에러와 예외의 최상위 클래스
* **`Exception`:** 모든 예외의 최상위 클래스
* **`RuntimeException`:** 이 클래스를 상속받으면 언체크 예외, 상속받지 않은 다른 Exception 하위 클래스들은 체크 예외가 됨