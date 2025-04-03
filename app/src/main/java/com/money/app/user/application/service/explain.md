# 용도(개발시 삭제예정)


command, query(생성, 조회) 전에 오류 검사용
ex) User 생성시 중복된 이메일로 가입했는지 체크하는 등


# 동작구조


[1] Controller (UserController)
      ↓
[2] application/command (CreateUserCommand)
      ↓
[3] domain/model (User 생성)
      ↓
[4] domain/repository (UserRepository 인터페이스)
      ↓
[5] infrastructure/persistence (JpaUserRepository 구현체)
      ↓
[6] DB에 저장 → 결과 반환 → Controller → 응답


# 예시

[사용자 HTTP 요청]
    ↓
[Controller] UserController
    ↓
[Command] CreateUserCommand
    ↓
[Entity] User 생성
    ↓
[Repository] UserRepository (interface)
    ↓
[JPA 구현체] JpaUserRepository
    ↓
[DB] insert into user ...
    ↓
[응답 DTO로 변환 → 사용자에게 반환]