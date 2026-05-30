# 스포츠센터 회원 관리 프로그램 - 기술 연구 및 구현 보고서 (Research & Implementation Plan)

본 문서는 `functional_specification.md`를 기반으로, 요구사항을 실제 Java 코드로 구현하기 위한 기술적 세부사항과 아키텍처, 데이터 흐름을 정의한 상세 보고서입니다. 향후 모든 코드 작성은 본 보고서의 기준을 철저히 따릅니다.

## 1. 아키텍처 및 기술 스택

- **언어:** Java 25
- **UI 프레임워크:** Swing
- **데이터베이스:** MariaDB (Docker 환경)
- **데이터 접근 API:** JDBC
- **설계 패턴:** 엄격한 MVC (Model-View-Controller) 패턴, DAO (Data Access Object), DTO (Data Transfer Object) 패턴

## 2. 핵심 구현 원칙 (User Rules 적용)

1. **MVC 분리:** View 계층(`JFrame`, `JPanel`, `JDialog` 등)에는 절대 DB 통신 로직이 들어가지 않아야 합니다. DB 접근은 오직 DAO 클래스에서만 수행하며, Controller가 이 둘을 중재합니다.
2. **자원 관리:** `Connection`, `PreparedStatement`, `ResultSet` 등 DB 관련 리소스는 반드시 `try-with-resources` 구문을 사용하여 메모리 누수와 커넥션 고갈을 방지합니다.
3. **네이밍 컨벤션:** 
   - 변수 및 메서드: `camelCase`
   - 클래스: `PascalCase`
   - DB 테이블 및 컬럼: 복수형 및 `snake_case`

## 3. 세부 기능별 구현 전략

### 3.1. 화면 네비게이션 및 전환 (CardLayout)
- 화면 최상단의 **[뒤로가기]**, **[회원 관리(초기화면)]**, **[회원 등록]**, **[센터 관리]** 버튼 네비게이션 구성을 위해 메인 프레임을 `CardLayout`으로 구성합니다.
- `CardLayout`은 한 패널 내에 여러 화면 패널을 겹쳐 두고 필요할 때 전환(`CardLayout.show()`)할 수 있도록 지원하므로, '메인 목록 화면' ↔ '회원 상세 정보 페이지' ↔ '센터 관리 페이지' 간의 원활한 이동을 팝업 없이 부드럽게 구현할 수 있습니다.
- 반면, 회원 등록, 정보 수정, 강좌 등록/수정 등 데이터를 직접 입력받는 작업은 모달 다이얼로그(`JDialog`)로 띄워 사용자의 작업 흐름(Focus)이 분산되지 않게 돕습니다.

### 3.2. 회원 관리 구현 로직 (Member)
- **Entity & DTO:** `id`, `name`, `phone`, `gender`, `birthDate`, `createdAt`, `notes`
- **조회 성능:** 검색 기능은 DAO에서 `SELECT * FROM members WHERE name LIKE ? OR phone LIKE ?` 형태로 구현합니다.
- **UI 렌더링 (만료 회원 강조):** 
  - `JTable`의 렌더러(`TableCellRenderer`)를 커스텀하여 적용합니다.
  - 조건식 로직: 회원 정보와 수강 내역을 조인하여 확인. 만료일이 7일 이내이거나 지났으면서 `익월 수강 내역이 존재하지 않는 경우`에만 렌더러가 배경색/글자색을 붉은색으로 변경하도록 Controller에서 로직을 처리합니다.

### 3.3. 수강 관리 구현 로직 (Enrollment)
- **비즈니스 로직 연산 (기간 자동 설정):** 
  - 회원 상세 페이지 내에서 수강 등록 버튼 클릭 시, Controller는 해당 회원의 최근 수강 내역을 조회합니다.
  - `java.time.LocalDate` API를 사용하여 날짜 로직을 분기합니다.
    - 당월 미등록: `LocalDate.now()` ~ `당월의 마지막 날`
    - 당월 등록 중: `익월 1일` ~ `익월의 마지막 날`
  - 계산된 날짜를 DTO에 담아 View(수강 등록 팝업창)에 초기값으로 전달하여 폼을 미리 채워줍니다.
- **DTO (`EnrollmentDTO`):** `id`, `memberId`, `lessonId`, `startDate`, `endDate`, `paymentDate`

### 3.4. 강좌 관리 구현 로직 (Lesson)
- **Entity & DTO:** `id`, `name`, `dayOfWeek`, `time`, `instructorName`, `capacity`, `price`
- **안전 삭제 로직 (유효성 검사):**
  - 강좌 DAO에서 삭제/수정 메서드를 호출할 때, `SELECT COUNT(*) FROM enrollments WHERE lesson_id = ?` 쿼리를 먼저 실행하여 수강 중인 회원이 있는지 확인합니다.
  - 결과가 1 이상이면 Controller에 실패 코드를 반환하고, View에서 `JOptionPane.showMessageDialog`를 통해 수강생 존재에 대한 경고를 띄워 수정을 차단합니다.
- **저장 위치:** 기능 명세서에 명시된 바와 같이 이 모든 강좌 정보는 DB의 `lessons` 테이블에서 무결성 있게 관리됩니다.

### 3.5. 비동기 UI 처리 (EDT 보호)
- DB I/O 작업은 `javax.swing.SwingWorker`를 상속받은 별도의 백그라운드 작업 클래스에서 `doInBackground()`로 처리합니다.
- 쿼리 완료 후 `done()` 메서드 내부에서 View의 JTable이나 컴포넌트를 갱신하여 메인 스레드 멈춤 현상을 근본적으로 차단합니다.

## 4. 진행 순서 가이드라인 (Plan)
1. **DB 설계 및 구축:** Docker MariaDB 컨테이너 실행 및 `members`, `lessons`, `enrollments` 테이블 DDL(Data Definition Language) 스키마 작성 후 적용.
2. **Model 계층 구현:** 각 도메인별 DTO 및 순수 JDBC 기반 DAO 인터페이스/클래스 구현. (try-with-resources 필수).
3. **Controller/View 뼈대 구성:** JFrame 메인 창에 CardLayout 적용, 상단 네비게이션 바 구축, JDialog 레이아웃 세팅.
4. **기능 통합 및 테스트:** 비동기 작업 로직(SwingWorker) 적용 및 각 기능별 CRUD 결합 테스트 진행.

본 문서는 프로젝트의 핵심 뼈대 역할을 하며, 향후 프로세스는 이 문서의 지침을 절대적으로 따릅니다.
