# 기능 구현을 위한 상세 설계 및 구현 방안 분석 보고서 (Research)

본 문서는 `functional_specification.md`와 실제 구축된 `members`, `lessons`, `enrollments` 테이블 스키마를 바탕으로, 시스템의 MVC 아키텍처 구조를 정립하고 세부 구현 전략을 심층 분석한 문서입니다. 이 분석을 통해 UI(View), 제어 로직(Controller), 데이터 접근 로직(DAO)을 구체화합니다.

## 1. 데이터 모델 (Model) 레이어 구현 방안

세 개의 핵심 엔티티에 맞춰 DTO(Data Transfer Object)와 DAO(Data Access Object)를 각각 분리하여 단일 책임 원칙(SRP)을 준수합니다.

### 1.1. DTO 설계 및 데이터 매핑
*   **`MemberDTO`:** DB의 `members` 테이블 구조와 1:1 매핑됩니다. 
    *   보유 필드: `id`, `name`, `phone`, `gender`, `birthDate`, `createdAt`, `notes`
*   **`LessonDTO`:** `lessons` 테이블과 1:1 매핑됩니다.
    *   보유 필드: `id`, `name`, `dayOfWeek`, `time`, `instructorName`, `capacity`, `price`
*   **`EnrollmentDTO`:** `enrollments` 테이블과 매핑됩니다. 
    *   보유 필드: `id`, `memberId`, `lessonId`, `startDate`, `endDate`, `paymentDate`
    *   **※ 복합 매핑의 필요성:** `functional_specification.md` 2.1항의 회원 상세 페이지 명세에 따르면, 회원의 수강 내역에 단순한 `lessonId`(숫자)가 아닌 실제 "강좌명"을 보여주어야 합니다. 이를 위해 순수 DB 컬럼 외에 `String lessonName` 이라는 확장 필드를 DTO 내부에 추가하거나 별도의 `EnrollmentViewDTO`를 구성하여 `JOIN` 쿼리 결과를 매핑할 수 있도록 설계해야 합니다.

### 1.2. DAO 구현 전략 및 제약 조건 준수
*   **공통 사항:** `DBConnectionUtil`을 통해 Connection을 확보하며, 리소스 누수(Memory Leak) 방지를 위해 반드시 `try-with-resources` 블록 안에서 `PreparedStatement`와 `ResultSet`을 닫아야 합니다.
*   **`MemberDAO`:** 
    *   기능: 회원 등록(`INSERT`), 전체 및 조건 검색(`SELECT ... LIKE`), 정보 수정(`UPDATE`), 회원 삭제(`DELETE`).
    *   회원 삭제의 경우 DB 테이블 생성 시 `ON DELETE CASCADE`를 설정해 두었으므로, DAO 내부에서 `enrollments` 테이블까지 직접 수동으로 삭제할 필요가 없어 코드가 간결해집니다.
*   **`LessonDAO`:**
    *   기능: 강좌 등록, 조회, 수정, 삭제 수행.
    *   **예외 처리 로직 (명세서 2.3항 반영):** 강좌 삭제 전, DB의 `ON DELETE RESTRICT` 속성으로 인해 발생할 수 있는 `SQLIntegrityConstraintViolationException` (또는 해당 SQL 상태 코드의 `SQLException`)을 예외 처리(`catch`)하여 뷰 계층으로 던져 사용자에게 "수강 중인 회원이 있어 해당 강좌를 삭제할 수 없습니다" 라는 팝업 메시지를 명확히 전달하는 로직이 핵심입니다.
*   **`EnrollmentDAO`:**
    *   기능: 수강 등록(`INSERT`), 특정 회원의 수강 이력 조회(`SELECT ... JOIN`).
    *   특정 회원(`member_id`)의 수강 이력을 불러올 때 `SELECT e.*, l.name AS lesson_name FROM enrollments e JOIN lessons l ON e.lesson_id = l.id` 와 같은 조인 쿼리를 작성하여 `EnrollmentDTO`에 값을 바인딩해야 합니다.

## 2. 화면 (View) 레이어 구현 방안

`Java Swing`을 이용하여 화면을 구성하되, 역할에 따라 패널과 다이얼로그를 명확히 분리합니다.

*   **`MainFrame` (메인 윈도우):** 애플리케이션의 뼈대입니다. 최상단 네비게이션(회원 관리, 강좌 관리 탭 등)을 보유하고, 중앙 작업 공간에 전체 회원 목록(`JTable`)을 띄워줍니다. (명세서 3항 UI 레이아웃 안 반영)
*   **`MemberDetailDialog` (회원 상세 조회 창):** 특정 회원을 더블클릭할 시 열리는 팝업으로 상단에는 회원의 기본 정보 및 [정보 수정], [수강 등록] 버튼을, 하단에는 해당 회원의 역대 수강 이력 그리드를 배치합니다.
*   **`MemberFormDialog` (입력/수정 공통 폼):** 생성자 매개변수로 `MemberDTO`가 들어오면 기존 데이터를 채운 "수정 모드", `null`이 들어오면 빈칸인 "신규 등록 모드"로 똑똑하게 분기 처리하여 코드 중복을 최소화합니다.
*   **`LessonManageDialog` (센터 강좌 관리 창):** [센터 관리] 버튼을 눌렀을 때 진입하며, 전체 강좌 목록과 우측(또는 하단)에 강좌를 새로 입력/수정할 수 있는 텍스트 필드를 일체형으로 제공합니다.
*   **`EnrollmentFormDialog` (수강 등록 창):** 강좌 목록을 보여주는 `JComboBox`를 배치하고, 등록 시점에 날짜를 선택할 수 있게 하여 DB의 `enrollments`로 전송할 폼을 구성합니다.

## 3. 제어 로직 (Controller) 구현 방안

뷰(View)의 이벤트 리스너가 가벼워지도록, 복잡한 비즈니스 처리는 전적으로 Controller 계층에 위임합니다.

*   **리스너 분리:** 각 다이얼로그 안에는 리스너를 직접 구현하지 않고, Controller가 View 객체를 주입받아 이벤트(`btnSave.addActionListener(...)`)를 달아주는 구조를 취합니다.
*   **비동기 스레드 도입 (SwingWorker):** 기능 명세서 2.5항에 따라 메인 이벤트 디스패치 스레드(EDT) 멈춤 현상을 막기 위해, 검색이나 DB 등록 처리 시 `SwingWorker` 내부 `doInBackground()`에서 DAO를 호출합니다. 작업 완료 시점인 `done()` 안에서 View의 `JTable` 모델을 갱신하거나 `JOptionPane` 성공 메시지를 표출합니다.

## 4. 향후 구현 로드맵 (Action Plan)
1. **모델 클래스 확충:** `LessonDTO`, `EnrollmentDTO` 및 관련 비어있는 DAO 클래스 파일들을 추가로 생성합니다.
2. **`MemberDAO` 선행 구현:** 가장 기본이 되는 회원 CRUD 로직과 `SwingWorker` 테스트용 코드를 먼저 짭니다.
3. **UI 뼈대 (Mockup) 생성:** 버튼과 빈 JTable만 존재하는 형태의 View 클래스들을 우선 생성하여 화면 연동 흐름을 체크합니다.
4. **결합 (Controller 주입):** DAO 로직과 UI 화면 사이에 Controller를 붙여 실제 쿼리 결과가 화면에 동적으로 표시되는지 완성합니다.
