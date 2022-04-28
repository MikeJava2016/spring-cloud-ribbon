# 工程简介

1、创建密钥库
keytool -genkey -alias “sunshine” -keyalg “RSA” -keysize 2048 -keystore “sunshine.keystore” -validity 4000 -dname “CN=Huzhanglin,OU=Citicbank,O=Citic,L=WUHAN,ST=HUBEI,C=CN”
keytool -genkey 生成密钥库文件
-alias ； 密钥对别名
-keyalg：密钥算法名称
-keysize：密钥位大小
-keystore：密钥库文件名
-validity：有效天数
-dname：唯一判别名
 keytool -importkeystore -srckeystore sunshine.keystore -destkeystore ./sunshine.p12 -deststoretype pkcs12

2、导出公钥文件
keytool -export -alias “sunshine” -file “sunshine.cer” -keystore “sunshine.keystore”
转换二进制der公钥证书转换为pem格式
openssl x509 -inform der -in “sunshine.cer” -outform pem “sunshinepem.cer”
只能通过openssl命令将二进制公钥证书转换为pem（base64编码）证书

3、导出私钥文件
keytool -v -importkeystore -srckeystore “sunshine.keystore” -srcalias “sunshine” -destkeystore “sunshine.pfx” -deststoretype PKCS12

# 延伸阅读

