# 라면연구소 🍜

<p align="center"><img src="https://github.com/RamyunLab/ramyunlab-be/assets/57437315/23fd4324-3ed0-4445-b18d-56280a553c33"></p>

- 배포 URL: http://43.203.209.183/

- 테스트 ID: test11

- 테스트 PW: test123!

<br />

## 📄목차

[1. 프로젝트 소개](#프로젝트-소개)

[2. 개발 기간](#개발-기간)

[3. 팀 소개](#팀-소개)

[4. 기술 스택](#%EF%B8%8F기술-스택)

[5. 기능](#기능)

<br />


## 💡프로젝트 소개

사용자의 취향에 맞는 라면을 검색할 수 있는 라면 추천 사이트입니다.

마음에 드는 라면을 찜하고 별점과 리뷰를 남길 수 있습니다.

<br />


## 🕑개발 기간
총 기간: 2024.05.31 ~ 2024.06.21

기획 및 설계: 2024.05.31 ~ 2024.06.03

개발: 2024.06.04 ~ 2024.06.20

<br />


## 🪪팀 소개

| 진현정(조장) | 신동원 | 이대원 | 전재민 | 추수연 |
|:---:|:---:|:---:|:---:|:---:|
| 백엔드 | 프론트엔드 | 백엔드 | 프론트엔드 | 백엔드 |
| 라면 필터링<br /> 상세 페이지 조회<br /> 게임 랜덤 데이터 조회<br /> 찜 기능 <br /> 배포 | 이상형 월드컵<br />라면 상세페이지 | 소셜로그인<br />회원정보 수정<br />마이페이지 | 로그인<br /> MBTI<br /> Up&Down<br /> 메인페이지<br /> 내 메뉴 | 인증,인가<br /> 리뷰<br /> 신고하기<br />메일<br />관리자페이지 |
| [Github](https://github.com/HJ17J) | [Github](https://github.com/eastorigin) | [Github](https://github.com/1ee-dw) | [Github](https://github.com/jaeminjeon123) | [Github](https://github.com/CHUSUEYEON) |

<br />


## ⚙️기술 스택

**Front-End**

<img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black">![SASS](https://img.shields.io/badge/SASS-hotpink.svg?style=for-the-badge&logo=SASS&logoColor=white)![TypeScript](https://img.shields.io/badge/typescript-%23007ACC.svg?style=for-the-badge&logo=typescript&logoColor=white)![Redux](https://img.shields.io/badge/redux-%23593d88.svg?style=for-the-badge&logo=redux&logoColor=white)![React Query](https://img.shields.io/badge/-React%20Query-FF4154?style=for-the-badge&logo=react%20query&logoColor=white)

**Back-End**

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"><img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white)

**Communication**

<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)<img src="https://img.shields.io/badge/slack-7952B3?style=for-the-badge&logo=slack&logoColor=white"><img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white">

**Deployment**

![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)

<br />


## 📌기능

### ◾[메인페이지] 라면 검색, 찜하기

![필터링222](https://github.com/RamyunLab/ramyunlab-be/assets/57437315/9ff70453-b99b-4910-9226-770fedaaf785)

- 라면 이름과 7가지 기준으로 라면을 상세하게 필터링 가능하며, 이름, 평점 높은 순, 리뷰 많은 순으로 정렬할 수 있음
- 여러가지 조건을 유연하게 검색 가능하도록 `JPA Querydsl`로 필터링 기능 구현
<br />

### ◾[로그인, 회원가입]

![로그인_-카카오_-회원가입-2배속](https://github.com/RamyunLab/ramyunlab-be/assets/57437315/1066a96b-cb17-4a63-a8fb-94f1be45dc4f)

- `bcrypt`를 이용한 비밀번호 암호화 / 로그인 시 `JWT` 토큰으로 인증 / 카카오 로그인 가능
- `Spring Security` 라이브러리를 이용해 사용자 인증 및 서비스 인가를 쉽게 관리
<br />

### ◾[마이페이지] 회원 정보 수정 및 탈퇴, 건의하기, 찜 목록, 최근 본 라면, 내가 쓴 리뷰, 내가 추천한 리뷰

![건의하기_-마이페이지-2배속](https://github.com/RamyunLab/ramyunlab-be/assets/57437315/1c4ba9e0-735e-4446-9cfe-dbeacb93e125)

- 스프링에서 지원하는 `Java Mail Sender`를 이용해 관리자에게 메일로 건의사항 전달
- 사용자가 조회한 라면을 세션에 기록한 다음 사용자의 ID를 기반으로 최근 본 라면 목록을 가져와 최근 본 라면 목록 구현
- 나의 찜 목록, 내가 작성한 리뷰 및 추천한 리뷰 목록을 조회
<br />

### ◾[상세페이지] 찜하기, 리뷰 등록

![라면-상세-페이지222](https://github.com/RamyunLab/ramyunlab-be/assets/57437315/04a26378-6a78-4180-aa34-602507ee8577)

- 마음에 드는 라면을 찜 목록에 추가하고, 찜한 라면은 사용자의 찜 목록에서 확인 가능
- 해당 라면에 대한 별점과 리뷰를 등록할 수 있으며, 사진 업로드 시엔 `URL.createObjectURL` 을 통해 사진 미리보기 기능을 구현
- 추천이 10개 이상인 경우 베스트 리뷰로 선정 / 신고가 5회 이상 누적될 경우 블라인드 처리됨
<br />

### ◾[게임] 라면 월드컵, 스코빌 Up & Down, 라면 MBTI

![미니게임-2배속2](https://github.com/RamyunLab/ramyunlab-be/assets/57437315/1982ce80-270e-4549-9361-4995fdbc5c2a)

- **[라면 월드컵]**
  -  더 좋아하는 라면을 고르는 월드컵 게임으로, 게임 종료 시 결과 화면을 카카오톡으로 공유 가능함
  -  라운드 선택이 가능하며 라운드에 맞게 랜덤으로 라면 데이터를 가져오며 `Redux`로 게임 상태를 관리함
- **[스코빌 Up & Down]**
  - 스코빌이 더 높은 라면을 선택하는 게임으로, 라운드가 끝날 때마다 라면의 스코빌 정보를 알려줌
  - `Redux-persist`를 사용하여 게임 라운드 상태를 관리하며, `React-confetti`를 사용하여 우승 페이지에 이펙트 구현
- **[라면 MBTI]**
  - `Redux-toolkit`을 사용하여 세션에서 구현
  - 질문의 답에 따라 MBTI 값을 다르게 받고, slice에 결과에 대한 값을 넣어 관리하며 해당 라면 MBTI별로 다른 색의 이펙트 적용
<br />

### ◾[관리자페이지]

![관리자페이지-222](https://github.com/RamyunLab/ramyunlab-be/assets/57437315/3ca543ff-d7c5-4fe7-82ed-cd7f8935563a)

- 상품 검색, 조회, 추가, 수정, 삭제 가능
- 회원 조회 및 삭제, 회원이 쓴 리뷰 조회 및 삭제 가능
- 회원이 쓴 리뷰가 신고 누적으로 블라인드 처리되었을 경우 관리자 페이지에서 신고 처리로 표시되며, 관리자가 신고 초기화 가능
- 회원, 상품, 리뷰의 경우 논리삭제로 구현하여 삭제된 내용은 회색배경으로 표시됨

  
