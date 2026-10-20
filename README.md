# Board Full Stack — Java/Spring 게시판과 API QA

Java 17 / Spring Boot / Spring Security/JWT / JPA / MySQL 기반 개인 게시판 프로젝트입니다.


## QA에서 확인한 내용

- 입력 조건: 필수값, 제목 길이 200/201자, 목록 크기 50/51
- 권한: 작성자/다른 일반 사용자·관리자 역할별 요청
- 데이터: 요청 차단 후 원본 유지, 본인 삭제 후 논리 삭제와 조회 차단
- 결함: 일반 USER의 관리자 API 요청에서 기대 403 대신 500이 반환되는 오류 응답

## 실행 환경

Java 17, MySQL, Gradle Wrapper를 사용합니다. DB 접속 정보와 JWT 설정은 개인 실행 환경에서 구성해야 합니다. 상세 API는 프로젝트의 컨트롤러와 요청 DTO를 기준으로 확인합니다.
