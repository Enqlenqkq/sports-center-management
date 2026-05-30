# 스포츠센터 회원 관리 프로그램 - 기술 연동 및 아키텍처 상세 분석 (Research)

본 문서는 Java Swing 기반의 UI와 Docker 컨테이너에서 동작하는 MariaDB(localhost:3306)를 연동하기 위한 아키텍처와 패키지 설계 전략을 다룹니다. (※ 소스 코드 없이 구조적 설계에 중점을 둡니다.)

## 1. 아키텍처 연동 전략 (Swing ↔ MariaDB)

### 1.1 데스크톱 애플리케이션의 한계와 비동기 통신
Java Swing은 단일 스레드 모델인 **EDT(Event Dispatch Thread)** 를 사용하여 화면을 그립니다. 만약 버튼 클릭 이벤트 리스너 내부에서 직접 DB(MariaDB)와 통신(Network I/O)을 수행하면, DB 응답이 올 때까지 전체 UI가 멈추는(Freezing) 현상이 발생합니다.

- **해결 방안:** 백그라운드 스레드를 통한 비동기 처리
  - Java Swing에서 제공하는 `SwingWorker` 클래스를 사용하거나 별도의 `Thread` / `ExecutorService`를 활용하여 DB 접근(DAO 호출)을 백그라운드에서 처리합니다.
  - DB 통신이 완료되면 `SwingWorker`의 `done()` 메서드를 통해 다시 EDT로 돌아와 JTable 등의 UI 컴포넌트를 안전하게 업데이트합니다.

### 1.2 Docker 내 MariaDB와의 네트워크 연결 통신
Docker 컨테이너 내부에서 실행 중인 MariaDB는 외부(호스트 PC)에서 접근 가능하도록 포트 매핑(`-p 3306:3306`)이 설정되어 있습니다.
- 애플리케이션은 JDBC URL(`jdbc:mariadb://localhost:3306/sportscenter_db`)을 통해 TCP/IP 소켓 통신으로 DB에 접근합니다.
- 네트워크 통신은 언제든 지연되거나 실패(`SQLException`, `ConnectionTimeout`)할 수 있으므로, 예외 발생 시 Swing UI에 적절한 에러 메시지(예: `JOptionPane` 경고창)를 띄우는 글로벌 예외 처리 전략이 필요합니다.

---

## 2. 엄격한 MVC 패키지 구조 설계

전체 소스 코드는 Maven의 표준 디렉토리(`src/main/java`) 내에서 `com.sportscenter`를 베이스 패키지로 하여 다음과 같이 엄격하게 분리됩니다.

### `com.sportscenter.model` (데이터 및 비즈니스 로직)
- **DTO (Data Transfer Object):** DB 테이블(`members`)의 레코드 1줄과 정확히 매핑되는 순수 데이터 컨테이너. 로직 없이 `getter/setter`만 포함.
- **DAO (Data Access Object):** 데이터베이스와의 실질적인 CRUD 통신을 전담하는 클래스. JDBC API(`PreparedStatement`, `ResultSet` 등)는 오직 이곳에서만 사용되며, 밖으로는 `List<DTO>`나 `boolean` 값 등 추상화된 결과만 반환합니다.

### `com.sportscenter.view` (사용자 인터페이스)
- **UI 컴포넌트:** `JFrame`, `JTable`, `JButton` 등 화면에 렌더링되는 모든 요소.
- **단방향 의존성:** View는 오직 Controller를 통해 이벤트를 전달할 뿐, 절대로 `DAO`나 `DBConnectionUtil`에 직접 접근해서는 안 됩니다.

### `com.sportscenter.controller` (중앙 제어)
- **이벤트 핸들링과 흐름 제어:** View에서 발생한 사용자 입력(예: 버튼 클릭)을 가로채서, 올바른 `DAO` 메서드를 호출(Background Thread 권장)합니다.
- **화면 갱신 명령:** 모델로부터 받아온 결과 데이터를 다시 View에 넘겨주어 화면 갱신을 지시합니다. Model과 View 사이의 중재자 역할을 완벽히 수행합니다.

### `com.sportscenter.util` (공통 유틸리티)
- **DB Connection 관리:** JDBC 드라이버 로드 및 물리적 `Connection` 객체 생성을 전담.

---

## 3. 리소스 관리 및 효율성 확보 방안

### 3.1 `try-with-resources`의 구조적 강제성
DB와 연결을 맺는 리소스(`Connection`, `PreparedStatement`, `ResultSet`)는 메모리 누수를 막기 위해 무조건 닫아야 합니다.
- 기존의 `try-catch-finally` 방식은 코드가 길어지고 `close()` 중 예외 처리가 누락되기 쉽습니다.
- Java 7 이상에서 지원하는 **`try-with-resources`** 문법을 `DAO`의 모든 메서드에 일관되게 적용하여, 정상 종료든 예외 발생이든 리소스가 즉각 자동 반환(`AutoCloseable`)되도록 설계합니다.

### 3.2 커넥션 풀링(Connection Pooling) 검토
현재는 대학 과제 수준의 데스크톱 앱이므로 사용자가 1명(단일 클라이언트)입니다. 따라서 매 요청마다 `DriverManager.getConnection()`을 맺는 단순한 방식(Direct Connection)으로도 성능상 큰 문제가 없습니다. 
*(만약 다중 접속 웹 애플리케이션으로 확장된다면 `HikariCP`와 같은 커넥션 풀을 도입하여 성능을 극대화해야 합니다.)*

---

## 결론
UI 스레드 안정성(`SwingWorker`), 역할 분리(`MVC`), 그리고 리소스 안정성(`try-with-resources`) 이 세 가지 축을 기반으로 구현을 진행할 때, 가장 안정적이고 확장성 높은 스포츠센터 회원 관리 프로그램이 완성될 것입니다.
