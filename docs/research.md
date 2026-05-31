# 데이터베이스 스키마 및 테이블 요구사항 분석 보고서

본 문서는 `functional_specification.md`를 바탕으로 도출된 데이터베이스의 테이블 및 컬럼 구조를 분석한 문서입니다.

## 1. 테이블 구성 요약
기능 명세에 따르면 시스템은 크게 3가지 주요 객체(회원, 강좌, 수강 내역)를 다루고 있으며, 이를 각각의 독립된 테이블로 분리하여 관계형 데이터베이스(RDB) 모델을 구성해야 합니다.
1.  **`members` (회원 테이블):** 회원의 기본 인적 사항을 저장합니다.
2.  **`lessons` (강좌 테이블):** 스포츠센터에서 운영하는 강좌 정보를 저장합니다.
3.  **`enrollments` (수강 내역 테이블):** 회원과 강좌 사이의 다대다(N:M) 관계를 해소하고 각 회원의 수강 이력을 추적하는 매핑 테이블입니다.

## 2. 세부 컬럼 및 제약 조건 분석

### 2.1. `members` (회원 테이블)
*   `id` (INT, PK, AUTO_INCREMENT): 회원 고유 번호
*   `name` (VARCHAR(50), NOT NULL): 회원의 이름 (필수 입력값)
*   `phone` (VARCHAR(20), NOT NULL): 회원 연락처 (필수 입력값)
*   `gender` (VARCHAR(10)): 성별 (선택 사항)
*   `birth_date` (DATE): 생년월일
*   `created_at` (TIMESTAMP): 가입일. 회원 등록 시점에 서버 시간을 기준으로 자동 설정되도록 `DEFAULT CURRENT_TIMESTAMP` 속성을 적용합니다.
*   `notes` (TEXT): 기타 특이사항 (내용이 길어질 수 있으므로 VARCHAR 대신 TEXT 사용)

### 2.2. `lessons` (강좌 테이블)
*   `id` (INT, PK, AUTO_INCREMENT): 강좌 고유 번호
*   `name` (VARCHAR(100), NOT NULL): 강좌 이름
*   `day_of_week` (VARCHAR(20)): 강좌 운영 요일 정보
*   `time` (VARCHAR(50)): 강좌 진행 시간 정보
*   `instructor_name` (VARCHAR(50)): 강사 이름
*   `capacity` (INT, DEFAULT 0): 강좌 수강 정원
*   `price` (INT, DEFAULT 0): 수강료 (결제 정보 연동을 위한 가격)

### 2.3. `enrollments` (수강 내역 테이블)
*   `id` (INT, PK, AUTO_INCREMENT): 수강 내역 고유 번호
*   `member_id` (INT, FK): 수강을 등록한 회원 참조 (`members` 테이블의 `id` 컬럼과 매핑). 회원이 시스템에서 완전 삭제될 경우 수강 내역만 덩그러니 남는 것을 방지하기 위해 `ON DELETE CASCADE` 설정을 적용하는 것이 바람직합니다.
*   `lesson_id` (INT, FK): 수강할 강좌 참조 (`lessons` 테이블의 `id` 컬럼과 매핑). 
    *   **(중요 제약사항):** 명세서 2.3항에 따르면 *"강좌 수정 및 삭제 시 해당 강좌를 수강 중인 회원이 한 명이라도 존재할 경우, 경고 메시지를 표시하고 수정/삭제를 막아야 함"* 이 명시되어 있습니다. 이를 데이터베이스 단에서 확실히 보장하기 위해, 외래 키 옵션에 `ON DELETE RESTRICT`를 부여하여 무결성을 보호해야 합니다.
*   `start_date` (DATE): 수강 시작 일자
*   `end_date` (DATE): 수강 종료(만료) 일자
*   `payment_date` (DATE): 비용을 지불한 결제 일자

## 3. 추가 설정 사항 (인코딩)
MariaDB 환경에서 한글 깨짐 이슈를 방지하고 이모지 등의 확장 문자를 안전하게 저장하기 위해 데이터베이스 및 테이블 수준에서 `UTF-8`의 확장형인 `utf8mb4` 캐릭터셋과 `utf8mb4_unicode_ci` 콜레이션(Collation)을 필수적으로 지정해야 합니다.
