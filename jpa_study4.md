# 4주차

## 객체지향 쿼리 언어

### Criteria

* 문자가 아닌 자바 코드로 JPQL을 작성
* JPA 공식 기능
* 너무 복작하고 실용성이 없다
* QueryDSL 사용 권장

### Jooq
* JOOQ DSL를 만들어 주어 DSL 클레스를 사용하여 쿼리를 작성
* 유로와 무료 버전(mysql, maria DB)
* DSL 컨텍스트를 기반으로 SQL을 생성하여 사용
* DSL 클레스를 생성하여 쿼리를 생성함.
* DB 스키마를 기반으로 JAVA 도메인을 생성함.
* JOOQ & QueryDSL 차이점
  * 성능 차이는 비슷
  * QueryDSL는 java 도메인을 생성해가며 스키마를 생성
  * jooq는 만들어진 스키마를 바탕으로 java 도메인으로 만들어 사용
  * [jOOQ vs. Hibernate: When to Choose Which](https://blog.jooq.org/jooq-vs-hibernate-when-to-choose-which/)
  * [QueryDSL vs jOOQ](https://blog.jooq.org/querydsl-vs-jooq-feature-completeness-vs-now-more-than-ever/)

### QueryDSL

* 문자가 아닌 자바코드로 JPQL을 작성할 수 있음
* JPQL 빌더 역할
* 컴파일 시점에 문법 오류를 찾을 수 있음
* 동적쿼리 작성 편리함
* 단순하고 쉬움

### JPQL

* 엔티티 객체를 대상으로 검색
* SQL과 문법 유사, SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN 지원
* 특정 데이터베이스 SQL에 의존하지 않음
* 엔티티와 속성은 대소문자 구분
* 엔티티 이름 사용, 테이블 이름이 아님
* 별칭은 필수

###### TypeQuery, Query
* TypeQuery : 반환 타입이 명확할 때 사용
* Query : 반환 타입이 명확하지 않을 때
###### 결과 조회
* query.getResultList(): 결과가 하나 이상일 때, 리스트 반환
* query.getSingleResult(): 결과가 정확히 하나, 단일 객체 반환
######  파라미터 바인딩
* 이름 기준
```
SELECT m FROM Member m where m.username=:username 
query.setParameter("username", usernameParam);
```
* 위치 기준
```
SELECT m FROM Member m where m.username=?1 
query.setParameter(1, usernameParam);
```
* <span style="color:red">**이름 기준 사용을 권장**</span>

###### 프로젝션
* 여러 값 조회
  * Query 타입으로 조회
  * Object[] 타입으로 조회
  * new 명령어로 조회
    * 단순 값을 DTO로 바로 조회
    ``` 
    SELECT new jpabook.jpql.UserDTO(m.username, m.age) FROM Member m; 
    ```
###### 페이징

* setFirstResult(int startPosition) : 조회 시작 위치
* setMaxResults(int maxResult) : 조회 할 데이터 

###### 조인
* 내부 조인, 외부 조인, 세타 조인
* ON절을 활용한 조인(JPA2.1부터 지원)
* 연관 관계가 없는 엔티티 외부 조인(하이버네이트 5.1부터)

###### 서브 쿼리
* JPA는 WHERE, HAVING 절에서만 서브 쿼리 사용 가능
* SELECT 절도 가능(하이버네이트에서 지원)
* FROM 절의 서브 쿼리는 현재 JPQL에서 불가능
  * 조인으로 풀 수 있으면 풀어서 해결

###### CASE, 기본 함수, 사용자 정의 함수 사용 가능
###### 경로 표현식
* 상태 필드(state field): 단순히 값을 저장하기 위한 필드
* 연관 필드(association field): 연관관계를 위한 필드
* 특징
  * 상태 필드(state field): 경로 탐색의 끝, 탐색X
  * 단일 값 연관 경로: 묵시적 내부 조인(inner join) 발생, 탐색O
  ```
  /* JPQL */
  select o.member from Order o
  /* SQL */
  select m.* 
  from Orders o
  inner join Member m on o.member_id = m.id
  ```  
  * 컬렉션 값 연관 경로: 묵시적 내부 조인 발생, 탐색X
    * FROM 절에서 명시적 조인을 통해 별칭을 얻으면 별칭을 통 해 탐색 가능
  * 
* 가급적 묵시적 조인 대신에 명시적 조인 사용 

###### 패치 조인
* SQL 조인 종류 아님
* JPQL에서 성능 최적화를 위해 제공하는 기능
* 연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 기능
```
  /* JPQL */
  select m from Member m join fetch m.team
  /* SQL */
  select m.*, t.* 
  from Member m
  inner join Team t on m.team_id = t.id
  ```  
* JPQL의 DISTINCT 2가지 기능 제공
  * SQL에 DISTINCT를 추가
  * 애플리케이션에서 엔티티 중복 제거
* 일반 조인 실행시 연관된 엔티티를 함께 조회하지 않음
* 페치 조인을 사용할 때만 연관된 엔티티도 함께 조회(즉시 로딩)
* 페치 조인은 객체 그래프를 SQL 한번에 조회하는 개념
* 패치 조인 특징
  * 페치 조인 대상에는 별칭을 줄 수 없다
  * 둘 이상의 컬렉션은 페치 조인 할 수 없다.
  * 컬렉션을 페치 조인하면 페이징 API(setFirstResult, setMaxResults)를 사용할 수 없다.
    * 일대일,다대일같은 단일 값 연관 필드들은 페치 조인해도 페이징 가능
    * <span style="color:red">**하이버네이트는 경고 로그를 남기고 메모리에서 페이징(매우 위험)**</span>
  * 연관된 엔티티들을 SQL 한 번으로 조회 - 성능 최적화
  * 글로벌 로딩 전략보다 우선함

###### Named 쿼리
* 미리 정의해서 이름을 부여해두고 사용하는 JPQL
* 정적쿼리
* 어노테이션, XML에 정의
* 애플리케이션 로딩 시점에 초기화 후 재사용
* 애플리케이션 로딩 시점에 쿼리를 검증

####### 벌크 연산
* 쿼리 한 번으로 여러 테이블 로우 변경
* executeUpdate()의 결과는 영향받은 엔티티 수 반환
* UPDATE, DELETE 지원
* INSERT(insert into .. select, 하이버네이트 지원)
* 벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리
  * 벌크 연산을 먼저 실행
  * 벌크 연산 수행 후 영속성 컨텍스트 초기화


