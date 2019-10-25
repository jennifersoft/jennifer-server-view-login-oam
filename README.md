# 프로젝트 디렉토리 구조

1. com/aries/oam/adapter : 실제 고객사의 제니퍼 뷰서버에 설정하는 로그인 어댑터
2. com/aries/oam/proxy : 테스트를 위한 스프링 기반의 프록시 서버 (고객사 프록시 서버로 대체)

# SSO 로그인 어댑터 설정

1. 관리 > 어댑터 및 실험실 > 로그인 탭으로 이동한다.
2. 경로는 dist/oam-sso_adapter-1.0.0.jar 파일을 선택한다.
3. 종류는 SSO, ID는 oam, 클래스는 com.aries.oam.adapter.SSOLoginAdapter로 설정한다.
4. 설정이 완료되면 바로 옵션 버튼을 클릭한다.
5. OAM_SINGLE_PASSWORD 값에 공통 비밀번호를 설정한다.
6. SSO 인증 대상 사용자를 추가해야하며, 앞에서 언급한 공통 비밀번호로 설정해야 한다.
7. 제니퍼 뷰서버를 재시작한다.

# 프록시 서버 설정

1. java -jar dist/oam-sso_proxy-1.0.0.jar 명령어를 입력한다.
2. http://localhost:8080/login?id=아이디 (앞에 6번에서 추가한 계정)
3. 로그인 버튼을 클릭한 후, 제니퍼 뷰서버 DB에 존재하는 계정이라면 인증이 성공한다.
4. 참고로 127.0.0.1로 접근하면 인증이 안됨. SSOLoginAdapter에서 Referer 체크할 때, localhost와 등록된 호스트만 허용하게 구현되어 있음

# 기타 참고사항

1. SSO 담당자와 통화했을 때, 포탈 사이트에서 링크를 클릭하면, 프록시 서버를 통해 SSO 인증을 한다고 구두로 확인하였음.
2. 프록시 서버가 어떤 식으로 구현되어 있는지 알 수가 없기 때문에 테스트를 위해 임의로 개발하였음.
3. 로그인 버튼을 클릭하면 URL 매개변수로 전달받은 값을 프록시 필터로 넘기는데, 여기서 OAM_SINGLE_ID라는 HTTP Request Header 값을 설정함.
4. SSO 인증을 위한 제니퍼 뷰서버 컨트롤러 URL은 http://localhost:7900/login/sso
