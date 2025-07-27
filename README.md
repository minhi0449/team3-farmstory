# Team3 _Farmstory  
> **개발 기간 : 2024. 09. 30 ~ 2024. 10. 04 (총 5일)**  

농산물 전자상거래(쇼핑몰)와 커뮤니티 기능을 통합한 **Spring Boot 3 + JPA** 기반 웹 플랫폼입니다.  
상품 거래(판매·구매)와 재배 노하우·Q&A 등 커뮤니티를 한 곳에서 제공하여,  
농업 종사자와 소비자가 쉽고 안전하게 소통·거래할 수 있도록 설계했습니다.

---

## ✨ 프로젝트 하이라이트
| 구분 | 요약 |
|-----|------|
| **핵심 기능** | 상품 등록·관리, 장바구니, 주문·결제, 회원(소셜 로그인 포함) & 관리자 페이지, 커뮤니티 게시판 |
| **아키텍처** | **도메인 계층 분리 + 클린 아키텍처** 적용 → 유지보수성·테스트 용이성 향상 |
| **보안** | Spring Security + BCrypt 암호화, OAuth 2.0(Google·Naver) 소셜 로그인 |
| **운영** | AWS EC2·RDS·S3 + Docker + GitHub Actions **CI/CD 자동 배포** |
| **관측성** | 패키지별 로그 레벨 관리(farmstory.log), Hibernate SQL 포맷팅·트레이싱 |
| **확장성** | JPA `ddl-auto:update` + Redis 캐시 구조 → 추후 트래픽 확대 대비 |

---
## ✦ 사용 기술 (Tech Stack)
| 분야 | 스택 |
|----|------|
| **Backend** | ![Java 17](https://img.shields.io/badge/Java%2017-007396?style=flat&logo=java&logoColor=white) ![Spring Boot 3](https://img.shields.io/badge/Spring%20Boot%203-6DB33F?style=flat&logo=spring-boot&logoColor=white) ![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat&logo=spring&logoColor=white) ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-59666C?style=flat) |
| **Frontend** | ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=flat&logo=thymeleaf&logoColor=white) ![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=html5&logoColor=white) ![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=flat&logo=css3&logoColor=white) ![JavaScript ES6](https://img.shields.io/badge/JavaScript%20ES6-F7DF1E?style=flat&logo=javascript&logoColor=black) |
| **Database** | ![MySQL 8](https://img.shields.io/badge/MySQL%208-4479A1?style=flat&logo=mysql&logoColor=white) ![Redis 5](https://img.shields.io/badge/Redis%205-DC382D?style=flat&logo=redis&logoColor=white) |
| **DevOps** | ![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white) ![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=flat&logo=github-actions&logoColor=white) ![AWS EC2 | RDS | S3](https://img.shields.io/badge/AWS%20(EC2%20%7C%20RDS%20%7C%20S3)-F7931E?style=flat&logo=amazon-aws&logoColor=white) |


| 분야        | 기술 스택 |
|------------|-------------------------------------------------------------------------------------|
| **DevOps** | ![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white) ![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=flat&logo=github-actions&logoColor=white) ![AWS EC2 \| RDS \| S3](https://img.shields.io/badge/AWS%20(EC2%20%7C%20RDS%20%7C%20S3)-F7931E?style=flat&logo=amazon-aws&logoColor=white) |
---

## ✦ 주요 기능 (Core Features)

### 1) 상품 관리 (Product Management)
- **CRUD** : 상품 등록·수정·삭제  
- 카테고리 및 키워드 기반 **검색/필터링**  
- 재고·판매 상태(판매중/품절) 실시간 표시  

### 2) 장바구니 & 주문·결제
- 장바구니 담기/수량 조정/삭제  
- 주문서 작성 → **PG 연동**(토스·카카오페이 등 확장 가능)  
- 주문 상태(결제 대기 → 결제 완료 → 배송) 트래킹  

### 3) 회원 & 관리자 시스템
- 이메일 회원가입, **BCrypt** 비밀번호 암호화  
- OAuth 2.0 **Google·Naver** 소셜 로그인  
- 역할(Role) 기반 권한: **USER / ADMIN**  
- 관리자 전용 대시보드(상품·게시글·회원 통계)  

### 4) 커뮤니티 게시판
- 공지·재배 노하우·Q&A 등 **카테고리별 게시판**  
- 파일(이미지) 업로드, 댓글·대댓글, 좋아요, 신고 기능  
- 페이지네이션, 검색(제목/내용/작성자) 지원  

### 5) 공통 부가 기능
- **멀티파트 파일 업로드**: 10 MB 제한, 이미지 리사이징  
- **로그 관리**: `farmstory.log` 분리, Hibernate SQL Trace  
- **DevTools LiveReload**: 실시간 반영으로 개발 생산성 향상  
