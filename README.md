# 포인트 적립 시스템 API 개발 내용입니다.

> 아래 `API 제공`하면서 계속 리팩토링 중입니다.  
> 현재 [`이펙티브 자바`](https://github.com/t0e8r1r4y/EffectiveJava), [`SQLP`](https://github.com/kmw8551/study/tree/main/oraclearch), [`대규모 시스템 설계`](https://github.com/t0e8r1r4y/SystemDesign) 스터디를 진행하며 계속 부족한 부분을 찾아내고 있습니다.   
> 파악되는 이슈에 대해서 github나 블로그에 글을 한번 작성해 보겠습니다.  

<br/>


## 사용 기술 스택
- Java11, QueryDsl, JUnit5, SpringBoot 2.6.13, Spring Rest Docs, h2, MySQL 8.0.31
- 기타 : Postman, DBeaver, Docker, SonarLint


<br/>

## 실행 방법
- 해당 프로젝트를 cloning 후 실행하면 Default 옵션으로 h2로 실행합니다.
- Dev 환경에서 MySQL Replication 구성까지 포함하여 사용하였는데, docker-compose.yml 파일은 추가하지 않았습니다. 관련 설정 구성은 [블로그](https://terrys-tech-log.tistory.com/11)에 기록하였습니다.

#### with h2 실행

- 프로젝트를 다운 받은 경로에서 아래 명령어를 실행합니다.
```Shell
./gradlew build && java -jar -Dspring.profiles.active=local ./build/libs/PointReserve-0.0.2-SNAPSHOT.jar

```


- 별도의 front page는 없습니다. 아래 화면처럼 h2 db가 뜨면 정상힙니다.

<img width="512" alt="스크린샷 2022-11-26 오후 5 26 24" src="https://user-images.githubusercontent.com/91730236/204082058-1123c088-d236-4927-8cb3-a8711ebdde21.png">


- 아래 3개 테이블이 생성되어야 합니다.

<img width="165" alt="스크린샷 2022-11-26 오후 5 26 37" src="https://user-images.githubusercontent.com/91730236/204082081-c55a8b50-8afd-4ceb-9c0c-66f7b8244757.png">


- API 문서에 따라 PostMan으로 테스트하면 됩니다. 프로젝트 실행 후 `http://localhost:8000/docs/index.html` 에서 API 확인.

<img width="512" alt="스크린샷 2022-11-26 오후 5 27 12" src="https://user-images.githubusercontent.com/91730236/204082089-f18e52ce-8461-492b-be63-5b84ccb22670.png">


<br/>

## 주요 기능 & 구현 의도

#### 1️⃣ ERD 설계

<img width="512" alt="스크린샷 2022-11-26 오후 5 05 19" src="https://user-images.githubusercontent.com/91730236/204082038-4469e965-182f-4b79-b134-884e61935686.png">

#### 2️⃣ 단위 테스트

<img width="512" alt="스크린샷 2022-11-26 오후 6 27 07" src="https://user-images.githubusercontent.com/91730236/204082097-8a60951c-bbd3-43dd-ae33-96261ca48ef4.png">


#### 3️⃣ [설계에 대한 생각](https://github.com/t0e8r1r4y/PointReserve/wiki/3.-%EC%96%B4%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98-%EC%84%A4%EA%B3%84)

![어플리케이션 설계 drawio](https://user-images.githubusercontent.com/91730236/201449839-4b844275-a5e8-4573-85b8-af6d6f9590d2.png)

- 개발 요구 사항은 [wiki](https://github.com/t0e8r1r4y/PointReserve/wiki)에 정리하였습니다.
- 처리 로직을 두가지로 나누어 생각했습니다.
    - 포인트 적립/사용 이벤트 처리 ( 사용자의 전체 포인트 집계 )
    - 포인트 상세 처리 ( FIFO, 유효기간 관리 등 상세 처리 )
- `필요성` : 돈과 관련된 로직은 history-replay 기반의 설계가 필요하다고 생각하였습니다.
- 어플리케이션 내에서 위 도식과 같이 Event Driven 구조를 적용하였습니다. 
- @EventListener 어노테이션과 @Scheulder 어노테이션을 사용하여 구현하였습니다. RabbitMQ나 Kafka 인스턴스도 고려하였지만 별도의 클러스터 구성을 고려하지 않고자 이렇게 구현하였습니다.
- Bucket4j를 사용하여 처리율 제한장치를 추가하여 보았습니다.


#### 4️⃣ 지속 가능한 개발에 대한 생각
- 레이어간/객체간 메시지 전달에 `dto를 활용`하고 구분된 네이밍을 적용하였습니다.
- spring `validator`를 활용하여 dto 검증하도록 적용하였습니다.
- `dirty check 방지` 및 `DB 이중화`를 통한 성능 향상을 위해 @Transactional(readOnly = true)를 적용하였습니다.
- API 문서 생성 자동화를 위해 Spring Rest Docs를 사용하였습니다.
- 표준 예외처리를 구현하여 처리하였습니다.


<br/>


## 다음 프로젝트에 개선을 고려해볼 점

#### 1️⃣ Table ID 생성 전략
- 표준 예외처리를 구현함에 있어서 ID 생성전략에서 UUID 사용이 필요하다는 생각이 많이 들었습니다

#### 2️⃣ 표준 예외 처리
- 예외 내용에 timestamp와 transaction ID를 추가하는것이 원인 분석에 더 도움이 된다고 생각하였습니다.
- 또한 예외 메시지를 file 형태로 저장, 불러와서 처리하면 커스텀 클래스의 갯수를 줄이고 효율적인 구현이 가능하다고 판단했습니다.

#### 3️⃣ 처리율 제한 장치에 대한 고려 ([상세](https://github.com/t0e8r1r4y/PointReserve/wiki/5.-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%B2%98%EB%A6%AC%EB%A5%BC-%EC%9C%84%ED%95%9C-%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98-%EA%B3%A0%EB%A0%A4%EC%82%AC%ED%95%AD))
- point 적립 사용과 관련된 부분은 결제 서비스를 만든다고 했을 때 DB와 가장 근접한 뒷단의 서비스라고 생각합니다.
- 그런 서비스에 처리율 제한 장치를 구현한 부분은 적절한 포인트는 아니었다고 생각합니다.
- 단일 프로젝트가 아니였다면 API Gateway에 구현이 되어야 될 부분이라고 생각합니다

#### 4️⃣ 멀티모듈 구조 적용
