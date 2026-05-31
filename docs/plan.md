# 초기 스키마 (init.sql) 작성 쿼리 기획안

본 문서는 `research.md`의 분석 결과를 토대로 실제 MariaDB에 적용할 `init.sql` 쿼리문의 구조와 의도를 기획한 문서입니다.

## 1. 데이터베이스 생성
```sql
CREATE DATABASE IF NOT EXISTS sportscenter_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sportscenter_db;
```
*   **의도:** 이모지 및 다국어 지원을 완벽히 소화하는 `utf8mb4`를 기본값으로 지정하여 문자열 깨짐을 원천 차단합니다.

## 2. 테이블 생성 순서
참조 무결성(`Foreign Key`) 설정 때문에 테이블의 생성 순서가 중요합니다. 참조 대상이 되는 독립적인 테이블(`members`, `lessons`)을 먼저 생성하고, 이를 참조하는 의존적인 테이블(`enrollments`)을 마지막에 생성해야만 `FOREIGN KEY` 설정 시 참조 오류가 발생하지 않습니다.

## 3. 테이블별 쿼리 기획

### 3.1. `members` (회원 테이블)
*   명세서에 지정된 `id`, `name`, `phone`, `gender`, `birth_date`, `created_at`, `notes`를 정의합니다.
*   `created_at`은 `TIMESTAMP DEFAULT CURRENT_TIMESTAMP`를 적용하여 데이터가 삽입될 때 DB 서버 시간을 기준으로 자동 기록되게 합니다.

### 3.2. `lessons` (강좌 테이블)
*   명세서에 지정된 `id`, `name`, `day_of_week`, `time`, `instructor_name`, `capacity`, `price`를 정의합니다.
*   `capacity`와 `price`는 기본값으로 0(`DEFAULT 0`)을 설정합니다.

### 3.3. `enrollments` (수강 내역 테이블)
*   `member_id`와 `lesson_id`를 외래 키(FK)로 설정합니다.
*   **제약 조건 기획:**
    *   **강좌 삭제 방지:** `FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE RESTRICT`
        *   **의도:** 특정 강좌가 `enrollments` 테이블에 하나라도 매핑되어 있다면(누군가 수강 중이라면) `lessons` 테이블에서 해당 강좌를 `DELETE` 하려 할 때 DB 엔진 차원에서 에러를 발생시켜 삭제를 원천 차단합니다. (명세서 2.3 요구사항의 DB 레벨 구현)
    *   **회원 삭제 연동:** `FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE`
        *   **의도:** 회원이 탈퇴/삭제될 경우 해당 회원의 모든 수강 내역을 함께 지워 데이터 찌꺼기(고아 레코드)가 남지 않도록 자동 삭제 처리합니다.

## 4. 실행 계획
위의 기획을 토대로 `src/main/resources/init.sql` 파일의 기존 내용을 완전히 지우고, 세 가지 테이블 쿼리를 포함한 새로운 스크립트로 덮어씌웁니다.
