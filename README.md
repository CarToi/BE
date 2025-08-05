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
  <img src="https://github.com/user-attachments/assets/6eebf717-0339-42d5-a1ab-2eef87af084e" alt="BE 아키텍처" width="85%" />
</p>
<p align="center"><em>새길 백엔드 아키텍처</em></p>

<br />

## 1. 소개
<!--
  프로젝트 개요를 서술합니다.
  예: 서비스 목적, 주요 기능 요약, 사용 기술 스택, 참여 인원 등
-->

>**새길**은 사용자 맞춤형 문화 콘텐츠를 추천하고, 이를 지도에 시각화하여 설문조사 데이터를 수집·활용하는 플랫폼입니다.\
>**Java의 가상 스레드(Virtual Thread)** 를 활용해서 단일 프로세스 내에서 데이터 수집과 소비를 병렬 처리하며, 전략 패턴과 팩토리 메소드 패턴을 적용해 유연성과 확장성을 갖췄습니다.\
>전체 구조는 Spring Boot의 DI / 비동기 처리를 기반으로 한 경량화된 고성능 서버 아키텍처를 지향합니다.

- **🌐 배포 링크** : [설문조사 참여하러 가기](https://saegil.vercel.app/)
- **🛠️ 사용 스택** : [![Next][Next.js]][Next-url]
- **👥 참여 인원** : 총 4명 (BE 1, FE 2, UX/UI 1)
- **⏳ 개발 기간** : 2025.06 ~ 2025.07

자바
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white">

스프링
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">

스프링부트
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">

스프링 시큐리티
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">

JUnit5
<img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=JUnit5&logoColor=white">

Hibernate
<img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white">

MySQL
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white">

nginx
<img src="https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white">

docker
<img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white"> 

GitHub Actions
<img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white">

EC2
<img src="https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=Amazon%20EC2&logoColor=white">

S3
<img src="https://img.shields.io/badge/Amazon%20S3-569A31?style=for-the-badge&logo=Amazon%20S3&logoColor=white">

Selenium
<img src="https://img.shields.io/badge/Selenium-43B02A?style=for-the-badge&logo=Selenium&logoColor=white">


## 2. 핵심 기능
<!--
  프로젝트에서 구현한 주요 기능을 나열합니다.
  기능 요약, URL (있다면), 설명 포함해도 좋습니다.
-->


## 3. 트러블 슈팅
<!--
  개발 중 마주한 주요 이슈와 해결 과정을 기술합니다.
  문제 요약 → 원인 분석 → 해결 방안 순으로 작성하면 좋아요.
-->

### 🎯 예시: 트러블 슈팅 1


## 4. 디렉토리 구조
<!--
  주요 폴더 구조를 간략하게 표현합니다.
  트리 구조 또는 코드 블럭 사용
-->

<details>
<summary>📁 구조</summary>

```
src/
├──

# tree 명령어를 쓰면 편해요
```

</details>


## 5. 설치 및 실행 방법
<!--
  로컬에서 프로젝트 실행을 위한 안내사항 작성
  build 도구, 실행 커맨드, 환경 변수 등
-->

```bash
```


## 6. 기타
<!--
  추가적으로 기록할 정보 (예: 향후 계획, 라이센스, 협업 도구 등)
-->

- Notion 문서: [노션 링크](https://kimd0ngjun.notion.site/200420aa1940809faa85e562a0fb1fbf)
- API 명세: [노션 링크](https://kimd0ngjun.notion.site/API-21c420aa194080199f99cdf97a0a39a1)


<p align="right">(<a href="#readme-top">🔝 back to top</a>)</p>

[Next.js]: https://img.shields.io/badge/next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white
[Next-url]: https://nextjs.org/
