# 인터페이스 기반 MVC 아키텍처 및 세부 구현 계획안 (Plan)

본 문서는 새롭게 정의된 기능 명세서(수강 취소 기능 포함)와 **인터페이스(Interface) 기반의 의존성 역전(DIP)** 원칙이 적용된 클래스 다이어그램을 바탕으로, 실제 Java 코드를 어떻게 구현해 나갈 것인지에 대한 구체적인 행동 계획을 서술합니다.

## 1. 아키텍처 리팩토링 계획 (Interface 분리)

과거 직접 구현체(DAO)를 호출하던 방식에서 벗어나, 향후 테스트 코드 작성 및 DB 마이그레이션을 용이하게 하기 위해 DAO 계층을 인터페이스와 구현체로 분리합니다.

*   **인터페이스 정의:** `src/main/java/com/taeyoonkim/model/` 패키지 하위에 `IMemberDAO`, `ILessonDAO`, `IEnrollmentDAO` 3개의 인터페이스를 생성합니다.
*   **구현체 명칭 변경:** 기존의 `MemberDAO.java`와 같은 파일명 및 클래스명을 `MemberDAOImpl.java` 등으로 변경(Rename)하여 명확한 책임을 부여합니다.
*   **Controller 의존성 주입 (DI):** 
    ```java
    // 팩토리나 외부에서 주입받아 느슨한 결합 달성
    public class EnrollmentController {
        private IEnrollmentDAO enrollmentDAO;
        
        public EnrollmentController(IEnrollmentDAO dao) {
            this.enrollmentDAO = dao;
        }
    }
    ```

## 2. 수강 취소 기능 상세 구현 계획

기능 명세서의 주석(`TODO`)을 통해 새롭게 확정된 "수강 취소" 기능을 구현하기 위해 View, Controller, DAO 계층을 다음과 같이 설계합니다.

### 2.1. UI (View - `MemberDetailDialog`)
*   기존 회원 상세 페이지의 하단에 수강 이력 테이블(`JTable`)을 스크롤 패널(`JScrollPane`)로 감싸 배치합니다.
*   테이블 하단(또는 우측)에 **[수강 취소]** 버튼(`btnCancelEnrollment`)을 추가합니다.
*   사용자가 테이블의 특정 행(Row)을 클릭하고 취소 버튼을 누르면 이벤트를 발생시키되, View 계층에서는 DB 로직을 섞지 않고 `ActionListener`를 통해 이벤트를 Controller로 즉시 넘깁니다.

### 2.2. 제어 (Controller - `EnrollmentController`)
*   `MemberDetailDialog`에서 [수강 취소] 이벤트가 넘어오면, `JOptionPane.showConfirmDialog()`를 띄워 "해당 수강 내역을 취소하시겠습니까?"라고 묻습니다.
*   사용자가 '예'를 누르면, 선택된 행의 `enrollment_id`를 추출합니다.
*   추출한 `id`를 가지고 `IEnrollmentDAO.deleteEnrollment(id)`를 호출합니다.
*   수행이 성공(true 반환)하면, `loadEnrollments(memberId)`를 다시 호출하여 UI의 테이블 데이터를 최신화(새로고침)합니다.

### 2.3. 데이터 접근 (Model - `EnrollmentDAOImpl`)
*   `deleteEnrollment(int id)` 메서드는 `DBConnectionUtil`을 통해 Connection을 열고, 
    `DELETE FROM enrollments WHERE id = ?` 쿼리를 실행합니다.
*   반드시 `try-with-resources` 블록을 활용해 `PreparedStatement` 자원 누수를 차단합니다.

## 3. 구현 프로세스 단계 (Action Item)

1.  **[Step 1] 리팩토링 진행:** `com.taeyoonkim.model` 패키지의 기존 DAO 클래스를 모두 `...Impl`로 변경하고, 인터페이스 세트를 새롭게 생성합니다.
2.  **[Step 2] DTO 완성:** 기능 명세의 컬럼에 맞게 `MemberDTO`, `LessonDTO`, `EnrollmentDTO`의 변수, `Getter/Setter`를 모두 채워넣습니다.
3.  **[Step 3] DAO 내부 쿼리 작성:** 분리된 `Impl` 클래스 내부에 실질적인 CRUD 쿼리문 및 예외 처리(`SQLException` 핸들링)를 작성합니다.
4.  **[Step 4] UI 뼈대 및 연결:** View 패키지에서 팝업창(MemberDetailDialog 등)을 생성하여 취소 버튼을 부착하고, Controller 패키지를 만들어 View와 DAO를 중계하는 이벤트를 등록합니다.

해당 계획이 검토/승인되면, 바로 1번 단계인 인터페이스 리팩토링 및 DTO 완성을 시작으로 코드를 작성해 나가겠습니다.
