# 工程简介

实现多多方式登录
https://gitee.com/it_freshman/multi-channel-login/blob/master/src/main/java/cn/jhs/config/security/SecurityConfiguration.java

实现多多方式登录  
SpringSecurity+JWT实现微信用户名密码等多种登录方式
https://www.jianshu.com/p/71f43c11a328

# 延伸阅读
系统学习 spring security https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&__biz=MzI1NDY0MTkzNQ==&scene=24&album_id=1818393942174810113&count=3#wechat_redirect
系统学习
https://mp.weixin.qq.com/s?__biz=MzI1NDY0MTkzNQ==&mid=2247493000&idx=2&sn=3d2862565e0f22968f1685199c6bdb87&chksm=e9c0b7e8deb73efe02bbab4ac6ddbefe88df60190aa49da063a161dc9fd5a0929ca130005469&scene=178&cur_album_id=1818393942174810113#rd


get http://localhost:8081/smscode?mobile=15972740935  发送短信

post http://localhost:8081/sms/login 登录接口
{"mobile":"15972740935","smsCode":"709231"}


get http://localhost:8081/hello  业务接口  需要token

过滤器链
[
org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@68229a6, 
org.springframework.security.web.context.SecurityContextPersistenceFilter@36cf6377, 
org.springframework.security.web.header.HeaderWriterFilter@3051e476,
org.springframework.security.web.csrf.CsrfFilter@76105ac0,
org.springframework.security.web.authentication.logout.LogoutFilter@574f9e36,
org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter@4e26040f, 
org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter@21ba0d33,
org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter@62615be,
org.springframework.security.web.authentication.www.BasicAuthenticationFilter@5b0835cb, 
org.springframework.security.web.savedrequest.RequestCacheAwareFilter@3151277f,
org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@3b1137b0,
org.springframework.security.web.authentication.AnonymousAuthenticationFilter@477bea57,
org.springframework.security.web.session.SessionManagementFilter@6d9ee75a, 
org.springframework.security.web.access.ExceptionTranslationFilter@5ae16aa, 
org.springframework.security.web.access.intercept.FilterSecurityInterceptor@3d7caf9c
]