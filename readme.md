안녕하세요. 

과제 제출드립니다.

## 작업 환경
```
kotlin: 1.9.25
boot: 3.4.0
node: 23.3.0
npm: 9.7.1
gradle: 8.11.1
h2 DB
```
## 코드 빌드 및 실행방법
1. cd {projectDir}
    - 프로젝트 디렉토리로 이동합니다.
2. ./gradlew bootRun
    - task 중 `frontendTask`에서 프론트엔드 리소스 생성합니다.
    - 서버가 기동됩니다. (8080번 포트)
    - 자동으로 A~I 브랜드와 상품을 등록합니다.
3. cd {projectDir}/src/main/resource/static
    - 프론트 디렉토리로 이동합니다.
4. npm run dev
    - 프론트 노드서버를 기동합니다.(3000번 포트)
5. localhost:3000에 접속하여 과제를 확인합니다.

## 구현범위
- 과제 4종 요구사항 주신 내용 반영했습니다. 
  - 카테고리별 최저가 브랜드 조회
  - 모든 카테고리를 동일 브랜드로 구매했을떄 가장 저렴한 브랜드 조회
  - 카테고리 특정하여 최저가와 최고가 조회
  - 상품과 브랜드 CRUD
    - 아래 과제 API를 호출해주시거나, 프론트 서버 기동 후 브라우저로 접속해주시면 확인가능합니다.
- 상품/브랜드 등록 어드민(vue)
  - 각각 생성/수정/삭제 기능

## 프로젝트 구조
```
api: http 처리하는 컨트롤러와 REQ/RES DTO 모델 클래스가 위치합니다.
ㄴ controller

application
ㄴ service: usecase를 처리하기 위해 컴포넌트를 호출하는 서비스가 위치합니다.
ㄴ component: service 요청을 처리하기 위한 상세 구현이 들어가도록 구현했습니다.
ㄴ domain: 내부에서 다루는 도메인 모델입니다. persistenct.entity와 동일할 수 있고, entity에 없는, 도메인에서만 사용할 목적의 모델도 있습니다. 

persistence
ㄴ entity: jpa 엔티티로 db에 저장하거나, 조회하는 용도로 사용했습니다.
ㄴ repository: jpa repository

outbound
ㄴ redis: 캐싱을 구현하기 위해 map을 갖는 컴포넌트를 위치시켰습니다.

```

## 과제 API curl
```
과제1)
curl --location 'http://localhost:8080/api/v1/home-work-1'
과제2)
curl --location 'http://localhost:8080/api/v1/home-work-2'
과제3)
curl --location 'http://localhost:8080/api/v1/home-work-3?category=상의'
```

## 상품&브랜드 등록방법
- 프론트 기동 후 브라우저에서 localhost:3000 페이지로 이동합니다. 
- 좌측 메뉴의 브랜드와 상품 페이지에서 등록가능합니다.
  - 상품 등록을 하려면 브랜드가 먼저 등록되어야 합니다.

## 구현방식
 
- 카테고리 별 최저가/최고가 정보를 디비에서 가져오지않고 캐싱해둔 값을 사용해서 응답하도록 구현 해봤습니다.
- 만약 상품 등록/변경/삭제 시 위 캐시들을 갱신합니다.
  - 최저가 혹은 최고가인 상품의 가격이 변경되거나, 카테고리가 변경되는 경우 캐시를 갱신합니다.
  - 갱신은 상품의 생성/변경/삭제 api에서 동기로 처리하고 있습니다.
    - 구현 상 갱신 도중 다른 상품이 변경된다면 데이터 유실이 발생할수있습니다. 
- 캐시에는 `fakeRedisStorage`라는 가짜 구현체(map)를 사용했습니다.

## 부족한점
- 최저가가 동일하더라도 한가지 상품만 최저가로 보이도록 구현되어있습니다.