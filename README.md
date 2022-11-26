# 포인트 적립 시스템 API 개발 내용입니다.

> 아래 `API 제공`하면서 계속 리팩토링 중입니다.  
> 현재 [`이펙티브 자바`](https://github.com/t0e8r1r4y/EffectiveJava), [`SQLP`](https://github.com/kmw8551/study/tree/main/oraclearch), [`대규모 시스템 설계`](https://github.com/t0e8r1r4y/SystemDesign) 스터디를 진행하며 계속 부족한 부분을 찾아내고 있습니다.   
> 파악되는 이슈에 대해서 github나 블로그에 글을 한번 작성해 보겠습니다.  

<br/>


## 사용 기술 스택
- java11, QueryDsl, Junit5, SpringBoot, Spring RestDoc, h2, mysql

## 설계내용
- 개발내용을 [WIKI](https://github.com/t0e8r1r4y/PointReserve/wiki)에 정리하였습니다.
- 아래 목차로 내용을 정리하였습니다.
    - 요구사항
    - 도메인 설계
    - 비즈니스 로직 설계
    - 어플리케이션 아키텍처 설계
    - 테스트와 API 문서 자동화
    - 동시성 처리를 위한 트랜잭션 고려사항
    - 처리율 제한 관련 구현
    - 추가적인 리팩터링을 고려한 보완사항
