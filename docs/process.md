# 작업 진행 계획 (Process)

## 현재 완료된 작업
- 기존 루트 경로(`c:\dev\USW_JAVA\project1`)의 불필요한 `src`, `target` 디렉토리 및 `pom.xml` 삭제 완료.

## 예정된 작업 내용 및 행동 (실행 전 승인 대기 중)

1. **[M1 보완] 신규 `pom.xml` 의존성 구성**
   - 새 프로젝트 폴더(`sports-center-management`) 내의 `pom.xml`에 MariaDB JDBC 라이브러리 의존성을 추가하고 Java 25 컴파일러 설정을 반영합니다.
2. **[M2 단계] DB 연동 모듈(Util, DTO, DAO) 코드 작성**
   새로운 폴더 경로 기반으로 다음 자바 소스 파일들을 작성합니다.
   - `sports-center-management/src/main/resources/init.sql` (테이블 초기화 스크립트)
   - `sports-center-management/src/main/java/com/sportscenter/util/DBConnectionUtil.java` (DB 커넥션)
   - `sports-center-management/src/main/java/com/sportscenter/model/LessonDTO.java`
   - `sports-center-management/src/main/java/com/sportscenter/model/MemberDTO.java`
   - `sports-center-management/src/main/java/com/sportscenter/model/LessonDAO.java`
   - `sports-center-management/src/main/java/com/sportscenter/model/MemberDAO.java`

> **확인 요청:** 본격적으로 새로운 워크스페이스(`sports-center-management`) 내부 환경을 설정하고 DB 관련 소스 코드를 생성하는 M2 단계를 진행하고자 합니다. 승인해 주시면 파일 생성을 시작하겠습니다.
