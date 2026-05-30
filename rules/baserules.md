---
trigger: always_on
---

# 프로젝트 기본 규칙

1. 기술 스택: Java 25 (Swing UI), MariaDB (Docker), JDBC
2. 아키텍처: 반드시 MVC(Model-View-Controller) 패턴과 DAO, DTO 패턴을 엄격하게 지킬 것.
3. 데이터베이스: Connection, PreparedStatement, ResultSet 등 DB 리소스는 반드시 `try-with-resources`로 안전하게 닫을 것.
4. 모든 코드를 작성하기 전, 반드시 계획된 .md 문서(plan.md, research.md 등)를 기반으로 작업할 것.
5. 변수/메서드는 카멜케이스(camelCase), 클래스는 파스칼케이스(PascalCase), DB 테이블은 복수형/스네이크케이스(snake_case) 등 표준 네이밍 관례를 따를 것.
