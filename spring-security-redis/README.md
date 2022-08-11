# Spring Security Redis Session

## Redis 실행
```
# docker run --name ${redisName} -d -p 6379:6379 redis
```

## Redis Cli 접속
```
# docker run -it ${redisName} redis-cli
```   

## (error) NOAUTH Authentication required. 에러 발생시
```
# AUTH ${password}
# OK
```
