# 3주차

## 프록시와 연관 관계 관리

### 프록시 특징

* 처음 사용할 때 한번만 초기화
* 원본 엔티티를 상속 받아서 만들어짐
* 타입 체크시 instance of 사용해야 함
* getReference : 데이터 베이스 조회를 안한 가짜 엔티티 객체 조회
* 영속성 컨텍스트에 찾는 엔티티가 존재하면 em.getReference()를 호출해도 실제 엔티티 반환
* 프록시 객체는 실제 객체의 참조를 보관하며 호출하면 실제 엔티티 반환하여 영속성 컨텍스트에 반영
* 준영속 상태일 때, 프록시를 초기화하면 문제 발생(org.hibernate.LazyInitializationException)

### 프록시 확인

* 프록시 인스턴스의 초기화 여부 확인
  * PersistenceUnitUtil.isLoaded
* 프록시 클래스 확인
  * entity.getClass().getName()
* 프록시 강제 초기화
  * org.hibernate.Hibernate.initialize(entity);

### 로딩
* 지연 로딩
  * 지연 로딩(LAZY)을 이용한 프록시 조회
  * 실제 사용 시점에 초기화
* 즉시 로딩
  * 즉시 로딩(EAGER)을 사용해서 실제 데이터를 함께 조회 
  * 프록시 X
* 가급적 지연 로딩만 사용
* 즉시 로딩으로 인한 지연이 발생할 수 있다
* JPQL에서 N+1 개 문제를 발생
* @ManyToOne, @OneToOne은 기본이 즉시 로딩(LAZY 옵션 설정)
* @OneToMany, @ManyToMany는 기본이 지연 로딩
* fetch 조인이나 엔티티 그래프 기능을 사용

### 영속성 전이(CASCADE)

* 연관된 엔티티도 함께 영속 상태로 만들때 사용
* CascadeType
  * **ALL** : 모두 적용
  * **PERSIST** : 영속
  * **REMOVE** : 삭제
  * MERGE : 병합
  * DETACH : 준영속
  * REFRESH : REFRESH

### 고아 객체

* 부모 엔티티와 연관 관계가 끊어진 자식 엔티티를 자동 삭제
* orphanRemoval = true(CascadeType.REMOVE랑 비슷하게 동작)
* 참조하는 곳이 하나일 때 사용
* 특정 엔티티가 개인 소유 할 경우에만 사용

### 영속성 전이와 고아 객체를 같이 사용

* 부모 엔티티를 통해서 자식의 생명 주기를 관리
* Aggregate Root개념을 구현


## 값 타입

### 기본값 타입

* 자바 기본 타입, 래퍼 클래스, String
* 생명 주기를 엔티티의 의존
* 값 타입은 공유하면 안됨

### 임베디드 타입(복합 값 타입)
* 재사용
* 높은 응집도
* 메소드를 만들 수 있음
* 엔티티에 생명주기를 의존
* @Embeddable, @Embedded
* 사용 전과 후에 매핑하는 테이블은 같다
* 한 엔티티에 같은 값 사용(@AttributeOverrides, @AttributeOverride를 사용해서 컬러 명 속성을 재정의)

### 값 타입과 불변 객체

* 임베디드 타입 같은 값 타입을 여러 엔티티에서 공유하면 위험
* 인스턴스를 복사해서 사용하면 공유 참조로 인한 부작용을 피할 수 있음
* 객체 타입은 공유 참조는 피할 수 없다.
* 불변 객체
  * 값 타입은 불변 객체로 설계
  * 생성 시점 이후 절대 값을 변경할 수 없는 객체
  * 생성자로만 값을 설정하고 수정자(Setter)를 만들지 않음
* 불변 객체 변경 시 신규로 생성해서 변경
* @org.hibernate.annotations.Immutable
  * Entity나 immutable로 지정하면 수정이 작동하지 않게 된다.
  * Collection을 immutable로 지정하면 추가나 삭제가 작동하지 않게 된다.
  * @Immutable에 update를 날리면 오류 없이 그냥 무시
  * 주의! : @Immutable 엔티티는 여전히 JPQL이나 Criteria로 업데이트 가능하다. 단, 5.2.17부터는 업데이트를 막았으나 WARNING만 남김

### 값 타입의 비교
* 값 타입은 equals를 사용해서 동등성 비교
* equals() 재정의해서 사용

### 값 타입 컬렉션
* 값 타입을 하나 이상 저장
* @ElementCollection, @CollectionTable 사용
* 컬렉션을 저장하기 위한 별도의 테이블이 필요
* 값 타입 조회 시 지연 로딩 전략 사용
* 값 타입 컬렉션에 변경 사항이 발생하면, 주인 엔티티와 연관된 모든 데이터를 삭제하고, 값 타입 컬렉션에 있는 현재 값을 모두 다시 저장
* 값은 변경하면 추적이 어렵다
* 값 타입 컬렉션 대신에 일대다 관계를 고려(이거때문에 개고생해서 ASM은 수정했던 적이 있음)
* 

