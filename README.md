# 포인트 적립 시스템 API 개발 내용입니다.

> 아래 `API 제공`하면서 계속 리팩토링 중입니다.  
> 현재 [`이펙티브 자바`](https://github.com/t0e8r1r4y/EffectiveJava), [`SQLP`](https://github.com/kmw8551/study/tree/main/oraclearch), [`대규모 시스템 설계`](https://github.com/t0e8r1r4y/SystemDesign) 스터디를 진행하며 계속 부족한 부분을 찾아내고 있습니다.   
> 파악되는 이슈에 대해서 github나 블로그에 글을 한번 작성해 보겠습니다.  

<br/>


## 사용 기술 스택
- Java11, QueryDsl, JUnit5, SpringBoot 2.6.13, Spring Rest Docs, h2, MySQL 8.0.31
- 기타 : Postman, DBeaver


<br/>

## 실행 방법
- 해당 프로젝트를 cloning 후 실행하면 Default 옵션으로 h2로 실행합니다.
- Dev 환경에서 MySQL Replication 구성까지 포함하여 사용하였는데, docker-compose.yml 파일은 추가하지 않았습니다. 관련 설정 구성은 [블로그](https://terrys-tech-log.tistory.com/11)에 기록하였습니다.

#### 실행


<img width="409" alt="스크린샷 2022-11-26 오후 5 26 24" src="https://user-images.githubusercontent.com/91730236/204082058-1123c088-d236-4927-8cb3-a8711ebdde21.png">


<img width="165" alt="스크린샷 2022-11-26 오후 5 26 37" src="https://user-images.githubusercontent.com/91730236/204082081-c55a8b50-8afd-4ceb-9c0c-66f7b8244757.png">


<img width="900" alt="스크린샷 2022-11-26 오후 5 27 12" src="https://user-images.githubusercontent.com/91730236/204082089-f18e52ce-8461-492b-be63-5b84ccb22670.png">


<br/>

## 주요 기능 & 구현 의도

#### ERD 설계

<img width="534" alt="스크린샷 2022-11-26 오후 5 05 19" src="https://user-images.githubusercontent.com/91730236/204082038-4469e965-182f-4b79-b134-884e61935686.png">

#### 단위 테스트

<img width="924" alt="스크린샷 2022-11-26 오후 6 27 07" src="https://user-images.githubusercontent.com/91730236/204082097-8a60951c-bbd3-43dd-ae33-96261ca48ef4.png">


#### 설계에 대한 생각


#### 지속 가능한 개발에 대한 생각


<br/>


## 다음 프로젝트에 개선을 고려해볼 점

#### 에러 처리

#### API에 대한 생각

