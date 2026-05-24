## 当前项目模块结构
后续编码任务请按此结构进行包创建
```bash
E:.
└───src.mainjava.com
            └───everypicfound
                ├───common
                │   ├───cache
                │   ├───config
                │   ├───constant
                │   ├───context
                │   ├───enums
                │   ├───event
                │   ├───exception
                │   ├───executor
                │   ├───filter
                │   ├───log
                │   ├───metric
                │   ├───ratelimit
                │   ├───response
                │   └───util
                ├───imageasset
                │   ├───application
                │   │   ├───command
                │   │   ├───dto
                │   │   ├───result
                │   │   └───service
                │   ├───domain
                │   │   ├───checker
                │   │   ├───duplicate
                │   │   ├───enums
                │   │   ├───extractor
                │   │   ├───generator
                │   │   ├───repository
                │   │   ├───service
                │   │   └───validator
                │   ├───error
                │   ├───infrastructure
                │   │   ├───converter
                │   │   ├───mapper
                │   │   ├───po
                │   │   └───repository
                │   └───interfaces
                │       ├───controller
                │       ├───request
                │       └───response
                ├───modelclient
                │   ├───api
                │   ├───domain
                │   │   ├───enums
                │   │   └───validator
                │   ├───error
                │   └───infrastructure
                │       ├───config
                │       ├───health
                │       └───http
                ├───search
                │   ├───application
                │   │   ├───command
                │   │   ├───context
                │   │   ├───pipeline
                │   │   └───service
                │   ├───config
                │   ├───domain
                │   │   ├───assembler
                │   │   ├───collection
                │   │   ├───embedding
                │   │   ├───filter
                │   │   ├───overfetch
                │   │   ├───rerank
                │   │   └───validator
                │   ├───error
                │   └───interfaces
                │       ├───controller
                │       ├───request
                │       └───response
                ├───storage
                │   ├───api
                │   ├───core
                │   ├───error
                │   └───infrastructure
                │       ├───config
                │       ├───health
                │       └───local
                ├───vectorindex
                │   ├───api
                │   ├───collection
                │   ├───domain
                │   │   └───enums
                │   ├───error
                │   └───infrastructure
                │       ├───client
                │       ├───config
                │       └───health
                └───vectorization
                    ├───api
                    ├───application
                    │   ├───processor
                    │   ├───publisher
                    │   ├───scanner
                    │   └───vectorizer
                    ├───config
                    ├───domain
                    │   ├───failure
                    │   ├───fusion
                    │   ├───model
                    │   └───retry
                    ├───error
                    └───infrastructure
                        └───publisher
```
