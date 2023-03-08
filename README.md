# 공매도 탐색기 <img src="https://user-images.githubusercontent.com/64154691/222951507-92692be9-cee1-4c33-8e15-b574745cd15f.png" width="36" height="36" /> 

공매도 탐색기는 한국 주식 종목들에 대한 공매도 현황을 볼 수 있는 안드로이드 앱 입니다. <br />
주식 커뮤니티에서 매일 공매도 현황 데이터를 올리는 것을 보고 '자동화 할 수 있는 방법이 없을까?' 하여 시작한 프로젝트입니다.

[구글 플레이 스토어](https://play.google.com/store/apps/details?id=com.theshortselling&pli=1)

# 아키텍쳐

![architecture](https://user-images.githubusercontent.com/64154691/222965715-94ff0262-f9de-45a8-be48-fd4bc9f47a1f.png)

## 테스트

이 프로젝트에서는 통합 테스트를 위해 ```TestContainer``` 를 사용하고 있습니다.

그래서 ```Docker``` 를 미리 설치해야 모든 테스트가 정상적으로 통과할 수 있습니다.

[Docker 설치](https://docs.docker.com/get-docker/)
```
git clone https://github.com/mjkam/short-selling-server.git
cd short-selling-server
./gradlew test
```
