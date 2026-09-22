# 실행 가이드

## 1. `.env` 파일 설정
스프링 부트 애플리케이션 등에서 사용할 DB 접속 정보를 `.env` 파일에 정의합니다.

```env
DB_URL=jdbc:mysql://localhost:<로컬 포트>/<데이터베이스명>
DB_USERNAME=<사용자 이름>
DB_PASSWORD=<비밀번호>
```

## 2. Docker 컨테이너 실행

### 컨테이너 실행 명령어
```bash
docker run --name mysql-minilog \          # 컨테이너 이름 설정 및 실행
  -e MYSQL_ROOT_PASSWORD=<루트 비밀번호> \   # 환경변수 설정
  -e MYSQL_DATABASE=<데이터베이스 이름> \    
  -e MYSQL_USER=<사용자 이름> \             
  -e MYSQL_PASSWORD=<사용자 비밀번호> \      
  -p <로컬 설정 포트>:3306 \                 # 포트 포워딩 설정 (호스트 포트 : 컨테이너 포트)
  -v mysql-minilog-data:/var/lib/mysql \   # 볼륨 바인딩 설정 (도커 볼륨 : 컨테이너 데이터 경로)
  -d mysql:8.0 \                           # 백그라운드 실행 및 이미지 버전 지정
  --default-time-zone='+9:00'              # 기본값이 UTC이므로 KST에 맞춰 UTC+9시간 설정
```