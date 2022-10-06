# 6주차

## 쿼리 메소드 기능

* 메소드 이름으로 쿼리 생성 
  * 조회: find...By ,read...By ,query...By get...By,
    * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/ #repositories.query-methods.query-creation 
    * 예:) findHelloBy 처럼 ...에 식별하기 위한 내용(설명)이 들어가도 된다. 
  * COUNT: count...By 반환타입 long 
  * EXISTS: exists...By 반환타입 boolean 
  * 삭제: delete...By, remove...By 반환타입 long 
  * DISTINCT: findDistinct, findMemberDistinctBy 
  * LIMIT: findFirst3, findFirst, findTop, findTop3
* NamedQuery 
  * 실무에서 잘 사용하지 않음
  * 어플리케이션 로딩 시점에 오류 확인 가능
* @Query - 리파지토리 메소드에 쿼리 정의 파라미터 바인딩
  * @org.springframework.data.jpa.repository.Query 어노테이션을 사용 
  * 실행할 메서드에 정적 쿼리를 직접 작성하므로 이름 없는 Named 쿼리라 할 수 있음 
  * JPA Named 쿼리처럼 애플리케이션 실행 시점에 문법 오류를 발견할 수 있음
    > 참고: 실무에서는 메소드 이름으로 쿼리 생성 기능은 파라미터가 증가하면 메서드 이름이 매우 지저분해진다. 따라서 @Query 기능을 자주 사용하게 된다.
  * DTO로 직접 조회
* 파라미터 바인딩
  * 위치 기반
    *  select m from Member m where m.username = ?0
  * 이름 기반
    * select m from Member m where m.username = :name
  > 참고: 코드 가독성과 유지보수를 위해 이름 기반 파라미터 바인딩을 사용 (위치기반은 순서 실수가 바꾸면...)
  * Collection 타입으로 in절 지원
* 반환 타입 
  * 컬렉션, 단건, 단건 Optional
  * 조회 결과가 많거나 없으면
    * 컬렉션 - 결과 없음: 빈 컬렉션 반환
    * 단건 조회
      * 결과 없음: null 반환
      * 결과가 2건 이상: javax.persistence.NonUniqueResultException 예외 발생
* 페이징과 정렬 
  * 스프링 데이터 JPA 페이징과 정렬
    * 페이징과 정렬 파라미터
      * org.springframework.data.domain.Sort : 정렬 기능
      * org.springframework.data.domain.Pageable : 페이징 기능 (내부에 Sort 포함)
    * 특별한 반환 타입
      * org.springframework.data.domain.Page : 추가 count 쿼리 결과를 포함하는 페이징
      * org.springframework.data.domain.Slice : 추가 count 쿼리 없이 다음 페이지만 확인 가능 (내부적으로 limit + 1조회)
      * List (자바 컬렉션): 추가 count 쿼리 없이 결과만 반환
    * count 쿼리 분리 가능
      * 대용량의 데이터 카운트 시 문제 가능성이 존재함
    * DTO 변환 가능
* 벌크성 수정 쿼리 
  * 벌크성 수정, 삭제 쿼리는 @Modifying 어노테이션을 사용
    * 사용하지 않으면 다음 예외 발생
    * org.hibernate.hql.internal.QueryExecutionRequestException: Not supported for DML operations
  * 벌크성 쿼리를 실행하고 나서 영속성 컨텍스트 초기화
    * em.clear();
    * @Modifying(clearAutomatically = true)
* @EntityGraph
  * 페치 조인(FETCH JOIN)의 간편 버전
  * LEFT OUTER JOIN 사용
  * fetch join을 사용하는걸 권장
* JPA Hint & Lock
  * JPA Hint
    * JPA 쿼리 힌트(SQL 힌트가 아니라 JPA 구현체에게 제공하는 힌트)
  * Lock
    * org.springframework.data.jpa.repository.Lock 어노테이션을 사용
    * 트랜잭션과 락에 대해 좀더 깊은 공부가 필요함