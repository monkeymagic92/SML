# SML
- 공부용
   
## RefreshableSqlMapClientFactoryBean
- *mapper.xml 파일 경로를 이동하면 refreshableSqlSessionFactoryBean이 작동안함
- 추측 : refreshableSqlSessionFactoryBean이 상속받는 SqlSessionFactoryBean에 private Resource[] mapperLocations; -> private Source[] mapperLocations; 로 변경후
- 모든 mapperLocations 타입을 Source[] 로 변경해서 해보기
- 현재 SqlSEssionFactoryBean 파일은 file is read only라서 코드수정이 안됨, file is read only 해제방법 알아보기
