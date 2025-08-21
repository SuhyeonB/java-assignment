# java-assignment
바로인턴 - 백엔드 개발 과제


인메모리 저장소 기반의 회원/인증 및 관리자 권한 부여 API입니다.
JWT 액세스 토큰(2시간 유효)만 사용하며, Refresh 토큰은 사용하지 않습니다.

----

기술 스택
- Java 17, Spring Boot 3.5.x
- Spring Web, Spring Security
- Validation (Jakarta), Lombok
- springdoc-openapi (Swagger UI)
- 테스트: JUnit 5

에러 응답 포맷
- USER_ALREADY_EXISTS (409)
- INVALID_CREDENTIALS (401)
- NOT_FOUND_USER (404)
- ACCESS_DENIED (403)
- INVALID_TOKEN (401)
- INVALID_ROLE (400/422)
- 기타 서버 오류는 SERVER ERROR (500)

## API 명세서

### 회원가입 / 로그인 / 권한 부여

| Method | Endpoint                  | 권한   | 설명                        | 요청 Body 예시                                                                 | 성공 응답 (예시)                                                                                       | 실패 응답 (예시)                                                                 |
|--------|---------------------------|--------|-----------------------------|--------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------|
| POST   | `/signup`                 | Public | 회원가입 (role 없으면 USER, `"ADMIN"`이면 관리자 가입) | ```json { "username": "abc", "password": "pw", "nickname": "Nick" }```          | **200 OK**<br>헤더: `Authorization: <JWT>`<br>바디: ```json { "username":"abc","nickname":"Nick","roles":[{"role":"USER"}] }``` | **409 Conflict**<br>```json { "error": { "code":"USER_ALREADY_EXISTS", "message":"이미 존재하는 사용자입니다." } }``` |
| POST   | `/login`                  | Public | 로그인, JWT 발급            | ```json { "username": "abc", "password": "pw" }```                              | **200 OK**<br>헤더: `Authorization: <JWT>`<br>바디: ```json { "token":"eyJhbGciOi..." }```                | **401 Unauthorized**<br>```json { "error": { "code":"INVALID_CREDENTIALS", "message":"아이디 또는 비밀번호가 올바르지 않습니다." } }``` |
| PATCH  | `/admin/users/{userId}/roles` | ADMIN | 특정 사용자에게 ADMIN 권한 부여 (멱등) | PathVariable: `userId`                                                          | **200 OK**<br>```json { "username":"target","nickname":"Nick","roles":[{"role":"ADMIN"}] }```             | **403 Forbidden** (권한 없음)<br>**404 Not Found** (사용자 없음)                                       |

---

### 에러 응답 포맷 (공통)

```json
{
  "error": {
    "code": "ERROR_CODE",
    "message": "메시지"
  }
}
```

----

## Deployment
- 배포 환경: AWS EC2 (Ubuntu 22.04, t2.micro)
- JDK: Amazon Corretto 17
- 빌드 툴: Gradle
- 실행: `nohup java -jar assignment-0.0.1-SNAPSHOT.jar --server.port=8080 &`

### URL
- http://52.23.235.123:8080/
