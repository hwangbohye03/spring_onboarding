# 🌟 요약

- **영속성:** 프로그램이 종료되어도 메모리의 객체 데이터를 DB 등 저장소에 영구적으로 유지·보관하는 특성
- **ORM:** Object-Relational Mapping이란, 자바 객체와 RDB 테이블을 매핑하여 SQL 없이 객체(코드)로 DB를 조작하는 기술
- **JPA:** 자바 객체에 영속성을 부여하기 위한 ORM 표준 규격 (인터페이스)
- **Hibernate:** JPA 표준을 실제로 구현한 엔진 중 하나 (구현체)
- **Spring Data JPA:** JPA를 더 편리하게 쓰게 해주는 도우미 (확장 도구/라이브러리 모듈)
- **DTO:** Data Transfer Object로, 여러 필드를 하나의 객체로 묶어 전달하는 데이터 교환용 클래스 객체
- **Entity:** DB 테이블과 1:1 매핑되어 DB와 상호작용하기 위한 클래스 객체
- **DTO & Entity:** 각 레이어의 책임을 명확히 구분하고 시스템 유연성 향상
- **DDD와 DTO:** 엔티티 내부에 핵심 처리 로직이 포함되므로, 외부 노출을 막기 위해 DTO를 적극 분리해서 사용
- **DTO 장점:** 보안성 + 데이터 캡슐화 + 레이어 간 결합도 감소
- **DTO 단점:** 코드 복잡도 증가 + 성능 저하 가능성

</br>

## 📌 JPA 개념

- **개념:** 객체 지향 언어인 자바의 (클래스) 객체와 RDB 테이블 간 데이터 매핑 및 관리를 위한 표준 ORM 기술
    - **ORM:** Object Relational Mapping (자바 객체와 RDB 테이블을 매핑하여 SQL 없이 객체로 DB를 조작하는 기술)
- **효과:**
    - 보일러플레이트 코드 (CRUD 등) 기본 제공하므로, SQL을 직접 작성하지 않고도 DB와 상호작용하는 앱 개발 가능
    - 개발자가 SQL 작성이나 커넥션 관리 등에 대한 작업을 줄여, 비즈니스 로직에 집중할 수 있어 개발 생산성 향상
    - DB 측에서 Connector 드라이버만 제공한다면, 어떤 DB든 연동 가능
- JPA 인터페이스를 통해 코드를 작성하면, 실제 DB 연동은 JPA의 구현체가 자동으로 처리
- **JPA의 대표적인 구현체:** Hibernate, EclipseLink, OpenJPA 등
    - *(MyBatis는 SQL 매퍼 프레임워크로 ORM이 아니며, SQL 쿼리를 개발자가 직접 작성 및 제어하는 구조)*

</br>

## 📌 JPA 등장 배경

- 2006년 EJB(Enterprise Java Beans)의 일부로 처음 도입되었으나, 복잡한 설정과 사용성 문제로 외면받음
- 이러한 문제를 극복하기 위해 더 간결하고 직관적인 ORM 기술인 JPA로 재탄생함
- JPA는 EJB의 일부로 시작되었지만 현재는 EJB 없이 독립적으로 동작하며, 자바 생태계의 표준 ORM 기술로 자리 잡음

</br>

## 📌 JPA와 Hibernate의 관계

- **JPA:** 자바 앱에서 ORM 구현을 위한 표준 명세(표준 규격)로, 자체로 실행 가능한 기술이 아닌 일종의 인터페이스 모음임
- **Hibernate:** JPA 표준을 실제로 구현한 대표적인 구현체 엔진으로, JPA 기본 기능 외에도 고급 기능(배치 처리, 2차 캐싱, 다양한 통계 기능 등)을 추가로 제공함


</br>

## 📌 JPA 주요 애노테이션

JPA 기반 DB 접근 코드 작성 시 활용하는 주요 애노테이션 정리입니다.

| 구분 | 애노테이션 | 주요 역할 및 설명 |
| :--- | :--- | :--- |
| **엔티티 & 테이블** | `@Entity` | • 해당 클래스가 JPA 엔티티임을 명시<br>• 해당 클래스를 DB 테이블과 매핑 |
| | `@Table` | • 엔티티 클래스와 1:1 매핑할 DB 테이블 이름 지정 (생략 시 클래스명 사용) |
| **기본키 (PK)** | `@Id` | • 기본키(PK) 필드임을 지정 |
| | `@GeneratedValue` | • 기본키(PK) 생성 전략 설정<br>• `IDENTITY`: DB에 PK 생성 위임 (예: MySQL `AUTO_INCREMENT`)<br>• `SEQUENCE`: DB 시퀀스 사용해 PK 생성 (예: 주로 Oracle에서 사용)<br>• `TABLE`: PK 생성용 전용 테이블을 만들어 PK 생성<br>• `AUTO`: JPA 구현체에 PK 생성 위임 |
| **컬럼 매핑** | `@Column` | • 필드와 DB 컬럼을 매핑<br>• 이름, 길이, null 허용 여부, 제약 조건 등 추가 설정도 가능 |
| **연관관계 (다대일/일대다)** | `@ManyToOne` | • N:1 관계 매핑 (예: 주문 N개 - 회원 1명) |
| | `@OneToMany` | • 1:N 관계 매핑 (예: 회원 1명 - 주문 N개) |
| **연관관계 (일대일/다대다)** | `@OneToOne` | • 1:1 관계 매핑 (예: 회원 1명 - 보관함 1개) |
| | `@ManyToMany` | • N:M 관계 매핑 (실무에서는 매핑 테이블 엔티티 분리 권장) |
| **매핑 연결** | `@JoinColumn` | • 외래키(FK)를 매핑할 DB 컬럼 지정 (연관관계의 주인 필드에 선언) |
| | `@JoinTable` | • N:M 또는 조인 테이블 방식 연결 시, 중간 매핑 테이블 지정 |


</br>

## 📌 Spring Data JPA

- **개념:** JPA를 더 쉽게 사용할 수 있도록 돕는 스프링 모듈
- **효과:** 개발자가 쿼리를 직접 작성하지 않아도, 인터페이스 메서드 이름만으로 다양한 DB 작업 수행 가능

#### 1. Repository 기반 데이터 접근
- `CrudRepository` 및 `JpaRepository` 같은 기본 인터페이스를 제공하며, 공통 DB 접근 로직을 자동 구현해 줌.

```java
/* 레포지토리 레이어 */
public interface UserRepository extends JpaRepository<User, Long> { 
    // Spring Data JPA가 인터페이스를 보고 자동으로 프록시 구현체를 생성함
}

/* 서비스 레이어 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository; // 자동 생성된 구현체 주입
    
    public void process(Long id) {    // [Spring Data JPA 내부 동작]
        userRepository.save(user);    // -> entityManager.persist(user)
        userRepository.findById(id);  // -> entityManager.find(User.class, id)
        userRepository.delete(user);  // -> entityManager.remove(user)
    }
}
```

#### 2. 메서드 이름 기반 쿼리 자동 생성
명명 규칙에 따라 메서드 이름을 작성하면 JPQL 쿼리를 자동으로 생성함. (`~` = `find`, `delete`, `count`, `exists`)

| 조건 | 일반화 메서드 패턴 | 메서드 예시 | 생성되는 쿼리 조건 (JPQL) |
| :--- | :--- | :--- | :--- |
| **전체** | `~All()` | `findAll()` / `deleteAll()` | 전체 대상 수행 |
| **일치** | `~ByUsername(name)` | `findByUsername("kim")` | `where u.username = :name` |
| **AND** | `~ByUsernameAndAge(name, age)` | `deleteByUsernameAndAge("kim", 20)` | `where u.username = :name and u.age = :age` |
| **OR** | `~ByUsernameOrEmail(name, email)` | `countByUsernameOrEmail("kim", "a@b.com")` | `where u.username = :name or u.email = :email` |
| **범위** | `~ByAgeGreaterThanEqual(age)` | `findByAgeGreaterThanEqual(20)` | `where u.age >= :age` |
| **포함** | `~ByUsernameContaining(keyword)` | `existsByUsernameContaining("admin")` | `where u.username like %:keyword%` |
| **정렬** | `~ByAgeOrderByUsernameDesc(age)` | `findByAgeOrderByUsernameDesc(20)` | `where u.age = :age order by u.username desc` |
| **개수 제한** | `~First3ByOrderByAgeDesc()` | `findFirst3ByOrderByAgeDesc()` | `order by u.age desc limit 3` |


#### 3. JPQL 및 네이티브 쿼리 지원
복잡한 쿼리가 필요한 경우 `@Query`를 활용해 JPQL 또는 네이티브 쿼리를 직접 작성함.

| 구분 | JPQL | 네이티브 쿼리 |
| :--- | :--- | :--- |
| **기준 대상** | 엔티티 객체 (`User u`) | DB 테이블 (`USERS U`) |
| **SQL 처리** | JPA가 DB 방언에 맞게 SQL 변환 | 작성한 SQL을 DB에 그대로 전달 |
| **DB 종속성** | 독립적 (DB 변경 시에도 쿼리 수정 없음) | 종속적 (특정 DB 전용 기능 사용 시) |
| **대소문자 구별** | 엔티티와 필드명 대소문자 구별 | DB 정책을 따름 |
| **주요 용도** | 객체 지향 조회 쿼리 | DB 특화 기능, 복잡한 통계/성능 최적화 |

```java
public interface UserRepository extends JpaRepository<User, Long> {
    // 1. JPQL: 엔티티 & 필드명 기준
    @Query("SELECT u FROM User u WHERE u.username = :name")
    List<User> findByName(@Param("name") String name);

    // 2. 네이티브 쿼리: DB 테이블 & 컬럼명 기준 (nativeQuery = true)
    @Query(value = "SELECT * FROM users WHERE user_name = :name", nativeQuery = true)
    List<User> findByNameNative(@Param("name") String name);
}
```


#### 4. 자동 Repository 생성
- **자동 스캔 (Component Scan):** `JpaRepository` 상속 인터페이스를 자동 감지해 동적 프록시 구현체를 생성한 뒤 스프링 빈(Bean)으로 등록
- **자동 구성 (Auto Configuration):** 별도 설정 없이 스프링 부트가 JPA 핵심 설정(`EntityManager` 등)을 자동구성하며, 스캔 범위 조정 시 `@EnableJpaRepositories` 사용 가능
- **개발자 편의:** 개발자는 인터페이스만 정의하면 구현체를 직접 작성할 필요가 없음

#### 5. 트랜잭션 관리 통합
- `@Transactional`을 Repository 메서드나 Service 레이어에 적용하여 트랜잭션 제어 가능
- **효과:** 개발자가 직접 커밋/롤백을 제어하지 않아도, 트랜잭션 안에서 DB 작업이 안전하게 수행됨



#### 6. 작동 흐름
```text
[개발자] 
   ▼
[Spring Data JPA]  ─(JPA 인터페이스 호출)─>  [JPA (EntityManager)]
                                                    ▼ (실제 구현체 실행)
                                                [Hibernate]
                                                    ▼
                                                [Database]
```

</br>

## 📌 DTO

#### DTO 개념
- **개념:** Data Transfer Object(데이터 전송 객체). 초기에는 네트워크 오버헤드를 줄이기 위해 여러 데이터를 하나로 묶어 전송하는 용도였으나, 오늘날은 계층(Layer) 간 데이터 전달용으로 활용함
- **예시:** 스프링 부트 앱에서 Controller와 Service 레이어 간 데이터 교환 시 주로 활용

#### Entity와 DTO의 차이
- **Entity:** DB와 상호작용하기 위한 객체로, `@Entity`를 통해 영속성 컨텍스트에서 관리되며 구조 변경 시 DB 스키마에 직접적인 영향을 미침
- **DTO:** 단순 데이터 전달을 위한 객체로 구조가 유연함
- **구분 이유:** 역할 분리를 통해 각 레이어의 책임을 명확히 하고 시스템 유연성을 향상시킴

#### 데이터 흐름
```text
클라이언트 ──(DTO)──> 컨트롤러 ──(DTO)──> 서비스 ──(Entity)──> 리포지토리 ──(Entity)──> DB
```

#### 도메인 주도 개발 (DDD)
- **DDD (Domain-Driven Design):** 소프트웨어의 복잡성을 해결하기 위해 '도메인(실제 비즈니스 영역)' 중심으로 코드를 설계하는 기법
- **DDD와 DTO:** 엔티티가 내부에 핵심 비즈니스 처리 로직을 가지며 비즈니스 핵심이 되므로, 엔티티를 외부에 직접 노출하지 않기 위해 표현 계층(API/화면)에서 DTO를 훨씬 적극적이고 엄격하게 분리하여 사용함


#### DTO 사용 장단점

- **장점**
  - **보안성 & 데이터 캡슐화:** DTO는 Entity의 모든 정보를 노출하지 않고 필요한 데이터만 선택적으로 노출할 수 있음
  - **레이어 간 결합도 감소:** DTO는 데이터 전송, Entity는 DB 통신 및 비즈니스 로직을 담당함. 역할이 분리되어 Entity의 비즈니스 로직 변경이 DTO 변경으로 이어지지 않음
- **단점**
  - **코드 복잡도 증가:** Entity와 DTO 간 변환 작업(Mapping)이 추가로 필요함
  - **성능 저하 가능성:** 변환 코드 실행 및 객체 생성 증가에 따라 미세한 성능 영향이 발생할 수 있음