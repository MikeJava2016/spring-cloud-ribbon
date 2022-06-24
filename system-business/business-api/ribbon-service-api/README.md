# 工程简介
@FeignClient:
1.不支持@GetMapping这样的混合注解只能使用@RequestMapping(value="/simple/{id}",method=RequestMethod.GET)这种方式来实现混合注解。

2.对于url路径上的PathVariable属性必须要有默认值，否者会报PathVariable annotation was empty on param 0.


# 延伸阅读

