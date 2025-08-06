# 새길 BE Repository
<!--
  프로젝트의 새길 리드미 타이틀입니다.
  FE: 프론트엔드 전용 README
  BE: 백엔드 전용 README
  적절히 맞춰서 기재해주세요
-->
<br />
<!-- 아키텍처 드로잉 혹은 웹뷰 캡쳐가 있다면 여기에 넣어주세요 -->
<!-- 예: figma 디자인 / ERD / 시스템 아키텍처 / 화면 캡처 등 -->
<p align="center">
  <img src="https://github.com/user-attachments/assets/6eebf717-0339-42d5-a1ab-2eef87af084e" alt="BE 아키텍처" width="47.5%" />
  <img src="https://github.com/user-attachments/assets/2810c0bf-2003-4ab4-9afb-0b893cfd1820" alt="BE ERD" width="47.5%" />
</p>


---

## 1. 소개
<!--
  프로젝트 개요를 서술합니다.
  예: 서비스 목적, 주요 기능 요약, 사용 기술 스택, 참여 인원 등
-->

>**새길**은 2025 새만금 공공데이터 공모전 신규데이터 생산 부문에 참가한 웹 개발 프로젝트 출품작입니다.\
>사용자 맞춤형 문화 콘텐츠를 추천하고, 결과를 지도에 시각화하는 설문조사 데이터 수집 플랫폼입니다.\
>**Java의 가상 스레드**로 단일 프로세스에서 데이터 수집과 소비를 병렬 처리하며, 전략 패턴과 팩토리 메소드 패턴으로 유연성과 확장성을 갖췄습니다.\
>전체 구조는 Spring Boot의 DI / 비동기 처리를 기반으로 한 경량화된 저비용 & 고성능 서버 아키텍처를 지향합니다.

<br />

- **🌐 배포 링크** : [설문조사 참여하기](https://saegil.vercel.app/)
- **🛠️ 사용 스택** : Java 21, Spring Boot, Spring Data JPA, Spring Caffeine Cache, JUnit5, JMH, MySQL, Docker, Github Actions, AWS, CLOVA Studio 임베딩v1
- **👥 참여 인원** : 총 4명 (BE 1, FE 2, UX/UI 1)
- **⏳ 개발 기간** : 2025.06 ~ 2025.07
- **📖 개발 일지** : [노션 참고](https://kimd0ngjun.notion.site/206420aa19408051bad5e9d1e05df172)

---

## 2. 수행 역할
<!--
  프로젝트에서 구현한 주요 기능을 나열합니다.
  기능 요약, URL (있다면), 설명 포함해도 좋습니다.
-->

### (1) 멀티 스레드 기반 데이터 수집 & 소비 서버 설계
- 단일 서버 프로세스 내에서 데이터 수집 파이프라인과 소비 처리를 멀티 스레드로 구현
- API 호출 및 웹 크롤링 기반의 데이터 수집기 할당에 팩토리 메소드 패턴 적용
- 유실율, 변환 속도를 고려한 임베딩 벡터 Float값 Byte 타입 변환 저장
- 데이터 수집 스레드 상태 모니터링 및 수집기별 결과 디스코드 알람봇 구축
- 수집 데이터 갱신 과정에서 데이터 조회 및 소비 안전을 위한 스왑 뷰 전략 도입
### (2) 기능별 단위 테스트 및 주요 성능 벤치마킹
- 서비스 기능별 단위 테스트코드 작성 100% 달성
- 배포 시, 외부 API 및 크롤링 사전 연결 점검 테스트 구축
- JMH 성능 계측으로 아키텍처 설계 근거 확보 및 처리 효율 3.5배 향상
### (3) AWS 배포 자동화 및 실제 운영하며 데이터 생산
- Dockerhub와 AWS EC2 인스턴스 연동 처리
- Github Actions 스크립트 기반 테스트 및 배포 자동화
- AWS IAM 권한 위임 스크립트를 통한 CI/CD 과정에서 EC2 IP 보안 확보

---

## 3. 트러블 슈팅
<!--
  개발 중 마주한 주요 이슈와 해결 과정을 기술합니다.
  문제 요약 → 원인 분석 → 해결 방안 순으로 작성하면 좋아요.
-->

### 🎯 예시: 트러블 슈팅 1

---

## 4. 서버 디렉토리 구조
<!--
  주요 폴더 구조를 간략하게 표현합니다.
  트리 구조 또는 코드 블럭 사용
-->

<details>
<summary>펼쳐보기📁</summary>

```bash
src
├── jmh # 자바 벤치마킹 하네스
│   └── java
│       └── org
│           └── jun
│               └── saemangeum
│                   └── benchmark
│                       └── process
│                           ├── ContentCollectServiceBenchmark.java # 데이터 수집 벤치마크
│                           └── MockContentCollectServiceBenchmark.java
├── main # 메인 애플리케이션
│   ├── java
│   │   └── org
│   │       └── jun
│   │           └── saemangeum
│   │               ├── SaemangeumApplication.java
│   │               ├── consume # 데이터 소비 3계층 아키텍처
│   │               │   ├── config
│   │               │   │   └── CoordinateConfig.java
│   │               │   ├── controller
│   │               │   │   └── SurveyController.java
│   │               │   ├── domain
│   │               │   │   ├── dto
│   │               │   │   │   ├── AverageRequest.java
│   │               │   │   │   ├── Coordinate.java
│   │               │   │   │   ├── KakaoResponse.java
│   │               │   │   │   ├── RecommendationResponse.java
│   │               │   │   │   ├── SurveyCreateRequest.java
│   │               │   │   │   └── SurveyUpdateRequest.java
│   │               │   │   ├── entity
│   │               │   │   │   ├── RecommendationLog.java
│   │               │   │   │   └── Survey.java
│   │               │   │   └── swap
│   │               │   │       ├── ContentView.java
│   │               │   │       └── VectorView.java
│   │               │   ├── repository
│   │               │   │   ├── entity
│   │               │   │   │   ├── RecommendationLogRepository.java
│   │               │   │   │   └── SurveyRepository.java
│   │               │   │   └── swap
│   │               │   │       ├── ContentViewRepository.java
│   │               │   │       └── VectorViewRepository.java
│   │               │   ├── service
│   │               │   │   ├── application
│   │               │   │   │   └── SurveyRecommendationService.java
│   │               │   │   ├── domain
│   │               │   │   │   ├── RecommendationLogService.java
│   │               │   │   │   └── SurveyService.java
│   │               │   │   ├── strategy
│   │               │   │   │   ├── EmbeddingVectorStrategy.java
│   │               │   │   │   ├── StrategyContextHolder.java
│   │               │   │   │   ├── TableEmbeddingVectorStrategy.java
│   │               │   │   │   └── ViewEmbeddingVectorStrategy.java
│   │               │   │   └── swap
│   │               │   │       └── SwapViewService.java
│   │               │   └── util
│   │               │       ├── CoordinateCalculator.java
│   │               │       └── ViewJdbcUtil.java
│   │               │
│   │               ├── global # 공통 도메인 및 설정정보
│   │               │   ├── cache
│   │               │   │   ├── CacheExpirePolicy.java
│   │               │   │   ├── CacheNames.java
│   │               │   │   └── CacheType.java
│   │               │   ├── config
│   │               │   │   ├── CacheConfig.java
│   │               │   │   ├── GlobalExceptionHandler.java
│   │               │   │   ├── Initializer.java
│   │               │   │   └── WebConfig.java
│   │               │   ├── controller
│   │               │   │   └── DeployController.java
│   │               │   ├── domain
│   │               │   │   ├── Category.java
│   │               │   │   ├── CollectSource.java
│   │               │   │   ├── Content.java
│   │               │   │   ├── Count.java
│   │               │   │   ├── IContent.java
│   │               │   │   └── Vector.java
│   │               │   ├── exception
│   │               │   │   ├── ClientIdException.java
│   │               │   │   ├── ErrorResponse.java
│   │               │   │   └── SatisfactionsException.java
│   │               │   ├── repository
│   │               │   │   ├── ContentRepository.java
│   │               │   │   ├── CountRepository.java
│   │               │   │   └── VectorRepository.java
│   │               │   └── service
│   │               │       ├── ContentService.java
│   │               │       ├── CountService.java
│   │               │       └── VectorService.java
│   │               │
│   │               └── pipeline # 데이터 수집 및 전처리 파이프라인
│   │                   ├── application
│   │                   │   ├── alarm
│   │                   │   │   ├── AlarmBuilder.java
│   │                   │   │   └── AlarmProcess.java
│   │                   │   ├── collect
│   │                   │   │   ├── api
│   │                   │   │   │   ├── GimjeCultureCollector.java
│   │                   │   │   │   ├── GunsanCultureCollector.java
│   │                   │   │   │   ├── SmgEventCollector.java
│   │                   │   │   │   └── SmgFestivalCollector.java
│   │                   │   │   ├── base
│   │                   │   │   │   ├── CheckedSupplier.java
│   │                   │   │   │   ├── CrawlingCollector.java
│   │                   │   │   │   ├── OpenApiCollector.java
│   │                   │   │   │   └── Refiner.java
│   │                   │   │   └── crawl
│   │                   │   │       ├── ArchipelagoCollector.java
│   │                   │   │       ├── BuanCultureCollector.java
│   │                   │   │       ├── City.java
│   │                   │   │       ├── CityTourCollector.java
│   │                   │   │       ├── GunsanFestivalCollector.java
│   │                   │   │       └── SeawallTourCollector.java
│   │                   │   ├── dto
│   │                   │   │   ├── ErrorResponse.java
│   │                   │   │   ├── Event.java
│   │                   │   │   ├── EventResponse.java
│   │                   │   │   ├── Festival.java
│   │                   │   │   ├── FestivalResponse.java
│   │                   │   │   ├── GimjeCulture.java
│   │                   │   │   ├── GimjeCultureResponse.java
│   │                   │   │   ├── GunsanCulture.java
│   │                   │   │   ├── GunsanCultureResponse.java
│   │                   │   │   └── RefinedDataDTO.java
│   │                   │   ├── schedule
│   │                   │   │   └── PipelineScheduler.java
│   │                   │   ├── service
│   │                   │   │   ├── ContentCollectService.java
│   │                   │   │   ├── DataCountUpdateService.java
│   │                   │   │   ├── EmbeddingVectorService.java
│   │                   │   │   └── PipelineService.java
│   │                   │   └── util
│   │                   │       ├── TitleDuplicateChecker.java
│   │                   │       └── VectorCalculator.java
│   │                   ├── domain
│   │                   │   ├── entity
│   │                   │   │   ├── Alarm.java
│   │                   │   │   ├── AlarmMessage.java
│   │                   │   │   ├── AlarmPayload.java
│   │                   │   │   └── AlarmType.java
│   │                   │   └── service
│   │                   │       └── AlarmService.java
│   │                   ├── infrastructure
│   │                   │   ├── api
│   │                   │   │   ├── OpenApiClient.java
│   │                   │   │   └── VectorClient.java
│   │                   │   ├── config
│   │                   │   │   ├── AsyncConfig.java
│   │                   │   │   ├── DiscordConfig.java
│   │                   │   │   ├── OpenApiConfig.java
│   │                   │   │   ├── SeleniumConfig.java
│   │                   │   │   └── VectorConfig.java
│   │                   │   ├── dto
│   │                   │   │   ├── EmbeddingJob.java
│   │                   │   │   ├── EmbeddingRequest.java
│   │                   │   │   └── EmbeddingResponse.java
│   │                   │   └── queue
│   │                   │       ├── EmbeddingJobQueue.java
│   │                   │       ├── EmbeddingWorker.java
│   │                   │       └── EmbeddingWorkerService.java
│   │                   └── presentation
│   │                       └── DiscordMessageAlarm.java
│   │
│   └── resources
│       └── application.yml # 서버 설정 프로퍼티
│
└── test # 단위 테스트 및 통합 테스트
    └── java
        └── org
            └── jun
                └── saemangeum
                    ├── SaemangeumApplicationTests.java
                    ├── collect
                    │   ├── CrawlingCollectorTest.java
                    │   ├── FallbackTest.java
                    │   ├── OpenApiCollectorTest.java
                    │   └── TitleDuplicateCheckerTest.java
                    ├── connect
                    │   ├── CoordinateTest.java
                    │   ├── EmbeddingTest.java
                    │   ├── MonitoringTest.java
                    │   ├── OpenApiTest.java
                    │   └── SeleniumTest.java
                    └── service
                        ├── ContentCollectServiceTest.java
                        ├── DataUpdateTest.java
                        ├── StrategyConcurrencyTest.java
                        ├── StrategyPatternTest.java
                        └── ViewFunctionTest.java
```

</details>

---

## 5. 기타
<!--
  추가적으로 기록할 정보 (예: 향후 계획, 라이센스, 협업 도구 등)
-->

- **Notion 문서** : [노션 링크](https://kimd0ngjun.notion.site/200420aa1940809faa85e562a0fb1fbf)
- **API 명세** : [노션 링크](https://kimd0ngjun.notion.site/API-21c420aa194080199f99cdf97a0a39a1)


<p align="right">(<a href="#readme-top">🔝 back to top</a>)</p>

[Next.js]: https://img.shields.io/badge/next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white
[Next-url]: https://nextjs.org/
