# 진행 상황 및 계획 (Process)

## 1. Git 리포지토리 초기화 및 연동 계획 (대기 중)

현재 'Git 리포지토리 설정 및 원격 리포지토리(GitHub 등) 연동 후 Push' 작업이 요청되었습니다. 
사용자 규칙에 따라 아래 작업을 실행하기 전 승인을 요청합니다.

### 실행 예정 작업 내용
1.  **로컬 Git 리포지토리 초기화:**
    *   명령어: `git init`
    *   기본 브랜치명을 `main`으로 설정합니다.
2.  **`.gitignore` 파일 생성:**
    *   Java, Maven, Eclipse/IntelliJ 등 IDE 관련 불필요한 파일이 커밋되지 않도록 기본 `.gitignore` 파일을 프로젝트 루트에 생성합니다.
3.  **파일 스테이징 및 초기 커밋:**
    *   명령어: `git add .`
    *   명령어: `git commit -m "Initial commit: docs 및 기본 구조 추가"`
4.  **원격 리포지토리 생성 및 연동 후 Push:**
    *   프로젝트명과 동일한 `sports-center-management` 리포지토리를 생성합니다.
    *   (방법 A) 환경에 GitHub CLI(`gh`)가 설치되어 있고 로그인이 되어 있다면, `gh repo create sports-center-management --public --source=. --remote=origin --push` 명령어로 자동 생성 및 푸시를 진행합니다.
    *   (방법 B) CLI 사용이 불가능한 경우, 사용자가 GitHub 웹에서 리포지토리를 직접 생성한 후 URL을 제공해주시면 `git remote add origin <URL>` 후 `git push -u origin main`을 실행합니다.

---

**[사용자 확인 필요]**
위 계획대로 Git 초기화 및 커밋, 리포지토리 연동 명령어를 실행해도 될까요? 
(또한, GitHub CLI(`gh`)가 설치되어 있고 로그인된 상태인지 확인 부탁드립니다. 만약 아니시라면 직접 빈 리포지토리를 만드신 후 주소를 알려주시면 연동하겠습니다.)
