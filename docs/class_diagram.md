# 스포츠센터 회원 관리 프로그램 - 클래스 다이어그램 (Class Diagram)

본 다이어그램은 MVC(Model-View-Controller) 아키텍처 패턴을 기반으로 한 전체 시스템의 주요 클래스 구조와 의존 관계를 나타냅니다.

## MVC 아키텍처 클래스 다이어그램

```mermaid
classDiagram
    %% Model (데이터 전달 및 접근)
    class MemberDTO {
        -int id
        -String name
        -String phone
        -String gender
        -Date birthDate
        -Date startDate
        -Date endDate
        -String notes
        +getId() int
        +setId(int)
        +getName() String
        +setName(String)
        +getPhone() String
        +setPhone(String)
    }

    class MemberDAO {
        +insertMember(MemberDTO member) boolean
        +getAllMembers() List~MemberDTO~
        +searchMembers(String keyword) List~MemberDTO~
        +updateMember(MemberDTO member) boolean
        +deleteMember(int id) boolean
    }

    %% Util (공통 유틸리티)
    class DBConnectionUtil {
        <<util>>
        +getConnection() Connection
        +close(AutoCloseable... resources) void
    }

    %% View (사용자 인터페이스)
    class MainFrame {
        -JTable memberTable
        -JTextField nameField
        -JTextField phoneField
        -JButton btnSave
        -JButton btnUpdate
        -JButton btnDelete
        -JButton btnReset
        +displayMembers(List~MemberDTO~ members)
        +clearForm()
        +showErrorMessage(String msg)
        +showConfirmMessage(String msg) boolean
    }

    %% Controller (중앙 제어 및 이벤트 처리)
    class MemberController {
        -MemberDAO dao
        -MainFrame view
        +MemberController(MainFrame view, MemberDAO dao)
        +initController()
        +loadAllMembers()
        +handleSaveMember()
        +handleUpdateMember()
        +handleDeleteMember()
        +handleSearchMember(String keyword)
    }

    %% 관계 (Relationships)
    MemberController --> MemberDAO : 데이터 처리 요청 (uses)
    MemberController --> MainFrame : 화면 갱신 지시 (controls)
    MainFrame --> MemberController : 사용자 이벤트 전달 (triggers)
    MemberDAO ..> MemberDTO : 데이터 반환 및 매개변수 사용 (depends)
    MainFrame ..> MemberDTO : 데이터 바인딩 (depends)
    MemberDAO --> DBConnectionUtil : DB 연결 획득 및 반환 (uses)
```

### 각 패키지 및 클래스 설명
* **`com.sportscenter.model` (Model 계층)**
  * `MemberDTO`: 회원 1명의 정보를 담는 데이터 전송 객체입니다. (로직 없음)
  * `MemberDAO`: MariaDB와 통신하여 실제 쿼리(`INSERT`, `SELECT`, `UPDATE`, `DELETE`)를 실행하는 데이터 접근 객체입니다.
* **`com.sportscenter.view` (View 계층)**
  * `MainFrame`: Java Swing 기반의 데스크톱 화면 UI입니다. 사용자 입력을 받고, 컨트롤러에 이벤트를 전달합니다.
* **`com.sportscenter.controller` (Controller 계층)**
  * `MemberController`: View에서 발생한 이벤트를 감지하여 DAO를 호출하고, 그 결과(DTO)를 다시 View로 넘겨주는 중재자 역할을 합니다. (`SwingWorker`를 통한 비동기 처리가 이곳에서 이루어질 수 있습니다.)
* **`com.sportscenter.util` (Utility 계층)**
  * `DBConnectionUtil`: DB 연결(Connection)을 가져오고 리소스를 안전하게 닫아주는 유틸리티 클래스입니다.
