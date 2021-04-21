# 配置文件外置
当application.yml的 mock.readFromResource = true ,则读取资源文件中的mock.json。  
当application.yml的 mock.readFromResource = false,则读取 mock.filepath的路径文件，路径可以为相对路径或绝对路径。  
# mock.json 配置说明
 ```json
[{
    "url": "/api/abc",//要处理的请求路径
    "method": "get", //要处理的请求类型
    "ignoreCase": false,//url是否忽略大小写，默认为true。
    "code": 200, //返回状态码
    "contentType": "application/json;charset=UTF-8", //返回的内容类型
    "rule": "name=李四", //条件(为空则只匹配url)，格式：[参数][匹配符号][值]，匹配符号可以是=（全匹配）、 *=（模糊匹配）、 !=(不匹配)，多个条件用& 分割，不支持or查询
    "result": "我是李四" //返回的内容，如果状态码为302，则为重定向路径,支持参数替换，参数格式：${请求参数名}
  }, 
  {
    "url": "/api/abc",
    "method": "get",
    "code": 200,
    "contentType": "application/json;charset=UTF-8",
    "result": "我是${name}"
  },
 {
    "url": "/api/def",
    "ignoreCase": false,
    "method": "get",
    "code": 302,
    "rule": "uuid=123",
    "result": "${redirectUrl}"
  },
  {
       "url": "/phone",
       "ignoreCase": false,
       "method": "get",
       "code": 200,
       "rule": "",
       "contentType":"text/html;charset=UTF-8",//返回网页
       "result": "",
       "filePath":"C:\\phone.html", //外部文件路径，优先级高于result，固定以utf-8编码读取文件文本内容
       "desc": "支持外部文件"
     }
]
``` 