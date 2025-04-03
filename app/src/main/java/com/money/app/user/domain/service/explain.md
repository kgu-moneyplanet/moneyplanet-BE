# 용도(개발시 삭제예정)


도메인 서비스
ex) 두개 이상의 엔티티에 대해서 조회하여 검증할경우
   - tx의 id와 user_id가 같은지 확인하는경우


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